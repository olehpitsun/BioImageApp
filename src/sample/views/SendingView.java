package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

/**
 * Created by oleh_pi on 18.09.2016.
 */
public class SendingView {
    Stage primaryStage = new Stage();

    public void render() throws Exception {
        Parent pr1 = FXMLLoader.load(getClass().getResource("/sample/views/fxml/SendingView.fxml"));
        primaryStage.setTitle("Направлення");
        Scene scene = new Scene(pr1);
        scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(275);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        CurrentStage.setMainStage(primaryStage);
        CurrentStage.getStage().close();
        primaryStage.show();
    }
}
