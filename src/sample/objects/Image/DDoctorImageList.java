package sample.objects.Image;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by oleh on 11.07.2016.
 */
public class DDoctorImageList {

    private final StringProperty fullPath;

    public DDoctorImageList(String fullPath) {
        this.fullPath = new SimpleStringProperty(fullPath);
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

}
