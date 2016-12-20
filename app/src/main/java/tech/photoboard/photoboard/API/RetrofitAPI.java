package tech.photoboard.photoboard.API;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import tech.photoboard.photoboard.Classes.Photo;
import tech.photoboard.photoboard.Classes.PhotoPool;
import tech.photoboard.photoboard.Classes.PictureGallery;
import tech.photoboard.photoboard.Classes.Response;
import tech.photoboard.photoboard.Classes.Subject;
import tech.photoboard.photoboard.Classes.TakePhotoResponse;
import tech.photoboard.photoboard.Classes.User;

/**
 * Created by pc1 on 23/10/2016.
 */

public interface RetrofitAPI {

    @POST("login")
    Call<Response> login(@Body User user) ;

    @FormUrlEncoded
    @POST("picture-request/")
    Call<TakePhotoResponse> takePhotoRequest(@Field("subject") int id);

    @GET("pool-request/{id}")
    Call<PhotoPool> getPhotoResquest(@Path("id") String url);

    @POST("subjects")
    Call<ArrayList<Subject>> getSubjectsList(@Body User user);

    @GET
    Call<PictureGallery> getSubjectPhotos(@Url String url);

}
