package sample.nodes;

import javafx.stage.Stage;
import sample.views.QuantitativeParametersPageView;

/**
 * Created by oleh_pi on 13.09.2016.
 */
public class QuantitativeParametersPageModule {
    QuantitativeParametersPageView quantitativeParametersPageView;
    protected QuantitativeParametersPageModule quantitativeParametersPageModule;
    private Stage primaryStage;

    public QuantitativeParametersPageModule() throws Exception {
        quantitativeParametersPageView = new QuantitativeParametersPageView();
        quantitativeParametersPageView.render();
    }
}
