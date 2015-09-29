package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cam.imagedatabase.DBHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.activity.EditProfilePage;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.activity.ProfileDrafts;
import com.zakoopi.activity.ProfileReviews;
import com.zakoopi.activity.ProflieLookbooks;
import com.zakoopi.activity.ZakoopiPoints;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.Variables;
import com.zakoopi.user.model.User;
import com.zakoopi.user.model.UserDetails;
import com.zakoopi.user.model.UserInfoPojo;
import com.zakoopi.userfeed.model.UserArticleData;
import com.zakoopi.userfeed.model.UserArticleImages;
import com.zakoopi.userfeed.model.UserFeed;
import com.zakoopi.userfeed.model.UserFeedAdapter;
import com.zakoopi.userfeed.model.UserFeedPojo;
import com.zakoopi.userfeed.model.UserStoreData;
import com.zakoopi.userfeed.model.UserStoreReviewData;
import com.zakoopi.userfeed.model.User_LookbookData;
import com.zakoopi.userfeed.model.User_lookbook_Cards;
import com.zakoopi.userfeed.model.feedTimeline;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class HomeProfileFrag extends Fragment {

	CircleImageView img_profile;
	TextView txt_user_name, txt_user_age, txt_user_gender, txt_user_city,
			txt_user_zakoopi_points, txt_user_look_count,
			txt_user_review_count, txt_user_like_count, user_activity,
			txt_zakoopi_point, txt_lookbook, txt_review, txt_like;
	RelativeLayout rel_lookbooks, rel_reviews, rel_draft;
	TextView txt_lookbooks, txt_reviews, txt_draft;
	private DisplayImageOptions options;
	private SharedPreferences pro_user_pref;
	private static String USER_INFO_REST_URL = "";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String user_email, user_password, user_id, pro_user_name, pro_user_pic_url,
			pro_user_age, pro_user_gender, pro_user_location;
	String user_age, user_gender, user_city, zakoopi_points, lookbook_count,
			review_count, like_count, fb_link, twitter_link, website_link, uid,
			lookbook_draft_count;
	public static String url_img, user_name;
	ImageView img_fb, img_twitter, img_website, img_edit_profile,
			img_question_mark;
	View view_fb, view_twitter;
	RelativeLayout rel_social;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	View v1, v2, v3;

	/**
	 * 
	 * 
	 */
	private UserFeedAdapter adapter;
	private EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	private static String USER_ACTIVITY_REST_URL = " ";
	int page = 1;
	Dialog diaog;
	public ArrayList<UserFeedPojo> feedPojo = null;
	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	UserDetails main_user;
	ProgressBar progressBar1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	RelativeLayout rel_point;
	UserInfoPojo uip;
	private SQLiteDatabase db;
	public static final String DBTABLE4 = "LoginUserInfo";
	public static final String DBTABLE5 = "LoginUserAct";
	private SQLiteStatement stm;
	private SQLiteStatement stm_act;
	private BroadcastReceiver br;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View view = inflater.inflate(R.layout.profile_main_layout, null);

		try {
			DBHelper hp = new DBHelper(getActivity());
			db = hp.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		stm = db.compileStatement("insert into  "
				+ DBTABLE4
				+ " (user_name,url_img,user_gender,user_age,user_city,zakoopi_points,lookbook_count,"
				+ "like_count,review_count,fb_link,twitter_link,website_link,lookbook_draft_count,uid) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		stm_act = db
				.compileStatement("insert into  "
						+ DBTABLE5
						+ " (mode,type,username,userimg,lookimg,img1,img2,"
						+ "likes,hits, title,store_name,store_location,review,"
						+ "image_count,idd,description, is_new,is_liked,slug,"
						+ "rated_color,rated,userid,comment_count,img_width,img_height,"
						+ "status,color,hits_view,hits_count,store_id) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		client = ClientHttp.getInstance(getActivity());
		cd = new ConnectionDetector(getActivity());
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Login user Profile");
		t.send(new HitBuilders.AppViewBuilder().build());

		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		user_id = pro_user_pref.getString("user_id", "adajfh");
		user_email = pro_user_pref.getString("user_email", "9089");
		pro_user_age = pro_user_pref.getString("user_age", "aasdf");
		pro_user_gender = pro_user_pref.getString("user_gender", "145");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_password = pro_user_pref.getString("password", "sar");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");

		// Variables.user_id_list.add(user_id);
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);

		/**
		 * User Login SharedPreferences
		 */
		// pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);

		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		findId(view);

		View profile_header = inflater
				.inflate(R.layout.home_profile_frag, null);
		img_profile = (CircleImageView) profile_header
				.findViewById(R.id.img_profile);

		txt_user_name = (TextView) profile_header
				.findViewById(R.id.txt_user_name);
		user_activity = (TextView) profile_header
				.findViewById(R.id.user_activity);
		txt_user_age = (TextView) profile_header
				.findViewById(R.id.txt_user_age);
		txt_user_city = (TextView) profile_header
				.findViewById(R.id.txt_user_location);
		txt_user_gender = (TextView) profile_header
				.findViewById(R.id.txt_user_gander);
		txt_user_like_count = (TextView) profile_header
				.findViewById(R.id.txt_like_count);
		/*
		 * txt_user_look_count = (TextView) profile_header
		 * .findViewById(R.id.txt_lookbook_count);
		 */
		/*
		 * txt_user_review_count = (TextView) profile_header
		 * .findViewById(R.id.txt_review_count);
		 */
		rel_lookbooks = (RelativeLayout) profile_header
				.findViewById(R.id.rel_lookbooks);
		rel_draft = (RelativeLayout) profile_header
				.findViewById(R.id.rel_draft);
		rel_reviews = (RelativeLayout) profile_header
				.findViewById(R.id.rel_reviews);
		txt_user_zakoopi_points = (TextView) profile_header
				.findViewById(R.id.txt_point);
		txt_zakoopi_point = (TextView) profile_header
				.findViewById(R.id.txt_zakoopi_point);
		/*
		 * txt_lookbook = (TextView) profile_header
		 * .findViewById(R.id.txt_lookbook);
		 */
		// txt_review = (TextView) profile_header.findViewById(R.id.txt_review);
		txt_like = (TextView) profile_header.findViewById(R.id.txt_like);
		img_edit_profile = (ImageView) profile_header
				.findViewById(R.id.img_edt_profile);
		rel_point = (RelativeLayout) profile_header
				.findViewById(R.id.rel_point);

		txt_lookbooks = (TextView) profile_header
				.findViewById(R.id.text_lookbooks);
		txt_reviews = (TextView) profile_header.findViewById(R.id.text_reviews);
		txt_draft = (TextView) profile_header.findViewById(R.id.text_draft);
		txt_lookbooks.setTypeface(typeface_semibold);
		txt_reviews.setTypeface(typeface_semibold);
		txt_draft.setTypeface(typeface_semibold);
		txt_user_age.setTypeface(typeface_regular);
		user_activity.setTypeface(typeface_semibold);
		txt_user_city.setTypeface(typeface_regular);
		txt_user_gender.setTypeface(typeface_regular);
		txt_user_like_count.setTypeface(typeface_semibold);
		// txt_user_look_count.setTypeface(typeface_semibold);
		txt_user_name.setTypeface(typeface_semibold);
		// txt_user_review_count.setTypeface(typeface_semibold);
		txt_user_zakoopi_points.setTypeface(typeface_semibold);
		txt_zakoopi_point.setTypeface(typeface_regular);
		// txt_lookbook.setTypeface(typeface_regular);
		// txt_review.setTypeface(typeface_regular);
		txt_like.setTypeface(typeface_regular);
		img_edit_profile = (ImageView) profile_header
				.findViewById(R.id.img_edt_profile);
		v1 = (View) profile_header.findViewById(R.id.v1);
		v2 = (View) profile_header.findViewById(R.id.v2);
		v3 = (View) profile_header.findViewById(R.id.v3);

		img_fb = (ImageView) profile_header.findViewById(R.id.img_facebook);
		img_twitter = (ImageView) profile_header.findViewById(R.id.img_twitter);
		img_website = (ImageView) profile_header.findViewById(R.id.img_website);
		rel_social = (RelativeLayout) profile_header
				.findViewById(R.id.rel_social);

		view_fb = (View) profile_header.findViewById(R.id.view_fb);
		view_twitter = (View) profile_header.findViewById(R.id.view_twitter);
		// mHaveMoreDataToLoad = true;

		endlessListView.addHeaderView(profile_header);
		endlessListView.setOnLoadMoreListener(loadMoreListener);
		USER_ACTIVITY_REST_URL = getString(R.string.base_url)
				+ "feedTimeline/byUserId/" + user_id + ".json?page=";
		// Log.e("URRRl", USER_ACTIVITY_REST_URL);
		// user_info();
		// activity_feed(page);
		social_click();
		rel_draft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), ProfileDrafts.class);
				i.putExtra("user_id", user_id);
				getActivity().startActivity(i);

			}
		});

		rel_lookbooks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					Intent i = new Intent(getActivity(), ProflieLookbooks.class);
					i.putExtra("user_id", user_id);
					getActivity().startActivity(i);
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(getActivity(), "No Internet Connection",
							"You don't have internet connection.", false);
				}

				// TODO Auto-generated method stub

			}
		});

		rel_reviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					Intent i = new Intent(getActivity(), ProfileReviews.class);
					i.putExtra("user_id", user_id);
					getActivity().startActivity(i);
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(getActivity(), "No Internet Connection",
							"You don't have internet connection.", false);
				}

				// TODO Auto-generated method stub

			}
		});

		return view;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(
				getActivity());
	}

	public void findId(View view) {

		endlessListView = (EndlessListView) view
				.findViewById(R.id.endlessListView_activity);

	}

	@Override
	public void onResume() {

		super.onResume();

		if (Variables.edt_profil.equals("new")) {
			adapter.notifyDataSetChanged();
			Picasso.with(getActivity()).load(MainActivity.imgurl)
					.placeholder(R.drawable.profile_img_3).resize(67, 67)
					.into(img_profile);

			// Log.e("ON1", "ON1");

			if (!MainActivity.age.equals("") && !MainActivity.loc.equals("")
					&& !MainActivity.gender.equals("")) {

				txt_user_age.setVisibility(View.VISIBLE);
				txt_user_gender.setVisibility(View.VISIBLE);
				txt_user_city.setVisibility(View.VISIBLE);
				txt_user_age.setText(MainActivity.age);
				txt_user_city.setText(MainActivity.loc);

				if (MainActivity.gender.equals("true")) {
					txt_user_gender.setText("Female");
				} else if (MainActivity.gender.equals("false")) {
					txt_user_gender.setText("Male");
				}
				v1.setVisibility(View.VISIBLE);
				v2.setVisibility(View.VISIBLE);
				
			} else if (MainActivity.age.equals("")
					&& MainActivity.loc.equals("")
					&& MainActivity.gender.equals("")) {

				txt_user_age.setVisibility(View.GONE);
				txt_user_city.setVisibility(View.GONE);
				txt_user_gender.setVisibility(View.GONE);

				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				// Log.e("Log2*", "Log2*");
			} else if (!MainActivity.age.equals("")
					&& !MainActivity.loc.equals("")
					&& MainActivity.gender.equals("")) {

				txt_user_age.setVisibility(View.VISIBLE);
				txt_user_gender.setVisibility(View.VISIBLE);
				txt_user_age.setText(MainActivity.age);
				txt_user_city.setText(MainActivity.loc);
				txt_user_gender.setVisibility(View.GONE);

				v1.setVisibility(View.VISIBLE);
				v2.setVisibility(View.GONE);
				// Log.e("Log3*", "Log3*");

			} else if (!MainActivity.age.equals("")
					&& MainActivity.loc.equals("")
					&& !MainActivity.gender.equals("")) {

				txt_user_age.setVisibility(View.VISIBLE);
				txt_user_gender.setVisibility(View.VISIBLE);

				txt_user_age.setText(MainActivity.age);
				txt_user_city.setVisibility(View.GONE);

				if (MainActivity.gender.equals("true")) {
					txt_user_gender.setText("Female");
				} else if (MainActivity.gender.equals("false")) {
					txt_user_gender.setText("Male");
				}

				v1.setVisibility(View.VISIBLE);
				v2.setVisibility(View.GONE);

				// Log.e("Log4*", "Log4*");

			} else if (MainActivity.age.equals("")
					&& !MainActivity.loc.equals("")
					&& !MainActivity.gender.equals("")) {

				txt_user_gender.setVisibility(View.VISIBLE);
				txt_user_city.setVisibility(View.VISIBLE);

				txt_user_age.setVisibility(View.GONE);
				txt_user_city.setText(MainActivity.loc);

				if (MainActivity.gender.equals("true")) {
					txt_user_gender.setText("Female");
				} else if (MainActivity.gender.equals("false")) {
					txt_user_gender.setText("Male");
				}

				v1.setVisibility(View.GONE);
				v2.setVisibility(View.VISIBLE);

				// Log.e("Log5*", "Log5*");

			} else if (!MainActivity.age.equals("")) {

				txt_user_age.setVisibility(View.VISIBLE);

				txt_user_age.setText(MainActivity.age);
				txt_user_city.setVisibility(View.GONE);

				txt_user_gender.setVisibility(View.GONE);

				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);

				// Log.e("Log6*", "Log6*");

			} else if (!MainActivity.loc.equals("")) {

				txt_user_city.setVisibility(View.VISIBLE);

				txt_user_age.setVisibility(View.GONE);
				txt_user_city.setText(MainActivity.loc);

				txt_user_gender.setVisibility(View.GONE);

				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				// Log.e("Log7*", "Log7*");

			} else if (!MainActivity.gender.equals("")) {

				txt_user_gender.setVisibility(View.VISIBLE);

				txt_user_age.setVisibility(View.GONE);
				txt_user_city.setVisibility(View.GONE);

				if (MainActivity.gender.equals("true")) {
					txt_user_gender.setText("Female");
				} else if (MainActivity.gender.equals("false")) {
					txt_user_gender.setText("Male");
				}

				v1.setVisibility(View.GONE);
				v2.setVisibility(View.GONE);
				// Log.e("Log8*", "Log8*");

			}

		} else {
			Picasso.with(getActivity()).load(pro_user_pic_url)
					.placeholder(R.drawable.profile_img_3).resize(67, 67)
					.into(img_profile);
			// Log.e("ON2", "ON2");
			checkInternetConnection();
		}

		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			// countDownTimer = new MyCountDownTimer(startTime, interval);
			// countDownTimer.start();
		}

	}

	private void checkInternetConnection() {

		if (br == null) {

			br = new BroadcastReceiver() {

				@Override
				public void onReceive(Context arg0, Intent intent) {
					// TODO Auto-generated method stub
					Bundle extras = intent.getExtras();

					NetworkInfo info = (NetworkInfo) extras
							.getParcelable("networkInfo");

					State state = info.getState();
					/*
					 * Log.d("TEST Internet", info.toString() + " " +
					 * state.toString());
					 */

					if (state == State.CONNECTED) {

					/*	Toast.makeText(getActivity(),
								"Internet connection is on", Toast.LENGTH_LONG)
								.show();*/

						try {
							DBHelper hp = new DBHelper(getActivity());
							db = hp.getWritableDatabase();
							db.delete(DBTABLE4, null, null);
							db.delete(DBTABLE5, null, null);
						} catch (Exception e) {
							// TODO: handle exception
						}
						

						// popular_feed(page);
						user_info();

					} else {

						/*Toast.makeText(getActivity(),
								"Internet connection is Off", Toast.LENGTH_LONG)
								.show();*/

						mHaveMoreDataToLoad = false;

						progressBar1.setVisibility(View.GONE);

						new localInfo().execute();

					}
				}
			};

			final IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			getActivity()
					.registerReceiver((BroadcastReceiver) br, intentFilter);

		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			getActivity().unregisterReceiver(br);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private class localInfo extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				Cursor c = db.rawQuery(" select * from " + DBTABLE4, null);

				if (c != null) {
					while (c.moveToNext()) {
						String user_name = c.getString(c
								.getColumnIndex("user_name"));
						String url_img = c.getString(c
								.getColumnIndex("url_img"));
						String user_gender = c.getString(c
								.getColumnIndex("user_gender"));
						String user_age = c.getString(c
								.getColumnIndex("user_age"));
						String user_city = c.getString(c
								.getColumnIndex("user_city"));
						String zakoopi_points = c.getString(c
								.getColumnIndex("zakoopi_points"));
						String lookbook_count = c.getString(c
								.getColumnIndex("lookbook_count"));
						String like_count = c.getString(c
								.getColumnIndex("like_count"));
						String review_count = c.getString(c
								.getColumnIndex("review_count"));
						String fb_link = c.getString(c
								.getColumnIndex("fb_link"));
						String twitter_link = c.getString(c
								.getColumnIndex("twitter_link"));
						String website_link = c.getString(c
								.getColumnIndex("website_link"));
						String lookbook_draft_count = c.getString(c
								.getColumnIndex("lookbook_draft_count"));
						String uid = c.getString(c.getColumnIndex("uid"));

						//Log.e("LOOOC", user_age+"--"+user_gender);
						uip = new UserInfoPojo(user_age, user_city,
								user_gender, user_name, url_img,
								zakoopi_points, lookbook_count, like_count,
								review_count, fb_link, twitter_link,
								website_link, lookbook_draft_count, uid);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {

				user_activity.setText("My Activity");
				txt_user_name.setText(uip.getUser_name());
				txt_user_like_count.setText(uip.getLike_count());
				txt_user_zakoopi_points.setText(uip.getZakoopi_points());

				if (Integer.parseInt(uip.getLookbook_count()) > 0) {
					txt_lookbooks.setText("Lookbooks ("
							+ uip.getLookbook_count() + ")");
					rel_lookbooks.setClickable(true);
				} else {
					txt_lookbooks.setText("Lookbooks ("
							+ uip.getLookbook_count() + ")");
					rel_lookbooks.setClickable(false);
				}

				Picasso.with(getActivity()).load(pro_user_pic_url)
						.placeholder(R.drawable.profile_img_3).resize(67, 67)
						.into(img_profile);

				if (!uip.getUser_age().equals("")
						&& !uip.getUser_city().equals("")
						&& !uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);
					txt_user_gender.setVisibility(View.VISIBLE);
					txt_user_city.setVisibility(View.VISIBLE);
					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setText(uip.getUser_city());

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}
					v1.setVisibility(View.VISIBLE);
					v2.setVisibility(View.VISIBLE);

					

				} else if (uip.getUser_age().equals("")
						&& uip.getUser_city().equals("")
						&& uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setVisibility(View.GONE);
					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);
				} else if (!uip.getUser_age().equals("")
						&& !uip.getUser_city().equals("")
						&& uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);
					txt_user_gender.setVisibility(View.VISIBLE);
					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setText(uip.getUser_city());
					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.VISIBLE);
					v2.setVisibility(View.GONE);

				} else if (!uip.getUser_age().equals("")
						&& uip.getUser_city().equals("")
						&& !uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);
					txt_user_gender.setVisibility(View.VISIBLE);

					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setVisibility(View.GONE);

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}

					v1.setVisibility(View.VISIBLE);
					v2.setVisibility(View.GONE);

				} else if (uip.getUser_age().equals("")
						&& !uip.getUser_city().equals("")
						&& !uip.getUser_gender().equals("")) {

					txt_user_gender.setVisibility(View.VISIBLE);
					txt_user_city.setVisibility(View.VISIBLE);

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setText(uip.getUser_city());

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.VISIBLE);

				} else if (!uip.getUser_age().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);

					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setVisibility(View.GONE);

					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);

				} else if (!uip.getUser_city().equals("")) {

					txt_user_city.setVisibility(View.VISIBLE);

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setText(uip.getUser_city());

					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);
				} else if (!uip.getUser_gender().equals("")) {

					txt_user_gender.setVisibility(View.VISIBLE);

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setVisibility(View.GONE);

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);

				}
				
				if (Integer.parseInt(uip.getReview_count()) > 0) {
					txt_reviews.setText("Reviews (" + uip.getReview_count()
							+ ")");
					rel_reviews.setClickable(true);
				} else {
					txt_reviews.setText("Reviews (" + uip.getReview_count()
							+ ")");
					rel_reviews.setClickable(false);
				}

				if (Integer.parseInt(uip.getReview_count()) > 0) {
					txt_draft.setText("Drafts ("
							+ uip.getLookbook_draft_count() + ")");
					rel_draft.setClickable(true);
				} else {
					txt_draft.setText("Drafts ("
							+ uip.getLookbook_draft_count() + ")");
					rel_draft.setClickable(false);
				}

				

			} catch (Exception e) {
				// TODO: handle exception
			}

			social_condition();
			feedPojo = new ArrayList<UserFeedPojo>();
			colorlist = new ArrayList<Integer>();
			feedPojo.clear();
			colorlist.clear();
			new localUpload().execute();
			progressBar1.setVisibility(View.GONE);
		}

	}

	/**
	 * 
	 * Feed From Database
	 * 
	 */

	private class localUpload extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void... params) {

			try {

				Cursor c = db.rawQuery(" select * from " + DBTABLE5
						+ " order by type", null);
				
				if (c != null) {
					if (c.getCount() > 0) {
						
					
					while (c.moveToNext()) {

						String mode = c.getString(c.getColumnIndex("mode"));
						String type = c.getString(c.getColumnIndex("type"));

						if (mode.equals("Lookbooks")) {

							try {

								String cards = c.getString(c
										.getColumnIndex("image_count"));

								if (cards.length() >= 3) {

									String username = c.getString(c
											.getColumnIndex("username"));
									String userimg = c.getString(c
											.getColumnIndex("userimg"));
									String lookimg = c.getString(c
											.getColumnIndex("lookimg"));
									String img1 = c.getString(c
											.getColumnIndex("img1"));
									String img2 = c.getString(c
											.getColumnIndex("img2"));
									String likes = c.getString(c
											.getColumnIndex("likes"));
									String hits = c.getString(c
											.getColumnIndex("hits"));
									String title = c.getString(c
											.getColumnIndex("title"));
									String store_name = c.getString(c
											.getColumnIndex("store_name"));
									String store_location = c.getString(c
											.getColumnIndex("store_location"));
									String review = c.getString(c
											.getColumnIndex("review"));
									String idd = c.getString(c
											.getColumnIndex("idd"));
									String description = c.getString(c
											.getColumnIndex("description"));
									String is_new = c.getString(c
											.getColumnIndex("is_new"));
									String is_liked = c.getString(c
											.getColumnIndex("is_liked"));
									String slug = c.getString(c
											.getColumnIndex("slug"));
									String rated_color = c.getString(c
											.getColumnIndex("rated_color"));
									String rated = c.getString(c
											.getColumnIndex("rated"));
									String userid = c.getString(c
											.getColumnIndex("userid"));
									String comment_count = c.getString(c
											.getColumnIndex("comment_count"));
									String img_width = c.getString(c
											.getColumnIndex("img_width"));
									String img_height = c.getString(c
											.getColumnIndex("img_height"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String hits_view = c.getString(c
											.getColumnIndex("hits_view"));
									String view_count = c.getString(c
											.getColumnIndex("hits_count"));
									String store_id = c.getString(c
											.getColumnIndex("store_id"));

									UserFeedPojo pp = new UserFeedPojo(mode,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											review, cards, idd, description,
											is_liked, slug, rated_color, rated,
											is_new, comment_count, img_width,
											img_height, username, userimg,
											userid, hits_view, view_count,
											status, store_id);

									feedPojo.add(pp);
									colorlist.add(Integer.parseInt(color));

								} else if (cards.length() == 2) {

									String username = c.getString(c
											.getColumnIndex("username"));
									String userimg = c.getString(c
											.getColumnIndex("userimg"));
									String lookimg = c.getString(c
											.getColumnIndex("lookimg"));
									String img1 = c.getString(c
											.getColumnIndex("img1"));
									String img2 = c.getString(c
											.getColumnIndex("img2"));
									String likes = c.getString(c
											.getColumnIndex("likes"));
									String hits = c.getString(c
											.getColumnIndex("hits"));
									String title = c.getString(c
											.getColumnIndex("title"));
									String store_name = c.getString(c
											.getColumnIndex("store_name"));
									String store_location = c.getString(c
											.getColumnIndex("store_location"));
									String review = c.getString(c
											.getColumnIndex("review"));
									String idd = c.getString(c
											.getColumnIndex("idd"));
									String description = c.getString(c
											.getColumnIndex("description"));
									String is_new = c.getString(c
											.getColumnIndex("is_new"));
									String is_liked = c.getString(c
											.getColumnIndex("is_liked"));
									String slug = c.getString(c
											.getColumnIndex("slug"));
									String rated_color = c.getString(c
											.getColumnIndex("rated_color"));
									String rated = c.getString(c
											.getColumnIndex("rated"));
									String userid = c.getString(c
											.getColumnIndex("userid"));
									String comment_count = c.getString(c
											.getColumnIndex("comment_count"));
									String img_width = c.getString(c
											.getColumnIndex("img_width"));
									String img_height = c.getString(c
											.getColumnIndex("img_height"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String hits_view = c.getString(c
											.getColumnIndex("hits_view"));
									String view_count = c.getString(c
											.getColumnIndex("hits_count"));
									String store_id = c.getString(c
											.getColumnIndex("store_id"));

									UserFeedPojo pp = new UserFeedPojo(mode,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											review, cards, idd, description,
											is_liked, slug, rated_color, rated,
											is_new, comment_count, img_width,
											img_height, username, userimg,
											userid, hits_view, view_count,
											status, store_id);

									feedPojo.add(pp);
									colorlist.add(Integer.parseInt(color));

								} else {

									String username = c.getString(c
											.getColumnIndex("username"));
									String userimg = c.getString(c
											.getColumnIndex("userimg"));
									String lookimg = c.getString(c
											.getColumnIndex("lookimg"));
									String img1 = c.getString(c
											.getColumnIndex("img1"));
									String img2 = c.getString(c
											.getColumnIndex("img2"));
									String likes = c.getString(c
											.getColumnIndex("likes"));
									String hits = c.getString(c
											.getColumnIndex("hits"));
									String title = c.getString(c
											.getColumnIndex("title"));
									String store_name = c.getString(c
											.getColumnIndex("store_name"));
									String store_location = c.getString(c
											.getColumnIndex("store_location"));
									String review = c.getString(c
											.getColumnIndex("review"));
									String idd = c.getString(c
											.getColumnIndex("idd"));
									String description = c.getString(c
											.getColumnIndex("description"));
									String is_new = c.getString(c
											.getColumnIndex("is_new"));
									String is_liked = c.getString(c
											.getColumnIndex("is_liked"));
									String slug = c.getString(c
											.getColumnIndex("slug"));
									String rated_color = c.getString(c
											.getColumnIndex("rated_color"));
									String rated = c.getString(c
											.getColumnIndex("rated"));
									String userid = c.getString(c
											.getColumnIndex("userid"));
									String comment_count = c.getString(c
											.getColumnIndex("comment_count"));
									String img_width = c.getString(c
											.getColumnIndex("img_width"));
									String img_height = c.getString(c
											.getColumnIndex("img_height"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String hits_view = c.getString(c
											.getColumnIndex("hits_view"));
									String view_count = c.getString(c
											.getColumnIndex("hits_count"));
									String store_id = c.getString(c
											.getColumnIndex("store_id"));

									// Log.e("LOKK_TITLE", title);
									UserFeedPojo pp = new UserFeedPojo(mode,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											review, cards, idd, description,
											is_liked, slug, rated_color, rated,
											is_new, comment_count, img_width,
											img_height, username, userimg,
											userid, hits_view, view_count,
											status, store_id);

									feedPojo.add(pp);
									colorlist.add(Integer.parseInt(color));

									/*
									 * Log.e("LOKK_TITLE_FEED", feedPojo.get(0)
									 * .getTitle());
									 */
									for (int i = 0; i < feedPojo.size(); i++) {
										// Log.e("lookimg",
										// feedPojo.get(i).getLookimg());
									}

								}
							} catch (Exception e) {

							}

						}

						/**
						 * For Articles
						 */
						else if (mode.equals("Articles")) {

							try {

								String cards = c.getString(c
										.getColumnIndex("image_count"));

								if (cards.length() >= 3) {

									String username = c.getString(c
											.getColumnIndex("username"));
									String userimg = c.getString(c
											.getColumnIndex("userimg"));
									String lookimg = c.getString(c
											.getColumnIndex("lookimg"));
									String img1 = c.getString(c
											.getColumnIndex("img1"));
									String img2 = c.getString(c
											.getColumnIndex("img2"));
									String likes = c.getString(c
											.getColumnIndex("likes"));
									String hits = c.getString(c
											.getColumnIndex("hits"));
									String title = c.getString(c
											.getColumnIndex("title"));
									String store_name = c.getString(c
											.getColumnIndex("store_name"));
									String store_location = c.getString(c
											.getColumnIndex("store_location"));
									String review = c.getString(c
											.getColumnIndex("review"));
									String idd = c.getString(c
											.getColumnIndex("idd"));
									String description = c.getString(c
											.getColumnIndex("description"));
									String is_new = c.getString(c
											.getColumnIndex("is_new"));
									String is_liked = c.getString(c
											.getColumnIndex("is_liked"));
									String slug = c.getString(c
											.getColumnIndex("slug"));
									String rated_color = c.getString(c
											.getColumnIndex("rated_color"));
									String rated = c.getString(c
											.getColumnIndex("rated"));
									String userid = c.getString(c
											.getColumnIndex("userid"));
									String comment_count = c.getString(c
											.getColumnIndex("comment_count"));
									String img_width = c.getString(c
											.getColumnIndex("img_width"));
									String img_height = c.getString(c
											.getColumnIndex("img_height"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String hits_view = c.getString(c
											.getColumnIndex("hits_view"));
									String view_count = c.getString(c
											.getColumnIndex("hits_count"));
									String store_id = c.getString(c
											.getColumnIndex("store_id"));

									UserFeedPojo pp = new UserFeedPojo(mode,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											review, cards, idd, description,
											is_liked, slug, rated_color, rated,
											is_new, comment_count, img_width,
											img_height, username, userimg,
											userid, hits_view, view_count,
											status, store_id);

									feedPojo.add(pp);
									colorlist.add(Integer.parseInt(color));

								} else if (cards.length() == 2) {

									String username = c.getString(c
											.getColumnIndex("username"));
									String userimg = c.getString(c
											.getColumnIndex("userimg"));
									String lookimg = c.getString(c
											.getColumnIndex("lookimg"));
									String img1 = c.getString(c
											.getColumnIndex("img1"));
									String img2 = c.getString(c
											.getColumnIndex("img2"));
									String likes = c.getString(c
											.getColumnIndex("likes"));
									String hits = c.getString(c
											.getColumnIndex("hits"));
									String title = c.getString(c
											.getColumnIndex("title"));
									String store_name = c.getString(c
											.getColumnIndex("store_name"));
									String store_location = c.getString(c
											.getColumnIndex("store_location"));
									String review = c.getString(c
											.getColumnIndex("review"));
									String idd = c.getString(c
											.getColumnIndex("idd"));
									String description = c.getString(c
											.getColumnIndex("description"));
									String is_new = c.getString(c
											.getColumnIndex("is_new"));
									String is_liked = c.getString(c
											.getColumnIndex("is_liked"));
									String slug = c.getString(c
											.getColumnIndex("slug"));
									String rated_color = c.getString(c
											.getColumnIndex("rated_color"));
									String rated = c.getString(c
											.getColumnIndex("rated"));
									String userid = c.getString(c
											.getColumnIndex("userid"));
									String comment_count = c.getString(c
											.getColumnIndex("comment_count"));
									String img_width = c.getString(c
											.getColumnIndex("img_width"));
									String img_height = c.getString(c
											.getColumnIndex("img_height"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String hits_view = c.getString(c
											.getColumnIndex("hits_view"));
									String view_count = c.getString(c
											.getColumnIndex("hits_count"));
									String store_id = c.getString(c
											.getColumnIndex("store_id"));

									UserFeedPojo pp = new UserFeedPojo(mode,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											review, cards, idd, description,
											is_liked, slug, rated_color, rated,
											is_new, comment_count, img_width,
											img_height, username, userimg,
											userid, hits_view, view_count,
											status, store_id);

									feedPojo.add(pp);
									colorlist.add(Integer.parseInt(color));

								} else {

									String username = c.getString(c
											.getColumnIndex("username"));
									String userimg = c.getString(c
											.getColumnIndex("userimg"));
									String lookimg = c.getString(c
											.getColumnIndex("lookimg"));
									String img1 = c.getString(c
											.getColumnIndex("img1"));
									String img2 = c.getString(c
											.getColumnIndex("img2"));
									String likes = c.getString(c
											.getColumnIndex("likes"));
									String hits = c.getString(c
											.getColumnIndex("hits"));
									String title = c.getString(c
											.getColumnIndex("title"));
									String store_name = c.getString(c
											.getColumnIndex("store_name"));
									String store_location = c.getString(c
											.getColumnIndex("store_location"));
									String review = c.getString(c
											.getColumnIndex("review"));
									String idd = c.getString(c
											.getColumnIndex("idd"));
									String description = c.getString(c
											.getColumnIndex("description"));
									String is_new = c.getString(c
											.getColumnIndex("is_new"));
									String is_liked = c.getString(c
											.getColumnIndex("is_liked"));
									String slug = c.getString(c
											.getColumnIndex("slug"));
									String rated_color = c.getString(c
											.getColumnIndex("rated_color"));
									String rated = c.getString(c
											.getColumnIndex("rated"));
									String userid = c.getString(c
											.getColumnIndex("userid"));
									String comment_count = c.getString(c
											.getColumnIndex("comment_count"));
									String img_width = c.getString(c
											.getColumnIndex("img_width"));
									String img_height = c.getString(c
											.getColumnIndex("img_height"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String hits_view = c.getString(c
											.getColumnIndex("hits_view"));
									String view_count = c.getString(c
											.getColumnIndex("hits_count"));
									String store_id = c.getString(c
											.getColumnIndex("store_id"));

									UserFeedPojo pp = new UserFeedPojo(mode,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											review, cards, idd, description,
											is_liked, slug, rated_color, rated,
											is_new, comment_count, img_width,
											img_height, username, userimg,
											userid, hits_view, view_count,
											status, store_id);

									feedPojo.add(pp);
									colorlist.add(Integer.parseInt(color));

								}
							} catch (Exception e) {

							}

						}
						/**
						 * For StoreReviews
						 */
						else if (mode.equals("StoreReviews")) {
							String cards = c.getString(c
									.getColumnIndex("image_count"));
							String username = c.getString(c
									.getColumnIndex("username"));
							String userimg = c.getString(c
									.getColumnIndex("userimg"));
							String lookimg = c.getString(c
									.getColumnIndex("lookimg"));
							String img1 = c.getString(c.getColumnIndex("img1"));
							String img2 = c.getString(c.getColumnIndex("img2"));
							String likes = c.getString(c
									.getColumnIndex("likes"));
							String hits = c.getString(c.getColumnIndex("hits"));
							String title = c.getString(c
									.getColumnIndex("title"));
							String store_name = c.getString(c
									.getColumnIndex("store_name"));
							String store_location = c.getString(c
									.getColumnIndex("store_location"));
							String review = c.getString(c
									.getColumnIndex("review"));
							String idd = c.getString(c.getColumnIndex("idd"));
							String description = c.getString(c
									.getColumnIndex("description"));
							String is_new = c.getString(c
									.getColumnIndex("is_new"));
							String is_liked = c.getString(c
									.getColumnIndex("is_liked"));
							String slug = c.getString(c.getColumnIndex("slug"));
							String rated_color = c.getString(c
									.getColumnIndex("rated_color"));
							String rated = c.getString(c
									.getColumnIndex("rated"));
							String userid = c.getString(c
									.getColumnIndex("userid"));
							String comment_count = c.getString(c
									.getColumnIndex("comment_count"));
							String img_width = c.getString(c
									.getColumnIndex("img_width"));
							String img_height = c.getString(c
									.getColumnIndex("img_height"));
							String status = c.getString(c
									.getColumnIndex("status"));
							String color = c.getString(c
									.getColumnIndex("color"));
							String hits_view = c.getString(c
									.getColumnIndex("hits_view"));
							String view_count = c.getString(c
									.getColumnIndex("hits_count"));
							String store_id = c.getString(c
									.getColumnIndex("store_id"));

							// Log.e("REV", review);
							UserFeedPojo pp = new UserFeedPojo(mode, lookimg,
									img1, img2, likes, hits, title, store_name,
									store_location, review, cards, idd,
									description, is_liked, slug, rated_color,
									rated, is_new, comment_count, img_width,
									img_height, username, userimg, userid,
									hits_view, view_count, status, store_id);
							feedPojo.add(pp);
							colorlist.add(Integer.parseInt(color));
							// Log.e("FEEDPOJO", "" + feedPojo.size());
							// Log.e("REV****00", feedPojo.get(0).getReview());
							// Log.e("REV****11", feedPojo.get(1).getReview());

						}

					}
					user_activity.setVisibility(View.VISIBLE);
					} else {
						user_activity.setVisibility(View.GONE);
					}
				}

			} catch (SQLiteException s) {
				s.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter = new UserFeedAdapter(getActivity(), feedPojo, colorlist,
					pro_user_name, pro_user_pic_url);
			endlessListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mHaveMoreDataToLoad = false;
			endlessListView.setVisibility(View.VISIBLE);

		}

	}

	public void user_info() {

		USER_INFO_REST_URL = getString(R.string.base_url) + "users/view/"
				+ user_id + ".json";
		//Log.e("MY PRO", USER_INFO_REST_URL);
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(USER_INFO_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
				progressBar1.setVisibility(View.VISIBLE);
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
					// Log.e("USER_INFo", text2);
					all_info_user(text2);

				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e1) {
				try {
					BufferedReader bufferedReader1 = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									errorResponse)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader1.readLine()) != null) {

						text2 = text2 + text1;
					}
					progressBar1.setVisibility(View.GONE);
				} catch (Exception e) {
					progressBar1.setVisibility(View.GONE);
				}
			}
		});
	}

	public void all_info_user(String info) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(info));
		jsonReader.setLenient(true);
		User main_user1 = gson.fromJson(jsonReader, User.class);
		main_user = main_user1.getUser();
		new UserInfo().execute(main_user);
	}

	private class UserInfo extends AsyncTask<UserDetails, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(UserDetails... params) {

			try {
				DBHelper hp = new DBHelper(getActivity());
				db = hp.getWritableDatabase();	
			} catch (Exception e) {
				// TODO: handle exception
			}
			

			UserDetails main_user = params[0];
			user_age = main_user.getAge();
			user_city = main_user.getLocation();
			user_gender = main_user.getGender();
			user_name = main_user.getFirst_name() + " "
					+ main_user.getLast_name();
			url_img = main_user.getAndroid_api_img();
			zakoopi_points = main_user.getPoints();
			lookbook_count = main_user.getLookbook_active_count();
			like_count = main_user.getPro_likes_count();
			review_count = main_user.getPro_review_count();
			fb_link = main_user.getFb_link();
			twitter_link = main_user.getTwitter_link();
			website_link = main_user.getOther_website();
			lookbook_draft_count = main_user.getLookbook_draft_count();
			uid = main_user.getUid();


			if (user_age == null && user_gender == null && user_city == null) {
				uip = new UserInfoPojo("", "", "", user_name, url_img,
						zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);
			} else if (user_age == null && user_gender == null) {
				uip = new UserInfoPojo("", user_city, "", user_name, url_img,
						zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);
			} else if (user_gender == null && user_city == null) {

				uip = new UserInfoPojo(user_age, "", "", user_name, url_img,
						zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);

			} else if (user_age == null && user_city == null) {

				uip = new UserInfoPojo("", "", user_gender, user_name,
						url_img, zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);
			} else if (user_age == null) {

				uip = new UserInfoPojo("", user_city, user_gender, user_name,
						url_img, zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);
			} else if (user_city == null) {

				uip = new UserInfoPojo(user_age, "", user_gender, user_name,
						url_img, zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);
			} else if (user_gender == null) {

				uip = new UserInfoPojo(user_age, user_city, "", user_name,
						url_img, zakoopi_points, lookbook_count, like_count,
						review_count, fb_link, twitter_link, website_link,
						lookbook_draft_count, uid);
			} else {
				uip = new UserInfoPojo(user_age, user_city, user_gender,
						user_name, url_img, zakoopi_points, lookbook_count,
						like_count, review_count, fb_link, twitter_link,
						website_link, lookbook_draft_count, uid);
			}

			if (user_age == null && user_gender == null && user_city == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, "");
				stm.bindString(4, "");
				stm.bindString(5, "");
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else if (user_age == null && user_gender == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, "");
				stm.bindString(4, "");
				stm.bindString(5, user_city);
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else if (user_age == null && user_city == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, user_gender);
				stm.bindString(4, "");
				stm.bindString(5, "");
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else if (user_gender == null && user_city == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, "");
				stm.bindString(4, user_age);
				stm.bindString(5, "");
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else if (user_age == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, user_gender);
				stm.bindString(4, "");
				stm.bindString(5, user_city);
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else if (user_gender == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, "");
				stm.bindString(4, user_age);
				stm.bindString(5, user_city);
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else if (user_city == null) {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, user_gender);
				stm.bindString(4, user_age);
				stm.bindString(5, "");
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			} else {
				stm.bindString(1, user_name);
				stm.bindString(2, url_img);
				stm.bindString(3, user_gender);
				stm.bindString(4, user_age);
				stm.bindString(5, user_city);
				stm.bindString(6, zakoopi_points);
				stm.bindString(7, lookbook_count);
				stm.bindString(8, like_count);
				stm.bindString(9, review_count);
				stm.bindString(10, fb_link);
				stm.bindString(11, twitter_link);
				stm.bindString(12, website_link);
				stm.bindString(13, lookbook_draft_count);
				stm.bindString(14, uid);
				stm.executeInsert();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				
				user_activity.setText("My Activity");
				txt_user_name.setText(uip.getUser_name());
				txt_user_like_count.setText(uip.getLike_count());
				txt_user_zakoopi_points.setText(uip.getZakoopi_points());

				if (Integer.parseInt(uip.getLookbook_count()) > 0) {
					txt_lookbooks.setText("Lookbooks ("
							+ uip.getLookbook_count() + ")");
					rel_lookbooks.setClickable(true);
				} else {
					txt_lookbooks.setText("Lookbooks ("
							+ uip.getLookbook_count() + ")");
					rel_lookbooks.setClickable(false);
				}

				if (Integer.parseInt(review_count) > 0) {
					txt_reviews.setText("Reviews (" + uip.getReview_count()
							+ ")");
					rel_reviews.setClickable(true);
				} else {
					txt_reviews.setText("Reviews (" + uip.getReview_count()
							+ ")");
					rel_reviews.setClickable(false);
				}

				if (Integer.parseInt(lookbook_draft_count) > 0) {
					txt_draft.setText("Drafts ("
							+ uip.getLookbook_draft_count() + ")");
					rel_draft.setClickable(true);
				} else {
					txt_draft.setText("Drafts ("
							+ uip.getLookbook_draft_count() + ")");
					rel_draft.setClickable(false);
				}

				Picasso.with(getActivity()).load(pro_user_pic_url)
						.placeholder(R.drawable.profile_img_3).resize(67, 67)
						.into(img_profile);
				
				
				if (!uip.getUser_age().equals("")
						&& !uip.getUser_city().equals("")
						&& !uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);
					txt_user_gender.setVisibility(View.VISIBLE);
					txt_user_city.setVisibility(View.VISIBLE);
					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setText(uip.getUser_city());

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}
					v1.setVisibility(View.VISIBLE);
					v2.setVisibility(View.VISIBLE);

					

				} else if (uip.getUser_age().equals("")
						&& uip.getUser_city().equals("")
						&& uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setVisibility(View.GONE);
					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);
				} else if (!uip.getUser_age().equals("")
						&& !uip.getUser_city().equals("")
						&& uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);
					txt_user_gender.setVisibility(View.VISIBLE);
					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setText(uip.getUser_city());
					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.VISIBLE);
					v2.setVisibility(View.GONE);

				} else if (!uip.getUser_age().equals("")
						&& uip.getUser_city().equals("")
						&& !uip.getUser_gender().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);
					txt_user_gender.setVisibility(View.VISIBLE);

					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setVisibility(View.GONE);

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}

					v1.setVisibility(View.VISIBLE);
					v2.setVisibility(View.GONE);

				} else if (uip.getUser_age().equals("")
						&& !uip.getUser_city().equals("")
						&& !uip.getUser_gender().equals("")) {

					txt_user_gender.setVisibility(View.VISIBLE);
					txt_user_city.setVisibility(View.VISIBLE);

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setText(uip.getUser_city());

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.VISIBLE);

				} else if (!uip.getUser_age().equals("")) {

					txt_user_age.setVisibility(View.VISIBLE);

					txt_user_age.setText(uip.getUser_age());
					txt_user_city.setVisibility(View.GONE);

					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);


				} else if (!uip.getUser_city().equals("")) {

					txt_user_city.setVisibility(View.VISIBLE);

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setText(uip.getUser_city());

					txt_user_gender.setVisibility(View.GONE);

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);

				} else if (!uip.getUser_gender().equals("")) {

					txt_user_gender.setVisibility(View.VISIBLE);

					txt_user_age.setVisibility(View.GONE);
					txt_user_city.setVisibility(View.GONE);

					if (uip.getUser_gender().equals("true")) {
						txt_user_gender.setText("Female");
					} else {
						txt_user_gender.setText("Male");
					}

					v1.setVisibility(View.GONE);
					v2.setVisibility(View.GONE);

				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}

			social_condition();

			activity_feed(page);

			progressBar1.setVisibility(View.GONE);
		}

	}

	public void social_condition() {

		try {
			if (uip.getUid().toString().length() < 1
					&& uip.getWebsite_link().equals("")) {
				rel_social.setVisibility(View.GONE);
			} else if ((uip.getUid().toString().length() < 20)
					&& (uip.getUid().toString().length() > 8)
					&& uip.getWebsite_link().equals("")) {
				rel_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.VISIBLE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.GONE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.GONE);
			} else if (uip.getUid().toString().length() > 20
					&& uip.getWebsite_link().equals("")) {
				rel_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.GONE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.VISIBLE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.GONE);
			} else if (uip.getFb_link().equals("")
					&& uip.getTwitter_link().equals("")
					&& !uip.getWebsite_link().equals("")) {
				rel_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.GONE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.GONE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.VISIBLE);
			} else if (uip.getUid().toString().length() > 20
					&& !uip.getWebsite_link().equals("")) {
				rel_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.GONE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.VISIBLE);
				view_twitter.setVisibility(View.VISIBLE);
				img_website.setVisibility(View.VISIBLE);
			} else if ((uip.getUid().toString().length() < 20)
					|| (uip.getUid().toString().length() > 8)
					&& !uip.getWebsite_link().equals("")) {
				rel_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.VISIBLE);
				view_fb.setVisibility(View.VISIBLE);
				img_twitter.setVisibility(View.GONE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.VISIBLE);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void social_click() {

		img_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://facebook.com/" + uid));
				startActivity(i);
				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on facebook")
						.setAction("clicked facebook  by" + pro_user_name)
						.setLabel("Facebook profile of" + user_name).build());
			}
		});

		img_twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://plus.google.com/" + uid));
				startActivity(i);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Twitter")
						.setAction("clicked Twitter  by" + pro_user_name)
						.setLabel("Twitter profile of" + user_name).build());
			}
		});

		img_website.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browseIntent = new Intent("android.intent.action.VIEW",
						Uri.parse(website_link));
				startActivity(browseIntent);
				getActivity().finish();

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Website")
						.setAction("clicked Website  by" + pro_user_name)
						.setLabel("Website of" + user_name).build());
			}
		});

		rel_point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), ZakoopiPoints.class);
				startActivity(i);
			}
		});

		img_edit_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.edt_prof = "edit";
				Intent edt_profile = new Intent(getActivity(),
						EditProfilePage.class);

				startActivityForResult(edt_profile, 0);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on edit profile")
						.setAction("clicked edit profile")
						.setLabel("Login user Profile").build());
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		try {
			String imgurl = data.getStringExtra("img");
			String name = data.getStringExtra("name");
			String loc = data.getStringExtra("loc");
			url_img = imgurl;
			user_name = name;
			user_city = loc;
			txt_user_city.setText(user_city);
			txt_user_name.setText(user_name);
			Picasso.with(getActivity()).load(url_img).resize(67, 67)
					.placeholder(R.drawable.profile_img_3).into(img_profile);
			Variables.edt_profil = "noedit";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @void loadMoreData()
	 */
	private void loadMoreData() {
		page++;
		activity_loadmoreFeed(page);

	}

	/**
	 * EndlessListview LodeMoreListener
	 */
	private EndlessListView.OnLoadMoreListener loadMoreListener = new EndlessListView.OnLoadMoreListener() {

		@Override
		public boolean onLoadMore() {
			if (true == mHaveMoreDataToLoad) {

				loadMoreData();

			} else {

				/*
				 * Toast.makeText(getActivity(), "No more data to load",
				 * Toast.LENGTH_SHORT).show();
				 */
			}
			return mHaveMoreDataToLoad;
		}
	};

	/**
	 * @popular_feed page
	 */
	public void activity_feed(int page) {
		long time = System.currentTimeMillis();
		//Log.e("url", USER_ACTIVITY_REST_URL + page + "&_=" + time);
		client.setBasicAuth(user_email, user_password);

		client.get(USER_ACTIVITY_REST_URL + page + "&_=" + time,
		// client.get("http://v3dev.zakoopi.com/api/Start/getClientLocation.json",
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// called before request is started

						progressBar1.setVisibility(View.VISIBLE);
						endlessListView.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						// called when response HTTP status is "200 OK"

						try {

							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String line = "";
							String text = "";
							while ((line = br.readLine()) != null) {

								text = text + line;
							}
							mHaveMoreDataToLoad = true;
							showData(text);
							// Log.e("Success", "-----" + text);
						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						// Log.e("FAIL", "" + e.getMessage());
						mHaveMoreDataToLoad = false;
						endlessListView.loadMoreCompleat();
						progressBar1.setVisibility(View.GONE);
						// diaog.dismiss();
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}

	/**
	 * @popular_loadmoreFeed page
	 */
	public void activity_loadmoreFeed(int page) {
		long time = System.currentTimeMillis();
		// Log.e("url_MORE", USER_ACTIVITY_REST_URL + page + "&_=" + time);

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(USER_ACTIVITY_REST_URL + page + "&_=" + time,

		// client.get("http://v3dev.zakoopi.com/api/Start/getClientLocation.json",
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// called before request is started
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						// called when response HTTP status is "200 OK"

						try {

							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String st = "";
							String st1 = "";
							while ((st = br.readLine()) != null) {

								st1 = st1 + st;

							}
							mHaveMoreDataToLoad = true;
							// Log.e("jjjjj", st1);
							showmoreData(st1);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						// called when response HTTP status is "4XX" (eg. 401,
						// 403, 404)
						mHaveMoreDataToLoad = false;
						/*
						 * Log.e("ggggg", e.getMessage() + "vvvvvv   " +
						 * statusCode);
						 */
						endlessListView.loadMoreCompleat();
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}

	/**
	 * @void onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		// scroll speed decreases as friction increases. a value of 2 worked
		// well in an emulator; i need to test it on a real device

		// Toast.makeText(getActivity(), "OnStart", Toast.LENGTH_SHORT).show();
		endlessListView.setFriction(ViewConfiguration.getScrollFriction() * 2);
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(
				getActivity());

	}

	/**
	 * Popular Feed Show Data
	 * 
	 * @showData data
	 */
	@SuppressWarnings("unchecked")
	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		UserFeed userFeed = gson.fromJson(reader, UserFeed.class);
		List<feedTimeline> feeds_user = userFeed.getFeedTimeline();
		// Log.e("FEED_USER", "" + feeds_user);
		new MyApp().execute(feeds_user);

	}

	/**
	 * MyApp extends AsyncTask<List<popularfeed>, Void, Void> for Showdata(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp extends AsyncTask<List<feedTimeline>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<feedTimeline>... params) {

			try {
				DBHelper hp = new DBHelper(getActivity());
				db = hp.getWritableDatabase();	
			} catch (Exception e) {
				// TODO: handle exception
			}
			

			feedPojo = new ArrayList<UserFeedPojo>();
			feedPojo.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			if (params[0].size() > 0) {

				for (int i = 0; i < params[0].size(); i++) {
					feedTimeline ft = params[0].get(i);

					if (ft.getModel().equals("Lookbooks")) {

						User_LookbookData look = ft.getLookbooksData();
						/**
						 * For Lookbook
						 */
						if (look != null) {

							try {

								ArrayList<User_lookbook_Cards> cards = look
										.getCards();

								if (cards.size() >= 3) {

									User_lookbook_Cards ccll = cards.get(0);
									User_lookbook_Cards ccll1 = cards.get(1);
									User_lookbook_Cards ccll2 = cards.get(2);
									String lookimg = ccll.getMedium_img();
									String img1 = ccll1.getMedium_img();
									String img2 = ccll2.getMedium_img();
									String medium_img_w = ccll
											.getMedium_img_w();
									String medium_img_h = ccll
											.getMedium_img_h();
									String comment_count = look
											.getLookbookcomment_count();
									String likes = look.getLikes_count();
									String hits = look.getHits_text();
									String view_count = look.getView_count();
									String title = look.getTitle();
									String description = ccll.getDescription();
									String is_liked = look.getIs_liked();
									String idd = look.getId();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Lookbooks", lookimg, img1, img2,
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", "na", comment_count,
											medium_img_w, medium_img_h,
											user_name, url_img, user_id, "na",
											view_count, "na", "na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

									stm_act.bindString(1, "Lookbooks");
									stm_act.bindString(2, "first");
									stm_act.bindString(3, user_name);
									stm_act.bindString(4, url_img);
									stm_act.bindString(5, lookimg);
									stm_act.bindString(6, img1);
									stm_act.bindString(7, img2);
									stm_act.bindString(8, likes);
									stm_act.bindString(9, hits);
									stm_act.bindString(10, title);
									stm_act.bindString(11, "na");
									stm_act.bindString(12, "na");
									stm_act.bindString(13, "na");
									stm_act.bindString(14,
											String.valueOf(cards.size()));
									stm_act.bindString(15, idd);
									stm_act.bindString(16, description);
									stm_act.bindString(17, "na");
									stm_act.bindString(18, is_liked);
									stm_act.bindString(19, slug);
									stm_act.bindString(20, "na");
									stm_act.bindString(21, "na");
									stm_act.bindString(22, user_id);
									stm_act.bindString(23, comment_count);
									stm_act.bindString(24, medium_img_w);
									stm_act.bindString(25, medium_img_h);
									stm_act.bindString(26, "na");
									stm_act.bindString(27,
											String.valueOf(colors[rnd]));

									stm_act.bindString(28, "na");
									stm_act.bindString(29, view_count);
									stm_act.bindString(30, "na");
									stm_act.executeInsert();

								} else if (cards.size() == 2) {

									User_lookbook_Cards ccll = cards.get(0);
									User_lookbook_Cards ccll1 = cards.get(1);
									String lookimg = ccll.getLarge_img();
									String img1 = ccll1.getMedium_img();
									String medium_img_w = ccll
											.getMedium_img_w();
									String medium_img_h = ccll
											.getMedium_img_h();
									String comment_count = look
											.getLookbookcomment_count();
									String likes = look.getLikes_count();
									String hits = look.getHits_text();
									String view_count = look.getView_count();
									String title = look.getTitle();
									String description = ccll.getDescription();
									String idd = look.getId();
									String is_liked = look.getIs_liked();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Lookbooks", lookimg, img1, "na",
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", "na", comment_count,
											medium_img_w, medium_img_h,
											user_name, url_img, user_id, "na",
											view_count, "na", "na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

									stm_act.bindString(1, "Lookbooks");
									stm_act.bindString(2, "first");
									stm_act.bindString(3, user_name);
									stm_act.bindString(4, url_img);
									stm_act.bindString(5, lookimg);
									stm_act.bindString(6, img1);
									stm_act.bindString(7, "na");
									stm_act.bindString(8, likes);
									stm_act.bindString(9, hits);
									stm_act.bindString(10, title);
									stm_act.bindString(11, "na");
									stm_act.bindString(12, "na");
									stm_act.bindString(13, "na");
									stm_act.bindString(14,
											String.valueOf(cards.size()));
									stm_act.bindString(15, idd);
									stm_act.bindString(16, description);
									stm_act.bindString(17, "na");
									stm_act.bindString(18, is_liked);
									stm_act.bindString(19, slug);
									stm_act.bindString(20, "na");
									stm_act.bindString(21, "na");
									stm_act.bindString(22, user_id);
									stm_act.bindString(23, comment_count);
									stm_act.bindString(24, medium_img_w);
									stm_act.bindString(25, medium_img_h);
									stm_act.bindString(26, "na");
									stm_act.bindString(27,
											String.valueOf(colors[rnd]));
									stm_act.bindString(28, "na");
									stm_act.bindString(29, view_count);
									stm_act.bindString(30, "na");
									stm_act.executeInsert();

								} else {

									User_lookbook_Cards ccll = cards.get(0);
									String lookimg = ccll.getLarge_img();
									String medium_img_w = ccll
											.getMedium_img_w();
									String medium_img_h = ccll
											.getMedium_img_h();
									String comment_count = look
											.getLookbookcomment_count();
									String likes = look.getLikes_count();
									String hits = look.getHits_text();
									String view_count = look.getView_count();
									String title = look.getTitle();
									String description = ccll.getDescription();
									String idd = look.getId();
									String is_liked = look.getIs_liked();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Lookbooks", lookimg, "na", "na",
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", "na", comment_count,
											medium_img_w, medium_img_h,
											user_name, url_img, user_id, "na",
											view_count, "na", "na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

									stm_act.bindString(1, "Lookbooks");
									stm_act.bindString(2, "first");
									stm_act.bindString(3, user_name);
									stm_act.bindString(4, url_img);
									stm_act.bindString(5, lookimg);
									stm_act.bindString(6, "na");
									stm_act.bindString(7, "na");
									stm_act.bindString(8, likes);
									stm_act.bindString(9, hits);
									stm_act.bindString(10, title);
									stm_act.bindString(11, "na");
									stm_act.bindString(12, "na");
									stm_act.bindString(13, "na");
									stm_act.bindString(14,
											String.valueOf(cards.size()));
									stm_act.bindString(15, idd);
									stm_act.bindString(16, description);
									stm_act.bindString(17, "na");
									stm_act.bindString(18, is_liked);
									stm_act.bindString(19, slug);
									stm_act.bindString(20, "na");
									stm_act.bindString(21, "na");
									stm_act.bindString(22, user_id);
									stm_act.bindString(23, comment_count);
									stm_act.bindString(24, medium_img_w);
									stm_act.bindString(25, medium_img_h);
									stm_act.bindString(26, "na");
									stm_act.bindString(27,
											String.valueOf(colors[rnd]));
									stm_act.bindString(28, "na");
									stm_act.bindString(29, view_count);
									stm_act.bindString(30, "na");
									stm_act.executeInsert();

								}
							} catch (Exception e) {

							}

						}
					}
					/**
					 * For Articles
					 */
					else if (ft.getModel().equals("Articles")) {

						UserArticleData look = ft.getArticlesData();
						if (look != null) {
							try {

								ArrayList<UserArticleImages> cards = look
										.getArticle_images();

								if (cards.size() >= 3) {

									UserArticleImages ccll = cards.get(0);
									UserArticleImages ccll1 = cards.get(1);
									UserArticleImages ccll2 = cards.get(2);
									String lookimg = ccll.getLarge_img();
									String img1 = ccll1.getMedium_img();
									String img2 = ccll2.getMedium_img();
									String medium_img_w = ccll
											.getMedium_img_w();
									String medium_img_h = ccll
											.getMedium_img_h();
									String comment_count = look
											.getArticle_comment_count();
									String likes = look.getLikes_count();
									String hits = look.getHits_text();
									String hits_count = look.getHits();
									String title = look.getTitle();
									String description = look.getDescription();
									String is_new = look.getIs_new();
									String idd = look.getId();
									String is_liked = look.getIs_liked();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Articles", lookimg, img1, img2,
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", is_new, comment_count,
											medium_img_w, medium_img_h,
											user_name, url_img, user_id,
											hits_count, "na", "na", "na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

									stm_act.bindString(1, "Articles");
									stm_act.bindString(2, "first");
									stm_act.bindString(3, user_name);
									stm_act.bindString(4, url_img);
									stm_act.bindString(5, lookimg);
									stm_act.bindString(6, img1);
									stm_act.bindString(7, img2);
									stm_act.bindString(8, likes);
									stm_act.bindString(9, hits);
									stm_act.bindString(10, title);
									stm_act.bindString(11, "na");
									stm_act.bindString(12, "na");
									stm_act.bindString(13, "na");
									stm_act.bindString(14,
											String.valueOf(cards.size()));
									stm_act.bindString(15, idd);
									stm_act.bindString(16, description);
									stm_act.bindString(17, is_new);
									stm_act.bindString(18, is_liked);
									stm_act.bindString(19, slug);
									stm_act.bindString(20, "na");
									stm_act.bindString(21, "na");
									stm_act.bindString(22, user_id);
									stm_act.bindString(23, comment_count);
									stm_act.bindString(24, medium_img_w);
									stm_act.bindString(25, medium_img_h);
									stm_act.bindString(26, "na");
									stm_act.bindString(27,
											String.valueOf(colors[rnd]));
									stm_act.bindString(28, hits_count);
									stm_act.bindString(29, "na");
									stm_act.bindString(30, "na");
									stm_act.executeInsert();

								} else if (cards.size() == 2) {

									UserArticleImages ccll = cards.get(0);
									UserArticleImages ccll1 = cards.get(1);

									String lookimg = ccll.getLarge_img();
									String img1 = ccll1.getMedium_img();
									String medium_img_w = ccll
											.getMedium_img_w();
									String medium_img_h = ccll
											.getMedium_img_h();
									String comment_count = look
											.getArticle_comment_count();
									String likes = look.getLikes_count();
									String hits = look.getHits_text();
									String hits_count = look.getHits();
									String title = look.getTitle();
									String description = look.getDescription();
									String is_new = look.getIs_new();
									String is_liked = look.getIs_liked();
									String idd = look.getId();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Articles", lookimg, img1, "na",
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", is_new, comment_count,
											medium_img_w, medium_img_h,
											user_name, url_img, user_id,
											hits_count, "na", "na", "na");
									feedPojo.add(pp);
									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

									stm_act.bindString(1, "Articles");
									stm_act.bindString(2, "first");
									stm_act.bindString(3, user_name);
									stm_act.bindString(4, url_img);
									stm_act.bindString(5, lookimg);
									stm_act.bindString(6, img1);
									stm_act.bindString(7, "na");
									stm_act.bindString(8, likes);
									stm_act.bindString(9, hits);
									stm_act.bindString(10, title);
									stm_act.bindString(11, "na");
									stm_act.bindString(12, "na");
									stm_act.bindString(13, "na");
									stm_act.bindString(14,
											String.valueOf(cards.size()));
									stm_act.bindString(15, idd);
									stm_act.bindString(16, description);
									stm_act.bindString(17, is_new);
									stm_act.bindString(18, is_liked);
									stm_act.bindString(19, slug);
									stm_act.bindString(20, "na");
									stm_act.bindString(21, "na");
									stm_act.bindString(22, user_id);
									stm_act.bindString(23, comment_count);
									stm_act.bindString(24, medium_img_w);
									stm_act.bindString(25, medium_img_h);
									stm_act.bindString(26, "na");
									stm_act.bindString(27,
											String.valueOf(colors[rnd]));
									stm_act.bindString(28, hits_count);
									stm_act.bindString(29, "na");
									stm_act.bindString(30, "na");
									stm_act.executeInsert();

								} else {

									UserArticleImages ccll = cards.get(0);
									String lookimg = ccll.getLarge_img();
									String medium_img_w = ccll
											.getMedium_img_w();
									String medium_img_h = ccll
											.getMedium_img_h();
									String comment_count = look
											.getArticle_comment_count();
									String likes = look.getLikes_count();
									String hits = look.getHits_text();
									String hits_count = look.getHits();
									String title = look.getTitle();
									String description = look.getDescription();
									String is_new = look.getIs_new();
									String idd = look.getId();
									String is_liked = look.getIs_liked();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Articles", lookimg, "na", "na",
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", is_new, comment_count,
											medium_img_w, medium_img_h,
											user_name, url_img, user_id,
											hits_count, "na", "na", "na");
									feedPojo.add(pp);
									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

									stm_act.bindString(1, "Articles");
									stm_act.bindString(2, "first");
									stm_act.bindString(3, user_name);
									stm_act.bindString(4, url_img);
									stm_act.bindString(5, lookimg);
									stm_act.bindString(6, "na");
									stm_act.bindString(7, "na");
									stm_act.bindString(8, likes);
									stm_act.bindString(9, hits);
									stm_act.bindString(10, title);
									stm_act.bindString(11, "na");
									stm_act.bindString(12, "na");
									stm_act.bindString(13, "na");
									stm_act.bindString(14,
											String.valueOf(cards.size()));
									stm_act.bindString(15, idd);
									stm_act.bindString(16, description);
									stm_act.bindString(17, is_new);
									stm_act.bindString(18, is_liked);
									stm_act.bindString(19, slug);
									stm_act.bindString(20, "na");
									stm_act.bindString(21, "na");
									stm_act.bindString(22, user_id);
									stm_act.bindString(23, comment_count);
									stm_act.bindString(24, medium_img_w);
									stm_act.bindString(25, medium_img_h);
									stm_act.bindString(26, "na");
									stm_act.bindString(27,
											String.valueOf(colors[rnd]));
									stm_act.bindString(28, hits_count);
									stm_act.bindString(29, "na");
									stm_act.bindString(30, "na");
									stm_act.executeInsert();

								}
							} catch (Exception e) {

							}
						}
					}
					/**
					 * For StoreReviews
					 */
					else if (ft.getModel().equals("StoreReviews")) {

						UserStoreReviewData store = ft.getStoreReviewsData();
						if (store != null) {
							try {

								UserStoreData likes = store.getStore();

								String likes1 = store.getLikes_count();
								String hits = store.getHits_text();
								String hits_count = store.getHits();
								String review = store.getReview();
								String store_id = store.getStore_id();
								String store_name = likes.getStore_name();
								String store_location = likes.getMarket();
								String idd = store.getId();
								String is_liked = store.getIs_liked();
								String slug = likes.getSlug();

								String rated_color = store.getRated_color();
								String rated = store.getRated();
								UserFeedPojo pp = new UserFeedPojo(
										"StoreReviews", "na", "na", "na",
										likes1, hits, "na", store_name,
										store_location, review, "na", idd,
										"na", is_liked, slug, rated_color,
										rated, "na", "na", "na", "na",
										user_name, url_img, user_id,
										hits_count, "na", "na", store_id);
								feedPojo.add(pp);
								colorlist.add(R.color.tabscolor);

								stm_act.bindString(1, "StoreReviews");
								stm_act.bindString(2, "first");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, "na");
								stm_act.bindString(6, "na");
								stm_act.bindString(7, "na");
								stm_act.bindString(8, likes1);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, "na");
								stm_act.bindString(11, store_name);
								stm_act.bindString(12, store_location);
								stm_act.bindString(13, review);
								stm_act.bindString(14, "na");
								stm_act.bindString(15, idd);
								stm_act.bindString(16, "na");
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, rated_color);
								stm_act.bindString(21, rated);
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, "na");
								stm_act.bindString(24, "na");
								stm_act.bindString(25, "na");
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(R.color.tabscolor));
								stm_act.bindString(28, hits_count);
								stm_act.bindString(29, "na");
								stm_act.bindString(30, store_id);
								stm_act.executeInsert();

							} catch (Exception e) {
							}
						}
					}

				}
				user_activity.setVisibility(View.VISIBLE);
			} else {
				user_activity.setVisibility(View.GONE);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter = new UserFeedAdapter(getActivity(), feedPojo, colorlist,
					pro_user_name, pro_user_pic_url);
			endlessListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			endlessListView.loadMoreCompleat();
			progressBar1.setVisibility(View.GONE);
			// diaog.dismiss();
			endlessListView.setVisibility(View.VISIBLE);

		}

	}

	/**
	 * Popular More Feed Show More Data
	 * 
	 * @showmoreData data
	 */
	@SuppressWarnings("unchecked")
	public void showmoreData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		UserFeed userFeed = gson.fromJson(reader, UserFeed.class);
		List<feedTimeline> feeds_user = userFeed.getFeedTimeline();
		// Log.e("FEED_USER", "" + feeds_user);
		new MyApp1().execute(feeds_user);
	}

	/**
	 * MyApp1 extends AsyncTask<List<popularfeed>, Void, Void> for
	 * showmoreData(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp1 extends AsyncTask<List<feedTimeline>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<feedTimeline>... params) {

			feedPojo = new ArrayList<UserFeedPojo>();
			feedPojo.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < params[0].size(); i++) {
				feedTimeline ft = params[0].get(i);

				if (ft.getModel().equals("Lookbooks")) {

					User_LookbookData look = ft.getLookbooksData();
					/**
					 * For Lookbook
					 */
					if (look != null) {

						try {

							ArrayList<User_lookbook_Cards> cards = look
									.getCards();

							if (cards.size() >= 3) {

								User_lookbook_Cards ccll = cards.get(0);
								User_lookbook_Cards ccll1 = cards.get(1);
								User_lookbook_Cards ccll2 = cards.get(2);
								String lookimg = ccll.getLarge_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								String medium_img_w = ccll.getMedium_img_w();
								String medium_img_h = ccll.getMedium_img_h();
								String comment_count = look
										.getLookbookcomment_count();
								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String is_liked = look.getIs_liked();
								String idd = look.getId();
								String slug = look.getSlug();
								UserFeedPojo pp = new UserFeedPojo("Lookbooks",
										lookimg, img1, img2, likes, hits,
										title, "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_liked, slug, "na",
										"na", "na", comment_count,
										medium_img_w, medium_img_h, user_name,
										url_img, user_id, "na", view_count,
										"na", "na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								stm_act.bindString(1, "Lookbooks");
								stm_act.bindString(2, "second");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, lookimg);
								stm_act.bindString(6, img1);
								stm_act.bindString(7, img2);
								stm_act.bindString(8, likes);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, title);
								stm_act.bindString(11, "na");
								stm_act.bindString(12, "na");
								stm_act.bindString(13, "na");
								stm_act.bindString(14,
										String.valueOf(cards.size()));
								stm_act.bindString(15, idd);
								stm_act.bindString(16, description);
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, "na");
								stm_act.bindString(21, "na");
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, comment_count);
								stm_act.bindString(24, medium_img_w);
								stm_act.bindString(25, medium_img_h);
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(colors[rnd]));
								stm_act.bindString(28, "na");
								stm_act.bindString(29, view_count);
								stm_act.bindString(30, "na");
								stm_act.executeInsert();

							} else if (cards.size() == 2) {

								User_lookbook_Cards ccll = cards.get(0);
								User_lookbook_Cards ccll1 = cards.get(1);

								String lookimg = ccll.getLarge_img();
								String img1 = ccll1.getMedium_img();
								String medium_img_w = ccll.getMedium_img_w();
								String medium_img_h = ccll.getMedium_img_h();
								String comment_count = look
										.getLookbookcomment_count();
								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								UserFeedPojo pp = new UserFeedPojo("Lookbooks",
										lookimg, img1, "na", likes, hits,
										title, "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_liked, slug, "na",
										"na", "na", comment_count,
										medium_img_w, medium_img_h, user_name,
										url_img, user_id, "na", view_count,
										"na", "na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								stm_act.bindString(1, "Lookbooks");
								stm_act.bindString(2, "second");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, lookimg);
								stm_act.bindString(6, img1);
								stm_act.bindString(7, "na");
								stm_act.bindString(8, likes);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, title);
								stm_act.bindString(11, "na");
								stm_act.bindString(12, "na");
								stm_act.bindString(13, "na");
								stm_act.bindString(14,
										String.valueOf(cards.size()));
								stm_act.bindString(15, idd);
								stm_act.bindString(16, description);
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, "na");
								stm_act.bindString(21, "na");
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, comment_count);
								stm_act.bindString(24, medium_img_w);
								stm_act.bindString(25, medium_img_h);
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(colors[rnd]));
								stm_act.bindString(28, "na");
								stm_act.bindString(29, view_count);
								stm_act.bindString(30, "na");
								stm_act.executeInsert();

							} else {

								User_lookbook_Cards ccll = cards.get(0);

								String lookimg = ccll.getLarge_img();
								String medium_img_w = ccll.getMedium_img_w();
								String medium_img_h = ccll.getMedium_img_h();
								String comment_count = look
										.getLookbookcomment_count();
								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								UserFeedPojo pp = new UserFeedPojo("Lookbooks",
										lookimg, "na", "na", likes, hits,
										title, "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_liked, slug, "na",
										"na", "na", comment_count,
										medium_img_w, medium_img_h, user_name,
										url_img, user_id, "na", view_count,
										"na", "na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								stm_act.bindString(1, "Lookbooks");
								stm_act.bindString(2, "second");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, lookimg);
								stm_act.bindString(6, "na");
								stm_act.bindString(7, "na");
								stm_act.bindString(8, likes);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, title);
								stm_act.bindString(11, "na");
								stm_act.bindString(12, "na");
								stm_act.bindString(13, "na");
								stm_act.bindString(14,
										String.valueOf(cards.size()));
								stm_act.bindString(15, idd);
								stm_act.bindString(16, description);
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, "na");
								stm_act.bindString(21, "na");
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, comment_count);
								stm_act.bindString(24, medium_img_w);
								stm_act.bindString(25, medium_img_h);
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(colors[rnd]));
								stm_act.bindString(28, "na");
								stm_act.bindString(29, view_count);
								stm_act.bindString(30, "na");
								stm_act.executeInsert();
							}
						} catch (Exception e) {

						}

					}
				}
				/**
				 * For Articles
				 */
				else if (ft.getModel().equals("Articles")) {

					UserArticleData look = ft.getArticlesData();
					if (look != null) {
						try {

							ArrayList<UserArticleImages> cards = look
									.getArticle_images();

							if (cards.size() >= 3) {

								UserArticleImages ccll = cards.get(0);
								UserArticleImages ccll1 = cards.get(1);
								UserArticleImages ccll2 = cards.get(2);
								String lookimg = ccll.getLarge_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								String medium_img_w = ccll.getMedium_img_w();
								String medium_img_h = ccll.getMedium_img_h();
								String comment_count = look
										.getArticle_comment_count();
								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String hits_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIs_new();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								UserFeedPojo pp = new UserFeedPojo("Articles",
										lookimg, img1, img2, likes, hits,
										title, "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_liked, slug, "na",
										"na", is_new, comment_count,
										medium_img_w, medium_img_h, user_name,
										url_img, user_id, hits_count, "na",
										"na", "na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								stm_act.bindString(1, "Articles");
								stm_act.bindString(2, "second");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, lookimg);
								stm_act.bindString(6, img1);
								stm_act.bindString(7, img2);
								stm_act.bindString(8, likes);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, title);
								stm_act.bindString(11, "na");
								stm_act.bindString(12, "na");
								stm_act.bindString(13, "na");
								stm_act.bindString(14,
										String.valueOf(cards.size()));
								stm_act.bindString(15, idd);
								stm_act.bindString(16, description);
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, "na");
								stm_act.bindString(21, "na");
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, comment_count);
								stm_act.bindString(24, medium_img_w);
								stm_act.bindString(25, medium_img_h);
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(colors[rnd]));
								stm_act.bindString(28, hits_count);
								stm_act.bindString(29, "na");
								stm_act.bindString(30, "na");
								stm_act.executeInsert();

							} else if (cards.size() == 2) {

								UserArticleImages ccll = cards.get(0);
								UserArticleImages ccll1 = cards.get(1);
								String lookimg = ccll.getLarge_img();
								String img1 = ccll1.getMedium_img();
								String medium_img_w = ccll.getMedium_img_w();
								String medium_img_h = ccll.getMedium_img_h();
								String comment_count = look
										.getArticle_comment_count();
								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String hits_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIs_new();
								String is_liked = look.getIs_liked();
								String idd = look.getId();
								String slug = look.getSlug();
								UserFeedPojo pp = new UserFeedPojo("Articles",
										lookimg, img1, "na", likes, hits,
										title, "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_liked, slug, "na",
										"na", is_new, comment_count,
										medium_img_w, medium_img_h, user_name,
										url_img, user_id, hits_count, "na",
										"na", "na");
								feedPojo.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								stm_act.bindString(1, "Articles");
								stm_act.bindString(2, "second");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, lookimg);
								stm_act.bindString(6, img1);
								stm_act.bindString(7, "na");
								stm_act.bindString(8, likes);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, title);
								stm_act.bindString(11, "na");
								stm_act.bindString(12, "na");
								stm_act.bindString(13, "na");
								stm_act.bindString(14,
										String.valueOf(cards.size()));
								stm_act.bindString(15, idd);
								stm_act.bindString(16, description);
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, "na");
								stm_act.bindString(21, "na");
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, comment_count);
								stm_act.bindString(24, medium_img_w);
								stm_act.bindString(25, medium_img_h);
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(colors[rnd]));
								stm_act.bindString(28, hits_count);
								stm_act.bindString(29, "na");
								stm_act.bindString(30, "na");
								stm_act.executeInsert();

							} else {

								UserArticleImages ccll = cards.get(0);
								String lookimg = ccll.getLarge_img();
								String medium_img_w = ccll.getMedium_img_w();
								String medium_img_h = ccll.getMedium_img_h();
								String comment_count = look
										.getArticle_comment_count();
								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String hits_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIs_new();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								UserFeedPojo pp = new UserFeedPojo("Articles",
										lookimg, "na", "na", likes, hits,
										title, "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_liked, slug, "na",
										"na", is_new, comment_count,
										medium_img_w, medium_img_h, user_name,
										url_img, user_id, hits_count, "na",
										"na", "na");
								feedPojo.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								stm_act.bindString(1, "Articles");
								stm_act.bindString(2, "second");
								stm_act.bindString(3, user_name);
								stm_act.bindString(4, url_img);
								stm_act.bindString(5, lookimg);
								stm_act.bindString(6, "na");
								stm_act.bindString(7, "na");
								stm_act.bindString(8, likes);
								stm_act.bindString(9, hits);
								stm_act.bindString(10, title);
								stm_act.bindString(11, "na");
								stm_act.bindString(12, "na");
								stm_act.bindString(13, "na");
								stm_act.bindString(14,
										String.valueOf(cards.size()));
								stm_act.bindString(15, idd);
								stm_act.bindString(16, description);
								stm_act.bindString(17, "na");
								stm_act.bindString(18, is_liked);
								stm_act.bindString(19, slug);
								stm_act.bindString(20, "na");
								stm_act.bindString(21, "na");
								stm_act.bindString(22, user_id);
								stm_act.bindString(23, comment_count);
								stm_act.bindString(24, medium_img_w);
								stm_act.bindString(25, medium_img_h);
								stm_act.bindString(26, "na");
								stm_act.bindString(27,
										String.valueOf(colors[rnd]));
								stm_act.bindString(28, hits_count);
								stm_act.bindString(29, "na");
								stm_act.bindString(30, "na");
								stm_act.executeInsert();
							}
						} catch (Exception e) {

						}
					}
				}
				/**
				 * For StoreReviews
				 */
				else if (ft.getModel().equals("StoreReviews")) {

					UserStoreReviewData store = ft.getStoreReviewsData();
					if (store != null) {
						try {

							UserStoreData likes = store.getStore();

							String likes1 = store.getLikes_count();
							String hits = store.getHits_text();
							String hits_count = store.getHits();
							String review = store.getReview();
							String store_id = store.getStore_id();
							String store_name = likes.getStore_name();
							String store_location = likes.getMarket();
							String idd = store.getId();
							String is_liked = store.getIs_liked();
							String slug = likes.getSlug();
							String rated_color = store.getRated_color();
							String rated = store.getRated();
							UserFeedPojo pp = new UserFeedPojo("StoreReviews",
									"na", "na", "na", likes1, hits, "na",
									store_name, store_location, review, "na",
									idd, "na", is_liked, slug, rated_color,
									rated, "na", "na", "na", "na", user_name,
									url_img, user_id, hits_count, "na", "na",
									store_id);
							feedPojo.add(pp);
							colorlist.add(R.color.tabscolor);

							stm_act.bindString(1, "StoreReviews");
							stm_act.bindString(2, "second");
							stm_act.bindString(3, user_name);
							stm_act.bindString(4, url_img);
							stm_act.bindString(5, "na");
							stm_act.bindString(6, "na");
							stm_act.bindString(7, "na");
							stm_act.bindString(8, likes1);
							stm_act.bindString(9, hits);
							stm_act.bindString(10, "na");
							stm_act.bindString(11, store_name);
							stm_act.bindString(12, store_location);
							stm_act.bindString(13, review);
							stm_act.bindString(14, "na");
							stm_act.bindString(15, idd);
							stm_act.bindString(16, "na");
							stm_act.bindString(17, "na");
							stm_act.bindString(18, is_liked);
							stm_act.bindString(19, slug);
							stm_act.bindString(20, rated_color);
							stm_act.bindString(21, rated);
							stm_act.bindString(22, user_id);
							stm_act.bindString(23, "na");
							stm_act.bindString(24, "na");
							stm_act.bindString(25, "na");
							stm_act.bindString(26, "na");
							stm_act.bindString(27,
									String.valueOf(R.color.tabscolor));
							stm_act.bindString(28, hits_count);
							stm_act.bindString(29, "na");
							stm_act.bindString(30, store_id);
							stm_act.executeInsert();

						} catch (Exception e) {
						}
					}
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter.addItems(feedPojo);
			adapter.addColors(colorlist);
			endlessListView.loadMoreCompleat();

			endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			endlessListView.setVerticalScrollBarEnabled(false);

		}

	}

	public void loadDialog() {

		diaog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		diaog.setCancelable(false);
		diaog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		diaog.setCancelable(false);
		diaog.getWindow().setGravity(Gravity.FILL_VERTICAL);
		diaog.setContentView(R.layout.load_dialog);
		// bar = (MaterialProgressBar) diaog.findViewById(R.id.progressBar1);
		// bar.setColorSchemeResources(R.color.red, R.color.green, R.color.blue,
		// R.color.orange);
		diaog.show();

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

						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
