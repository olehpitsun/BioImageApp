package sample.views;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.libs.CurrentStage;
import sample.nodes.StartApp;


import java.io.IOException;

public class AuthView{

    Stage primaryStage = new Stage();
    public void render() throws IOException
    {/*
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/AuthModuleView.fxml"));
        Scene scene = new Scene(parent);


        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(CurrentStage.getOwnerStage());
        primaryStage.setResizable(false);
        primaryStage.setTitle("Авторизація");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(0, "sample/views/css/style.css");

        CurrentStage.getStage().close();
        primaryStage.show();*/

            Parent parent = FXMLLoader.load(getClass().getResource("fxml/AuthModuleView.fxml"));
            Scene scene2 = new Scene(parent);

            // Create the dialog Stage.
            primaryStage.setTitle("Авторизація");
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.initOwner(primaryStage);
            scene2.getStylesheets().add(0, "sample/views/css/style.css");
            primaryStage.setScene(scene2);


            CurrentStage.getStage().close();
            primaryStage.showAndWait();

            //return controller.isOkClicked();


    }
}
