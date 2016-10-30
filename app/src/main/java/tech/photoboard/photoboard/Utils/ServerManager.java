package tech.photoboard.photoboard.Utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.Photo;

/**
 * Created by pc1 on 24/10/2016.
 */

public class ServerManager {

    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
    private ArrayList<Photo> photoList;
    getPhotosResquest myGetPhotoRequest;

    public ServerManager() {

    }

    public interface getPhotosResquest {
        void onGetPhotoResponse(ArrayList<Photo> photos);
    }
    public void getPhotos() {
        Call<ArrayList<Photo>> getPhotos = retrofitAPI.getPicturesList();
        getPhotos.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                photoList = response.body();
                for(int i=0; i < photoList.size(); i++){
                    Log.e("Message", String.valueOf(photoList.get(i).getId()) + " " + photoList.get(i).getPicture());
                }
                myGetPhotoRequest.onGetPhotoResponse(photoList);
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {

            }
        });

    }
}
