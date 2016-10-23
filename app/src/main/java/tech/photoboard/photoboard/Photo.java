package tech.photoboard.photoboard;

/**
 * Created by pc1 on 23/10/2016.
 */

public class Photo {
    private String picture;
    private int id;

    public Photo (String picture, int id) {
        this.id = id;
        this.picture = picture;
    }
    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
