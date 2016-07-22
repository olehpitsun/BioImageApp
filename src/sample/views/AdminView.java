package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

import java.io.IOException;

/**
 * Created by Pavlo Liashchynskyi on 10.07.2016.
 */
public class AdminView {

    Stage primaryStage = new Stage();
    public void render() throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("fxml/AdminView.fxml"));
        Scene scene = new Scene(root);
        CurrentStage.setStage(primaryStage);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/style.css");
        primaryStage.show();


    }
}
