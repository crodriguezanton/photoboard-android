package tech.photoboard.photoboard.Classes;

/**
 * Created by pc1 on 23/10/2016.
 */

public class TakePhotoResponse {

    boolean response;
    String id;

    public TakePhotoResponse(String id, boolean response) {
        this.id = id;
        this.response = response;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
