package sample.nodes;

import sample.views.AddUsersView;

/**
 * Created by Pavlo on 10.07.2016.
 */
public class AddUsers {
    AddUsersView addUsersView;
    public AddUsers() throws Exception {
        addUsersView = new AddUsersView();
        addUsersView.render();
    }
}
