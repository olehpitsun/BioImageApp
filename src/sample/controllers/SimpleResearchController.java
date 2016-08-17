package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import sample.libs.Image.ImageOperations;
import sample.libs.Nuclei;
import sample.libs.Segmentation.SegmentationColection;
import sample.libs.SimpleResearch.ResearchParam;
import sample.libs.SimpleResearch.SimpleResearchCollection;
import sample.models.CellEstimatorModel;
import sample.models.SimpleResearchModel;
import sample.nodes.StartApp;
import java.io.IOException;
import java.sql.SQLException;

public class SimpleResearchController {

    @FXML
    private Button loadImageButton, researchNameButton, automaticButton, handeButton, autoButton, cellEstimatorButton;
    @FXML
    protected ImageView segmentationImage, originalImage;
    @FXML
    private TextField researchNameField;
    @FXML
    private Label researchName, resLabel;
    @FXML
    private ComboBox<SimpleResearchCollection> comboBox;
    @FXML
    private ComboBox<SegmentationColection> SegmentationcomboBox;
    private ObservableList<SimpleResearchCollection> comboBoxData = FXCollections.observableArrayList();
    private ObservableList<SegmentationColection> comboBoxSegmentationData = FXCollections.observableArrayList();
    protected StartApp startApp;
    private String  researchname;
    @FXML
    private TableView<Nuclei> nucleiTable;
    @FXML
    private TableColumn<Nuclei, Integer> contourNumColumn;
    @FXML
    private TableColumn<Nuclei, Double> contourAreaColumn, contourPerimetrColumn, contourHeightColumn, contourWidthColumn,
            contourCircularityColumn, contourXcColumn, contourYcColumn, contourMajor_axisColumn, contourMinor_axisColumn,
            contourThetaColumn, contourquiDiameterColumn;

    private boolean preProcModeType = true;
    public Mat segmentedImage;

    public SimpleResearchController(){
    }

    @FXML
    private void initialize() {

        contourNumColumn.setCellValueFactory(cellData -> cellData.getValue().contourNumProperty().asObject());
        contourAreaColumn.setCellValueFactory(cellData -> cellData.getValue().contourAreaProperty().asObject());
        contourPerimetrColumn.setCellValueFactory(cellData -> cellData.getValue().contourPerimetrProperty().asObject());
        contourHeightColumn.setCellValueFactory(cellData -> cellData.getValue().contourHeightProperty().asObject());
        contourWidthColumn.setCellValueFactory(cellData -> cellData.getValue().contourWidthtProperty().asObject());
        contourCircularityColumn.setCellValueFactory(cellData -> cellData.getValue().contourCircularityProperty().asObject());
        contourXcColumn.setCellValueFactory(cellData -> cellData.getValue().contourXcProperty().asObject());
        contourYcColumn.setCellValueFactory(cellData -> cellData.getValue().contourYcProperty().asObject());
        contourMajor_axisColumn.setCellValueFactory(cellData -> cellData.getValue().contourMajor_axisProperty().asObject());
        contourMinor_axisColumn.setCellValueFactory(cellData -> cellData.getValue().contourMinor_axisProperty().asObject());
        contourThetaColumn.setCellValueFactory(cellData -> cellData.getValue().contourThetaProperty().asObject());
        contourquiDiameterColumn.setCellValueFactory(cellData -> cellData.getValue().contourEquiDiameterProperty().asObject());
    }

    public void setMainApp(StartApp startApp) {
        this.startApp = startApp;
    }

    public void chooseImage(ActionEvent actionEvent) throws IOException, SQLException {
        SimpleResearchModel srm = new SimpleResearchModel();
        srm.chooseFile(actionEvent);

        setOriginalImage(sample.libs.Image.Image.getImageMat());
        autoButton.setDisable(false);
    }

    @FXML
    private void setOriginalImage(Mat dst ){
        this.originalImage.setImage(ImageOperations.mat2Image(dst));
        this.originalImage.setFitWidth(450.0);
        this.originalImage.setFitHeight(450.0);
        this.originalImage.setPreserveRatio(true);
    }

    @FXML
    private void setSegmentedImage(Mat dst ){
        this.segmentationImage.setImage(ImageOperations.mat2Image(dst));
        this.segmentationImage.setFitWidth(450.0);
        this.segmentationImage.setFitHeight(450.0);
        this.segmentationImage.setPreserveRatio(true);
    }

    /**
     * результат попередньої обробки
     */
    @FXML
    private void autoSetting(){
        SimpleResearchModel simpleResearchModel = new SimpleResearchModel();
        simpleResearchModel.autoPreProcFiltersSegmentationSetting();
        this.setOriginalImage(simpleResearchModel.getPreprocimage());// вивід обробленого зображення

        this.segmentedImage = simpleResearchModel.getSegmentationimage();
        sample.libs.Image.Image.setSegmentedImage(this.segmentedImage);
        this.setSegmentedImage(this.segmentedImage);

        this.autoButton.setDisable(true);
        this.cellEstimatorButton.setDisable(false);
    }

    @FXML
    private void selectPreprocMode(){

        System.out.println(preProcModeType);
        if(preProcModeType == true){
            this.handleStart(); handeButton.setDisable(false); automaticButton.setDisable(true);  this.preProcModeType = false;
        }else{
            this.manualMode();handeButton.setDisable(true); automaticButton.setDisable(false); this.preProcModeType = true;
        }
    }


    /**
     * вибірка всіх дослідів з БД
     * та занесення в випадаючий список
     */
    @FXML
    private void handleStart() {

        /**
         * відображення виділеного обєкта
         *//*
        nucleiTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();
                    cellEstimatorModel.showOnlyOneObject(nucleiTable.getSelectionModel().getSelectedItem().contourNumProperty().get());
                    setSegmentedImage(cellEstimatorModel.getOneObjectImage());
                }
            }
        });*/

        SimpleResearchModel sm = new SimpleResearchModel();
        try {
            comboBoxData = sm.showNucleiClasses();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        resLabel.setVisible(true);
        comboBox.setVisible(true);
        comboBox.setItems(comboBoxData);
    }


    @FXML
    private void handleComboBoxAction() {

        SimpleResearchCollection selectedResearch = comboBox.getSelectionModel().getSelectedItem();
        if(selectedResearch.getId() == "0"){
            researchNameField.setVisible(true);
            researchNameButton.setVisible(true);
        }else{
            ResearchParam.setResearch_id(Integer.parseInt(selectedResearch.getId()));
            researchNameField.setVisible(false);
            researchNameButton.setVisible(false);
            loadImageButton.setDisable(false);
        }
    }

    @FXML
    private void manualMode(){
        System.out.println("Ручний режим. Робить Андрій");
    }

    /**
     * Поле вводу назви нового класу (досліду)
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    public void setResearchName() throws IOException, SQLException, ClassNotFoundException {

        if(researchNameField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Попередження");
            alert.setHeaderText("Поле не може бути пустим");
            alert.showAndWait();
        }else{
            this.researchname = researchNameField.getText();

            SimpleResearchModel srm = new SimpleResearchModel();
            srm.insertResearchNameToDb(this.researchname);
            loadImageButton.setDisable(false);
        }
    }


    @FXML
    private void cellEstimator() throws SQLException {
/*
        CellEstimatorModel cellEstimatorModel = new CellEstimatorModel();
        cellEstimatorModel.SimpleDetect(this.segmentedImage);

        this.setSegmentedImage(cellEstimatorModel.getnewDrawImage());

        nucleiTable.setItems(cellEstimatorModel.getNucleiData());

        this.cellEstimatorButton.setDisable(true);*/
    }
}
