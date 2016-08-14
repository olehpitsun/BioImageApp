package sample.libs.SimpleResearch;

/**
 * Created by oleh on 05.05.2016.
 */
public class SimpleResearchCollection {

    private String id;
    private String name;

    public SimpleResearchCollection(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name1) {
        this.name = name1;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }

}
