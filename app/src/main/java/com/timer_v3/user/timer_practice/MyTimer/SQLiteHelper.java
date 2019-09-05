package com.timer_v3.user.timer_practice.MyTimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static SQLiteHelper sqLiteHelper = null;
    public static String DATABASE_NAME = "Timer_DB";
    public static int DB_VERSION = 1;
    public static String tableName1 = "Categories";
    public static String tableName2 = "EachTests";

    private SQLiteDatabase db;

    public static SQLiteHelper getInstance(Context context) {
        if(sqLiteHelper == null){
            sqLiteHelper = new SQLiteHelper(context);
        }

        return sqLiteHelper;
    }

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName1 + " (CID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(50), upload_time VARCHAR(50));";
        String sql2 = "CREATE TABLE IF NOT EXISTS " + tableName2 + " (TID INTEGER PRIMARY KEY AUTOINCREMENT, CID INTEGER,  title VARCHAR(50), time INTEGER, upload_time VARCHAR(50));";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName1);
        db.execSQL("DROP TABLE IF EXISTS " + tableName2);
        onCreate(db);
    }

    public boolean insertToCategory(CategoryData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", data.getTitle());
        contentValues.put("upload_time", data.getUTime());
        long result = db.insert(tableName1, null, contentValues);
        return result != -1;
    }

    public boolean insertToEachTest(TestData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CID", data.getCID());
        contentValues.put("title", data.getTitle());
        contentValues.put("time", data.getTime());
        contentValues.put("upload_time", data.getUTime());
        long result = db.insert(tableName1, null, contentValues);
        return result != -1;
    }

    public void deleteCategory(int CID) {
        String sql = "DELETE FROM " + tableName1 + "WHERE CID = " + CID + ";";
        db.execSQL(sql);
        deleteEachTest(CID, -1);

    }

    public void deleteEachTest(int CID, int TID) {
        String sql;
        if(CID != -1 && TID == -1) {
            sql = "DELETE FROM " + tableName2 + "WHERE CID = " + CID + ";";
        } else {
            sql = "DELETE FROM " + tableName2 + "WHERE TID = " + TID + ";";
        }
        db.execSQL(sql);
    }

    public ArrayList<CategoryData> searchCategory() {
        ArrayList<CategoryData> result = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName1 + ";" ;
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            do {
                CategoryData data = new CategoryData(res.getInt(0), res.getString(1), res.getString(2));
                System.out.println(data.getCID() + " " + data.getTitle() + " " + data.getUTime());
                result.add(data);
            } while (res.moveToNext());
        }

        return result;
    }

    public ArrayList<TestData> searchEachTest(int category_id) {
        ArrayList<TestData> result = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName2 + "WHERE CID = " + category_id + ";";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            do {
                TestData data = new TestData(res.getInt(0), res.getInt(1), res.getString(2), res.getInt(3), res.getString(4));
                result.add(data);
            } while (res.moveToNext());
        }
        return result;
    }
}
