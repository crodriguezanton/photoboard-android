package tech.photoboard.photoboard.API;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.photoboard.photoboard.Photo;
import tech.photoboard.photoboard.Utils.User;

/**
 * Created by pc1 on 23/10/2016.
 */

public interface RetrofitAPI {

    @GET("/login/{user}")
    Call<String> login(@Path("user") User user) ;

    @GET("pictures")

    Call<ArrayList<Photo>> getPicturesList();

    @GET("/takePhotoRequest/")

    Call<TakePhotoResponse> takePhotoRequest();

    @GET("/getPhotoRequest/{token}")

    Call<String> getPhotoResquest(@Path("token") String token);


}
