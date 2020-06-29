package com.example.realtimechat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.UnicodeSetSpanner;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.App;
import com.example.realtimechat.database.ControllerDB;
import com.example.realtimechat.model.UploadFile;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Settings extends AppCompatActivity {

    ControllerDB controllerDB;
    private Button btnSelect;
    private Button btnSave;
    private ImageView imageProfile;
    private Uri uriImage;
    private String base64Image;
    private static final int SELECT_CONTENT_IMG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        controllerDB = ControllerDB.getInstance();

        imageProfile = findViewById(R.id.profile_photo);

        String urlImg = controllerDB.getProfile();

        if(urlImg.substring(0, 4).equals("http")){
            Picasso.get().load(urlImg).into(imageProfile);
        } else {
            Bitmap bitmap = convert(urlImg);
            imageProfile.setImageBitmap(bitmap);
        }

        btnSelect = findViewById(R.id.select_profile);
        btnSelect.setAlpha(0);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto(v);
            }
        });
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(base64Image != null) {
                    controllerDB.updateProfile(base64Image);
                    upload();
                    finish();
                } else {
                    Log.d("teste", controllerDB.getProfile());
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_CONTENT_IMG) {
            if(resultCode == RESULT_OK) {
                uriImage = data.getData();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                base64Image = convert(resizedBitmap);
                imageProfile.setImageBitmap(bitmap);
            }
        }
    }

    private void selectPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_CONTENT_IMG);
    }


    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap convert(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void upload() {
        try {
            UploadFile uploadFile = new UploadFile(base64Image);
                    uploadFile.execute(App.APLICATION_ADDRESS + "/upload");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
