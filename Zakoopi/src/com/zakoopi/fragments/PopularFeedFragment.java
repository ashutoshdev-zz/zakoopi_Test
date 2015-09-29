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

import android.annotation.SuppressLint;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cam.imagedatabase.DBHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zakoopi.R;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.POJO;
import com.zakoopi.homefeed.Popular;
import com.zakoopi.homefeed.Popular_ArticleData;
import com.zakoopi.homefeed.Popular_Article_Images;
import com.zakoopi.homefeed.Popular_Article_User;
import com.zakoopi.homefeed.Popular_Lookbook_Cards;
import com.zakoopi.homefeed.Popular_Lookbook_User;
import com.zakoopi.homefeed.Popular_Lookbookdata;
import com.zakoopi.homefeed.Popular_StoreReviewData;
import com.zakoopi.homefeed.Popular_StoreReview_Store;
import com.zakoopi.homefeed.Popular_StoreReview_Users;
import com.zakoopi.homefeed.Popular_Teamsdata;
import com.zakoopi.homefeed.Recent;
import com.zakoopi.homefeed.Recent_ArticleData;
import com.zakoopi.homefeed.Recent_Article_Images;
import com.zakoopi.homefeed.Recent_Article_User;
import com.zakoopi.homefeed.Recent_Lookbook_Cards;
import com.zakoopi.homefeed.Recent_Lookbook_User;
import com.zakoopi.homefeed.Recent_Lookbookdata;
import com.zakoopi.homefeed.Recent_StoreReviewData;
import com.zakoopi.homefeed.Recent_StoreReview_Store;
import com.zakoopi.homefeed.Recent_StoreReview_Users;
import com.zakoopi.homefeed.Recent_Teamsdata;
import com.zakoopi.homefeed.popularfeed;
import com.zakoopi.homefeed.recentfeed;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.PopularAdapter1;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class PopularFeedFragment extends Fragment {

	private PopularAdapter1 adapter;
	private EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	// private static final String POPULAR_REST_URL =
	// "http://v3.zakoopi.com/api/feedPopular.json?page=";
	private static String POPULAR_REST_URL = " ";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	public int page = 1;
	private SharedPreferences pro_user_pref, pref_location;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	Fragment popularFrag, recentFrag;
	LinearLayout popular_linear, recent_linear;
	View include_view;
	ArrayList<POJO> pojolist = null;
	Typeface typeface_semibold;
	// RelativeLayout rel_pop, rel_recent;
	// TextView txt_popular, txt_recent, txt_popular1, txt_recent1;
	// View view_popular, view_recent, view_popular1, view_recent1;
	// View header_view;
	private CountDownTimer countDownTimer;
	private final long startTime = 60 * 1000;
	private final long interval = 1 * 1000;
	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	private String city_name;
	ImageView float_new_stories;
	ProgressBar progressBar1, progressBar2;
	public static String statuscode = "202";
	static int pageRecent = 0;
	String username, userimg;

	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	private SQLiteDatabase db;
	public static final String DBTABLE2 = "HomeFeed";
	private SQLiteStatement stm;
	String data;
	boolean bool = false;
	int database_id = 0;
	int next = 2;
	int ppp = 1;
	private BroadcastReceiver br;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/**
		 * StrictMode for smooth list scroll
		 */

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		View view = inflater.inflate(R.layout.popular_feed_home, null);
		
		try {
			DBHelper hp = new DBHelper(getActivity());
			db = hp.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		stm = db.compileStatement("insert into  "
				+ DBTABLE2
				+ " (mode,type,username,userimg,lookimg,img1,img2,"
				+ "likes,hits, title,store_name,store_location,store_rate,review,"
				+ "image_count,idd,description, is_new,is_liked,slug,"
				+ "rated_color,rated,userid,comment_count,img_width,img_height,"
				+ "feed_id,status,color,hits_count,store_id) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		cd = new ConnectionDetector(getActivity());
		
		

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Featured Feed");
		t.send(new HitBuilders.AppViewBuilder().build());

		pref_location = getActivity().getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "123");

		client = ClientHttp.getInstance(getActivity());
		// Log.e("NAME_CITY", city_name);

		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");

		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		include_view = (View) view.findViewById(R.id.previewLayout);
		popular_linear = (LinearLayout) include_view
				.findViewById(R.id.lin_popular);
		recent_linear = (LinearLayout) include_view
				.findViewById(R.id.lin_recent);

		POPULAR_REST_URL = getString(R.string.base_url)
				+ "feedFeatured.json?page=";

	
		float_new_stories = (ImageView) view
				.findViewById(R.id.float_new_button);
		

		mHaveMoreDataToLoad = true;
		endlessListView = (EndlessListView) view
				.findViewById(R.id.endlessListView);
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);
		progressBar1.setVisibility(View.VISIBLE);
		endlessListView.setOnLoadMoreListener(loadMoreListener);

		// header_click();

		float_new_stories.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {

					if (statuscode.equals("202")) {
						float_new_stories.setVisibility(View.GONE);
						progressBar2.setVisibility(View.VISIBLE);
						// new change
						endlessListView.smoothScrollToPosition(0, 0);
						countDownTimer.start();
						page++;
						popular_loadmoreFeed(page);

					} else {
						float_new_stories.setVisibility(View.GONE);
						progressBar2.setVisibility(View.GONE);
						countDownTimer.cancel();
					}
				} else {
					/*Toast.makeText(getActivity(),
							"Please Enable Internet Connection...",
							Toast.LENGTH_SHORT).show();*/
				}

			}
		});
		return view;
	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			float_new_stories.setVisibility(View.VISIBLE);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			float_new_stories.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			//countDownTimer = new MyCountDownTimer(startTime, interval);
			//countDownTimer.start();
		}
		
		checkInternetConnection();

		

		super.onResume();
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
					/*Log.d("TEST Internet",
							info.toString() + " " + state.toString());*/

					if (state == State.CONNECTED) {

					/*	Toast.makeText(getActivity(),
								"Internet connection is on", Toast.LENGTH_LONG)
								.show();*/

						try {
							DBHelper hp = new DBHelper(getActivity());
							db = hp.getWritableDatabase();
							db.delete(DBTABLE2, null, null);
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					

					//	if (page == 1) {

							countDownTimer = new MyCountDownTimer(startTime,
									interval);
							countDownTimer.start();
							mHaveMoreDataToLoad = true;

							popular_feed(page);

						/*} else {

							countDownTimer = new MyCountDownTimer(startTime,
									interval);
							countDownTimer.start();
							mHaveMoreDataToLoad = true;
						}*/

					} else {

					/*	Toast.makeText(getActivity(),
								"Internet connection is Off", Toast.LENGTH_LONG)
								.show();*/
						countDownTimer = new MyCountDownTimer(startTime,
								interval);
						countDownTimer.cancel();
						mHaveMoreDataToLoad = false;

						pojolist = new ArrayList<POJO>();
						colorlist = new ArrayList<Integer>();
						pojolist.clear();
						colorlist.clear();
						progressBar1.setVisibility(View.GONE);

						new localUpload().execute();

					}
				}
			};

			final IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			getActivity().registerReceiver((BroadcastReceiver) br, intentFilter);

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

				Cursor c = db.rawQuery(" select * from " + DBTABLE2
						+ " order by type", null);
				if (c != null) {
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
									String store_rate = c.getString(c
											.getColumnIndex("store_rate"));
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
									String feed_id = c.getString(c
											.getColumnIndex("feed_id"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									
									String view_count = c.getString(c.getColumnIndex("hits_count"));
									String store_id = c.getString(c.getColumnIndex("store_id"));
									
									POJO pp = new POJO(mode, username, userimg,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											store_rate, review, cards, idd,
											description, is_new, is_liked,
											slug, rated_color, rated, userid,
											comment_count, img_width,
											img_height, feed_id, status,view_count,store_id);

									pojolist.add(pp);
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
									String store_rate = c.getString(c
											.getColumnIndex("store_rate"));
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
									String feed_id = c.getString(c
											.getColumnIndex("feed_id"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String view_count = c.getString(c.getColumnIndex("hits_count"));
									String store_id = c.getString(c.getColumnIndex("store_id"));

									POJO pp = new POJO(mode, username, userimg,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											store_rate, review, cards, idd,
											description, is_new, is_liked,
											slug, rated_color, rated, userid,
											comment_count, img_width,
											img_height, feed_id, status,view_count,store_id);

									pojolist.add(pp);
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
									String store_rate = c.getString(c
											.getColumnIndex("store_rate"));
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
									String feed_id = c.getString(c
											.getColumnIndex("feed_id"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String view_count = c.getString(c.getColumnIndex("hits_count"));
									String store_id = c.getString(c.getColumnIndex("store_id"));

									POJO pp = new POJO(mode, username, userimg,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											store_rate, review, cards, idd,
											description, is_new, is_liked,
											slug, rated_color, rated, userid,
											comment_count, img_width,
											img_height, feed_id, status,view_count,store_id);

									pojolist.add(pp);
									colorlist.add(Integer.parseInt(color));

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
									String store_rate = c.getString(c
											.getColumnIndex("store_rate"));
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
									String feed_id = c.getString(c
											.getColumnIndex("feed_id"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String view_count = c.getString(c.getColumnIndex("hits_count"));
									String store_id = c.getString(c.getColumnIndex("store_id"));

									POJO pp = new POJO(mode, username, userimg,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											store_rate, review, cards, idd,
											description, is_new, is_liked,
											slug, rated_color, rated, userid,
											comment_count, img_width,
											img_height, feed_id, status,view_count,store_id);

									pojolist.add(pp);
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
									String store_rate = c.getString(c
											.getColumnIndex("store_rate"));
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
									String feed_id = c.getString(c
											.getColumnIndex("feed_id"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String view_count = c.getString(c.getColumnIndex("hits_count"));
									String store_id = c.getString(c.getColumnIndex("store_id"));

									POJO pp = new POJO(mode, username, userimg,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											store_rate, review, cards, idd,
											description, is_new, is_liked,
											slug, rated_color, rated, userid,
											comment_count, img_width,
											img_height, feed_id, status,view_count, store_id);

									pojolist.add(pp);
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
									String store_rate = c.getString(c
											.getColumnIndex("store_rate"));
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
									String feed_id = c.getString(c
											.getColumnIndex("feed_id"));
									String status = c.getString(c
											.getColumnIndex("status"));
									String color = c.getString(c
											.getColumnIndex("color"));
									String view_count = c.getString(c.getColumnIndex("hits_count"));
									String store_id = c.getString(c.getColumnIndex("store_id"));

									POJO pp = new POJO(mode, username, userimg,
											lookimg, img1, img2, likes, hits,
											title, store_name, store_location,
											store_rate, review, cards, idd,
											description, is_new, is_liked,
											slug, rated_color, rated, userid,
											comment_count, img_width,
											img_height, feed_id, status,view_count,store_id);

									pojolist.add(pp);
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
							String store_rate = c.getString(c
									.getColumnIndex("store_rate"));
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
							String feed_id = c.getString(c
									.getColumnIndex("feed_id"));
							String status = c.getString(c
									.getColumnIndex("status"));
							String color = c.getString(c
									.getColumnIndex("color"));
							
							String view_count = c.getString(c.getColumnIndex("hits_count"));
							String store_id = c.getString(c.getColumnIndex("store_id"));

							POJO pp = new POJO(mode, username, userimg,
									lookimg, img1, img2, likes, hits, title,
									store_name, store_location, store_rate,
									review, cards, idd, description, is_new,
									is_liked, slug, rated_color, rated, userid,
									comment_count, img_width, img_height,
									feed_id, status,view_count,store_id);
							pojolist.add(pp);
							colorlist.add(Integer.parseInt(color));

						}
						/**
						 * For Teams
						 */
						else if (mode.equals("Teams")) {
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
							String store_rate = c.getString(c
									.getColumnIndex("store_rate"));
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
							String feed_id = c.getString(c
									.getColumnIndex("feed_id"));
							String status = c.getString(c
									.getColumnIndex("status"));
							String color = c.getString(c
									.getColumnIndex("color"));
							String view_count = c.getString(c.getColumnIndex("hits_count"));
							String store_id = c.getString(c.getColumnIndex("store_id"));

							POJO pp = new POJO(mode, username, userimg,
									lookimg, img1, img2, likes, hits, title,
									store_name, store_location, store_rate,
									review, cards, idd, description, is_new,
									is_liked, slug, rated_color, rated, userid,
									comment_count, img_width, img_height,
									feed_id, status,view_count,store_id);
							pojolist.add(pp);
							colorlist.add(Integer.parseInt(color));
						}

					}

				}

			} catch (SQLiteException s) {
				s.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter = new PopularAdapter1(getActivity(), pojolist, colorlist,userimg, username);
			endlessListView.setAdapter(adapter);
			mHaveMoreDataToLoad = false;
			endlessListView.setVisibility(View.VISIBLE);

		}

	}
	

	/**
	 * @void loadMoreData()
	 */
	private void loadMoreData() {
		mHaveMoreDataToLoad = true;
		pageRecent++;
		MainActivity.mypage++;
		recent_homeFeed(pageRecent);

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
	public void popular_feed(int page) {
		long time = System.currentTimeMillis();
		// client.setBasicAuth(user_email, user_passwor d);
		// Log.e("URL", POPULAR_REST_URL + page + "&_=" + time);
		client.post(POPULAR_REST_URL + page + "&_=" + time,
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
							while ((line = br.readLine()) != null) {

								text = text + line;
							}

							showData(text);
							// Log.e("RESP", text);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {

						mHaveMoreDataToLoad = false;
						endlessListView.loadMoreCompleat();
						progressBar1.setVisibility(View.GONE);
						// Log.e("RESP_FAIL", "FAIL "+statusCode);
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
	public void popular_loadmoreFeed(int page) {
		long time = System.currentTimeMillis();
		// Log.e("URL_MORE", POPULAR_REST_URL + page + "&_=" + time);
		// client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		for (int j = 0; j < MainActivity.mypojolist.size(); j++) {

			String id_id = MainActivity.mypojolist.get(j).getFeed_id();

			params.put("notin[" + j + "]", id_id);

		}

		client.post(POPULAR_REST_URL + page + "&_=" + time, params,

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

						statuscode = String.valueOf(statusCode);
						progressBar2.setVisibility(View.GONE);
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

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(
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
		Popular ppp = gson.fromJson(reader, Popular.class);
		List<popularfeed> feeds = ppp.getFeedPopular();

		new MyApp().execute(feeds);

	}
	
	/**
	 * MyApp extends AsyncTask<List<popularfeed>, Void, Void> for Showdata(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp extends AsyncTask<List<popularfeed>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<popularfeed>... params) {

			try {
				DBHelper hp = new DBHelper(getActivity());
				db = hp.getWritableDatabase();	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
			pojolist = new ArrayList<POJO>();
			pojolist.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < params[0].size(); i++) {
				popularfeed pop = params[0].get(i);
				String feed_id = pop.get_id();
				if (pop.getModel().equals("Lookbooks")) {

					Popular_Lookbookdata look = pop.getLookbookdata();
					/**
					 * For Lookbook
					 */
					if (look != null) {

						try {

							Popular_Lookbook_User user = look.getUser();
							ArrayList<Popular_Lookbook_Cards> cards = look
									.getCards();

							if (cards.size() >= 3) {

								Popular_Lookbook_Cards ccll = cards.get(0);
								Popular_Lookbook_Cards ccll1 = cards.get(1);
								Popular_Lookbook_Cards ccll2 = cards.get(2);
								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String is_liked = look.getIs_liked();
								String idd = look.getId();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookcomment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, img1, img2, likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "second");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, img2);
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else if (cards.size() == 2) {

								Popular_Lookbook_Cards ccll = cards.get(0);
								Popular_Lookbook_Cards ccll1 = cards.get(1);

								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookcomment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, img1, "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "second");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);

							} else {

								Popular_Lookbook_Cards ccll = cards.get(0);

								String lookimg = ccll.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookcomment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, "na", "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "second");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, "na");
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);

							}
						} catch (Exception e) {

						}

					}
				}
				/**
				 * For Articles
				 */
				else if (pop.getModel().equals("Articles")) {

					Popular_ArticleData look = pop.getArticaldata();
					if (look != null) {
						try {

							Popular_Article_User user = look.getUser();
							ArrayList<Popular_Article_Images> cards = look
									.getArticle_images();

							if (cards.size() >= 3) {

								Popular_Article_Images ccll = cards.get(0);
								Popular_Article_Images ccll1 = cards.get(1);
								Popular_Article_Images ccll2 = cards.get(2);
								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, img1, img2, likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "second");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, img2);
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
							//	MainActivity.mycolorlist.add(colors[rnd]);
							//	MainActivity.mypojolist.add(pp);

							} else if (cards.size() == 2) {

								Popular_Article_Images ccll = cards.get(0);
								Popular_Article_Images ccll1 = cards.get(1);

								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String is_liked = look.getIs_liked();
								String idd = look.getId();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, img1, "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "second");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);

							} else {

								Popular_Article_Images ccll = cards.get(0);

								String lookimg = ccll.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, "na", "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "second");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, "na");
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);

							}
						} catch (Exception e) {

						}
					}
				}
				/**
				 * For StoreReviews
				 */
				else if (pop.getModel().equals("StoreReviews")) {

					Popular_StoreReviewData store = pop.getStorereviewdata();
					if (store != null) {
						try {

							Popular_StoreReview_Users user = store.getUser();
							Popular_StoreReview_Store likes = store.getStore();

							username = user.getFirst_name() + " "
									+ user.getLast_name();
							userimg = user.getAndroid_api_img();
							// String lookimg = "na";
							String likes1 = store.getLikes_count();
							String hits = store.getHits_text();
							String view_count = store.getHits();
							String store_id = store.getStore_id();
							String review = store.getReview();
							String store_name = likes.getStore_name();
							String store_location = likes.getMarket();
							String store_rate = likes.getOverall_ratings();
							// String description = "na";
							String idd = store.getId();
							String is_liked = store.getIs_liked();
							String slug = likes.getSlug();
							String rated_color = store.getRated_color();
							String rated = store.getRated();
							String userid = user.getId();
							POJO pp = new POJO("StoreReviews", username,
									userimg, "na", "na", "na", likes1, hits,
									"na", store_name, store_location,
									store_rate, review, "na", idd, "na", "na",
									is_liked, slug, rated_color, rated, userid,
									"na", "na", "na", feed_id, "na", view_count,store_id);
							pojolist.add(pp);
							colorlist.add(R.color.tabscolor);
							
							stm.bindString(1, "StoreReviews");
							stm.bindString(2, "second");
							stm.bindString(3, username);
							stm.bindString(4, userimg);
							stm.bindString(5, "na");
							stm.bindString(6, "na");
							stm.bindString(7, "na");
							stm.bindString(8, likes1);
							stm.bindString(9, hits);
							stm.bindString(10, "na");
							stm.bindString(11, store_name);
							stm.bindString(12, store_location);
							stm.bindString(13, store_rate);
							stm.bindString(14, review);
							stm.bindString(15, "na");
							stm.bindString(16, idd);
							stm.bindString(17, "na");
							stm.bindString(18, "na");
							stm.bindString(19, is_liked);
							stm.bindString(20, slug);
							stm.bindString(21, rated_color);
							stm.bindString(22, rated);
							stm.bindString(23, userid);
							stm.bindString(24, "na");
							stm.bindString(25, "na");
							stm.bindString(26, "na");
							stm.bindString(27, feed_id);
							stm.bindString(28, "na");
							stm.bindString(29, String.valueOf(R.color.tabscolor));
							stm.bindString(30, view_count);
							stm.bindString(31, store_id);
							stm.executeInsert();
							
							//MainActivity.mycolorlist.add(R.color.tabscolor);
							//MainActivity.mypojolist.add(pp);

						} catch (Exception e) {
						}
					}
				}
				/**
				 * For Teams
				 */
				else if (pop.getModel().equals("Teams")) {

					Popular_Teamsdata team = pop.getTeamsdata();
					if (team != null) {

						try {

							String username = "Zakoopi Team";
							String userimg = "na";
							String lookimg = team.getAndroid_api_img();
							String likes1 = team.getLikes_count();
							String hits = team.getHits();
							String title = team.getTitle();
							// String description = "na";
							String idd = team.getId();
							// String is_liked = "na";
							String slug = team.getSlug();
							POJO pp = new POJO("Teams", username, userimg,
									lookimg, "na", "na", likes1, hits, title,
									"na", "na", "na", "na", "na", idd, "na",
									"na", "false", slug, "na", "na", "na",
									"na", "na", "na", feed_id, "na", "na","na");
							pojolist.add(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
							
							stm.bindString(1, "Teams");
							stm.bindString(2, "second");
							stm.bindString(3, username);
							stm.bindString(4, userimg);
							stm.bindString(5, lookimg);
							stm.bindString(6, "na");
							stm.bindString(7, "na");
							stm.bindString(8, likes1);
							stm.bindString(9, hits);
							stm.bindString(10, title);
							stm.bindString(11, "na");
							stm.bindString(12, "na");
							stm.bindString(13, "na");
							stm.bindString(14, "na");
							stm.bindString(15, "na");
							stm.bindString(16, idd);
							stm.bindString(17, "na");
							stm.bindString(18, "na");
							stm.bindString(19, "false");
							stm.bindString(20, slug);
							stm.bindString(21, "na");
							stm.bindString(22, "na");
							stm.bindString(23, "na");
							stm.bindString(24, "na");
							stm.bindString(25, "na");
							stm.bindString(26, "na");
							stm.bindString(27, feed_id);
							stm.bindString(28, "na");
							stm.bindString(29,
									String.valueOf(R.color.tabscolor));
							stm.bindString(30,"na");
							stm.bindString(31, "na");
							stm.executeInsert();
							
							//MainActivity.mycolorlist.add(colors[rnd]);
							//MainActivity.mypojolist.add(pp);

						} catch (Exception e) {
						}
					}
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter = new PopularAdapter1(getActivity(), pojolist, colorlist,
					userimg, username);
			endlessListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			progressBar1.setVisibility(View.GONE);
			mHaveMoreDataToLoad = true;
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
		Popular ppp = gson.fromJson(reader, Popular.class);
		List<popularfeed> feeds = ppp.getFeedPopular();
		new MyApp1().execute(feeds);
	}

	/**
	 * MyApp1 extends AsyncTask<List<popularfeed>, Void, Void> for
	 * showmoreData(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp1 extends AsyncTask<List<popularfeed>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<popularfeed>... params) {

			pojolist = new ArrayList<POJO>();
			pojolist.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < params[0].size(); i++) {
				popularfeed pop = params[0].get(i);
				String feed_id = pop.get_id();
				if (pop.getModel().equals("Lookbooks")) {

					Popular_Lookbookdata look = pop.getLookbookdata();
					/**
					 * For Lookbook
					 */
					if (look != null) {
						try {

							Popular_Lookbook_User user = look.getUser();
							ArrayList<Popular_Lookbook_Cards> cards = look
									.getCards();

							if (cards.size() >= 3) {

								Popular_Lookbook_Cards ccll = cards.get(0);
								Popular_Lookbook_Cards ccll1 = cards.get(1);
								Popular_Lookbook_Cards ccll2 = cards.get(2);
								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookcomment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, img1, img2, likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "first");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, img2);
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else if (cards.size() == 2) {

								Popular_Lookbook_Cards ccll = cards.get(0);
								Popular_Lookbook_Cards ccll1 = cards.get(1);

								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookcomment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, img1, "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "first");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else {

								Popular_Lookbook_Cards ccll = cards.get(0);

								String lookimg = ccll.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookcomment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, "na", "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");

								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "first");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, "na");
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							}

						} catch (Exception e) {

						}
					}
				}
				/**
				 * For Articles
				 */
				else if (pop.getModel().equals("Articles")) {

					Popular_ArticleData look = pop.getArticaldata();
					if (look != null) {
						try {

							Popular_Article_User user = look.getUser();
							ArrayList<Popular_Article_Images> cards = look
									.getArticle_images();

							// article_image_url_list.clear();

							if (cards.size() >= 3) {

								Popular_Article_Images ccll = cards.get(0);
								Popular_Article_Images ccll1 = cards.get(1);
								Popular_Article_Images ccll2 = cards.get(2);
								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, img1, img2, likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "first");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, img2);
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);

							} else if (cards.size() == 2) {

								Popular_Article_Images ccll = cards.get(0);
								Popular_Article_Images ccll1 = cards.get(1);

								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, img1, "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "first");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else {

								Popular_Article_Images ccll = cards.get(0);

								String lookimg = ccll.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, "na", "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "first");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, "na");
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							}

						} catch (Exception e) {

						}
					}
				}
				/**
				 * For StoreReviews
				 */
				else if (pop.getModel().equals("StoreReviews")) {

					Popular_StoreReviewData store = pop.getStorereviewdata();
					if (store != null) {
						try {

							Popular_StoreReview_Users user = store.getUser();
							Popular_StoreReview_Store likes = store.getStore();

							username = user.getFirst_name() + " "
									+ user.getLast_name();
							userimg = user.getAndroid_api_img();
							// String lookimg = "na";
							String likes1 = store.getLikes_count();
							String hits = store.getHits_text();
							String view_count = store.getHits();
							String store_id = store.getStore_id();
							String review = store.getReview();
							String store_name = likes.getStore_name();
							// String description = "na";
							String store_location = likes.getMarket();
							String store_rate = likes.getOverall_ratings();
							String idd = store.getId();
							String is_liked = store.getIs_liked();
							String slug = likes.getSlug();
							String rated_color = store.getRated_color();
							String rated = store.getRated();
							String userid = user.getId();
							POJO pp = new POJO("StoreReviews", username,
									userimg, "na", "na", "na", likes1, hits,
									"na", store_name, store_location,
									store_rate, review, "na", idd, "na", "na",
									is_liked, slug, rated_color, rated, userid,
									"na", "na", "na", feed_id, "na", view_count,store_id);
							pojolist.add(pp);
							colorlist.add(R.color.tabscolor);
							
							stm.bindString(1, "StoreReviews");
							stm.bindString(2, "first");
							stm.bindString(3, username);
							stm.bindString(4, userimg);
							stm.bindString(5, "na");
							stm.bindString(6, "na");
							stm.bindString(7, "na");
							stm.bindString(8, likes1);
							stm.bindString(9, hits);
							stm.bindString(10, "na");
							stm.bindString(11, store_name);
							stm.bindString(12, store_location);
							stm.bindString(13, store_rate);
							stm.bindString(14, review);
							stm.bindString(15, "na");
							stm.bindString(16, idd);
							stm.bindString(17, "na");
							stm.bindString(18, "na");
							stm.bindString(19, is_liked);
							stm.bindString(20, slug);
							stm.bindString(21, rated_color);
							stm.bindString(22, rated);
							stm.bindString(23, userid);
							stm.bindString(24, "na");
							stm.bindString(25, "na");
							stm.bindString(26, "na");
							stm.bindString(27, feed_id);
							stm.bindString(28, "na");
							stm.bindString(29, String.valueOf(R.color.tabscolor));
							stm.bindString(30, view_count);
							stm.bindString(31, store_id);
							stm.executeInsert();
							
							//MainActivity.mycolorlist.add(R.color.tabscolor);
							//MainActivity.mypojolist.add(pp);
						} catch (Exception e) {
						}
					}
				}
				/**
				 * For Teams
				 */
				else if (pop.getModel().equals("Teams")) {

					Popular_Teamsdata team = pop.getTeamsdata();
					if (team != null) {
						try {

							String username = "Zakoopi Team";
							String userimg = "na";
							String lookimg = team.getAndroid_api_img();
							String likes1 = team.getLikes_count();
							String hits = team.getHits();
							String title = team.getTitle();
							// String description = "na";
							String idd = team.getId();
							// String is_liked = "na";
							String slug = team.getSlug();
							POJO pp = new POJO("Teams", username, userimg,
									lookimg, "na", "na", likes1, hits, title,
									"na", "na", "na", "na", "na", idd, "na",
									"na", "false", slug, "na", "na", "na",
									"na", "na", "na", feed_id, "na", "na","na");
							pojolist.add(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
							
							stm.bindString(1, "Teams");
							stm.bindString(2, "first");
							stm.bindString(3, username);
							stm.bindString(4, userimg);
							stm.bindString(5, lookimg);
							stm.bindString(6, "na");
							stm.bindString(7, "na");
							stm.bindString(8, likes1);
							stm.bindString(9, hits);
							stm.bindString(10, title);
							stm.bindString(11, "na");
							stm.bindString(12, "na");
							stm.bindString(13, "na");
							stm.bindString(14, "na");
							stm.bindString(15, "na");
							stm.bindString(16, idd);
							stm.bindString(17, "na");
							stm.bindString(18, "na");
							stm.bindString(19, "false");
							stm.bindString(20, slug);
							stm.bindString(21, "na");
							stm.bindString(22, "na");
							stm.bindString(23, "na");
							stm.bindString(24, "na");
							stm.bindString(25, "na");
							stm.bindString(26, "na");
							stm.bindString(27, feed_id);
							stm.bindString(28, "na");
							stm.bindString(29,
									String.valueOf(R.color.tabscolor));
							stm.bindString(30,"na");
							stm.bindString(31, "na");
							stm.executeInsert();
							
							//MainActivity.mycolorlist.add(colors[rnd]);
							//MainActivity.mypojolist.add(pp);
						} catch (Exception e) {

						}
					}
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter.addColorsNew(colorlist);
			adapter.addItemsNew(pojolist);
			// new change
			endlessListView.smoothScrollToPosition(0);
			endlessListView.setSelection(0);

			progressBar2.setVisibility(View.GONE);
			/*
			 * endlessListView.loadMoreCompleat();
			 * endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			 * endlessListView.setVerticalScrollBarEnabled(false);
			 */

		}

	}

	/**
	 * @popular_feed recent
	 */
	public void recent_homeFeed(int page) {
		long time = System.currentTimeMillis();
		String RECENT_REST_URL = getString(R.string.base_url)
				+ "feedRecent.json?page=";
		// Log.e("jjjjj", RECENT_REST_URL + page + "&_=" + time);
		// client.setBasicAuth(user_email, user_password);o
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(RECENT_REST_URL + page + "&_=" + time,
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

							String text = "";
							String line = "";

							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							while ((line = br.readLine()) != null) {

								text = text + line;
							}

							showDataRecent(text);

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

					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}

	/**
	 * Latest Feed showDataRecent
	 * 
	 * @showData data
	 */
	@SuppressWarnings("unchecked")
	public void showDataRecent(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		Recent ppp = gson.fromJson(reader, Recent.class);
		List<recentfeed> feeds = ppp.getFeedRecent();

		new MyApp123().execute(feeds);

	}

	private class MyApp123 extends AsyncTask<List<recentfeed>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<recentfeed>... params) {

			pojolist = new ArrayList<POJO>();
			pojolist.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < params[0].size(); i++) {
				recentfeed pop = params[0].get(i);
				String feed_id = pop.get_id();
				if (pop.getModel().equals("Lookbooks")) {

					Recent_Lookbookdata look = pop.getLookbookdata();
					/**
					 * For Lookbook
					 */
					if (look != null) {

						try {

							Recent_Lookbook_User user = look.getUser();
							ArrayList<Recent_Lookbook_Cards> cards = look
									.getCards();

							if (cards.size() >= 3) {

								Recent_Lookbook_Cards ccll = cards.get(0);
								Recent_Lookbook_Cards ccll1 = cards.get(1);
								Recent_Lookbook_Cards ccll2 = cards.get(2);
								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String is_liked = look.getIs_liked();
								String idd = look.getId();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookComment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, img1, img2, likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "third");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, img2);
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else if (cards.size() == 2) {

								Recent_Lookbook_Cards ccll = cards.get(0);
								Recent_Lookbook_Cards ccll1 = cards.get(1);

								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookComment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, img1, "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "third");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else {

								Recent_Lookbook_Cards ccll = cards.get(0);

								String lookimg = ccll.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLookbooklike_count();
								String hits = look.getHits_text();
								String view_count = look.getView_count();
								String title = look.getTitle();
								String description = ccll.getDescription();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getLookbookComment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Lookbooks", username,
										userimg, lookimg, "na", "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, "na", is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Lookbooks");
								stm.bindString(2, "third");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, "na");
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, "na");
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							}
						} catch (Exception e) {

						}

					}
				}
				/**
				 * For Articles
				 */
				else if (pop.getModel().equals("Articles")) {

					Recent_ArticleData look = pop.getArticaldata();
					if (look != null) {
						try {

							Recent_Article_User user = look.getUser();
							ArrayList<Recent_Article_Images> cards = look
									.getArticle_images();

							if (cards.size() >= 3) {

								Recent_Article_Images ccll = cards.get(0);
								Recent_Article_Images ccll1 = cards.get(1);
								Recent_Article_Images ccll2 = cards.get(2);
								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();
								String img2 = ccll2.getMedium_img();
								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, img1, img2, likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "third");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, img2);
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);

							} else if (cards.size() == 2) {

								Recent_Article_Images ccll = cards.get(0);
								Recent_Article_Images ccll1 = cards.get(1);

								String lookimg = ccll.getMedium_img();
								String img1 = ccll1.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String is_liked = look.getIs_liked();
								String idd = look.getId();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, img1, "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "third");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, img1);
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							} else {

								Recent_Article_Images ccll = cards.get(0);

								String lookimg = ccll.getMedium_img();

								username = user.getFirst_name() + " "
										+ user.getLast_name();
								userimg = user.getAndroid_api_img();

								String likes = look.getLikes_count();
								String hits = look.getHits_text();
								String view_count = look.getHits();
								String title = look.getTitle();
								String description = look.getDescription();
								String is_new = look.getIsNew();
								String idd = look.getId();
								String is_liked = look.getIs_liked();
								String slug = look.getSlug();
								String userid = user.getId();
								String comment_count = look
										.getArticle_comment_count();
								String img_width = ccll.getMedium_img_w();
								String img_height = ccll.getMedium_img_h();

								POJO pp = new POJO("Articles", username,
										userimg, lookimg, "na", "na", likes,
										hits, title, "na", "na", "na", "na",
										String.valueOf(cards.size()), idd,
										description, is_new, is_liked, slug,
										"na", "na", userid, comment_count,
										img_width, img_height, feed_id, "na",
										view_count,"na");
								pojolist.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								
								stm.bindString(1, "Articles");
								stm.bindString(2, "third");
								stm.bindString(3, username);
								stm.bindString(4, userimg);
								stm.bindString(5, lookimg);
								stm.bindString(6, "na");
								stm.bindString(7, "na");
								stm.bindString(8, likes);
								stm.bindString(9, hits);
								stm.bindString(10, title);
								stm.bindString(11, "na");
								stm.bindString(12, "na");
								stm.bindString(13, "na");
								stm.bindString(14, "na");
								stm.bindString(15, String.valueOf(cards.size()));
								stm.bindString(16, idd);
								stm.bindString(17, description);
								stm.bindString(18, is_new);
								stm.bindString(19, is_liked);
								stm.bindString(20, slug);
								stm.bindString(21, "na");
								stm.bindString(22, "na");
								stm.bindString(23, userid);
								stm.bindString(24, comment_count);
								stm.bindString(25, img_width);
								stm.bindString(26, img_height);
								stm.bindString(27, feed_id);
								stm.bindString(28, "na");
								stm.bindString(29, String.valueOf(colors[rnd]));
								stm.bindString(30, view_count);
								stm.bindString(31, "na");
								stm.executeInsert();
								
								//MainActivity.mycolorlist.add(colors[rnd]);
								//MainActivity.mypojolist.add(pp);
							}
						} catch (Exception e) {

						}
					}
				}
				/**
				 * For StoreReviews
				 */
				else if (pop.getModel().equals("StoreReviews")) {

					Recent_StoreReviewData store = pop.getStorereviewdata();
					if (store != null) {
						try {

							Recent_StoreReview_Users user = store.getUser();
							Recent_StoreReview_Store likes = store.getStore();

							username = user.getFirst_name() + " "
									+ user.getLast_name();
							userimg = user.getAndroid_api_img();
							// String lookimg = "na";
							String likes1 = store.getLikes_count();
							String hits = store.getHits_text();
							String view_count = store.getHits();
							String store_id = store.getStore_id();
							String review = store.getReview();
							String store_name = likes.getStore_name();
							String store_location = likes.getMarket();
							String store_rate = likes.getOverall_ratings();
							// String description = "na";
							String idd = store.getId();
							String is_liked = store.getIs_liked();
							String slug = likes.getSlug();
							String rated_color = store.getRated_color();
							String rated = store.getRated();
							String userid = user.getId();

							POJO pp = new POJO("StoreReviews", username,
									userimg, "na", "na", "na", likes1, hits,
									"na", store_name, store_location,
									store_rate, review, "na", idd, "na", "na",
									is_liked, slug, rated_color, rated, userid,
									"na", "na", "na", feed_id, "na", view_count, store_id);
							pojolist.add(pp);
							colorlist.add(R.color.tabscolor);
							
							stm.bindString(1, "StoreReviews");
							stm.bindString(2, "third");
							stm.bindString(3, username);
							stm.bindString(4, userimg);
							stm.bindString(5, "na");
							stm.bindString(6, "na");
							stm.bindString(7, "na");
							stm.bindString(8, likes1);
							stm.bindString(9, hits);
							stm.bindString(10, "na");
							stm.bindString(11, store_name);
							stm.bindString(12, store_location);
							stm.bindString(13, store_rate);
							stm.bindString(14, review);
							stm.bindString(15, "na");
							stm.bindString(16, idd);
							stm.bindString(17, "na");
							stm.bindString(18, "na");
							stm.bindString(19, is_liked);
							stm.bindString(20, slug);
							stm.bindString(21, rated_color);
							stm.bindString(22, rated);
							stm.bindString(23, userid);
							stm.bindString(24, "na");
							stm.bindString(25, "na");
							stm.bindString(26, "na");
							stm.bindString(27, feed_id);
							stm.bindString(28, "na");
							stm.bindString(29, String.valueOf(R.color.tabscolor));
							stm.bindString(30, view_count);
							stm.bindString(31, store_id);
							stm.executeInsert();
							
							//MainActivity.mycolorlist.add(R.color.tabscolor);
							//MainActivity.mypojolist.add(pp);
						} catch (Exception e) {
						}
					}
				}
				/**
				 * For Teams
				 */
				else if (pop.getModel().equals("Teams")) {

					Recent_Teamsdata team = pop.getTeamsdata();
					if (team != null) {

						try {

							String username = "Zakoopi Team";
							String userimg = "na";
							String lookimg = team.getAndroid_api_img();
							String likes1 = team.getLikes_count();
							String hits = team.getHits();
							String title = team.getTitle();
							// String description = "na";
							String idd = team.getId();
							// String is_liked = "na";
							String slug = team.getSlug();
							POJO pp = new POJO("Teams", username, userimg,
									lookimg, "na", "na", likes1, hits, title,
									"na", "na", "na", "na", "na", idd, "na",
									"na", "false", slug, "na", "na", "na",
									"na", "na", "na", feed_id, "na", "na","na");
							pojolist.add(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
							
							stm.bindString(1, "Teams");
							stm.bindString(2, "third");
							stm.bindString(3, username);
							stm.bindString(4, userimg);
							stm.bindString(5, lookimg);
							stm.bindString(6, "na");
							stm.bindString(7, "na");
							stm.bindString(8, likes1);
							stm.bindString(9, hits);
							stm.bindString(10, title);
							stm.bindString(11, "na");
							stm.bindString(12, "na");
							stm.bindString(13, "na");
							stm.bindString(14, "na");
							stm.bindString(15, "na");
							stm.bindString(16, idd);
							stm.bindString(17, "na");
							stm.bindString(18, "na");
							stm.bindString(19, "false");
							stm.bindString(20, slug);
							stm.bindString(21, "na");
							stm.bindString(22, "na");
							stm.bindString(23, "na");
							stm.bindString(24, "na");
							stm.bindString(25, "na");
							stm.bindString(26, "na");
							stm.bindString(27, feed_id);
							stm.bindString(28, "na");
							stm.bindString(29,
									String.valueOf(R.color.tabscolor));
							stm.bindString(30,"na");
							stm.bindString(31, "na");
							stm.executeInsert();
							
							//MainActivity.mycolorlist.add(colors[rnd]);
							//MainActivity.mypojolist.add(pp);
						} catch (Exception e) {
						}
					}
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter.addItems(pojolist);
			adapter.addColors(colorlist);
			endlessListView.loadMoreCompleat();

			endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			endlessListView.setVerticalScrollBarEnabled(false);

		}

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
						getActivity().finish();
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
