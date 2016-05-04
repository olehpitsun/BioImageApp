package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import sample.libs.Messages;
import sample.models.AddPatientModel;
import sample.models.PatientsModel;
import sample.objects.Patient;

/**
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * --------------------------
 * Клас є контролером для модуля додавання пацієнтів у систему.
 */

public class AddPatientClassController {

    @FXML
    private TextField full_name_of_patient;
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
    private TextField date_of_completion;
    @FXML
    private TextField full_name_of_doctor;
    private Patient patient;
    AddPatientModel addPatientModel;

    @FXML
    private ToggleGroup Gender;
    public void addAPatientInDB(){
        addPatientModel = new AddPatientModel();
        addPatientModel.setFull_name_of_patient(full_name_of_patient.getText());
        addPatientModel.setDate_of_birth(date_of_birth.getText());
        addPatientModel.setGender(Gender);
        addPatientModel.setResults_of_researsh(results_of_research.getText());
        addPatientModel.setDiagnosis(diagnosis.getText());
        addPatientModel.setDate_of_completion(date_of_completion.getText());
        addPatientModel.setFull_name_of_doctor(full_name_of_doctor.getText());
        addPatientModel.addToDB();

    }

} // class AdClassController
