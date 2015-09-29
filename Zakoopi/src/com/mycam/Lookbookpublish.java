package com.mycam;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cam.imagedatabase.DBHelper;
import com.cam.imagedatabase.PublishListAdapter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.image.effects.HorizontalListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zakoopi.R;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.activity.ProfileDrafts;
import com.zakoopi.fragments.HomeProfileFrag;
import com.zakoopi.helper.ProgressHUD;
import com.zakoopi.searchstore.model.StoreIdMainModel;
import com.zakoopi.searchstore.model.StoreTagPojo;
import com.zakoopi.searchstore.model.StoreidDetailModel;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class Lookbookpublish extends FragmentActivity implements
		OnCancelListener {

	HorizontalListView imagelist;
	ArrayList<byte[]> imageArry = new ArrayList<byte[]>();
	PublishListAdapter adapter;
	Button publish;
	EditText search_title;
	JSONArray js;
	boolean rate_bool = false;
	private SQLiteDatabase db;
	public static final String DBTABLE = "lookbook";
	long count;
	DBHelper hp;
	List<StoreidDetailModel> list  = new ArrayList<StoreidDetailModel>();
	long idd;
	ArrayList<File> files = new ArrayList<File>();
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	ProgressDialog bar;
	private static String LOOKBOOK_URL;
	ProgressHUD mProgressHUD;
	String title;
	JSONObject obj;
	private SharedPreferences pro_user_pref;
	private String user_email, pro_user_name;
	private String user_password;
	ArrayList<String> storeId = new ArrayList<String>();
	private String user_id;
	LinearLayout rate_list;
	ArrayList<StoreTagPojo> StoreTag = new ArrayList<StoreTagPojo>();
	ArrayList<String> idlist = new ArrayList<String>();
	ArrayList<String> ratelist = new ArrayList<String>();
	ArrayList<String> checkratelist = new ArrayList<String>();
	TextView txt_image_detail, txt_lookbook_title, txt_photostream,
			txt_rate_your, txt_error;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	RelativeLayout rel_back;
	ScrollView myScrollView;
	String intent_string, lookbook_title;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.lookbook_publish);
		client = ClientHttp.getInstance(Lookbookpublish.this);

		imageArry.clear();
		files.clear();
		StoreTag.clear();
		idlist.clear();
		ratelist.clear();
		checkratelist.clear();
		storeId.clear();
		list.clear();
		
		Intent in = getIntent();
		intent_string = in.getStringExtra("image_effect");
		lookbook_title = in.getStringExtra("lookbook_title");

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Lookbook Publish");
		t.send(new HitBuilders.AppViewBuilder().build());

		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = this.getSharedPreferences("User_detail", 0);

		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		user_id = pro_user_pref.getString("user_id", "asds");
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
		typeface_light = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		imagelist = (HorizontalListView) findViewById(R.id.listview);
		publish = (Button) findViewById(R.id.imageView1);
		search_title = (EditText) findViewById(R.id.edt_title);
		rate_list = (LinearLayout) findViewById(R.id.additem);
		txt_image_detail = (TextView) findViewById(R.id.txt_image_detail);
		txt_lookbook_title = (TextView) findViewById(R.id.txt_lookbook_title);
		txt_photostream = (TextView) findViewById(R.id.txt_photostream);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_rate_your = (TextView) findViewById(R.id.txt_rate_your);
		txt_error = (TextView) findViewById(R.id.txt_error);
		myScrollView = (ScrollView) findViewById(R.id.scrollView1);
		search_title.setTypeface(typeface_regular);
		txt_photostream.setTypeface(typeface_semibold);
		txt_image_detail.setTypeface(typeface_bold);
		txt_lookbook_title.setTypeface(typeface_bold);
		txt_rate_your.setTypeface(typeface_bold);
		pro_user_pref = getSharedPreferences("User_detail", 0);
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
		user_id = pro_user_pref.getString("user_id", "0");
		// Reading all contacts from database
		try {
			search_title.setText(lookbook_title);
		} catch (Exception e) {
			// TODO: handle exception
		}

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		try {
			hp = new DBHelper(Lookbookpublish.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(
					" select id,photo,tag,desc,storeid,slug from " + DBTABLE,
					null);
			count = c.getCount();
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						int id = c.getInt(c.getColumnIndex("id"));
						String st = c.getString(c.getColumnIndex("tag"));

						String st1 = c.getString(c.getColumnIndex("desc"));

						String st2 = c.getString(c.getColumnIndex("storeid"));
						String[] sid = st2.split(",");
						for (int i = 0; i < sid.length; i++) {
							storeId.add(sid[i]);
						}

						byte[] arr = c.getBlob(c.getColumnIndex("photo"));
						imageArry.add(arr);
						
					} while (c.moveToNext());
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}

		RateReview();

		adapter = new PublishListAdapter(Lookbookpublish.this, imageArry);
		imagelist.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String st = "true";
				title = search_title.getText().toString().trim();
				if ((title.length() >= 20) && (title.length() <= 120)) {
                  try{
					for (int i = 0; i < ratelist.size(); i++) {

						if (checkratelist.get(i).equals("false")) {

							txt_error.setVisibility(View.VISIBLE);
							st = "false";

						}

					}
                  }catch(Exception e){
                	  e.printStackTrace();
                  }
					if (st.equals("true")) {
						txt_error.setVisibility(View.GONE);
						RateReviewViews();
					}

					Tracker t = ((UILApplication) getApplication())
							.getTracker(TrackerName.APP_TRACKER);

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Lookbook Publish")
							.setAction("Click on Lookbook Publish")
							.setLabel("Lookbook Publish").build());

				} else {
					search_title.setError("Minimum 20 characters");
				}

			}
		});

		search_title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

				search_title.setError(null);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(Lookbookpublish.this).reportActivityStart(
				this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(Lookbookpublish.this).reportActivityStop(
				this);
	}

	/*
	 * RateReview data for editing user profile
	 */

	@SuppressWarnings("deprecation")
	public void RateReviewViews() {
		String profile_URL = getResources().getString(R.string.base_url)
				+ "Common/storeRateNReview/" + user_id + ".json";

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {

			for (int i = 0; i < ratelist.size(); i++) {
				params.put("d[" + i + "][store_id]", idlist.get(i));
				params.put("d[" + i + "][review]", "");
				params.put("d[" + i + "][rating]", ratelist.get(i));
				params.put("d[" + i + "][with_review]", "0");
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
				mProgressHUD = ProgressHUD.show(Lookbookpublish.this,
						"Processing...", true, true, Lookbookpublish.this);
				mProgressHUD.setCancelable(true);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				// wtire stuff here
				/**
				 * 
				 * Comment Edit
				 */
				
				try {
					if (ProfileDrafts.idd != null && !ProfileDrafts.idd.equals("")) {
						
						obj = getData1(search_title.getText().toString());
						upload1(obj);
					} else {
						
						obj = getData(search_title.getText().toString().trim());
						upload(obj);
					}
				} catch (Exception e) {
					
					e.printStackTrace();
			
				}
				

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				

				mProgressHUD.cancel();

			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	/*
	 * List of Stores which needed to be rated by the auth User
	 */

	@SuppressWarnings("deprecation")
	public void RateReview() {
		String profile_URL = getResources().getString(R.string.base_url)
				+ "Common/getListOfStoresForRating.json";

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {
			params.put("user_id", user_id);
			for (int i = 0; i < storeId.size(); i++) {
			
				params.put("store_ids[" + i + "]", storeId.get(i));
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

				try {
					String line = "";
					String text = "";
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = bufferedReader.readLine()) != null) {

						text = text + line;
					}
					// Log.e("hhhh", text);
					all_update(text);
				} catch (Exception e) {

				}

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

	@SuppressWarnings("unchecked")
	public void all_update(String data) {
		list = new ArrayList<StoreidDetailModel>();
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		StoreIdMainModel sid = gson
				.fromJson(jsonReader, StoreIdMainModel.class);
		ArrayList<StoreidDetailModel> msid = sid.getStore_ids();
		/*
		 * for (int i = 0; i < msid.size(); i++) {
		 * 
		 * Log.e("name", msid.get(i).getMarket_name()); }
		 */
		list.addAll(msid);

		new MyApp().execute();
	}

	/**
	 * MyApp extends AsyncTask<List<StoreidDetailModel>, Void, Void> for
	 * showmoreData(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(Void... params) {

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			idlist = new ArrayList<String>();
			ratelist = new ArrayList<String>();
			checkratelist = new ArrayList<String>();

			if (list.size() == 0) {
				txt_rate_your.setVisibility(View.GONE);
			} else {
				
				for (int i = 0; i < list.size(); i++) {

					String st1 = list.get(i).getMarket_name();
					String st2 = list.get(i).getStore_name();
					String st3 = list.get(i).getStore_id();

					rate_list.addView(AddView(st2, st1, st3, i));
					
				}
			}

		}

	}

	public View AddView(String name, String mname, final String id,
			final int pos) {

		ratelist.add(String.valueOf(pos));
		idlist.add(id);
		checkratelist.add("false");

		LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inf.inflate(R.layout.lookbook_rate_view_items, null);
		final TextView storename = (TextView) view.findViewById(R.id.textView1);
		final TextView marketname = (TextView) view
				.findViewById(R.id.textView3);
		final TextView ratecount = (TextView) view.findViewById(R.id.txt_rate);
		final View view_1 = (View) view.findViewById(R.id.view_1);
		final View view_2 = (View) view.findViewById(R.id.view_2);
		final View view_3 = (View) view.findViewById(R.id.view_3);
		final View view_4 = (View) view.findViewById(R.id.view_4);
		final View view_5 = (View) view.findViewById(R.id.view_5);
		final View view_6 = (View) view.findViewById(R.id.view_6);
		final View view_7 = (View) view.findViewById(R.id.view_7);
		final View view_8 = (View) view.findViewById(R.id.view_8);
		final View view_9 = (View) view.findViewById(R.id.view_9);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
			}
		});
		try {
			storename.setTypeface(typeface_bold);
			marketname.setTypeface(typeface_regular);
			ratecount.setTypeface(typeface_semibold);
			txt_rate_your.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			// TODO: handle exception
		}
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
					ratelist.set(pos, ratecount.getText().toString());
					checkratelist.set(pos, "true");
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(), 0);

				} else {

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
					ratelist.set(pos, ratecount.getText().toString());
					checkratelist.set(pos, "false");
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(), 0);
				}
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
				ratelist.set(pos, ratecount.getText().toString());
				checkratelist.set(pos, "true");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				ratelist.set(pos, ratecount.getText().toString());
				checkratelist.set(pos, "true");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				checkratelist.set(pos, "true");
				ratelist.set(pos, ratecount.getText().toString());
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				checkratelist.set(pos, "true");
				ratelist.set(pos, ratecount.getText().toString());
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				checkratelist.set(pos, "true");
				ratelist.set(pos, ratecount.getText().toString());
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				checkratelist.set(pos, "true");
				ratelist.set(pos, ratecount.getText().toString());
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				checkratelist.set(pos, "true");
				ratelist.set(pos, ratecount.getText().toString());
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);
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
				checkratelist.set(pos, "true");
				ratelist.set(pos, ratecount.getText().toString());
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);

			}
		});

		storename.setText(name);
		marketname.setText(mname);
		return view;

	}

	public JSONObject getData(String title) {

		JSONArray jsonArray = new JSONArray();
		JSONObject finalobject = new JSONObject();
		try {
			hp = new DBHelper(Lookbookpublish.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(
					" select id,photo,tag,desc,imagepath,storeid,slug from "
							+ DBTABLE, null);
			count = c.getCount();

			if (c != null) {
				while (c.moveToNext()) {
					JSONObject obj = new JSONObject();
					int id = c.getInt(c.getColumnIndex("id"));
					String st = c.getString(c.getColumnIndex("tag"));
					String st1 = c.getString(c.getColumnIndex("desc"));
					String path = c.getString(c.getColumnIndex("imagepath"));
					String storeid = c.getString(c.getColumnIndex("storeid"));
					String storeslug = c.getString(c.getColumnIndex("slug"));
					byte[] arr = c.getBlob(c.getColumnIndex("photo"));
					try {
						files.add(new File(path));
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						// obj.put("id", id);
						obj.put("storetag", storeslug);
						obj.put("description", st1);
						obj.put("photo_path", path);
						obj.put("storeid", storeid);
						jsonArray.put(obj);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				try {

					JSONObject myobj = new JSONObject();
					myobj.put("title", title);
					myobj.put("draft", "0");
					myobj.put("user_id", user_id);
					myobj.put("cards", jsonArray);

					finalobject.put("lookbook", myobj);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return finalobject;

	}

	@SuppressWarnings("deprecation")
	public void upload(final JSONObject arr) {
		LOOKBOOK_URL = getResources().getString(R.string.base_url)
				+ "lookbooks/add.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("jsonData", arr);

		try {
			for (int i = 0; i < files.size(); i++) {
				params.put("files[" + files.get(i).getName() + "]",
						files.get(i));
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				// mProgressHUD = ProgressHUD.show(Lookbookpublish.this,
				// "Processing...", true, true, Lookbookpublish.this);
				// mProgressHUD.setCancelable(false);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
			
				try {
					
				
				DBHelper hp = new DBHelper(Lookbookpublish.this);
				db = hp.getWritableDatabase();
				// search_title.setText("");
				db.delete(DBTABLE, null, null);
				customDailog();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				mProgressHUD.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void customDailog() {

		final Dialog dd = new Dialog(Lookbookpublish.this);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setCancelable(false);
		dd.setContentView(R.layout.custom_dialog);
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
		
		try {
			
		
		txt_message.setTypeface(typeface_semibold);
		txt_upload.setTypeface(typeface_bold);
		txt_go_home.setTypeface(typeface_bold);
		} catch (Exception e) {
			// TODO: handle exception
		}
		rel_upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dd.dismiss();
				/**
				 * 
				 * Comment Edit
				 */
				try {
					ProfileDrafts.idd = "";
				} catch (Exception e) {
					e.printStackTrace();
				}

				ImageDetail.lookbook_title = "";
				Intent in = new Intent(Lookbookpublish.this,
						LookBookTabsActivity.class);
				startActivity(in);
				
				finish();
			}
		});

		rel_go_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dd.dismiss();
				/**
				 * 
				 * Comment Edit
				 */
				try {
					ProfileDrafts.idd = "";
				} catch (Exception e) {
					e.printStackTrace();
				}
				ImageDetail.lookbook_title = "";
				Intent feed = new Intent(Lookbookpublish.this,
						MainActivity.class);
				feed.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(feed);
				
				finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mProgressHUD.dismiss();
	}

	/**
	 * 
	 * Draft Lookbook
	 */

	public JSONObject getData1(String title) {

		JSONArray jsonArray = new JSONArray();
		JSONObject finalobject = new JSONObject();
		try {
			hp = new DBHelper(Lookbookpublish.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(
					" select id,photo,tag,desc,imagepath,mypath,card_id,storeid,slug from "
							+ DBTABLE, null);
			count = c.getCount();

			if (c != null) {
				while (c.moveToNext()) {
					JSONObject obj = new JSONObject();
					int id = c.getInt(c.getColumnIndex("id"));
					String st = c.getString(c.getColumnIndex("tag"));
					String st1 = c.getString(c.getColumnIndex("desc"));
					String path = c.getString(c.getColumnIndex("imagepath"));
					String mypath = c.getString(c.getColumnIndex("mypath"));
					String card_id = c.getString(c.getColumnIndex("card_id"));
					String storeid = c.getString(c.getColumnIndex("storeid"));
					String storeslug = c.getString(c.getColumnIndex("slug"));
					byte[] arr = c.getBlob(c.getColumnIndex("photo"));
                    if(path==null||path.equals("no")){
						files.add(null);
						
					} else {

						files.add(new File(path));
						
					}

					if (path.equals("no")) {
						try {
							obj.put("id", card_id);
							obj.put("storetag", storeslug);
							obj.put("description", st1);
							obj.put("photo_path", mypath);
							obj.put("storeid", storeid);
							jsonArray.put(obj);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {

						try {
							// obj.put("id", id);
							obj.put("storetag", storeslug);
							obj.put("description", st1);
							obj.put("photo_path", path);
							jsonArray.put(obj);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {

					JSONObject myobj = new JSONObject();
					myobj.put("title", title);
					myobj.put("draft", "0");
					myobj.put("user_id", user_id);
					myobj.put("cards", jsonArray);
					/**
					 * 
					 * Comment Edit
					 */
					myobj.put("id", ProfileDrafts.idd);

					finalobject.put("lookbook", myobj);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return finalobject;

	}

	@SuppressWarnings("deprecation")
	public void upload1(final JSONObject arr) {

		/**
		 * 
		 * Comment Edit
		 */
		LOOKBOOK_URL = getResources().getString(R.string.base_url)
				+ "Lookbooks/edit/" + ProfileDrafts.idd + ".json";
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("jsonData", arr);

		
			for (int i = 0; i < files.size(); i++) {
			
				if (files.get(i) == null) {
				
					
					params.put("files[nochange"+i+"][jpg][error]", 4);
					
				} else {
					
					
					try {
						params.put("files[" + files.get(i).getName() + "]",
								files.get(i));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}

				
			}


		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				 customDailog();
				mProgressHUD.dismiss();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				mProgressHUD.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

}
