package sample.libs;

import java.sql.SQLException;

/**
 * Created by Petro
 */
public class EventLogger extends SQLDatabase {
    public static void createEvent(String user, String event, String date) throws SQLException
    {
       CreateEvent("INSERT INTO logs (user, event, date) VALUES ('"+Session.getKeyValue("name")+"', '"+event+"', '"+Date.getTime()+"')");
    }
}
