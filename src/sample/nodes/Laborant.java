package sample.nodes;

import javafx.stage.Stage;
import sample.views.LaborantView;

/**
 * Created by oleh on 22.08.2016.
 */
public class Laborant {
    LaborantView laborantView;
    protected Laborant laborant;
    private Stage primaryStage;

    public Laborant() throws Exception {
        laborantView = new LaborantView();
        laborantView.render();
    }
}
