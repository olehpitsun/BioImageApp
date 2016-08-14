package sample.models;

import sample.controllers.MainWindowController;
import sample.controllers.writeMessageController;
import sample.libs.Messenger.Messenger;
import sample.libs.SQLDatabase;
import sample.libs.Session;
import sample.libs.UsersColection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by oleh on 18.06.2016.
 */
public class MessengerModel extends SQLDatabase {

    private String text, messageDate, send_from_id, surname, name, fathername;
    private int id, send_to_id, counts;

    public MessengerModel() throws SQLException
    {
        sqlSetConnect();
        sqlExecute("SELECT messages.id, messages.text, messages.date, users.name FROM messages " +
                "INNER JOIN users " +
                "ON " +
                "messages.send_from_id = users.id WHERE send_to_id = " + Session.getKeyValue("id") + " ORDER BY id DESC");
    }

    public void selectData() throws SQLException
    {
        while(resultSet.next()) {
            setData();
            counts++;
            MainWindowController.messengersData.add(new Messenger(id, text, send_from_id, send_to_id, messageDate));
        }
    }

    public int getCounts()
    {
        return counts;
    }

    public void setData() throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.text = resultSet.getString("text");
        this.send_from_id = resultSet.getString("name");
        this.messageDate = resultSet.getString("date");
    }

    public boolean isInputValid(String title, String text) {
        String errorMessage = "";

        if (title == null || title.length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (text == null || text.length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Занесенняповідомлення в БД
     * @param toId
     * @param title
     * @param text
     * @return
     */
    public boolean insertMessageToDb(int toId, String title, String text){

        Date now = new Date();
        try {
            sqlInsertExecute("INSERT INTO messages (send_from_id, send_to_id, title, text, date) " +
                    "VALUES ("+"'"+Session.getKeyValue("id")+"',"+"'"+toId+"',"+"'"+title+"',"+"'"+text+"',"+"'"+DateFormat.getInstance().format(now)+"')");
        return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void selectUsers() throws SQLException
    {
        sqlExecute("SELECT * FROM users");
        while(resultSet.next()) {
            setUsersData();
            counts++;
            writeMessageController.comboBoxData.add(new UsersColection(id, name, name, name));
        }
    }

    public void setUsersData() throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.surname = resultSet.getString("name");
        this.name = resultSet.getString("name");
        this.fathername = resultSet.getString("name");
    }
}
