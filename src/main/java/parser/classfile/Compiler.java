package parser.classfile;

import static proguard.classfile.JavaConstants.JAVA_FILE_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Compiler {

    public static void compileSourceProject(Path sourcePackage) {
        try (Stream<Path> pathStream = Files.walk(sourcePackage)) {
            File compile = Files.createTempDirectory("compiled").toFile();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

            List<File> javaFiles =
                    pathStream
                            .filter(it -> it.endsWith(JAVA_FILE_EXTENSION))
                            .map(Path::toFile)
                            .collect(Collectors.toCollection(ArrayList::new));

            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromFiles(javaFiles);
            List<String> options = Arrays.asList("-d", compile.getPath());
            compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();

            fileManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** For testing purposes only. */
    public static void main(String... args) {
        compileSourceProject(Paths.get(args[0]));
    }
}
