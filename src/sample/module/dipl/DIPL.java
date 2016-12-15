package sample.module.dipl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import sample.module.dipl.controler.Controller;

public class DIPL extends Application {


    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resource/MainWindow.fxml"));
        VBox root = loader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("resource/MainWindow.fxml"));
        primaryStage.setTitle("Preprocessing");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Controller controller = loader.getController();
        //controller.setStage(this.primaryStage);
        controller.init();
    }

}