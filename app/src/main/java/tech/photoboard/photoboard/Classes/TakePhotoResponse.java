package tech.photoboard.photoboard.Classes;

/**
 * Created by pc1 on 23/10/2016.
 */

public class TakePhotoResponse extends Response {

    String id;

    public TakePhotoResponse(boolean success, int error_code, String error, String id) {
        super(success, error_code, error);
        this.id = id;
    }

    public TakePhotoResponse(boolean success, int error_code, String id) {
        super(success, error_code);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
