package sample.models;

import sample.controllers.StatisticsController;
import sample.libs.SQLDatabase;
import sample.objects.StatisticsList;

import java.sql.SQLException;

/**
 * Created by Pavlo on 22.07.2016.
 */
public class StatisticsModel extends SQLDatabase{
    private String user;
    private String event;
    private String date;
    private int id;
    public int counts;
    public StatisticsModel() throws SQLException
    {
        sqlSetConnect();
        sqlExecute("SELECT * FROM logs ORDER BY id DESC");
    }

    public void setData() throws SQLException
    {
        this.user = resultSet.getString("User");
        this.event = resultSet.getString("Event");
        this.date = resultSet.getString("Date");
        this.id = Integer.valueOf(resultSet.getString("ID"));
    }
    public void selectData() throws SQLException
    {
        StatisticsController.statisticsData.clear();
        while(resultSet.next()) {
            setData();
            counts++;
            StatisticsController.statisticsData.add(new StatisticsList(id, user, event, date));
        }
        StatisticsController.backupStatisticsData.clear();
        StatisticsController.backupStatisticsData.addAll(StatisticsController.statisticsData);
    }

    public int getCounts()
    {
        return counts;
    }

}
