package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

import java.io.IOException;

/**
 * Created by Pavlo Liashchynskyi on 22.07.2016.
 */
public class StatisticsView {

    Stage primaryStage = new Stage();
    public void render() throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("fxml/StatisticsView.fxml"));
        Scene scene = new Scene(root);
        CurrentStage.setOwnerStage(primaryStage);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/style.css");
        primaryStage.show();

    }
}
