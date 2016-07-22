package sample.libs.Image;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by oleh on 11.07.2016.
 */
public class ImageList {

    private final StringProperty fullPath;
    private final IntegerProperty imageDbID;

    public ImageList(String fullPath, Integer imageDbID) {
        this.fullPath = new SimpleStringProperty(fullPath);
        this.imageDbID = new SimpleIntegerProperty(imageDbID);
    }

    public void setFullPath(String fullPath1){
        this.fullPath.set(fullPath1);
    }
    public String getFullPath(){
        return fullPath.get();
    }
    public StringProperty fullPathProperty() {
        return fullPath;
    }

    public void setImageDbID(int imageDbID1){this.imageDbID.set(imageDbID1);}
    public int getImageDbID(){return imageDbID.get();}
    public IntegerProperty imageDbIDProperty(){return imageDbID;}
}
