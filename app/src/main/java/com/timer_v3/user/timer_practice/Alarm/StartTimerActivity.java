package com.timer_v3.user.timer_practice.Alarm;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.timer_v3.user.timer_practice.ICounterInterface;
import com.timer_v3.user.timer_practice.R;

import java.util.ArrayList;

public class StartTimerActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    String testname;
    ArrayList<String> category_extra;
    ArrayList<Integer> time_extra;
    ArrayList<String> category_data = new ArrayList<>();
    ArrayList<Integer> time_data = new ArrayList<>();
    ArrayList<Integer> result_data = new ArrayList<>();
    public ViewPager timerPager;
    public int size;
    public long startTime = System.currentTimeMillis();

    public boolean isRunning = false;
    public boolean check_alerm = false;
    public boolean check_sound = false;
    public boolean check_vibrate = false;
    public boolean check_displaylock = false;

    private ImageView alarm, sound, vibrate, displaylock;

    TimerFrgament frgament = new TimerFrgament();
    public ICounterInterface binder;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = ICounterInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_timer);

        mContext = this;
        testname = getIntent().getStringExtra("test_name");
        category_extra = getIntent().getStringArrayListExtra("category_data");
        time_extra = getIntent().getIntegerArrayListExtra("time_data");
        timerPager = findViewById(R.id.starttimer_viewpager);
        TextView title = findViewById(R.id.starttimer_title);
        AdView adview = findViewById(R.id.starttimer_adview);
        alarm = findViewById(R.id.start_timer_alarm);
        sound = findViewById(R.id.start_timer_sound);
        vibrate = findViewById(R.id.start_timer_vibrate);
        displaylock = findViewById(R.id.start_timer_displayloc);

        AdRequest request = new AdRequest.Builder().build();
        adview.loadAd(request);
        title.setText(testname);

        for(int i=0; i<time_extra.size(); i++) {
            int temp_time = time_extra.get(i);
            String temp_category = category_extra.get(i);
            if(temp_time > 0) {
                time_data.add(temp_time);
                category_data.add(temp_category);
                result_data.add(0);
            }
        }
        size = category_data.size();
        timerPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), category_data, time_data));

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning) {
                    Toast.makeText(StartTimerActivity.this, "타이머가 작동중입니다. 일시정지 후 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(check_alerm) {
                        alarm.setBackgroundResource(R.drawable.ic_alarm_off_black_24dp);
                        Toast.makeText(StartTimerActivity.this, "알림을 받지 않습니다", Toast.LENGTH_SHORT).show();
                        check_alerm = false;
                    } else {
                        alarm.setBackgroundResource(R.drawable.ic_alarm_on_black_24dp);
                        Toast.makeText(StartTimerActivity.this, "타이머가 종료되면 알림을 받습니다", Toast.LENGTH_SHORT).show();
                        check_alerm = true;
                    }
                }
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning) {
                    Toast.makeText(StartTimerActivity.this, "타이머가 작동중입니다. 일시정지 후 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(check_sound) {
                        sound.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
                        Toast.makeText(StartTimerActivity.this, "소리 알림을 받지 않습니다", Toast.LENGTH_SHORT).show();
                        check_sound = false;
                    } else {
                        sound.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
                        Toast.makeText(StartTimerActivity.this, "소리 알림을 받습니다", Toast.LENGTH_SHORT).show();
                        check_sound = true;
                    }
                }
            }
        });

        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning) {
                    Toast.makeText(StartTimerActivity.this, "타이머가 작동중입니다. 일시정지 후 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if(check_vibrate) {
                        vibrate.setBackgroundResource(R.drawable.ic_phonelink_erase_black_24dp);
                        Toast.makeText(StartTimerActivity.this, "진동 알림을 받지 않습니다.", Toast.LENGTH_SHORT).show();
                        check_vibrate = false;
                    } else {
                        vibrate.setBackgroundResource(R.drawable.ic_vibration_black_24dp);
                        Toast.makeText(StartTimerActivity.this, "진동 알림을 받습니다.", Toast.LENGTH_SHORT).show();
                        check_vibrate= true;
                    }
                }

            }
        });

        displaylock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_displaylock) {
                    displaylock.setBackgroundResource(R.drawable.ic_phone_android_black_24dp);
                    Toast.makeText(StartTimerActivity.this, "화면잠금을 사용하지 않습니다.", Toast.LENGTH_SHORT).show();
                    check_displaylock = false;
                } else {
                    displaylock.setBackgroundResource(R.drawable.ic_screen_lock_portrait_black_24dp);
                    Toast.makeText(StartTimerActivity.this, "화면잠금을 사용합니다.", Toast.LENGTH_SHORT).show();
                    check_displaylock = true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!check_displaylock) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StartTimerActivity.this);
            alertDialog.setTitle("타이머 종료 안내")
                    .setMessage("지금 뒤로가기를 누르시면 진행중인 타이머가 종료됩니다. 뒤로 가시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isRunning) {
                                isRunning = false;
                                unbindService(connection);
                            }
                            frgament.onDestroy();
                            finish();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else {
            Toast.makeText(StartTimerActivity.this, "화면잠금 상태입니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
