package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.models.SendingModel;
import sample.nodes.StartApp;
import sample.objects.Sendings;

import java.sql.SQLException;

/**
 * Created by oleh_pi on 18.09.2016.
 */
public class SendingController {
    SendingModel sendingModel;
    @FXML
    private TableView<Sendings> messenger;
    @FXML
    private TableColumn<Sendings, Integer> idColumn;
    @FXML
    private TableColumn<Sendings, String> messageDateColumn, send_FromColumn;
    public static ObservableList<Sendings> messengersData = FXCollections.observableArrayList();
    private boolean okClicked = false;
    private Stage stage;

    public void initialize(){
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        messageDateColumn.setCellValueFactory(cellData -> cellData.getValue().messageDateProperty());
        send_FromColumn.setCellValueFactory(cellData -> cellData.getValue().send_from_idProperty());
        messengersData.clear();// очистка списку повідомлень
        this.showMessenger();
    }



    @FXML
    private void showMessages() throws SQLException {


    }


    @FXML
    private void showMessenger(){

        /*** обробка вибраного повідомлення*/
        messenger.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Sendings selected = messenger.getSelectionModel().getSelectedItem();
                    StartApp.showSendingMessage(selected);
                }
            }
        });

        messengersData.clear();// очистка списку повідомлень
        try {
            sendingModel = new SendingModel();
            sendingModel.selectData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        messenger.setItems(SendingController.messengersData);
    }

    @FXML
    private void createSendingMessage(){
        StartApp.writeSendingMessage();
    }


}
