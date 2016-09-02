package sample.models;

import com.mysql.jdbc.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import sample.libs.Estimate;
import sample.libs.Filters.FilterColection;
import sample.libs.Filters.FiltersOperations;
import sample.libs.Hash;
import sample.libs.PreProcessing.PreProcessingOperation;
import sample.libs.PreProcessing.StartImageParams;
import sample.libs.SQLDatabase;
import sample.libs.Segmentation.SegmentationOperations;
import sample.libs.Session;
import sample.libs.SimpleResearch.ResearchParam;
import sample.libs.SimpleResearch.SimpleResearchCollection;

import java.awt.List;
import java.awt.Point;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.lang.Math.sqrt;

/**
 * Created by oleh on 04.05.2016.
 */
public class SimpleResearchModel extends SQLDatabase {

    private ObservableList<SimpleResearchCollection> comboBoxData = FXCollections.observableArrayList();
    protected Mat image, preprocimage, segmentationimage;
    public int  id;
    public String name, originalImagePath;

    public SimpleResearchModel(){
        sqlSetConnect();
    }
    /**
     * зчитування вхідного зображення в формат Mat
     * @param actionEvent
     * @throws IOException
     */
    public void chooseFile(ActionEvent actionEvent) throws IOException, SQLException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.bmp", "*.png", "*.jpg", "*.gif"));
        File file = chooser.showOpenDialog(new Stage());
        if(file != null) {

            this.originalImagePath = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1);
            this.insertImageNameToDB(this.originalImagePath);

