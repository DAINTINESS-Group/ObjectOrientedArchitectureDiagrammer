package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectTreeView {

    @FXML
    TreeView<String> treeView;

    private CheckBoxTreeItem<String> rootItem;
    private ObservableSet<CheckBoxTreeItem<?>> checkedItems;
    private final List<String> folderFiles;
    private final List<String> javaSourceFiles;
    private final Path sourceFolderPath;
    private final TreeViewResizer resizer;

    public ProjectTreeView(TreeView<String> treeView, Path sourceFolderPath) {
        this.sourceFolderPath = sourceFolderPath;
        this.treeView = treeView;
        folderFiles = new ArrayList<>();
        javaSourceFiles = new ArrayList<>();
        resizer = new TreeViewResizer();
    }

    public void createTreeView(){
        rootItem = new CheckBoxTreeItem<>(sourceFolderPath.normalize().toString()
                .substring(sourceFolderPath.normalize().toString().lastIndexOf("\\") + 1));
        treeView.setShowRoot(true);
        treeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        resizer.makeResizable(treeView);
        try (DirectoryStream<Path> folderStream = Files.newDirectoryStream(sourceFolderPath)) {
            for (Path path: folderStream) {
                createTree(path, rootItem);
            }
            treeView.setRoot(rootItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTree(Path path, CheckBoxTreeItem<String> parent) {
        if (Files.isDirectory(path)) {
            folderFiles.add(getRelativePath(path));
            CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(getRelativePath(path));
            parent.getChildren().add(treeItem);

            try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(path)) {
                for (Path subPath: filesStream) {
                    createTree(subPath, treeItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isFileExtensionJava(path.normalize().toString())) {
            parent.getChildren().add(new CheckBoxTreeItem<>(path.getFileName().toString()));
            javaSourceFiles.add(path.getFileName().toString());
        }
    }

    private String getRelativePath(Path path) {
        return path.normalize().toString().replace(sourceFolderPath.normalize().toString().substring(0,
                sourceFolderPath.normalize().toString().lastIndexOf("\\") + 1), "").replace("\\", ".");
    }

    public List<String> getSelectedFiles(List<String> files, String fileType) {
        List<String> selectedFiles = new ArrayList<>();
        for (CheckBoxTreeItem<?> c: checkedItems) {
            if (!files.contains((String) c.getValue())) {
                continue;
            }
            if (fileType.equals("java")) {
                selectedFiles.add(subtractFileExtension((String) c.getValue()));
            }else {
                selectedFiles.add(((String) c.getValue()));
            }
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

    private boolean isFileExtensionJava(String c) {
        return c.toLowerCase().endsWith(".java");
    }

    private String subtractFileExtension(String s) {
        return s.substring(0, s.lastIndexOf("."));
    }

    public List<String> getFolderFiles() {
        return folderFiles;
    }

    public List<String> getJavaSourceFiles() {
        return javaSourceFiles;
    }

    public CheckBoxTreeItem<String> getRootItem() {
        return rootItem;
    }

    public Path getSourceFolderPath() {
        return sourceFolderPath;
    }
}
