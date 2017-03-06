package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

public class SimpleResearchModuleView {

    Stage primaryStage = new Stage();

    public void render() throws Exception {
        Parent pr1 = FXMLLoader.load(getClass().getResource("/sample/views/fxml/SimpleResearchModuleView.fxml"));
        primaryStage.setTitle("Досліди");

        Scene scene = new Scene(pr1);
        scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(275);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        CurrentStage.getStage().close();
        primaryStage.show();
    }



}
