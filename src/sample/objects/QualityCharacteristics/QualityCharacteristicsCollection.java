package sample.objects.QualityCharacteristics;

/**
 * Created by oleh_pi on 13.12.2016.
 */
public class QualityCharacteristicsCollection {
    private int id;
    private String title;

    public QualityCharacteristicsCollection(int id, String title){
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return id + " " + title;
    }
}
