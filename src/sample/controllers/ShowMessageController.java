package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.objects.Messenger.Messenger;

/**
 * Created by oleh on 18.06.2016.
 */
public class ShowMessageController {

    @FXML
    private TextField MessageTextField;
    @FXML
    private Label senderField, MessageTextLabel, dateLabel;

    private Stage dialogStage;
    private Messenger messenger;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Messenger messenger) {
        this.messenger = messenger;

        senderField.setText(messenger.getSend_from_id());
        MessageTextLabel.setText(messenger.getMessage());
        MessageTextLabel.setWrapText(true);

        dateLabel.setText(messenger.getMessageDate());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
