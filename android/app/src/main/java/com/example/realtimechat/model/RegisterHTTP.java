package com.example.realtimechat.model;

import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.App;
import com.example.realtimechat.controller.TokenController;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RegisterHTTP extends AsyncTask<Void, Void, Integer> {

    private final String username;
    private final String password;

    public RegisterHTTP(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Integer doInBackground(Void... voids) {

        TokenController tokenController = TokenController.getInstance();
        String urlParameters = "username=" + username + "&password=" + password;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        try {
            URL url = new URL(App.APLICATION_ADDRESS + "/auth/register");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            connection.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(postData);


            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode != connection.HTTP_OK) {
                return statusCode;
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            String response = scanner.next();

            if (!response.isEmpty())
                tokenController.setToken(response);

            return statusCode;
        } catch (IOException e) {
            e.printStackTrace();
            return 502;
        }
    }
}