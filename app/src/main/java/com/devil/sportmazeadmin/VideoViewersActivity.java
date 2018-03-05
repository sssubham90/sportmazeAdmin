package com.devil.sportmazeadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private String key;
    private TextView textView;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewers);
        viewerList = new ArrayList<>();
        key = getIntent().getStringExtra("value");
        textView = findViewById(R.id.video);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ViewerListAdapter(this, viewerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Video");
        myRef.child(key).addValueEventListener(new ValueEventListener() {
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
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(key).removeValue();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.getParent().child("Featured Videos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==10){
                            myRef.child("10").setValue(dataSnapshot.child("9").getValue());
                            myRef.child("9").setValue(dataSnapshot.child("8").getValue());
                            myRef.child("8").setValue(dataSnapshot.child("7").getValue());
                            myRef.child("7").setValue(dataSnapshot.child("6").getValue());
                            myRef.child("6").setValue(dataSnapshot.child("5").getValue());
                            myRef.child("5").setValue(dataSnapshot.child("4").getValue());
                            myRef.child("4").setValue(dataSnapshot.child("3").getValue());
                            myRef.child("3").setValue(dataSnapshot.child("2").getValue());
                            myRef.child("2").setValue(dataSnapshot.child("1").getValue());
                            myRef.child("1").setValue(key);
                        }
                        else
                            myRef.child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(key);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
