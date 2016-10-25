package sample.libs;

import javafx.scene.control.Alert;

public class Messages {

    public static void information(String header, String message, String title)
    {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setTitle(title);
        alert.setHeaderText(header);
        String s =message;
        alert.setContentText(s);
        alert.showAndWait();

    }

    public static void error(String header, String message, String title)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information");
        alert.setTitle(title);
        alert.setHeaderText(header);
        String s =message;
        alert.setContentText(s);
        alert.showAndWait();

    }
}
