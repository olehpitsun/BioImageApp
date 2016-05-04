package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Admin on 04.05.2016.
 */
public class EditPatientView {

    Stage primaryStage = new Stage();


    public void render() throws IOException {

        Parent parent_panel = FXMLLoader.load(getClass().getResource("fxml/EditPatientClassView.fxml"));
        primaryStage.setTitle("EDITING A PATIENT");
        Scene cont = new Scene(parent_panel);
        cont.getStylesheets().add(0, "sample/views/css/style.css");


        primaryStage.setMaxWidth(605);
        primaryStage.setMaxHeight(600);
        primaryStage.setScene(cont);
        primaryStage.setResizable(false);
        primaryStage.show();


    }



}