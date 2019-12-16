package com.example.mu338.stampinseoul;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "StampInSeoulDB";
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  db.execSQL("DROP TABLE IF EXISTS userTBL");
        String str = "CREATE TABLE userTBL("
                + "userId TEXT PRIMARY KEY,"
                + "userName TEXT, "
                + "profileImage TEXT); ";
        db.execSQL(str);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userTBL");
        onCreate(db);
    }
}
