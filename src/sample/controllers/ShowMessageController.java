package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.libs.Messenger.Messenger;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by oleh on 18.06.2016.
 */
public class ShowMessageController {

    @FXML
    private TextField MessageTextField;
    @FXML
    private Label senderField, MessageTextLabel;

    private Stage dialogStage;
    private Messenger messenger;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Messenger messenger) {
        this.messenger = messenger;
        Date now = new Date();

        System.out.println(" 2. " + DateFormat.getInstance().format(now));

        // And the default time and date-time DateFormats
        System.out.println(" 3. " + DateFormat.getTimeInstance().format(now));
        System.out.println(" 4. " +
                DateFormat.getDateTimeInstance().format(now));

        // Next, try the short, medium and long variants of the
        // default time format
        System.out.println(" 5. " +
                DateFormat.getTimeInstance(DateFormat.SHORT).format(now));
        System.out.println(" 6. " +
                DateFormat.getTimeInstance(DateFormat.MEDIUM).format(now));
        System.out.println(" 7. " +
                DateFormat.getTimeInstance(DateFormat.LONG).format(now));

        // For the default date-time format, the length of both the
        // date and time elements can be specified. Here are some examples:
        System.out.println(" 8. " + DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.SHORT).format(now));
        System.out.println(" 9. " + DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT).format(now));
        System.out.println("10. " + DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.LONG).format(now));

        senderField.setText(Integer.toString(messenger.getSend_from_id()));
        MessageTextLabel.setText(messenger.getMessage());
        MessageTextLabel.setWrapText(true);
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
