package com.devil.sportmazeadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> implements Filterable{
    private List<Video> mFilteredList,mArrayList;
    private Context mcontext;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<Video> filteredList = new ArrayList<>();
                    for (Video video : mArrayList) {
                        if (video.getName().toLowerCase().contains(charString)) {
                            filteredList.add(video);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Video>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        String key;
        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }
    }


    public VideoListAdapter(Context context, List<Video> videoList) {
        this.mFilteredList = videoList;
        this.mArrayList = videoList;
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
        final Video video = mFilteredList.get(position);
        holder.name.setText(video.getName());
        holder.key = video.getKey();
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcontext.startActivity(new Intent(mcontext,VideoViewersActivity.class).putExtra("key",video.getKey()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
}