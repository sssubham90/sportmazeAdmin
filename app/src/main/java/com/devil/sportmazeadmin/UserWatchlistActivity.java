package com.devil.sportmazeadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserWatchlistActivity extends AppCompatActivity {

    ArrayList<Date> dates;
    private DatabaseReference mRef;
    private WatchHistoryAdapter mAdapter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_watchlist);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        name = getIntent().getStringExtra("name");
        ((TextView)findViewById(R.id.name)).setText(String.format("%s%s", ((TextView) findViewById(R.id.name)).getText(), name));
        dates = new ArrayList<>();
        mAdapter = new WatchHistoryAdapter(this,dates);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRef.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("duration")!=null)
                ((TextView)findViewById(R.id.duration)).setText(String.format("%s%s", ((TextView) findViewById(R.id.duration)).getText(),mFormat(dataSnapshot.child("Total Duration").getValue().toString())));
                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    if(childSnapshot.getKey().equals("Total Duration")) continue;
                    dates.add(new Date(name,childSnapshot.getKey()));
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String mFormat(String s) {
    String string;
    long durMin = Long.parseLong(s);
    string = (durMin/60)+"hours "+(durMin-((durMin/60)*60))+"minutes";
    return string;
    }
}
