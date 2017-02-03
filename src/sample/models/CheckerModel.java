package sample.models;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sample.controllers.StartAppController;
import sample.nodes.AuthModule;
import sample.nodes.StartApp;
import sample.views.CheckerView;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by Admin on 26.04.2016.
 */
public class CheckerModel extends Task<Void> {
    public void checkInternet(String host) throws Exception
    {
        Boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(host).openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {

            System.out.println("Проблеми з підключенням");

        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {



                    System.out.println("Проблеми з підключенням");

                }
            }
        }
        System.out.println(result ? "True" : "False");

    }

    @Override
    protected Void call() throws Exception {

                    checkInternet("http://www.google.com/");
                    checkInternet("https://www.youtube.com/");
                    checkInternet("https://www.instagram.com/");
                    checkInternet("https://www.twitter.com/");
                    System.out.println("Done");
                    Thread.sleep(200);
                    //StartApp.startAuth();
                    StartApp.startMainPage();
        return null;
    }
}
