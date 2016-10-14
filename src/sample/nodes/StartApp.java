/**
 *
 */

package sample.nodes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import org.opencv.core.Core;
import sample.libs.Messages;
import sample.objects.Messenger.Messenger;
import sample.libs.Session;
import sample.models.CheckerModel;
import sample.models.DbModel;
import sample.objects.Sendings;
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
            Messages.error("Помилка БД!", "Не встановлено з'єднання з БД", "БД");
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

    public static void adminPage()throws IOException{
        try {
            Admin admin = new Admin();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static void laborantPage(){
        try {
            Laborant laborant = new Laborant();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static void diagnosisDoctor(){
        try {
            DiagnosisDoctor diagnosisDoctor = new DiagnosisDoctor();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static void likDoctorPage() throws IOException{
        try {
            LikDoctorPageModule likDoctorPageModule = new LikDoctorPageModule();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static void showDBSettingsPage() throws IOException{

        DBConnectionModule db = new DBConnectionModule();
        db.showDbConnectDialog();
    }

    public static void showMessage(Messenger messenger){
        if(Session.getKeyValue("activeStatus") == "1") {
            ShowMessageModule showMessageModule = new ShowMessageModule();
            showMessageModule.showMessageDialog(messenger);
        }else{
            Messages.error("Помилка авторизації!", "Увійдіть в систему", " ");
        }
    }

    public static void showSendingMessage(Sendings sendings){
        if(Session.getKeyValue("activeStatus") == "1") {
            ShowSendingMessageModule showMessageModule = new ShowSendingMessageModule();
            showMessageModule.showSendingMessageDialog(sendings);
        }else{
            Messages.error("Помилка авторизації!", "Увійдіть в систему", " ");
        }
    }

    public static void writeMessage(){
        if(Session.getKeyValue("activeStatus") == "1"){
            writeMessageModule writeMessageModule = new writeMessageModule();
            writeMessageModule.writeMessageeDialog();
        }else{
            Messages.error("Помилка авторизації!", "Увійдіть в систему", " ");
        }
    }

    public static void writeSendingMessage(){
        if(Session.getKeyValue("activeStatus") == "1"){
            writeSendingMessageModule writeMessageModule = new writeSendingMessageModule();
            writeMessageModule.writeSendingMessageeDialog();
        }else{
            Messages.error("Помилка авторизації!", "Увійдіть в систему", " ");
        }
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

    public static void QuantitativeParametersPage(){
        try {
            QuantitativeParametersPageModule quantitativeParametersPageModule = new QuantitativeParametersPageModule();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    public static void sending(){
        try {
            SendingModule sendingModule = new SendingModule();
        }catch (Exception e){
            System.err.println(e);
        }
    }
}


