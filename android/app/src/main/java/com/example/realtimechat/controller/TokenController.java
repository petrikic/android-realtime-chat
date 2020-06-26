package com.example.realtimechat.controller;

import com.example.realtimechat.database.ControllerDB;

public class TokenController {

    private static String mToken;
    private static ControllerDB controllerDB;
    private static TokenController tokenController;

    private TokenController(){
        createController();
    }

    private void createController() {
        controllerDB = ControllerDB.getInstance();
    }

    public static synchronized TokenController getInstance() {
        if(tokenController == null)
            tokenController = new TokenController();
        return tokenController;
    }

    public String getToken () {
        if(mToken == null){
            mToken = controllerDB.getToken();
        }
        return mToken;
    }

    public void setToken (String token) {
        if(controllerDB.getToken() != null)
            controllerDB.update(token);
        else
            controllerDB.insert(token);
        mToken = token;
    }

    public boolean isEmpty() {
        return getToken() == null;
    }
}
