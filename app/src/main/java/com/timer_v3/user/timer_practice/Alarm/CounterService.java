package com.timer_v3.user.timer_practice.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.timer_v3.user.timer_practice.ICounterInterface;
import com.timer_v3.user.timer_practice.R;

import java.util.ArrayList;

public class CounterService extends Service {

    private boolean isStop, check_alarm, check_sound, check_vibrate;
    private long count;
    long[] vibrates = {1000};

    ICounterInterface.Stub binder = new ICounterInterface.Stub() {
        @Override
        public long getCount() {
            return count;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        long now = intent.getLongExtra("now", 0);
        long target = intent.getLongExtra("target", 0);
        String testname = intent.getStringExtra("testname");
        String title = intent.getStringExtra("title");
        check_alarm = intent.getBooleanExtra("alarm", true);
        check_sound = intent.getBooleanExtra("sound", false);
        check_vibrate = intent.getBooleanExtra("vibrate", false);

        Thread counter = new Thread(new Counter(((target - now)/1000)-1, testname, title));
        counter.start();

        return binder;
    }

    private class Counter implements Runnable {
        private long set_time;
        private Handler handler = new Handler();
        private String inner_title, testname;

        Counter(long set_time, String testname, String title) {
            this.set_time = set_time;
            this.inner_title = title;
            this.testname = testname;
        }

        @Override
        public void run() {
            for(count=set_time; count>=0; count--) {
                if(isStop) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(check_alarm && !isStop) {
                        makeNotification(testname, inner_title);
                    }
                    onDestroy();
                }
            });
        }
    }

    private void makeNotification(String testname, String inner_title) {
        String channelId = "default_notification_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_volume_up_black_24dp)
                        .setContentTitle("시험 종료 알림")
                        .setContentText(testname.concat(" : ").concat(inner_title).concat("(이)가 종료되었습니다!"))
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

        if(check_sound) {
            notificationBuilder.setSound(defaultSoundUri);
        }

        if(check_vibrate) {
            notificationBuilder.setVibrate(vibrates);
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        assert manager != null;
        manager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();
    }
}
