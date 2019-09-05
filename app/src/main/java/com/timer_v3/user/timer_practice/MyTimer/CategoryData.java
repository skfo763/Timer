package com.timer_v3.user.timer_practice.MyTimer;

public class CategoryData {

    public static int CID;
    public static String title;
    public static String upload_time;

    public CategoryData(int i_CID, String i_title, String i_time) {
        CID = i_CID;
        title = i_title;
        upload_time = i_time;
    }

    public int getCID() {
        return CID;
    }

    public String getTitle() {
        return title;
    }

    public String getUTime() {
        return upload_time;
    }

}
