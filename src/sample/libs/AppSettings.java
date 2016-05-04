package sample.libs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Автор: Петро Лящинський
 * Дата створення: 23.04.2016.
 */
public class AppSettings {
    private static FileInputStream fileInputStream;
    static Properties prop = new Properties();
    public static void loadPropertiesXML(String path)
    {
        try {
            fileInputStream = new FileInputStream(path);
            prop.loadFromXML(fileInputStream);
        }
        catch (Exception io)
        {
            io.printStackTrace();
        }
    }
    public static void load(String path)
    {
        try {
            fileInputStream = new FileInputStream(path);
            prop.load(fileInputStream);
        }
        catch (Exception io)
        {
            io.printStackTrace();
        }
    }
    public static String getProperty(String key)
    {
        return prop.getProperty(key);
    }
    public static void setProperty(String key, String value)
    {
        prop.setProperty(key, value);
    }
}
