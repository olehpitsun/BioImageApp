package sample.models;

import sample.controllers.PatientsController;
import sample.libs.Date;
import sample.libs.EventLogger;
import sample.libs.SQLDatabase;
import sample.libs.Session;
import sample.objects.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Admin on 04.05.2016.
 */
public class PatientsModel extends SQLDatabase {
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
    private int id;
    public int counts;
    public PatientsModel() throws SQLException
    {
        sqlSetConnect();
        sqlExecute("SELECT * FROM patients");
    }
    public void remove(Patient patient) throws SQLException
    {
        removeExecute("DELETE FROM patients WHERE ID='"+patient.getId()+"'");
        EventLogger.createEvent(Session.getKeyValue("name"), "Patient " +patient.getSurname_of_patient() + " " + patient.getName_of_patient() +" deleted", Date.getTime());
    }
    public void setData() throws SQLException
    {
            this.surname_of_patient = resultSet.getString("Surname");
            this.name_of_patient = resultSet.getString("Name");
            this.fathername_of_patient = resultSet.getString("Fathername");
            this.id = Integer.valueOf(resultSet.getString("ID"));
            this.date_of_birth = resultSet.getString("Date_of_birth");
            this.gender = resultSet.getString("Gender");
            this.results_of_research = resultSet.getString("Results_of_research");
            this.diagnosis = resultSet.getString("Diagnosis");
            this.date_of_completion = resultSet.getString("Date_of_completion");
            this.full_name_of_doctor = resultSet.getString("Name_of_doctor");
            this.status = resultSet.getString("Status");
    }
    public void selectData() throws SQLException
    {
        PatientsController.patientsData.clear();
        while(resultSet.next()) {
            setData();
            counts++;
            PatientsController.patientsData.add(new Patient(id, surname_of_patient, name_of_patient, fathername_of_patient, date_of_birth,
                    gender,
                    results_of_research, diagnosis,
                    date_of_completion, full_name_of_doctor, status));
        }
        PatientsController.backupPatientsData.clear();
        PatientsController.backupPatientsData.addAll(PatientsController.patientsData);
    }

    public int getCounts()
    {
        return counts;
    }

}
