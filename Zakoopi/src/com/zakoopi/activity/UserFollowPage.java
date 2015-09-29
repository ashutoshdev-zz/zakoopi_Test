package com.zakoopi.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zakoopi.R;
import com.zakoopi.adapter.UserFollowRecyclerViewAdapter;
import com.zakoopi.helper.DividerItemDecoration;
import com.zakoopi.model.UserFollowDataObject;

/**
 * Created by neokree on 16/12/14.
 */
public class UserFollowPage extends FragmentActivity {

	RelativeLayout rel_popular, rel_recent;
	TextView txt_popular, txt_recent;
	View view_popular, view_recent;
	ViewSwitcher viewSwitcher;
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private static String LOG_TAG = "CardViewActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_following_screen);
		mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));
		mAdapter = new UserFollowRecyclerViewAdapter(getDataSet());
		mRecyclerView.setAdapter(mAdapter);
	}

	private ArrayList<UserFollowDataObject> getDataSet() {
		ArrayList results = new ArrayList<UserFollowDataObject>();
		for (int index = 0; index < 20; index++) {
			UserFollowDataObject obj = new UserFollowDataObject("Store Name",
					"Store Location");
			results.add(index, obj);
		}
		return results;
	}
}
