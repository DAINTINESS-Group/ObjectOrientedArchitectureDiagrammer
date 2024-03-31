package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import model.Dish;
import model.Employee;
import controller.IMainEngine;
import controller.MainEngineFactory;

public final class ApplicationMainGUI extends Application
{


    //web http://java-buddy.blogspot.com/
//http://docs.oracle.com/javafx/2/ui_controls/button.htm
//http://docs.oracle.com/javafx/2/ui_controls/ButtonSample.java.html
//http://docs.oracle.com/javafx/2/ui_controls/jfxpub-ui_controls.htm    
    @Override
    public void start(final Stage primaryStage)
    {

        MainEngineFactory mainEngineFactory = new MainEngineFactory();
        final IMainEngine mainEngine        = mainEngineFactory.createMainEngine();

        int _loadFlag;
        _loadFlag = mainEngine.loadAllData();
        mainEngine.computeAllStats();
        System.out.println("Loading returned: " + _loadFlag + "\n");

        mainEngine.createReports("ReportGeneratorForFiles");
        mainEngine.createReports("ReportGeneratorForHTML");

        Button btnEmps = new Button();
        btnEmps.setText("Employees");
        btnEmps.setMinSize(80, 40);
        btnEmps.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {

                ObservableList<Employee> data            = FXCollections.observableArrayList();
                int                      empLoadedStatus = mainEngine.getEmployees(data);
                if (empLoadedStatus == -1)
                {
                    System.err.println("ApplicationMainGui: Could not load employees to gui, exiting");
                    System.exit(0);
                }
                EmpTableView empTV = new EmpTableView(data);

                StackPane empReportLayout = new StackPane();
                empReportLayout.getChildren().add(empTV);

                Scene empReportScene = new Scene(empReportLayout, 800, 600);

                //New window for Report
                Stage empReportStage = new Stage();
                empReportStage.setTitle("Emp Report");
                empReportStage.setScene(empReportScene);

                //Set position of second window, related to primary window.
                empReportStage.setX(primaryStage.getX() + 250);
                empReportStage.setY(primaryStage.getY() + 100);

                empReportStage.show();
            }
        });

        Button btnDish = new Button();
        btnDish.setText("Dishes");
        btnDish.setMinSize(80, 40);
        btnDish.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                ObservableList<Dish> dataDish         = FXCollections.observableArrayList();
                int                  dishLoadedStatus = mainEngine.getDishes(dataDish);
                if (dishLoadedStatus == -1)
                {
                    System.err.println("ApplicationMainGui: Could not load dishes to gui, exiting");
                    System.exit(0);
                }

                DishTableView dishTV = new DishTableView(dataDish);

                StackPane dishReportLayout = new StackPane();
                dishReportLayout.getChildren().add(dishTV);

                Scene dishReportScene = new Scene(dishReportLayout, 800, 600);

                //New window for Report
                Stage dishReportStage = new Stage();
                dishReportStage.setTitle("Dish Report");
                dishReportStage.setScene(dishReportScene);

                //Set position of second window, related to primary window.
                dishReportStage.setX(primaryStage.getX() + 300);
                dishReportStage.setY(primaryStage.getY() - 100);

                dishReportStage.show();
            }
        });

        Button btnExit = new Button();
        btnExit.setText("Exit");
        btnExit.setMinSize(80, 40);
        btnExit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                System.exit(0);
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().add(btnEmps);
        vbox.getChildren().add(btnDish);
        vbox.getChildren().add(btnExit);
        vbox.setSpacing(10);
        vbox.setAlignment(javafx.geometry.Pos.BASELINE_CENTER);


        Scene scene = new Scene(vbox, 300, 250);

        primaryStage.setTitle("Main control");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    //   private static MainEngine mainEngine;
}