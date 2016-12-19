package tech.photoboard.photoboard.Classes;

/**
 * Created by pc1 on 23/10/2016.
 */

public class TakePhotoResponse extends Response {

    String url;

    public TakePhotoResponse(boolean success, int error_code, String error, String url) {
        super(success, error_code, error);
        this.url = url;
    }

    public TakePhotoResponse(boolean success, int error_code, String url) {
        super(success, error_code);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
