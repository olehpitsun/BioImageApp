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
    private final StringProperty imageName;

    public ImageList(String fullPath, String imageName) {
        this.fullPath = new SimpleStringProperty(fullPath);
        this.imageName = new SimpleStringProperty(imageName);
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

    public void setImageName(String imageName1){this.imageName.set(imageName1);}
    public String getImageName(){return imageName.get();}
    public StringProperty imageNameProperty(){return imageName;}
}
