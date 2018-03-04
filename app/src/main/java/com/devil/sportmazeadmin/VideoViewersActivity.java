package com.devil.sportmazeadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoViewersActivity extends AppCompatActivity {

    private ArrayList<Viewer> viewerList;
    private ViewerListAdapter mAdapter;
    private String value;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewers);
        viewerList = new ArrayList<>();
        value = getIntent().getStringExtra("value");
        textView = findViewById(R.id.video);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ViewerListAdapter(this, viewerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Video/"+value);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.child("Name").getValue().toString());
                for(DataSnapshot childSnapshot : dataSnapshot.child("Viewers").getChildren()){
                    viewerList.add(new Viewer(childSnapshot.getValue().toString(),childSnapshot.getKey()));
                    mAdapter.notifyItemInserted(viewerList.size());
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("sm", "Failed to read value.", error.toException());
            }
        });

    }
}
