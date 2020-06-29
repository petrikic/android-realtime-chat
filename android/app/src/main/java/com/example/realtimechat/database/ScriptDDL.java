package com.example.realtimechat.database;

import com.example.App;

public class ScriptDDL {
    private ScriptDDL() {
    }

    public static final String tokenDB() {
        String stmt = "CREATE TABLE IF NOT EXISTS token (\n" +
                "  token TEXT\n" +
                ");";
        return stmt;
    }

    public static final String profileDB() {
        String stmt = "CREATE TABLE IF NOT EXISTS profile (\n" +
                "   image TEXT\n" +
                ");";
        return stmt;
    }

    public static final String setProfile() {
        String stmt = "INSERT INTO profile VALUES(\"" + App.APLICATION_ADDRESS + "/unknow.jpg\")\n";
        return stmt;
    }

    public static final String messageDB() {
        String stmt = "CREATE TABLE IF NOT EXISTS message (\n" +
                "  messageId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  userReferenceId INTEGER,\n" +
                "  receiverId INTEGER,\n" +
                "  senderId INTEGER,\n" +
                "  text TEXT,\n" +
                "  timestamp INTEGER\n" +
                ");";
        return stmt;
    }
}
