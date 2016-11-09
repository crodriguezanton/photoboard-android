package tech.photoboard.photoboard.API;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.photoboard.photoboard.Classes.Photo;
import tech.photoboard.photoboard.Classes.TakePhotoResponse;
import tech.photoboard.photoboard.Classes.User;

/**
 * Created by pc1 on 23/10/2016.
 */

public interface RetrofitAPI {

    @GET("/login/{user}")

    Call<Boolean> login(@Path("user") User user) ;

    @GET("/pictures")

    Call<ArrayList<Photo>> getPicturesList();

    @GET("/takePhotoRequest")

    Call<TakePhotoResponse> takePhotoRequest();

    @GET("/getPhotoRequest/{id}")

    Call<Photo> getPhotoResquest(@Path("id") String id);


}
