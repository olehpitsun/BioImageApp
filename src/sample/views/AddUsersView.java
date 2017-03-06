package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

/**
 * Created by user1 on 10.07.2016.
 */
public class AddUsersView {
    Stage primaryStage = new Stage();
    public void render() throws Exception
    {
        Parent panel = FXMLLoader.load(getClass().getResource("/sample/views/fxml/AddUsersView.fxml"));
        Scene scene = new Scene(panel);
        CurrentStage.setOwnerStage(primaryStage);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Adding users");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/style.css");
        primaryStage.show();



    }
}
