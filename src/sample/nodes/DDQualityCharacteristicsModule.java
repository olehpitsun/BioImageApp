package sample.nodes;

import javafx.stage.Stage;
import sample.views.DDQualityCharacteristicsView;
/**
 * Created by oleh_pi on 19.10.2016.
 */
public class DDQualityCharacteristicsModule {
    DDQualityCharacteristicsView DDQualityCharacteristicsView;
    protected DDQualityCharacteristicsModule ddQualityCharacteristicsModule;
    private Stage primaryStage;

    public DDQualityCharacteristicsModule() throws Exception {
        DDQualityCharacteristicsView = new DDQualityCharacteristicsView();
        DDQualityCharacteristicsView.render();
    }
}
