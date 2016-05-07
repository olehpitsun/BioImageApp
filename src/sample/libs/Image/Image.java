package sample.libs.Image;

import org.opencv.core.Mat;

public class Image {

    private static Mat image;
    private static Mat segmentedImage;

    static {
        image = new Mat();
    }

    public static Mat getImageMat(){
        return image;
    }

    public static Mat setImageMat(Mat newImage){
        image = newImage;
        return image;
    }

    public static void setSegmentedImage(Mat newImage){
        segmentedImage = newImage;
    }

    public static Mat getSegmentedImage(){
        return segmentedImage;
    }
}