package com.example.gerro.gra;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "kawa.db";
    public static final String TABLE_NAME = "highscore_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "RESULT";


    SQLiteDatabase mSQLiteDatabase;

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, RESULT INTEGER);";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String create_table = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(create_table);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    public void insertResult(Integer result) {
        mSQLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RESULT", result);
        mSQLiteDatabase.insert(TABLE_NAME, null, contentValues);
        mSQLiteDatabase.close();
    }

    public Integer getResults() {
        mSQLiteDatabase = this.getReadableDatabase();
        String selectall = "SELECT MAX(RESULT) FROM highscore_table;";
        Integer result = 0;
        Cursor cursor = mSQLiteDatabase.rawQuery(selectall, null);
        if(cursor.moveToFirst()) {
            do {
                result = cursor.getInt(0);
                //Log.e("WYNIKI: ", result.toString());
            } while(cursor.moveToNext());
        }
        mSQLiteDatabase.close();
        return result;
    }
}
