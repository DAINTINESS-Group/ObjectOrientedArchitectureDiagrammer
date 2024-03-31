package app;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import model.Employee;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;


/**
 * <h1>EmpTableView</h1>
 * Responsible for showing employees as a javafx table
 * <p>
 * Observe that the class has package-private visibility: clients should only use the public Interface for work-to-be-done and the factory for object creation
 * <p>
 * The class is final: cannot be subclassed; if need, create a new v. of the class and exploit the factory for its generation
 *
 * @author pvassil
 * @version 1.0
 */
final class EmpTableView extends VBox
{


    public EmpTableView(ObservableList<Employee> data)
    {
        TableView<Employee> table = new TableView<Employee>(data);

        table.setEditable(false);


        /*
         * Xtikio:
         *	- convert simple values to ObservableValues :(
         *  - link the respective class method with the table column
         */
        TableColumn<Employee, String> nameCol = new TableColumn<Employee, String>("Name");
        nameCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>> nameColData =
            new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>()
            {

                @Override
                public ObservableValue<String> call(
                    javafx.scene.control.TableColumn.CellDataFeatures<Employee, String> param)
                {
                    return new ReadOnlyObjectWrapper<String>(param.getValue().getName());
                }
            };
        nameCol.setCellValueFactory(nameColData);
        nameCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(nameCol);


        TableColumn<Employee, Integer> ordersCol = new TableColumn<Employee, Integer>("NumOrders");
        ordersCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, Integer>, ObservableValue<Integer>> ordersColData =
            new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, Integer>, ObservableValue<Integer>>()
            {
                @Override
                public ObservableValue<Integer> call(
                    javafx.scene.control.TableColumn.CellDataFeatures<Employee, Integer> param)
                {
                    return new ReadOnlyObjectWrapper<Integer>(param.getValue().getNumberOfOrders());
                }
            };
        ordersCol.setCellValueFactory(ordersColData);
        ordersCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(ordersCol);

        TableColumn<Employee, Double> starsCol = new TableColumn<Employee, Double>("Stars");
        starsCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, Double>, ObservableValue<Double>> starsColData =
            new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, Double>, ObservableValue<Double>>()
            {
                @Override
                public ObservableValue<Double> call(
                    javafx.scene.control.TableColumn.CellDataFeatures<Employee, Double> param)
                {

                    DecimalFormat formatter = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                    formatter.setRoundingMode(RoundingMode.UP);
                    String s = formatter.format(param.getValue().getEvaluation());
                    return new ReadOnlyObjectWrapper<Double>(Double.parseDouble(s));
                    //Replaced the return below, to avoid the zillion decimals via a quick hack.
                    //Could have returned just the conversion to String; yet, this also shows how to return Double
                    //return new ReadOnlyObjectWrapper<Double>(param.getValue().getEvaluation() );
                }
            };
        starsCol.setCellValueFactory(starsColData);
        starsCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(starsCol);


        TableColumn<Employee, Double> salCol = new TableColumn<Employee, Double>("Salary");
        salCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, Double>, ObservableValue<Double>> salColData =
            new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Employee, Double>, ObservableValue<Double>>()
            {
                @Override
                public ObservableValue<Double> call(
                    javafx.scene.control.TableColumn.CellDataFeatures<Employee, Double> param)
                {
                    DecimalFormat formatter = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                    formatter.setRoundingMode(RoundingMode.UP);
                    String s = formatter.format(param.getValue().getSalary());
                    return new ReadOnlyObjectWrapper<Double>(Double.parseDouble(s));
                    //return new ReadOnlyObjectWrapper<Double>(param.getValue().getSalary());
                }
            };
        salCol.setCellValueFactory(salColData);
        salCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(salCol);


        table.setItems(data);


        final Label label = new Label("Employees");
        label.setFont(new Font("Calibri", 20));

        setSpacing(5);
        setPadding(new Insets(10, 0, 0, 10));
        getChildren().addAll(label, table);


    }
}