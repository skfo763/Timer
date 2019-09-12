package com.timer_v3.user.timer_practice.MyTimer;

public class CategoryData {

    private  int CID;
    private  String title;
    private  String upload_time;
    private  String test_time;
    private  String operator;
    private  int alarmState;

    CategoryData() {
        CID = alarmState = -1;
        title = upload_time = test_time = operator = "";
    }

    CategoryData(int i_CID, String i_title, String i_time, String t_time, String i_operator, int aState) {
        CID = i_CID;
        title = i_title;
        upload_time = i_time;
        test_time = t_time;
        operator = i_operator;
        alarmState = aState;
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

    public String getTTime() {
        return test_time;
    }

    public String getOperator() {
        return operator;
    }

    public int getalarmState() {
        return alarmState;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public  void setOperator(String operator) {
        this.operator = operator;
    }

    public  void setTest_time(String test_time) {
        this.test_time = test_time;
    }

    public  void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }
}
