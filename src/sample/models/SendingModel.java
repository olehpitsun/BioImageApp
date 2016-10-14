package sample.models;

import sample.controllers.SendingController;
import sample.controllers.writeSendingMessageController;
import sample.libs.SQLDatabase;
import sample.libs.Session;
import sample.objects.User.UsersColection;
import sample.objects.Sendings;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by oleh on 18.06.2016.
 */
public class SendingModel extends SQLDatabase {

    private String text, messageDate, send_from_id, surname, name, fathername;
    private int id, send_to_id, counts;

    public SendingModel() throws SQLException
    {
        System.out.println("ssss " + Session.getKeyValue("id"));
        sqlSetConnect();
        sqlExecute("SELECT napravlennya.id, napravlennya.text, napravlennya.date, users.name FROM napravlennya " +
                "INNER JOIN users " +
                "ON " +
                "napravlennya.send_from_id = users.id WHERE napravlennya.send_to_id = " + Session.getKeyValue("id") + " ORDER BY id DESC");
    }

    public void selectData() throws SQLException
    {
        while(resultSet.next()) {
            setData();
            counts++;
            SendingController.messengersData.add(new Sendings(id, text, send_from_id, send_to_id, messageDate));
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

    public boolean isInputValid(String medicalCardNum, String text) {
        String errorMessage = "";

        if (medicalCardNum == null || medicalCardNum.length() == 0) {
            errorMessage += "No valid medicalCardNum!\n";
        }
        if (text == null || text.length() == 0) {
            errorMessage += "No valid text!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Занесення повідомлення в БД
     * @param toId
     * @param medicalCardNum
     * @param text
     * @return
     */
    public boolean insertMessageToDb(int toId, String medicalCardNum, String text){

        Date now = new Date();
        try {
            sqlInsertExecute("INSERT INTO napravlennya (send_from_id, send_to_id, medicalCardNum, text, date) " +
                    "VALUES ("+"'"+Session.getKeyValue("id")+"',"+"'"+toId+"',"+"'"+medicalCardNum+"',"+"'"+text+"',"+"'"+DateFormat.getInstance().format(now)+"')");
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
            writeSendingMessageController.comboBoxData.add(new UsersColection(id, name, name, name));
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
