package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.nodes.AuthModule;
import java.io.IOException;


public class StartAppController{
    @FXML
    private Label currentOper;
    public void setText(String st)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentOper.setText(st);
            }
        });
    }
}
