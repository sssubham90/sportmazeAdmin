package com.devil.sportmazeadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VideoViewersActivity extends AppCompatActivity {

    private ArrayList<Viewer> viewerList;
    private ViewerListAdapter mAdapter;
    private String key;
    private TextView textView;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewers);
        viewerList = new ArrayList<>();
        key = getIntent().getStringExtra("key");
        textView = findViewById(R.id.video);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ViewerListAdapter(this, viewerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        myRef.child("Video").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
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
                myRef.child("Video").child(key).removeValue();
                mStorageReference.child("Images").child(key).child("thumbnail.png").delete();
                mStorageReference.child("Videos").child(key).child("video.mp4").delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                    }
                });
            }
        });

        findViewById(R.id.feature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("Featured Videos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()==5){
                            myRef.child("Featured Videos").child("4").setValue(dataSnapshot.child("3").getValue());
                            myRef.child("Featured Videos").child("3").setValue(dataSnapshot.child("2").getValue());
                            myRef.child("Featured Videos").child("2").setValue(dataSnapshot.child("1").getValue());
                            myRef.child("Featured Videos").child("1").setValue(dataSnapshot.child("0").getValue());
                            myRef.child("Featured Videos").child("0").setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(VideoViewersActivity.this,"Video Featured", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                            myRef.child("Featured Videos").child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(VideoViewersActivity.this,"Video Featured", Toast.LENGTH_LONG).show();
                                }
                            });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
