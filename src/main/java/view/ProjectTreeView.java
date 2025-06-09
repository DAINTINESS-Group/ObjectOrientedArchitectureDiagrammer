package view;

import static proguard.classfile.ClassConstants.CLASS_FILE_EXTENSION;
import static proguard.classfile.JavaConstants.JAVA_FILE_EXTENSION;
import static view.FileType.PACKAGE;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import proguard.io.ClassPathEntry;

public class ProjectTreeView {
    @FXML TreeView<String> treeView;

    private final Path sourceFolderPath;

    private final List<String> folderFiles = new ArrayList<>();
    private final List<String> files = new ArrayList<>();
    private final TreeViewResizer resizer = new TreeViewResizer();
    private CheckBoxTreeItem<String> rootItem;

    private ObservableSet<CheckBoxTreeItem<?>> checkedItems;

    public ProjectTreeView(TreeView<String> treeView, Path sourceFolderPath) {
        this.treeView = treeView;
        this.sourceFolderPath = sourceFolderPath;
    }

    public void createTreeView() {
        rootItem = new CheckBoxTreeItem<>(sourceFolderPath.getFileName().toString());
        treeView.setShowRoot(true);
        treeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        resizer.makeResizable(treeView);
        try (DirectoryStream<Path> folderStream = Files.newDirectoryStream(sourceFolderPath)) {
            for (Path path : folderStream) {
                createTree(path, rootItem);
            }
            treeView.setRoot(rootItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTree(Path path, CheckBoxTreeItem<String> parent) {
        if (Files.isDirectory(path)) {
            String relativePath = sourceFolderPath.relativize(path).toString();
            folderFiles.add(relativePath);
            CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(relativePath);

            parent.getChildren().add(treeItem);

            try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(path)) {
                for (Path subPath : filesStream) {
                    createTree(subPath, treeItem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (isSupported(path)) {
            String string = path.toAbsolutePath().toString();
            String name =
                    string.endsWith(JAVA_FILE_EXTENSION)
                            ? path.getFileName().toString()
                            : subtractFileExtension(sourceFolderPath.relativize(path).toString());
            parent.getChildren().add(new CheckBoxTreeItem<>(name));
            files.add(name);
        }
    }

    private static boolean isSupported(Path path) {
        String string = path.toAbsolutePath().toString();
        return string.endsWith(JAVA_FILE_EXTENSION)
                || string.endsWith(CLASS_FILE_EXTENSION)
                || isEntrySupported(path);
    }

    private static boolean isEntrySupported(Path path) {
        ClassPathEntry entry = new ClassPathEntry(path.toFile(), false);
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

    public List<String> getSelectedFiles(FileType fileType) {
        List<String> files = fileType.equals(PACKAGE) ? folderFiles : this.files;

        List<String> selectedFiles = new ArrayList<>();
        for (CheckBoxTreeItem<?> item : checkedItems) {
            String name = (String) item.getValue();
            if (!files.contains(name)) continue;

            if (name.endsWith(JAVA_FILE_EXTENSION)) {
                name = subtractFileExtension(name);
            }

            selectedFiles.add(name);
        }

        return selectedFiles;
    }

    public void setCheckedItems(CheckBoxTreeItem<?> rootItem) {
        checkedItems = FXCollections.observableSet();
        findCheckedItems(rootItem);
    }

    private void findCheckedItems(CheckBoxTreeItem<?> item) {
        if (item.isSelected()) {
            checkedItems.add(item);
        }
        for (TreeItem<?> child : item.getChildren()) {
            findCheckedItems((CheckBoxTreeItem<?>) child);
        }
    }

    private static String subtractFileExtension(String s) {
        return s.substring(0, s.lastIndexOf("."));
    }

    public CheckBoxTreeItem<String> getRootItem() {
        return rootItem;
    }

    public Path getSourceFolderPath() {
        return sourceFolderPath;
    }
}
