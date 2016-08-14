package sample.libs;

/**
 * Created by oleh on 10.07.2016.
 */
public class PatientCollection {
    private int id;
    private String surname, name, fathername, medical_card;
    public PatientCollection(int id, String medical_card, String surname, String name, String fathername){
        this.id = id;
        this.medical_card = medical_card;
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
    public void setMedical_card(String medical_card1){this.medical_card = medical_card1;}
    public String getMedical_card(){return  this.medical_card;}
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
        return id + " " + medical_card + " " + surname + " " + name + " " + fathername ;
    }

}
