package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mycam.LookBookTabsActivity;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.floatlib.FloatingActionButton;
import com.zakoopi.fragments.HomeDiscoverFragment;
import com.zakoopi.fragments.HomeFeedFragment;
import com.zakoopi.fragments.HomeProfileFrag;
import com.zakoopi.fragments.HomeSearchTopMarket;
import com.zakoopi.fragments.HomeSearchTopTrends;
import com.zakoopi.fragments.PopularFeedFragment;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.POJO;
import com.zakoopi.helper.Variables;
import com.zakoopi.search.AllArea;
import com.zakoopi.search.Area;
import com.zakoopi.search.SearchUpdate;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.search.updateSearch;
import com.zakoopi.tab_layout.MaterialTab;
import com.zakoopi.tab_layout.MaterialTabHostIcon;
import com.zakoopi.tab_layout.MaterialTabListener;
import com.zakoopi.user.model.ClientLocationModel;
import com.zakoopi.user.model.MainClient;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements
		MaterialTabListener, ConnectionCallbacks, OnConnectionFailedListener {
	public static ViewPager pager;
	private ViewPagerAdapter pagerAdapter;
	MaterialTabHostIcon tabHost;
	private Resources res;
	ImageView img_menu;
	SimpleSideDrawer slide_me;
	FloatingActionButton float_btn;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password,pro_user_age,pro_user_gender;
	CircleImageView pro_user_pic;
	private SharedPreferences pro_user_pref;
	RelativeLayout rel_background, rel_edt_profile, rel_about, rel_sign_out,
			rel_rate, rel_loc;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	TextView txt_city_name;
	 TextView txt_head;
	AsyncHttpClient client;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;
	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	private static String LAST_UPDATE_REST_URL = "";
	private static String ALL_AREA_REST_URL = "";
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	HomeSearchAllAreaDatabaseHandler home_search_area_db;
	String model_market1, time_market1;
	String save_model_market1, save_time_market1;
	Editor editor_market1;
	SharedPreferences prefs_area1;
	String text = "";
	String line = "";
	ArrayList<updateSearch> updateSearch = new ArrayList<updateSearch>();
	String market_name;
	SharedPreferences pref_city, pref_location1;
	Editor edit_city;
	public static String city;
	int fragment_pos;
	public static boolean callme = false;
	private GoogleApiClient mGoogleApiClient;
	public static int height;
	Display display;
	public static boolean area_bol = false;
	ListPopupWindow listPopupWindow;
	String[] product_city = { "Delhi / NCR", "Mumbai" };
	public static ArrayList<Integer> mycolorlist = new ArrayList<Integer>();
	public static ArrayList<POJO> mypojolist = new ArrayList<POJO>();
	public static int mypage = 1;

	public static ArrayList<Integer> mycolorlist1 = new ArrayList<Integer>();
	public static ArrayList<POJO> mypojolist1 = new ArrayList<POJO>();
	public static int mypage1 = 1;

	public static String img_detail = "nocome";
	public static String edt_prof = "noedit";
	public static String imgurl,age,loc,gender;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	// make interface
	public interface Updateable {
		public void update();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		cd = new ConnectionDetector(getApplicationContext());
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Main Activity");
		t.send(new HitBuilders.AppViewBuilder().build());

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(MainActivity.this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		client = ClientHttp.getInstance(MainActivity.this);
		/*
		 * pref_city = getSharedPreferences("CITYPREF", 1); edit_city =
		 * pref_city.edit();
		 */
		try {
			pref_location1 = getSharedPreferences("location", 1);
			edit_city = pref_location1.edit();

			/*
			 * edit_city.putString("loc", pref_location1.getString("city",
			 * "Delhi/NCR")); edit_city.commit();
			 */
			city = pref_location1.getString("city", "Delhi");
			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = getSharedPreferences("User_detail", 0);
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_age = pro_user_pref.getString("user_age", "aasdf");
			pro_user_gender = pro_user_pref.getString("user_gender", "145");
			
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
//Log.e("user_password", user_password);
			prefs_area1 = getSharedPreferences("PREF", 1);
			editor_market1 = prefs_area1.edit();
			model_market1 = prefs_area1.getString("model_market1", "NA");
			time_market1 = prefs_area1.getString("time_market1", "N/A");

			save_model_market1 = prefs_area1.getString("model_market1", "gh");
			save_time_market1 = prefs_area1.getString("time_market1", "ghf");
//Log.e("pro_user_pic_url", pro_user_pic_url);
			home_search_area_db = new HomeSearchAllAreaDatabaseHandler(
					getApplicationContext());
			display = getWindowManager().getDefaultDisplay();
			height = display.getHeight();
		} catch (Exception e) {
			// TODO: handle exception
		}
		/**
		 * Side Menu
		 */
		img_menu = (ImageView) findViewById(R.id.img_menu);
		res = this.getResources();
		txt_city_name = (TextView) findViewById(R.id.txt_city_name);
		txt_head = (TextView) findViewById(R.id.txt);
		rel_loc = (RelativeLayout) findViewById(R.id.rel_loc);
		slide_me = new SimpleSideDrawer(this);
		slide_me.setRightBehindContentView(R.layout.side_menu);
		pro_user_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
		side_user_name = (TextView) findViewById(R.id.txt_user_name);
		side_about = (TextView) findViewById(R.id.txt_about);
		rel_about = (RelativeLayout) findViewById(R.id.rel_about);
		side_con_ac = (TextView) findViewById(R.id.txt_con_ac);
		side_edt_profile = (TextView) findViewById(R.id.txt_edit);
		side_noti_settings = (TextView) findViewById(R.id.txt_noti);
		side_rate = (TextView) findViewById(R.id.txt_rate);
		rel_rate = (RelativeLayout) findViewById(R.id.rel_rate);
		rel_sign_out = (RelativeLayout) findViewById(R.id.rel_sign_out);
		side_sign_out = (TextView) findViewById(R.id.txt_sign_out);
		side_sug_store = (TextView) findViewById(R.id.txt_sug_store);
		rel_edt_profile = (RelativeLayout) findViewById(R.id.rel_edt_profile);
		
		
		Picasso.with(getApplicationContext()).load(pro_user_pic_url).placeholder(R.drawable.profile_img_3).resize(50, 50)
		.into(pro_user_pic);
		
		side_user_name.setText(pro_user_name);

		/**
		 * Typeface
		 */

		typeface_semibold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		try {

			/**
			 * set Font on TextView
			 */
			 txt_city_name.setTypeface(typeface_regular);
			side_user_name.setTypeface(typeface_bold);
			side_about.setTypeface(typeface_regular);
			side_con_ac.setTypeface(typeface_regular);
			side_edt_profile.setTypeface(typeface_regular);
			side_noti_settings.setTypeface(typeface_regular);
			side_rate.setTypeface(typeface_regular);
			side_sign_out.setTypeface(typeface_regular);
			side_sug_store.setTypeface(typeface_regular);
			txt_head.setTypeface(typeface_semibold);
			txt_head.setText("Feed");
		} catch (Exception e) {
			// TODO: handle exception
		}
		/**
		 * SideMenu ClickListener
		 */

		rel_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
				slide_me.toggleRightDrawer();
				Intent i = new Intent(MainActivity.this, AboutUs.class);
				startActivity(i);

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on About Us")
						.setAction("Clicked About Us by " + pro_user_name)
						.setLabel("Main Activity").build());
				} else {
					showAlertDialog(MainActivity.this,
							"No Internet Connection",
							"You don't have internet connection.", false);
				}
			}
		});

		rel_rate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slide_me.toggleRightDrawer();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=com.zakoopi"));
				startActivity(intent);

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Rate US")
						.setAction("clicked Rate Us by " + pro_user_name)
						.setLabel("Main Activity").build());
			}
		});

		rel_edt_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slide_me.toggleRightDrawer();
				
				edt_prof = "noedit";
				
				Intent edt_profile = new Intent(MainActivity.this,
						EditProfilePage.class);
				startActivityForResult(edt_profile, 0);
				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Edit Profile View")
						.setAction("Clicked Edit Profile by " + pro_user_name)
						.setLabel("Main Activity").build());

			}
		});

		rel_sign_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// add stuff here
				slide_me.toggleRightDrawer();
				Editor editor = getSharedPreferences("User_detail",
						Context.MODE_PRIVATE).edit();
				editor.clear();
				editor.commit();
				if (mGoogleApiClient.isConnected()) {
					Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
					mGoogleApiClient.disconnect();
					Intent in = new Intent(MainActivity.this,
							LoginActivity.class);
					in.putExtra("data", "no");
					
					startActivity(in);
					finish();
					Tracker t = ((UILApplication) getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Click on G+ SignOut View")
							.setAction(
									"Cilcked SignOut via Google+ by "
											+ pro_user_name)
							.setLabel("Main Activity").build());

				} else {

					Intent in = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(in);
					finish();

					Tracker t = ((UILApplication) getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.
					t.send(new HitBuilders.EventBuilder()
							.setCategory("Click on SignOut View")
							.setAction("Cilcked SignOut by " + pro_user_name)
							.setLabel("Main Activity").build());
				}
			}
		});

		img_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				slide_me.toggleRightDrawer();

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Navigation Menu")
						.setAction(
								"Cilcked Navigation Menu by " + pro_user_name)
						.setLabel("Main Activity").build());
			}
		});

		/**
		 * TabHost & ViewPager
		 */
		tabHost = (MaterialTabHostIcon) this.findViewById(R.id.tabHost);
		pager = (ViewPager) this.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		
		area_bol = true;

		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// when user do a swipe the selected tab change
				tabHost.setSelectedNavigationItem(position);

				fragment_pos = position;
				
				if (fragment_pos == 0) {
					txt_head.setText("Feed");
				} else if (fragment_pos == 1) {
					txt_head.setText("Discover");
				} else {
					txt_head.setText("My Profile");
				}
			}
		});
		// insert all tabs from pagerAdapter data
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			tabHost.addTab(tabHost.newTab().setIcon(getIcon(i))
					.setTabListener(this));
		}

		/**
		 * Add Lookbook Button
		 */
		float_btn = (FloatingActionButton) findViewById(R.id.float_button);
		float_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final Dialog dialog_add_lookbook = new Dialog(MainActivity.this);
				dialog_add_lookbook
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_add_lookbook
						.setContentView(R.layout.add_lookbook_dialog);
				TextView txt_upload_lookbook = (TextView) dialog_add_lookbook
						.findViewById(R.id.txt_upload_lookbook);
				TextView txt_write_review = (TextView) dialog_add_lookbook
						.findViewById(R.id.txt_write_review);
				RelativeLayout rel_upload_lookbook = (RelativeLayout) dialog_add_lookbook
						.findViewById(R.id.rel_upload);
				RelativeLayout rel_write_review = (RelativeLayout) dialog_add_lookbook
						.findViewById(R.id.rel_write);
				ImageView img_close = (ImageView) dialog_add_lookbook
						.findViewById(R.id.img_close);

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Float Button")
						.setAction("Cilcked Float Button by " + pro_user_name)
						.setLabel("Main Activity").build());

				txt_upload_lookbook.setTypeface(typeface_regular);
				txt_write_review.setTypeface(typeface_regular);

				/**
				 * dialog ClcikListener
				 */
				rel_upload_lookbook.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent upload_lookbook = new Intent(MainActivity.this,
								LookBookTabsActivity.class);
						startActivity(upload_lookbook);
						dialog_add_lookbook.dismiss();

						Tracker t = ((UILApplication) getApplication())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.
						t.send(new HitBuilders.EventBuilder()
								.setCategory("Click on Upload Lookbook")
								.setAction(
										"Cilcked Upload Lookbook by "
												+ pro_user_name)
								.setLabel("Main Activity").build());
					}
				});
				rel_write_review.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent in = new Intent(MainActivity.this,
								SearchStoreActivity.class);
						in.putExtra("SearchStore", "Discover");
						in.putExtra("SearchStore", "review");
						startActivity(in);
						dialog_add_lookbook.dismiss();

						Tracker t = ((UILApplication) getApplication())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.
						t.send(new HitBuilders.EventBuilder()
								.setCategory("Click on Write Review")
								.setAction(
										"Cilcked Write Review by "
												+ pro_user_name)
								.setLabel("Main Activity").build());
					}
				});
				img_close.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_add_lookbook.dismiss();
					}
				});
				dialog_add_lookbook.show();

			}
		});

		if (callme == false) {
			// last_update();
			// all_area();
		}
		if (city.equals("Delhi")) {
			txt_city_name.setText("Delhi / NCR");
		} else {
			txt_city_name.setText(city);
		}

		
	    
		txt_city_name.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.
				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Change City")
						.setAction("Cilcked Change City by " + pro_user_name)
						.setLabel("Main Activity").build());

				listPopupWindow = new ListPopupWindow(MainActivity.this);
				listPopupWindow.setAdapter(new ArrayAdapter(MainActivity.this,
						R.layout.popup_menu_item,R.id.txt_popup ,product_city));
				listPopupWindow.setAnchorView(rel_loc);
				listPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
				listPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);

				listPopupWindow.setModal(true);

				listPopupWindow
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								if (!txt_city_name.getText().toString()
										.equals("Delhi / NCR")) {

									edit_city.putString("city", "Delhi");
									txt_city_name.setText("Delhi / NCR");

								} else {

									edit_city.putString("city",
											product_city[position]);
									txt_city_name
											.setText(product_city[position]);
								}

								edit_city.commit();

								city = pref_location1
										.getString("city", "Delhi");

								callme = true;
								HomeDiscoverFragment.top_mar = false;
								Variables.areaList.clear();
								home_search_area_db.allDelete();
								//HomeSearchTopMarket.marketsTop.clear();
								HomeSearchTopTrends.trendsTop.clear();
								
								PopularFeedFragment.statuscode="202";
								
							
								
								MainWebservice();

								listPopupWindow.dismiss();
							}
						});
				listPopupWindow.show();
			}

		});

		if (!Variables.showdrafts.equals("nodrafts")) {

			pager.setCurrentItem(2);
			Variables.showdrafts = "nodrafts";
		}

		// try {
		// if (img_detail.equals("success")) {
		// img_detail = "success";
		// pager.setCurrentItem(2);
		// }
		//
		// if (edt_prof.equals("edit")) {
		// edt_prof = "edit";
		// pager.setCurrentItem(2);
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// }

	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) {
	 * 
	 * InputMethodManager imm = (InputMethodManager)
	 * getSystemService(Context.INPUT_METHOD_SERVICE);
	 * imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	 * return true; }
	 */

	// for g+
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
		GoogleAnalytics.getInstance(MainActivity.this)
				.reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		try {
			imgurl = data.getStringExtra("img");
			age = data.getStringExtra("age");
			String name = data.getStringExtra("name");
			loc = data.getStringExtra("loc");
			gender = data.getStringExtra("gender");
			Picasso.with(getApplicationContext()).load(imgurl)
					.placeholder(R.drawable.profile_img_3).resize(50, 50)
					.into(pro_user_pic);
			side_user_name.setText(name);
			Variables.edt_profil = "new";

			if (edt_prof.equals("noedit")) {

				// pagerAdapter = new
				// ViewPagerAdapter(getSupportFragmentManager());
				// pager.setAdapter(null);
				// pager.setAdapter(pagerAdapter);
				pagerAdapter.notifyDataSetChanged();
				pager.setCurrentItem(2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zakoopi.tab_layout.MaterialTabListener#onTabSelected(com.zakoopi.
	 *      tab_layout.MaterialTab)
	 */
	@Override
	public void onTabSelected(MaterialTab tab) {
		pager.setCurrentItem(tab.getPosition());
		
		if (tab.getPosition() == 0) {
			txt_head.setText("Feed");
		} else if (tab.getPosition() == 1) {
			txt_head.setText("Search");
		} else {
			txt_head.setText("My Profile");
		}
	}

	@Override
	public void onTabReselected(MaterialTab tab) {

	}

	@Override
	public void onTabUnselected(MaterialTab tab) {

	}

	/**
	 * ViewPagerAdapter Class
	 * 
	 * Set Fragment 1. HomeFeedFragment 2. HomeDiscoverFragment 3.
	 * HomeProfileFrag
	 */
	private class ViewPagerAdapter extends FragmentPagerAdapter {
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int num) {
			if (num == 0) {
				HomeFeedFragment frag = new HomeFeedFragment();
				
				return frag;
			} else if (num == 1) {
				HomeDiscoverFragment fragmentSearch = new HomeDiscoverFragment();
				
				return fragmentSearch;
			} else {
				HomeProfileFrag fragmentProfile = new HomeProfileFrag();
				
				return fragmentProfile;
			}

		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "tab 1";
			case 1:
				return "tab 2";
			case 2:
				return "tab 3";

			default:
				return null;
			}
		}
	}

	/**
	 * Tab Drawable Icon
	 */
	private Drawable getIcon(int position) {
		switch (position) {
		case 0:
			return res.getDrawable(R.drawable.home_home_inactive);
		case 1:
			return res.getDrawable(R.drawable.home_discover_inactive);
		case 2:
			return res.getDrawable(R.drawable.home_profile_active);
		}
		return null;
	}

	public void last_update() {

		LAST_UPDATE_REST_URL = getString(R.string.base_url)
				+ "lastUpdates.json";

		// Log.e("update_url", LAST_UPDATE_REST_URL);
		// client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(LAST_UPDATE_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = bufferedReader.readLine()) != null) {

						text = text + line;
					}
					all_update(text);
				} catch (Exception e) {

				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}

	public void all_update(String data) {
		// String timeStamp = "";
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		SearchUpdate searchUpdate = gson.fromJson(jsonReader,
				SearchUpdate.class);
		updateSearch = searchUpdate.getUpdate();

		for (int i = 0; i < updateSearch.size(); i++) {
			updateSearch ser = updateSearch.get(i);
			// Log.e("SER", "" + ser);
			if (ser.get_model().equals("Markets")) {
				// timeStamp = ser.get_timestamp();

				if (!time_market1.equals(ser.get_timestamp())) {
					// Log.e("AREA1", time_market1 + "--" +
					// ser.get_timestamp());
					editor_market1.putString("model_market1", ser.get_model());
					editor_market1.putString("time_market1",
							ser.get_timestamp());
					editor_market1.commit();
					// Log.e("EDi", "" + editor_market1);
					// Log.e("AREA", time_market1 + "--" + ser.get_timestamp());
					all_area();

				}

			}

		}
		/*
		 * if (time_market1.equals(timeStamp)) { areaList =
		 * home_search_area_db.getAllAreas1(); ArrayAdapter<String> adp = new
		 * ArrayAdapter<String>(getApplicationContext(),
		 * R.layout.home_search_spinner_item, R.id.txt_product, areaList);
		 * search_spinner_area.setAdapter(adp);
		 * 
		 * }
		 */

	}

	/**
	 * {@code} all_area()
	 * 
	 * @return void
	 */
	public void all_area() {
		ALL_AREA_REST_URL = getString(R.string.base_url) + "markets.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_AREA_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {

						text2 = text2 + text1;
					}
					allArea_showData(text2);
					// /Log.e("AREA", text2);
				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

			}
		});
	}

	/**
	 * 
	 * @allArea_showData data1
	 * @return void
	 */
	public void allArea_showData(String data1) {

		markets.clear();
		home_search_area_db.allDelete();
		home_search_area_db.addAllArea(new AllArea("All Areas", "All Areas"));
		Gson gson1 = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data1));
		jsonReader.setLenient(true);

		Area area1 = gson1.fromJson(jsonReader, Area.class);
		markets = area1.getMarkets();
		for (int i = 0; i < markets.size(); i++) {
			market_name = markets.get(i).get_market_name();
			String slug_name = markets.get(i).getUrl_slug();
			home_search_area_db.addAllArea(new AllArea(market_name, slug_name));
		}
		Variables.areaList.clear();
		Variables.areaList = home_search_area_db.getAllAreas1();

	}

	/**
	 * MainWebService()
	 */

	public void MainWebservice() {
		String MAIN_WEBSERVICE_REST_URL = "";
		// Log.e("CITY", city);
		// Log.e("MAIN", MAIN_WEBSERVICE_REST_URL);
		MAIN_WEBSERVICE_REST_URL = getString(R.string.base_url)
				+ "start/setClientLocation/" + city + ".json";
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(MAIN_WEBSERVICE_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;
						// Log.e("success", "success");

					}
					// Log.e("RES", st1);
					setCitySlug(st1);

					pager.setAdapter(null);
					pager.setAdapter(pagerAdapter);
					pager.setCurrentItem(fragment_pos);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

				// Log.e("ggg", "fail");
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	public void setCitySlug(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		MainClient main_user1 = gson.fromJson(jsonReader, MainClient.class);
		ClientLocationModel client = main_user1.getClientLocation();

		SharedPreferences clientcity = getSharedPreferences("clientcity", 10);
		Editor edit = clientcity.edit();
		edit.putString("id", client.getId());
		edit.putString("city", client.getCity_name());
		edit.putString("cityslug", client.getSlug());
		edit.commit();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title,
			String message, Boolean status) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle("No Internet Connection!");

		// Setting Dialog Message
		alertDialog.setMessage("Enable Internet Connection.");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						context.startActivity(intent);
						dialog.cancel();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Dismiss",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
}