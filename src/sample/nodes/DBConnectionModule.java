package sample.nodes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by oleh on 30.04.2016.
 */
public class DBConnectionModule {

    private Stage primaryStage;

    public boolean showDbConnectDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartApp.class.getResource("/sample/views/fxml/DbConnectionModuleView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Під'єднання до БД");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(0, "/sample/views/css/DarkTheme.css");

            dialogStage.setScene(scene);

            // Set the person into the controller.
            sample.controllers.DBConnectionController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setConnectField();

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