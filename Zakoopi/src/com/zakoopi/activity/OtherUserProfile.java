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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.Variables;
import com.zakoopi.user.model.User;
import com.zakoopi.user.model.UserDetails;
import com.zakoopi.user.model.UserInfoPojo;
import com.zakoopi.userfeed.model.OtherUserFeedAdapter;
import com.zakoopi.userfeed.model.UserArticleData;
import com.zakoopi.userfeed.model.UserArticleImages;
import com.zakoopi.userfeed.model.UserFeed;
import com.zakoopi.userfeed.model.UserFeedPojo;
import com.zakoopi.userfeed.model.UserStoreData;
import com.zakoopi.userfeed.model.UserStoreReviewData;
import com.zakoopi.userfeed.model.User_LookbookData;
import com.zakoopi.userfeed.model.User_lookbook_Cards;
import com.zakoopi.userfeed.model.feedTimeline;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class OtherUserProfile extends Activity {

	CircleImageView img_profile;
	View v1, v2, v3;
	TextView txt_user_name, txt_user_age, txt_user_gender, txt_user_city,
			txt_user_zakoopi_points, txt_user_look_count,
			txt_user_review_count, txt_user_like_count, user_activity,
			txt_zakoopi_point, txt_lookbook, txt_review, txt_like;
	private DisplayImageOptions options;
	private SharedPreferences pro_user_pref;
	private String USER_INFO_REST_URL = "";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String user_email, user_password, user_id, pro_user_name;
	String user_age, user_gender, user_city, zakoopi_points, lookbook_count,lookbook_draft_count,
			review_count, like_count, fb_link, twitter_link, website_link, uid;
	RelativeLayout rel_back;
	TextView header_user_name;

	public String url_img, user_name, other_user_id;
	ImageView img_fb, img_twitter, img_website, img_edit_profile,
			img_question_mark;
	View view_fb, view_twitter;
	RelativeLayout rel_social;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	LinearLayout lin_info;
	/**
	 * Feed Parameter
	 */
	private OtherUserFeedAdapter adapter;
	private EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	private String USER_ACTIVITY_REST_URL = " ";
	int page = 1;
	Dialog diaog;
	public ArrayList<UserFeedPojo> feedPojo1 = null;
	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	UserDetails main_user;
	RelativeLayout rel_lookbooks, rel_reviews, rel_draft;
	TextView txt_lookbooks, txt_reviews;
	ProgressBar progressBar1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	RelativeLayout rel_point;
	UserInfoPojo uip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.other_user_profile_main_layout);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		client = ClientHttp.getInstance(OtherUserProfile.this);
		cd = new ConnectionDetector(getApplicationContext());
		try {

			Intent data = getIntent();
			user_id = data.getStringExtra("user_id");
			//Log.e("GET_ID", user_id);
			Variables.user_id_list.add(user_id);

			pro_user_pref = getSharedPreferences("User_detail", 0);
			// user_id1 = pro_user_pref.getString("user_id", "adajfh");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");

			/**
			 * Typeface
			 */
			typeface_semibold = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Semibold.ttf");
			typeface_black = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Black.ttf");
			typeface_bold = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Bold.ttf");
			typeface_light = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Light.ttf");
			typeface_regular = Typeface.createFromAsset(getAssets(),
					"fonts/SourceSansPro-Regular.ttf");

		} catch (Exception e) {
			// TODO: handle exception
		}

		// findId(view);
		View profile_header = LayoutInflater.from(getApplicationContext())
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
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		header_user_name = (TextView) findViewById(R.id.txt);
		/*
		 * txt_user_look_count = (TextView) profile_header
		 * .findViewById(R.id.txt_lookbook_count);
		 */
		/*
		 * txt_user_review_count = (TextView) profile_header
		 * .findViewById(R.id.txt_review_count);
		 */
		rel_draft = (RelativeLayout) profile_header
				.findViewById(R.id.rel_draft);
		rel_lookbooks = (RelativeLayout) profile_header
				.findViewById(R.id.rel_lookbooks);
		rel_reviews = (RelativeLayout) profile_header
				.findViewById(R.id.rel_reviews);
		txt_user_zakoopi_points = (TextView) profile_header
				.findViewById(R.id.txt_point);
		txt_zakoopi_point = (TextView) profile_header
				.findViewById(R.id.txt_zakoopi_point);
		rel_draft.setVisibility(View.GONE);
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

		txt_lookbooks.setTypeface(typeface_semibold);
		txt_reviews.setTypeface(typeface_semibold);
		header_user_name.setTypeface(typeface_semibold);
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
		endlessListView = (EndlessListView) findViewById(R.id.endlessListView_activity);
		
		
		endlessListView.addHeaderView(profile_header);
		endlessListView.setOnLoadMoreListener(loadMoreListener);

		USER_ACTIVITY_REST_URL = getString(R.string.base_url)
				+ "feedTimeline/byUserId/" + user_id + ".json?page=";

		//Log.e("OTHER", USER_ACTIVITY_REST_URL);
		user_info();
		// activity_feed(page);
		// social_click();

		img_edit_profile.setVisibility(View.GONE);

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Variables.user_id_list.remove(Variables.user_id_list.size() - 1);
				finish();
			}
		});

		rel_point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(OtherUserProfile.this, ZakoopiPoints.class);
				startActivity(i);
			}
		});
		


		rel_lookbooks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 isInternetPresent = cd.isConnectingToInternet();
	             // check for Internet status
	                if (isInternetPresent) {
	                    // Internet Connection is Present
	                	Intent i = new Intent(OtherUserProfile.this,
	    						ProflieLookbooks.class);
	    				i.putExtra("user_img", url_img);
	    				i.putExtra("user_name", user_name);
	    				i.putExtra("user_id", user_id);
	    				startActivity(i);
	                } else {
	                    // Internet connection is not present
	                    // Ask user to connect to Internet
	                    showAlertDialog(OtherUserProfile.this, "No Internet Connection",
	                            "You don't have internet connection.", false);
	                }
				
				
				
			}
		});

		rel_reviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				 isInternetPresent = cd.isConnectingToInternet();
	             // check for Internet status
	                if (isInternetPresent) {
	                    // Internet Connection is Present
	                	Intent i = new Intent(OtherUserProfile.this,
	    						ProfileReviews.class);
	    				i.putExtra("user_id", user_id);
	    				startActivity(i);
	                } else {
	                    // Internet connection is not present
	                    // Ask user to connect to Internet
	                    showAlertDialog(OtherUserProfile.this, "No Internet Connection",
	                            "You don't have internet connection.", false);
	                }
	                
	                
				
			}
		});
		social_click();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(OtherUserProfile.this).reportActivityStop(
				OtherUserProfile.this);
	}

	@Override
	public void onResume() {

		super.onResume();
		/*
		 * //if (Variables.myact.equals("otherLook") && (feedPojo1.size() > 0))
		 * { if (adapter != null && (feedPojo1.size() > 0)) { Log.e("IFFF",
		 * "IFFF"); adapter.notifyDataSetChanged(); user_id =
		 * Variables.user_id_list.get(Variables.user_id_list.size()-1); // page
		 * = LookbookView1.mypage1; // progressBar1.setVisibility(View.GONE);
		 * Variables.myact = "noway"; } else { Log.e("IFELS", "ELSE");
		 * 
		 * }
		 */

	}

	public void user_info() {

		USER_INFO_REST_URL = getString(R.string.base_url) + "users/view/"
				+ user_id + ".json";
//Log.e("OTHER_USER", USER_INFO_REST_URL);
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
			//	Log.e("FAIL1", "FAIL");
				try {
					BufferedReader bufferedReader1 = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									errorResponse)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader1.readLine()) != null) {

						text2 = text2 + text1;
					}
				//	Log.e("USER_INFo", text2);
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
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				header_user_name.setText(uip.getUser_name());
				user_activity.setText(uip.getUser_name() + "'s Activity");
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

				Picasso.with(OtherUserProfile.this).load(url_img)
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


				progressBar1.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
				progressBar1.setVisibility(View.GONE);
			}
			social_condition();

			activity_feed(page);
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
			//	Log.e("FB", "https://facebook.com/" + uid);
				Tracker t = ((UILApplication) OtherUserProfile.this
						.getApplication()).getTracker(TrackerName.APP_TRACKER);
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

				Tracker t = ((UILApplication) OtherUserProfile.this
						.getApplication()).getTracker(TrackerName.APP_TRACKER);
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
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse(website_link));
				startActivity(i);

				Tracker t = ((UILApplication) OtherUserProfile.this
						.getApplication()).getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Website")
						.setAction("clicked Website  by" + pro_user_name)
						.setLabel("Website of" + user_name).build());
			}
		});

	}

	/**
	 * @void loadMoreData()
	 */
	private void loadMoreData() {

		user_id = Variables.user_id_list.get(Variables.user_id_list.size() - 1);
		USER_ACTIVITY_REST_URL = getString(R.string.base_url)
				+ "feedTimeline/byUserId/" + user_id + ".json?page=";

		page++;
		//Log.e("urlmmm", USER_ACTIVITY_REST_URL + "=====" + page);

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
	//	Log.e("url_MORE", USER_ACTIVITY_REST_URL + page + "&_=" + time);

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(USER_ACTIVITY_REST_URL + page + "&_=" + time,

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
						/*Log.e("ggggg", e.getMessage() + "vvvvvv   "
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
		GoogleAnalytics.getInstance(OtherUserProfile.this).reportActivityStart(
				OtherUserProfile.this);

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
		//Log.e("FEED_USER", "" + feeds_user);
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

			feedPojo1 = new ArrayList<UserFeedPojo>();
			feedPojo1.clear();
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
											user_name, url_img, user_id,"na",view_count,"na","na");
									feedPojo1.add(pp);

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
											user_name, url_img, user_id,"na",view_count,"na","na");
									feedPojo1.add(pp);

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
											user_name, url_img, user_id,"na",view_count,"na","na");
									feedPojo1.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
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
											user_name, url_img, user_id,hits_count,"na","na","na");
									feedPojo1.add(pp);

									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);

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
											user_name, url_img, user_id,hits_count,"na","na","na");
									feedPojo1.add(pp);
									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
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
											user_name, url_img, user_id,hits_count,"na","na","na");
									feedPojo1.add(pp);
									int rnd = new Random()
											.nextInt(colors.length);
									colorlist.add(colors[rnd]);
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
										user_name, url_img, user_id,hits_count,"na","na",store_id);
								feedPojo1.add(pp);
								colorlist.add(R.color.tabscolor);

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

			adapter = new OtherUserFeedAdapter(OtherUserProfile.this,
					feedPojo1, colorlist, url_img, user_name);
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
		//Log.e("FEED_USER", "" + feeds_user);
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

			feedPojo1 = new ArrayList<UserFeedPojo>();
			feedPojo1.clear();
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
										url_img, user_id,"na",view_count,"na","na");
								feedPojo1.add(pp);

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
										medium_img_w, medium_img_h, user_name,
										url_img, user_id,"na",view_count,"na","na");
								feedPojo1.add(pp);

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
										medium_img_w, medium_img_h, user_name,
										url_img, user_id,"na",view_count,"na","na");
								feedPojo1.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
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
										url_img, user_id,hits_count,"na","na","na");
								feedPojo1.add(pp);

								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);

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
										url_img, user_id,hits_count,"na","na","na");
								feedPojo1.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
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
										url_img, user_id,hits_count,"na","na","na");
								feedPojo1.add(pp);
								int rnd = new Random().nextInt(colors.length);
								colorlist.add(colors[rnd]);
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
									url_img, user_id,hits_count,"na","na",store_id);
							feedPojo1.add(pp);
							colorlist.add(R.color.tabscolor);

						} catch (Exception e) {
						}
					}
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			adapter.addItems(feedPojo1);
			adapter.addColors(colorlist);
			endlessListView.loadMoreCompleat();

			endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			endlessListView.setVerticalScrollBarEnabled(false);

		}

	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title, String message, Boolean status) {
	 AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("No Internet Connection!");
 
        // Setting Dialog Message
        alertDialog.setMessage("Enable Internet Connection.");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_SETTINGS);
            	context.startActivity(intent);
            	 dialog.cancel();
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	finish();
            dialog.cancel();
            }
        });
	 
	        // Showing Alert Message
	        alertDialog.show();
	    }

}
