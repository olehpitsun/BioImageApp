package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.libs.FTP.FTPFunctions;
import sample.libs.Image.ImageOperations;
import sample.models.DDQualityCharacteristicsModel;
import sample.models.LikDoctorModel;
import sample.objects.Image.DDoctorImageList;
import sample.objects.Patients.PatientCollection;
import sample.objects.QualityCharacteristics.QualityCharacteristicsCollection;
import sample.objects.Research.ResearchCollection;
import sample.tools.Md5Util;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sample.controllers.LikDoctorController.comboBoxData;
import static sample.controllers.LikDoctorController.comboBoxResearchData;

/**
 * Created by oleh_pi on 19.10.2016.
 */
public class DDQualityCharacteristicsController {

    @FXML
    private TableView<DDoctorImageList> imageListTableView;
    @FXML
    private TableColumn<DDoctorImageList, String> fullPathColumn;
    @FXML
    private ComboBox<PatientCollection> patientListComboBox;
    @FXML
    private ComboBox<ResearchCollection> researchListComboBox;
    @FXML
    private ImageView selectedImageView;
    @FXML
    private Label researchNameLabel, researchNameValueLabel, researchGlassLabel, researchGlassValueLabel;
    @FXML
    public ScrollPane scroll;
    public int research_id, patientId, imgID;
    public static ObservableList<DDoctorImageList> imageListData = FXCollections.observableArrayList();
    public static ArrayList<String> imageList = new ArrayList<String>();
    private DDQualityCharacteristicsModel ddqcmodel = new DDQualityCharacteristicsModel();// підєднання до БД

    @FXML
    public ComboBox<QualityCharacteristicsCollection> emailComboBox = new ComboBox<>();
    @FXML
    public ComboBox<QualityCharacteristicsCollection> typeComboBox = new ComboBox<>();
    @FXML
    public ComboBox<QualityCharacteristicsCollection> sizeComboBox = new ComboBox<>();


    @FXML
    private void initialize() {
        try {
            LikDoctorModel likDoctorModel = new LikDoctorModel();
            likDoctorModel.getPatientList();
        }catch (SQLException e){
            System.err.println(e);
        }
        patientListComboBox.setItems(comboBoxData);

        // колекція елементів форми
        List<ComboBox<QualityCharacteristicsCollection>> qualityCharacteristicsCollectionList =
                Arrays.asList(emailComboBox,typeComboBox,sizeComboBox);

        // перебір якісних параметрів. Виділення їх значень
        for(int i = 0, j = 1; i < qualityCharacteristicsCollectionList.size(); i++, j++){
            setQualityValues(qualityCharacteristicsCollectionList.get(i),j);
        }
    }

    public DDQualityCharacteristicsController(){}

    /*** виділення значень для якісних характеристик
     * @param srt - ComboBox-елемент
     * @param id - ідентифікатор значення якісного параметру в БД*/
    @FXML
    private void setQualityValues(ComboBox<QualityCharacteristicsCollection> srt, int id){
        try {
            srt.setItems(ddqcmodel.getQualityCharacteristicsValuesFromDB(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

        imageListData.clear();
        this.downloadFromFTP();

        imageListTableView.setVisible(true);
        imageListTableView.setItems(imageListData);

        /*** обробка натиску на запис в таблиці та передача
         * у метод setSelectedImageView для виводу зображення на екран*/
        imageListTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    DDoctorImageList selectedImg = imageListTableView.getSelectionModel().getSelectedItem();
                    setSelectedImageView(selectedImg.fullPathProperty().getValue().toString());//показати вибране оригінальне зображення
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

    /*** завантаження файлів на локальний диск*/
    @FXML
    private void downloadFromFTP(){

        Task<Void> task = new Task<Void>() {
            @Override public Void call() throws Exception {

                String patientDirName = Md5Util.md5(patientId+"");
                String researchName = Md5Util.md5(research_id+"");
                String fullpath = patientDirName + "/" + researchName + "/";

                System.out.println(fullpath);
                try {
                    FTPFunctions ftpF = new FTPFunctions("cafki.hol.es", 21, "u911040123.oleh", "olko111");
                    List<String> imagesList = ftpF.listFTPFiles(fullpath);

                    // створення локальної директорії
                    new File("C:/bioimg/" + fullpath).mkdirs();

                    for (String img : imagesList){
                        String imgpsth = "C:/bioimg/" + fullpath + img;
                        imageListData.add(new DDoctorImageList(imgpsth));
                        //скачування файлів
                        ftpF.downloadFTPFile("/" + fullpath + "/" + img, imgpsth);
                    }
                    ftpF.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread tasks = new Thread(task);
        tasks.start();
    }
}
