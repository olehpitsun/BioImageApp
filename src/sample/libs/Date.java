package sample.libs;


import java.text.SimpleDateFormat;

public class Date {
    public static String getTime()
    {
        java.util.Date now = new java.util.Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return format1.format(now);
    }
}
