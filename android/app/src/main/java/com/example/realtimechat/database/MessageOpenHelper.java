package com.example.realtimechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MessageOpenHelper extends SQLiteOpenHelper {
    public MessageOpenHelper(@Nullable Context context) {
        super(context, "Converse.io", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptDDL.tokenDB());
        db.execSQL(ScriptDDL.messageDB());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
