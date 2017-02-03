/**
 * Authorization Module
 * Автор: Петро Лящинський
 * Версія: 1.0-b

  -------------------------------------------------------------------
  ЗАГАЛЬНИЙ ОПИС МОДУЛЯ
  Даний модуль призначений для авторизації користувача у системі.
  -------------------------------------------------------------------
  СТРУКТУРА
  Модуль розроблений за структурою MVC:
                   модель          - models/AuthModel.java
                   вид             - views/AuthModuleView.fxml та views/AuthView.java
                   контролер       - controllers/AuthController.java
                   головний клас   - nodes/AuthModule.java
  -------------------------------------------------------------------
  ОПИС ГЛОБАЛЬНИХ ТА ВНУТРІШНІХ ЗМІННИХ (ТИП НАЗВА)
  AuthModel:
         SQLDatabase db; ------  змінна типу SQLDatabase призначена для зберігання об'єкта
                                 бази даних.
                                 про клас SQLDatabase можна прочитати у файлі readme/SQL.md
         ResultSet resultSet; -  змінна призначена для зберігання результату SQL
                                 запиту
         String login, --------  змінні призначені для тимчасового зберігання логіна
         password                та пароля користувача
 AuthController:
         TextField login, -----  текстові поля у AuthModuleView.fxml
                password;
         Button signIn;   -----  кнопка SIGN IN у AuthModuleView.fxml
         AuthModel authModel; -  зберігання об'єкта моделі
 */


package sample.nodes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controllers.AuthController;
import sample.libs.CurrentStage;
import sample.models.AuthModel;
import sample.views.AuthView;

import java.io.IOException;

public class AuthModule {

    AuthView authView;
    private Stage primaryStage;
/*
    public AuthModule() throws IOException
    {
        authView = new AuthView();
        authView.render();
    }
*/
    public boolean authDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartApp.class.getResource("/sample/views/fxml/AuthModuleView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Авторизація");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(0, "sample/views/css/style.css");
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AuthController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            //CurrentStage.setStage(dialogStage);
            //controller.setConnectField();

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            //CurrentStage.getStage().close();
            dialogStage.showAndWait();

            //return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

}