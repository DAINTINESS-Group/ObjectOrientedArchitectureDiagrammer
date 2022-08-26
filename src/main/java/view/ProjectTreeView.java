package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectTreeView {

    @FXML
    TreeView treeView;

    private CheckBoxTreeItem<String> rootItem;
    private ObservableSet<CheckBoxTreeItem<?>> checkedItems;

    private List<String> folderFiles;
    private List<String> javaSourceFiles;
    private final String sourceFolderPath;

    public ProjectTreeView(TreeView treeView, String sourceFolderPath) {
        this.sourceFolderPath = sourceFolderPath;
        this.treeView = treeView;
    }

    public void createTreeView(){
        folderFiles = new ArrayList<>();
        javaSourceFiles = new ArrayList<>();
        rootItem = new CheckBoxTreeItem<>(sourceFolderPath
                .substring(sourceFolderPath.lastIndexOf("\\") + 1));
        treeView.setShowRoot(true);
        treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        File sourceFolder = new File(sourceFolderPath);
        try {
            for (File file : Objects.requireNonNull(sourceFolder.listFiles())) {
                createTree(file, rootItem);
            }
            treeView.setRoot(rootItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTree(File file, CheckBoxTreeItem<String> parent) {
        if (file.isDirectory()) {
            folderFiles.add(getRelativePath(file));
            CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(getRelativePath(file));
            parent.getChildren().add(treeItem);
            for (File f : Objects.requireNonNull(file.listFiles())) {
                createTree(f, treeItem);
            }
        }else if (isFileExtensionJava(file.getPath())) {
            parent.getChildren().add(new CheckBoxTreeItem<>(file.getName()));
            javaSourceFiles.add(file.getName());
        }
    }

    private String getRelativePath(File file) {
        return file.getPath().replace(sourceFolderPath.substring(0, sourceFolderPath.lastIndexOf("\\") + 1), "").replace("\\", ".");
    }

    public List<String> getSelectedFiles(List<String> files, String fileType) {
        List<String> selectedFiles = new ArrayList<>();
        for (CheckBoxTreeItem<?> c: checkedItems) {
            if (files.contains((String) c.getValue())) {
                if (fileType.equals("java")) {
                    selectedFiles.add(subtractFileExtension((String) c.getValue()));
                }else{
                    selectedFiles.add(getFullPath(c));
                }
            }
        }
        return selectedFiles;
    }

    private String getFullPath(CheckBoxTreeItem<?> c) {
        return sourceFolderPath.substring(0, sourceFolderPath.lastIndexOf("\\") + 1) + ((String) c.getValue()).replace(".", "\\");
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

    public String getSourceFolderPath() {
        return sourceFolderPath;
    }
}
