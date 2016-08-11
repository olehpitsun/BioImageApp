package sample.models;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import sample.controllers.LikDoctorController;
import sample.libs.*;
import sample.libs.Image.ImageList;
import sample.libs.Image.ImageOperations;
import sample.libs.PreProcessing.PreProcessingOperation;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by oleh on 10.07.2016.
 */
public class LikDoctorModel extends SQLDatabase{
    private int id;
    public int counts;
    private String medical_card, surname, fathername, name, num_glass;
    private ArrayList imagePathList = new ArrayList();

    public LikDoctorModel() throws SQLException {sqlSetConnect();}

    /**
     * отримання списку пацієнтів для даного лікаря(користувача)
     * @throws SQLException
     */
    public void getPatientList() throws SQLException{

        int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
        sqlExecute("SELECT * FROM patients WHERE doctor_id = "+doctor_id+"");

        LikDoctorController.comboBoxData.clear();
        while(resultSet.next()) {
            setData();
            counts++;
            LikDoctorController.comboBoxData.add(new PatientCollection(id, medical_card, surname, name, fathername));
        }
    }

    public void setData() throws SQLException
    {
        this.medical_card = resultSet.getString("medical_card");
        this.surname = resultSet.getString("Surname");
        this.id = Integer.valueOf(resultSet.getString("ID"));
        this.name = resultSet.getString("Name");
        this.fathername = resultSet.getString("Fathername");
    }

    public void setResearchData() throws SQLException{
        this.id = Integer.valueOf(resultSet.getString("ID"));
        this.name = resultSet.getString("name");
        this.num_glass = resultSet.getString("num_glass");
    }

    /**
     * отримання списку дослідів для даного пацієнта
     * @param patientID - ідентифікатор пацієнта
     * @throws SQLException
     */
    public void getResearchesByPatient(int patientID) throws SQLException{

        int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
        sqlExecute("SELECT * FROM researches WHERE patient_id = "+patientID+" AND user_id = "+doctor_id+"");

        LikDoctorController.comboBoxResearchData.clear();
        while(resultSet.next()) {
            setResearchData();
            LikDoctorController.comboBoxResearchData.add(new ResearchCollection(id, name, num_glass));
        }
    }

