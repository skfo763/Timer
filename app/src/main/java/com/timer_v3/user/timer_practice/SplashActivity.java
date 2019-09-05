package com.timer_v3.user.timer_practice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MobileAds.initialize(this, "ca-app-pub-2826122867888366~9567916066");

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();

        if(ninfo == null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
            alertDialog.setTitle("네트워크 연결 안내")
                    .setMessage("네트워크 연결이 감지되지 않습니다. 직접 설정한 시간표만 이용하실 수 있습니다. 계속 이용하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    intent.putExtra("networkState", false);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }
                    }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("networkState", true);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }
}
