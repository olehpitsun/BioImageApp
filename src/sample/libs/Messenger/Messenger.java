package sample.libs.Messenger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by oleh on 18.06.2016.
 */
public class Messenger {

    private final StringProperty message;
    private final IntegerProperty id;
    private final IntegerProperty send_from_id;
    private final IntegerProperty send_to_id;

    public Messenger(int id, String message, int sendFromId, int sendToId) {
        this.id = new SimpleIntegerProperty(id);
        this.message = new SimpleStringProperty(message);
        this.send_from_id = new SimpleIntegerProperty(sendFromId);
        this.send_to_id = new SimpleIntegerProperty(sendToId);
    }

    public void setId(Integer id1){
        this.id.set(id1);
    }
    public Integer getId(){
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }

    public void setMessage(String message1){
        this.message.set(message1);
    }
    public String getMessage(){
        return message.get();
    }
    public StringProperty messageProperty() {
        return message;
    }

    public void setSend_from_id(Integer send_from_id1){this.send_from_id.set(send_from_id1);}
    public Integer getSend_from_id(){return send_from_id.get();}
    public IntegerProperty send_from_idProperty(){return send_from_id;}

    public void setSend_to_id(Integer send_to_id1){this.send_to_id.set(send_to_id1);}
    public Integer getSend_to_id(){return send_to_id.get();}
    public IntegerProperty send_to_idProperty(){return send_to_id;}
}
