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
    private final ObservableSet<CheckBoxTreeItem<?>> checkedItems = FXCollections.observableSet();

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
        rootItem = new CheckBoxTreeItem<>(sourceFolderPath);
        treeView.setShowRoot(false);
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
            folderFiles.add(file.getName());
            CheckBoxTreeItem<String> treeItem = new CheckBoxTreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f : Objects.requireNonNull(file.listFiles())) {
                createTree(f, treeItem);
            }
        } else if (isFileExtensionJava(file.getPath())) {
            parent.getChildren().add(new CheckBoxTreeItem<>(file.getName()));
            javaSourceFiles.add(file.getName());
        }
    }

    public List<String> getSelectedFiles(List<String> projectsFiles) {
        List<String> selectedFiles = new ArrayList<>();
        for (CheckBoxTreeItem<?> c: checkedItems) {
            if (projectsFiles.contains((String) c.getValue())) {
                if (isFileExtensionJava(((String) c.getValue()))) {
                    selectedFiles.add(subtractFileExtension((String) c.getValue()));
                }else {
                    selectedFiles.add((String) c.getValue());
                }
            }
        }
        return selectedFiles;
    }

    public void findCheckedItems(CheckBoxTreeItem<?> item) {
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
