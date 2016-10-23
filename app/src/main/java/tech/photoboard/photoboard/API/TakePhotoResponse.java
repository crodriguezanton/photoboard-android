package tech.photoboard.photoboard.API;

/**
 * Created by pc1 on 23/10/2016.
 */

public class TakePhotoResponse {

    String response;
    String id;

    public TakePhotoResponse(String id, String response) {
        this.id = id;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
