package com.zakoopi.fragments;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.zakoopi.R;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class StoreFragment extends Fragment{
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location;
	LinearLayout lin_all_store,lin_all_exp;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.store_fragment, null);
		
		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Store Search");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		lin_all_store = (LinearLayout) view.findViewById(R.id.lin_all_store);
		lin_all_exp = (LinearLayout) view.findViewById(R.id.lin_all_exp);
		lin_all_exp.setVisibility(View.GONE);
		
		/**
		 * Login User Preferences
		 */
		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		
		/**
		 * Typeface
		 */
		typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
	   
		
		StoreListFragment .page = 1;
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		fm.beginTransaction();
		Fragment fragTwo = new StoreListFragment();
		Bundle arguments = new Bundle();
		arguments.putBoolean("shouldYouCreateAChildFragment", false);
		fragTwo.setArguments(arguments);
		ft.add(R.id.lin_all_store, fragTwo);

		ft.commit();
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
