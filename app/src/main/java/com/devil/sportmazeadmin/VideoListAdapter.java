package com.devil.sportmazeadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    private List<Video> videoList;
    private Context mcontext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, value;
        MyViewHolder(View view) {
            super(view);
            value = view.findViewById(R.id.value);
            name = view.findViewById(R.id.name);
        }
    }


    public VideoListAdapter(Context context, List<Video> videoList) {
        this.videoList = videoList;
        mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Video video = videoList.get(position);
        holder.name.setText(video.getName());
        holder.value.setText(video.getValue());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcontext.startActivity(new Intent(mcontext,VideoViewersActivity.class).putExtra("value",video.getValue()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}