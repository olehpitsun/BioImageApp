package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

/**
 * Created by oleh on 22.08.2016.
 */
public class LaborantView {
    Stage primaryStage = new Stage();
    public void render() throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/sample/views/fxml/LaborantView.fxml"));
        Scene scene = new Scene(root);
        CurrentStage.setStage(primaryStage);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Лаборант");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/style.css");
        primaryStage.show();
    }
}
