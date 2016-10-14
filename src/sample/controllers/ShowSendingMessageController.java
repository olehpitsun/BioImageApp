package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.objects.Sendings;

/**
 * Created by oleh on 18.06.2016.
 */
public class ShowSendingMessageController {

    @FXML
    private TextField MessageTextField;
    @FXML
    private Label senderField, MessageTextLabel, dateLabel;

    private Stage dialogStage;
    private Sendings sendings;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Sendings sendings) {
        this.sendings = sendings;

        senderField.setText(sendings.getSend_from_id());
        MessageTextLabel.setText(sendings.getMessage());
        MessageTextLabel.setWrapText(true);

        dateLabel.setText(sendings.getMessageDate());
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
