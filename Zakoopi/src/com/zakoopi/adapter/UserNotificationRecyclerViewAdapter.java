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
import com.zakoopi.model.UserNotificationDataObject;
 
public class UserNotificationRecyclerViewAdapter extends RecyclerView
        .Adapter<UserNotificationRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<UserNotificationDataObject> mDataset;
    private static MyClickListener myClickListener;
 
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView txt_notification;
 
        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_notification = (TextView) itemView.findViewById(R.id.txt_comment);
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
 
    public UserNotificationRecyclerViewAdapter(ArrayList<UserNotificationDataObject> myDataset) {
        mDataset = myDataset;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_notification_recycler_item, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txt_notification.setText(mDataset.get(position).gettxtNotification());
    }
 
    public void addItem(UserNotificationDataObject dataObj, int index) {
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