package sample.nodes;

import javafx.stage.Stage;
import sample.views.SendingView;

/**
 * Created by oleh_pi on 18.09.2016.
 */
public class SendingModule {

    SendingView sendingView;
    private Stage primaryStage;

    public SendingModule() throws Exception {
        sendingView = new SendingView();
        sendingView.render();
    }
}
