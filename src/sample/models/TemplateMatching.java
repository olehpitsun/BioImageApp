package sample.models;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by oleh on 23.08.2016.
 */
public class TemplateMatching {
    /**
     *
     * @param img -вхідне зображення
     * @param templ -зображення для пошуку
     * @return mat результат пошуку
     */
    public Mat run(Mat img, Mat templ){

        // / Create the result matrix
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img, templ, result, Imgproc.TM_CCOEFF);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc;
        matchLoc = mmr.maxLoc;
        Core.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
                matchLoc.y + templ.rows()), new Scalar(0, 255, 0), 4);

        Core.putText(img, "Found", new Point(matchLoc.x - 60,matchLoc.y - 10),
                Core.FONT_HERSHEY_COMPLEX, 1.0 ,new  Scalar(0,255,0));
        return img;
    }
}
