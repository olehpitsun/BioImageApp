package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

import java.io.IOException;

/**
 * Created by Petro on 04.05.2016.
 */
public class PatientsView {
    Stage primaryStage = new Stage();
    public void render() throws IOException
    {
        Parent parent = FXMLLoader.load(getClass().getResource("/sample/views/fxml/Patients.fxml"));
        Scene scene = new Scene(parent, 600, 400);
        CurrentStage.setOwnerStage(primaryStage);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Patients List");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/style.css");
        primaryStage.show();


    }
}
