package sample.libs;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.Notifications;

/**
 * Created by Pavlo Liashchynskyi on 05.09.2016.
 */
public class Notifi {

    public static void notification(Pos pos, String title, String text){
        Notifications.create()
                .title(title)
                .text(text)
                .position(pos)
                .showWarning();
    }
}
