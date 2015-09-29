package com.zakoopi.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.zakoopi.R;
import com.zakoopi.model.UserFollowDataObject;
 
public class UserFollowRecyclerViewAdapter extends RecyclerView
        .Adapter<UserFollowRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<UserFollowDataObject> mDataset;
    private static MyClickListener myClickListener;
 
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView store_name;
        TextView store_location;
 
        public DataObjectHolder(View itemView) {
            super(itemView);
            store_name = (TextView) itemView.findViewById(R.id.txt_store_name);
            store_location = (TextView) itemView.findViewById(R.id.txt_store_location);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }
 
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
 
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
 
    public UserFollowRecyclerViewAdapter(ArrayList<UserFollowDataObject> myDataset) {
        mDataset = myDataset;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_follow_recycler_item, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.store_name.setText(mDataset.get(position).gettxtStoreName());
        holder.store_location.setText(mDataset.get(position).gettxtStoreLocation());
    }
 
    public void addItem(UserFollowDataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }
 
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
 
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
 
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}