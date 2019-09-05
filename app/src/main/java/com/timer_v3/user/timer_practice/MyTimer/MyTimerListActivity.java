package com.timer_v3.user.timer_practice.MyTimer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.timer_v3.user.timer_practice.R;

import java.util.ArrayList;

public class MyTimerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdView adView;
    public SQLiteHelper helper;
    private ArrayList<CategoryData> cat_data;
    private ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timer_list);

        helper = SQLiteHelper.getInstance(this);
        recyclerView = findViewById(R.id.mytimer_recyclerview);
        adView = findViewById(R.id.mytimer_adview);
        add = findViewById(R.id.mytimer_add);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTimerListActivity.this, AddMyCategoryActivity.class);
                startActivity(intent);
            }
        });

        cat_data = helper.searchCategory();
        recyclerView.setLayoutManager(new LinearLayoutManager(MyTimerListActivity.this));
        recyclerView.setAdapter(new MyTimerListAdapter(cat_data));
    }

    private class MyTimerListAdapter extends RecyclerView.Adapter {
        private ArrayList<CategoryData> data;

        public MyTimerListAdapter(ArrayList<CategoryData> cat_data) {
            data = cat_data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
            return new CustomCatHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ((CustomCatHolder)viewHolder).textview.setText(data.get(i).getTitle());
            ((CustomCatHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(MyTimerListActivity.this, R.anim.fromright, R.anim.toleft);
                            intent = new Intent(MyTimerListActivity.this, ShowMyTestListActivity.class);
                            intent.putExtra("CID", data.get(i).getCID());
                            intent.putExtra("category_name", data.get(i).getTitle());
                            intent.putExtra("upload_time", data.get(i).getUTime());
                            startActivity(intent, options.toBundle());
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class CustomCatHolder extends RecyclerView.ViewHolder {
            TextView textview;
            LinearLayout line;

            public CustomCatHolder(View view) {
                super(view);
                textview = view.findViewById(R.id.item_category_name);
                line = view.findViewById(R.id.item_category_line);
            }
        }
    }
}
