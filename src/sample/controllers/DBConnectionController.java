package sample.controllers;

import com.jfoenix.controls.JFXCheckBox;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by oleh on 30.04.2016.
 */
public class DBConnectionController {
    @FXML
    private JFXTextField hostField, portField, userField, dbnameField;
    @FXML
    private JFXCheckBox remember;
    @FXML
    private JFXPasswordField passwordField;
    private Stage dialogStage;
    //private Person person;
    private boolean okClicked = false;
    FileInputStream fis;
    Properties property;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        property = new Properties();
        try {
            fis = new FileInputStream("src/sample/props/db.properties");
            property.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        hostField.setText(property.getProperty("db.host"));
        portField.setText(property.getProperty("db.port"));
        userField.setText(property.getProperty("db.user"));
        passwordField.setText(property.getProperty("db.pass"));
        dbnameField.setText(property.getProperty("db.name"));
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
            if(remember.isSelected())
            {
                try {
                    FileOutputStream fout = new FileOutputStream("src/sample/props/db.properties");
                    Properties props = new Properties();
                    props.setProperty("db.host", hostField.getText());
                    props.setProperty("db.port", portField.getText());
                    props.setProperty("db.user", userField.getText());
                    props.setProperty("db.pass", passwordField.getText());
                    props.setProperty("db.name", dbnameField.getText());
                    props.store(fout, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
