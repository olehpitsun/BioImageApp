package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import sample.libs.Filters.FilterColection;
import sample.libs.Image.ImageOperations;
import sample.libs.Segmentation.SegmentationColection;
import sample.libs.SimpleResearch.ResearchParam;
import sample.libs.SimpleResearch.SimpleResearchCollection;
import sample.models.SimpleResearchModel;
import sample.nodes.StartApp;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleResearchController {

    @FXML
    private Button loadImageButton, researchNameButton, automaticButton, handeButton;
    @FXML
    protected ImageView preProcImage, segmentationImage, originalImage;
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
    private boolean preProcModeType = true;

    public SimpleResearchController(){
    }

    public void setMainApp(StartApp startApp) {
        this.startApp = startApp;
    }

    public void chooseImage(ActionEvent actionEvent) throws IOException {
        SimpleResearchModel srm = new SimpleResearchModel();
        srm.chooseFile(actionEvent);

        setOriginalImage(sample.libs.Image.Image.getImageMat());
    }


    @FXML
    private void setOriginalImage(Mat dst ){
        this.originalImage.setImage(ImageOperations.mat2Image(dst));
        this.originalImage.setFitWidth(450.0);
        this.originalImage.setFitHeight(450.0);
        this.originalImage.setPreserveRatio(true);
    }

    @FXML
    public void setResearchName() throws IOException{

        this.researchname = researchNameField.getText();
        loadImageButton.setVisible(true);

    }

    /**
     * результат попередньої обробки
     */
    @FXML
    private void autoSetting(){
        SimpleResearchModel simpleResearchModel = new SimpleResearchModel();
        simpleResearchModel.autoPreProcFiltersSegmentationSetting();
        this.setOriginalImage(simpleResearchModel.getPreprocimage());// вивід обробленого зображення
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
            loadImageButton.setVisible(true);
        }
    }

    @FXML
    private void manualMode(){
        System.out.println("Ручний режим. Робить Андрій");
    }

}
