package com.mystores;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.store.model.StoreDetail;
import com.store.model.StoreRatings;
import com.store.model.StoreReviewArrays;
import com.store.model.Stores;
import com.zakoopi.R;
import com.zakoopi.helper.CustomScrollView;
import com.zakoopi.helper.Variables;
import com.zakoopi.searchResult.TopMarketPojo;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.TopMarketStoreAdapter;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class MainFragment extends Fragment {

	LinearLayout lin1, lin2, lin3, lin_social;
	RelativeLayout rel_feature, rel_genral, rel_review, rel_contact,
			rel_follow;
	private static String STORE_DETAIL_URL = "";
	static AsyncHttpClient client;
	LinearLayout lin_sc;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	public static Stores stores;
	public static StoreDetail detail;
	TextView store_name, store_address, store_rate, store_like, store_timings,
			store_close, call, rate, review, follow, store_general,
			store_featured, store_review;
	ImageView img_fb, img_twitter, img_website, img_follow, img_rate,img_review;
	View view_fb, view_twitter;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	View v1, v2, v3;
	public static CustomScrollView scroll;
	RelativeLayout rateme, reviewme, rel_rate_box;
	TextView ratecount;
	private SharedPreferences pro_user_pref;
	public static String pro_user_pic_url, pro_user_name, pro_user_location,
			user_email, user_password, pro_user_id;
	boolean rate_bool = false;
	String self_rate="not", self_review = "no";
	String phone_num,uri = "";
	private String chkfollow;
	String status;
	ProgressBar progressBar1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.main_fragment, null);

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Store Info");
		t.send(new HitBuilders.AppViewBuilder().build());
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		lin1 = (LinearLayout) view.findViewById(R.id.lin1);
		lin2 = (LinearLayout) view.findViewById(R.id.lin2);
		lin3 = (LinearLayout) view.findViewById(R.id.lin3);
		// lin4 = (LinearLayout) view.findViewById(R.id.lin4);
		lin_sc = (LinearLayout) view.findViewById(R.id.sc);
		rel_genral = (RelativeLayout) view.findViewById(R.id.tt1);
		rel_feature = (RelativeLayout) view.findViewById(R.id.tt2);
		rel_review = (RelativeLayout) view.findViewById(R.id.tt3);
		img_follow = (ImageView) view.findViewById(R.id.img_follow);
		rel_follow = (RelativeLayout) view.findViewById(R.id.rel_follow);
		rel_contact = (RelativeLayout) view.findViewById(R.id.rel_contact);

		STORE_DETAIL_URL = getString(R.string.base_url) + "stores/view/"
				+ StoreActivity.store_id + ".json";
