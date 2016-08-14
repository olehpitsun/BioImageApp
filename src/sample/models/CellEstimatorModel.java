package sample.models;

import com.mysql.jdbc.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import sample.libs.Nuclei;
import sample.libs.SQLDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

/**
 * Created by oleh on 07.05.2016.
 */
public class CellEstimatorModel extends SQLDatabase {

    private Mat newDrawImage;
    public Mat oneObjectImage;
    private ObservableList<Nuclei> nucleiData = FXCollections.observableArrayList();

    public CellEstimatorModel(){
        sqlSetConnect();
    }

    public ObservableList<Nuclei> getNucleiData() {
        return nucleiData;
    }

    /**
     * функція, де відбувається підрахунок параметрів обєктів на зображення
     * та відбувається занесення їх в БД
     * Спочатку визначаються усі контури,
     * потім оброхунок по кожному контуру
     * input:  (Mat) this.image
     * @throws SQLException
     */
    @FXML
    public void SimpleDetect(Mat src) throws SQLException {

        double xc,yc,major_axis,minor_axis,theta;
        //Mat src = src1;
        Mat src_gray = new Mat();
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(src_gray, src_gray, new Size(3, 3));

        java.util.List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat mMaskMat = new Mat();

        Scalar lowerThreshold = new Scalar ( 0, 0, 0 );
        Scalar upperThreshold = new Scalar ( 10, 10, 10 );
        Core.inRange(src, lowerThreshold, upperThreshold, mMaskMat);
        Imgproc.findContours(mMaskMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        java.util.List<Moments> mu = new ArrayList<Moments>(contours.size());
        java.util.List<org.opencv.core.Point> mc = new ArrayList<org.opencv.core.Point>(contours.size());
        Mat drawing = Mat.zeros( mMaskMat.size(), CvType.CV_8UC3 );
        Rect rect ;

        for( int i = 0; i< contours.size(); i++ ) {
            rect = Imgproc.boundingRect(contours.get(i));
            mu.add(i, Imgproc.moments(contours.get(i), false));
            mc.add(i, new org.opencv.core.Point(mu.get(i).get_m10() / mu.get(i).get_m00(), mu.get(i).get_m01() / mu.get(i).get_m00()));
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            /** малювання обєктів**/
            Imgproc.drawContours(drawing, contours, i, new Scalar(255, 0, 0), 4, 1, hierarchy, 0, new org.opencv.core.Point());
            Core.circle(drawing, mc.get(i), 4, new Scalar(0, 0, 255), -1, 2, 0);
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            // Core.putText(drawing, Integer.toString(i) , new org.opencv.core.Point(rect.x-20,rect.y),
            //    Core.FONT_HERSHEY_TRIPLEX, 1.7 ,new  Scalar(255,255,255));
            /*** Занесення даних до бази даних*/
            double contourArea, perimetr, i_height, i_width, circular, equiDiameter;
            MatOfPoint2f mMOP2f1;

            contourArea = Imgproc.contourArea(contours.get(i));
            perimetr = Imgproc.arcLength(contour2f, true);
            i_height = rect.height;
            i_width = rect.y;
            circular = 4 * Math.PI * Imgproc.contourArea(contours.get(i)) / Imgproc.arcLength(contour2f, true)
                    * Imgproc.arcLength(contour2f, true);
            /**
             * блок підрахунку xc, yc, major_axis, minor_axis, theta
             * якщо площа більше 2. то все йде норм, інакше 0 , щоб не викидало помилок
             */
            mMOP2f1 = new MatOfPoint2f();
            contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);

            if (contourArea > 8) {
                RotatedRect e = Imgproc.fitEllipse(mMOP2f1);
                xc = e.center.x;
                yc = e.center.y;
                major_axis = e.size.height;    // width >= height
                minor_axis = e.size.width;
                theta = e.angle;
            } else {
                xc = 0;
                yc = 0;
                major_axis = 0;    // width >= height
                minor_axis = 0;
                theta = 0;
            }
            equiDiameter = sqrt(4 * contourArea / Math.PI);

            this.setCellParamstoDB(i,contourArea,perimetr,i_height, i_width, circular, xc, yc, major_axis, minor_axis,
                    theta, equiDiameter);
        }

        //nucleiTable.setItems(getNucleiData());
        //this.setOriginalImage(drawing);
        this.newDrawImage = new Mat();
        drawing.copyTo(this.newDrawImage);
    }

