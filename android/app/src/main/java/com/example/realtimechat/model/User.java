package com.example.realtimechat.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
    private long id;
    private String urlPhoto;
    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(int id, String urlPhoto, String username) {
        this.id = id;
        this.urlPhoto = urlPhoto;
        this.username = username;
    }

    protected User(Parcel in) {
        id = in.readLong();
        urlPhoto = in.readString();
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrlPhoto() {
        return this.urlPhoto;
    }

    public long getId() {
        return  this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(urlPhoto);
        dest.writeString(username);
        dest.writeString(password);
    }
}
