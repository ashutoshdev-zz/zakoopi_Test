package com.zakoopi.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zakoopi.R;
import com.zakoopi.adapter.UserNotificationRecyclerViewAdapter;
import com.zakoopi.helper.DividerItemDecoration;
import com.zakoopi.model.UserNotificationDataObject;

/**
 * Created by neokree on 16/12/14.
 */
public class UserNotificationPage extends FragmentActivity {
	
	private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.user_notification_screen);
    	 mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
	        mRecyclerView.setHasFixedSize(true);
	        mLayoutManager = new LinearLayoutManager(this);
	        mRecyclerView.setLayoutManager(mLayoutManager);
	        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
	        mAdapter = new UserNotificationRecyclerViewAdapter(getDataSet());
	        mRecyclerView.setAdapter(mAdapter);
    }
    
    private ArrayList<UserNotificationDataObject> getDataSet() {
        ArrayList results = new ArrayList<UserNotificationDataObject>();
        for (int index = 0; index < 20; index++) {
        	UserNotificationDataObject obj = new UserNotificationDataObject("User Name commented on your lookbook");
            results.add(index, obj);
        }
        return results;
    }
}
