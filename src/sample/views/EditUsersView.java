package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

import java.io.IOException;

/**
 * Created by user1 on 10.07.2016.
 */
public class EditUsersView {
    Stage primaryStage = new Stage();


    public void render() throws IOException {

        Parent parent_panel = FXMLLoader.load(getClass().getResource("fxml/EditUsersView.fxml"));
        primaryStage.setTitle("EDITING USERS");
        Scene cont = new Scene(parent_panel);
        cont.getStylesheets().add(0, "sample/views/css/style.css");
        CurrentStage.setStage(primaryStage);
        primaryStage.setMaxWidth(605);
        primaryStage.setMaxHeight(600);
        primaryStage.setScene(cont);
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(CurrentStage.getOwnerStage());
        primaryStage.setResizable(false);
        primaryStage.show();


    }

}
