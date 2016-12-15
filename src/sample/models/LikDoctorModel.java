package sample.models;

import sample.controllers.LikDoctorController;
import sample.controllers.QuantitativeParametersController;
import sample.libs.*;
import sample.objects.Image.ImageList;
import sample.objects.Patients.PatientCollection;
import sample.objects.Research.ResearchCollection;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by oleh on 10.07.2016.
 */
public class LikDoctorModel extends SQLDatabase{
    private int id;
    public int counts;
    private String medical_card, surname, fathername, name, num_glass;
    private ArrayList imagePathList = new ArrayList();

    public LikDoctorModel() throws SQLException {sqlSetConnect();}

    public void createFolder(String pathToFolder){
        try {
            //new File(pathToFolder +"/pretreated").mkdir();
        }catch (Exception e){
            System.err.println(e);
        }
    }
    /**
     * отримання списку пацієнтів для даного лікаря(користувача)
     * @throws SQLException
     */
    public void getPatientList() throws SQLException{

        int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
        sqlExecute("SELECT * FROM patients ORDER BY ID DESC");// WHERE doctor_id = "+doctor_id+"");

        LikDoctorController.comboBoxData.clear();
        while(resultSet.next()) {
            setData();
            counts++;
            LikDoctorController.comboBoxData.add(new PatientCollection(id, medical_card, surname, name, fathername));
            QuantitativeParametersController.comboBoxData.add(new PatientCollection(id, medical_card));
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
        //sqlExecute("SELECT * FROM researches WHERE patient_id = "+patientID+" AND user_id = "+doctor_id+"");
        sqlExecute("SELECT * FROM researches WHERE patient_id = "+patientID+"");

        LikDoctorController.comboBoxResearchData.clear();
        while(resultSet.next()) {
            setResearchData();
            LikDoctorController.comboBoxResearchData.add(new ResearchCollection(id, name, num_glass));
            QuantitativeParametersController.comboBoxResearchData.add(new ResearchCollection(id, name, num_glass));
        }
    }

    /**
     * Отримуємо список зображень для досліду
     * @param research_id - ідентифікатор досліду
     */
    public void getImageByResearch(int research_id){

        sqlExecute("SELECT * FROM images WHERE research_id = "+research_id+" ");
        try {
            LikDoctorController.imageListData.clear();
            while(resultSet.next()) {
                LikDoctorController.imageListData.add(new ImageList(resultSet.getString("image_path"),
                        Integer.valueOf(resultSet.getString("id"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param patientId - ідентифікатор пацієнта
     * @param researchName - назва досліду
     * @param researchGlass - номер скла
     * @return
     */
    public int setNewResearchForPatient(int patientId, String researchName, String researchGlass){

        int research_id = 0;

        try {
            int doctor_id = Integer.valueOf(Session.getKeyValue("id"));
            sqlInsertExecute("INSERT INTO researches (name, num_glass, patient_id, user_id)" + "VALUES ("+"'"+researchName+"',"
                    +"'"+researchGlass+"'," + "'"+patientId+"',"+"'"+doctor_id+"' )");
            sqlExecute("SELECT id FROM researches WHERE patient_id='"+patientId+"' AND name='"+researchName+"'");
            if(resultSet.next()) {
                research_id = Integer.valueOf(resultSet.getString("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return research_id;
    }

    /**
     * формування списку зображень в директорії
     * @param dir - шлях до директорії
     */
    public void selectFileseFromDir(File dir, int research_id){

        final String[] mask = { ".png", ".jpg", ".jpeg", ".bmp" };
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            for (String s : mask) {
                if (fileName.toLowerCase(Locale.US).endsWith(s)) {
                    if (files[i].isFile()) {
                        imagePathList.add(files[i].getAbsolutePath());
                    }
                }
                if (files[i].isDirectory()) {
                    selectFileseFromDir(files[i], research_id);
                }
            }
        }
        prepareImageToInsert(research_id);
    }

    /**
     * Перебірзображень із списку дял додавання в бд
     * @param res_id - research_id
     */
    private void prepareImageToInsert(int res_id){
        for(int i = 0; i < imagePathList.size(); i++){
            this.addImageToDB(imagePathList.get(i).toString(), res_id);

        }
    }

    /**
     * Додавання шляху до зображення в бд
     * @param imgPath - повний шлях до файлу
     * @param research_id - research_id
     */
    private void addImageToDB(String imgPath, int research_id) {
        try {
            imgPath=imgPath.replace('\\','/') ;

            sqlInsertExecute("INSERT INTO images (research_id, image_path)" + "VALUES ("+"'"+research_id+"', "+"'"+imgPath+"')");
            sqlExecute("SELECT id FROM images WHERE research_id='"+research_id+"' AND image_path='"+imgPath+"'");
            if(resultSet.next()) {
                LikDoctorController.imageListData.add(new ImageList(imgPath, Integer.valueOf(resultSet.getString("id"))));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // void addToDB
}
