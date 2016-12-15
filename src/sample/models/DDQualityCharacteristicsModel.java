package sample.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.libs.SQLDatabase;
import sample.objects.QualityCharacteristics.QualityCharacteristicsCollection;
import java.sql.SQLException;

/**
 * Created by oleh_pi on 13.12.2016.
 */
public class DDQualityCharacteristicsModel extends SQLDatabase {

    public DDQualityCharacteristicsModel(){sqlSetConnect();}

    /**
     * Вибврка з БД підкаегорій до якісної характеристики
     * @param id - ідентифікатор якісної характеричтики (наприклад ФОРМА)
     * @return ObservableList<QualityCharacteristicsCollection> колекція підкатегорій
     * @throws SQLException
     */
    public ObservableList<QualityCharacteristicsCollection> getQualityCharacteristicsValuesFromDB(int id) throws SQLException{

        ObservableList<QualityCharacteristicsCollection> tempValue = FXCollections.observableArrayList();

        sqlExecute("SELECT id, title FROM QualityCharacteristics WHERE parent_id = " + id);
        while(resultSet.next()) {
            int id_v = resultSet.getInt("id");
            String title = resultSet.getString("title");
            tempValue.add(new QualityCharacteristicsCollection(id_v,title));
        }
        return tempValue;
    }
}
