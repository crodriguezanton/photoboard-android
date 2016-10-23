package tech.photoboard.photoboard.API;

import org.json.JSONObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pc1 on 23/10/2016.
 */

public class ApiClient {

    public static final String BASE_URL = "http://10.180.16.62:8000/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}