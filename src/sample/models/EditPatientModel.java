package sample.models;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import sample.controllers.EditPatientController;
import sample.controllers.PatientsController;
import sample.libs.*;
import sample.objects.Patient;

/**
 * Created by Petro on 04.05.2016.
 */
public class EditPatientModel extends SQLDatabase {


    private String surname_of_patient;
    private String name_of_patient;
    private String fathername_of_patient;
    private String date_of_birth;
    private String gender;
    private String results_of_research;
    private String diagnosis;
    private String date_of_completion;
    private String doctor_id;
    private String status, medical_card;
    private int id;

    public void setId(int id)
    {
        this.id = id;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
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

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
    public void setMedical_card(String medical_card1) {this.medical_card = medical_card1;}
    public EditPatientModel()
    {
        sqlSetConnect();
    }
    public void addToDB() {

        if(surname_of_patient.isEmpty() || name_of_patient.isEmpty() || fathername_of_patient.isEmpty() || date_of_birth.isEmpty() || results_of_research.isEmpty() || diagnosis.isEmpty()
                || !Regex.checkWithRegex(surname_of_patient, "^[a-zA-Zа-яА-Я\\s]+$") || !Regex.checkWithRegex(name_of_patient, "^[a-zA-Zа-яА-Я\\s]+$") || !Regex.checkWithRegex(fathername_of_patient, "^[a-zA-Zа-яА-Я\\s]+$") ||
                !Regex.checkWithRegex(date_of_birth, "^[0-9.]+$") ||
                !Regex.checkWithRegex(results_of_research, "^[a-zA-Zа-яА-Я0-9\\s.,!;?]+$") ||
                !Regex.checkWithRegex(diagnosis, "^[a-zA-Zа-яА-Я0-9.,!;?\\s]+$")) {
            Messages.error("Помилка заповнення!", "Будь ласка, заповніть коректно всі поля!", "Помилка!");
        } else {
            try {
                status = EditPatientController.patient.getStatus();
                updateExecute("UPDATE patients SET Status='" + status + "', Surname='" + surname_of_patient + "', Name='" + name_of_patient + "', Fathername='" + fathername_of_patient + "', Date_of_birth='" + date_of_birth + "', Gender='" + gender + "', Results_of_research='" + results_of_research + "', Diagnosis='" + diagnosis + "', Date_of_completion='" + date_of_completion + "', doctor_id='" + doctor_id + "' WHERE ID='" + id + "'");
                PatientsController.patientsData.remove(EditPatientController.patient);
                PatientsController.backupPatientsData.remove(EditPatientController.patient);
                PatientsController.patientsData.add(new Patient(id, surname_of_patient, name_of_patient, fathername_of_patient, date_of_birth,
                        gender,
                        results_of_research, diagnosis,
                        date_of_completion, doctor_id, status,medical_card));
                PatientsController.backupPatientsData.add(new Patient(id, surname_of_patient, name_of_patient, fathername_of_patient, date_of_birth,
                        gender,
                        results_of_research, diagnosis,
                        date_of_completion, doctor_id, status, medical_card));
                EventLogger.createEvent(Session.getKeyValue("name"), "Patient " + surname_of_patient + " " + name_of_patient + " edited", Date.getTime());
                CurrentStage.getStage().close();
                //database.sqlInsertExecute("INSERT INTO patients VALUES ('2', '', '', '', '', '', '', '', '')");
            } catch (Exception ex) {
                ex.printStackTrace();
                Messages.error("Помилка", "Пацієнт не може бути доданий!", "БД");
            }
        }


    } // void addToDB

}
