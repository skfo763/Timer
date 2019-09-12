package com.timer_v3.user.timer_practice;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomTestHolder extends RecyclerView.ViewHolder {
    public CheckBox checkBox;
    public TextView title, time, time_minute;
    public ImageButton timeup, timedown;

    public CustomTestHolder(View view) {
        super(view);
        checkBox = view.findViewById(R.id.item_test_checkbox);
        title = view.findViewById(R.id.item_test_title);
        time = view.findViewById(R.id.item_test_time);
        timeup = view.findViewById(R.id.item_test_timeup);
        timedown = view.findViewById(R.id.item_test_timedown);
        time_minute = view.findViewById(R.id.item_test_time_minute);
    }
}
