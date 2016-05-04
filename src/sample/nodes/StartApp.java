/**
 *
 */

package sample.nodes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.opencv.core.Core;
import sample.models.CheckerModel;
import sample.models.DbModel;
import sample.views.CheckerView;

import java.io.IOException;


public class StartApp extends Application {

    public static boolean DONE = false;
    private Stage primaryStage;

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        CheckerView checkerView = new CheckerView();
        checkerView.render();
        Task<Void> task = new CheckerModel();
        Thread tasks = new Thread(task);
        tasks.start();

    }
    public static void startAuth() throws IOException
    {
        DbModel db = new DbModel();
        if(db.checkDbConnection() == true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        AuthModule auth = new AuthModule();
                        auth.authDialog();
                    } catch (Exception e) {
                    }
                }
            });
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("БД");
            alert.setHeaderText("Помилка");
            alert.setContentText("Не встановлено з'єднання з БД");
            alert.showAndWait();
        }
    }

    /**
     * виклик головного вікна
     * @throws IOException
     */
    public static void startMainPage() throws IOException{
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                try {
                    MainApp mainApp = new MainApp();
                } catch (Exception e) {}
            }
        });
    }

    public static void showDBSettingsPage() throws IOException{
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                try {
                    DBConnectionModule db = new DBConnectionModule();
                    db.showDbConnectDialog();
                } catch (Exception e) {}
            }
        });
    }
}


/////////////

