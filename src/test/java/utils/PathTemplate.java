package utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTemplate {
    public enum LatexEditor {
        SRC(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src", "test", "resources", "LatexEditor", "src")))),

        COMMANDS(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands"))),

        STRATEGIES(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "strategies"))),

        CONTROLLER(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src", "test", "resources", "LatexEditor", "src", "controller"))),

        MODEL(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src", "test", "resources", "LatexEditor", "src", "model"))),

        VIEW(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src", "test", "resources", "LatexEditor", "src", "view"))),

        CHOOSE_TEMPLATE(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "view",
                                "ChooseTemplate.java"))),

        LATEX_EDITOR_VIEW(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "view",
                                "LatexEditorView.java"))),
        MAIN_WINDOW(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "view",
                                "MainWindow.java"))),

        OPENING_WINDOW(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "view",
                                "OpeningWindow.java"))),

        LATEX_EDITOR_CONTROLLER(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "LatexEditorController.java"))),

        STABLE_VERSIONS_STRATEGY(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "strategies",
                                "StableVersionsStrategy.java"))),

        VERSIONS_STRATEGY(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "strategies",
                                "VersionsStrategy.java"))),

        VOLATILE_VERSIONS_STRATEGY(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "strategies",
                                "VolatileVersionsStrategy.java"))),

        VERSIONS_STRATEGY_FACTORY(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "strategies",
                                "VersionsStrategyFactory.java"))),

        DOCUMENT(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "Document.java"))),

        DOCUMENT_MANAGER(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "DocumentManager.java"))),

        VERSIONS_MANAGER(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "model",
                                "VersionsManager.java"))),

        ADD_LATEX_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "AddLatexCommand.java"))),

        COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "Command.java"))),

        COMMAND_FACTORY(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "CommandFactory.java"))),

        CREATE_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "CreateCommand.java"))),

        EDIT_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "EditCommand.java"))),

        LOAD_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "LoadCommand.java"))),
        SAVE_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "SaveCommand.java"))),

        CHANGE_VERSIONS_STRATEGY_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "ChangeVersionsStrategyCommand.java"))),

        DISABLE_VERSIONS_MANAGER_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "DisableVersionsManagementCommand.java"))),

        ENABLE_VERSIONS_MANAGE_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "EnableVersionsManagementCommand.java"))),

        ROLLBACK_TO_PREVIOUS_VERSION_COMMAND(
                Paths.get(
                        PathConstructor.getCurrentPath().normalize().toString(),
                        PathConstructor.constructPath(
                                "src",
                                "test",
                                "resources",
                                "LatexEditor",
                                "src",
                                "controller",
                                "commands",
                                "RollbackToPreviousVersionCommand.java")));

        public final Path path;

        LatexEditor(Path path) {
            this.path = path;
        }
    }

    public enum ParserTesting {
        SRC(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src", "test", "resources", "ParserTesting"))));

        public final Path path;

        ParserTesting(Path path) {
            this.path = path;
        }
    }

    public enum BookstoreAdvanced {
        SRC_ROOT(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src", "test", "resources", "BookstoreAdvancedV01")))),
        SRC(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src")))),
        BOOKSTORE(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        "bookstore")))),
        GUI(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        "gui")))),
        DOT_SETTINGS(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        ".settings")))),
        BIN(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        "bin")))),
        LIB(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        "lib")))),
        BIN_GUI(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        "bin",
                                        "gui")))),
        BIN_BOOKSTORE(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src",
                                        "test",
                                        "resources",
                                        "BookstoreAdvancedV01",
                                        "src",
                                        "bin",
                                        "bookstore"))));

        public final Path path;

        BookstoreAdvanced(Path path) {
            this.path = path;
        }
    }
}
