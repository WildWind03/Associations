package com.chirikhin.association.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WORD_DATABASE";
    public static final String TABLE_NAME = "WORD_TABLE";

    public static final String ID_COLUMN_NAME = "_ID";
    public static final String WORD_COLUMN_NAME = "WORDS";

    public static final int DATABASE_VERSION = 1;

    public static SQLiteOpenHelper newInstance(Context context) {
        return new MySQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( " +
                ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WORD_COLUMN_NAME +  " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
