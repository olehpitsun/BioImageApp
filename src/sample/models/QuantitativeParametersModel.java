package sample.models;

import sample.controllers.QuantitativeParametersController;
import sample.libs.SQLDatabase;
import sample.objects.ImagesColection;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by oleh_pi on 14.09.2016.
 */
public class QuantitativeParametersModel extends SQLDatabase {

    public QuantitativeParametersModel(){
        sqlSetConnect();
    }

    /**
     *
     * @param research_id - ідентифікатор досліду
     * @param researchName - назва досліду
     * @throws SQLException
     * @throws IOException
     */
    public void getNucleiParamsData(int research_id, String researchName) throws SQLException, IOException {

        sqlExecute("SELECT id FROM images WHERE research_id = "+research_id+" ");

        while(resultSet.next()) {
            int id = resultSet.getInt("id");
           System.out.println(id);
            QuantitativeParametersController.comboBoxImagesData.add(new ImagesColection(Integer.toString(id)));
        }

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        File fout = new File("_" + timeStamp + ".arff");// назва файлу
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        /** встановлення назви відношення */
        bw.write("@relation " + researchName);
        bw.newLine();

        for (int i = 0; i < QuantitativeParametersController.selectedNucleiParam.size(); i++) {
            bw.write("@attribute " + QuantitativeParametersController.selectedNucleiParam.get(i) + " numeric");
            bw.newLine();
        }

        bw.write("@data");
        bw.newLine();

        /*** вибірка параметрів ядер для кожного зображення із comboBoxImagesData*/
        for(int i=0; i<QuantitativeParametersController.comboBoxImagesData.size(); i++ ){
           writeNucleiParam(bw, QuantitativeParametersController.comboBoxImagesData.get(i).getId() );
        }
        bw.close();
    }

    /**
     * вибірка параметрів для кожного ядра
     * кожного зображення
     * запис в ФАЙЛ
     * @param bw
     * @throws SQLException
     * @throws IOException
     */
    public void writeNucleiParam(BufferedWriter bw, String image_id) throws SQLException, IOException {

        String ncp = nucleiParamToString();/** виклик функції для формування полів для запиту**/

        sqlExecute("SELECT image_id, contour_num, " + ncp + " FROM nuclei_params WHERE image_id = " + image_id + " ");

        while(resultSet.next()) {
            Integer img_id = resultSet.getInt(1);
            Integer contour_num = resultSet.getInt(2);System.out.println(contour_num);
            Double contour_area = resultSet.getDouble(3);
            Double contour_perimetr = resultSet.getDouble(4);
            Double contour_height = resultSet.getDouble(5);
            Double contour_width = resultSet.getDouble(6);
            Double contour_circularity = resultSet.getDouble(7);
            Double xc = resultSet.getDouble(8);
            Double yc = resultSet.getDouble(9);
            Double major_axis = resultSet.getDouble(10);
            Double minor_axis = resultSet.getDouble(11);
            Double theta = resultSet.getDouble(12);
            Double equiDiameter = resultSet.getDouble(13);

            String image_name = getImageName(img_id);
            bw.write(image_name + ", " + contour_num + ", ");
            bw.newLine();
            bw.write(image_name + ", " + contour_num + ", " + contour_area + ", " + contour_perimetr + ", " + contour_height + ", "
                    + contour_width + ", " + contour_circularity +
                    xc + ", " + yc + ", " + major_axis + ", " + minor_axis + ", " + theta + ", " + equiDiameter);

            bw.newLine();
        }
    }


    /*** функція генерує SQl стрічку запиту полів
     * @return str
     * */
    public String nucleiParamToString(){
        String str = "";
        for(int i = 0; i< QuantitativeParametersController.selectedNucleiParam.size(); i++){
            if(i < QuantitativeParametersController.selectedNucleiParam.size() - 1){
                str+=QuantitativeParametersController.selectedNucleiParam.get(i) + ", ";
            }else{
                str+=QuantitativeParametersController.selectedNucleiParam.get(i);
            }
        }
        return str;
    }

    /**
     * функція повертає назву зображення по його id
     * @param img_id
     * @return
     * @throws SQLException
     */
    public String getImageName(Integer img_id) throws SQLException, NullPointerException {

        String img_name = "";
        sqlExecute("SELECT image_path FROM images WHERE id = " + img_id + " ");

        while (resultSet.next()) {
            img_name = resultSet.getString(1);
        }
        return img_name;
    }
}
