package sample.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.libs.SQLDatabase;
import sample.libs.*;
import sample.models.DbModel;
import sample.nodes.DBConnectionModule;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by oleh on 30.04.2016.
 */
public class DBConnectionController {
    @FXML
    private JFXTextField hostField, portField, userField, dbnameField;
    @FXML
    private JFXPasswordField passwordField;
    private Stage dialogStage;
    //private Person person;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setConnectField() {
        hostField.setText("localhost");
        portField.setText("3306");
        userField.setText("oleh");
        passwordField.setText("oleh123");
        dbnameField.setText("bioimageapp");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    public void handleOk() throws ClassNotFoundException, SQLException {

        if (isInputValid()) {
            System.out.print("Ok");

            SQLDatabaseParam.setHost(hostField.getText());
            SQLDatabaseParam.setPort(portField.getText());
            SQLDatabaseParam.setDbuser(userField.getText());
            SQLDatabaseParam.setDbpass(passwordField.getText());
            SQLDatabaseParam.setDbname(dbnameField.getText());

            DbModel db = new DbModel();
            if(db.checkDbConnection() == true){
                Messages.information("Зв'язок з БД", "З'єднання успішно встановлено", "БД");
            }

            okClicked = true;
            dialogStage.close();


        }
    }

    /**
     * Called when the user clicks cancel@FXML
     * private void handleCancel() {
     * dialogStage.close();
     * }
     * <p>
     * /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (hostField.getText() == null || hostField.getText().length() == 0) {
            errorMessage += "Заповніть коректно поле ХОСТ!\n";
        }

        if (portField.getText() == null || portField.getText().length() == 0) {
            errorMessage += "Заповніть коректно поле ПОРТ!\n";
        }

        if (userField.getText() == null || userField.getText().length() == 0) {
            errorMessage += "Заповніть коректно поле Користувач!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Заповніть коректно поле пароль!\n";
        }
        if (dbnameField.getText() == null || dbnameField.getText().length() == 0) {
            errorMessage += "Заповніть коректно поле Назва БД!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Заповніть коректно поля");
            alert.setContentText(errorMessage);

            alert.showAndWait();


            return false;
        }
    }
}