    private void setCellParamstoDB(int contour_num, double contourArea, double contour_perimetr, double contour_height,
                                   double contour_width, double contour_circularity, double xc, double yc, double major_axis,
                                   double minor_axis, double theta, double equiDiameter) throws SQLException {

        int image_id = 22;
        String query = "INSERT INTO research_nuclei_params (image_id, contour_num, contour_area, contour_perimetr," +
                " contour_height,contour_width, contour_circularity, xc, yc, major_axis, minor_axis, theta," +
                " equiDiameter  ) VALUES ("+image_id+", "+contour_num+", "+contourArea+", "+contour_perimetr+", "+contour_height+"," +
                ""+contour_width+", "+contour_circularity+", "+xc+", "+yc+", "+major_axis+", " + minor_axis+", "+theta+", "+equiDiameter+" )";

        sqlInsertExecute(query);

        nucleiData.add(new Nuclei(contour_num,contourArea,contour_perimetr, contour_height,contour_width,contour_circularity,
                xc,yc,major_axis,minor_axis, theta,equiDiameter));
    }

    /**
     * відображає лише одне ядро на зображенні
     * по номеру
     * @param objNum
     */
    @FXML
    public void showOnlyOneObject(Integer objNum){

        Mat src = sample.libs.Image.Image.getSegmentedImage();
        Mat src_gray = new Mat();
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(src_gray, src_gray, new Size(3, 3));
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat mMaskMat = new Mat();
        Scalar lowerThreshold = new Scalar ( 0, 0, 0 ); // Blue color – lower hsv values
        Scalar upperThreshold = new Scalar ( 10, 10, 10 ); // Blue color – higher hsv values
        Core.inRange(src, lowerThreshold, upperThreshold, mMaskMat);
        Imgproc.findContours(mMaskMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Moments> mu = new ArrayList<Moments>(contours.size());
        List<Point> mc = new ArrayList<Point>(contours.size());
        Mat drawing = Mat.zeros( mMaskMat.size(), CvType.CV_8UC3 );
        Rect rect ;
        for( int i = 0; i< contours.size(); i++ )
        {
            rect = Imgproc.boundingRect(contours.get(i));
            mu.add(i, Imgproc.moments(contours.get(i), false));
            mc.add(i, new Point(mu.get(i).get_m10() / mu.get(i).get_m00(), mu.get(i).get_m01() / mu.get(i).get_m00()));
            MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );
            /** малювання обєктів**/

            if(objNum == i){
                Imgproc.drawContours(drawing, contours, i, new Scalar(255, 0, 0), 4, 1, hierarchy, 0, new Point());
                Core.circle(drawing, mc.get(i), 4, new Scalar(0, 0, 255), -1, 2, 0);
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                Core.putText(drawing, Integer.toString(i) , new Point(rect.x,rect.y),
                        Core.FONT_HERSHEY_COMPLEX, 10.0 ,new  Scalar(0,255,0));
            }else{
                Imgproc.drawContours(drawing, contours, i, new Scalar(255, 255, 255), 4, 1, hierarchy, 0, new Point());
            }
            MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
            contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
        }

        this.oneObjectImage = new Mat();
        drawing.copyTo(this.oneObjectImage);
    }

    /**
     * повертає зображення з виділеними обєктами
     * @return
     */
    public Mat getnewDrawImage(){
        return this.newDrawImage;
    }

    /**
     * повертає зображення з виділеним одним обєктом
     * @return
     */
    public Mat getOneObjectImage(){
        return this.oneObjectImage; }
}
