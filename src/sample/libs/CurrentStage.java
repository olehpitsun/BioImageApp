package sample.libs;

import javafx.stage.Stage;

/**
 * Автор: Петро Лящинський
 * Дата створення: 23.04.2016.
 */

public class CurrentStage {
    public static Stage stage;
    public static Stage prev;


    public static void setStage(Stage stages)
    {
        stage = stages;
    }
    public static Stage getStage()
    {
        return stage;
    }
    public static void setPrevStage()
    {
        prev = stage;
    }
    public static Stage getPrevStage()
    {
        return prev;
    }
}
