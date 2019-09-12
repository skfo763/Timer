package com.timer_v3.user.timer_practice.MyTimer;

public class TestData {

    private  int CID;
    private  int TID;
    private  String title;
    private  int time;

    TestData(int i_TID, int i_CID, String i_title, int i_time) {
        TID = i_TID;
        CID = i_CID;
        title = i_title;
        time = i_time;
    }

    TestData() {
        TID = CID = time = -1;
        title = "";
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

    public  void setCID(int CID) {
        this.CID = CID;
    }

    public  void setTID(int TID) {
        this.TID = TID;
    }

    public  void setTime(int time) {
        this.time = time;
    }

    public  void setTitle(String title) {
        this.title = title;
    }
}
