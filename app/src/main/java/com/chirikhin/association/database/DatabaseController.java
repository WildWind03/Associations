package com.chirikhin.association.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseController implements GetWordInterface {

    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public DatabaseController(Context context) {
        sqLiteOpenHelper = MySQLiteOpenHelper.newInstance(context);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    @Override
    public long getMaxId() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + MySQLiteOpenHelper.ID_COLUMN_NAME + " FROM " + MySQLiteOpenHelper.TABLE_NAME + " ORDER BY " +  MySQLiteOpenHelper.ID_COLUMN_NAME + " DESC LIMIT 1", null);

        cursor.moveToFirst();
        long maxId = cursor.getLong(0);
        cursor.close();

        return maxId;
    }

    @Override
    public String getWord(int id) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + MySQLiteOpenHelper.WORD_COLUMN_NAME + " FROM " + MySQLiteOpenHelper.TABLE_NAME + " WHERE " + MySQLiteOpenHelper.ID_COLUMN_NAME + " = " + String.valueOf(id), null);

        cursor.moveToFirst();
        String word = cursor.getString(0);

        cursor.close();

        return word;
    }

    public void addNewWord(String word) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteOpenHelper.WORD_COLUMN_NAME, word);

        sqLiteDatabase.insert(MySQLiteOpenHelper.TABLE_NAME, MySQLiteOpenHelper.WORD_COLUMN_NAME, contentValues);
    }

    public void close() {
        sqLiteDatabase.close();
    }
}
