package sample.models;

import sample.controllers.AdminController;
import sample.controllers.PatientsController;
import sample.libs.Date;
import sample.libs.EventLogger;
import sample.libs.SQLDatabase;
import sample.libs.Session;
import sample.objects.User.Users;

import java.sql.SQLException;

/**
 * Created by Pavlo on 10.07.2016.
 */
public class AdminModel extends SQLDatabase{
    private String surname_of_user;
    private String name_of_user;
    private String fathername_of_user;
    private String status_of_user;
    private String login_of_user;
    private String password_of_user;
    private int id;
    public int counts;
    public AdminModel() throws SQLException
    {
        sqlSetConnect();
        sqlExecute("SELECT * FROM users");
    }
    public void remove(Users users) throws SQLException
    {
        removeExecute("DELETE FROM users WHERE ID='"+users.getId()+"'");
        EventLogger.createEvent(Session.getKeyValue("name "), "User " +users.getSurname_of_user() + " " + users.getName_of_user() +" deleted", Date.getTime());
    }
    public void setData() throws SQLException
    {
        this.surname_of_user = resultSet.getString("Surname");
        this.name_of_user = resultSet.getString("Name");
        this.fathername_of_user = resultSet.getString("Fathername");
        this.id = Integer.valueOf(resultSet.getString("ID"));
        this.status_of_user = resultSet.getString("Status");
        this.login_of_user = resultSet.getString("Login");
        this.password_of_user = resultSet.getString("Password");
    }
    public void selectData() throws SQLException
    {
        PatientsController.patientsData.clear();
        while(resultSet.next()) {
            setData();
            counts++;
            AdminController.usersData.add(new Users(id, surname_of_user, name_of_user, fathername_of_user,
                    status_of_user,
                    login_of_user, password_of_user));
        }
        AdminController.backupUsersData.clear();
        AdminController.backupUsersData.addAll(AdminController.usersData);
    }
    public int getCounts()
    {
        return counts;
    }

}
