package sample.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.libs.Messenger.Messenger;
import sample.libs.UsersColection;
import sample.models.MessengerModel;
import java.sql.SQLException;

/**
 * Created by oleh on 02.07.2016.
 */
public class writeMessageController {

    @FXML
    private JFXTextField titleMessage;
    @FXML
    private JFXTextArea messageText;
    @FXML
    private JFXComboBox<UsersColection> receiverComboBox;
    @FXML
    private Label messageTextError;
    private Stage dialogStage;
    private Messenger messenger;
    private boolean okClicked = false;
    public static ObservableList<UsersColection> comboBoxData = FXCollections.observableArrayList();

    private int receiverId;
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setReceiverList() throws SQLException {

        MessengerModel messengerModel = new MessengerModel();
        messengerModel.selectUsers();
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

        MessengerModel messengerModel = new MessengerModel();
        if (messengerModel.isInputValid(titleMessage.getText(), messageText.getText())){
            okClicked = true;
            dialogStage.close();
            if(messengerModel.insertMessageToDb(this.receiverId,titleMessage.getText(), messageText.getText())){
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
