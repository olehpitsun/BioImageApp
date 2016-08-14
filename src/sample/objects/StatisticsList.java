package sample.objects;

/**
 * Created by Pavlo on 22.07.2016.
 */
public class StatisticsList {
    private String user;
    private String event;
    private String date;
    private int id;

    public StatisticsList(int id, String name, String event, String date) {
        this.user = name;
        this.event = event;
        this.date = date;
        this.id = id;
    }

    public void setUser(String name) {
        this.user = name;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUser() {
        return this.user;
    }

    public String getEvent() {
        return this.event;
    }

    public String getDate() {
        return this.date;
    }

    public int getId() {
        return this.id;
    }

}
