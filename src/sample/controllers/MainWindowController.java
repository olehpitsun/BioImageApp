package sample.controllers;

/**
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * ---------------------------
 * Клас є контролером для головного вікна, яке з'являється після успішної авторизації в системі.
 * Це вікно є робочим простором лікаря, яке містить свій функціонал.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import sample.libs.Messenger.Messenger;
import sample.libs.Nuclei;
import sample.libs.Session;
import sample.models.CellEstimatorModel;
import sample.models.DbModel;
import sample.models.MessengerModel;
import sample.models.PatientsModel;
import sample.nodes.*;
import sample.objects.Patient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class MainWindowController {

    MessengerModel messengerModel;
    @FXML
    private Button signInButton, settingsButton, webcamButton, photoCameraButton, address_bookButton, showButton1,simpleResearchButton;
    @FXML
    private TableView<Messenger> messenger;
    @FXML
    private TableColumn<Messenger, Integer> idColumn, send_FromColumn;
    @FXML
    private TableColumn<Messenger, String> messageColumn;
    public static ObservableList<Messenger> messengersData = FXCollections.observableArrayList();



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
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        messageColumn.setCellValueFactory(cellData -> cellData.getValue().messageProperty());
        send_FromColumn.setCellValueFactory(cellData -> cellData.getValue().send_from_idProperty().asObject());
    }

    public MainWindowController()
    {
    }

    @FXML
    private void showMessages() throws SQLException {

        messengerModel = new MessengerModel();
        messenger.setVisible(true);
        MainWindowController.messengersData.add(new Messenger(4, "Hello", 3,4));
        messengerModel.selectData();
        messenger.setItems(MainWindowController.messengersData);

        /**
         * обробка вибраного повідомлення
         */
        messenger.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    System.out.println(messenger.getSelectionModel().getSelectedItem().idProperty().get());
                    Messenger selected = messenger.getSelectionModel().getSelectedItem();
                    StartApp.showMessage(selected);
                }
            }
        });
    }

    @FXML
    public void registerADoctor(){

    }

    @FXML
    public void handleSignIn(){
        try {
            StartApp.startAuth();

            if(Session.getKeyValue("activeStatus") == "1"){

                infoLabel.setText("Вітаю, " + Session.getKeyValue("name"));
                settingsButton.setDisable(false);
                webcamButton.setDisable(false);
                photoCameraButton.setDisable(false);
                address_bookButton.setDisable(false);
                showButton1.setDisable(false);
                simpleResearchButton.setDisable(false);
                //AuthModule auth = new AuthModule();
            }

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
