package com.timer_v3.user.timer_practice.Alarm;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timer_v3.user.timer_practice.R;
import com.timer_v3.user.timer_practice.ShowResultActivity;

import java.util.Objects;

public class TimerFrgament extends android.support.v4.app.Fragment {

    public static TimerFrgament newInstance(String title, Integer time, int i) {
        Bundle args = new Bundle();
        args.putInt("index", i);
        args.putString("title", title);
        args.putInt("time", time);

        TimerFrgament fragment = new TimerFrgament();
        fragment.setArguments(args);
        return fragment;
    }

    TextView minute, title, second;
    ImageView play, stop, pause, fast;
    Context context;
    long now, target;
    public int index, m_val, s_val;
    boolean onView = true;
    Intent pintent;
    Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_timer, container, false);

        this.context = getContext();
        assert context != null;

        title = view.findViewById(R.id.item_timer_title);
        minute = view.findViewById(R.id.item_timer_time);
        second = view.findViewById(R.id.item_timer_second);
        play = view.findViewById(R.id.item_timer_play);
        stop = view.findViewById(R.id.item_timer_stop);
        pause = view.findViewById(R.id.item_timer_pause);
        fast = view.findViewById(R.id.item_timer_fast);

        assert getArguments() != null;
        index = Integer.parseInt(Objects.requireNonNull(getArguments().get("index")).toString());
        title.setText(Objects.requireNonNull(getArguments().get("title")).toString());
        minute.setText(Objects.requireNonNull(getArguments().get("time")).toString());
        second.setText("00");

        play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                if(!((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning) {
                    m_val = Integer.parseInt(minute.getText().toString());
                    s_val = Integer.parseInt(second.getText().toString());
                    now = System.currentTimeMillis();
                    target = now + ((m_val * 60) + s_val) * 1000;

                    pintent = new Intent(getContext(), CounterService.class);
                    pintent.putExtra("now", now);
                    pintent.putExtra("target", target);
                    pintent.putExtra("title", title.getText().toString());
                    pintent.putExtra("testname", ((StartTimerActivity)getActivity()).testname);
                    pintent.putExtra("alarm", ((StartTimerActivity)getActivity()).check_alerm);
                    pintent.putExtra("sound", ((StartTimerActivity)getActivity()).check_sound);
                    pintent.putExtra("vibrate", ((StartTimerActivity)getActivity()).check_vibrate);

                    context.bindService(pintent, ((StartTimerActivity) Objects.requireNonNull(getActivity())).connection, Context.BIND_AUTO_CREATE);
                    ((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning = true;
                    Thread thread = new Thread(new SetUIThread());
                    thread.start();

                    play.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                } else {
                    Toast.makeText(getContext(), "알람이 이미 실행중입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stop.setBackgroundResource(R.drawable.ic_stop_red_24dp);
                    case MotionEvent.ACTION_UP:
                        stop.setBackgroundResource(R.drawable.ic_stop_black_24dp);
                    default:
                        stop.setBackgroundResource(R.drawable.ic_stop_black_24dp);
                }
                return false;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning) {
                    context.unbindService(((StartTimerActivity) Objects.requireNonNull(getActivity())).connection);
                    ((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning = false;
                    play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                    minute.setText(String.valueOf(Objects.requireNonNull(getArguments().get("time"))));
                    second.setText("00");

                } else {
                    play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                    minute.setText(String.valueOf(Objects.requireNonNull(getArguments().get("time"))));
                    second.setText("00");
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning) {
                    context.unbindService(((StartTimerActivity) Objects.requireNonNull(getActivity())).connection);
                    ((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning = false;
                    play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);

                    int min = Integer.parseInt(minute.getText().toString());
                    int sec = Integer.parseInt(second.getText().toString());
                    target = now + ((min * 60) + sec) * 1000;

                } else {
                    Toast.makeText(getContext(), "정지된 타이머는 멈출 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((StartTimerActivity)getActivity()).isRunning) {
                    AlertDialog.Builder ald = new AlertDialog.Builder(getContext());
                    ald.setTitle("타이머 종료 안내")
                            .setMessage("실행중인 타이머가 종료됩니다. 다음 시험으로 넘어가시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkTwice();
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    if(index<((StartTimerActivity)getActivity()).size-1) {
                        if(Integer.parseInt(second.getText().toString()) >= 0) {
                            ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(minute.getText().toString())*60 + Integer.parseInt(second.getText().toString()));
                        } else {
                            ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(second.getText().toString()));
                        }

                        ((StartTimerActivity) Objects.requireNonNull(getActivity())).timerPager.setCurrentItem(index+1, true);
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("시험 종료 안내")
                                .setMessage("설정하신 모든 시험이 종료되었습니다. 결과 확인 페이지로 넘어가시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                                        pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                                        ((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning = false;

                                        if(Integer.parseInt(second.getText().toString()) >= 0) {
                                            ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(minute.getText().toString())*60 + Integer.parseInt(second.getText().toString()));
                                        } else {
                                            ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(second.getText().toString()));
                                        }

                                        Intent intent = new Intent(getContext(), ShowResultActivity.class);
                                        intent.putExtra("testname", title.getText().toString());
                                        intent.putExtra("startTime", ((StartTimerActivity)getActivity()).startTime);
                                        intent.putStringArrayListExtra("category_data", ((StartTimerActivity)getActivity()).category_extra);
                                        intent.putIntegerArrayListExtra("time_data", ((StartTimerActivity)getActivity()).time_data);
                                        intent.putIntegerArrayListExtra("result_data", ((StartTimerActivity)getActivity()).result_data);
                                        ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fromright, R.anim.toleft);
                                        startActivity(intent, options.toBundle());
                                        getActivity().finish();
                                    }
                                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkTwice() {
        if(index < ((StartTimerActivity) Objects.requireNonNull(getActivity())).size-1) {
            if(Integer.parseInt(second.getText().toString()) >= 0) {
                ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(minute.getText().toString())*60 + Integer.parseInt(second.getText().toString()));
            } else {
                ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(second.getText().toString()));
            }

            getActivity().unbindService(((StartTimerActivity)getActivity()).connection);
            play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
            pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
            ((StartTimerActivity)getActivity()).isRunning = false;
            ((StartTimerActivity)getActivity()).timerPager.setCurrentItem(index+1, true);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("시험 종료 안내")
                    .setMessage("설정하신 모든 시험이 종료되었습니다. 결과 확인 페이지로 넘어가시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(Integer.parseInt(second.getText().toString()) >= 0) {
                                ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(minute.getText().toString())*60 + Integer.parseInt(second.getText().toString()));
                            } else {
                                ((StartTimerActivity)getActivity()).result_data.set(index, Integer.parseInt(second.getText().toString()));
                            }

                            getActivity().unbindService(((StartTimerActivity) Objects.requireNonNull(getActivity())).connection);
                            play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                            pause.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                            ((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning = false;

                            Intent intent = new Intent(getContext(), ShowResultActivity.class);
                            intent.putExtra("testname", title.getText().toString());
                            intent.putExtra("startTime", ((StartTimerActivity)getActivity()).startTime);
                            intent.putStringArrayListExtra("category_data", ((StartTimerActivity)getActivity()).category_data);
                            intent.putIntegerArrayListExtra("time_data", ((StartTimerActivity)getActivity()).time_data);
                            intent.putIntegerArrayListExtra("result_data", ((StartTimerActivity)getActivity()).result_data);
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.fromright, R.anim.toleft);
                            startActivity(intent, options.toBundle());
                            getActivity().finish();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void onDestroy() {
        onView = false;
        super.onDestroy();
    }

    private class SetUIThread implements Runnable {
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                while(onView && ((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning) {
                    if(((StartTimerActivity) getActivity()).binder != null) {
                        final int min, sec;
                        try {
                            min = (int) (((StartTimerActivity) getActivity()).binder.getCount() / 60);
                            sec = (int) (((StartTimerActivity) getActivity()).binder.getCount() % 60);

                            if (min == 0 && sec == 0) {
                                break;
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (min >= 0 && min <= 9) {
                                            minute.setText("0".concat(String.valueOf(min)));
                                        } else {
                                            minute.setText(String.valueOf(min));
                                        }

                                        if (sec >= 0 && sec <= 9) {
                                            second.setText("0".concat(String.valueOf(sec)));
                                        } else {
                                            second.setText(String.valueOf(sec));
                                        }
                                    }
                                });
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                while(((StartTimerActivity) Objects.requireNonNull(getActivity())).isRunning) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            second.setText(String.valueOf(Integer.parseInt(second.getText().toString())-1));
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
