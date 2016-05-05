package sample.controllers;

/**
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * ---------------------------
 * Клас є контролером для головного вікна, яке з'являється після успішної авторизації в системі.
 * Це вікно є робочим простором лікаря, яке містить свій функціонал.
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import sample.libs.Session;
import sample.models.DbModel;
import sample.nodes.*;
import sample.objects.Patient;

import java.io.IOException;
import java.util.List;


public class MainWindowController {

    @FXML
    private Button signInButton, settingsButton, webcamButton, photoCameraButton, address_bookButton, showButton1,simpleResearchButton;
    @FXML
    private TextField researchNameField;

    @FXML
    private TextField researchPathField;


    @FXML
    private Label researchName;
    @FXML
    private Label researchPathLabel;

    @FXML
    protected ImageView preProcImage;
    @FXML
    protected ImageView segmentationImage;
    @FXML
    protected ImageView originalImage;
    private boolean okClicked = false;

    private String filterType;
    private String segType;

    private Stage stage;

    // the JavaFX file chooser
    private FileChooser fileChooser;
    // support variables
    protected Mat image;
    private Stage dialogStage;

    protected List<Mat> planes;
    // Reference to the main application.
    protected Mat changedimage;

    protected MainApp mainApp;

    private String researchname;
    private String researchPath;

    private String originalImagePath;
    private String generatedImagePath;

    private float meanSquaredError;
    private double psnr;

    @FXML
    public Image img;
    @FXML
    private Label infoLabel;


    @FXML
    public ImageView imgview;
    @FXML
    public void registerADoctor(){

    }

    @FXML
    public void handleSignIn(){
        try {
            StartApp.startAuth();

            infoLabel.setText("Вітаю, " + Session.getKeyValue("name"));
            settingsButton.setDisable(false);
            webcamButton.setDisable(false);
            photoCameraButton.setDisable(false);
            address_bookButton.setDisable(false);
            showButton1.setDisable(false);
            simpleResearchButton.setDisable(false);
            //AuthModule auth = new AuthModule();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addPatient() throws Exception{
        AddPatientModule addPatientModule = new AddPatientModule();

    }

    @FXML
    private void handleDBConnect() throws Exception {

        StartApp.showDBSettingsPage();

        DbModel db = new DbModel();
        if(db.checkDbConnection()) {
            signInButton.setDisable(false);
        }
    }

    @FXML
    private void handleSimpleResearch(){
        StartApp.showSimpleResearch();
    }

    @FXML
    private void handlePacientList() throws Exception{

        Patients patient = new Patients();
    }

    @FXML
    private void handleResearchList(){

    }
} // class MainWindowController
