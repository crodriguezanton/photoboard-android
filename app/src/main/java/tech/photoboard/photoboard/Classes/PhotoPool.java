package tech.photoboard.photoboard.Classes;

/**
 * Created by sergi on 16/12/2016.
 */

public class PhotoPool extends Response {

    boolean ready;
    Photo picture;

    public PhotoPool(boolean success, int error_code, String error, boolean ready, Photo picture) {
        super(success, error_code, error);
        this.ready = ready;
        this.picture = picture;
    }

    public PhotoPool(boolean success, int error_code, boolean ready, Photo picture) {
        super(success, error_code);
        this.ready = ready;
        this.picture = picture;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Photo getPicture() {
        return picture;
    }

    public void setPicture(Photo picture) {
        this.picture = picture;
    }
}
