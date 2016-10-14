package sample.models;

import sample.controllers.AdminController;
import sample.controllers.EditUsersController;
import sample.libs.*;
import sample.objects.User.Users;

/**
 * Created by Petro on 04.05.2016.
 */
public class EditUsersModel extends SQLDatabase {


    private String surname_of_user;
    private String name_of_user;
    private String fathername_of_user;
    private String status_of_user;
    private String login_of_user;
    private String password_of_user;
    private int id;

    public void setId(int id)
    {
        this.id = id;
    }
    public void setStatus(String status)
    {
        this.status_of_user = status;
    }
    public void setSurname_of_user(String surname_of_user) { this.surname_of_user = surname_of_user; }

    public void setName_of_user(String name_of_user) { this.name_of_user = name_of_user; }

    public void setFathername_of_user(String fathername_of_user) { this.fathername_of_user = fathername_of_user; }

    public void setStatus_of_user(String status) {
        this.status_of_user = status;
    }

    public void setLogin_of_user(String login) {
        this.login_of_user = login;
    }

    public void setPassword_of_user(String password) {
        this.password_of_user = password;
    }
    public EditUsersModel()
    {
        sqlSetConnect();
    }
    public void addToDB() {
        if(surname_of_user.isEmpty() || name_of_user.isEmpty() || fathername_of_user.isEmpty() || status_of_user.isEmpty() || login_of_user.isEmpty() || password_of_user.isEmpty()
                && !Regex.checkWithRegex(surname_of_user, "^[a-zA-Zа-яА-Я]+$") ||
                !Regex.checkWithRegex(name_of_user, "^[a-zA-Zа-яА-Я]+$") ||
                !Regex.checkWithRegex(fathername_of_user, "^[a-zA-Zа-яА-Я]+$") ||
                !Regex.checkWithRegex(status_of_user, "^[a-zA-Zа-яА-Я]+$") || !RegExp.checkWithRegExp(login_of_user)) {
            Messages.error("Помилка заповнення!", "Будь ласка, заповніть коректно всі поля!", "Помилка!");
        } else {
            try {

                updateExecute("UPDATE users SET Surname='" + surname_of_user + "', Name='" + name_of_user + "', Fathername='" + fathername_of_user + "', Status='" + status_of_user + "', Login='" + login_of_user + "', Password='" + password_of_user + "' WHERE ID='" + id + "'");
                AdminController.usersData.remove(EditUsersController.users);
                AdminController.backupUsersData.remove(EditUsersController.users);
                AdminController.usersData.add(new Users(id, surname_of_user, name_of_user, fathername_of_user,
                        status_of_user, login_of_user, password_of_user));
                AdminController.backupUsersData.add(new Users(id, surname_of_user, name_of_user, fathername_of_user,
                        status_of_user, login_of_user, password_of_user));
                EventLogger.createEvent(Session.getKeyValue("name "), "User " + surname_of_user + " " + name_of_user + " edited", Date.getTime());
                CurrentStage.getStage().close();
                //database.sqlInsertExecute("INSERT INTO patients VALUES ('2', '', '', '', '', '', '', '', '')");
            } catch (Exception ex) {
                ex.printStackTrace();
                Messages.error("Помилка", "Користувач не може бути доданий!", "БД");
            }
        }


    } // void addToDB

}
