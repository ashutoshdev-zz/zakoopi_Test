package com.zakoopi.adapter;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zakoopi.R;
import com.zakoopi.model.HomeLookbookDataObject;
 
public class ProfileRecyclerViewAdapter extends RecyclerView
        .Adapter<ProfileRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<HomeLookbookDataObject> mDataset;
    private static MyClickListener myClickListener;
 
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView user_name;
 
        public DataObjectHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
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
 
    public ProfileRecyclerViewAdapter(ArrayList<HomeLookbookDataObject> myDataset) {
        mDataset = myDataset;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_profile_followed, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.user_name.setText(mDataset.get(position).gettxtUserName());
    }
 
    public void addItem(HomeLookbookDataObject dataObj, int index) {
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