package com.example.realtimechat.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.App;
import com.example.realtimechat.model.Message;

public class ControllerDB {

    private static SQLiteDatabase db;
    private static MessageOpenHelper messageOpenHelper;
    private static ControllerDB controllerDB;

    private ControllerDB(Context context) {
        createConnection(context.getApplicationContext());
    }

    private static void createConnection(Context context) {
        try {

            messageOpenHelper = new MessageOpenHelper(context);
            db = messageOpenHelper.getWritableDatabase();

        } catch (SQLException ex) {

            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("Error");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();

        }
    }

    public static ControllerDB getInstance() {
        if (controllerDB == null)
            controllerDB = new ControllerDB(App.getContext());
        return controllerDB;
    }

    public void insert(String token) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("token", token);

        db.insertOrThrow("token", null, contentValues);
    }

    public void insert(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("heId", message.getUserReferenceId());
        contentValues.put("senderId", message.getSenderId());
        contentValues.put("text", message.getText());
        contentValues.put("timestamp", message.getTimestamp());

        db.insertOrThrow("message", null, contentValues);
    }

    public void update(String token) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("token", token);

        db.update("token", contentValues, null, null);
    }

    public String getToken() {
        String stmt = "SELECT token FROM token";
        String token = null;

        Cursor c = db.rawQuery(stmt, null);
        if (c.moveToFirst())
            token = c.getString(0);

        return token;
    }
}
