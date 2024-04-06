package manager;

import static proguard.classfile.ClassConstants.CLASS_FILE_EXTENSION;
import static proguard.classfile.JavaConstants.JAVA_FILE_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import parser.ast.ASTInterpreter;
import parser.classfile.ClassFileInterpreter;
import proguard.io.ClassPathEntry;

public class Project {
    private final Path path;

    private InterpreterType interpreterType;

    public Project(Path path) {
        this.path = path;
    }

    /** Initializes the type of the interpreter based on the type of the input. */
    public void initialize() {
        File file = path.toFile();
        // Are we parsing from a directory?
        if (file.isDirectory()) {
            try {
                Files.walkFileTree(
                        path,
                        new SimpleFileVisitor<>() {
                            @Override
                            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                                // We don't support a mix of class and source files, so
                                // exit as soon as we find either one or the other.
                                if (path.toAbsolutePath()
                                        .toString()
                                        .endsWith(JAVA_FILE_EXTENSION)) {
                                    interpreterType = InterpreterType.SOURCE_FILE;
                                    return FileVisitResult.TERMINATE;
                                } else if (path.toAbsolutePath()
                                        .toString()
                                        .endsWith(CLASS_FILE_EXTENSION)) {
                                    interpreterType = InterpreterType.CLASS_FILE;
                                    return FileVisitResult.TERMINATE;
                                }

                                return FileVisitResult.CONTINUE;
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            // If this is not a directory, then the entry must be one of the supported types.
            if (!isEntrySupported(new ClassPathEntry(file, false)))
                throw new IllegalStateException();

            interpreterType = InterpreterType.CLASS_FILE;
        }
    }

    public Collection<ClassifierVertex> createClassGraph(ClassDiagram classDiagram) {
        // Can happen if the given directory does not have any source
        // or class files.
        if (interpreterType == null) throw new IllegalStateException();

        return switch (interpreterType) {
            case CLASS_FILE -> {
                ClassFileInterpreter interpreter = new ClassFileInterpreter();
                interpreter.parseProject(path);

                interpreter.convertToGraph();
                Collection<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();
                classDiagram.setSinkVertices(sinkVertices);

                yield sinkVertices;
            }
            case SOURCE_FILE -> {
                ASTInterpreter interpreter = new ASTInterpreter();
                interpreter.parseProject(path);

                interpreter.convertToGraph();
                Collection<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();
                classDiagram.setSinkVertices(sinkVertices);

                yield sinkVertices;
            }
        };
    }

    public Collection<PackageVertex> createPackageGraph(PackageDiagram packageDiagram) {
        return switch (interpreterType) {
            case CLASS_FILE -> {
                ClassFileInterpreter interpreter = new ClassFileInterpreter();
                interpreter.parseProject(path);

                interpreter.convertToGraph();
                Collection<PackageVertex> vertices = interpreter.getVertices();
                packageDiagram.setVertices(vertices);

                yield vertices;
            }
            case SOURCE_FILE -> {
                ASTInterpreter interpreter = new ASTInterpreter();
                interpreter.parseProject(path);

                interpreter.convertToGraph();
                Collection<PackageVertex> vertices = interpreter.getVertices();
                packageDiagram.setVertices(vertices);

                yield vertices;
            }
        };
    }

    enum InterpreterType {
        CLASS_FILE,
        SOURCE_FILE
    }

    private static boolean isEntrySupported(ClassPathEntry entry) {
        return entry.isJar()
                || entry.isAar()
                || entry.isApk()
                || entry.isDex()
                || entry.isZip()
                || entry.isAab()
                || entry.isEar()
                || entry.isJmod()
                || entry.isWar();
    }
}
