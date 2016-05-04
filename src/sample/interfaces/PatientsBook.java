package sample.interfaces;

import sample.objects.Patient;

/**
 * Created by Admin on 04.05.2016.
 */
public interface PatientsBook {
    void add(Patient patient);
    void remove(Patient patient);
    void update(Patient patient);
}
