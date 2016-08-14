package sample.objects;

/**
 * Created by Pavlo on 10.07.2016.
 */
public class Users {
    private String surname_of_user;
    private String name_of_user;
    private String fathername_of_user;
    private String status_of_user;
    private String login_of_user;
    private String password_of_user;
    private int id;

    public Users(int id, String surname, String name, String fathername, String status, String login,
                 String password) {
        this.surname_of_user = surname;
        this.name_of_user = name;
        this.fathername_of_user = fathername;
        this.status_of_user = status;
        this.login_of_user = login;
        this.password_of_user = password;
        this.id = id;
    }

    public void setSurname_of_user(String surname) {
        this.surname_of_user = surname;
    }

    public void setName_of_user(String name) {
        this.name_of_user = name;
    }

    public void setFathername_of_user(String fathername) {
        this.fathername_of_user = fathername;
    }

    public void setStatus_of_user(String status) {
        this.status_of_user = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin_of_user(String login) {
        this.login_of_user = login;
    }

    public void setPassword_of_user(String password) {
        this.password_of_user = password;
    }


    public String getSurname_of_user() {
        return this.surname_of_user;
    }

    public String getName_of_user() {
        return this.name_of_user;
    }

    public String getFathername_of_user() {
        return this.fathername_of_user;
    }

    public int getId() {
        return this.id;
    }

    public String getStatus_of_user() {
        return this.status_of_user;
    }

    public String getLogin_of_user() {
        return this.login_of_user;
    }

    public String getPassword_of_user() {
        return this.password_of_user;
    }
}
