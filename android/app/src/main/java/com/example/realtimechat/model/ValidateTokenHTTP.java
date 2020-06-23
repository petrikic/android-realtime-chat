package com.example.realtimechat.model;

import android.os.AsyncTask;

import com.example.realtimechat.controller.TokenController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ValidateTokenHTTP extends AsyncTask <Void, Void, Integer> {
    @Override
    protected Integer doInBackground(Void... voids) {

        try {
            URL url = new URL("http://10.0.2.2:3000/auth/validatetoken");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("authorization", "Bearer " + TokenController.getToken());
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(false);
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return 502;
        }
    }
}
