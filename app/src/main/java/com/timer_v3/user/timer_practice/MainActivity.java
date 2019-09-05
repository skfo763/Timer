package com.timer_v3.user.timer_practice;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.timer_v3.user.timer_practice.MyTimer.MyTimerListActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdView adview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> category_data = new ArrayList<>();
    private boolean networkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recyclerview);
        adview = findViewById(R.id.main_adview);
        networkState = getIntent().getBooleanExtra("networkState", true);

        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);

        if(networkState) {
            db.collection("tests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        category_data.add("나만의 시험 시간표");
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            if (snapshot.exists()) {
                                category_data.add(snapshot.getId());
                            }
                        }
                        if (!category_data.isEmpty()) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(new mainActivityAdapter(category_data));

                        } else {
                            Toast.makeText(MainActivity.this, "정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "시험별 알람 서비스를 이용하시려면, WIFI 혹은 데이터 네트워크를 연결 후 앱을 다시 실행해주세요.", Toast.LENGTH_LONG).show();
            category_data.add("나만의 시험 시간표");
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(new mainActivityAdapter(category_data));
        }
    }

    private class mainActivityAdapter extends RecyclerView.Adapter {

        ArrayList<String> c_data;
        mainActivityAdapter(ArrayList<String> category_data) {
            this.c_data = category_data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ((CustomViewHolder)viewHolder).textView.setText(c_data.get(i));
            if(c_data.get(i).equals("나만의 시험 시간표")) {
                ((CustomViewHolder)viewHolder).textView.setTypeface(((CustomViewHolder)viewHolder).textView.getTypeface(), Typeface.BOLD);
                ((CustomViewHolder)viewHolder).itemView.setBackgroundColor(0xffdddddd);
            }

            ((CustomViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(MainActivity.this, R.anim.fromright, R.anim.toleft);

                    if(!c_data.get(i).equals("나만의 시험 시간표")) {
                        Intent intent = new Intent(MainActivity.this, ShowTestListActivity.class);
                        intent.putExtra("document_id", c_data.get(i));
                        startActivity(intent, options.toBundle());
                    } else {
                        Intent intent = new Intent(MainActivity.this, My
                    }TimerListActivity.class);
                    startActivity(intent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return c_data.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout line;
        CustomViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_category_name);
            line = view.findViewById(R.id.item_category_line);
        }
    }
}
