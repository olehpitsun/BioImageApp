package sample.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.libs.Image.ImageList;
import sample.libs.Image.ImageOperations;
import sample.libs.Image.StartImageParams;
import sample.libs.PatientCollection;
import sample.libs.ResearchCollection;
import sample.models.LaborantModel;
import sample.models.LikDoctorModel;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by oleh on 22.08.2016.
 */
public class LaborantController {
    @FXML
    private JFXButton createResearchButton, handleCreateResearchButton, loadFolderButton;
    @FXML
    private TextField researchGlassField, researchNameField;
    @FXML
    private Label researchNameLabel, researchNameValueLabel, researchGlassLabel, researchGlassValueLabel;
    @FXML
    private ComboBox<PatientCollection> patientListComboBox;
    @FXML
    private ComboBox<ResearchCollection> researchListComboBox;
    @FXML
    private TableView<ImageList> imageListTableView;
    @FXML
    private TableColumn<ImageList, String> fullPathColumn;
    @FXML
    private ImageView selectedImageView, autoPreprocImageView;
    public static ObservableList<ImageList> imageListData = FXCollections.observableArrayList();
    private Mat selectedImageMat;
    public static ObservableList<PatientCollection> comboBoxData = FXCollections.observableArrayList();
    public static ObservableList<ResearchCollection> comboBoxResearchData = FXCollections.observableArrayList();
    private String researchName, researchGlass, pathToFolder;
    public int research_id, patientId, imgID;

    LaborantModel laborantModel;
    public LaborantController() throws SQLException
    {
        laborantModel = new LaborantModel();
    }

    /**
     * обробка інформації про обраного пацієнта
     * @throws SQLException
     */
    @FXML
    private void getResearchesByPatientList() throws SQLException {
        PatientCollection patientCollection = patientListComboBox.getSelectionModel().getSelectedItem();
        this.patientId = patientCollection.getId();
        LikDoctorModel likDoctorModel = new LikDoctorModel();
        likDoctorModel.getResearchesByPatient(this.patientId);
        researchListComboBox.setItems(comboBoxResearchData);
        researchListComboBox.setVisible(true); createResearchButton.setVisible(true);
    }

    /*** отримання інформації про обраний дослід*/
    @FXML
    private void getResearchByID(){
        researchNameField.setVisible(false); researchGlassField.setVisible(false); handleCreateResearchButton.setVisible(false);

        ResearchCollection selectedResearch = researchListComboBox.getSelectionModel().getSelectedItem();

        researchNameLabel.setVisible(true);
        researchNameValueLabel.setVisible(true); researchNameValueLabel.setText(selectedResearch.getName());
        researchGlassLabel.setVisible(true);
        researchGlassValueLabel.setVisible(true); researchGlassValueLabel.setText(selectedResearch.getNum_glass());

        this.research_id = selectedResearch.getId();
        this.researchName = selectedResearch.getName();
        this.researchGlass = selectedResearch.getNum_glass();

        try {
            LikDoctorModel lk = new LikDoctorModel();
            lk.getImageByResearch(this.research_id);

            setImageListToTable();// заповнення таблиці (список зображень)
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*** формування полів для нового дослідення*/
    @FXML
    private void createNewResearch(){
        researchNameField.setVisible(true); researchGlassField.setVisible(true); handleCreateResearchButton.setVisible(true);
    }

    /*** заповнення таблиці imageListTableView контентом*/
    @FXML
    private void setImageListToTable(){
        fullPathColumn.setCellValueFactory(cellData -> cellData.getValue().fullPathProperty());
        imageListTableView.setVisible(true);
        imageListTableView.setItems(LikDoctorController.imageListData);

        /**
         * обробка натиску на запис в таблиці та передача
         * у метод setSelectedImageView для виводу зображення на екран
         */
        imageListTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    ImageList selectedImg = imageListTableView.getSelectionModel().getSelectedItem();
                    setSelectedImageView(selectedImg.fullPathProperty().getValue().toString());//показати вибране оригінальне зображення
                    imgID = Integer.valueOf(selectedImg.imageDbIDProperty().getValue().toString());
                }
            }
        });
    }

    /**
     * вивід зображення на екран
     * @param path - повний шлях до файлу
     */
    @FXML
    private void setSelectedImageView(String path){
        System.out.println("1" +path);

        this.selectedImageMat = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);
        StartImageParams.getStartValues(this.selectedImageMat);
        ImageOperations.deleteFile("temp.png");
        this.selectedImageView.setImage(ImageOperations.mat2Image(this.selectedImageMat));
        this.selectedImageView.setFitWidth(450.0);
        this.selectedImageView.setFitHeight(450.0);
        this.selectedImageView.setPreserveRatio(true);
    }

    /*** занесення інформації про нове дослідження*/
    @FXML
    private void handleCreateResearch(){
        this.researchName = researchNameField.getText();
        this.researchGlass = researchGlassField.getText();

        LikDoctorModel likDoctorModel = null;
        try {
            likDoctorModel = new LikDoctorModel();
            this.research_id = likDoctorModel.setNewResearchForPatient(this.patientId, this.researchName, this.researchGlass);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        researchNameField.setVisible(false); researchGlassField.setVisible(false); handleCreateResearchButton.setVisible(false);

        researchNameLabel.setVisible(true);
        researchNameValueLabel.setVisible(true); researchNameValueLabel.setText(this.researchName);
        researchGlassLabel.setVisible(true);
        researchGlassValueLabel.setVisible(true); researchGlassValueLabel.setText(this.researchGlass);

        loadFolderButton.setVisible(true);
    }

    /*** вибір директорії дослідження*/
    @FXML
    private void loadFolder(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(new Stage());
        if(selectedDirectory == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не вибрано директорію");
            alert.setHeaderText("Виберіть директорію");
            alert.showAndWait();
        }else{
            this.pathToFolder = selectedDirectory.getAbsolutePath();
            LikDoctorModel likDoctorModel = null;
            try {
                imageListData.clear();// очистка списку зображень
                likDoctorModel = new LikDoctorModel();
                likDoctorModel.createFolder(this.pathToFolder);
                likDoctorModel.selectFileseFromDir(selectedDirectory, this.research_id);

                this.setImageListToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
