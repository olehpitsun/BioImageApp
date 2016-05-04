package sample.models;


import sample.libs.*;
import sample.nodes.MainApp;
import java.sql.SQLException;

public class AuthModel extends SQLDatabase {


    public AuthModel()
    {
        sqlSetConnect();
    }

    private String login;
    private String password;

    public void setLogin(String login)
    {
        this.login = login;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void comparePassword() throws SQLException
    {
        sqlExecute("SELECT keymap FROM users WHERE login='" + login + "'");

        if(resultSet.next())
        {
            password = Hash.hash(password, resultSet.getString("keymap"));
        }
    }
    public void signingIn() throws SQLException, Exception
    {
        if (!RegExp.checkWithRegExp(login))
        {
            Messages.information("Please, check your login", "It must have more than 6 characters", "Перевірка даних");
        } else {
            sqlExecute("SELECT * FROM users WHERE login='" + login + "' AND pass='" + password + "'");


            if(!resultSet.next())
            {
                Messages.error("Wrong password or login", "Please, check it and try again", "Перевірка даних");
            }
            else {
                Session.setKeyValue("id", resultSet.getString("id"));
                Session.setKeyValue("name", resultSet.getString("name"));

                System.out.println(Session.getKeyValue("id"));
                System.out.println(Session.getKeyValue("name"));

                //MainApp mainApp = new MainApp();

            }

        }
    }


}
