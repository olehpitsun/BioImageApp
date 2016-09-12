package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.libs.Image.ImageList;
import sample.libs.Image.ImageOperations;
import sample.libs.Image.StartImageParams;
import sample.libs.Nuclei;
import sample.libs.PatientCollection;
import sample.libs.ResearchCollection;
import sample.models.CellEstimatorModel;
import sample.models.ImageManagerModule;
import sample.models.LikDoctorModel;
import sample.models.TemplateMatching;
import sample.nodes.StartApp;

import java.io.File;
import java.io.IOException;
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
    private JFXHamburger LikDoctorHamburger;
    @FXML
    private JFXDrawer LikDoctorDrawer;
    @FXML
    private TableView<ImageList> imageListTableView;
    @FXML
    private TableColumn<ImageList, String> fullPathColumn;
    @FXML
    private ImageView selectedImageView, autoPreprocImageView;
    @FXML
    private TableView<Nuclei> nucleiTable;
    @FXML
    private TableColumn<Nuclei, Integer> contourNumColumn;
    @FXML
    private TableColumn<Nuclei, Double> contourAreaColumn, contourPerimetrColumn;
    @FXML
    private JFXButton autoImageCorectionButton,handeImageCorectionButton, cellParams, templateMatch;
    private ArrayList<JFXDrawer> drawers = new ArrayList<>();
    private Node content ;
    public static ObservableList<ImageList> imageListData = FXCollections.observableArrayList();
    public static ArrayList<String> imageList = new ArrayList<String>();
    private Mat selectedImageMat, autoPreprocImageMat, templateMatching;
    private boolean okClicked = false;
    private Stage stage;
    private String researchName, researchGlass, pathToFolder;
    public static ObservableList<PatientCollection> comboBoxData = FXCollections.observableArrayList();
    public static ObservableList<ResearchCollection> comboBoxResearchData = FXCollections.observableArrayList();
    public int research_id, patientId, imgID;
    @FXML
    private void initialize() {

        try {
            VBox box = FXMLLoader.load(getClass().getResource("../views/fxml/LikDoctorDrawerContent.fxml"));
            LikDoctorDrawer.setSidePane(box);
            LikDoctorDrawer.setOverLayVisible(false);

            for(Node node : box.getChildren()){
                if(node.getAccessibleText() != null){
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getAccessibleText()){
                            case "cellParams" :
                                try {
                                    calculateCellParameters();
                                }catch (Exception exc){
                                    System.err.println(exc);
                                }
                                break;
                            case "allCellParams" :
                                try {
                                    calculateAllCellParameters();
                                }catch (Exception exc){
                                    System.err.println(exc);
                                }
                                break;
                            case "templateMatch" :
                                try {
                                    setTMVisible();
                                }catch (Exception exc){
                                    System.err.println(exc);
                                }
                                break;
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HamburgerBackArrowBasicTransition authBurgerTask = new HamburgerBackArrowBasicTransition(LikDoctorHamburger);
        authBurgerTask.setRate(-1);
        LikDoctorHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            authBurgerTask.setRate(authBurgerTask.getRate() * -1);
            authBurgerTask.play();
            if(LikDoctorDrawer.isShown() || LikDoctorDrawer.isShowing()){
                LikDoctorDrawer.close();
                LikDoctorDrawer.setOverLayVisible(false);
            }
            else{
                updateDrawerPosition(LikDoctorDrawer);
                LikDoctorDrawer.open();
            }
        });

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
     * update drawers position in the stack once a drawer is drawn
     * @param drawer
     */
    private void updateDrawerPosition(JFXDrawer drawer){
        int index = drawers.indexOf(drawer);
        if(index + 1 < drawers.size()){
            if(index - 1 >= 0) drawers.get(index+1).setContent(drawers.get(index-1));
            else if(index == 0) drawers.get(index+1).setContent(content);
        }
        if(index < drawers.size() - 1){
            drawer.setContent(drawers.get(drawers.size()-1));
            drawers.remove(drawer);
            drawers.add(drawer);
            //this.getChildren().add(drawer);
        }
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
        researchGlassValueLabel.setVisible(false);researchNameValueLabel.setVisible(false);
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
                //likDoctorModel.createFolder(this.pathToFolder);
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
                    ImageList selectedImg = imageListTableView.getSelectionModel().getSelectedItem();
                    setSelectedImageView(selectedImg.fullPathProperty().getValue().toString());//показати вибране оригінальне зображення
                    imgID = Integer.valueOf(selectedImg.imageDbIDProperty().getValue().toString());
                    //settingsAutoPreprocImageView(selectedImg.fullPathProperty().getValue().toString());
                    templateMatch.setVisible(false);
                    autoImageCorectionButton.setVisible(true);
                    handeImageCorectionButton.setVisible(true);
                }
            }
        });
    }

    /*** вивід зображення на екран
     * @param path - повний шлях до файлу*/
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
    private void setSelImageView(Mat dst){
        this.selectedImageView.setImage(ImageOperations.mat2Image(dst));
        this.selectedImageView.setFitWidth(450.0);
        this.selectedImageView.setFitHeight(450.0);
        this.selectedImageView.setPreserveRatio(true);
    }

    @FXML
    private void setAutoPreprocImageView(Mat dst){
        this.autoPreprocImageView.setImage(ImageOperations.mat2Image(dst));
        this.autoPreprocImageView.setFitWidth(450.0);
        this.autoPreprocImageView.setFitHeight(450.0);
        this.autoPreprocImageView.setPreserveRatio(true);
    }



    /*** Автоматичне покращення якості та обробки
     * (сегментації) зображення*/
    @FXML
    private void autoImageCorection(){
        try {
            ImageManagerModule imageManagerModule = new ImageManagerModule();
            this.autoPreprocImageMat = imageManagerModule.autoImageCorrection(this.selectedImageMat);
            //imageManagerModule.saveOneImageOnDisk(path, this.autoPreprocImageMat);
            setAutoPreprocImageView(this.autoPreprocImageMat);
        }catch (Exception e){
            System.err.println(e);
        }
    }

    /**
     * Підрахунок параметрів обєктів
     * на зображенні
     * Обробка вибраного обєкта
     * @throws SQLException
     */
    @FXML
    private void calculateCellParameters() throws SQLException{

        CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();
        cellEstimatorModel.SimpleDetect(imgID, this.autoPreprocImageMat);
        setAutoPreprocImageView(cellEstimatorModel.getnewDrawImage());

        nucleiTable.setVisible(true);
        nucleiTable.setItems(cellEstimatorModel.getNucleiData());
        /*** відображення виділеного обєкта*/
        nucleiTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();
                    cellEstimatorModel.showOnlyOneObject(autoPreprocImageMat, nucleiTable.getSelectionModel().getSelectedItem().contourNumProperty().get());
                    setAutoPreprocImageView(cellEstimatorModel.getOneObjectImage());
                }
            }
        });
    }

    /**
     * Автоматична попередня обробка та обрахунок параметрів
     * обєктів зі списку зображень в досліду
     * потребує доопрацювання
     * @throws SQLException
     */
    @FXML
    private void calculateAllCellParameters()throws SQLException{
        ImageManagerModule imageManagerModule = new ImageManagerModule();
        CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();

        for(int i = 0; i < imageListData.size(); i++){
            Mat src = imageManagerModule.autoImageCorrection(Highgui.imread(imageListData.get(i).getFullPath().toString()));
            cellEstimatorModel.SimpleDetect(imageListData.get(i).getImageDbID(),src);
        }
    }

    @FXML
    private void setTMVisible(){
        handeImageCorectionButton.setVisible(false);
        autoImageCorectionButton.setVisible(false);
        templateMatch.setVisible(true);
    }

    @FXML
    private void TemplateMatch(){

        TemplateMatching tm =new TemplateMatching();
        setSelImageView(tm.run(this.selectedImageMat, this.templateMatching));
    }

    public void chooseTMFile(ActionEvent actionEvent) throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.bmp", "*.png", "*.jpg", "*.gif", "*.jpeg"));
        File file = chooser.showOpenDialog(new Stage());
        if(file != null) {
            this.templateMatching = Highgui.imread(file.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);
            setAutoPreprocImageView(this.templateMatching);
            TemplateMatch();

            //Image img = new Image("../views/images/ok-512.png");
            Notifications ntBuilder = Notifications.create()
                    .title("Template Matching")
                    .text("Пошук завершено")
                    .graphic(null)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT);
            ntBuilder.darkStyle();
            ntBuilder.show();

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please Select a File");
            alert.showAndWait();
        }
    }

    @FXML
    private void handeImageCorection(){
        try {
            StartApp.handeImagePreprocessing();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
