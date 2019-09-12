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
import com.timer_v3.user.timer_practice.MainActivity;
import com.timer_v3.user.timer_practice.R;
import com.timer_v3.user.timer_practice.SetTimerActivity;

import java.util.ArrayList;
import java.util.List;

public class MyTimerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdView adView;
    public SQLiteHelper helper;
    private List<CategoryData> cat_data = new ArrayList<>();
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
        recyclerView.setAdapter(new MyTimerListAdapter());
    }

    private class MyTimerListAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
            return new CustomCatHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            final ActivityOptions options = ActivityOptions.makeCustomAnimation(MyTimerListActivity.this, R.anim.fromright, R.anim.toleft);

            ((CustomCatHolder)viewHolder).textview.setText(cat_data.get(i).getTitle());
            ((CustomCatHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MyTimerListActivity.this, SetMyTimerActivity.class);
                            intent.putExtra("CID", cat_data.get(i).getCID());
                            startActivity(intent, options.toBundle());
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return cat_data.size();
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
