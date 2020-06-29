package com.example.realtimechat.controller;

import com.example.realtimechat.model.User;

import java.util.ArrayList;

public class OnlineController {
    private ArrayList<User> users;
    private static OnlineController onlineController;

    private OnlineController() {
        users = new ArrayList<User>();
    }

    public void addOne(User user) {
        users.add(user);
    }

    public void removeOne(User user) {
        for(User u : users) {
            if (user.getId() == u.getId()) {
                users.remove(u);
            }
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public static OnlineController getInstance() {
        if(onlineController == null)
            onlineController = new OnlineController();
        return onlineController;
    }


}
