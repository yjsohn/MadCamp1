package com.example.basicapplicationfunction.Gallery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Gallery.db";
    public static final String TABLE_NAME = "images";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOC = "location";
    public static final String COLUMN_DESCRIPTION = "description";
    private File tempFile;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME +
                "(path text, date text , location text, description text, primary key(path))");
    }

    public boolean insertPicture(String path, String date, String location, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            String sql = "insert or replace into images(path, date, location, description) values(?, ?, ?, ?)";
            Object[] params = {path, date, location, description};
            db.execSQL(sql, params);
        }

        return true;
    }
    public ArrayList<Picture> getAllImages() {

        ArrayList<Picture> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from images", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new Picture(res.getString(res.getColumnIndex(COLUMN_PATH))
                    ,res.getString(res.getColumnIndex(COLUMN_DATE))
                    ,res.getString(res.getColumnIndex(COLUMN_LOC)), res.getString(res.getColumnIndex(COLUMN_DESCRIPTION))));
            res.moveToNext();
        }

        return array_list;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
        onCreate(db);
    }
}
