package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.libs.CurrentStage;
import sample.libs.Messages;
import sample.models.AdminModel;
import sample.models.StatisticsModel;
import sample.nodes.AddUsers;
import sample.nodes.Statistics;
import sample.objects.StatisticsList;
import sample.objects.Users;
import sample.views.AddUsersView;
import sample.views.AdminView;
import sample.views.EditUsersView;

import java.sql.SQLException;

/**
 * Created by Pavlo on 22.07.2016.
 */
public class StatisticsController {
    StatisticsModel statisticsModel;
    public StatisticsController() throws SQLException
    {
        statisticsModel = new StatisticsModel();
    }
    public static ObservableList<StatisticsList> statisticsData = FXCollections.observableArrayList();
    public static ObservableList<StatisticsList> backupStatisticsData = FXCollections.observableArrayList();
    @FXML
    private TableView<StatisticsList> table;
    @FXML
    private TableColumn<StatisticsList, Integer> id;
    @FXML
    private TableColumn<StatisticsList, String> user;
    @FXML
    private TableColumn<StatisticsList, String> event;
    @FXML
    private TableColumn<StatisticsList, String> date;

    @FXML
    private Button searchButton;
    @FXML
    private Button close;
    @FXML
    private TextField search;
    @FXML
    private Label count;
    @FXML
    public void search()
    {
        statisticsData.clear();
        String text = search.getText();
        if (text == "")
        {
            statisticsData.clear();
            statisticsData.addAll(backupStatisticsData);
            backupStatisticsData.clear();
        }
        for (StatisticsList statisticsList : backupStatisticsData)
        {
            if(String.valueOf(statisticsList.getId()).contains(text) ||
                    statisticsList.getUser().toLowerCase().contains(text) ||
                    statisticsList.getEvent().toLowerCase().contains(text) ||
                    statisticsList.getDate().toLowerCase().contains(text) ||

                    statisticsList.getUser().toUpperCase().contains(text) ||
                    statisticsList.getEvent().toUpperCase().contains(text) ||
                    statisticsList.getDate().toUpperCase().contains(text) ||

                    statisticsList.getUser().contains(text) ||
                    statisticsList.getEvent().contains(text) ||
                    statisticsList.getDate().contains(text))
            {
                statisticsData.add(statisticsList);
            }
        }
    }

    @FXML
    public void close()
    {
        CurrentStage.getOwnerStage().close();
    }


    public void updateCount(int counts)
    {
        count.setText(String.valueOf(counts));
    }

    @FXML
    public void initialize() throws Exception{
        statisticsData.clear();
        id.setCellValueFactory(new PropertyValueFactory<StatisticsList, Integer>("id"));
        user.setCellValueFactory(new PropertyValueFactory<StatisticsList, String>("user"));
        event.setCellValueFactory(new PropertyValueFactory<StatisticsList, String>("event"));
        date.setCellValueFactory(new PropertyValueFactory<StatisticsList, String>("date"));
        statisticsData.addListener(new ListChangeListener<StatisticsList>() {
            @Override
            public void onChanged(Change<? extends StatisticsList> c){
                updateCount(statisticsData.size());
            }
        });
        statisticsModel.selectData();
        table.setItems(statisticsData);

    }

}
