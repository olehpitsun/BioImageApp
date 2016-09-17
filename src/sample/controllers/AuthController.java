package sample.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.libs.CurrentStage;
import sample.libs.SQLDatabaseParam;
import sample.models.AuthModel;
import sample.models.DbModel;

import java.sql.SQLException;

public class AuthController {
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Button SignIn;
    AuthModel authModel;
    private Stage dialogStage;
    //private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void checkIt() throws SQLException, Exception {
        authModel = new AuthModel();
        authModel.setLogin(login.getText());
        authModel.setPassword(password.getText());
        authModel.comparePassword();
        authModel.signingIn();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }



    /**
     * Returns true if the user clicked OK, false otherwise.
     * @return
     */
   // public boolean isOkClicked() {
        //return okClicked;
  //  }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    public void SignIn() throws Exception {

        System.out.print("Ok");
        this.checkIt();
        dialogStage.close();
        //CurrentStage.getStage().close();

    }


}
