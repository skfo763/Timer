package com.timer_v3.user.timer_practice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.timer_v3.user.timer_practice.Alarm.StartTimerActivity;

import java.util.ArrayList;

public class SetTimerActivity extends AppCompatActivity {

    String document_id;
    String each_test_id;
    private AdView adView;
    private TextView title, alltime;
    private Button start;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> category_data = new ArrayList<>();
    ArrayList<Integer> time_data = new ArrayList<>();
    public int allint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);

        document_id = getIntent().getStringExtra("document_id");
        each_test_id = getIntent().getStringExtra("each_test_id");

        adView = findViewById(R.id.settimer_adview);
        title = findViewById(R.id.settimer_title);
        alltime = findViewById(R.id.settimer_alltime);
        start = findViewById(R.id.settimer_start);
        recyclerView = findViewById(R.id.settimer_recyclerview);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        title.setText(each_test_id);

        db.collection("tests").document(document_id).collection("each_test")
                .document(each_test_id).collection("category")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot snapshot : task.getResult()) {
                        int temp_time = Integer.parseInt(snapshot.get("time").toString());
                        category_data.add(snapshot.getId());
                        time_data.add(temp_time);
                        allint += temp_time;
                    }

                    if(!category_data.isEmpty()) {
                        alltime.setText(String.valueOf(allint));
                        recyclerView.setLayoutManager(new LinearLayoutManager(SetTimerActivity.this));
                        recyclerView.setAdapter(new MySetTimerAdapter(category_data, time_data));
                    }
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTimerActivity.this, StartTimerActivity.class);
                intent.putExtra("test_name", each_test_id);
                intent.putIntegerArrayListExtra("time_data", time_data);
                intent.putStringArrayListExtra("category_data", category_data);
                startActivity(intent);
                finish();
            }
        });
    }

    private class MySetTimerAdapter extends RecyclerView.Adapter {
        ArrayList<String> data;
        ArrayList<Integer> time_data;
        public MySetTimerAdapter(ArrayList<String> category_data, ArrayList<Integer> time_data) {
            this.data = category_data;
            this.time_data = time_data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_test_category, viewGroup, false);
            return new CustomTestHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
            ((CustomTestHolder)viewHolder).time.setText(String.valueOf(time_data.get(i)));
            ((CustomTestHolder)viewHolder).title.setText(data.get(i));

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
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class CustomTestHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView title, time, time_minute;
        ImageButton timeup, timedown;
        CustomTestHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.item_test_checkbox);
            title = view.findViewById(R.id.item_test_title);
            time = view.findViewById(R.id.item_test_time);
            timeup = view.findViewById(R.id.item_test_timeup);
            timedown = view.findViewById(R.id.item_test_timedown);
            time_minute = view.findViewById(R.id.item_test_time_minute);
        }
    }
}
