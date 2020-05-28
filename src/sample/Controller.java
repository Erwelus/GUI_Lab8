package sample;

//import com.sun.prism.paint.Color;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import packet.BDconnector;
import packet.Person;

import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    private ObservableList<Person> list;
    private Drawer drawer;
    private Manager manager;
    private BDconnector bc;
    private TimerTask task;
    private Timer timer = new Timer();

    @FXML
    private TextField name;
    @FXML
    private TableColumn<Person, Long> colId;
    @FXML
    private TableColumn<Person, String > colName;
    @FXML
    private TableColumn<Person, Float> colX;
    @FXML
    private TableColumn<Person, Double> colY;
    @FXML
    private TableColumn<Person, Double> colHeight;
    @FXML
    private TableColumn<Person, String> colEyeColor;
    @FXML
    private TableColumn<Person, String> colHairColor;
    @FXML
    private TableColumn<Person, String> colLocName;
    @FXML
    private TableColumn<Person, Float> colLocX;
    @FXML
    private TableColumn<Person, Double> colLocY;
    @FXML
    private TableColumn<Person, String> colNationality;
    @FXML
    private TableColumn<Person, String> colCreator;
    @FXML
    private TableView<Person> personTable;
    @FXML
    private Canvas canvas;

    @FXML
    private void filter(ActionEvent event){
        ObservableList<Person> filteredList = manager.getList(bc);
        if(name.getText().length()>0){
            filteredList.clear();
            list.stream().filter(p -> p.getName().equals(name.getText())).forEach(filteredList::add);
            task.cancel();
        }else startUpdating();
        personTable.setItems(filteredList);
    }

    private void update(){
        list = manager.getList(bc);
        personTable.setItems(list);
        drawer.draw(list);
    }


    @FXML
    private void initialize(){
        colId.setCellValueFactory(cellData -> cellData.getValue().getPropID().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().getPropName());
        colX.setCellValueFactory(cellData -> cellData.getValue().getCoordinates().getPropX().asObject());
        colY.setCellValueFactory(cellData -> cellData.getValue().getCoordinates().getPropY().asObject());
        colHeight.setCellValueFactory(cellData -> cellData.getValue().getPropHeight().asObject());
        colEyeColor.setCellValueFactory(cellData -> cellData.getValue().getPropEyeColor());
        colHairColor.setCellValueFactory(cellData -> cellData.getValue().getPropHairColor());
        colLocName.setCellValueFactory(cellData -> cellData.getValue().getLocation().getPropName());
        colLocX.setCellValueFactory(cellData -> cellData.getValue().getLocation().getPropX().asObject());
        colLocY.setCellValueFactory(cellData -> cellData.getValue().getLocation().getPropY().asObject());
        colNationality.setCellValueFactory(cellData -> cellData.getValue().getPropNationality());
        colCreator.setCellValueFactory(cellData -> cellData.getValue().getPropCreator());

        manager = new Manager();
        bc = new BDconnector();
        drawer = new Drawer(canvas);

        personTable.setOnSort(event -> task.cancel());

        /*list = manager.getList(bc);
        personTable.setItems(list);
        drawer.draw(list);*/

        startUpdating();
    }

    private void startUpdating(){
        task = new TimerTask() {
            @Override
            public void run() {
                update();
                System.out.println("hui");
            }
        };
        timer.schedule(task, 0L, 1000L);
    }

}