//Log.e("STORE", STORE_DETAIL_URL);
		try {

			client = ClientHttp.getInstance(getActivity());
			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = getActivity()
					.getSharedPreferences("User_detail", 0);
			pro_user_id = pro_user_pref.getString("user_id", "NA");
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
			typeface_semibold = Typeface.createFromAsset(getActivity()
					.getAssets(), "fonts/SourceSansPro-Semibold.ttf");
			typeface_black = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/SourceSansPro-Black.ttf");
			typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/SourceSansPro-Bold.ttf");
			typeface_light = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/SourceSansPro-Light.ttf");
			typeface_regular = Typeface.createFromAsset(getActivity()
					.getAssets(), "fonts/SourceSansPro-Regular.ttf");

		} catch (Exception e) {
			// TODO: handle exception
		}

		/*
		 * check phone state
		 */

		MyPhoneListener phoneListener = new MyPhoneListener();
		TelephonyManager telephonyManager = (TelephonyManager) getActivity()
				.getSystemService(Context.TELEPHONY_SERVICE);
		// receive notifications of telephony state changes
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		img_rate = (ImageView) view.findViewById(R.id.img_rate);
		store_name = (TextView) view.findViewById(R.id.txt_store_name);
		store_address = (TextView) view.findViewById(R.id.store_location);
		store_rate = (TextView) view.findViewById(R.id.txt_rate);
		store_like = (TextView) view.findViewById(R.id.txt_vote);
		store_timings = (TextView) view.findViewById(R.id.txt_store_time);
		store_close = (TextView) view.findViewById(R.id.txt_store_close_day);
		call = (TextView) view.findViewById(R.id.txt_call);
		rate = (TextView) view.findViewById(R.id.txt_rate1);
		review = (TextView) view.findViewById(R.id.txt_review);
		follow = (TextView) view.findViewById(R.id.txt_follow);
		store_general = (TextView) view.findViewById(R.id.textView1);
		store_featured = (TextView) view.findViewById(R.id.textView2);
		store_review = (TextView) view.findViewById(R.id.textView3);
		scroll = (CustomScrollView) view.findViewById(R.id.scrollView1);
		rateme = (RelativeLayout) view.findViewById(R.id.rel_rate1);
		reviewme = (RelativeLayout) view.findViewById(R.id.rel_review);
		rel_rate_box = (RelativeLayout) view.findViewById(R.id.rel_rated_box1);
		img_fb = (ImageView) view.findViewById(R.id.img_facebook);
		img_twitter = (ImageView) view.findViewById(R.id.img_twitter);
		img_website = (ImageView) view.findViewById(R.id.img_website);
		img_review = (ImageView) view.findViewById(R.id.img_review);
		lin_social = (LinearLayout) view.findViewById(R.id.lin_social);

		view_fb = (View) view.findViewById(R.id.view_fb);
		view_twitter = (View) view.findViewById(R.id.view_twitter);

		v1 = (View) view.findViewById(R.id.view111);
		v2 = (View) view.findViewById(R.id.view11);
		v3 = (View) view.findViewById(R.id.view14);

		/**
		 * Set Typeface on TextView
		 */
		store_name.setTypeface(typeface_semibold);
		store_address.setTypeface(typeface_regular);
		store_rate.setTypeface(typeface_bold);
		store_like.setTypeface(typeface_regular);
		store_timings.setTypeface(typeface_regular);
		store_close.setTypeface(typeface_regular);
		call.setTypeface(typeface_regular);
		rate.setTypeface(typeface_regular);
		review.setTypeface(typeface_regular);
		follow.setTypeface(typeface_regular);
		store_general.setTypeface(typeface_semibold);
		store_featured.setTypeface(typeface_semibold);
		store_review.setTypeface(typeface_semibold);

		getStoresInfo();
		lin1.setVisibility(View.VISIBLE);
		lin2.setVisibility(View.GONE);
		lin3.setVisibility(View.GONE);
		// lin4.setVisibility(View.GONE);

		store_general.setTextColor(Color.BLACK);
		store_featured.setTextColor(Color.GRAY);
		store_review.setTextColor(Color.GRAY);
		v1.setBackgroundResource(R.drawable.store_rating_bar_blue);
		v2.setBackgroundResource(R.drawable.store_rating_bar);
		v3.setBackgroundResource(R.drawable.store_rating_bar);

		rateme.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rateDialog();

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click Rate box on Store")
						.setAction("Rate Box open by " + pro_user_name)
						.setLabel("Store").build());
			}
		});

		reviewme.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), ReviewMe.class);
				in.putExtra("store_id", StoreActivity.store_id);
				in.putExtra("rate", "main_fragment");
				startActivity(in);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click Review Me on Store")
						.setAction("Review box open by " + pro_user_name)
						.setLabel("Store").build());

			}
		});

		rel_feature.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				lin1.setVisibility(View.GONE);
				lin2.setVisibility(View.VISIBLE);
				lin3.setVisibility(View.GONE);
				// lin4.setVisibility(View.GONE);

				store_general.setTextColor(Color.GRAY);
				store_featured.setTextColor(Color.BLACK);
				store_review.setTextColor(Color.GRAY);
				v1.setBackgroundResource(R.drawable.store_rating_bar);
				v2.setBackgroundResource(R.drawable.store_rating_bar_blue);
				v3.setBackgroundResource(R.drawable.store_rating_bar);

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Feature gen = new Feature();
				Bundle bnd = new Bundle();
				bnd.putBoolean("gen23", false);
				gen.setArguments(bnd);
				ft.replace(R.id.lin2, gen);
				ft.commit();

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("featured of Store")
						.setAction("store featured viewed by " + pro_user_name)
						.setLabel("Store").build());
			}
		});

		rel_genral.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				lin1.setVisibility(View.VISIBLE);
				lin2.setVisibility(View.GONE);
				lin3.setVisibility(View.GONE);
				// lin4.setVisibility(View.GONE);

				store_general.setTextColor(Color.BLACK);
				store_featured.setTextColor(Color.GRAY);
				store_review.setTextColor(Color.GRAY);
				v1.setBackgroundResource(R.drawable.store_rating_bar_blue);
				v2.setBackgroundResource(R.drawable.store_rating_bar);
				v3.setBackgroundResource(R.drawable.store_rating_bar);

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				General gen = new General();
				Bundle bnd = new Bundle();
				bnd.putBoolean("gen23", false);
				gen.setArguments(bnd);
				ft.replace(R.id.lin1, gen);
				ft.commit();

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("General of store")
						.setAction("Store General viewed by " + pro_user_name)
						.setLabel("Store").build());
			}
		});

		rel_review.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				lin1.setVisibility(View.GONE);
				lin2.setVisibility(View.GONE);
				lin3.setVisibility(View.VISIBLE);
				// lin4.setVisibility(View.GONE);

				store_general.setTextColor(Color.GRAY);
				store_featured.setTextColor(Color.GRAY);
				store_review.setTextColor(Color.BLACK);
				v1.setBackgroundResource(R.drawable.store_rating_bar);
				v2.setBackgroundResource(R.drawable.store_rating_bar);
				v3.setBackgroundResource(R.drawable.store_rating_bar_blue);

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Review gen = new Review();
				Bundle bnd = new Bundle();
				bnd.putBoolean("gen2333", false);
				gen.setArguments(bnd);
				ft.replace(R.id.lin3, gen);
				ft.commit();

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Review of Store")
						.setAction("Store Review viewed by " + pro_user_name)
						.setLabel("Store").build());
			}
		});

		
		rel_contact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					
					if (!phone_num.equals("")) {
						final String[] num = phone_num.split(";");
						/*for (int i = 0; i < num.length; i++) {
							Log.e("PHO", ""+num[i]);
							 uri = "tel:" + num[i];
						}*/
						
						if (num.length >1) {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
							// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							View convertView = (View) LayoutInflater.from(getActivity()).inflate(R.layout.contact_dialog, null);
								alertDialog.setView(convertView);
								TextView txt_con = (TextView) convertView.findViewById(R.id.txt_con);
						        ListView lv = (ListView) convertView.findViewById(R.id.contact_listView);
						        txt_con.setTypeface(typeface_semibold);
						        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.contact_list_item,R.id.textView_contact,num);
						        lv.setAdapter(adapter);
						        alertDialog.show();
						//	Log.e("PHONE", phone_num);
							
							lv.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									uri = "tel:" + num[position];
									Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri
									.parse(uri));
							startActivity(dialIntent);
								}
							});
						} else {
							uri = "tel:" + num[0];
							Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri
									.parse(uri));
							startActivity(dialIntent);
						}
					} else {
						Toast.makeText(getActivity(), "No Contact No. Found", Toast.LENGTH_SHORT).show();
					}
					
					
					 
					

					Tracker t = ((UILApplication) getActivity()
							.getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Call on Store")
							.setAction("Call by " + pro_user_name)
							.setLabel("Store").build());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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

	public void getStoresInfo() {

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(STORE_DETAIL_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started
				progressBar1.setVisibility(View.VISIBLE);
				lin_sc.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));

					String line = "";
					String text = "";

					while ((line = br.readLine()) != null) {

						text = text + line;

					}

					showData(text);

					lin_sc.setVisibility(View.VISIBLE);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)

				progressBar1.setVisibility(View.GONE);
				lin_sc.setVisibility(View.VISIBLE);

			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		stores = gson.fromJson(reader, Stores.class);
		detail = stores.getStore();
		phone_num = detail.getTelephone();
	//	Log.e("PHONE__", phone_num);
		chkfollow = detail.getIs_followed();
		ArrayList<StoreRatings> store_ratings = new ArrayList<StoreRatings>();
		store_ratings = detail.getStore_ratings();
		ArrayList<StoreReviewArrays> store_review = new ArrayList<StoreReviewArrays>();
		store_review = detail.getStore_reviews();

		try {

			store_name.setText(detail.getStore_name());
			store_timings.setText(detail.getStore_timing_from() + " - "
					+ detail.getStore_timing_to());
			store_close.setText(detail.getClosed_day());

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (store_ratings.size() > 0) {
			for (int i = 0; i < store_ratings.size(); i++) {
				if (store_ratings.get(i).getUser_id().equals(pro_user_id)) {
					self_rate = store_ratings.get(i).getRating();
					if (self_rate.equals("0.0")) {
						self_rate = "not";
					} 
					
			//		Log.e("self_", self_rate);
					break;
				} else {
					self_rate = "not";
				}
			}
		} else {
			self_rate = "not";
		}
		

		try {

			if (detail.getPin_code() == null) {
				/*store_address.setText(detail.getStore_address() + ","
						+ detail.getArea());*/
				store_address.setText(detail.getStore_address());
			} else if (detail.getPin_code().equals("0")) {
				/*store_address.setText(detail.getStore_address() + ","
						+ detail.getArea());*/
				store_address.setText(detail.getStore_address());
			} else if (!detail.getPin_code().equals("")) {
				/*store_address.setText(detail.getStore_address() + ","
						+ detail.getArea() + "," + detail.getPin_code());*/
				store_address.setText(detail.getStore_address() +"," + detail.getPin_code());
			}
			
			if (detail.getStore_rating_count() == null) {
				store_like.setText("0 Votes");
			}else {
				store_like.setText(detail.getStore_rating_count() + " Votes");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		for (int i = 0; i < store_review.size(); i++) {
			if (store_review.get(i).getUser_id().equals(pro_user_id)) {
				// self_rate_review = store_ratings.get(i).getRating();
				self_review = store_review.get(i).getReview();
				break;

			} else {
				self_review = "no";
			}

		}

		try {
			
			if (!self_rate.equals("not") ) {
				rate.setText("Rated");
				img_rate.setImageResource(R.drawable.store_rated);
			//	Log.e("self_IF", self_rate);
			} else {
				rate.setText("Rate");
				img_rate.setImageResource(R.drawable.store_rate);
			//	Log.e("self_ELSE", self_rate);
			}
			
			if (!self_review.equals("no")) {
				review.setText("Reviewed");
				img_review.setImageResource(R.drawable.store_reviewed);
			} else {
				review.setText("Review");
				img_review.setImageResource(R.drawable.store_review);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if ((detail.getRelated_lookbooks().size() > 0) || (detail.getArticle_stores().size() > 0)) {
			rel_feature.setVisibility(View.VISIBLE);
		} else {
			rel_feature.setVisibility(View.GONE);

		}

		List<StoreReviewArrays> lis_review = new ArrayList<StoreReviewArrays>();
		
		lis_review = detail.getStore_reviews();
		for (int i = 0; i < lis_review.size(); i++) {
			StoreReviewArrays pop = lis_review.get(i);
			 status = pop.getStatus();
			// Log.e("status", status);
			 if (status.equals("true")) {
				 rel_review.setVisibility(View.VISIBLE);
				 break;
			}else {
				rel_review.setVisibility(View.GONE);
				// break;
			}
			 
		}
		
		/*if (status.equals("true")) {
			rel_review.setVisibility(View.VISIBLE);
		} else {
			rel_review.setVisibility(View.GONE);
		}*/

		try {

			if (detail.getFacebook_link().equals("")
					&& detail.getTwitter_link().equals("")
					&& detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.GONE);
			} else if (!detail.getFacebook_link().equals("")
					&& detail.getTwitter_link().equals("")
					&& detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.VISIBLE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.GONE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.GONE);
			} else if (detail.getFacebook_link().equals("")
					&& !detail.getTwitter_link().equals("")
					&& detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.GONE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.VISIBLE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.GONE);
			} else if (detail.getFacebook_link().equals("")
					&& detail.getTwitter_link().equals("")
					&& !detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.GONE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.GONE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.VISIBLE);
			} else if (!detail.getFacebook_link().equals("")
					&& !detail.getTwitter_link().equals("")
					&& detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.VISIBLE);
				view_fb.setVisibility(View.VISIBLE);
				img_twitter.setVisibility(View.VISIBLE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.GONE);
			} else if (detail.getFacebook_link().equals("")
					&& !detail.getTwitter_link().equals("")
					&& !detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.GONE);
				view_fb.setVisibility(View.GONE);
				img_twitter.setVisibility(View.VISIBLE);
				view_twitter.setVisibility(View.VISIBLE);
				img_website.setVisibility(View.VISIBLE);
			} else if (!detail.getFacebook_link().equals("")
					&& detail.getTwitter_link().equals("")
					&& !detail.getWebsite().equals("")) {
				lin_social.setVisibility(View.VISIBLE);
				img_fb.setVisibility(View.VISIBLE);
				view_fb.setVisibility(View.VISIBLE);
				img_twitter.setVisibility(View.GONE);
				view_twitter.setVisibility(View.GONE);
				img_website.setVisibility(View.VISIBLE);
			} else {
				lin_social.setVisibility(View.VISIBLE);
				view_fb.setVisibility(View.VISIBLE);
				img_twitter.setVisibility(View.VISIBLE);
				view_twitter.setVisibility(View.VISIBLE);
				img_website.setVisibility(View.VISIBLE);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {

			String rated_color = detail.getRated_color();
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_0);
			GradientDrawable drawable = (GradientDrawable) rel_rate_box
					.getBackground();
			// drawable.setColor(Color.parseColor(rated_color));
			
			if (detail.getOverall_ratings().equals("0.0")) {
				store_rate.setText("-");
				drawable.setColor(Color.parseColor(rated_color));
			} else {
				store_rate.setText(detail.getOverall_ratings());
				drawable.setColor(Color.parseColor(rated_color));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (chkfollow.equals("false")) {

			img_follow.setImageResource(R.drawable.store_follow);
			follow.setText("Follow");

		} else {

			img_follow.setImageResource(R.drawable.store_following);
			follow.setText("Following");
		}
		
		
		/*if (!phone_num.equals("")) {
			rel_contact.setVisibility(View.VISIBLE);
		} else {
			rel_contact.setVisibility(View.GONE);
		}*/

		social_click();
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		General gen = new General();
		Bundle bnd = new Bundle();
		bnd.putBoolean("gen", false);
		gen.setArguments(bnd);
		ft.replace(R.id.lin1, gen);
		ft.commit();
		progressBar1.setVisibility(View.GONE);

	}

	public void social_click() {

		img_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (detail.getFacebook_link().contains("http://")) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(detail
							.getFacebook_link()));
					startActivity(i);
					getActivity().finish();
				} else {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://" + detail.getFacebook_link()));
					startActivity(i);
					getActivity().finish();
				}

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on facebook")
						.setAction("clicked facebook by " + pro_user_name)
						.setLabel("Facebook profile").build());
			}
		});

		img_twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (detail.getTwitter_link().contains("http://")) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(detail
							.getTwitter_link()));
					startActivity(i);
					getActivity().finish();
				} else {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://" + detail.getTwitter_link()));
					startActivity(i);
					getActivity().finish();
				}

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Twitter")
						.setAction("clicked Twitter by " + pro_user_name)
						.setLabel("Twitter profile").build());
			}
		});

		img_website.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (detail.getWebsite().contains("http://")) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(detail
							.getWebsite()));
					startActivity(i);
					getActivity().finish();

				} else {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://" + detail.getWebsite()));
					startActivity(i);
					getActivity().finish();
				}

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Click on Website")
						.setAction("clicked Website by " + pro_user_name)
						.setLabel("Twitter profile").build());
			}
		});

		rel_follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Variables.myactclass.equals("yesit")) {
					final TopMarketPojo pojo = TopMarketStoreAdapter.mList
							.get(TopMarketStoreAdapter.pos);

					if (chkfollow.equals("false")) {
						store_follow("1");
						chkfollow = "true";
						pojo.setIs_followed("true");
						img_follow.setImageResource(R.drawable.store_following);
						follow.setText("Following");
					} else {

						store_follow("0");
						chkfollow = "false";
						pojo.setIs_followed("false");
						img_follow.setImageResource(R.drawable.store_follow);
						follow.setText("Follow");
					}
					Variables.myactclass = "noone";
				} else {

					if (chkfollow.equals("false")) {
						store_follow("1");
						chkfollow = "true";

						img_follow.setImageResource(R.drawable.store_following);
						follow.setText("Following");
					} else {

						store_follow("0");
						chkfollow = "false";

						img_follow.setImageResource(R.drawable.store_follow);
						follow.setText("Follow");
					}

				}
			}
		});
	}

	public void rateDialog() {

		/*
		 * final Dialog dd = new Dialog(getActivity(),
		 * android.R.style.Theme_Translucent_NoTitleBar);
		 */
		final Dialog dd = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setCancelable(false);
		dd.getWindow().setGravity(Gravity.TOP);
		dd.setContentView(R.layout.ratestore_dialog);
		dd.show();

		ratecount = (TextView) dd.findViewById(R.id.txt_rating);
		final TextView ratemsg = (TextView) dd.findViewById(R.id.txt_mean);
		final RelativeLayout cnl = (RelativeLayout) dd
				.findViewById(R.id.button1);
		final RelativeLayout smt = (RelativeLayout) dd
				.findViewById(R.id.button2);
		final TextView cancl = (TextView) dd.findViewById(R.id.txt_cancel);
		final TextView subm = (TextView) dd.findViewById(R.id.txt_Submit);
		final RelativeLayout rel_back = (RelativeLayout) dd
				.findViewById(R.id.rel_back);
		final TextView txt_back = (TextView) dd.findViewById(R.id.txt_back);
		final View view_1 = (View) dd.findViewById(R.id.view_1);
		final View view_2 = (View) dd.findViewById(R.id.view_2);
		final View view_3 = (View) dd.findViewById(R.id.view_3);
		final View view_4 = (View) dd.findViewById(R.id.view_4);
		final View view_5 = (View) dd.findViewById(R.id.view_5);
		final View view_6 = (View) dd.findViewById(R.id.view_6);
		final View view_7 = (View) dd.findViewById(R.id.view_7);
		final View view_8 = (View) dd.findViewById(R.id.view_8);
		final View view_9 = (View) dd.findViewById(R.id.view_9);

		ratemsg.setTypeface(typeface_regular);
		ratecount.setTypeface(typeface_semibold);
		txt_back.setTypeface(typeface_semibold);
		cancl.setTypeface(typeface_bold);
		subm.setTypeface(typeface_bold);
		txt_back.setText("Rating");

		cnl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dd.dismiss();
			}
		});

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dd.dismiss();
			}
		});

		smt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RateReview(ratecount.getText().toString().trim());
				dd.dismiss();
			}
		});

		try {

			if (self_rate.equals("1.0")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				ratecount.setText("1.0");
				ratemsg.setText("Never ever");
				rate_bool = true;
			} else if (self_rate.equals("1.5")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				ratecount.setText("1.5");
				ratemsg.setText("Doesn’t impress me");
				rate_bool = false;
			} else if (self_rate.equals("2.0")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				ratecount.setText("2.0");
				ratemsg.setText("Hmmm…");
				rate_bool = false;
			} else if (self_rate.equals("2.5")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				ratecount.setText("2.5");
				ratemsg.setText("Maybe");
				rate_bool = false;
			} else if (self_rate.equals("3.0")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				ratecount.setText("3.0");
				ratemsg.setText("Alright");
				rate_bool = false;
			} else if (self_rate.equals("3.5")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				ratecount.setText("3.5");
				ratemsg.setText("Better");
				rate_bool = false;
			} else if (self_rate.equals("4.0")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				ratecount.setText("4.0");
				ratemsg.setText("Worth a visit");
				rate_bool = false;
			} else if (self_rate.equals("4.5")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_4_1);
				ratecount.setText("4.5");
				ratemsg.setText("Definitely check it out");
				rate_bool = false;
			} else if (self_rate.equals("5.0")) {
				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_4_1);
				view_9.setBackgroundResource(R.drawable.rating_box_4_6);
				ratecount.setText("5.0");
				ratemsg.setText("If only I could buy the whole store!");
				rate_bool = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		view_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (rate_bool == false) {
					view_1.setBackgroundResource(R.drawable.rating_box_0);
					view_2.setBackgroundResource(R.drawable.rating_box_base);
					view_3.setBackgroundResource(R.drawable.rating_box_base);
					view_4.setBackgroundResource(R.drawable.rating_box_base);
					view_5.setBackgroundResource(R.drawable.rating_box_base);
					view_6.setBackgroundResource(R.drawable.rating_box_base);
					view_7.setBackgroundResource(R.drawable.rating_box_base);
					view_8.setBackgroundResource(R.drawable.rating_box_base);
					view_9.setBackgroundResource(R.drawable.rating_box_base);
					ratecount.setText("1.0");
					ratemsg.setText("Never ever");
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
					ratemsg.setText("Never ever");
					rate_bool = false;
				}*/
			}
		});

		view_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_base);
				view_4.setBackgroundResource(R.drawable.rating_box_base);
				view_5.setBackgroundResource(R.drawable.rating_box_base);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("1.5");
				ratemsg.setText("Doesn’t impress me");
				rate_bool = false;
			}
		});

		view_3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_base);
				view_5.setBackgroundResource(R.drawable.rating_box_base);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("2.0");
				ratemsg.setText("Hmmm…");
				rate_bool = false;
			}
		});

		view_4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_base);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("2.5");
				ratemsg.setText("Maybe");
				rate_bool = false;
			}
		});

		view_5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_base);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("3.0");
				ratemsg.setText("Alright");
				rate_bool = false;
			}
		});

		view_6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_base);
				view_8.setBackgroundResource(R.drawable.rating_box_base);

				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("3.5");
				ratemsg.setText("Better");
				rate_bool = false;
			}
		});

		view_7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_base);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("4.0");
				ratemsg.setText("Worth a visit");
				rate_bool = false;
			}
		});

		view_8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_4_1);
				view_9.setBackgroundResource(R.drawable.rating_box_base);
				ratecount.setText("4.5");
				ratemsg.setText("Definitely check it out");
				rate_bool = false;
			}
		});

		view_9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				view_1.setBackgroundResource(R.drawable.rating_box_0);
				view_2.setBackgroundResource(R.drawable.rating_box_1_1);
				view_3.setBackgroundResource(R.drawable.rating_box_1_6);
				view_4.setBackgroundResource(R.drawable.rating_box_2_1);
				view_5.setBackgroundResource(R.drawable.rating_box_2_6);
				view_6.setBackgroundResource(R.drawable.rating_box_3_1);
				view_7.setBackgroundResource(R.drawable.rating_box_3_6);
				view_8.setBackgroundResource(R.drawable.rating_box_4_1);
				view_9.setBackgroundResource(R.drawable.rating_box_4_6);
				ratecount.setText("5.0");
				ratemsg.setText("If only I could buy the whole store!");
				rate_bool = false;
			}
		});

	}

	/*
	 * RateReview data for
	 */

	@SuppressWarnings("deprecation")
	public void RateReview(String rate) {
		String profile_URL = getResources().getString(R.string.base_url)
				+ "Common/storeRateNReview/" + pro_user_id + ".json";

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {
			params.put("d[0][store_id]", StoreActivity.store_id);
			params.put("d[0][review]", "");
			params.put("d[0][rating]", rate);
			params.put("d[0][with_review]", "0");

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

				getStoresInfo();

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

	public float getConvertedValue(int intVal) {
		float floatVal = (float) 0.0;
		floatVal = 1.0f * intVal;
		return floatVal;
	}

	private class MyPhoneListener extends PhoneStateListener {

		private boolean onCall = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// phone ringing...
				// Toast.makeText(MainActivity.this, incomingNumber +
				// " calls you",
				// Toast.LENGTH_LONG).show();
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				// one call exists that is dialing, active, or on hold
				// Toast.makeText(MainActivity.this, "on call...",
				// Toast.LENGTH_LONG).show();
				// because user answers the incoming call
				onCall = true;
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				// in initialization of the class and at the end of phone call

				// detect flag from CALL_STATE_OFFHOOK
				if (onCall == true) {
					// Toast.makeText(MainActivity.this,
					// "restart app after call",
					// Toast.LENGTH_LONG).show();

					// restart our application
					Intent restart = getActivity().getPackageManager()
							.getLaunchIntentForPackage(
									getActivity().getPackageName());
					restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(restart);

					onCall = false;
				}
				break;
			default:
				break;
			}

		}
	}

	/*
	 * store follow service
	 */

	public void store_follow(final String follow) {
		/*
		 * Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/" +
		 * article_id + ".json");
		 */
		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		String user_id = pro_user_pref.getString("user_id", "0");

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("follow", follow);
		params.put("entity", "Stores");

		client.post(getActivity().getString(R.string.base_url)
				+ "Common/follow/" + StoreActivity.store_id + ".json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						try {

							BufferedReader bufferedReader = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String text1 = "";
							String text2 = "";
							while ((text1 = bufferedReader.readLine()) != null) {

								text2 = text2 + text1;
							}

						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] response, Throwable e) {

					}
				});
	}

	

}
