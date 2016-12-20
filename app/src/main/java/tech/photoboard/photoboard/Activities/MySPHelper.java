package tech.photoboard.photoboard.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tech.photoboard.photoboard.Classes.Subject;
import tech.photoboard.photoboard.Classes.User;

/**
 * Created by Elias on 15/12/2016.
 */

public class MySPHelper {
    private static final String ADD_FAVORITE_IN = "ADD_FAVORITE_IN";
    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String USER = "USER";
    private static final String FAV_MODE = "FAV_MODE";

    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public MySPHelper (Activity activity) {
        this.activity = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public String getCurrentSubject() {
        return sharedPreferences.getString(SUBJECT_ID, null);
    }
    public void setCurrentSubject (Subject subject) {
        if (subject != null) {
            editor.putString(SUBJECT_ID, subject.getShort_name());
            editor.commit();
        } else if (getCurrentSubject() != null) {
            editor.remove(SUBJECT_ID);
            editor.commit();
        }
    }

    public ArrayList<String> getFavPhotos (String actualSubject) {

        String json = sharedPreferences.getString(ADD_FAVORITE_IN + actualSubject, null);
        if(json == null) {
            Toast.makeText(activity, "No favorites added.", Toast.LENGTH_SHORT).show();
            return null;
        }
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setFavPhotos (ArrayList<String> favPhotos) {
        String json = gson.toJson(favPhotos);
        editor.putString(ADD_FAVORITE_IN + getCurrentSubject(), json);
        editor.commit();
    }

    public void setUser(User user) {
        String json = gson.toJson(user);
        editor.putString(USER,json);
        editor.commit();
    }
    public User getUser () {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER, null);
        return gson.fromJson(json, User.class);
    }
    public void removeUser() {
        editor.remove(USER);
    }

    public boolean getLoggedInState() {
        return sharedPreferences.getBoolean(LOGGED_IN, false);
    }
    public void setLoggedIn(boolean bool) {
        if (bool) {
            editor.putBoolean(LOGGED_IN, bool);
        } else {
            editor.remove(LOGGED_IN);
        }
        editor.commit();
    }

    public void setFavMode(boolean bool) {
        editor.putBoolean(FAV_MODE, bool);
        editor.commit();
    }
    public boolean getFavMode() {
        return sharedPreferences.getBoolean(FAV_MODE, false);
    }
}

