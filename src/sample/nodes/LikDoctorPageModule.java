package sample.nodes;

import javafx.stage.Stage;
import sample.views.LikDoctorPageView;
import sample.views.MainWindowView;

/**
 * Created by oleh on 10.07.2016.
 */
public class LikDoctorPageModule {
    LikDoctorPageView likDoctorPageView;
    protected LikDoctorPageModule likDoctorPageModule;
    private Stage primaryStage;

    public LikDoctorPageModule() throws Exception {
        likDoctorPageView = new LikDoctorPageView();
        likDoctorPageView.render();
    }


    public void setLikDoctorPageView(LikDoctorPageModule likDoctorPageModule1) {

        this.likDoctorPageModule = likDoctorPageModule;
    }
}
