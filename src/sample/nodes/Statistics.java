package sample.nodes;


import sample.views.StatisticsView;

/**
 * Created by Pavlo on 22.07.2016.
 */
public class Statistics {
    StatisticsView statisticsView;

    public Statistics() throws Exception{
        statisticsView = new StatisticsView();
        statisticsView.render();
    }
}
