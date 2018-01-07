package com.example.dell.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
public class database extends SQLiteOpenHelper{
    private static final String DB_NAME = "testsql1";
    public static final String TABLE_NAME = "Mytable2";
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
    public ArrayList<Person> myfind(HashMap<filtersData.filterType, String> filters) {
        db = getWritableDatabase();
        String query = "select * from " + TABLE_NAME+ " where ";
        ArrayList<Person> result = new ArrayList<Person>();
        int index = 0;
        for (HashMap.Entry<filtersData.filterType, String> entry : filters.entrySet()) {
            switch (entry.getKey()) {
                case first_char_filter:
                    if (entry.getValue().length() != 0)
                        if (index == 0) {
                            index++;
                            query += "first_char == '" + entry.getValue() + "' ";
                        } else {
                            query += "and first_char == '" + entry.getValue() + "' ";
                        }
                    break;
                case native_place_modern_filter:
                    if (entry.getValue().length() != 0)
                        if (index == 0) {
                            index++;
                            query += "now_place == '" + entry.getValue() + "' ";
                        } else {
                            query += "and now_place == '" + entry.getValue() + "' ";
                        }
                    break;
                case native_place_ancient_filter:
                    if (entry.getValue().length() != 0)
                        if (index == 0) {
                            index++;
                            query += "old_place == '" + entry.getValue() + "' ";
                        } else {
                            query += "and old_place == '" + entry.getValue() + "' ";
                        }
                    break;
                case sex_filter:
                    if (entry.getValue().length() != 0)
                        if (index == 0) {
                            index++;
                            query += "sex == '" + entry.getValue() + "' ";
                        }
                        else {
                            query += "and sex == '" + entry.getValue() + "' ";
                        }
                    break;
                case camp_filter:
                    if (entry.getValue().length() != 0)
                        if (index == 0) {
                            index++;
                            query += "shili == '" + entry.getValue() + "' ";
                        } else {
                            query += "and shili == '" + entry.getValue() + "' ";
                        }
                    break;
                case name_filter:
                    if (entry.getValue().length() != 0)
                        if (index == 0) {
                            index++;
                            query += "name == '" + entry.getValue() + "' ";
                        } else {
                            query += "and name == '" + entry.getValue() + "' ";
                        }
                    break;
            }
        }
        if (query.equals("select * from " + TABLE_NAME + " where ")){
            query = "select * from " + TABLE_NAME;

        }
        Cursor cursor = db.rawQuery(query,
                null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String sex = cursor.getString(1);
            String first_char = cursor.getString(2);
            String shili= cursor.getString(3);
            String now_place = cursor.getString(4);
            String old_place= cursor.getString(5);
            int life = cursor.getInt(6);
            String description = cursor.getString(7);
            String picture = cursor.getString(8);
            Person person = new Person(first_char, name, shili, sex, old_place,
                    now_place, life, picture, description);
            result.add(person);
        }
        cursor.close();
        db.close();
        return result;
    }
}
