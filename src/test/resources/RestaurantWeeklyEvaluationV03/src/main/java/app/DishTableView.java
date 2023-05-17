package app;

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

import model.Dish;


/**
 * <h1>DishTableView</h1>
 * Responsible for showing dishes as a javafx table 
 *
 * Observe that the class has package-private visibility:
 * clients should only use the public Interface for work-to-be-done
 * and the factory for object creation
 * 
 * The class is final: cannot be subclassed; if need, create a new v. of the class
 * and exploit the factory for its generation
 * 
 * @version 1.0
 * @author pvassil
 *
 */
final class DishTableView extends VBox {

	public DishTableView(ObservableList<Dish> data){
		TableView<Dish> table = new TableView<Dish>(data);
        table.setEditable(false);
        
        TableColumn<Dish, String> nameCol = new TableColumn<Dish, String>("Name");
        nameCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, String>, ObservableValue<String>> nameColData = 
        		new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(
					javafx.scene.control.TableColumn.CellDataFeatures<Dish, String> param) {
				return new ReadOnlyObjectWrapper<String>(param.getValue().getDishName());
			}
         };
        nameCol.setCellValueFactory(nameColData);
        nameCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(nameCol);  
        
        
        TableColumn<Dish, Double> gainCol = new TableColumn<Dish, Double>("Gain");
        gainCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double>, ObservableValue<Double>> gainColData = 
        		new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double>, ObservableValue<Double>>() {
			@Override
			public ObservableValue<Double> call(
					javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double> param) {
				return new ReadOnlyObjectWrapper<Double>(param.getValue().getGain());
			}
         };
        gainCol.setCellValueFactory(gainColData);
        gainCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(gainCol);
        
        TableColumn<Dish, Double> timeCol = new TableColumn<Dish, Double>("Construction Time");
        timeCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double>, ObservableValue<Double>> timeColData = 
        		new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double>, ObservableValue<Double>>() {
			@Override
			public ObservableValue<Double> call(
					javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double> param) {
				return new ReadOnlyObjectWrapper<Double>(param.getValue().getAvgTimeToMake());
			}
         };
        timeCol.setCellValueFactory(timeColData);
        timeCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(timeCol);

        TableColumn<Dish, Double> starsCol = new TableColumn<Dish, Double>("Stars");
        starsCol.setMinWidth(100);
        Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double>, ObservableValue<Double>> starsColData = 
        		new Callback<javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double>, ObservableValue<Double>>() {
			@Override
			public ObservableValue<Double> call(
					javafx.scene.control.TableColumn.CellDataFeatures<Dish, Double> param) {
				return new ReadOnlyObjectWrapper<Double>(param.getValue().getEvaluation());
			}
         };
        starsCol.setCellValueFactory(starsColData);
        starsCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        table.getColumns().add(starsCol);        
        
		table.setItems(data);

		 
        final Label label = new Label("Dishes");
        label.setFont(new Font("Calibri", 20));
        
        setSpacing(5);
        setPadding(new Insets(10, 0, 0, 10));
        getChildren().addAll(label, table);
	}
}
