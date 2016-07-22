package sample.models;


import sample.libs.*;
import sample.nodes.Admin;
import sample.nodes.MainApp;
import java.sql.SQLException;

public class AuthModel extends SQLDatabase {


    public AuthModel()
    {
        sqlSetConnect();
        Session.setKeyValue("activeStatus", "0");
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
        sqlExecute("SELECT keymap FROM users WHERE Login='" + login + "'");

        if(resultSet.next())
        {
            password = Hash.hash(password, resultSet.getString("keymap"));
        }
    }
    public void signingIn() throws SQLException, Exception
    {
        if (!RegExp.checkWithRegExp(login))
        {
            Messages.information("Будь ласка, перевірте свої логін та пароль!", "Вони повинні містити більше 6-ти символів.", "Перевірка даних");
        } else {
            sqlExecute("SELECT * FROM users WHERE Login='" + login + "' AND Password='" + password + "'");


            if(!resultSet.next())
            {
                Messages.error("Неправильний логін або пароль!", "Будь ласка, перевірте їх і спробуйте знову.", "Перевірка даних");
            }
            else {
                if (resultSet.getString("Status").compareTo("Administrator") == 0) {
                    Admin admin = new Admin();
                }
                Session.setKeyValue("activeStatus", "1");
                Session.setKeyValue("id", resultSet.getString("id"));
                Session.setKeyValue("name", resultSet.getString("name"));
                Session.setKeyValue("role_id", resultSet.getString("role_id"));

                System.out.println(Session.getKeyValue("id"));
                System.out.println(Session.getKeyValue("name"));
                System.out.println(Session.getKeyValue("role_id"));
                EventLogger.createEvent(Session.getKeyValue("name"), "Authorization complete", Date.getTime());

               // MainApp mainApp = new MainApp();

            }

        }
    }


}
