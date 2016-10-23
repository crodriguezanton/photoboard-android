package tech.photoboard.photoboard.API;

import retrofit2.Retrofit;

/**
 * Created by pc1 on 23/10/2016.
 */

public class ApiClient {

    private static final String BASE_URL = "https://www.api.photoboard.tech/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        }
        return retrofit;
    }
}