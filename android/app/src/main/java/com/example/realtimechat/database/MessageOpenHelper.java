package com.example.realtimechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.App;

public class MessageOpenHelper extends SQLiteOpenHelper {

    private static MessageOpenHelper messageOpenHelper;

    private MessageOpenHelper(@Nullable Context context) {
        super(context, "Converse.io", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptDDL.tokenDB());
        db.execSQL(ScriptDDL.profileDB());
        db.execSQL(ScriptDDL.messageDB());
        db.execSQL(ScriptDDL.setProfile());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static MessageOpenHelper getInstance() {
        if(messageOpenHelper == null)
            messageOpenHelper = new MessageOpenHelper(App.getContext());
        return messageOpenHelper;
    }
}
