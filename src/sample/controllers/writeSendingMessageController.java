package sample.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.libs.UsersColection;
import sample.models.SendingModel;
import sample.objects.Sendings;

import java.sql.SQLException;

/**
 * Created by oleh_pi on 18.09.2016.
 */
public class writeSendingMessageController {
    @FXML
    private JFXTextField medicalCardNum;
    @FXML
    private JFXTextArea messageText;
    @FXML
    private JFXComboBox<UsersColection> receiverComboBox;
    @FXML
    private Label messageTextError;
    private Stage dialogStage;
    private Sendings sendings;
    private boolean okClicked = false;
    public static ObservableList<UsersColection> comboBoxData = FXCollections.observableArrayList();

    private int receiverId;
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setReceiverList() throws SQLException {

        SendingModel sendingModel = new SendingModel();
        sendingModel.selectUsers();
        receiverComboBox.setItems(comboBoxData);
    }

    @FXML
    private void handleReceiversComboBoxAction(){
        UsersColection selectedSegMetod = receiverComboBox.getSelectionModel().getSelectedItem();
        this.receiverId = selectedSegMetod.getId();
        System.out.print(this.receiverId);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() throws SQLException {

        SendingModel sendingModel = new SendingModel();
        if (sendingModel.isInputValid(medicalCardNum.getText(), messageText.getText())){
            okClicked = true;
            dialogStage.close();
            if(sendingModel.insertMessageToDb(this.receiverId, medicalCardNum.getText(), messageText.getText())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setTitle("відправлено");
                alert.setContentText("Повідомлення відправлено");
                alert.showAndWait();
            }else{
                messageTextError.setVisible(true);
                messageTextError.setText("Помилка запису. Спробуйте знову");
            }
        }else{
            messageTextError.setVisible(true);
            messageTextError.setText(" Заповніть коректно усі поля");
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
