package com.example.realtimechat.model;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.App;
import com.example.realtimechat.controller.TokenController;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UploadFile extends AsyncTask<String, Void, Void> {


    private String Content;
    private String Error = null;
    private String data = "";
    private BufferedReader reader;
    private String image;

    public UploadFile(String image) {
        this.image = image;
    }

    protected void onPreExecute() {

        try {

            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + "data:image/png;base64," + image;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    protected Void doInBackground(String... urls) {

        TokenController tokenController = TokenController.getInstance();

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("authorization", "Bearer " + tokenController.getToken());
            connection.setRequestProperty("Content-Length", "" + data.getBytes().length);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


            writer.write(data);
            writer.flush();
            writer.close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Content = sb.toString();
        } catch (Exception ex) {
            Error = ex.getMessage();
        }
        return null;

    }

    protected void onPostExecute(Void unused) {

        try {

            if (Content != null) {
                JSONObject jsonResponse = new JSONObject(Content);
                String status = jsonResponse.getString("status");
                if ("200".equals(status)) {

                    Toast.makeText(App.getContext(), "Imagem alterada com sucesso.", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(App.getContext(), "Algo deu errado, tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
