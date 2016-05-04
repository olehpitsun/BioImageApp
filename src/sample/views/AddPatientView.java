package sample.views;

/**
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * ---------------------------
 * Клас завантажує FXML файл, в якому прописаний загальний вигляд форми додавання пацієнта.
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AddPatientView {

    Stage primaryStage = new Stage();


    public void render() throws IOException {

        Parent parent_panel = FXMLLoader.load(getClass().getResource("fxml/AddPatientClassView.fxml"));
        primaryStage.setTitle("ADDING A PATIENT");
        Scene cont = new Scene(parent_panel);
        cont.getStylesheets().add(0, "sample/views/css/style.css");


        primaryStage.setMaxWidth(605);
        primaryStage.setMaxHeight(600);
        primaryStage.setScene(cont);
        primaryStage.setResizable(false);
        primaryStage.show();


    }



}