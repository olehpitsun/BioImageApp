package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.controlsfx.ControlsFXSample;
import sample.libs.CurrentStage;
import sample.libs.Messages;
import sample.libs.Notifi;
import sample.models.AdminModel;
import sample.nodes.AddUsers;
import sample.nodes.Statistics;
import sample.objects.Users;
import sample.views.AddUsersView;
import sample.views.AdminView;
import sample.views.EditUsersView;
import org.controlsfx.control.*;

import java.sql.SQLException;

/**
 * Created by Pavlo on 10.07.2016.
 */
public class AdminController {
    AdminModel adminModel;
    public AdminController() throws SQLException
    {
        adminModel = new AdminModel();
    }
    public static ObservableList<Users> usersData = FXCollections.observableArrayList();
    public static ObservableList<Users> backupUsersData = FXCollections.observableArrayList();
    @FXML
    private TableView<Users> table;
    @FXML
    private TableColumn<Users, String> surname_of_user;
    @FXML
    private TableColumn<Users, String> name_of_user;
    @FXML
    private TableColumn<Users, String> fathername_of_user;
    @FXML
    private TableColumn<Users, Integer> id;
    @FXML
    private TableColumn<Users, String> status_of_user;
    @FXML
    private TableColumn<Users, String> login_of_user;
    @FXML
    private TableColumn<Users, String> password_of_user;

    @FXML
    private Button addUser;
    @FXML
    private Button editUser;
    @FXML
    private Button removeUser;
    @FXML
    private Button searchButton;
    @FXML
    private Button close;
    @FXML
    private TextField search;
    @FXML
    private Label count;
    @FXML
    private Pagination pagination;
    @FXML
    public void search()
    {
        usersData.clear();
        String text = search.getText();
        if (text == "")
        {
            usersData.clear();
            usersData.addAll(backupUsersData);
            backupUsersData.clear();
        }
        for (Users users : backupUsersData)
        {
            if(String.valueOf(users.getId()).contains(text) ||
                    users.getSurname_of_user().toLowerCase().contains(text) ||
                    users.getName_of_user().toLowerCase().contains(text) ||
                    users.getFathername_of_user().toLowerCase().contains(text) ||
                    users.getStatus_of_user().toLowerCase().contains(text) ||
                    users.getLogin_of_user().toLowerCase().contains(text) ||
                    users.getPassword_of_user().toLowerCase().contains(text) ||

                    users.getSurname_of_user().toUpperCase().contains(text) ||
                    users.getName_of_user().toUpperCase().contains(text) ||
                    users.getFathername_of_user().toUpperCase().contains(text) ||
                    users.getStatus_of_user().toUpperCase().contains(text) ||
                    users.getLogin_of_user().toUpperCase().contains(text) ||
                    users.getPassword_of_user().toUpperCase().contains(text) ||

                    users.getSurname_of_user().contains(text) ||
                    users.getName_of_user().contains(text) ||
                    users.getFathername_of_user().contains(text) ||
                    users.getStatus_of_user().contains(text) ||
                    users.getLogin_of_user().contains(text) ||
                    users.getPassword_of_user().contains(text))
            {
                usersData.add(users);
            }
        }
    }
    @FXML
    public void addUser() throws Exception
    {
        AddUsers addUsers = new AddUsers();
        Notifi.notification(Pos.TOP_RIGHT, "Увага!", "У всіх полях, крім логіна та пароля, заборонено використання цифр.");

    }
    @FXML
    public void close()
    {
        CurrentStage.getStage().close();
    }

    @FXML
    public void viewstatistics() throws Exception{
        Statistics statistics = new Statistics();
    }

    @FXML
    public void editUser(ActionEvent event)
    {
        try {
            Users users = (Users) table.getSelectionModel().getSelectedItem();
            EditUsersController.users = users;
            EditUsersView editUsersView = new EditUsersView();
            editUsersView.render();
            Notifi.notification(Pos.TOP_RIGHT, "Увага!", "У всіх полях, крім логіна та пароля, заборонено використання цифр.");
        } catch (Exception ex) {
            Messages.error("Помилка","Не вибрано користувача","TABLE");
        }


    }
    @FXML
    public void deleteUser() throws SQLException
    {
        try {
            Users users = (Users) table.getSelectionModel().getSelectedItem();
            //EditPatientController.patient = patient;
            adminModel.remove(users);
            usersData.remove(users);
            backupUsersData.remove(users);
        } catch (Exception ex) {
            Messages.error("Помилка","Не вибрано пацієнта","TABLE");
        }
    }
    public void updateCount(int counts)
    {
        count.setText(String.valueOf(counts));
    }

    @FXML
    public void initialize() throws Exception{
        usersData.clear();
        id.setCellValueFactory(new PropertyValueFactory<Users, Integer>("id"));
        surname_of_user.setCellValueFactory(new PropertyValueFactory<Users, String>("surname_of_user"));
        name_of_user.setCellValueFactory(new PropertyValueFactory<Users, String>("name_of_user"));
        fathername_of_user.setCellValueFactory(new PropertyValueFactory<Users, String>("fathername_of_user"));
        status_of_user.setCellValueFactory(new PropertyValueFactory<Users, String>("status_of_user"));
        login_of_user.setCellValueFactory(new PropertyValueFactory<Users, String>("login_of_user"));
        password_of_user.setCellValueFactory(new PropertyValueFactory<Users, String>("password_of_user"));
        usersData.addListener(new ListChangeListener<Users>() {
            @Override
            public void onChanged(Change<? extends Users> c){

                updateCount(usersData.size());
                table.getItems().removeAll(backupUsersData);
                table.getItems().addAll(usersData);
            }
        });
        adminModel.selectData();
        pagination.setPageCount(usersData.size() / rowsPerPage + 1);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
        //table.setItems(usersData);
    }
    private final static int dataSize = 10_023;
    private final static int rowsPerPage = 50;
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, usersData.size());
        table.setItems(FXCollections.observableArrayList(usersData.subList(fromIndex, toIndex)));
        return new BorderPane(table);
    }

}
