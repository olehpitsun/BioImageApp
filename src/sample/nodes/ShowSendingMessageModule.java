package sample.nodes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controllers.ShowSendingMessageController;
import sample.objects.Sendings;

import java.io.IOException;

/**
 * Created by oleh_pi on 18.09.2016.
 */
public class ShowSendingMessageModule {

    private Stage primaryStage;
    public boolean showSendingMessageDialog(Sendings sendings){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartApp.class.getResource("../views/fxml/ShowSendingMessage.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Вхідне направлення");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");

            dialogStage.setScene(scene);

            // Set the person into the controller.
            ShowSendingMessageController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(sendings);

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