    public int setNewResearchForPatient(int patientId, String researchName, String researchGlass){

        int research_id = 0;

        try {
            int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
            sqlInsertExecute("INSERT INTO researches (name, num_glass, patient_id, user_id)" + "VALUES ("+"'"+researchName+"',"
                    +"'"+researchGlass+"'," + "'"+patientId+"',"+"'"+doctor_id+"' )");
            sqlExecute("SELECT id FROM researches WHERE patient_id='"+patientId+"' AND name='"+researchName+"'");
            if(resultSet.next()) {
                research_id = Integer.valueOf(resultSet.getString("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return research_id;

    }
    /**
     * формування списку зображень в директорії
     * @param dir - шлях до директорії
     */
    public void selectFileseFromDir(File dir, int research_id){

        final String[] mask = { ".png", ".jpg", ".jpeg", ".bmp" };
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            for (String s : mask) {
                if (fileName.toLowerCase(Locale.US).endsWith(s)) {
                    if (files[i].isFile()) {
                        imagePathList.add(files[i].getAbsolutePath());
                    }
                }
                if (files[i].isDirectory()) {
                    selectFileseFromDir(files[i], research_id);
                }
            }
        }
        prepareImageToInsert(research_id);
    }

    /**
     * Перебірзображень із списку дял додавання в бд
     * @param res_id - research_id
     */
    private void prepareImageToInsert(int res_id){
        for(int i = 0; i < imagePathList.size(); i++){
            this.addImageToDB(imagePathList.get(i).toString(), res_id);
        }
    }

    /**
     * Додавання шляху до зображення в бд
     * @param imgPath - повний шлях до файлу
     * @param research_id - research_id
     */
    private void addImageToDB(String imgPath, int research_id) {
        try {
            sqlInsertExecute("INSERT INTO images (research_id, image_path)" + "VALUES ("+"'"+research_id+"',"+"'"+imgPath+"')");
            sqlExecute("SELECT id FROM images WHERE research_id='"+research_id+"' AND image_path='"+imgPath+"'");
            if(resultSet.next()) {
                LikDoctorController.imageListData.add(new ImageList(imgPath, Integer.valueOf(resultSet.getString("id"))));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } // void addToDB

    public Mat maskToOriginalImageImposition(Mat image, Mat mask){

        Rect rectangle = new Rect(10, 10, image.cols() - 20, image.rows() - 20);
        Mat bgdModel = new Mat(); // extracted features for background
        Mat fgdModel = new Mat(); // extracted features for foreground
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(0));
        ImageOperations.convertToOpencvValues(mask); // from human readable values to OpenCV values
        int iterCount = 1;
        Imgproc.grabCut(image, mask, rectangle, bgdModel, fgdModel, iterCount, Imgproc.GC_INIT_WITH_MASK);
        ImageOperations.convertToHumanValues(mask); // back to human readable values
        Imgproc.threshold(mask,mask,0,128,Imgproc.THRESH_TOZERO);

        Mat foreground = new Mat(image.size(), CvType.CV_8UC1, new Scalar(255, 255, 255));
        image.copyTo(foreground, mask);

        Mat src_gray = new Mat();
        Imgproc.cvtColor(foreground, src_gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(src_gray, src_gray, new Size(3, 3));

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Mat mMaskMat = new Mat();

        Scalar lowerThreshold = new Scalar ( 0, 0, 0 );
        Scalar upperThreshold = new Scalar ( 10, 10, 10 );
        Core.inRange(foreground, lowerThreshold, upperThreshold, mMaskMat);

        Imgproc.findContours(mMaskMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        List<Moments> mu = new ArrayList<Moments>(contours.size());
        List<Point> mc = new ArrayList<Point>(contours.size());
        Mat drawing = Mat.zeros( mMaskMat.size(), CvType.CV_8UC3 );

        Rect rect ;

        for( int i = 0; i< contours.size(); i++ ) {
            rect=null;
            rect = Imgproc.boundingRect(contours.get(i));
            Mat crop = foreground.submat(rect );

            Mat rgba =crop; Mat tempMat = crop;
            rgba = new Mat(crop.cols(), crop.rows(), CvType.CV_8UC3);
            crop.copyTo(rgba);

            List<Mat> hsv_planes_temp = new ArrayList<Mat>(3);
            Core.split(tempMat, hsv_planes_temp);

            double threshValue1 = PreProcessingOperation.getHistAverage(crop, hsv_planes_temp.get(0));
            System.out.println("thresh " + i + " " +  threshValue1);

            Core.rectangle( foreground, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0,0,250), 3);
            //Highgui.imwrite("C:\\IMAGES\\test\\result.jpg", foreground);

            //Highgui.imwrite("C:\\IMAGES\\test\\img"+i+".jpg", crop);
            //mu.add(i, Imgproc.moments(contours.get(i), false));
            // mc.add(i, new Point(mu.get(i).get_m10() / mu.get(i).get_m00(), mu.get(i).get_m01() / mu.get(i).get_m00()));
            //MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            /** малювання обєктів**/
            //Imgproc.drawContours(drawing, contours, i, new Scalar(255, 0, 0), 4, 1, hierarchy, 0, new Point());
            //Core.circle(drawing, mc.get(i), 4, new Scalar(0, 0, 255), -1, 2, 0);
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            //Core.putText(drawing, Integer.toString(i), new Point(rect.x - 20, rect.y),
            //    Core.FONT_HERSHEY_TRIPLEX, 1.7, new Scalar(255, 255, 255));
            /*** Занесення даних до бази даних*/
        /*
            double contourArea, perimetr, i_height, i_width, circular, equiDiameter;
            MatOfPoint2f mMOP2f1;

            contourArea = Imgproc.contourArea(contours.get(i));
            perimetr = Imgproc.arcLength(contour2f, true);
            System.out.println("Square " + contourArea);
            System.out.println("Perimetr " + perimetr);

            this.setOriginalImage(drawing);*/
        }
        return foreground;
    }
}
