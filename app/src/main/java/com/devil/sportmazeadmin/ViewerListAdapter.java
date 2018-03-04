package com.devil.sportmazeadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewerListAdapter extends RecyclerView.Adapter<ViewerListAdapter.MyViewHolder> {
    private List<Viewer> viewerList;
    private Context mcontext;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, value;
        MyViewHolder(View view) {
            super(view);
            value = view.findViewById(R.id.value);
            name = view.findViewById(R.id.name);
        }
    }


    public ViewerListAdapter(Context context, List<Viewer> viewerList) {
        this.viewerList = viewerList;
        mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewer_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Viewer viewer = viewerList.get(position);
        holder.name.setText(viewer.getName());
        holder.value.setText(viewer.getValue());
    }

    @Override
    public int getItemCount() {
        return viewerList.size();
    }
}