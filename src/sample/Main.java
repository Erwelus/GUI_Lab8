package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import packet.BDconnector;


import java.util.ResourceBundle;


public class Main extends Application {
    public static ResourceBundle resources;
    public static Manager manager;
    public static BDconnector bc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        manager = new Manager();
        bc = new BDconnector();
        resources = ResourceBundle.getBundle("resources/locale_ru");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"), resources);
        primaryStage.setTitle("Another Trash");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}