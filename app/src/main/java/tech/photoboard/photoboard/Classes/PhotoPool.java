package tech.photoboard.photoboard.Classes;

/**
 * Created by sergi on 16/12/2016.
 */

public class PhotoPool extends Response {

    String id;
    Photo picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Photo getPicture() {
        return picture;
    }

    public void setPicture(Photo picture) {
        this.picture = picture;
    }

    public PhotoPool(boolean success, int error_code, String error, String id, Photo picture) {
        super(success, error_code, error);
        this.id = id;
        this.picture = picture;
    }

    public PhotoPool(boolean success, int error_code, String error) {
        super(success, error_code, error);
    }

    public PhotoPool(boolean success, int error_code) {
        super(success, error_code);
    }
}
