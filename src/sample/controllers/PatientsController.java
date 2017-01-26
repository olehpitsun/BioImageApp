package sample.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import sample.interfaces.PatientsBook;
import sample.libs.*;
import sample.models.AddPatientModel;
import sample.models.EditPatientModel;
import sample.models.PatientsModel;
import sample.nodes.AddPatientModule;
import sample.nodes.Patients;
import sample.objects.Patient;
import sample.views.AddPatientView;
import sample.views.EditPatientView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
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
    private Button pdfBtn;
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
    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = null;
        try {
            BaseFont baseFont = BaseFont.createFont("src/sample/res/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font c = new Font(baseFont, 14, Font.NORMAL);
            cell = new PdfPCell(new Phrase(text, c));
            cell.setPaddingBottom(15);
            cell.setHorizontalAlignment(alignment);
            cell.setBorder(PdfPCell.NO_BORDER);

        } catch (Exception e)
        {}
        return cell;
    }
    @FXML
    public void pdfExport()
    {
        try {
            Patient pat = (Patient) table.getSelectionModel().getSelectedItem();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(Hash.hash(pat.getName_of_patient(), Hash.generateSalt(10)) + ".pdf"));
            document.open();
            //meta
            BaseFont baseFont = BaseFont.createFont("src/sample/res/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font t = new Font(baseFont, 16, Font.BOLD);
            Font c = new Font(baseFont, 14, Font.NORMAL);
            //content
            Paragraph title = new Paragraph("Результати дослідження", t);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(90);
            table.setSpacingAfter(20);
            table.addCell(getCell("ПІБ пацієнта: ", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(pat.getSurname_of_patient() + " " +
                    pat.getName_of_patient() + " " +
                    pat.getFathername_of_patient(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Дата народження: ", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(pat.getDate_of_birth(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Стать: ", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(pat.getGender(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Результати дослідження: ", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(pat.getResults_of_research(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Діагнози: ", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(pat.getDiagnosis(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Дата заповнення: ", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(pat.getDate_of_completion(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("ПІБ лікаря: ", PdfPCell.ALIGN_LEFT));
            patientsModel.getdoctor(pat.getDoctor_id());
            ResultSet set = patientsModel.returnResult();
            set.next();
            table.addCell(getCell(set.getString("Surname") + " " +
                    set.getString("Name") + " " + set.getString("Fathername"), PdfPCell.ALIGN_RIGHT));
            document.add(table);
            document.close();
        }
        catch (Exception ex)
        {
            Messages.error("Помилка","Не вибрано пацієнта","TABLE");
            ex.printStackTrace();
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
