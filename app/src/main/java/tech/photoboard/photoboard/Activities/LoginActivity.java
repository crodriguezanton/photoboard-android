package tech.photoboard.photoboard.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.photoboard.photoboard.API.ApiClient;
import tech.photoboard.photoboard.API.RetrofitAPI;
import tech.photoboard.photoboard.R;
import tech.photoboard.photoboard.Classes.User;

/**
 * Created by pc1 on 23/10/2016.
 */

public class LoginActivity extends Activity {

    final RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

    static final String LOGGED_IN = "LOGGED_IN";
    public static final String USER = "USER";

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private User user = new User();
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Checking if we are already logged in*/

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        String loggedIn = sharedPreferences.getString(LOGGED_IN,null);
        if(loggedIn!=null&&loggedIn.equals(LOGGED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        };

        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.et_username_login);
        passwordEditText = (EditText) findViewById(R.id.et_password_login);
        loginButton = (Button) findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setEnabled(false);

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {
                    user = new User(email,password);
                    //logIn(user);
                    newActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Introduce all the empty fields", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void logIn(User user){

        Call<tech.photoboard.photoboard.Classes.Response> logResponse = retrofitAPI.login(user);
        logResponse.enqueue(new Callback<tech.photoboard.photoboard.Classes.Response>() {

            @Override
            public void onResponse(Call<tech.photoboard.photoboard.Classes.Response> call,
                                   Response<tech.photoboard.photoboard.Classes.Response> response) {
                Boolean success = response.body().isSuccess();
                loginButton.setEnabled(true);
                if(success) newActivity();
                else Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<tech.photoboard.photoboard.Classes.Response> call, Throwable t) {
                loginButton.setEnabled(true);
                Toast.makeText(LoginActivity.this,"Unreachable Server",Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void newActivity() {
        editor.putString(LOGGED_IN, LOGGED_IN);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USER,json);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
