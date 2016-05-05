package sample.nodes;

import javafx.stage.Stage;
import sample.views.MainWindowView;
import sample.views.SimpleResearchModuleView;

/**
 * Created by oleh on 04.05.2016.
 */
public class SimpleResearchModule {

    SimpleResearchModuleView simpleResearchModuleView;
    protected SimpleResearchModule simpleResearchModule;
    private Stage primaryStage;

    public SimpleResearchModule() throws Exception {

        simpleResearchModuleView = new SimpleResearchModuleView();
        simpleResearchModuleView.render();
    }

    public void setSimpleResearchModule(SimpleResearchModule simpleResearchModule) {

        this.simpleResearchModule = simpleResearchModule;
    }
}
