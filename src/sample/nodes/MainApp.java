package sample.nodes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.views.MainWindowView;

import java.io.IOException;

/**
 * Автор: Петро Лящинський
 * Дата створення: 23.04.2016.
 */

public class MainApp {

    MainWindowView mainWindowView;
    protected MainApp mainApp;
    private Stage primaryStage;

    public MainApp() throws Exception {
        mainWindowView = new MainWindowView();
        mainWindowView.render();
    }


    public void setMainApp(MainApp mainApp) {

        this.mainApp = mainApp;
    }


}
