package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.models.AddPatientModel;
import sample.models.AddUsersModel;
import sample.objects.Patient;
import sample.objects.Users;

/**
 * Created by user1 on 10.07.2016.
 */
public class AddUsersController {
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
    private Users users;
    AddUsersModel addUsersModel;


    @FXML
    public void addAUserToDB(){
        addUsersModel = new AddUsersModel();
        addUsersModel.setSurname_of_user(surname_of_user.getText());
        addUsersModel.setName_of_user(name_of_user.getText());
        addUsersModel.setFathername_of_user(fathername_of_user.getText());
        addUsersModel.setStatus_of_user(status_of_user.getText());
        addUsersModel.setLogin_of_user(login_of_user.getText());
        addUsersModel.setPassword_of_user(password_of_user.getText());
        addUsersModel.addToDB();
    }

}
