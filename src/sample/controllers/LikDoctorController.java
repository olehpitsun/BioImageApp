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
import org.opencv.imgproc.Imgproc;
import sample.libs.Filters.FiltersOperations;
import sample.libs.Image.ImageList;
import sample.libs.Image.ImageOperations;
import sample.libs.Image.StartImageParams;
import sample.libs.Nuclei;
import sample.libs.PatientCollection;
import sample.libs.ResearchCollection;
import sample.libs.Segmentation.SegmentationOperations;
import sample.models.CellEstimatorModel;
import sample.models.ImageManagerModule;
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
    private TableColumn<ImageList, String> fullPathColumn;
    @FXML
    private ImageView selectedImageView;
    @FXML
    private TableView<Nuclei> nucleiTable;
    @FXML
    private TableColumn<Nuclei, Integer> contourNumColumn;
    @FXML
    private TableColumn<Nuclei, Double> contourAreaColumn, contourPerimetrColumn;
    public static ObservableList<ImageList> imageListData = FXCollections.observableArrayList();
    public static ArrayList<String> imageList = new ArrayList<String>();
    private Mat selectedImageMat, autoPreprocImageMat;
    private boolean okClicked = false;
    private Stage stage;
    private String researchName, researchGlass, pathToFolder;
    public static ObservableList<PatientCollection> comboBoxData = FXCollections.observableArrayList();
    public static ObservableList<ResearchCollection> comboBoxResearchData = FXCollections.observableArrayList();
    private int research_id, patientId;
    @FXML
    private void initialize() {

        try {
            LikDoctorModel likDoctorModel = new LikDoctorModel();
            likDoctorModel.getPatientList();
        }catch (SQLException e){
            System.err.println(e);
        }
        patientListComboBox.setItems(comboBoxData);

        /** поля таблиці для зберігання параметрів обєктів*/
        contourNumColumn.setCellValueFactory(cellData -> cellData.getValue().contourNumProperty().asObject());
        contourAreaColumn.setCellValueFactory(cellData -> cellData.getValue().contourAreaProperty().asObject());
        contourPerimetrColumn.setCellValueFactory(cellData -> cellData.getValue().contourPerimetrProperty().asObject());
    }

    public LikDoctorController() {}

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
        //loadFolderButton.setVisible(true);

        this.research_id = selectedResearch.getId();
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

        LikDoctorModel likDoctorModel = null;
        try {
            likDoctorModel = new LikDoctorModel();
            this.research_id = likDoctorModel.setNewResearchForPatient(this.patientId, this.researchName, this.researchGlass);
            System.out.println("s" + this.research_id);
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
                likDoctorModel.selectFileseFromDir(selectedDirectory, this.research_id);

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
                    System.out.println(imageListTableView.getSelectionModel().getSelectedItem().getImageDbID());
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
        StartImageParams.getStartValues(this.selectedImageMat);
        ImageOperations.deleteFile("temp.png");
        this.selectedImageView.setImage(ImageOperations.mat2Image(this.selectedImageMat));
        this.selectedImageView.setFitWidth(450.0);
        this.selectedImageView.setFitHeight(450.0);
        this.selectedImageView.setPreserveRatio(true);
    }

    @FXML
    private void autoImagePreprocessing(){

        FiltersOperations filtroperation = new FiltersOperations(this.selectedImageMat, "4", "5", "", "", ""); // медіанний фільтр

        FiltersOperations filtersOperations_1;
        if(Imgproc.PSNR( filtroperation.getOutputImage(),this.selectedImageMat) < 30){
            filtersOperations_1 = filtroperation;
        }else{
            filtersOperations_1 = new FiltersOperations(filtroperation.getOutputImage(), "1", "3",
                    "1.0", "", "" ); // гаусовий фільтр
        }

        SegmentationOperations segoperation = new SegmentationOperations(filtersOperations_1.getOutputImage(), "3",
                "0", "0");

        filtroperation.getOutputImage().release(); /** очистка памяті **/
        filtersOperations_1.getOutputImage().release(); /** очистка памяті **/

        SegmentationOperations segoperation_1 = new SegmentationOperations(segoperation.getOutputImage(), "1",
                "250", "255");
        segoperation.getOutputImage().release(); /** очистка памяті **/

        this.selectedImageView.setImage(ImageOperations.mat2Image(segoperation_1.getOutputImage()));
        this.autoPreprocImageMat = segoperation_1.getOutputImage();
    }

    @FXML
    private void cellEstimator() throws SQLException {

        CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();
        cellEstimatorModel.SimpleDetect(this.autoPreprocImageMat);

        this.setSegmentedImage(cellEstimatorModel.getnewDrawImage());

        nucleiTable.setItems(cellEstimatorModel.getNucleiData());

        /**
         * відображення виділеного обєкта
         */
        nucleiTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();
                    cellEstimatorModel.showOnlyOneObject(nucleiTable.getSelectionModel().getSelectedItem().contourNumProperty().get());
                    setSegmentedImage(cellEstimatorModel.getOneObjectImage());
                }
            }
        });
    }

    @FXML
    private void setSegmentedImage(Mat dst ){
        this.selectedImageView.setImage(ImageOperations.mat2Image(dst));
        this.selectedImageView.setFitWidth(650.0);
        this.selectedImageView.setFitHeight(650.0);
        this.selectedImageView.setPreserveRatio(true);
    }

    @FXML
    private void maskToOriginalImageImposition() throws SQLException {
        LikDoctorModel likDoctorModel = new LikDoctorModel();
        this.setSegmentedImage(likDoctorModel.maskToOriginalImageImposition(this.selectedImageMat, this.autoPreprocImageMat));
    }

    @FXML
    private void autoImageCorection(){
        ImageManagerModule imageManagerModule = new ImageManagerModule(this.pathToFolder);
    }
}
