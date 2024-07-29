package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.nio.file.Path;

public class DiagramCreationController
{

    @FXML
    MenuBar          menuBar;
    @FXML
    TreeView<String> treeView;
    @FXML
    HBox             hBox;
    @FXML
    BorderPane       borderPane;

    private ProjectTreeView projectTreeView;


    public void createTreeView(Path sourceFolderPath)
    {
        projectTreeView = new ProjectTreeView(treeView, sourceFolderPath);
        projectTreeView.createTreeView();
    }


    public void createDiagram(ActionEvent event)
    {
        createProject(event);
        loadProjectFiles();
        viewProject();
    }

    public void createDiagramSvg(ActionEvent event)
    {
        createProject(event);
        loadProjectFiles();
        DiagramCreation.getInstance().viewProject("plantuml");
    }


    public void createProject(ActionEvent event)
    {
        DiagramCreation.getInstance().setProjectTreeView(projectTreeView);
        DiagramCreation.getInstance().setMenuBar(menuBar);
        DiagramCreation.getInstance().createProject(((MenuItem)event.getSource()).getText());
        borderPane.setLeft(projectTreeView.treeView);
    }


    public void loadProjectFiles()
    {
        DiagramCreation.getInstance().loadProject();
    }


    public void viewProject()
    {
        DiagramCreation.getInstance().viewProject("smartgraph");
    }


    public void openProject()
    {
        MenuUtility.openProject(menuBar);
    }


    public void closeProject()
    {
        MenuUtility.closeProject(menuBar);
    }


    public void loadDiagram(ActionEvent event)
    {
        FileUtility.setLoadedDiagramName(MenuUtility.loadDiagram(menuBar, event));
    }


    public void aboutPage() {MenuUtility.aboutPage(menuBar);}


    public void quitApp()   {MenuUtility.quitApp(menuBar);}


    public void setProject()
    {
        this.projectTreeView = DiagramCreation.getInstance().getProjectTreeView();
        borderPane.setLeft(projectTreeView.treeView);
        DiagramCreation.getInstance().setMenuBar(menuBar);
    }
}
