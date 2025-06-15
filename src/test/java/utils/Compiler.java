package utils;

import static proguard.classfile.JavaConstants.JAVA_FILE_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Compiler {

    /**
     * Compiles the source files in the given directory to class files.
     *
     * @param sourcePackage Path to the directory containing the source files.
     * @return Path a temporary directory containing the compiled class files.
     */
    public static File compileSourceProject(Path sourcePackage) {
        try {
            File compile = Files.createTempDirectory("compiled").toFile();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

            Set<File> javaFiles = new HashSet<>();
            Files.walkFileTree(
                    sourcePackage,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                            if (path.toAbsolutePath().toString().endsWith(JAVA_FILE_EXTENSION)) {
                                javaFiles.add(path.toFile());
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });

            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromFiles(javaFiles);
            List<String> options = Arrays.asList("-d", compile.getPath());
            compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();

            fileManager.close();

            return compile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** For testing purposes only. */
    public static void main(String... args) {
        compileSourceProject(Paths.get(args[0]));
    }
}
