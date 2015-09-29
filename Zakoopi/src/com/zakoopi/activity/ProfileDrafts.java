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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.fragments.HomeProfileFrag;
import com.zakoopi.userfeed.model.DraftFeedData;
import com.zakoopi.userfeed.model.DraftModel;
import com.zakoopi.userfeed.model.UserFeedPojo;
import com.zakoopi.userfeed.model.User_LookbookData;
import com.zakoopi.userfeed.model.User_lookbook_Cards;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.ProfileDraftAdapter;

public class ProfileDrafts extends Activity{

	private ProfileDraftAdapter adapter;
	RelativeLayout rel_back;
	TextView txt_back;
	private static String USER_FEED_DRAFT_REST_URL = "";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	 EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	List<DraftFeedData> draft_feed_list = null;
	public ArrayList<UserFeedPojo> feedPojo_draft = null;
	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	private SharedPreferences pro_user_pref, pref_location;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password,city_name;
	public int draftpage = 1;
	ProgressBar progressBar1;
	public static String idd;
	Typeface typeface_semibold, typeface_black, typeface_bold;
	String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_activityes);
		
		try {
			pref_location = getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "123");

			client = ClientHttp.getInstance(ProfileDrafts.this);
			// Log.e("NAME_CITY", city_name);

			Intent i = getIntent();
			user_id = i.getStringExtra("user_id");
			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = getSharedPreferences("User_detail", 0);
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
					+ pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref.getString("user_location", "4267");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
			
			USER_FEED_DRAFT_REST_URL = getString(R.string.base_url)
					+ "Lookbooks/getMyDrafts/" + user_id + ".json";
			
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
		txt_back.setText("My Draft");
		endlessListView.setOnLoadMoreListener(loadMoreListener);
		draft_feed(draftpage);
		
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * @void loadMoreData()
	 *//*
	private void loadMoreData() {
		mHaveMoreDataToLoad = true;
		draftpage++;
		//MainActivity.mypage++;
		draft_loadmoreFeed(draftpage);

	}*/

	/**
	 * EndlessListview LodeMoreListener
	 */
	private EndlessListView.OnLoadMoreListener loadMoreListener = new EndlessListView.OnLoadMoreListener() {

		@Override
		public boolean onLoadMore() {
			if (true == mHaveMoreDataToLoad) {

			//	loadMoreData();

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
	 * 
	 * DRAFT
	 */

	public void draft_feed(int draftpage) {
		long time = System.currentTimeMillis();

		client.setBasicAuth(user_email, user_password);
//Log.e("URL", USER_FEED_DRAFT_REST_URL);
		//client.get(USER_FEED_DRAFT_REST_URL + draftpage + "&_=" + time,
client.get(USER_FEED_DRAFT_REST_URL,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						progressBar1.setVisibility(View.VISIBLE);
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
						//		Log.e("DRAFT1111", text);
							}
							// mHaveMoreDataToLoad = true;
						//	Log.e("DRAFT", text);
							showDataDraft(text);
							

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
	public void draft_loadmoreFeed(int draftpage) {
		long time = System.currentTimeMillis();

		// client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(USER_FEED_DRAFT_REST_URL + draftpage + "&_=" + time,

		// client.get("http://v3dev.zakoopi.com/api/Start/getClientLocation.json",
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
					//	lin_pro.setVisibility(View.VISIBLE);
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
							// mHaveMoreDataToLoad = true;
							//Log.e("jjjjj", st1);
							showmoreDataDraft(st1);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						// called when response HTTP status is "4XX" (eg. 401,
						// 403, 404)
					//	lin_pro.setVisibility(View.GONE);
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
	 * Popular Feed Show Data
	 * 
	 * @showData data
	 */
	@SuppressWarnings("unchecked")
	public void showDataDraft(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		DraftModel draftFeed = gson.fromJson(reader, DraftModel.class);
		List<DraftFeedData> feeds_draft = draftFeed.getData();
		new MyAppDraft().execute(feeds_draft);

	}

	/**
	 * MyApp extends AsyncTask<List<popularfeed>, Void, Void> for Showdata(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyAppDraft extends AsyncTask<List<DraftFeedData>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<DraftFeedData>... params) {
			draft_feed_list = new ArrayList<DraftFeedData>();
			draft_feed_list.clear();
			draft_feed_list = params[0];
			feedPojo_draft = new ArrayList<UserFeedPojo>();
			feedPojo_draft.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			if (draft_feed_list.size() > 0) {

				for (int i = 0; i < draft_feed_list.size(); i++) {
					DraftFeedData ft = draft_feed_list.get(i);

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
									 idd = look.getId();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Lookbooks", lookimg, img1, img2,
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", "na", comment_count,
											medium_img_w, medium_img_h,
											HomeProfileFrag.user_name,
											HomeProfileFrag.url_img, user_id,"na",view_count,"na","na");
									feedPojo_draft.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
								/*	lin_draft.addView(add_look_article(i,
											feedPojo_draft));*/
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

									 idd = look.getId();
									String is_liked = look.getIs_liked();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Lookbooks", lookimg, img1, "na",
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", "na", comment_count,
											medium_img_w, medium_img_h,
											HomeProfileFrag.user_name,
											HomeProfileFrag.url_img, user_id,"na",view_count,"na","na");
									feedPojo_draft.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
									/*lin_draft.addView(add_look_article(i,
											feedPojo_draft));*/
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

									 idd = look.getId();
									String is_liked = look.getIs_liked();
									String slug = look.getSlug();
									UserFeedPojo pp = new UserFeedPojo(
											"Lookbooks", lookimg, "na", "na",
											likes, hits, title, "na", "na",
											"na", String.valueOf(cards.size()),
											idd, description, is_liked, slug,
											"na", "na", "na", comment_count,
											medium_img_w, medium_img_h,
											HomeProfileFrag.user_name,
											HomeProfileFrag.url_img, user_id,"na",view_count,"na","na");
									feedPojo_draft.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
									/*lin_draft.addView(add_look_article(i,
											feedPojo_draft));*/
								}
							} catch (Exception e) {

							}

						}
					}

				}
				// user_activity.setVisibility(View.VISIBLE);
			} else {
				// lin_activity.addView(no_recent());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			
			
			adapter = new ProfileDraftAdapter(ProfileDrafts.this, feedPojo_draft, colorlist);
			endlessListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			progressBar1.setVisibility(View.GONE);
		//	mHaveMoreDataToLoad = true;
			endlessListView.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * Popular More Feed Show More Data
	 * 
	 * @showmoreData data
	 */
	@SuppressWarnings("unchecked")
	public void showmoreDataDraft(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		DraftModel draftFeed = gson.fromJson(reader, DraftModel.class);
		List<DraftFeedData> feeds_draft = draftFeed.getData();

		new MyAppDraftView().execute(feeds_draft);
	}

	/**
	 * MyApp1 extends AsyncTask<List<popularfeed>, Void, Void> for
	 * showmoreData(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyAppDraftView extends
			AsyncTask<List<DraftFeedData>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<DraftFeedData>... params) {
	
			draft_feed_list.clear();
			draft_feed_list = params[0];
			feedPojo_draft = new ArrayList<UserFeedPojo>();
			feedPojo_draft.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < draft_feed_list.size(); i++) {
				DraftFeedData ft = draft_feed_list.get(i);

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
										medium_img_w, medium_img_h,
										HomeProfileFrag.user_name,
										HomeProfileFrag.url_img, user_id,"na",view_count,"na","na");
								feedPojo_draft.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

								/*lin_draft.addView(add_look_article(i,
										feedPojo_draft));*/
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
										medium_img_w, medium_img_h,
										HomeProfileFrag.user_name,
										HomeProfileFrag.url_img, user_id,"na",view_count,"na","na");
								feedPojo_draft.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								/*lin_draft.addView(add_look_article(i,
										feedPojo_draft));*/
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
										medium_img_w, medium_img_h,
										HomeProfileFrag.user_name,
										HomeProfileFrag.url_img, user_id,"na",view_count,"na","na");
								feedPojo_draft.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
								/*lin_draft.addView(add_look_article(i,
										feedPojo_draft));*/
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
			adapter.addItems(feedPojo_draft);
			adapter.addColors(colorlist);
			endlessListView.loadMoreCompleat();

			endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			endlessListView.setVerticalScrollBarEnabled(false);
		//	lin_pro.setVisibility(View.GONE);
		}

	}
}
