package sample.models;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import sample.objects.Estimate.Estimate;
import sample.libs.Filters.FiltersOperations;
import sample.libs.Image.ImageOperations;
import sample.libs.Segmentation.SegmentationOperations;
import sample.tools.DivideString;

/**
 * Created by oleh on 11.08.2016.
 */
public class ImageManagerModule {

    //private String pathToImage;
    private Mat image;

    public ImageManagerModule(){
        //this.pathToImage = pathToImage;

        /*
        for(int i = 0; i < LikDoctorController.imageListData.size(); i++ ){

            System.out.println(LikDoctorController.imageListData.get(i).getFullPath());
            DivideString divideString = new DivideString(LikDoctorController.imageListData.get(i).getFullPath(), '\\', '.');

            image = this.autoImageCorrection(Highgui.imread(LikDoctorController.imageListData.get(i).getFullPath(),
                    Highgui.CV_LOAD_IMAGE_COLOR));

            ImageOperations.saveMatOnDisk(divideString.path() + "\\pretreated\\" + divideString.filename() + ".png",image);
        }*/
    }

    public void saveOneImageOnDisk(String path, Mat src){
        DivideString divideString = new DivideString(path, '\\', '.');
        ImageOperations.saveMatOnDisk(divideString.path() + "\\pretreated\\" + divideString.filename() + ".png",src);
    }

    public Mat autoImageCorrection(Mat src){

        FiltersOperations filtroperation = new FiltersOperations(src, "4", "5", "", "", ""); // медіанний фільтр
        FiltersOperations filtersOperations_1;
        //if(Imgproc.PSNR( filtroperation.getOutputImage(),src) < 30){
            filtersOperations_1 = filtroperation;
        //}
        //else{
            //filtersOperations_1 = new FiltersOperations(filtroperation.getOutputImage(), "1", "3",
              //      "1.0", "", "" ); // гаусовий фільтр
        //}
        //dst.release();/** очистка памяті **/

        SegmentationOperations segoperation = new SegmentationOperations(filtersOperations_1.getOutputImage(), "3",
                "0", "0");

        filtroperation.getOutputImage().release(); /** очистка памяті **/
        filtersOperations_1.getOutputImage().release(); /** очистка памяті **/

        SegmentationOperations segoperation_1 = new SegmentationOperations(segoperation.getOutputImage(), "1",
                "0", "178");

        segoperation.getOutputImage().release(); /** очистка памяті **/

        Estimate.setFirstHistAverageValue(null);
        Estimate.setSecondHistAverageValue(null);
        return segoperation_1.getOutputImage();
    }
}
