package com.timer_v3.user.timer_practice;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ShowTestListActivity extends AppCompatActivity {

    private RecyclerView testRec;
    private AdView adview;
    private TextView title;
    String document_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> each_test_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test_list);
        document_id = getIntent().getStringExtra("document_id");
        testRec = findViewById(R.id.showtestlist_recyclerview);
        adview = findViewById(R.id.showtestlist_adview);
        title = findViewById(R.id.showtestlist_title);

        AdRequest request = new AdRequest.Builder().build();
        adview.loadAd(request);

        title.setText(document_id);

        db.collection("tests").document(document_id).collection("each_test").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot snapshot : task.getResult()) {
                                if(snapshot.exists()) {
                                    each_test_data.add(snapshot.getId());
                                }
                            }

                            if(!each_test_data.isEmpty()) {
                                testRec.setLayoutManager(new LinearLayoutManager(ShowTestListActivity.this));
                                testRec.setAdapter(new ShowTestAdapter(each_test_data));
                            }
                        }
                    }
                });
    }

    private class ShowTestAdapter extends RecyclerView.Adapter {

        ArrayList<String> inner_data;
        public ShowTestAdapter(ArrayList<String> each_test_data) {
            this.inner_data = each_test_data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            ((CustomViewHolder)viewHolder).textView.setText(inner_data.get(i));
            ((CustomViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowTestListActivity.this, SetTimerActivity.class);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(ShowTestListActivity.this, R.anim.fromright, R.anim.toleft);
                    intent.putExtra("type_of_call", 0);
                    intent.putExtra("document_id", document_id);
                    intent.putExtra("each_test_id", inner_data.get(i));
                    startActivity(intent, options.toBundle());
            }
            });
        }

        @Override
        public int getItemCount() {
            return inner_data.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CustomViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_category_name);
        }
    }
}
