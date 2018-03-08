package com.devil.sportmazeadmin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WatchHistoryAdapter extends RecyclerView.Adapter<WatchHistoryAdapter.MyViewHolder> {
    private List<Date> historyList;
    private Context mcontext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView historyData;
        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.name);
            historyData = view.findViewById(R.id.data);
        }
    }


    public WatchHistoryAdapter(Context context, List<Date> historyList) {
        this.historyList = historyList;
        mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Date history = historyList.get(position);
        holder.date.setText(history.getDate());
        FirebaseDatabase.getInstance().getReference("Users/"+history.getName()+"/"+history.getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder str= new StringBuilder("\n");
                for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                    str.append(childSnapshot.getKey()).append("\nDuration:").append(childSnapshot.getValue()).append("\n\n");
                }
                holder.historyData.setText(str.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}