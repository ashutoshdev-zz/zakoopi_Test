package com.mystores;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.loopj.android.http.RequestParams;
import com.mycam.ImageDetail;
import com.mycam.LookBookTabsActivity;
import com.mycam.Lookbookpublish;
import com.store.model.MyDataModel;
import com.store.model.MyRateModel;
import com.store.model.MyRateReviewModel;
import com.store.model.MyReviewModel;
import com.zakoopi.R;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.fragments.HomeProfileFrag;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class ReviewMe extends FragmentActivity {
	RelativeLayout rel_publish, rel_back;
	private SharedPreferences pro_user_pref;
	private String user_email;
	private String user_password;
	private String user_id;
	TextView ratecount;
	boolean rate_bool = false;
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	EditText etMessageBox;
	TextView txt_error1, txt_submit, txt_back, review_cat;
	View view_1, view_2, view_3, view_4, view_5, view_6, view_7, view_8,
			view_9;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	String self_rate, self_review1, review_store_id;
	
	String rate_rev_user_id, store_review_id, my_review, my_rating,
			rate_intent;
ProgressBar progressBar1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.write_review);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		client = ClientHttp.getInstance(ReviewMe.this);

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Store Give Review");
		t.send(new HitBuilders.AppViewBuilder().build());

		pro_user_pref = getSharedPreferences("User_detail", 0);
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
		user_id = pro_user_pref.getString("user_id", "0");

		Intent intent = getIntent();
		// self_rate = intent.getStringExtra("self_rate");
		rate_intent = intent.getStringExtra("rate");
		review_store_id = intent.getStringExtra("store_id");
		MyRate();
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

		rel_publish = (RelativeLayout) findViewById(R.id.publish);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		ratecount = (TextView) findViewById(R.id.textView3);
		etMessageBox = (EditText) findViewById(R.id.etMessageBox);
		// counter = (TextView) findViewById(R.id.txt_counter);
		txt_error1 = (TextView) findViewById(R.id.txt_error1);
		ratecount = (TextView) findViewById(R.id.txt_rate);
		txt_submit = (TextView) findViewById(R.id.txt_submit);
		txt_back = (TextView) findViewById(R.id.txt_back);
		review_cat = (TextView) findViewById(R.id.review_cat);
		ratecount.setTypeface(typeface_semibold);
		review_cat.setTypeface(typeface_regular);
		txt_submit.setTypeface(typeface_semibold);
		etMessageBox.setTypeface(typeface_regular);
		txt_back.setTypeface(typeface_semibold);
		txt_error1.setTypeface(typeface_bold);
		// counter.setTypeface(typeface_semibold);
		txt_back.setText("My Review");

		view_1 = (View) findViewById(R.id.view_1);
		view_2 = (View) findViewById(R.id.view_2);
		view_3 = (View) findViewById(R.id.view_3);
		view_4 = (View) findViewById(R.id.view_4);
		view_5 = (View) findViewById(R.id.view_5);
		view_6 = (View) findViewById(R.id.view_6);
		view_7 = (View) findViewById(R.id.view_7);
		view_8 = (View) findViewById(R.id.view_8);
		view_9 = (View) findViewById(R.id.view_9);

		click();

		/*InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(250);
		etMessageBox.setFilters(FilterArray);*/

		etMessageBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				// counter.setText(s.length() + "/250");

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(ReviewMe.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(ReviewMe.this).reportActivityStop(this);
	}

	public void click() {

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		rel_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((ratecount.getText().toString().equals("0.0"))
						&& ((etMessageBox.getText().length()) < 140)) {

					txt_error1.setVisibility(View.VISIBLE);
					txt_error1
							.setText("Please rate the store & Write your review with minimum 140 characters...");

				} else if ((!ratecount.getText().toString().equals("0.0"))
						&& ((etMessageBox.getText().length()) < 140)) {
					txt_error1.setVisibility(View.VISIBLE);
					txt_error1
							.setText("Please Write your review with minimum 140 characters...");
				} else if ((ratecount.getText().toString().equals("0.0"))
						&& ((etMessageBox.getText().length()) > 140)) {
					txt_error1.setVisibility(View.VISIBLE);
					txt_error1.setText("Please rate the store...");
				} else {
					txt_error1.setVisibility(View.GONE);
					RateReview(ratecount.getText().toString().trim());

					Tracker t = ((UILApplication) getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Review Publish")
							.setAction(
									"Review Published by "
											+ MainFragment.pro_user_name)
							.setLabel("Review Me").build());
				}

			}
		});

		view_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (rate_bool == false) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_base);
					view_3.setBackgroundResource(R.drawable.rating_box_base);
					view_4.setBackgroundResource(R.drawable.rating_box_base);
					view_5.setBackgroundResource(R.drawable.rating_box_base);
					view_6.setBackgroundResource(R.drawable.rating_box_base);
					view_7.setBackgroundResource(R.drawable.rating_box_base);
					view_8.setBackgroundResource(R.drawable.rating_box_base);
					view_9.setBackgroundResource(R.drawable.rating_box_base);
					ratecount.setText("1.0");
					rate_bool = true;
				} /*else {

					view_1.setBackgroundResource(R.drawable.rating_box_base);
					view_2.setBackgroundResource(R.drawable.rating_box_base);
					view_3.setBackgroundResource(R.drawable.rating_box_base);
					view_4.setBackgroundResource(R.drawable.rating_box_base);
					view_5.setBackgroundResource(R.drawable.rating_box_base);
					view_6.setBackgroundResource(R.drawable.rating_box_base);
					view_7.setBackgroundResource(R.drawable.rating_box_base);
					view_8.setBackgroundResource(R.drawable.rating_box_base);
					view_9.setBackgroundResource(R.drawable.rating_box_base);
					ratecount.setText("0.0");
					rate_bool = false;
				}*/
			}
		});

		view_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_base);
				view_4.setBackgroundResource(R.drawable.rating_box_base);
				view_5.setBackgroundResource(R.drawable.rating_box_base);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("1.5");
				rate_bool = false;
			}
		});

		view_3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_base);
				view_5.setBackgroundResource(R.drawable.rating_box_base);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("2.0");
				rate_bool = false;
			}
		});

		view_4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_base);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("2.5");
				rate_bool = false;
			}
		});

		view_5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("3.0");
				rate_bool = false;
			}
		});

		view_6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);

				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("3.5");
				rate_bool = false;
			}
		});

		view_7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("4.0");
				rate_bool = false;
			}
		});

		view_8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_4_1);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("4.5");
				rate_bool = false;
			}
		});

		view_9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.mycolor);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_4_1);
				view_9.setBackgroundResource(R.drawable.rating_box_4_6);
				ratecount.setText("5.0");
				rate_bool = false;
			}
		});
	}

	/*
	 * RateReview data for editing user profile
	 */

	@SuppressWarnings("deprecation")
	public void RateReview(String rate) {
		String profile_URL = getResources().getString(R.string.base_url)
				+ "Common/storeRateNReview/" + user_id + ".json";
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {

			if (rate_intent.equals("main_fragment")) {
				params.put("d[0][store_id]", StoreActivity.store_id);
				params.put("d[0][review]", etMessageBox.getText().toString());
				params.put("d[0][rating]", rate);
				params.put("d[0][with_review]", "1");
			} else if (rate_intent.equals("user_feed")) {
				params.put("d[0][store_id]", review_store_id);
				params.put("d[0][review]", etMessageBox.getText().toString());
				params.put("d[0][rating]", rate);
				params.put("d[0][with_review]", "1");
			}else {
				params.put("d[0][store_id]", review_store_id);
				params.put("d[0][review]", etMessageBox.getText().toString());
				params.put("d[0][rating]", rate);
				params.put("d[0][with_review]", "1");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		client.post(profile_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				customDailog();
				/*if (rate_intent.equals("main_fragment")) {
					Intent in = new Intent(ReviewMe.this, StoreActivity.class);
					in.putExtra("store_id", StoreActivity.store_id);
					startActivity(in);
					finish();
				} else {
					Intent in = new Intent(ReviewMe.this, MainActivity.class);
					startActivity(in);
					finish();
					
				}*/

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	/**
	 * 
	 * Rate & Review
	 * 
	 */

	public void MyRate() {

		String profile_URL = getResources().getString(R.string.base_url)
				+ "stores/getByStoreRatingUserID/" + review_store_id + ".json";
		
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {
			params.put("user_id", user_id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		client.post(profile_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
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

					show_review_rate(text2);
				} catch (Exception e) {

				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				progressBar1.setVisibility(View.GONE);
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});
	}

	public void show_review_rate(String data) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		MyRateReviewModel main = gson.fromJson(jsonReader,
				MyRateReviewModel.class);
		MyDataModel dm = main.getData();
		new MyModel().execute(dm);

	}

	private class MyModel extends AsyncTask<MyDataModel, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(MyDataModel... params) {
			MyDataModel dm = params[0];

			MyReviewModel rem = dm.getStoreReviews();
			if (rem == null) {
				
			} else {
				// rate_rev_user_id = rem.getUser_id();
				store_review_id = rem.getStore_id();
				my_review = rem.getReview();
			}

			MyRateModel rm = dm.getStoreRatings();
			if (rm == null) {
			} else {
				my_rating = rm.getRating();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {

				
				if (my_rating.equals("1.0")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					ratecount.setText("1.0");
					rate_bool = true;
				} else if (my_rating.equals("1.5")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					ratecount.setText("1.5");
					rate_bool = false;
				} else if (my_rating.equals("2.0")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					ratecount.setText("2.0");
					rate_bool = false;
				} else if (my_rating.equals("2.5")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					view_4.setBackgroundResource(R.drawable.rating_box_2_1);
					ratecount.setText("2.5");
					rate_bool = false;
				} else if (my_rating.equals("3.0")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					view_4.setBackgroundResource(R.drawable.rating_box_2_1);
					view_5.setBackgroundResource(R.drawable.rating_box_2_6);
					ratecount.setText("3.0");
					rate_bool = false;
				} else if (my_rating.equals("3.5")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					view_4.setBackgroundResource(R.drawable.rating_box_2_1);
					view_5.setBackgroundResource(R.drawable.rating_box_2_6);
					view_6.setBackgroundResource(R.drawable.rating_box_3_1);
					ratecount.setText("3.5");
					rate_bool = false;
				} else if (my_rating.equals("4.0")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					view_4.setBackgroundResource(R.drawable.rating_box_2_1);
					view_5.setBackgroundResource(R.drawable.rating_box_2_6);
					view_6.setBackgroundResource(R.drawable.rating_box_3_1);
					view_7.setBackgroundResource(R.drawable.rating_box_3_6);
					ratecount.setText("4.0");
					rate_bool = false;
				} else if (my_rating.equals("4.5")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					view_4.setBackgroundResource(R.drawable.rating_box_2_1);
					view_5.setBackgroundResource(R.drawable.rating_box_2_6);
					view_6.setBackgroundResource(R.drawable.rating_box_3_1);
					view_7.setBackgroundResource(R.drawable.rating_box_3_6);
					view_8.setBackgroundResource(R.drawable.rating_box_4_1);
					ratecount.setText("4.5");
					rate_bool = false;
				} else if (my_rating.equals("5.0")) {
					view_1.setBackgroundResource(R.drawable.mycolor);
					view_2.setBackgroundResource(R.drawable.rating_box_1_1);
					view_3.setBackgroundResource(R.drawable.rating_box_1_6);
					view_4.setBackgroundResource(R.drawable.rating_box_2_1);
					view_5.setBackgroundResource(R.drawable.rating_box_2_6);
					view_6.setBackgroundResource(R.drawable.rating_box_3_1);
					view_7.setBackgroundResource(R.drawable.rating_box_3_6);
					view_8.setBackgroundResource(R.drawable.rating_box_4_1);
					view_9.setBackgroundResource(R.drawable.rating_box_4_6);
					ratecount.setText("5.0");
					rate_bool = false;
				}

				etMessageBox.setText(my_review);
				progressBar1.setVisibility(View.GONE);
			} catch (Exception e) {
				progressBar1.setVisibility(View.GONE);
			}

		}

	}

	public void customDailog() {

		final Dialog dd = new Dialog(ReviewMe.this);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setCancelable(false);
		dd.setContentView(R.layout.cust_review_dialog);
		dd.show();
	//	mProgressHUD.dismiss();
		// final ImageView img1 = (ImageView) dd.findViewById(R.id.imageView1);
		// final ImageView img2 = (ImageView) dd.findViewById(R.id.imageView2);
		final TextView txt_message = (TextView) dd
				.findViewById(R.id.txt_message);
		final RelativeLayout rel_upload = (RelativeLayout) dd
				.findViewById(R.id.rel_upload);
		final RelativeLayout rel_go_home = (RelativeLayout) dd
				.findViewById(R.id.rel_go_home);
		final TextView txt_upload = (TextView) dd.findViewById(R.id.txt_upload);
		final TextView txt_go_home = (TextView) dd
				.findViewById(R.id.txt_go_home);
		txt_message.setTypeface(typeface_semibold);
		txt_upload.setTypeface(typeface_bold);
		txt_go_home.setTypeface(typeface_bold);
		rel_upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dd.dismiss();
				if (rate_intent.equals("main_fragment")) {
					Intent in = new Intent(ReviewMe.this, StoreActivity.class);
					in.putExtra("store_id", StoreActivity.store_id);
					startActivity(in);
					finish();
					
				} else if (rate_intent.equals("user_feed")) {
					Intent in = new Intent(ReviewMe.this, StoreActivity.class);
					in.putExtra("store_id", review_store_id);
					startActivity(in);
					finish();
				}
			}
		});

		rel_go_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dd.dismiss();
				try {
					Intent in = new Intent(ReviewMe.this, MainActivity.class);
					startActivity(in);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

}
