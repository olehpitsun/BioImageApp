package sample.nodes;

import sample.views.AdminView;

/**
 * Created by Pavlo on 10.07.2016.
 */
public class Admin {
    AdminView adminView;

    public Admin() throws Exception{
        adminView = new AdminView();
        adminView.render();
    }
}
