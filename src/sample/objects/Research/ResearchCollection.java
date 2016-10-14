package sample.objects.Research;

/**
 * Created by oleh on 10.07.2016.
 */
public class ResearchCollection {
    private int id;
    private String name, num_glass;
    public ResearchCollection(int id, String name, String num_glass){
        this.id = id;
        this.name = name;
        this.num_glass = num_glass;
    }

    public void setId(int id1){
        this.id = id1;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name1){
        this.name = name1;
    }
    public String getName(){
        return this.name;
    }
    public void setNum_glass (String num_glass1){
        this.num_glass = num_glass1;
    }
    public String getNum_glass(){
        return this.num_glass;
    }
    @Override
    public String toString() {
        return id + " " + name + " " + num_glass;
    }

}
