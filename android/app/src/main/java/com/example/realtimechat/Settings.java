package com.example.realtimechat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.realtimechat.model.UploadFile;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Settings extends AppCompatActivity {

    private Button btnSelect;
    private Button btnSave;
    private Uri mSelectedUri;
    private ImageView imageProfile;
    private String base64Image;
    private static final int SELECT_CONTENT_IMG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageProfile = findViewById(R.id.profile_photo);
        
        btnSelect = findViewById(R.id.select_profile);
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
                    upload();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_CONTENT_IMG) {
            if(resultCode == RESULT_OK) {
                Uri uriImage = data.getData();
                String[] rows = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uriImage, rows, null, null, null);
                cursor.moveToFirst();

                int index = cursor.getColumnIndex(rows[0]);
                String path = cursor.getString(index);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                base64Image = convertBitmapToString(bitmap);
                btnSelect.setAlpha(0);
                Picasso.get().load(uriImage).into(imageProfile);
            }
        }
    }

    private void selectPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_CONTENT_IMG);
    }


    public String convertBitmapToString(Bitmap bitmap){
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
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
