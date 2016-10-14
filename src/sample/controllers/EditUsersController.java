package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.models.EditUsersModel;
import sample.objects.User.Users;

/**
 * Автор: Павло Лящинський
 * Дата створення: 10.07.2016.
 * --------------------------
 *
 */

public class EditUsersController {

    @FXML
    private TextField surname_of_user;
    @FXML
    private TextField name_of_user;
    @FXML
    private TextField fathername_of_user;
    @FXML
    private TextField status_of_user;
    @FXML
    private TextField login_of_user;
    @FXML
    private PasswordField password_of_user;

    EditUsersModel editUsersModel;
    public static Users users;


    @FXML
    public void update()
    {
        editUsersModel.setId(users.getId());
        editUsersModel.setSurname_of_user(surname_of_user.getText());
        editUsersModel.setName_of_user(name_of_user.getText());
        editUsersModel.setFathername_of_user(fathername_of_user.getText());
        editUsersModel.setStatus_of_user(status_of_user.getText());
        editUsersModel.setLogin_of_user(login_of_user.getText());
        editUsersModel.setPassword_of_user(password_of_user.getText());
        editUsersModel.addToDB();

    }
    @FXML
    public void initialize(){
        editUsersModel = new EditUsersModel();

        surname_of_user.setText(users.getSurname_of_user());
        name_of_user.setText(users.getName_of_user());
        fathername_of_user.setText(users.getFathername_of_user());
        status_of_user.setText(users.getStatus_of_user());
        login_of_user.setText(users.getLogin_of_user());
        password_of_user.setText(users.getPassword_of_user());
    }

} // class AdClassController
