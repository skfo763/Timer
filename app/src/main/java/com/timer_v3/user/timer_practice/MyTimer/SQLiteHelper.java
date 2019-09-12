package com.timer_v3.user.timer_practice.MyTimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static SQLiteHelper sqLiteHelper = null;
    public static String DATABASE_NAME = "Timer_DB";
    public static int DB_VERSION = 3;
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
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName1 +
                " (CID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR(50), upload_time VARCHAR(50), " +
                "test_time VARCHAR(50), operator VARCHAR(50), " +
                "alarmState INTEGER);";
        String sql2 = "CREATE TABLE IF NOT EXISTS " + tableName2 +
                " (TID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CID INTEGER,  title VARCHAR(50), time INTEGER);";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName1);
        db.execSQL("DROP TABLE IF EXISTS " + tableName2);
        onCreate(db);
    }

    public void showTables() {
        String sql = "SELECT name FROM sqlite_master " +
                "WHERE type IN ('table', 'view') " +
                "AND name NOT LIKE 'sqlite_%' " +
                "UNION ALL SELECT name " +
                "FROM sqlite_temp_master WHERE type IN ('table', 'view') ORDER BY 1;";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            do {
                System.out.println(res.getString(res.getColumnIndex("name")));
            } while (res.moveToNext());
        }
    }

    public boolean insertToCategory(CategoryData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", data.getTitle());
        contentValues.put("upload_time", data.getUTime());
        contentValues.put("test_time", data.getTTime());
        contentValues.put("operator", data.getOperator());
        contentValues.put("alarmState", data.getalarmState());

        long result = db.insert(tableName1, null, contentValues);
        return result != -1;
    }

    public boolean insertToEachTest(TestData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CID", data.getCID());
        contentValues.put("title", data.getTitle());
        contentValues.put("time", data.getTime());
        long result = db.insert(tableName2, null, contentValues);
        return result != -1;
    }

    public void deleteCategory(int CID) {
        String sql = "DELETE FROM " + tableName1 + " WHERE CID = " + CID + ";";
        db.execSQL(sql);
        deleteEachTest(CID, -1);
    }

    public void deleteEachTest(int CID, int TID) {
        String sql;
        if(CID != -1 && TID == -1) {
            sql = "DELETE FROM " + tableName2 + " WHERE CID = " + CID + ";";
        } else {
            sql = "DELETE FROM " + tableName2 + " WHERE TID = " + TID + ";";
        }
        db.execSQL(sql);
    }

    public void deleteAllTest(int CID) {
        String sql = "DELETE FROM " + tableName2 + " WHERE CID = " + CID + ";";
        db.execSQL(sql);
    }

    public int searchWithModel(CategoryData data) {
        String sql = "SELECT CID FROM " + tableName1 + " WHERE title = '"+ data.getTitle() + "' and upload_time = '" + data.getUTime() + "' " +
                "and operator = '" + data.getOperator() + "';";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            return res.getInt(res.getColumnIndex("CID"));
        }
        return -1;
    }

    public List<CategoryData> searchCategory() {
        List<CategoryData> result = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName1 + ";";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            do {
                CategoryData data = new CategoryData();
                data.setCID(res.getInt(res.getColumnIndex("CID")));
                data.setTitle(res.getString(res.getColumnIndex("title")));
                data.setUpload_time(res.getString(res.getColumnIndex("upload_time")));
                data.setTest_time(res.getString(res.getColumnIndex("test_time")));
                data.setOperator(res.getString(res.getColumnIndex("operator")));
                data.setAlarmState(res.getInt(res.getColumnIndex("alarmState")));
                result.add(data);
            } while (res.moveToNext());
        }
        return result;
    }

    public CategoryData searchEachCategory(int category_id) {
        CategoryData data = new CategoryData();
        String sql = "SELECT * FROM " + tableName1 + " WHERE CID = " + category_id + ";";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            data.setCID(res.getInt(res.getColumnIndex("CID")));
            data.setTitle(res.getString(res.getColumnIndex("title")));
            data.setUpload_time(res.getString(res.getColumnIndex("upload_time")));
            data.setTest_time(res.getString(res.getColumnIndex("test_time")));
            data.setOperator(res.getString(res.getColumnIndex("operator")));
            data.setAlarmState(res.getInt(res.getColumnIndex("alarmState")));
        }
        return data;
    }

    public List<TestData> searchEachTest(int category_id) {
        List<TestData> result = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName2 + " WHERE CID = " + category_id + ";";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            do {
                TestData data = new TestData();
                data.setTID(res.getInt(res.getColumnIndex("TID")));
                data.setCID(res.getInt(res.getColumnIndex("CID")));
                data.setTitle(res.getString(res.getColumnIndex("title")));
                data.setTime(res.getInt(res.getColumnIndex("time")));
                result.add(data);
            } while (res.moveToNext());
        }
        return result;
    }

    public void searchAllTest() {
        String sql = "SELECT * FROM " + tableName2 + ";";
        Cursor res = db.rawQuery(sql, null);

        if(res.moveToFirst()) {
            do {
                TestData data = new TestData(res.getInt(res.getColumnIndex("TID")), res.getInt(res.getColumnIndex("CID")),
                        res.getString(res.getColumnIndex("title")), res.getInt(res.getColumnIndex("time")));

                System.out.println(data.getTID() + " " + data.getCID() + " " + data.getTitle() + " " + data.getTime());
            } while (res.moveToNext());
        }
    }

    public void updateCategory() {

    }

    public void updateEachTest() {

    }
}
