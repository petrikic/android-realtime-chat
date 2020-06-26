package com.example.realtimechat.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.App;
import com.example.realtimechat.model.Message;
import com.example.realtimechat.model.User;

import java.util.ArrayList;
import java.util.List;

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
        contentValues.put("userReferenceId", message.getUserReferenceId());
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
            token = c.getString(c.getColumnIndex("token"));

        return token;
    }

    public List<Message> getMessages(User user) {
        List<Message> messages = new ArrayList<Message>();
        String stmt = "SELECT * FROM message WHERE UserReferenceId = ?";

        Cursor c = db.rawQuery(stmt, new String[]{user.getId() + ""});
        if (c.moveToFirst()) {
            do {
                long messageId = c.getLong(c.getColumnIndex("messageId"));
                long userReferenceId = c.getLong(c.getColumnIndex("userReferenceId"));
                long senderId = c.getLong(c.getColumnIndex("senderId"));
                String text = c.getString(c.getColumnIndex("text"));
                long timestamp = c.getLong(c.getColumnIndex("timestamp"));

                Message message = new Message(messageId, userReferenceId, senderId, text, timestamp);
                messages.add(message);

            } while (c.moveToNext());
        }

        return messages;
    }
}
