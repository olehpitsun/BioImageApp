package sample.models;

import sample.controllers.MainWindowController;
import sample.libs.Messenger.Messenger;
import sample.libs.SQLDatabase;
import java.sql.SQLException;

/**
 * Created by oleh on 18.06.2016.
 */
public class MessengerModel extends SQLDatabase {

    private String text;
    private int id, send_from_id, send_to_id, counts;

    public MessengerModel() throws SQLException
    {
        sqlSetConnect();
        sqlExecute("SELECT * FROM messages");
    }

    public void selectData() throws SQLException
    {
        while(resultSet.next()) {
            setData();
            counts++;
            MainWindowController.messengersData.add(new Messenger(id, text, send_from_id, send_to_id));
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
        this.send_from_id = resultSet.getInt("send_from_id");
        this.send_to_id = resultSet.getInt("send_to_id");
    }
}
