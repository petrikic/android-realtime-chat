package com.example.realtimechat.controller;

import com.example.realtimechat.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class OnlineController {
    private static ArrayList<User> users = new ArrayList<User>();

    private OnlineController() {

    }

    public static void setOnline(JSONArray jUsers) throws JSONException, MalformedURLException {
        for(int i = 0; i < jUsers.length(); i++) {
            JSONObject jUser = jUsers.getJSONObject(i);
            long id = jUser.getLong("id");
            String username = jUser.getString("username");
            URL urlPhoto = new URL(jUser.getString("urlPhoto"));
            User u = new User(21231, "https://www.google.com/", "username");
            //users.add(u);
        }
    }

    public static void addOne(User user) {
        users.add(user);
    }

    public static ArrayList<User> getUsers() {
        return users;
    }


}
