package tech.photoboard.photoboard.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    boolean logInSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.et_username_login);
        passwordEditText = (EditText) findViewById(R.id.et_password_login);
        loginButton = (Button) findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {
                    User user = new User(email,password);
//loggIn(user)
                    if(true) {
                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Introduce all the empty fields", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public boolean loggIn(User user){

        logInSuccess = false;

        Call<tech.photoboard.photoboard.Classes.Response> logResponse = retrofitAPI.login(user);
        logResponse.enqueue(new Callback<tech.photoboard.photoboard.Classes.Response>() {

            @Override
            public void onResponse(Call<tech.photoboard.photoboard.Classes.Response> call,
                                   Response<tech.photoboard.photoboard.Classes.Response> response) {
                Boolean success = response.body().isSuccess();
                if(success) logInSuccess = true;
            }

            @Override
            public void onFailure(Call<tech.photoboard.photoboard.Classes.Response> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Unreachable Server",Toast.LENGTH_SHORT);
            }

        });

        return logInSuccess;

    }

}
