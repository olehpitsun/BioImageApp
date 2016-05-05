package sample.views;

/**
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * ---------------------------
 * Клас завантажує FXML файл, в якому прописаний загальний вигляд робочого простору користувача (лікаря).
 */


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.omg.CORBA.Current;
import sample.libs.CurrentStage;


public class MainWindowView {

    Stage primaryStage = new Stage();

    public void render() throws Exception {
        Parent pr1 = FXMLLoader.load(getClass().getResource("fxml/MainWindowView.fxml"));
        primaryStage.setTitle("MW");
        Scene scene = new Scene(pr1);
        scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(275);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        CurrentStage.setMainStage(primaryStage);
        CurrentStage.getStage().close();
        primaryStage.show();
    }



}
