package sample.models;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import sample.controllers.PatientsController;
import sample.libs.*;
import sample.objects.Patient;

import java.sql.ResultSet;

/**
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * --------------------------
 * Клас є моделлю для модуля додавання пацієнтів у систему.
 */

public class AddPatientModel extends  SQLDatabase{


    private String surname_of_patient;
    private String name_of_patient;
    private String fathername_of_patient;
    private String date_of_birth;
    private String gender;
    private String results_of_research;
    private String diagnosis;
    private String date_of_completion;
    private String full_name_of_doctor;


    public void setSurname_of_patient(String surname_of_patient) { this.surname_of_patient = surname_of_patient; }

    public void setName_of_patient(String name_of_patient) { this.name_of_patient = name_of_patient; }

    public void setFathername_of_patient(String fathername_of_patient) { this.fathername_of_patient = fathername_of_patient; }

    public void setDate_of_birth(String date_of_birth) { this.date_of_birth = date_of_birth; }

    public void setGender(ToggleGroup toggle) {
                RadioButton chk = (RadioButton)toggle.getSelectedToggle();
                gender = chk.getText();
    }

    public void setResults_of_researsh(String results_of_research) {
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
    public AddPatientModel()
    {
        sqlSetConnect();
    }
    public void addToDB() {
        try {

            sqlInsertExecute("INSERT INTO patients (Status, Surname, Name, Fathername, Date_of_birth, Gender, Results_of_research, Diagnosis, Date_of_completion, Name_of_doctor) VALUES ("+"'Patient',"+"'"+surname_of_patient+"',"+"'"+name_of_patient+"',"+"'"+fathername_of_patient+"',"+"'"+date_of_birth+"',"+"'"+gender+"',"+"'"+results_of_research+"',"+"'"+diagnosis+"',"+"'"+date_of_completion+"',"+"'"+full_name_of_doctor+"')");
            sqlExecute("SELECT id, status FROM patients WHERE Surname='"+surname_of_patient+"' AND Name='"+name_of_patient+"' AND Fathername='"+fathername_of_patient+"' AND Date_of_birth='"+date_of_birth+"'");
            if(resultSet.next()) {
                PatientsController.patientsData.add(new Patient(Integer.valueOf(resultSet.getString("ID")), surname_of_patient, name_of_patient, fathername_of_patient, date_of_birth,
                        gender,
                        results_of_research, diagnosis,
                        date_of_completion, full_name_of_doctor, resultSet.getString("Status")));
                PatientsController.backupPatientsData.add(new Patient(Integer.valueOf(resultSet.getString("ID")), surname_of_patient, name_of_patient, fathername_of_patient, date_of_birth,
                        gender,
                        results_of_research, diagnosis,
                        date_of_completion, full_name_of_doctor, resultSet.getString("Status")));
                //database.sqlInsertExecute("INSERT INTO patients VALUES ('2', '', '', '', '', '', '', '', '')");
                EventLogger.createEvent(Session.getKeyValue("name"), "Added patient" + surname_of_patient + " " + name_of_patient, Date.getTime());
            }

            CurrentStage.getStage().close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.error("Помилка", "Пацієнт не може бути доданий!", "БД");
        }


    } // void addToDB

}
