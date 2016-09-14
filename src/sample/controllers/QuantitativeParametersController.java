package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.libs.PatientCollection;
import sample.libs.ResearchCollection;
import sample.models.LikDoctorModel;
import sample.models.QuantitativeParametersModel;
import sample.objects.ImagesColection;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by oleh_pi on 13.09.2016.
 */
public class QuantitativeParametersController {

    @FXML
    private Button generateArffFileButton;
    @FXML
    private ComboBox<PatientCollection> patientListComboBox;
    @FXML
    private ComboBox<ResearchCollection> researchListComboBox;
    private String researchName, researchGlass, pathToFolder;
    public static ObservableList<PatientCollection> comboBoxData = FXCollections.observableArrayList();
    public static ObservableList<ResearchCollection> comboBoxResearchData = FXCollections.observableArrayList();

    @FXML
    private CheckBox contour_area, contour_perimetr, contour_height, contour_width, contour_circularity,
            xc, yc, major_axis, minor_axis, theta, equiDiameter, Bx, By, B_width, B_height, B_area, aspect_ratio,
            roudness, compactness;
    public int research_id, patientId;
    private Stage dialogStage;
    public static ArrayList selectedNucleiParam = new ArrayList();
    public static ObservableList<ImagesColection> comboBoxImagesData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try {
            LikDoctorModel likDoctorModel = new LikDoctorModel();
            likDoctorModel.getPatientList();
        }catch (SQLException e){
            System.err.println(e);
        }
        patientListComboBox.setItems(comboBoxData);

        contour_area.setSelected(true);
        contour_perimetr.setSelected(true);
        contour_height.setSelected(true);
        contour_width.setSelected(true);
        contour_circularity.setSelected(true);
        xc.setSelected(true);
        yc.setSelected(true);
        major_axis.setSelected(true);
        minor_axis.setSelected(true);
        theta.setSelected(true);
        equiDiameter.setSelected(true);

        Bx.setSelected(true);
        By.setSelected(true);
        B_width.setSelected(true);
        B_height.setSelected(true);
        B_area.setSelected(true);
        aspect_ratio.setSelected(true);
        roudness.setSelected(true);
        compactness.setSelected(true);

        handleCheckBoxAction();
    }

    public QuantitativeParametersController(){}

    /*** обробка інформації про обраного пацієнта
     * @throws SQLException*/
    @FXML
    private void getResearchesByPatientList() throws SQLException {
        PatientCollection patientCollection = patientListComboBox.getSelectionModel().getSelectedItem();
        this.patientId = patientCollection.getId();
        LikDoctorModel likDoctorModel = new LikDoctorModel();
        likDoctorModel.getResearchesByPatient(this.patientId);
        researchListComboBox.setItems(comboBoxResearchData);
        researchListComboBox.setVisible(true);
    }

    /*** отримання інформації про обраний дослід*/
    @FXML
    private void getResearchByID(){

        ResearchCollection selectedResearch = researchListComboBox.getSelectionModel().getSelectedItem();

        this.research_id = selectedResearch.getId();
        this.researchName = selectedResearch.getName();
        this.researchGlass = selectedResearch.getNum_glass();

        generateArffFileButton.setVisible(true);
    }

    /*** занесення вибраних параметрів для ядер*/
    @FXML
    private void handleCheckBoxAction() {

        selectedNucleiParam.clear();

        if(contour_area.isSelected()){
            selectedNucleiParam.add(contour_area.getId());
        }
        if(contour_perimetr.isSelected()){
            selectedNucleiParam.add(contour_perimetr.getId());
        }
        if(contour_height.isSelected()){
            selectedNucleiParam.add(contour_height.getId());
        }
        if(contour_width.isSelected()){
            selectedNucleiParam.add(contour_width.getId());
        }
        if(contour_circularity.isSelected()){
            selectedNucleiParam.add(contour_circularity.getId());
        }
        if(xc.isSelected()){
            selectedNucleiParam.add(xc.getId());
        }
        if(yc.isSelected()){
            selectedNucleiParam.add(yc.getId());
        }
        if(major_axis.isSelected()){
            selectedNucleiParam.add(major_axis.getId());
        }
        if(minor_axis.isSelected()){
            selectedNucleiParam.add(minor_axis.getId());
        }
        if(theta.isSelected()){
            selectedNucleiParam.add(theta.getId());
        }
        if(equiDiameter.isSelected()){
            selectedNucleiParam.add(equiDiameter.getId());
        }
    }

    /**
     * метод генерує arff файли з дослідів для Weka
     * @throws IOException
     * @throws SQLException
     * @throws NullPointerException
     */
    public void generateArffFile() throws IOException, SQLException, NullPointerException {

        /*** якщо немає підключення до БД
         * та не вибрано класу
         * генерація файлу не відбуватиметься*/
        if(selectedNucleiParam.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Помилка");
            alert.setHeaderText("Виникла помилка");
            alert.setContentText("Виберіть параметри оцінки ядра");

            alert.showAndWait();
        }
        QuantitativeParametersModel qpm = new QuantitativeParametersModel();
        qpm.getNucleiParamsData(this.research_id,  this.researchName);
        this.showNotofication();
    }

    @FXML
    public void showNotofication(){
        Notifications ntBuilder = Notifications.create()
                .title("Збереження")
                .text("Дані збережено в arff")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        ntBuilder.darkStyle();
        ntBuilder.show();
    }
}
