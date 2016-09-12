package sample.nodes;

import sample.views.HandeImagePreprocessingView;

/**
 * Created by oleh_pi on 12.09.2016.
 */
public class HandeImagePreprocessing {
    HandeImagePreprocessingView handeImagePreprocessingView;

    public HandeImagePreprocessing() throws Exception{
        handeImagePreprocessingView = new HandeImagePreprocessingView();
        handeImagePreprocessingView.render();
    }
}
