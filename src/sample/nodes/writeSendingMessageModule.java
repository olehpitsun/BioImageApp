package sample.nodes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controllers.writeSendingMessageController;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by oleh_pi on 18.09.2016.
 */
public class writeSendingMessageModule {

    private Stage primaryStage;

    public boolean writeSendingMessageeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartApp.class.getResource("../views/fxml/writeSendingMessage.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Створити направлення");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");

            dialogStage.setScene(scene);

            // Set the person into the controller.
            writeSendingMessageController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            try {
                controller.setReceiverList();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
