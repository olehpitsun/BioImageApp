package sample.models;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sample.controllers.StartAppController;
import sample.nodes.AuthModule;
import sample.nodes.StartApp;
import sample.views.CheckerView;

/**
 * Created by Admin on 26.04.2016.
 */
public class CheckerModel extends Task<Void> {
    public void checkInternet(String host) throws Exception
    {
        Process proc = Runtime.getRuntime().exec("ping -n 1 " + host);
        boolean reachable = (proc.waitFor()==0);
        System.out.println(reachable ? "Host is reachable" : "Host is NOT reachable");

    }

    @Override
    protected Void call() throws Exception {

                    checkInternet("google.com");
                    checkInternet("youtube.com");
                    checkInternet("vk.com");
                    checkInternet("instagram.com");
                    checkInternet("google.com");
                    checkInternet("northbridge.16mb.com");
                    checkInternet("ki.tneu.edu.ua");
                    checkInternet("twitter.com");
                    System.out.println("Done");
                    Thread.sleep(200);
                    //StartApp.startAuth();
                    StartApp.startMainPage();
        return null;
    }
}
