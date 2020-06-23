package com.example.realtimechat.controller;

public class TokenController {
    private static String mToken;

    private TokenController(){

    }

    public static synchronized String getToken () {
        return mToken;
    }

    public static synchronized void setToken (String token) {
        mToken = token;
    }

    public static boolean isEmpty() {
        return getToken() == null;
    }
}
