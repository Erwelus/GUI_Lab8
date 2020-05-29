package sample;

//import com.sun.prism.paint.Color;
import com.jcraft.jsch.JSchException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import packet.BDconnector;
import packet.Person;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    private ObservableList<Person> list;
    private Drawer drawer;
    //private Manager manager;
    //private BDconnector bc;
    private TimerTask task;
    private Timer timer = new Timer();
    private Timer animeTimer = new Timer();
    private TimerTask draw;
    private static boolean flag = true;

    @FXML
    private Button translate;
    @FXML
    private ComboBox<String> language;
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
        ObservableList<Person> filteredList = Main.manager.getList(Main.bc);
        if(name.getText().length()>0){
            filteredList.clear();
            list.stream().filter(p -> p.getName().equals(name.getText())).forEach(filteredList::add);
            task.cancel();
        }else startUpdating();
        personTable.setItems(filteredList);
    }

    private void update(){
        list = Main.manager.getList(Main.bc);
        personTable.setItems(list);
        //drawer.draw(list);
    }

    private void translate(String lang){
        flag = false;
        switch (lang){
            case "Русский": Main.resources = ResourceBundle.getBundle("resources/locale_ru"); break;
            case "Nederlands": Main.resources = ResourceBundle.getBundle("resources/locale_nd"); break;
            case "Svensk": Main.resources = ResourceBundle.getBundle("resources/locale_sw"); break;
            case "Español": Main.resources = ResourceBundle.getBundle("resources/locale_sp"); break;
        }
        Scene scene = translate.getScene();
        try {
            scene.setRoot(FXMLLoader.load(getClass().getResource("sample.fxml"), Main.resources));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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


        language.getItems().addAll("Русский", "Nederlands", "Svensk", "Español");
        translate.setOnAction(event -> translate(language.getValue()));

        list = Main.manager.getList(Main.bc);
        drawer = new Drawer(canvas);
        personTable.setOnSort(event -> task.cancel());


        /*
        personTable.setItems(list);
        drawer.draw(list);*/

        startUpdating();

        draw = new TimerTask() {
            @Override
            public void run() {
                drawer.draw(list);
            }
        };
        animeTimer.schedule(draw, 0L, 100L);
    }

    private void startUpdating() {
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