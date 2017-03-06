package sample.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.libs.CurrentStage;

/**
 * Created by oleh on 10.07.2016.
 */
public class LikDoctorPageView {
    Stage primaryStage = new Stage();

    public void render() throws Exception {
        Parent pr1 = FXMLLoader.load(getClass().getResource("/sample/views/fxml/LikDoctorPageView.fxml"));
        primaryStage.setTitle("Лікуючий лікар");
        Scene scene = new Scene(pr1);
        scene.getStylesheets().add(0, "sample/views/css/DarkTheme.css");

        primaryStage.setMinWidth(1300);
        primaryStage.setMinHeight(675);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        CurrentStage.setMainStage(primaryStage);
        CurrentStage.getStage().close();
        primaryStage.show();
    }

}
