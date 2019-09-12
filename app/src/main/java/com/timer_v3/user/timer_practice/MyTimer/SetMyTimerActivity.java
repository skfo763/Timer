package com.timer_v3.user.timer_practice.MyTimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.timer_v3.user.timer_practice.Alarm.StartTimerActivity;
import com.timer_v3.user.timer_practice.CustomTestHolder;
import com.timer_v3.user.timer_practice.R;
import com.timer_v3.user.timer_practice.SetTimerActivity;

import java.util.ArrayList;
import java.util.List;

public class SetMyTimerActivity extends AppCompatActivity {
    
    private TextView testTime, uploadTime, operator, alarmState;
    private AdView adView;
    private TextView title, alltime;
    private Button start, revise;
    private RecyclerView recyclerView;
    private int CID, totaltime;
    private SQLiteHelper helper;
    private CategoryData cat_data;
    private List<TestData> test_data;
    public ArrayList<String> data = new ArrayList<>();
    public ArrayList<Integer> time_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_timer);
        CID = getIntent().getIntExtra("CID", -1);
        helper = SQLiteHelper.getInstance(SetMyTimerActivity.this);
        totaltime = 0;

        testTime = findViewById(R.id.set_mytimer_testtime);
        uploadTime = findViewById(R.id.set_mytimer_uploadtime);
        operator = findViewById(R.id.set_mytimer_operator);
        alarmState = findViewById(R.id.set_mytimer_alarmstate);

        adView = findViewById(R.id.set_mytimer_adview);
        title = findViewById(R.id.set_mytimer_title);
        alltime = findViewById(R.id.set_mytimer_alltime);
        start = findViewById(R.id.set_mytimer_start);
        recyclerView = findViewById(R.id.set_mytimer_recyclerview);
        revise = findViewById(R.id.set_mytimer_revise);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        if(CID == -1) {
            execeptionHandler();
        } else {
            cat_data = helper.searchEachCategory(CID);

            UISetting();
            test_data = helper.searchEachTest(cat_data.getCID());

            for(int i=0; i<test_data.size(); i++) {
                data.add(test_data.get(i).getTitle());
                time_data.add(test_data.get(i).getTime());
                totaltime += test_data.get(i).getTime();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(SetMyTimerActivity.this));
            recyclerView.setAdapter(new SetMyTestAdapter());
            alltime.setText(String.valueOf(totaltime));

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SetMyTimerActivity.this, StartTimerActivity.class);
                    intent.putExtra("test_name", cat_data.getTitle());
                    intent.putIntegerArrayListExtra("time_data", time_data);
                    intent.putStringArrayListExtra("category_data", data);
                    startActivity(intent);
                    finish();
                }
            });

            revise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SetMyTimerActivity.this, AddMyCategoryActivity.class);
                    intent.putExtra("CID", CID);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    private void UISetting() {
        title.setText(cat_data.getTitle());
        testTime.setText(cat_data.getTTime());
        uploadTime.setText(cat_data.getUTime());
        operator.setText(cat_data.getOperator());

        if(cat_data.getalarmState() == 0) {
            alarmState.setText("아니오");
        } else {
            alarmState.setText("예");
        }
    }

    private void execeptionHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SetMyTimerActivity.this);
        alertDialog.setTitle("시험 정보를 불러올 수 없습니다. 뒤로 가시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    private class SetMyTestAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_test_category, viewGroup, false);
            return new CustomTestHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
            ((CustomTestHolder)viewHolder).title.setText(data.get(i));
            ((CustomTestHolder)viewHolder).time.setText(String.valueOf(time_data.get(i)));
            ((CustomTestHolder)viewHolder).checkBox.setChecked(true);

            ((CustomTestHolder)viewHolder).timeup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CustomTestHolder)viewHolder).checkBox.isChecked()) {
                        int t_data = Integer.parseInt(((CustomTestHolder) viewHolder).time.getText().toString()) + 1;
                        int alltime_data = Integer.parseInt(alltime.getText().toString()) + 1;
                        time_data.set(i, t_data);
                        ((CustomTestHolder) viewHolder).time.setText(String.valueOf(t_data));
                        alltime.setText(String.valueOf(alltime_data));
                    }
                }
            });

            ((CustomTestHolder)viewHolder).timedown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CustomTestHolder)viewHolder).checkBox.isChecked()) {
                        int t_data = Integer.parseInt(((CustomTestHolder) viewHolder).time.getText().toString()) - 1;
                        int alltime_data = Integer.parseInt(alltime.getText().toString()) - 1;
                        time_data.set(i, t_data);
                        ((CustomTestHolder) viewHolder).time.setText(String.valueOf(t_data));
                        alltime.setText(String.valueOf(alltime_data));
                    }
                }
            });

            ((CustomTestHolder)viewHolder).checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CustomTestHolder)viewHolder).checkBox.isChecked()) {
                        int alltime_data = Integer.parseInt(alltime.getText().toString()) + Integer.parseInt(((CustomTestHolder)viewHolder).time.getText().toString());
                        time_data.set(i, time_data.get(i));
                        alltime.setText(String.valueOf(alltime_data));
                    } else {
                        int alltime_data = Integer.parseInt(alltime.getText().toString()) - Integer.parseInt(((CustomTestHolder)viewHolder).time.getText().toString());
                        time_data.set(i, 0);
                        alltime.setText(String.valueOf(alltime_data));
                    }
                }
            });

            ((CustomTestHolder)viewHolder).time_minute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final EditText edittext = new EditText(SetMyTimerActivity.this);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SetMyTimerActivity.this);
                    alertDialog.setTitle("시간을 입력해 주세요\n예) 15, 65, 100")
                            .setView(edittext)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = edittext.getText().toString();
                                    str = str.replaceAll("[^0-9]", "");
                                    ((CustomTestHolder)viewHolder).time.setText(str);
                                }
                            });

                }
            });

            ((CustomTestHolder)viewHolder).time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText edittext = new EditText(SetMyTimerActivity.this);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SetMyTimerActivity.this);
                    alertDialog.setTitle("시간을 입력해 주세요\n예) 15, 65, 100")
                            .setView(edittext)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = edittext.getText().toString();
                                    str = str.replaceAll("[^0-9]", "");
                                    ((CustomTestHolder)viewHolder).time.setText(str);
                                }
                            }).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
