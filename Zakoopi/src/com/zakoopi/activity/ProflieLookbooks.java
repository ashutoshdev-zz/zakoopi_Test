package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mystores.StoreActivity;
import com.zakoopi.R;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.helper.Variables;
import com.zakoopi.userfeed.model.Lookbook_User_Data;
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
import com.zakoopi.utils.ProfileLookbooksAdapter;
import com.zakoopi.utils.TopMarketStoreAdapter;

public class ProflieLookbooks extends Activity {
	ProfileLookbooksAdapter adapter;
	RelativeLayout rel_back;
	TextView txt_back;
	private static String USER_FEED_LOOKBOOK_REST_URL = "";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	public ArrayList<UserFeedPojo> feedPojo = null;
	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	private SharedPreferences pro_user_pref, pref_location;
	String  pro_user_pic_url, pro_user_name, pro_user_location,
			user_email, user_password, city_name;
	public int page = 1;
	public String url_img, user_name,user_id;
	Typeface typeface_semibold, typeface_black, typeface_bold;
	ProgressBar progressBar1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_activityes);

		try {
			pref_location = getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "123");

			client = ClientHttp.getInstance(ProflieLookbooks.this);
			// Log.e("NAME_CITY", city_name);
			Intent i = getIntent();
			user_id = i.getStringExtra("user_id");
			url_img = i.getStringExtra("user_img");
			user_name = i.getStringExtra("user_name");
		//	Log.e("PRO", user_id+"--"+url_img+"--"+user_name);
		//	Variables.user_id_list.add(user_id);
			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = getSharedPreferences("User_detail", 0);
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");

			

			/**
			 * Typeface
			 */
			typeface_semibold = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Semibold.ttf");
			typeface_black = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Black.ttf");
			typeface_bold = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Bold.ttf");

		} catch (Exception e) {
			// TODO: handle exception
		}

		endlessListView = (EndlessListView) findViewById(R.id.endlessListView);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		txt_back = (TextView) findViewById(R.id.txt);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_back.setTypeface(typeface_semibold);
		txt_back.setText("Lookbooks");
		endlessListView.setOnLoadMoreListener(loadMoreListener);
		USER_FEED_LOOKBOOK_REST_URL = getString(R.string.base_url)
				+ "feedTimeline/getLookbooksbyUserId/" + user_id
				+ ".json?page=";
		 activity_feed(page);

		rel_back.setOnClickListener(new OnClickListener() {  

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*if (Variables.myact.equals("proflooks") && (feedPojo.size() > 0)) {
Log.e("IFFF", "IFFF");
			adapter.notifyDataSetChanged();
		//	page = LookbookView1.mypage1;
			//user_id = Variables.user_id_list.get(Variables.user_id_list.size()-1);
			Variables.myact = "noway";
			progressBar1.setVisibility(View.GONE);
			

		} else {
			Log.e("IFELS", "ELSE");
			USER_FEED_LOOKBOOK_REST_URL = getString(R.string.base_url)
					+ "feedTimeline/getLookbooksbyUserId/" + user_id
					+ ".json?page=";
			activity_feed(page);
			
		}*/
		
		
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

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
		//Log.e("url", USER_FEED_LOOKBOOK_REST_URL + page + "&_=" + time);
		client.setBasicAuth(user_email, user_password);

		client.get(USER_FEED_LOOKBOOK_REST_URL + page + "&_=" + time,
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
		//Log.e("url_MORE", USER_FEED_LOOKBOOK_REST_URL + page + "&_=" + time);

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(USER_FEED_LOOKBOOK_REST_URL + page + "&_=" + time,

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
					/*	Log.e("ggggg", e.getMessage() + "vvvvvv   "
								+ statusCode);*/
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
		GoogleAnalytics.getInstance(ProflieLookbooks.this).reportActivityStart(
				ProflieLookbooks.this);

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
	//	Log.e("FEED_USER", "" + feeds_user);
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

			feedPojo = new ArrayList<UserFeedPojo>();
			feedPojo.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			if (params[0].size() > 0) {

				for (int i = 0; i < params[0].size(); i++) {
					feedTimeline ft = params[0].get(i);

					if (ft.getModel().equals("Lookbooks")) {

						User_LookbookData look = ft.getLookbooksData();
						Lookbook_User_Data ud = look.getUser();
						String look_user_name = ud.getFirst_name()+" "+ud.getLast_name();
						String look_user_img = ud.getAndroid_api_img();
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
											look_user_name, look_user_img, user_id,"na",view_count,"na","na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
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
											look_user_name, look_user_img, user_id,"na",view_count,"na","na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
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
											look_user_name, look_user_img, user_id,"na",view_count,"na","na");
									feedPojo.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
								}
							} catch (Exception e) {

							}

						}
					}

				}

			} else {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter = new ProfileLookbooksAdapter(ProflieLookbooks.this,
					feedPojo, colorlist,url_img,user_name);
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
	//	Log.e("FEED_USER", "" + feeds_user);
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
					Lookbook_User_Data ud = look.getUser();
					String look_user_name = ud.getFirst_name()+" "+ud.getLast_name();
					String look_user_img = ud.getAndroid_api_img();
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
										medium_img_w, medium_img_h, look_user_name,
										look_user_img, user_id,"na",view_count,"na","na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
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
										medium_img_w, medium_img_h, look_user_name,
										look_user_img, user_id,"na",view_count,"na","na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
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
										medium_img_w, medium_img_h, look_user_name,
										look_user_img, user_id,"na",view_count,"na","na");
								feedPojo.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
							}
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
}
