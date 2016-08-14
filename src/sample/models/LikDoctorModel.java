package sample.models;

import sample.controllers.LikDoctorController;
import sample.libs.Image.ImageList;
import sample.libs.PatientCollection;
import sample.libs.ResearchCollection;
import sample.libs.SQLDatabase;
import sample.libs.Session;
import java.io.File;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by oleh on 10.07.2016.
 */
public class LikDoctorModel extends SQLDatabase{
    private int id;
    public int counts;
    private String medical_card, surname, fathername, name, num_glass;

    public LikDoctorModel() throws SQLException {sqlSetConnect();}

    /**
     * отримання списку пацієнтів для даного лікаря(користувача)
     * @throws SQLException
     */
    public void getPatientList() throws SQLException{

        int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
        sqlExecute("SELECT * FROM patients WHERE doctor_id = "+doctor_id+"");

        LikDoctorController.comboBoxData.clear();
        while(resultSet.next()) {
            setData();
            counts++;
            LikDoctorController.comboBoxData.add(new PatientCollection(id, medical_card, surname, name, fathername));
        }
    }

    public void setData() throws SQLException
    {
        this.medical_card = resultSet.getString("medical_card");
        this.surname = resultSet.getString("Surname");
        this.id = Integer.valueOf(resultSet.getString("ID"));
        this.name = resultSet.getString("Name");
        this.fathername = resultSet.getString("Fathername");
    }

    public void setResearchData() throws SQLException{
        this.id = Integer.valueOf(resultSet.getString("ID"));
        this.name = resultSet.getString("name");
        this.num_glass = resultSet.getString("num_glass");
    }

    /**
     * отримання списку дослідів для даного пацієнта
     * @param patientID - ідентифікатор пацієнта
     * @throws SQLException
     */
    public void getResearchesByPatient(int patientID) throws SQLException{

        int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
        sqlExecute("SELECT * FROM researches WHERE patient_id = "+patientID+" AND user_id = "+doctor_id+"");

        LikDoctorController.comboBoxResearchData.clear();
        while(resultSet.next()) {
            setResearchData();
            LikDoctorController.comboBoxResearchData.add(new ResearchCollection(id, name, num_glass));
        }
    }

    /**
     * формування списку зображень в директорії
     * @param dir - шлях до директорії
     */
    public void selectFileseFromDir(File dir){

        final String[] mask = { ".png", ".jpg", ".jpeg", ".bmp" };
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            for (String s : mask) {
                if (fileName.toLowerCase(Locale.US).endsWith(s)) {
                    if (files[i].isFile()) {
                        LikDoctorController.imageListData.add(new ImageList(files[i].getPath(), files[i].getName()));
                    }
                }
                if (files[i].isDirectory()) {
                    selectFileseFromDir(files[i]);
                }
            }
        }
    }
}
