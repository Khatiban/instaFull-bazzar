package com.fury.instafull;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fury on 8/28/2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "data_base64EncodedPublicKey.db";
    public static final String TBL_NAME = "Data_base64EncodedPublicKey";

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TBL_NAME +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT , Name TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public boolean insertData(String n, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Name", n);
        cv.put("Id", id);

        long result = db.insert(TBL_NAME, null, cv);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TBL_NAME, "Id = ?", new String[]{id});

        if (result == 0)
            return false;
        else
            return true;
    }

    public boolean updateData(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Id", id);
        cv.put("Name", name);

        long result = db.update(TBL_NAME, cv, "Id = ?", new String[]{id});

        if (result < 1)
            return false;
        else
            return true;
    }

    public Cursor showAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from " + TBL_NAME, null);

        return result;
    }

}
