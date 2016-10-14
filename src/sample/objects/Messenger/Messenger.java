package sample.objects.Messenger;

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
    private final StringProperty send_from_id;
    private final IntegerProperty send_to_id;
    private final StringProperty messageDate;

    public Messenger(int id, String message, String sendFromId, int sendToId, String messageDate) {
        this.id = new SimpleIntegerProperty(id);
        this.message = new SimpleStringProperty(message);
        this.send_from_id = new SimpleStringProperty(sendFromId);
        this.send_to_id = new SimpleIntegerProperty(sendToId);
        this.messageDate = new SimpleStringProperty(messageDate);
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

    public void setSend_from_id(String send_from_id1){this.send_from_id.set(send_from_id1);}
    public String getSend_from_id(){return send_from_id.get();}
    public StringProperty send_from_idProperty(){return send_from_id;}

    public void setSend_to_id(Integer send_to_id1){this.send_to_id.set(send_to_id1);}
    public Integer getSend_to_id(){return send_to_id.get();}
    public IntegerProperty send_to_idProperty(){return send_to_id;}

    public void setMessageDate(String messageDate){this.messageDate.set(messageDate);}
    public String getMessageDate(){return messageDate.get();}
    public StringProperty messageDateProperty(){return messageDate;}
}
