package com.example.dell.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
public class database extends SQLiteOpenHelper{
    private static final String DB_NAME = "AppSql";
    public static final String TABLE_NAME = "Diary";
    private static final int DB_VERSION = 3;
    private SQLiteDatabase db;
    private Context mcontext;
    public database(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //创建表
        //标题，内容，图片，表情，日期
        String todo = "create table if not exists " + TABLE_NAME + " (title text primary key, des text , url text," +
                "face text, date text)";
        // String todo = "drop table mytable";
        sqLiteDatabase.execSQL(todo);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {
        db = sqLiteDatabase;
    }

    public void myinsert( String title ,String des, String url, String face,
                  String date ){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (title != null)
            cv.put("title", title);
        if (des != null)
            cv.put("des", des);
        if (url != null)
            cv.put("url", url);
        if (face != null)
            cv.put("face", face);
        if (date != null)
            cv.put("date", date);

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void myupdate( String title ,String des, String url, String face,
                  String date){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
         if (title != null)
            cv.put("title", title);
        if (des != null)
            cv.put("des", des);
        if (url != null)
            cv.put("url", url);
        if (face != null)
            cv.put("face", face);
        if (date != null)
            cv.put("date", date);
        String whereClause = "title=?";
        String[] whereArgs = {title};
        db.update(TABLE_NAME,cv,whereClause,whereArgs);
        db.close();
    }

    public void deleteDB(String title) {
        db = getWritableDatabase();
        String whereClause = "title=?";
        String[] whereArgs = {title};

        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();

    }
    public ArrayList<Diary> myfind(String toFind) {
        db = getWritableDatabase();
        String query = "select * from " + TABLE_NAME + " where title like '%?%' or des like '%?%'";

        Cursor cursor = db.rawQuery(query,
                new String[]{toFind, toFind});
        ArrayList<Diary> result = new ArrayList<Diary>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            String content = cursor.getString(1);
            String img_url = cursor.getString(2);
            String emotion = cursor.getString(3);
            String date = cursor.getString(4);
            Diary Diary = new Diary(title, content, img_url, emotion, date);
            result.add(Diary);
        }
        cursor.close();
        db.close();
        return result;
    }
}
