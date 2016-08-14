package sample.libs;

/**
 * Created by oleh on 02.07.2016.
 */
public class UsersColection {

    private int id;
    private String surname, name, fathername;

    public UsersColection(int id, String surname, String name, String fathername){
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.fathername = fathername;
    }

    public void setId(int id1){
        this.id = id1;
    }
    public int getId(){
        return this.id;
    }
    public void setSurname(String surname1){
        this.surname = surname1;
    }
    public String getSurname(){
        return this.surname;
    }
    public void setName(String name1){
        this.name = name1;
    }
    public String getName(){
        return this.name;
    }
    public void setFathername(String fathername1){
        this.fathername = fathername1;
    }
    public String getFathername(){
        return this.fathername;
    }
    @Override
    public String toString() {
        return id + " " + surname + " " + name + " " + fathername ;
    }
}
