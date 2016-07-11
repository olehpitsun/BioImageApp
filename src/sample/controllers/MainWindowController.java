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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.libs.Messenger.Messenger;
import sample.libs.Session;
import sample.models.DbModel;
import sample.models.MessengerModel;
import sample.nodes.*;
import java.io.IOException;
import java.sql.SQLException;


public class MainWindowController {

    MessengerModel messengerModel;
    @FXML
    private Button signInButton, settingsButton, webcamButton, photoCameraButton, address_bookButton, showButton1,
            directionButton, writeMessageButton, refreshMessageButton, histologyButton, cytologyButton, diagnosisRulesButton,
            objectTemplatesButton;
    @FXML
    private TableView<Messenger> messenger;
    @FXML
    private TableColumn<Messenger, Integer> idColumn;
    @FXML
    private Label infoLabel;
    @FXML
    private TableColumn<Messenger, String> messageDateColumn, send_FromColumn;
    public static ObservableList<Messenger> messengersData = FXCollections.observableArrayList();
    private boolean okClicked = false;
    private Stage stage;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        messageDateColumn.setCellValueFactory(cellData -> cellData.getValue().messageDateProperty());
        send_FromColumn.setCellValueFactory(cellData -> cellData.getValue().send_from_idProperty());
    }

    public MainWindowController()
    {
    }

    @FXML
    private void writeMessage(){
        StartApp.writeMessage();
    }

    @FXML
    private void showMessages() throws SQLException {

        /*** обробка вибраного повідомлення*/
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
                directionButton.setDisable(false);
                cytologyButton.setDisable(false);
                histologyButton.setDisable(false);
                //AuthModule auth = new AuthModule();
                messengersData.clear();// очистка списку повідомлень
                messengerModel = new MessengerModel();
                messenger.setVisible(true);
                messengerModel.selectData();
                messenger.setItems(MainWindowController.messengersData);
                writeMessageButton.setVisible(true); refreshMessageButton.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*** обробка вибраного повідомлення*/
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
    private void refreshMessages() throws SQLException {
        messengersData.clear();// очистка списку повідомлень
        messengerModel = new MessengerModel();
        messenger.setVisible(true);
        messengerModel.selectData();
        messenger.setItems(MainWindowController.messengersData);
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
    private void handleSimpleResearch() throws IOException {

        //StartApp.showSimpleResearch();
        StartApp.likDoctorPage();
    }

    @FXML
    private void handlePacientList() throws Exception{

        Patients patient = new Patients();
    }

    @FXML
    private void handleResearchList(){

    }
} // class MainWindowController
