package tech.photoboard.photoboard.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.System.out;

/**
 * Created by pc1 on 23/10/2016.
 */

public class User implements Parcelable {
    String email;
    String password;

    public User(){}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User (Parcel in) {
        readFromParcel(in);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
    }
    private void readFromParcel(Parcel in) {
        email = in.readString();
        password = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>(){
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };



}