            /** return RGB values, average bright**/
            StartImageParams.getStartValues(file);
            this.image = Highgui.imread(file.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);
            sample.libs.Image.Image.setImageMat(this.image);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please Select a File");
            alert.showAndWait();
        }
    }

    /**
     * відображає усі класи(досліди)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<SimpleResearchCollection> showNucleiClasses()throws SQLException, ClassNotFoundException{

        comboBoxData.add(new SimpleResearchCollection("0", "Новий дослід"));
        sqlExecute("SELECT * FROM simple_research");

        while(resultSet.next())
        {
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            comboBoxData.add(new SimpleResearchCollection(Integer.toString(id), name));
            System.out.println(id + " " + name);
        }
        return comboBoxData;
    }





























    /**
     * функція для підбору параметрів для автоматичного покращення якості зображень
     *
     * взагаді брєд повний. Але такий срач для відлагодження
     */
    public void autoPreProcFiltersSegmentationSetting(){

        Mat dst = new Mat();
        Mat testDst = new Mat();

        sample.libs.Image.Image.getImageMat().copyTo(dst);
        sample.libs.Image.Image.getImageMat().copyTo(testDst);

        System.out.println("-----------------------------------------------------------------" + sample.libs.Image.Image.getImageMat().size() + " " +
                dst.size());
        /** use testing parametrs for getting HistAverValue **/
        PreProcessingOperation properation = new PreProcessingOperation(testDst,"1","15", "1", "1");

        //SegmentationOperations segoperation = new SegmentationOperations(properation.getOutputImage(), "3",
        //        "0", "0");
        testDst.release();//clear memory
        //properation.getOutputImage().release();


        float tempBrightValue = Estimate.getBrightVal();

        /** for very blue **/

        if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 83 && Estimate.getRedAverage() > 140){
            System.out.println ("1");
            this.setImageParam(dst, "1","20","1","2");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 110 && Estimate.getRedAverage() > 140){
            System.out.println ("2");
            this.setImageParam(dst, "1","15","1","2");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 110 && Estimate.getRedAverage() > 135){
            System.out.println ("3");
            this.setImageParam(dst, "1","25","1","2");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 110 && Estimate.getRedAverage() > 115){
            System.out.println ("4");
            this.setImageParam(dst, "1","15","1","2");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 120 && Estimate.getRedAverage() > 130){
            System.out.println ("5");
            this.setImageParam(dst, "1","25","2","2");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 120 && Estimate.getRedAverage() > 115){
            System.out.println ("6");
            this.setImageParam(dst, "1","20","2","4");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130 && Estimate.getRedAverage() > 90){
            System.out.println ("7");
            this.setImageParam(dst, "1","19","2","2");
        }

        else if(tempBrightValue < 0.9 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() < 100
                ){
            System.out.println ("8");

            this.setImageParam(dst, "1","10","2","2");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getBlueAverage() < 185 && Estimate.getRedAverage() < 90){
            System.out.println ("9");

            this.setImageParam(dst, "1","20","1","6");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getBlueAverage() < 185 && Estimate.getRedAverage() < 100){
            System.out.println ("10");

            this.setImageParam(dst, "1","17","2","2");
        }
        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getBlueAverage() < 200 && Estimate.getRedAverage() < 100){
            System.out.println ("11");

            this.setImageParam(dst, "1","15","10","10");
        }

        else if(tempBrightValue > 1.5 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 220){
            System.out.println ("12");

            this.setImageParam(dst, "1","16","1","2");

        }

        else if(tempBrightValue > 1.5 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 100){
            System.out.println ("13");

            this.setImageParam(dst, "1","8","2","1");

        }

        else if(tempBrightValue > 1.1 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 160){
            System.out.println ("14");

            this.setImageParam(dst, "1","15","10","1");

        }

        else if(tempBrightValue > 1.1 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 140){
            System.out.println ("15");

            this.setImageParam(dst, "1","15","5","1");

        }

        else if(tempBrightValue > 1.1 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 100){
            System.out.println ("16");

            this.setImageParam(dst, "1","19","1","1");

        }

        else if(tempBrightValue > 1 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 100){
            System.out.println ("17");

            this.setImageParam(dst, "1","18","1","1");

        }

        else if(tempBrightValue > 1 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 130){
            System.out.println ("18");

            this.setImageParam(dst, "1","10","1","3");

        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() > 120 && Estimate.getRedAverage() > 100
                && Estimate.getSecondHistAverageValue() > 140){
            System.out.println ("19");

            this.setImageParam(dst, "1","22","1","3");

        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() > 130 && Estimate.getRedAverage() > 100){
            System.out.println ("20");

            this.setImageParam(dst, "1","15","1","3");

        }


        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130
                && Estimate.getFirstHistAverageValue() >100){
            System.out.println ("21");

            this.setImageParam(dst, "1","20","1","1");

        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 90 && Estimate.getRedAverage() > 130
                && Estimate.getSecondHistAverageValue() >110){
            System.out.println ("22");

            this.setImageParam(dst, "1","11","1","9");

        }


        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 100 && Estimate.getRedAverage() > 130
                && Estimate.getSecondHistAverageValue() >45){
            System.out.println ("23");

            this.setImageParam(dst, "1","18","9","1");
        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130 && Estimate.getRedAverage() > 130
                && Estimate.getSecondHistAverageValue() >165){
            System.out.println ("24");

            this.setImageParam(dst, "1","32","17","1");

        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130 && Estimate.getRedAverage() > 130
                && Estimate.getSecondHistAverageValue() >110){
            System.out.println ("25");

            this.setImageParam(dst, "1","33","8","1");

        }


        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130
                && Estimate.getFirstHistAverageValue() >55){
            System.out.println ("26");

            this.setImageParam(dst, "1","17","6","1");

        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130
                && Estimate.getFirstHistAverageValue() >55){
            System.out.println ("27");

            this.setImageParam(dst, "1","18","6","1");

        }

        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130
                && Estimate.getFirstHistAverageValue() >20){
            System.out.println ("28");

            this.setImageParam(dst, "1","18","1","1");

        }


        else if(tempBrightValue > 0.9 && tempBrightValue < 2 && Estimate.getBlueAverage() < 130
                && Estimate.getSecondHistAverageValue() >20){
            System.out.println ("29");

            this.setImageParam(dst, "1","21","3","1");

        }

        else if(tempBrightValue <= 0.9 && Estimate.getFirstHistAverageValue() < 100 && Estimate.getRedAverage() < 80) {
            System.out.println ("30");
            this.setImageParam(dst, "1","9","25","11");

        }

        else if(tempBrightValue <= 0.1 && Estimate.getFirstHistAverageValue() < 40 && Estimate.getRedAverage() >= 200 && Estimate.getBlueAverage() >= 100) {
            System.out.println ("31");
            this.setImageParam(dst, "1","5","1","1");//6-br

        }

        else if(tempBrightValue <= 0.1 && Estimate.getFirstHistAverageValue() < 100 && Estimate.getBlueAverage() >= 210 && Estimate.getRedAverage() >= 210) {
            System.out.println ("32");

            //thresholdSegmentation(dst);
            this.setImageParam(dst, "1","15","1","1");//6-br

        }

        else if(tempBrightValue <= 0.9 && Estimate.getFirstHistAverageValue() < 100 && Estimate.getRedAverage() >= 110) {
            System.out.println ("33");
            this.setImageParam(dst, "1","18","1","3");//6-br

        }

        else if(tempBrightValue <= 0.9 && Estimate.getFirstHistAverageValue() < 100 && Estimate.getRedAverage() >= 80) {
            System.out.println ("34");
            this.setImageParam(dst, "1","8","23","1");

        }

        else if(tempBrightValue <= 0.9 && Estimate.getFirstHistAverageValue()>100 && Estimate.getRedAverage() < 80) {
            System.out.println ("35");
            this.setImageParam(dst, "1","9","25","11");

        }

        else if(tempBrightValue <= 0.9 && tempBrightValue >= 0.5 && Estimate.getFirstHistAverageValue()>100 && Estimate.getRedAverage() > 100) {
            System.out.println ("36");
            this.setImageParam(dst, "1","15","14","11");
        }

        else if(tempBrightValue <= 0.5 && Estimate.getRedAverage() > 170 && Estimate.getRedAverage()<190 && Estimate.getBlueAverage()>205
                && Estimate.getBlueAverage()<225) {
            System.out.println ("37");
            //thresholdSegmentation(dst);
            this.setImageParam(dst, "1","20","1","1");
        }

        else if(tempBrightValue <= 0.5 && Estimate.getRedAverage() > 140 && Estimate.getBlueAverage()>200
                && Estimate.getFirstHistAverageValue() > 120) {
            System.out.println ("38");
            //thresholdSegmentation(dst);
            //this.setImageParam(dst, "1","20","1","1");

        }

        else if(tempBrightValue <= 0.5 && Estimate.getFirstHistAverageValue()>130 && Estimate.getRedAverage() > 170 && Estimate.getBlueAverage()>170) {
            System.out.println ("39");
            //thresholdSegmentation(dst);
            //this.setImageParam(dst, "1","1","1","1");

        }
        else if(tempBrightValue <= 0.5 && Estimate.getRedAverage() > 190 && Estimate.getBlueAverage()>100) {
            System.out.println ("40");
            //thresholdSegmentation(dst);
            this.setImageParam(dst, "1","15","1","1");

        }

        else if(tempBrightValue > 0.8 && tempBrightValue < 2 && Estimate.getBlueAverage() > 100 && Estimate.getRedAverage() > 100){
            System.out.println ("41");

            this.setImageParam(dst, "1","14","5","1");
        }
        else {
            this.setImageParam(dst, "1","15","1","1");
            System.out.println ("else");
        }
    }

    /**
     * Задає підібрані параметри в фільтрацію,
     * попередню обробку,
     * сегментацію
     * @param dst
     * @param contrast
     * @param bright
     * @param dilate
     * @param erode
     */
    public void setImageParam(Mat dst, String contrast, String bright, String dilate, String erode){


        //фільтрація
        FiltersOperations filtroperation = new FiltersOperations(dst, "4", "3", "", "", "");
        //попередня обробка (покращення якості)
        PreProcessingOperation properation = new PreProcessingOperation(filtroperation.getOutputImage(),contrast,bright,
                dilate, erode);

        filtroperation.getOutputImage().release();// очиска результатів фільтрації з памяті

        this.preprocimage = new Mat();
        properation.getOutputImage().copyTo(this.preprocimage);



        //sample.libs.Image.Image.setImageMat(properation.getOutputImage());
        //this.setPreProcImage(properation.getOutputImage());


        SegmentationOperations segoperation = new SegmentationOperations(properation.getOutputImage(), "3",
                "0", "0");

        properation.getOutputImage().release(); // очистка памяті після попередньої обробки

       SegmentationOperations segoperation_1 = new SegmentationOperations(segoperation.getOutputImage(), "1",
                "200", "255");

        //this.setSegmentationImage(segoperation_1.getOutputImage());
        this.segmentationimage = new Mat();
        segoperation_1.getOutputImage().copyTo(this.segmentationimage);

        segoperation.getOutputImage().release();
        segoperation_1.getOutputImage().release();

        //Estimate.setFirstHistAverageValue(null);
        //Estimate.setSecondHistAverageValue(null);
    }

    /**
     * занесення нового класу (досліду) до БД
     * @param res_name
     */
    @FXML
    public void insertResearchNameToDb(String res_name) throws SQLException {

        //Integer.parseInt(Session.getKeyValue("id")
        String stwww = "277";
        String query = "INSERT INTO simple_research (user_id, name) VALUES ('"+stwww+"', '"+res_name+"' )";
        sqlInsertExecute(query);

        String lastIDquery = "select MAX(id) from simple_research";
        sqlExecute(lastIDquery);

        while(resultSet.next())
        {
            int lastID = resultSet.getInt(1);
            ResearchParam.setResearch_id(lastID);
        }
    }

    /**
     * занесення назви файлу в БД
     * @param imgN
     * @throws SQLException
     */
    public void insertImageNameToDB(String imgN) throws SQLException {

        ResearchParam.setImg_name(imgN);

        String query = "INSERT INTO research_images (research_id, image_name) VALUES ('"+ResearchParam.getResearch_id()+"', '"+imgN+"')";
        sqlInsertExecute(query);

        String lastIDquery = "select MAX(id) from research_images";
        sqlExecute(lastIDquery);

        while(resultSet.next())
        {
            int lastID = resultSet.getInt(1);
            ResearchParam.setImg_id(lastID);
        }
    }

    /**
     * повертає зображення після попередньої обробки в форматі opencv Mat
     * @return
     */
    public Mat getPreprocimage(){
        return this.preprocimage;
    }

    /**
     * повертає зображення після сегментації в форматі opencv Mat
     * @return
     */
    public Mat getSegmentationimage(){return this.segmentationimage;}
}
