package sample.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import sample.objects.Nuclei.Nuclei;
import sample.libs.SQLDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

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
    public void SimpleDetect(int imgID, Mat src) throws SQLException {

        double Bx, By1, B_width, B_height, B_area, aspect_ratio, roudness, compactness;
        double xc,yc,major_axis,minor_axis,theta;

        Mat src_gray = new Mat();
        //Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(src_gray, src_gray, new Size(3, 3));

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat mMaskMat = new Mat();

        Scalar lowerThreshold = new Scalar ( 0, 0, 0 );
        Scalar upperThreshold = new Scalar ( 10, 10, 10 );
        Core.inRange(src, lowerThreshold, upperThreshold, mMaskMat);
        Imgproc.findContours(mMaskMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Moments> mu = new ArrayList<Moments>(contours.size());
        List<Point> mc = new ArrayList<Point>(contours.size());
        Mat drawing = Mat.zeros( mMaskMat.size(), CvType.CV_8U );
        Rect rect ;

        for( int i = 0; i< contours.size(); i++ ) {
            rect = Imgproc.boundingRect(contours.get(i));
            mu.add(i, Imgproc.moments(contours.get(i), false));

            mc.add(i, new Point(mu.get(i).get_m10() / mu.get(i).get_m00(), mu.get(i).get_m01() / mu.get(i).get_m00()));

            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            /** малювання обєктів**/
            Imgproc.drawContours(drawing, contours, i, new Scalar(255, 0, 0), 4, 1, hierarchy, 0, new Point());
            // Core.circle(drawing, mc.get(i), 4, new Scalar(0, 0, 255), -1, 2, 0);
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            //Core.putText(drawing, Integer.toString(i) , new Point(rect.x-20,rect.y),
            //      Core.FONT_HERSHEY_TRIPLEX, 1.7 ,new  Scalar(255,255,255));

            /*** Занесення даних до бази даних*/
            double contourArea, perimetr, i_height, i_width, circular, equiDiameter;
            MatOfPoint2f mMOP2f1;

            contourArea = Imgproc.contourArea(contours.get(i));
            if(contourArea > 200) {

                perimetr = Imgproc.arcLength(contour2f, true);
                i_height = rect.height;
                i_width = rect.width;
                circular = 4 * Math.PI * Imgproc.contourArea(contours.get(i)) / Imgproc.arcLength(contour2f, true)
                        * Imgproc.arcLength(contour2f, true);


                circular = Math.round(circular * 100.0) / 100.0;

                /**
                 * блок підрахунку xc, yc, major_axis, minor_axis, theta
                 * якщо площа більше 2. то все йде норм, інакше 0 , щоб не викидало помилок
                 */
                mMOP2f1 = new MatOfPoint2f();
                contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
                RotatedRect e = Imgproc.fitEllipse(mMOP2f1);
                if (contourArea > 7) {
                    xc = e.center.x;
                    yc = e.center.y;

                    if (e.size.height >= e.size.width) {
                        major_axis = e.size.height;    // width >= height
                        minor_axis = e.size.width;
                    } else {
                        major_axis = e.size.width;
                        minor_axis = e.size.height;
                    }

                    theta = e.angle;

                } else {
                    xc = 0;
                    yc = 0;
                    major_axis = 0;    // width >= height
                    minor_axis = 0;
                    theta = 0;
                }
                equiDiameter = sqrt(4 * contourArea / Math.PI);
                Bx = e.boundingRect().x;
                By1 = e.boundingRect().y;
                B_width = e.boundingRect().width;
                B_height = e.boundingRect().height;
                B_area = e.boundingRect().area();
                aspect_ratio = major_axis / minor_axis;
                roudness = 4 * contourArea / (Math.PI * pow(major_axis, 2));
                compactness = Math.pow(perimetr, 2) / 4 * Math.PI * contourArea;

                this.setCellParamstoDB(imgID, i, contourArea, perimetr, i_height, i_width, circular, xc, yc, major_axis,
                        minor_axis, theta, equiDiameter, Bx, By1, B_width, B_height, B_area, aspect_ratio, roudness,
                        compactness);

                this.newDrawImage = drawing;
            }
        }
    }

    /**
     * занесення інформації про обєкт в БД
     * @param imgID - ідентифікатор зображення
     * @param contour_num - ідентифікатор обєкта
     * @param contourArea - площа
     * @param contour_perimetr - периметр
     * @param contour_height - висота
     * @param contour_width - ширина
     * @param contour_circularity - окружність
     * @param xc - центр X
     * @param yc - центр Y
     * @param major_axis - головна вісь
     * @param minor_axis - допоміжна вісь
     * @param theta - кут
     * @param equiDiameter - діаметр Ферета
     * @param Bx  - ліва верхня точка обмежуючого прямокутника X
     * @param By1 - ліва верхня точка обмежуючого прямокутника Y
     * @param B_width - ширина обмежуючого прямокутника
     * @param B_height - довжина обмежуючого прямокутника
     * @param B_area - площа обмежуючого прямокутника
     * @param aspect_ratio - aspect_ratio
     * @param roudness - roudness
     * @param compactness - compactness
     * @throws SQLException
     */
    private void setCellParamstoDB(int imgID, int contour_num, double contourArea, double contour_perimetr, double contour_height,
                                   double contour_width, double contour_circularity, double xc, double yc, double major_axis,
                                   double minor_axis, double theta, double equiDiameter, double Bx, double By1, double B_width,
                                   double B_height, double B_area, double aspect_ratio, double roudness, double compactness)
            throws SQLException {

        String query = "INSERT INTO nuclei_params (image_id, contour_num, contour_area, contour_perimetr," +
                " contour_height, contour_width, contour_circularity, xc, yc, major_axis, minor_axis, theta," +
                " equiDiameter, Bx, By1, B_width, B_height, B_area, aspect_ratio, roudness, compactness) " +
                "VALUES ("+imgID+", "+contour_num+", "+contourArea+", "+contour_perimetr+", "+contour_height+"," +
                ""+contour_width+", "+contour_circularity+", "+xc+", "+yc+", "+major_axis+", " + minor_axis+", "+theta+"," +
                " "+equiDiameter+", "+Bx+", "+By1+", "+B_width+", "+B_height+", "+B_area+", "+aspect_ratio+", "+roudness+"," +
                " "+compactness+" )";

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
    public void showOnlyOneObject(Mat src, Integer objNum){

        Mat src_gray = new Mat();
//        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(src_gray, src_gray, new Size(3, 3));
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat mMaskMat = new Mat();
        Scalar lowerThreshold = new Scalar ( 0, 0, 0 );
        Scalar upperThreshold = new Scalar ( 10, 10, 10 );
        Core.inRange(src, lowerThreshold, upperThreshold, mMaskMat);
        Imgproc.findContours(mMaskMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Moments> mu = new ArrayList<Moments>(contours.size());
        List<Point> mc = new ArrayList<Point>(contours.size());
        Mat drawing = Mat.zeros( mMaskMat.size(), CvType.CV_8U );
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
                //Imgproc.drawContours(drawing, contours, i, new Scalar(255, 255, 255), 4, 1, hierarchy, 0, new Point());
            }
            MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
            contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);
        }
        this.oneObjectImage = new Mat(drawing.rows(),drawing.cols(),CvType.CV_8U);
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
