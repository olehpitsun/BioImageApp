package sample.models;

import sample.libs.SQLDatabase;

/**
 * Created by oleh on 01.05.2016.
 */
public class DbModel extends SQLDatabase {

    public boolean checkDbConnection(){
        sqlSetConnect();
        if(connection !=null){
            return true;
        }else{
            return false;
        }
    }
}
