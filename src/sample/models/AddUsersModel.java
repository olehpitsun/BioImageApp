package sample.models;

import sample.controllers.AdminController;
import sample.libs.*;
import sample.objects.User.Users;

import java.util.Random;

/**
 * Created by user1 on 10.07.2016.
 */
public class AddUsersModel extends SQLDatabase{
    private String surname_of_user;
    private String name_of_user;
    private String fathername_of_user;
    private String status_of_user;
    private String login_of_user;
    private String password_of_user;
    private String salt;



    public void setStatus(String status)
    {
        this.status_of_user = status;
    }
    public void setSurname_of_user(String surname_of_patient) { this.surname_of_user = surname_of_patient; }

    public void setName_of_user(String name_of_patient) { this.name_of_user = name_of_patient; }

    public void setFathername_of_user(String fathername_of_patient) { this.fathername_of_user = fathername_of_patient; }

    public void setStatus_of_user(String status) {
        this.status_of_user = status;
    }

    public void setLogin_of_user(String login) {
        this.login_of_user = login;
    }

    public void setPassword_of_user(String password) {
        this.password_of_user = password;
    }
    public AddUsersModel()
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
                String temppass = password_of_user;
                salt = Hash.generateSalt(32);
                password_of_user = Hash.hash(temppass, salt);
                Random random = new Random();
                sqlInsertExecute("INSERT INTO users (Surname, Name, Fathername, Status, Login, Password, keymap, role_id) VALUES ('" + surname_of_user + "','" + name_of_user + "','" + fathername_of_user + "','" + status_of_user + "','" + login_of_user + "','" + password_of_user + "','" + salt + "','" + random.nextInt(10) + "')");
                sqlExecute("SELECT id, status FROM users WHERE Surname='" + surname_of_user + "' AND Name='" + name_of_user + "' AND Fathername='" + fathername_of_user + "'");
                if (resultSet.next()) {
                    AdminController.usersData.add(new Users(Integer.valueOf(resultSet.getString("id")), surname_of_user, name_of_user, fathername_of_user,
                            status_of_user, login_of_user, password_of_user));
                    AdminController.backupUsersData.add(new Users(Integer.valueOf(resultSet.getString("id")), surname_of_user, name_of_user, fathername_of_user,
                            status_of_user, login_of_user, password_of_user));
                    EventLogger.createEvent(Session.getKeyValue("name "), "User " + surname_of_user + " " + name_of_user + " edited", Date.getTime());
                }

                CurrentStage.getOwnerStage().close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Messages.error("Помилка", "Користувач не може бути доданий!", "БД");
            }
        }


    } // void addToDB
}
