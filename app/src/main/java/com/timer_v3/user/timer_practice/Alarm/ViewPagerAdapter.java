package com.timer_v3.user.timer_practice.Alarm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> category_dt;
    private ArrayList<Integer> time_dt;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> datainput, ArrayList<Integer> timeinput) {
        super(fm);
        this.category_dt = datainput;
        this.time_dt = timeinput;
    }

    @Override
    public Fragment getItem(int i) {
        return TimerFrgament.newInstance(category_dt.get(i), time_dt.get(i), i);
    }

    @Override
    public int getCount() {
        return category_dt.size();
    }
}
