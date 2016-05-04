package sample.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.libs.CurrentStage;
import sample.nodes.AuthModule;
import sample.nodes.StartApp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Admin on 25.04.2016.
 */
public class CheckerView{
    Stage primaryStage = new Stage(StageStyle.UNDECORATED);
    public synchronized void render() throws Exception
    {
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/START.fxml"));
        Scene scene = new Scene(parent, 369, 478);
        primaryStage.setResizable(false);
        primaryStage.setMinHeight(478);
        primaryStage.setMinWidth(369);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);
        CurrentStage.setStage(primaryStage);

    }

}
