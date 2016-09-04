package sample.models;

import sample.libs.SQLDatabase;
import java.sql.SQLException;

/**
 * Created by oleh on 22.08.2016.
 */
public class LaborantModel extends SQLDatabase {
    public LaborantModel() throws SQLException {sqlSetConnect();}
}
