package sample.nodes;

import sample.views.DiagnosisDoctorView;

/**
 * Created by oleh_pi on 12.09.2016.
 */
public class DiagnosisDoctor {
    DiagnosisDoctorView diagnosisDoctor;

    public DiagnosisDoctor() throws Exception {
        diagnosisDoctor = new DiagnosisDoctorView();
        diagnosisDoctor.render();
    }
}
