package sample.models;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import sample.controllers.LikDoctorController;
import sample.libs.Filters.FiltersOperations;
import sample.libs.Image.Image;
import sample.libs.Image.ImageOperations;
import sample.libs.Segmentation.SegmentationOperations;
import sample.tools.DivideString;

import java.io.File;

/**
 * Created by oleh on 11.08.2016.
 */
public class ImageManagerModule {

    private String pathToFolder;
    private Mat image;

    public ImageManagerModule(String pathToFolder){
        this.pathToFolder = pathToFolder;

        try {
            new File(this.pathToFolder +"/pretreated").mkdir();
        }catch (Exception e){
            System.err.println(e);
        }

        for(int i = 0; i < LikDoctorController.imageListData.size(); i++ ){

            System.out.println(LikDoctorController.imageListData.get(i).getFullPath());
            DivideString divideString = new DivideString(LikDoctorController.imageListData.get(i).getFullPath(), '\\', '.');

            image = this.autoImageCorrection(Highgui.imread(LikDoctorController.imageListData.get(i).getFullPath(),
                    Highgui.CV_LOAD_IMAGE_COLOR));

            ImageOperations.saveMatOnDisk(divideString.path() + "\\pretreated\\" + divideString.filename() + ".png",image);
        }
    }


    private Mat autoImageCorrection(Mat src){



        return src;
    }
}
