package sample.nodes;

import sample.views.PatientsView;

/**
 * Created by Petro on 04.05.2016.
 */
public class Patients {
    PatientsView patientsView;
    public Patients() throws Exception
    {
        patientsView = new PatientsView();
        patientsView.render();
    }
}
