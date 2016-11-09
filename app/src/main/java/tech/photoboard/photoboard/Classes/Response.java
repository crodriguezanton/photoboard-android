package tech.photoboard.photoboard.Classes;

/**
 * Created by sergi on 09/11/2016.
 */

public class Response {
    boolean success;
    int error_code;
    String error;

    public Response(boolean success, int error_code, String error) {
        this.success = success;
        this.error_code = error_code;
        this.error = error;
    }

    public Response(boolean success, int error_code) {
        this.success = success;
        this.error_code = error_code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
