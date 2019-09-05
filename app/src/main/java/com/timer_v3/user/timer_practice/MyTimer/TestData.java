package com.timer_v3.user.timer_practice.MyTimer;

public class TestData {

    public static int CID;
    public static int TID;
    public static String title;
    public static int time;
    public static String upload_time;

    public TestData(int i_TID, int i_CID, String i_title, int i_time, String i_utime) {
        TID = i_TID;
        CID = i_CID;
        title = i_title;
        time = i_time;
        upload_time = i_utime;
    }

    public int getTID() {
        return TID;
    }

    public int getCID() {
        return CID;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public String getUTime() {
        return upload_time;
    }
}
