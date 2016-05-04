/**
 * Adding Patient Module
 * Автор: Павло Лящинський
 * Дата створення: 23.04.2016.
 * Версія: 1.0-b

    * -------------------------------------------------------------------
    * ЗАГАЛЬНИЙ ОПИС МОДУЛЯ
            * Даний модуль призначений для додавання у систему пацієнтів лікарем.
            * -------------------------------------------------------------------
            * СТРУКТУРА
            * Модуль розроблений за структурою MVC:
                * модель          - models/AddPatientModel.java
                * вид             - views/AddPatientClassView.fxml та views/AddPatientView.java
                * контролер       - controllers/AddPatientClassController.java
                * головний клас   - nodes/AddPatientModule.java
    * -------------------------------------------------------------------
 */



package sample.nodes;

import sample.views.AddPatientView;
import java.io.IOException;

public class AddPatientModule {

    AddPatientView addPatientView;


    public AddPatientModule() throws IOException{
        addPatientView = new AddPatientView();
        addPatientView.render();
    }
}
