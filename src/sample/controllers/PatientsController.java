package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.interfaces.PatientsBook;
import sample.libs.CurrentStage;
import sample.libs.SQLDatabase;
import sample.models.AddPatientModel;
import sample.models.EditPatientModel;
import sample.models.PatientsModel;
import sample.nodes.AddPatientModule;
import sample.nodes.Patients;
import sample.objects.Patient;
import sample.views.AddPatientView;
import sample.views.EditPatientView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Petro on 04.05.2016.
 */
public class PatientsController {
    PatientsModel patientsModel;
    public PatientsController() throws SQLException
    {
        patientsModel = new PatientsModel();
    }
    public static ObservableList<Patient> patientsData = FXCollections.observableArrayList();
    @FXML
    private TableView<Patient> table;
    @FXML
    private TableColumn<Patient, String> name;
    @FXML
    private TableColumn<Patient, Integer> id;
    @FXML
    private TableColumn<Patient, String> birth;
    @FXML
    private TableColumn<Patient, String> status;
    @FXML
    private TableColumn<Patient, String> gender;
    @FXML
    private TableColumn<Patient, String> diagnosis;
    @FXML
    private TableColumn<Patient, String> research;
    @FXML
    private TableColumn<Patient, String> completion;
    @FXML
    private TableColumn<Patient, String> doctor;
    @FXML
    private Button addPatient;
    @FXML
    private Button editPatient;
    @FXML
    private Button removePatient;
    @FXML
    private Button searchButton;
    @FXML
    private Button close;
    @FXML
    private TextField search;
    @FXML
    private Label count;

    @FXML
    public void addPatient() throws Exception
    {
        AddPatientView addPatientView = new AddPatientView();
        addPatientView.render();
    }
    @FXML
    public void close()
    {
        CurrentStage.getOwnerStage().close();
    }
    @FXML
    public void editPatient(ActionEvent event)
    {
        Patient patient = (Patient) table.getSelectionModel().getSelectedItem();
        EditPatientController.patient = patient;
        EditPatientView editPatientView = new EditPatientView();
        try {
            editPatientView.render();
        } catch (Exception e) {e.printStackTrace();}


    }
    @FXML
    public void deletePatient()
    {
        Patient patient = (Patient) table.getSelectionModel().getSelectedItem();
        EditPatientController.patient = patient;
        patientsModel.remove(patient);
        patientsData.remove(patient);
    }
    public void updateCount(int counts)
    {
        count.setText(String.valueOf(counts));
    }

    @FXML
    public void initialize() throws Exception{
        patientsData.clear();
        id.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Patient, String>("full_name_of_patient"));
        birth.setCellValueFactory(new PropertyValueFactory<Patient, String>("date_of_birth"));
        status.setCellValueFactory(new PropertyValueFactory<Patient, String>("status"));
        gender.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        diagnosis.setCellValueFactory(new PropertyValueFactory<Patient, String>("diagnosis"));
        research.setCellValueFactory(new PropertyValueFactory<Patient, String>("results_of_research"));
        completion.setCellValueFactory(new PropertyValueFactory<Patient, String>("date_of_completion"));
        doctor.setCellValueFactory(new PropertyValueFactory<Patient, String>("full_name_of_doctor"));
        patientsData.addListener(new ListChangeListener<Patient>() {
            @Override
            public void onChanged(Change<? extends Patient> c){
                updateCount(patientsData.size());
            }
        });
        patientsModel.selectData();
        table.setItems(patientsData);
    }

}
