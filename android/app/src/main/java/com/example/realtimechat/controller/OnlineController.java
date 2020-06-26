package com.example.realtimechat.controller;

import com.example.realtimechat.model.User;

import java.util.ArrayList;

public class OnlineController {
    private static ArrayList<User> users = new ArrayList<User>();

    private OnlineController() {

    }

    public static void addOne(User user) {
        users.add(user);
    }

    public static void removeOne(User user) {
        for(User u : users) {
            if (user.getId() == u.getId()) {
                users.remove(u);
            }
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }


}
