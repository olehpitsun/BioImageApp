package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.libs.FTP.FTPFunctions;
import sample.libs.Image.ImageOperations;
import sample.libs.Image.StartImageParams;
import sample.models.LikDoctorModel;
import sample.objects.Image.ImageList;
import sample.objects.Patients.PatientCollection;
import sample.objects.Research.ResearchCollection;
import sample.tools.DivideString;
import sample.tools.Md5Util;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static sample.controllers.LikDoctorController.comboBoxData;
import static sample.controllers.LikDoctorController.comboBoxResearchData;

/**
 * Created by oleh_pi on 19.10.2016.
 */
public class DDQualityCharacteristicsController {

    @FXML
    private TableView<ImageList> imageListTableView;
    @FXML
    private TableColumn<ImageList, String> fullPathColumn;
    @FXML
    private ComboBox<PatientCollection> patientListComboBox;
    @FXML
    private ComboBox<ResearchCollection> researchListComboBox;
    @FXML
    private ImageView selectedImageView;
    @FXML
    private Label researchNameLabel, researchNameValueLabel, researchGlassLabel, researchGlassValueLabel;
    public int research_id, patientId, imgID;
    public static ObservableList<ImageList> imageListData = FXCollections.observableArrayList();
    public static ArrayList<String> imageList = new ArrayList<String>();

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

    public DDQualityCharacteristicsController(){}

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
        researchListComboBox.setVisible(true);
    }

    /*** отримання інформації про обраний дослід*/
    @FXML
    private void getResearchByID(){

        ResearchCollection selectedResearch = researchListComboBox.getSelectionModel().getSelectedItem();

        researchNameLabel.setVisible(true);
        researchNameValueLabel.setVisible(true); researchNameValueLabel.setText(selectedResearch.getName());
        researchGlassLabel.setVisible(true);
        researchGlassValueLabel.setVisible(true); researchGlassValueLabel.setText(selectedResearch.getNum_glass());

        this.research_id = selectedResearch.getId();

        try {
            LikDoctorModel lk = new LikDoctorModel();
            lk.getImageByResearch(this.research_id);

            setImageListToTable();// заповнення таблиці (список зображень)
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*** заповнення таблиці imageListTableView контентом*/
    @FXML
    private void setImageListToTable(){
        fullPathColumn.setCellValueFactory(cellData -> cellData.getValue().fullPathProperty());
        imageListTableView.setVisible(true);
        imageListTableView.setItems(LikDoctorController.imageListData);

        this.downloadFromFTP();


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

    /*** вивід зображення на екран
     * @param path - повний шлях до файлу*/
    @FXML
    private void setSelectedImageView(String path){
        System.out.println(Thread.currentThread().getName());
        Task<Void> task = new Task<Void>() {
            @Override public Void call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                LikDoctorController.selectedImageMat = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);
                selectedImageView.setImage(ImageOperations.mat2Image(LikDoctorController.selectedImageMat));
                selectedImageView.setFitWidth(450.0);
                selectedImageView.setFitHeight(450.0);
                selectedImageView.setPreserveRatio(true);

                return null;
            }
        };

        Thread tasks = new Thread(task);
        tasks.start();
    }

    @FXML
    private void setSelImageView(Mat dst){
        this.selectedImageView.setImage(ImageOperations.mat2Image(dst));
        this.selectedImageView.setFitWidth(450.0);
        this.selectedImageView.setFitHeight(450.0);
        this.selectedImageView.setPreserveRatio(true);
    }

    /**
     * завантаження файлів на локальний диск
     */
    @FXML
    private void downloadFromFTP(){

        String patientDirName = Md5Util.md5(this.patientId+"");
        String researchName = Md5Util.md5(this.research_id+"");

        try {
            FTPFunctions ftpF = new FTPFunctions("cafki.hol.es", 21, "u911040123.oleh", "olko111");

            List<String> imagesList = ftpF.listFTPFiles(patientDirName + "/" + researchName + "/");

            // створення локальної директорії
            new File("C:/bioimg/" + patientDirName + "/" + researchName).mkdirs();

            for (String img : imagesList){
                //скачування файлів
                ftpF.downloadFTPFile(img, "C:/bioimg/" + patientDirName + "/" + researchName + "/" + img);
                System.out.println(img);
            }

            ftpF.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
