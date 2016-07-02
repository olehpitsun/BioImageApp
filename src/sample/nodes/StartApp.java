/**
 *
 */

package sample.nodes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import sample.libs.Messages;
import sample.libs.Messenger.Messenger;
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

            AuthModule auth = new AuthModule();
            auth.authDialog();

        }else{
            Messages.error("Помилка БД!", "Не встановлено з\'єднання з БД", "БД");
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

        DBConnectionModule db = new DBConnectionModule();
        db.showDbConnectDialog();

    }

    public static void showMessage(Messenger messenger){

        ShowMessageModule showMessageModule = new ShowMessageModule();
        showMessageModule.showMessageDialog(messenger);
    }

    public static void writeMessage(){
        writeMessageModule writeMessageModule = new writeMessageModule();
        writeMessageModule.writeMessageeDialog();
    }

    /**
     * функція для відображення сторінки з дослідами
     */
    public static void showSimpleResearch(){
        try {
            SimpleResearchModule srm = new SimpleResearchModule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


