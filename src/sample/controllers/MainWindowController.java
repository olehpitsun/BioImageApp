package sample.controllers;

/**
 * Автор: Павло Лящинський, Піцун Олег
 * Дата створення: 23.04.2016.
 * ---------------------------
 * Клас є контролером для головного вікна, яке з'являється після успішної авторизації в системі.
 * Це вікно є робочим простором лікаря, яке містить свій функціонал.
 */

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.objects.Messenger.Messenger;
import sample.libs.Session;
import sample.models.MessengerModel;
import sample.nodes.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{

    MessengerModel messengerModel;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private JFXHamburger authHamburger;
    @FXML
    private JFXDrawer authDrawer;
    @FXML
    private JFXPasswordField passwordTextField;
    @FXML
    private JFXButton dbSettings, authButton, dbSettingButton, but;
    private ArrayList<JFXDrawer> drawers = new ArrayList<>();
    private Node content ;

    @FXML
    private Button  directionButton, showButton1,cytologyButton, histologyButton, DiagnosisRulesButton,
            PatternsDescriptionButton, objectCharacterParamButton;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            VBox box = FXMLLoader.load(getClass().getResource("../views/fxml/DrawerContent.fxml"));
            authDrawer.setSidePane(box);
            authDrawer.setOverLayVisible(false);

            for(Node node : box.getChildren()){
                if(node.getAccessibleText() != null){
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getAccessibleText()){
                            case "dbSettings" :
                                try {
                                    handleDBConnect();
                                }catch (Exception exc){
                                    System.err.println(exc);
                                }
                                break;
                            case "auth" :
                                handleSignIn();
                                break;
                            case "logout" :
                                logout();
                                break;
                            case "writeMessage" :
                                writeMessage();
                                break;
                            case "refreshMessages" :
                                try {
                                    refreshMessages();
                                }catch (SQLException sqlEx){
                                    System.err.print(sqlEx);
                                }
                                break;
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HamburgerBackArrowBasicTransition authBurgerTask = new HamburgerBackArrowBasicTransition(authHamburger);
        authBurgerTask.setRate(-1);
        authHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            authBurgerTask.setRate(authBurgerTask.getRate() * -1);
            authBurgerTask.play();
            if(authDrawer.isShown() || authDrawer.isShowing()){
                authDrawer.close();
                authDrawer.setOverLayVisible(false);
            }
            else{
                updateDrawerPosition(authDrawer);
                authDrawer.open();
            }
        });

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        messageDateColumn.setCellValueFactory(cellData -> cellData.getValue().messageDateProperty());
        send_FromColumn.setCellValueFactory(cellData -> cellData.getValue().send_from_idProperty());
    }

    /**
     * update drawers position in the stack once a drawer is drawn
     * @param drawer
     */
    private void updateDrawerPosition(JFXDrawer drawer){
        int index = drawers.indexOf(drawer);
        if(index + 1 < drawers.size()){
            if(index - 1 >= 0) drawers.get(index+1).setContent(drawers.get(index-1));
            else if(index == 0) drawers.get(index+1).setContent(content);
        }
        if(index < drawers.size() - 1){
            drawer.setContent(drawers.get(drawers.size()-1));
            drawers.remove(drawer);
            drawers.add(drawer);
            //this.getChildren().add(drawer);
        }
    }

    public MainWindowController() {}

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

    @FXML    public void registerADoctor(){}

    @FXML
    public void handleSignIn(){
        try {
            StartApp.startAuth();

            if(Session.getKeyValue("activeStatus") == "1"){
                switch (Session.getKeyValue("role_id")){
                    case "1":
                        this.activateMainPage();
                        StartApp.adminPage();
                        break;
                    case "2":
                        this.activateMainPage();
                        break;
                    case "3":
                        StartApp.laborantPage();
                        break;
                    case "4":
                        this.ShowDiagnosisDoctorElements();
                        break;
                }

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
    private void activateMainPage() throws SQLException {

        showButton1.setDisable(false); directionButton.setDisable(false);
        cytologyButton.setDisable(false); histologyButton.setDisable(false);
        objectCharacterParamButton.setVisible(false);
        //AuthModule auth = new AuthModule();
        this.showMessenger();

    }


    @FXML
    private void showMessenger(){
        messengersData.clear();// очистка списку повідомлень
        try {
            messengerModel = new MessengerModel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        messenger.setVisible(true);
        try {
            messengerModel.selectData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        messenger.setItems(MainWindowController.messengersData);
    }

    @FXML
    private void logout(){
        Session.clear();

        objectCharacterParamButton.setVisible(false);
        DiagnosisRulesButton.setVisible(false);
        PatternsDescriptionButton.setVisible(false);

        showButton1.setDisable(true); directionButton.setDisable(true);
        cytologyButton.setVisible(true); histologyButton.setVisible(true);
        cytologyButton.setDisable(true); histologyButton.setDisable(true);
        messenger.setVisible(false);


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
    public void handleDBConnect() throws Exception {
        StartApp.showDBSettingsPage();
    }

    @FXML
    private void handleSimpleResearch()throws IOException{
        StartApp.likDoctorPage();
        //StartApp.showSimpleResearch();
    }

    @FXML
    private void handlePacientList() throws Exception{

        Patients patient = new Patients();
    }

    @FXML
    private void handleResearchList(){
    }

    @FXML
    private void ShowDiagnosisDoctorElements(){
        showButton1.setDisable(false);
        directionButton.setDisable(false);
        cytologyButton.setDisable(false); histologyButton.setDisable(false);
        cytologyButton.setVisible(false);histologyButton.setVisible(false);

        objectCharacterParamButton.setVisible(true);
        DiagnosisRulesButton.setVisible(true);
        PatternsDescriptionButton.setVisible(true);

        this.showMessenger();
    }

    @FXML
    private void handleDiagnosisRules(){
        StartApp.DDQualityCharacteristics();
    }

    @FXML
    private void handlePatternsDescription(){
        System.out.println("handlePatternsDescription");
    }

    /**
     * Кількісні характеристики
     * Формування звіту
     */
    @FXML
    private void handleQuantitativeParameters(){
        StartApp.QuantitativeParametersPage();
    }

    @FXML
    private void Sending(){
        StartApp.sending();
    }

} // class MainWindowController