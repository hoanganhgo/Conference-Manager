package view;

import dao.Business;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("frame/Home.fxml"));
        primaryStage.setTitle("Trang chủ");
        Scene scene=new Scene(root, 1280, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
        Business.homeStage=primaryStage;      
    }


    public static void main(String[] args) {
        launch(args);
    }
}
