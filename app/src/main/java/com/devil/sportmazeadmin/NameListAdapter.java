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

public class NameListAdapter extends RecyclerView.Adapter<NameListAdapter.MyViewHolder> implements Filterable{
    private List<String> mFilteredList,mArrayList;
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
                    ArrayList<String> filteredList = new ArrayList<>();
                    for (String string : mArrayList) {
                        if (string.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(string);
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
                mFilteredList = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }
    }


    public NameListAdapter(Context context, List<String> nameList) {
        this.mFilteredList = nameList;
        this.mArrayList = nameList;
        mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String string = mFilteredList.get(position);
        holder.name.setText(string);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mcontext.startActivity(new Intent(mcontext,UserWatchlistActivity.class).putExtra("name",string));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
}