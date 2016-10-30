package tech.photoboard.photoboard.API;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.R;
import tech.photoboard.photoboard.Utils.User;

/**
 * Created by pc1 on 23/10/2016.
 */

public class ThisIsNotARealClass extends Activity {

    boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String device = "";

        final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

        Call<TakePhotoResponse> takePhotoCall = retrofitAPI.takePhotoRequest();

        bool = true;

        takePhotoCall.enqueue(new Callback<TakePhotoResponse>() {
            @Override
            public void onResponse(Call<TakePhotoResponse> call, Response<TakePhotoResponse> response) {
                TakePhotoResponse photoResponse = response.body();
                if(photoResponse != null) {
                    if (photoResponse.getResponse().equals("OK")) {
                        Call<String> urlImage = retrofitAPI.getPhotoResquest(photoResponse.getId());
                        getImage(urlImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<TakePhotoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Callback failure", Toast.LENGTH_LONG);
            }
        });

        Call<String> loginCall = retrofitAPI.login(new User());

        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Intent
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    String url = null;

    public String getImage (Call<String> urlImage){
        while (bool) {
            urlImage.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String urlResponse = response.body();
                    if(urlResponse != null) {
                        bool = false;
                        url = urlResponse;
                        //save the image url, return it or whatever
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Callback failure", Toast.LENGTH_LONG);
                }
            });
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}