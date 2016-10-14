package sample.tools;

import javafx.concurrent.Task;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.controllers.LikDoctorController;
import sample.libs.Image.ImageOperations;
import sample.libs.Image.StartImageParams;

/**
 * Created by oleh_pi on 12.10.2016.
 */
public class Load extends Task<Void> {

    String imgpath;
    public Load(String imgpath){
        this.imgpath = imgpath;
    }

    public void getImg(){

        LikDoctorController.selectedImageMat = Highgui.imread(this.imgpath, Highgui.CV_LOAD_IMAGE_COLOR);
        StartImageParams.getStartValues(LikDoctorController.selectedImageMat);
        ImageOperations.deleteFile("temp.png");
    }

    @Override
    protected Void call() throws Exception {

        getImg();
        return null;
    }
}
