package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

/**
 * Created by oleh_pi on 12.09.2016.
 */
public class DiagnosisDoctorView {
    Stage primaryStage = new Stage();
    public void render() throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/sample/views/fxml/DiagnosisDoctorView.fxml"));
        Scene scene = new Scene(root);
        CurrentStage.setStage(primaryStage);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Лікар діагност");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");
        primaryStage.show();
    }
}
