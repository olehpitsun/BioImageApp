package sample.libs;

import javafx.stage.Stage;

/**
 * Автор: Петро Лящинський
 * Дата створення: 23.04.2016.
 */

public class CurrentStage {
    public static Stage stage;
    public static Stage prev;
    public static Stage temp;
    public static Stage owner;
    public static Stage main;

    public static void setMainStage(Stage stage)
    {
        main = stage;
    }
    public static Stage getMainStage()
    {
        return main;
    }
    public static void setStage(Stage stages)
    {

        temp = stage;
        stage = stages;
        prev = temp;
    }
    public static void setOwnerStage(Stage stage)
    {
        owner = stage;
    }
    public static Stage getOwnerStage()
    {
        return owner;
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
