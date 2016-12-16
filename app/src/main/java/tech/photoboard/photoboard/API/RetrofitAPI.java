package tech.photoboard.photoboard.API;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @POST("/login")
<<<<<<< HEAD
=======

>>>>>>> origin/master
    Call<Response> login(@Body User user) ;

    @GET("/pictures")
    Call<ArrayList<Photo>> getPicturesList();

    @POST("/takephoto")
<<<<<<< HEAD
=======

>>>>>>> origin/master
    Call<TakePhotoResponse> takePhotoRequest(@Body int id);

    @GET("/poolphoto/{id}")
    Call<PhotoPool> getPhotoResquest(@Path("id") String id);

    @GET("/subjects")
    Call<ArrayList<Subject>> getSubjectsList();

    @GET("/subjectgallery/{id}")
    Call<PictureGallery> getSubjectPhotos(@Path("id") int id);

    @GET("/subjects")

    Call<ArrayList<Subject>> getSubjectsList();

    @GET("/subjectgallery/{id}")

    Call<ArrayList<Photo>> getSubjectPhotos(@Path("id") int id);


}
