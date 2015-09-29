package com.zakoopi.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.zakoopi.R;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class HomeFeedFragment extends Fragment {
	LinearLayout lin_pop, lin_recent;
	RelativeLayout rel_pop, rel_recent;
	private SharedPreferences pref_location;
	private String city_name;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_feed_main_frag, null);

		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home Feed");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		lin_pop = (LinearLayout) view.findViewById(R.id.lin_popular);
		lin_recent = (LinearLayout) view.findViewById(R.id.lin_recent);
		lin_pop.setVisibility(View.VISIBLE);
		lin_recent.setVisibility(View.GONE);
		
		try {
			
		
		pref_location = getActivity().getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "delhi");

		
		if (city_name.equals("Mumbai")) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.beginTransaction();
			Fragment fragTwo = new PopularFeedFragment();
			Bundle arguments = new Bundle();
			arguments.putBoolean("shouldYouCreateAChildFragment", false);
			fragTwo.setArguments(arguments);
			ft.add(R.id.lin_popular, fragTwo);

			ft.commit();
		} else {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			fm.beginTransaction();
			Fragment fragTwo = new PopularFeedFragment();
			Bundle arguments = new Bundle();
			arguments.putBoolean("shouldYouCreateAChildFragment", false);
			fragTwo.setArguments(arguments);
			ft.add(R.id.lin_popular, fragTwo);

			ft.commit();
		}
		/**
		 * Add popularFeedFragment
		 */
		} catch (Exception e) {
			// TODO: handle exception
		}

		return view;
	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
	}


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}
}
