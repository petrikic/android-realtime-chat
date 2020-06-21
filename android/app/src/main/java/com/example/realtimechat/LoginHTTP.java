package com.example.realtimechat;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.realtimechat.controller.TokenController;
import com.example.realtimechat.model.User;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginHTTP extends AsyncTask<Void, Void, Integer> {

    private final String username;
    private final String password;

    public LoginHTTP(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Integer doInBackground(Void... voids) {
        String urlParameters = "username=" + username + "&password=" + password;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        try {
            URL url = new URL("http://10.0.2.2:3000/auth/login");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            connection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode != connection.HTTP_OK) {
                return statusCode;
            }

            InputStream responseStream = new
                    BufferedInputStream(connection.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
            String response = stringBuilder.toString();
            if (!response.isEmpty())
                TokenController.setToken(response);
            return statusCode;
        } catch (IOException e) {
            e.printStackTrace();
            return 502;
        }
    }
}
