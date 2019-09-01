package com.timer_v3.user.timer_practice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowResultActivity extends AppCompatActivity {

    TextView title, starttime, endtime, totaltime, goback;
    RecyclerView recyclerView;
    String testname;
    long sTime, cTime, ttime;
    ArrayList<String> category_data = new ArrayList<>();
    ArrayList<Integer> time_data = new ArrayList<>();
    ArrayList<Integer> result_data = new ArrayList<>();
    ArrayList<Integer> final_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        testname = getIntent().getStringExtra("testname");
        cTime = getIntent().getLongExtra("startTime", System.currentTimeMillis());
        sTime = System.currentTimeMillis();
        category_data = getIntent().getStringArrayListExtra("category_data");
        time_data = getIntent().getIntegerArrayListExtra("time_data");
        result_data = getIntent().getIntegerArrayListExtra("result_data");

        title = findViewById(R.id.showresult_title);
        starttime = findViewById(R.id.showresult_starttime);
        endtime = findViewById(R.id.showresult_endtime);
        totaltime = findViewById(R.id.showresult_totaltime);
        goback = findViewById(R.id.showresult_goback);
        recyclerView = findViewById(R.id.showresult_recyclerview);

        for(int i=0; i<time_data.size(); i++) {
            int temp = time_data.get(i)*60 - result_data.get(i);
            final_data.add(i, temp);
            ttime += temp;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat cdate = new SimpleDateFormat("YYYY년 MM월 dd일 hh시 mm분 ss초");
        String s_str = cdate.format(new Date(sTime));
        String c_str = cdate.format(new Date(cTime));
        title.setText(testname);
        starttime.setText(s_str);
        endtime.setText(c_str);
        totaltime.setText(String.valueOf(ttime).concat("초"));

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(ShowResultActivity.this));
        recyclerView.setAdapter(new MyResultAdapter(category_data, final_data));
    }

    private class MyResultAdapter extends RecyclerView.Adapter {
        ArrayList<String> c_dt;
        ArrayList<Integer> r_dt;

        public MyResultAdapter(ArrayList<String> category_data, ArrayList<Integer> result_data) {
            this.c_dt = category_data;
            this.r_dt = result_data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_result, viewGroup, false);
            return new MyResultCustomHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int min = r_dt.get(i) / 60;
            int sec = r_dt.get(i) % 60;

            ((MyResultCustomHolder)viewHolder).title.setText(c_dt.get(i));
            ((MyResultCustomHolder)viewHolder).time.setText(String.valueOf(min).concat("분 ").concat(String.valueOf(sec).concat("초")));
        }

        @Override
        public int getItemCount() {
            return r_dt.size();
        }
    }

    private class MyResultCustomHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        MyResultCustomHolder(View view) {
            super(view);
            title = view.findViewById(R.id.item_result_title);
            time = view.findViewById(R.id.item_result_time);
        }
    }
}
