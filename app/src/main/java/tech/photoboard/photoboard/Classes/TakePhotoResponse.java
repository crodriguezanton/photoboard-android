package tech.photoboard.photoboard.Classes;

/**
 * Created by pc1 on 23/10/2016.
 */

public class TakePhotoResponse extends Response {

    String uuid;
    String url;

    public String getUuid() {
        return uuid;
    }

    public TakePhotoResponse(boolean success, int error_code, String error, String uuid, String url) {
        super(success, error_code, error);
        this.uuid = uuid;
        this.url = url;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public TakePhotoResponse(boolean success, int error_code, String error, String uuid) {
        super(success, error_code, error);
        this.uuid = uuid;
    }

    public TakePhotoResponse(boolean success, int error_code, String uuid) {
        super(success, error_code);
        this.uuid = uuid;
    }

    public String getUrl() {
        return uuid;
    }

    public void setUrl(String id) {
        this.uuid = id;
    }
}
