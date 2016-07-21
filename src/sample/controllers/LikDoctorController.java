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
import sample.libs.PatientCollection;
import sample.libs.ResearchCollection;
import sample.models.LikDoctorModel;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by oleh on 10.07.2016.
 */
public class LikDoctorController {

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
    private TableColumn<ImageList, String> fullPathColumn, imageNameColumn;
    @FXML
    private ImageView selectedImageView;
    public static ObservableList<ImageList> imageListData = FXCollections.observableArrayList();
    public static ArrayList<String> imageList = new ArrayList<String>();
    private Mat selectedImageMat;
    private boolean okClicked = false;
    private Stage stage;
    private String researchName, researchGlass, pathToFolder;
    public static ObservableList<PatientCollection> comboBoxData = FXCollections.observableArrayList();
    public static ObservableList<ResearchCollection> comboBoxResearchData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        try {
            LikDoctorModel likDoctorModel = new LikDoctorModel();
            likDoctorModel.getPatientList();
        }catch (SQLException e){
            System.err.println(e);
        }
        patientListComboBox.setItems(comboBoxData);
    }

    public LikDoctorController() {}

    /**
     * обробка інформації про обраного пацієнта
     * @throws SQLException
     */
    @FXML
    private void getResearchesByPatientList() throws SQLException {
        PatientCollection patientCollection = patientListComboBox.getSelectionModel().getSelectedItem();

        LikDoctorModel likDoctorModel = new LikDoctorModel();
        likDoctorModel.getResearchesByPatient(patientCollection.getId());
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
        loadFolderButton.setVisible(true);

        this.researchName = selectedResearch.getName();
        this.researchGlass = selectedResearch.getNum_glass();
    }

    /*** формування полів для нового дослідення*/
    @FXML
    private void createNewResearch(){
        researchNameField.setVisible(true); researchGlassField.setVisible(true); handleCreateResearchButton.setVisible(true);
    }

    /*** занесення інформації про нове дослідження*/
    @FXML
    private void handleCreateResearch(){
        this.researchName = researchNameField.getText();
        this.researchGlass = researchGlassField.getText();
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
                likDoctorModel.selectFileseFromDir(selectedDirectory);

                this.setImageListToTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*** заповнення таблиці imageListTableView контентом*/
    @FXML
    private void setImageListToTable(){
        fullPathColumn.setCellValueFactory(cellData -> cellData.getValue().fullPathProperty());
        imageNameColumn.setCellValueFactory(cellData -> cellData.getValue().imageNameProperty());
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
                    System.out.println(imageListTableView.getSelectionModel().getSelectedItem().fullPathProperty().toString());
                    ImageList selectedImg = imageListTableView.getSelectionModel().getSelectedItem();
                    setSelectedImageView(selectedImg.fullPathProperty().getValue().toString());
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

        this.selectedImageMat = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);

        this.selectedImageView.setImage(ImageOperations.mat2Image(this.selectedImageMat));
        this.selectedImageView.setFitWidth(450.0);
        this.selectedImageView.setFitHeight(450.0);
        this.selectedImageView.setPreserveRatio(true);
    }
}
