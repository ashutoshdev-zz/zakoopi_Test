package com.zakoopi.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.navdrawer.SimpleSideDrawer;
import com.zakoopi.R;
import com.zakoopi.fragments.StoreFragment;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class UserStoreAndExperiences extends FragmentActivity {

	RelativeLayout rel_store, rel_exp;
	TextView txt_store, txt_exp;
	View view_store, view_exp;
	LinearLayout lin_store, lin_exp;
	ImageView img_menu;
	RelativeLayout rel_back, rel_edt_profile;
	TextView txt_topbar;
	SimpleSideDrawer slide_me;
	public static String search_slug, product_slug, area_slug, market_slug,
			trend_slug;

	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_result_store_main);

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("User Store & Experiences View");
		t.send(new HitBuilders.AppViewBuilder().build());

		/**
		 * Typeface
		 */
		typeface_bold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_regular = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		typeface_semibold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		/**
		 * Get Data using Intent
		 */
		Intent i = getIntent();

		search_slug = i.getStringExtra("search");
		product_slug = i.getStringExtra("product_slug");
		area_slug = i.getStringExtra("area_slug");
		market_slug = i.getStringExtra("market_slug");
		trend_slug = i.getStringExtra("trend_slug");
	
		Fragment fr = new StoreFragment();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.lin_search_store, fr);
		ft.commit();

		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_topbar = (TextView) findViewById(R.id.txt);
		txt_topbar.setTypeface(typeface_semibold);
		txt_topbar.setText("Search Results");
		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(UserStoreAndExperiences.this).reportActivityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(UserStoreAndExperiences.this).reportActivityStop(this);
	}

}
