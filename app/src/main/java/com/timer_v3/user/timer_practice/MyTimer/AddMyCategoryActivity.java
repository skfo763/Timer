package com.timer_v3.user.timer_practice.MyTimer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.timer_v3.user.timer_practice.CustomTestHolder;
import com.timer_v3.user.timer_practice.MainActivity;
import com.timer_v3.user.timer_practice.R;
import com.timer_v3.user.timer_practice.SetTimerActivity;
import com.timer_v3.user.timer_practice.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMyCategoryActivity extends AppCompatActivity {

    Calendar cal = Calendar.getInstance();
    private EditText titleText, operatorText,dateText, testTitle;
    private TextView testMinute, testtime;
    private ImageButton dateButton, timeUp, timeDown;
    private Button confirm, revise, delete;
    private RadioGroup alarmState;
    private RecyclerView recyclerView;
    private LinearLayout addSubject, addTemp, wait, layout_rev;
    private SQLiteHelper helper;
    private ArrayList<Pair<String, Integer>> testDatas = new ArrayList<>();
    private List<TestData> td = new ArrayList<>();
    private CategoryData data = new CategoryData();
    private InputMethodManager inputMethodManager;

    private int CID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_category);
        helper = SQLiteHelper.getInstance(this);
        CID = getIntent().getIntExtra("CID", -1);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분");
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        titleText = findViewById(R.id.add_cat_titletext);
        operatorText = findViewById(R.id.add_cat_operatortext);
        dateText = findViewById(R.id.add_cat_dateEditText);
        dateButton = findViewById(R.id.add_cat_dateButton);
        confirm = findViewById(R.id.add_cat_confirm);
        alarmState = findViewById(R.id.add_cat_radiogroup);
        recyclerView = findViewById(R.id.add_cat_recyclerview);
        addSubject = findViewById(R.id.add_cat_addTest);

        layout_rev = findViewById(R.id.addcat_layout_rev);
        revise = findViewById(R.id.add_cat_revise);
        delete = findViewById(R.id.add_cat_delete);

        wait = findViewById(R.id.add_cat_waiting);
        addTemp = findViewById(R.id.add_cat_temp);
        testTitle = findViewById(R.id.add_cat_testtitle);
        timeUp = findViewById(R.id.addcat_test_timeup);
        timeDown = findViewById(R.id.addcat_test_timedown);
        testMinute = findViewById(R.id.addcat_test_time);
        testtime = findViewById(R.id.addcat_test_time_minute);

        if(CID == -1) {
            layout_rev.setVisibility(View.GONE);
            confirm.setVisibility(View.VISIBLE);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addWithoutPrevCID(format2);
                }
            });

        } else {
            layout_rev.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.GONE);
            data = helper.searchEachCategory(CID);

            titleText.setText(data.getTitle());
            operatorText.setText(data.getOperator());
            dateText.setText(data.getTTime());

            if(data.getalarmState() == 1) {
                alarmState.check(R.id.add_cat_radioyes);
            } else {
                alarmState.check(R.id.add_cat_radiono);
            }
            getTestWithCID(CID);

            revise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMyCategoryActivity.this);
                    alertDialog.setTitle("시험정보 수정 안내")
                            .setMessage("입력하신 정보로 타임 테이블이 수정됩니다. 수정 확정하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reviseWithCID();
                                    wait.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            wait.setVisibility(View.GONE);
                                            Intent intent = new Intent(AddMyCategoryActivity.this, MyTimerListActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }, 750);
                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMyCategoryActivity.this);
                    alertDialog.setTitle("시험정보 삭제 안내")
                            .setMessage("타임 테이블 및 시험정보가 삭제됩니다. 정말로 삭제 하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteWithCID();
                                    wait.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            wait.setVisibility(View.GONE);
                                            Intent intent = new Intent(AddMyCategoryActivity.this, MyTimerListActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }, 750);
                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            });
        }

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTestDate();
            }
        });

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTestWithoutCID();

            }
        });

        timeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ttime = Integer.parseInt(testMinute.getText().toString()) + 1;
                testMinute.setText(String.valueOf(ttime));
            }
        });

        timeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ttime = Integer.parseInt(testMinute.getText().toString()) - 1;
                testMinute.setText(String.valueOf(ttime));
            }
        });

        testMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTestTimeManually();
            }
        });

        testtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTestTimeManually();
            }
        });
    }

    private void deleteWithCID() {
        helper.deleteCategory(CID);
    }

    private void reviseWithCID() {
        helper.deleteAllTest(CID);

        for(Pair<String, Integer> temp : testDatas) {
            TestData t = new TestData();
            t.setTID(-1);
            t.setCID(CID);
            t.setTitle(temp.first);
            t.setTime(temp.second);

            helper.insertToEachTest(t);
        }
    }

    private void getTestWithCID(int CID) {
        td = helper.searchEachTest(CID);

        for(TestData temp : td) {
            testDatas.add(new Pair<>(temp.getTitle(), temp.getTime()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(AddMyCategoryActivity.this));
        recyclerView.setAdapter(new MyAddTestAdapter());
    }

    private void addTestWithoutCID() {
        if(addTemp.getVisibility() == View.GONE) {
            testTitle.setText("");
            testMinute.setText("0");
            addTemp.setVisibility(View.VISIBLE);
        } else {
            if(testTitle.getText().toString().isEmpty()) {
                Toast.makeText(AddMyCategoryActivity.this, "과목 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                String temp_title = testTitle.getText().toString();
                int temp_time = Integer.parseInt(testMinute.getText().toString());
                testDatas.add(new Pair<>(temp_title, temp_time));

                inputMethodManager.hideSoftInputFromWindow(testTitle.getWindowToken(), 0);
                addTemp.setVisibility(View.GONE);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddMyCategoryActivity.this));
                recyclerView.setAdapter(new MyAddTestAdapter());
            }
        }
    }

    private void addWithoutPrevCID(SimpleDateFormat format2) {
        if(titleText.getText().toString().isEmpty() || operatorText.getText().toString().isEmpty()
                || dateText.getText().toString().isEmpty()) {
            Toast.makeText(AddMyCategoryActivity.this, "정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
        } else {
            int astate;
            if(alarmState.getCheckedRadioButtonId() == R.id.add_cat_radioyes) {
                astate = 1;
            } else {
                astate = 0;
            }
            String now = format2.format(System.currentTimeMillis());
            final CategoryData cd = new CategoryData(0, titleText.getText().toString(),
                    now, dateText.getText().toString(), operatorText.getText().toString(), astate);
            helper.insertToCategory(cd);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMyCategoryActivity.this);
            alertDialog.setTitle("시험 추가 안내")
                    .setMessage("입력하신 정보로 타임 테이블이 추가됩니다. 추가하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int cid = helper.searchWithModel(cd);
                            if(cid != -1) {
                                for (Pair<String, Integer> temp : testDatas) {
                                    TestData td = new TestData(-1, cid, temp.first, temp.second);
                                    helper.insertToEachTest(td);
                                }
                            }

                            wait.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    wait.setVisibility(View.GONE);
                                    Intent intent = new Intent(AddMyCategoryActivity.this, MyTimerListActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }, 750);
                        }
                    }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    private void setTestDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddMyCategoryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                @SuppressLint("DefaultLocale") String msg = String.format("%d년 %d월 %d일", i, i1+1, i2);
                dateText.setText(msg);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        datePickerDialog.show();
    }

    private void setTestTimeManually() {
        final EditText edittext = new EditText(AddMyCategoryActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMyCategoryActivity.this);
        alertDialog.setTitle("시간을 입력해 주세요.")
                .setMessage("예) 15, 65, 100")
                .setView(edittext)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = edittext.getText().toString();
                        str = str.replaceAll("[^0-9]", "");
                        testMinute.setText(str);
                        inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                    }
                }).show();
    }

    private class MyAddTestAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_test_category, viewGroup, false);
            return new CustomTestHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
            ((CustomTestHolder)viewHolder).checkBox.setVisibility(View.GONE);
            ((CustomTestHolder)viewHolder).title.setPadding(10, 0, 0, 0);
            ((CustomTestHolder)viewHolder).title.setText(testDatas.get(i).first);
            ((CustomTestHolder)viewHolder).time.setText(String.valueOf(testDatas.get(i).second));

            ((CustomTestHolder)viewHolder).timeup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<String, Integer> p = Pair.create(
                            ((CustomTestHolder)viewHolder).title.getText().toString(),
                            Integer.parseInt(((CustomTestHolder)viewHolder).time.getText().toString()) + 1);

                    testDatas.set(i, p);
                    ((CustomTestHolder)viewHolder).time.setText(String.valueOf(testDatas.get(i).second));
                }
            });

            ((CustomTestHolder)viewHolder).timedown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<String, Integer> p = Pair.create(
                            ((CustomTestHolder)viewHolder).title.getText().toString(),
                            Integer.parseInt(((CustomTestHolder)viewHolder).time.getText().toString()) - 1);

                    testDatas.set(i, p);
                    ((CustomTestHolder)viewHolder).time.setText(String.valueOf(testDatas.get(i).second));
                }
            });

            ((CustomTestHolder)viewHolder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText edittext = new EditText(AddMyCategoryActivity.this);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMyCategoryActivity.this);
                    alertDialog.setTitle("수정할 과목 이름을 입력해주세요")
                            .setView(edittext)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Pair<String, Integer> p = Pair.create(
                                            edittext.getText().toString(),
                                            Integer.parseInt(((CustomTestHolder)viewHolder).time.getText().toString()));

                                    testDatas.set(i, p);
                                    ((CustomTestHolder)viewHolder).title.setText(testDatas.get(i).first);
                                    inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                                }
                            }).show();
                }
            });

            ((CustomTestHolder)viewHolder).time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText edittext = new EditText(AddMyCategoryActivity.this);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMyCategoryActivity.this);
                    alertDialog.setTitle("시간을 입력해 주세요\n예) 15, 65, 100")
                            .setView(edittext)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(edittext.getText().toString().isEmpty()) {
                                        dialog.dismiss();
                                    } else {
                                        Pair<String, Integer> p = Pair.create(
                                                ((CustomTestHolder)viewHolder).title.getText().toString(),
                                                Integer.parseInt(edittext.getText().toString()));

                                        testDatas.set(i, p);
                                        ((CustomTestHolder)viewHolder).time.setText(testDatas.get(i).second);
                                        inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
                                    }
                                }
                            }).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return testDatas.size();
        }
    }
}
