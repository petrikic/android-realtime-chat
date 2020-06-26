package com.example.realtimechat.database;

public class ScriptDDL {
    private ScriptDDL() {
    }

    public static final String tokenDB() {
        String stmt = "CREATE TABLE IF NOT EXISTS token (\n" +
                "  token TEXT\n" +
                ");";
        return stmt;
    }

    public static final String messageDB() {
        String stmt = "CREATE TABLE IF NOT EXISTS message (\n" +
                "  messageId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  heId INTEGER,\n" +
                "  senderId INTEGER,\n" +
                "  text TEXT,\n" +
                "  timestamp INTEGER\n" +
                ");";
        return stmt;
    }
}
