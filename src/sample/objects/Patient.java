package sample.objects;

/**
 * Created by Admin on 04.05.2016.
 */
public class Patient {
    private String surname_of_patient;
    private String name_of_patient;
    private String fathername_of_patient;
    private String date_of_birth;
    private String gender;
    private String results_of_research;
    private String diagnosis;
    private String date_of_completion;
    private String full_name_of_doctor;
    private String status;
    private String medical_card;
    private int id;

    public Patient(int id, String surname, String name, String fathername, String birth, String gender,
                String research, String diagnosis, String completion,
                String doctor, String status, String medical_card) {
        this.surname_of_patient = surname;
        this.name_of_patient = name;
        this.fathername_of_patient = fathername;
        this.id = id;
        this.date_of_birth = birth;
        this.gender = gender;
        this.results_of_research = research;
        this.diagnosis = diagnosis;
        this.date_of_completion = completion;
        this.full_name_of_doctor = doctor;
        this.status = status;
        this.medical_card = medical_card;
    }
    public void setSurname_of_patient(String surname) {this.surname_of_patient = surname;}
    public void setName_of_patient(String name) {this.name_of_patient = name;}
    public void setFathername_of_patient(String fathername) {this.fathername_of_patient = fathername;}
    public void setStatus(String status) {this.status = status;}
    public void setId(int id) {this.id = id;}
    public void setDate_of_birth(String date_of_birth) { this.date_of_birth = date_of_birth; }
    public void setGender(String gender) {this.gender = gender;}
    public void setResults_of_research(String results_of_research) {
        this.results_of_research = results_of_research;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void setDate_of_completion(String date_of_completion) {
        this.date_of_completion = date_of_completion;
    }
    public void setFull_name_of_doctor(String full_name_of_doctor) {
        this.full_name_of_doctor = full_name_of_doctor;
    }
    public void setMedical_card(String medical_card){ this.medical_card = medical_card;}

    public String getSurname_of_patient() {return this.surname_of_patient;}
    public String getName_of_patient() {return this.name_of_patient;}
    public String getFathername_of_patient() {return this.fathername_of_patient;}
    public int getId() {
        return this.id;
    }
    public String getStatus() {return this.status;}
    public String getDate_of_birth() { return this.date_of_birth; }
    public String getGender() {return this.gender;}
    public String getResults_of_research() {return this.results_of_research;}
    public String getDiagnosis() {return this.diagnosis;}
    public String getDate_of_completion() {return this.date_of_completion;}
    public String getFull_name_of_doctor() {return this.full_name_of_doctor;}
    public String getMedical_card(){return this.medical_card;}
}
