package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import sample.interfaces.PatientsBook;
import sample.libs.CurrentStage;
import sample.libs.Messages;
import sample.libs.Notifi;
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
    public static ObservableList<Patient> backupPatientsData = FXCollections.observableArrayList();
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Patient> table;
    @FXML
    private TableColumn<Patient, String> surname;
    @FXML
    private TableColumn<Patient, String> name;
    @FXML
    private TableColumn<Patient, String> fathername;
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
    public void search()
    {
        patientsData.clear();
        String text = search.getText();
        if (text == "")
        {
            patientsData.clear();
            patientsData.addAll(backupPatientsData);
            backupPatientsData.clear();
        }
        for (Patient patient : backupPatientsData)
        {
            if(String.valueOf(patient.getId()).contains(text) ||
                    patient.getDate_of_birth().toLowerCase().contains(text) ||
                    patient.getDate_of_completion().toLowerCase().contains(text) ||
                    patient.getDiagnosis().toLowerCase().contains(text) ||
                    patient.getSurname_of_patient().toLowerCase().contains(text) ||
                    patient.getName_of_patient().toLowerCase().contains(text) ||
                    patient.getFathername_of_patient().toLowerCase().contains(text) ||
                    patient.getDoctor_id().toLowerCase().contains(text) ||
                    patient.getGender().toLowerCase().contains(text) ||
                    patient.getResults_of_research().toLowerCase().contains(text) ||
                    patient.getStatus().toLowerCase().contains(text) ||
                    patient.getDate_of_birth().toUpperCase().contains(text) ||
                    patient.getDate_of_completion().toUpperCase().contains(text) ||
                    patient.getDiagnosis().toUpperCase().contains(text) ||
                    patient.getSurname_of_patient().toUpperCase().contains(text) ||
                    patient.getName_of_patient().toUpperCase().contains(text) ||
                    patient.getFathername_of_patient().toUpperCase().contains(text) ||
                    patient.getDoctor_id().toUpperCase().contains(text) ||
                    patient.getGender().toUpperCase().contains(text) ||
                    patient.getResults_of_research().toUpperCase().contains(text) ||
                    patient.getStatus().toUpperCase().contains(text) ||
                    patient.getDate_of_birth().contains(text) ||
                    patient.getDate_of_completion().contains(text) ||
                    patient.getDiagnosis().contains(text) ||
                    patient.getSurname_of_patient().contains(text) ||
                    patient.getName_of_patient().contains(text) ||
                    patient.getFathername_of_patient().contains(text) ||
                    patient.getDoctor_id().contains(text) ||
                    patient.getGender().contains(text) ||
                    patient.getResults_of_research().contains(text) ||
                    patient.getStatus().contains(text))
            {
                patientsData.add(patient);
            }
        }
    }
    static public TableView<Patient> staticTable;
    @FXML
    public void addPatient() throws Exception
    {
        AddPatientView addPatientView = new AddPatientView();
        addPatientView.render();
        Notifi.notification(Pos.TOP_RIGHT, "Увага!", "У всіх полях, крім дати народження, " +
                "заборонено використання цифр.");
    }
    @FXML
    public void close()
    {
        CurrentStage.getOwnerStage().close();
    }

    @FXML
    public void editPatient(ActionEvent event)
    {
        try {
            Patient patient = (Patient) table.getSelectionModel().getSelectedItem();
        EditPatientController.patient = patient;
        EditPatientView editPatientView = new EditPatientView();
            editPatientView.render();
            Notifi.notification(Pos.TOP_RIGHT, "Увага!", "У всіх полях, крім дати народження, " +
                    "заборонено використання цифр.");
        } catch (Exception ex) {
            Messages.error("Помилка","Не вибрано пацієнта","TABLE");
        }


    }
    @FXML
    public void deletePatient() throws SQLException
    {
        try {
        Patient patient = (Patient) table.getSelectionModel().getSelectedItem();
        //EditPatientController.patient = patient;
        patientsModel.remove(patient);
        patientsData.remove(patient);
        backupPatientsData.remove(patient);
        //table.getItems().remove(table.getSelectionModel().getSelectedIndex());
        } catch (Exception ex) {
            Messages.error("Помилка","Не вибрано пацієнта","TABLE");
        }
    }
    public void updateCount(int counts)
    {
        count.setText(String.valueOf(counts));
    }

    @FXML
    public void initialize() throws Exception{
        patientsData.clear();
        id.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname_of_patient"));
        name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name_of_patient"));
        fathername.setCellValueFactory(new PropertyValueFactory<Patient, String>("fathername_of_patient"));
        birth.setCellValueFactory(new PropertyValueFactory<Patient, String>("date_of_birth"));
        status.setCellValueFactory(new PropertyValueFactory<Patient, String>("status"));
        gender.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        diagnosis.setCellValueFactory(new PropertyValueFactory<Patient, String>("diagnosis"));
        research.setCellValueFactory(new PropertyValueFactory<Patient, String>("results_of_research"));
        completion.setCellValueFactory(new PropertyValueFactory<Patient, String>("date_of_completion"));
        doctor.setCellValueFactory(new PropertyValueFactory<Patient, String>("doctor_id"));
        patientsData.addListener(new ListChangeListener<Patient>() {
            @Override
            public void onChanged(Change<? extends Patient> c){
                updateCount(patientsData.size());
                table.getItems().removeAll(backupPatientsData);
                table.getItems().addAll(patientsData);
            }
        });
        patientsModel.selectData();
        pagination.setPageCount(patientsData.size() / rowsPerPage + 1);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
        //table.setItems(patientsData);
    }
    private final static int dataSize = 10_023;
    private final static int rowsPerPage = 5;
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, patientsData.size());
        table.setItems(FXCollections.observableArrayList(patientsData.subList(fromIndex, toIndex)));
        return new BorderPane(table);
    }

}
