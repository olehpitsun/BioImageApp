package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import sample.libs.Date;
import sample.libs.Messages;
import sample.libs.Session;
import sample.models.AddPatientModel;
import sample.models.EditPatientModel;
import sample.objects.Patient;

/**
 * Автор: Павло Лящинський
 * Дата створення: 04.05.2016.
 * --------------------------
 *
 */

public class EditPatientController {

    @FXML
    private TextField surname_of_patient;
    @FXML
    private TextField name_of_patient;
    @FXML
    private TextField fathername_of_patient;
    @FXML
    private TextField date_of_birth;
    @FXML
    private RadioButton male_gender;
    @FXML
    private RadioButton female_gender;
    @FXML
    private TextArea results_of_research;
    @FXML
    private TextArea diagnosis;
    @FXML
    private String date_of_completion;

    private String doctor_id;


    public static Patient patient;

    @FXML
    private ToggleGroup Gender;
    @FXML
    private TextField medical_card_of_patient;

    EditPatientModel editPatientModel;


    @FXML
    public void update()
    {
        editPatientModel.setSurname_of_patient(surname_of_patient.getText());
        editPatientModel.setName_of_patient(name_of_patient.getText());
        editPatientModel.setFathername_of_patient(fathername_of_patient.getText());
        editPatientModel.setDate_of_birth(date_of_birth.getText());
        editPatientModel.setGender(Gender);
        editPatientModel.setId(patient.getId());
        editPatientModel.setResults_of_researsh(results_of_research.getText());
        editPatientModel.setDiagnosis(diagnosis.getText());
        editPatientModel.setDate_of_completion(Date.getTime());
        editPatientModel.setDoctor_id(Session.getKeyValue("id"));
        editPatientModel.setMedical_card_of_patient(medical_card_of_patient.getText());
        editPatientModel.addToDB();

    }
    @FXML
    public void initialize(){
        editPatientModel = new EditPatientModel();
        if(patient.getGender().equals("Чоловік"))
        {
            male_gender.setSelected(true);
        } else female_gender.setSelected(true);
        surname_of_patient.setText(patient.getSurname_of_patient());
        name_of_patient.setText(patient.getName_of_patient());
        fathername_of_patient.setText(patient.getFathername_of_patient());
        date_of_birth.setText(patient.getDate_of_birth());
        results_of_research.setText(patient.getResults_of_research());
        diagnosis.setText(patient.getDiagnosis());
        date_of_completion = Date.getTime();
        doctor_id = Session.getKeyValue("id");
        medical_card_of_patient.setText(patient.getMedical_card());
    }

} // class AdClassController
