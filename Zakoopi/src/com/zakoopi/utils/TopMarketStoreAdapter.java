package com.zakoopi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.StoreActivity;
import com.zakoopi.R;
import com.zakoopi.activity.SplashScreen;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.Variables;
import com.zakoopi.searchResult.TopMarketPojo;
import com.zakoopi.utils.UILApplication.TrackerName;

public class TopMarketStoreAdapter extends BaseAdapter {

	public static List<TopMarketPojo> mList;
	Context ctx;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	SharedPreferences pro_user_pref;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	StoreHolder storeHolder;
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	public static int pos;
	public static String type = "no";
	Boolean isInternetPresent = false;
    
    // Connection detector class
    ConnectionDetector cd;

	public TopMarketStoreAdapter(Context context, List<TopMarketPojo> list) {
		ctx = context;
		mList = list;
		client = ClientHttp.getInstance(ctx);
		try {
			 cd = new ConnectionDetector(ctx);
			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
		} catch (Exception e) {
			// TODO: handle exception
		}
		/**
		 * Typeface
		 */

		typeface_semibold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
	}

	public void addItems(List<TopMarketPojo> newItems) {
		if (null == newItems || newItems.size() <= 0) {
			return;
		}

		if (null == mList) {
			mList = new ArrayList<TopMarketPojo>();
		}

		mList.addAll(newItems);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null == mList) {
			return 0;
		}

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View store_result = convertView;

		if (store_result == null) {
			LayoutInflater inf = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			store_result = inf.inflate(R.layout.user_store_item_cardview,
					parent, false);
			storeHolder = new StoreHolder();

			storeHolder.txt_store_name = (TextView) store_result
					.findViewById(R.id.txt_store_name);
			storeHolder.txt_store_location = (TextView) store_result
					.findViewById(R.id.store_location);
			storeHolder.txt_lookbook_count = (TextView) store_result
					.findViewById(R.id.txt_lookbook_count);
			storeHolder.txt_review_count = (TextView) store_result
					.findViewById(R.id.txt_review_count);
			storeHolder.txt_image_count = (TextView) store_result
					.findViewById(R.id.txt_photo_count);
			storeHolder.txt_store_rate = (TextView) store_result
					.findViewById(R.id.txt_rate);
			storeHolder.rel_rate_box = (RelativeLayout) store_result
					.findViewById(R.id.rel_rated_box1);
			storeHolder.rel_lookbook = (RelativeLayout) store_result
					.findViewById(R.id.rel_lookbook);
			storeHolder.rel_review = (RelativeLayout) store_result
					.findViewById(R.id.rel_review);
			storeHolder.rel_photo = (RelativeLayout) store_result
					.findViewById(R.id.rel_photo);
			storeHolder.rel_store_click = (RelativeLayout) store_result
					.findViewById(R.id.rel_store_click);
			storeHolder.img_follow = (ImageView) store_result
					.findViewById(R.id.img_follow);
			storeHolder.rel_store_rate = (RelativeLayout) store_result
					.findViewById(R.id.rel_store_rate);

			try {

				storeHolder.txt_store_name.setTypeface(typeface_bold);
				storeHolder.txt_store_location.setTypeface(typeface_regular);
				storeHolder.txt_lookbook_count.setTypeface(typeface_regular);
				storeHolder.txt_review_count.setTypeface(typeface_regular);
				storeHolder.txt_image_count.setTypeface(typeface_regular);
				storeHolder.txt_store_rate.setTypeface(typeface_bold);
			} catch (Exception e) {
				// TODO: handle exception
			}
			store_result.setTag(storeHolder);

			storeHolder.img_follow
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							ImageView img = (ImageView) v;
							TopMarketPojo ppp = (TopMarketPojo) img.getTag();
							String storeid = ppp.getId();

							if (ppp.getIs_followed().equals("false")) {

								ppp.setIs_followed("true");
								storeHolder.img_follow
										.setImageResource(R.drawable.experience_following_button);
								store_follow("1", storeid);
								notifyDataSetChanged();
								
								Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
									    TrackerName.APP_TRACKER);
									// Build and send an Event.
								
									t.send(new HitBuilders.EventBuilder()
									    .setCategory("Follow Store")
									    .setAction(ppp.getStore_name()+" Follow by "+pro_user_name)
									    .setLabel("Store View")
									    .build());
								
							} else {
								ppp.setIs_followed("false");
								storeHolder.img_follow
										.setImageResource(R.drawable.results_follow);
								store_follow("0", storeid);
								notifyDataSetChanged();
								
								Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
									    TrackerName.APP_TRACKER);
									// Build and send an Event.
								
									t.send(new HitBuilders.EventBuilder()
									    .setCategory("Unfollow Store")
									    .setAction(ppp.getStore_name()+" Unfollow by "+pro_user_name)
									    .setLabel("Store View")
									    .build());
							}

						}
					});

		} else {
			storeHolder = (StoreHolder) store_result.getTag();
		}

		final TopMarketPojo pojo = mList.get(position);

		storeHolder.img_follow.setTag(pojo);
		if (pojo.getIs_followed().equals("false")) {

			storeHolder.img_follow.setImageResource(R.drawable.results_follow);

		} else {

			storeHolder.img_follow
					.setImageResource(R.drawable.experience_following_button);

		}

		try {

			storeHolder.txt_store_name.setText(pojo.getStore_name());
			storeHolder.txt_store_location.setText(pojo.getMarket_name() + ", "
					+ pojo.getSub_city());
			storeHolder.rel_rate_box
					.setBackgroundResource(R.drawable.rating_box_0);
			GradientDrawable drawable = (GradientDrawable) storeHolder.rel_rate_box
					.getBackground();

			if (pojo.getOverall_ratings().equals("0")) {
				storeHolder.txt_store_rate.setText("-");
				drawable.setColor(Color.parseColor(pojo.getRated_color()));
			} else {
				storeHolder.txt_store_rate.setText(pojo.getOverall_ratings());
				drawable.setColor(Color.parseColor(pojo.getRated_color()));
			}

			if (pojo.getStore_featured_count().equals("0")) {
				storeHolder.rel_lookbook.setVisibility(View.GONE);
			} else {
				storeHolder.rel_lookbook.setVisibility(View.VISIBLE);
				storeHolder.txt_lookbook_count.setText(pojo
						.getStore_featured_count());
			}

			if (pojo.getStore_image().equals("0")) {
				storeHolder.rel_photo.setVisibility(View.GONE);
			} else {
				storeHolder.rel_photo.setVisibility(View.VISIBLE);
				storeHolder.txt_image_count.setText(pojo.getStore_image());
			}

			if (pojo.getStore_review_count().equals("0")) {
				storeHolder.rel_review.setVisibility(View.GONE);
			} else {
				storeHolder.rel_review.setVisibility(View.VISIBLE);
				storeHolder.txt_review_count.setText(pojo
						.getStore_review_count());
			}

			if (pojo.getStore_featured_count().equals("0")
					&& pojo.getStore_image().equals("0")
					&& pojo.getStore_review_count().equals("0")) {
				storeHolder.rel_store_rate.setVisibility(View.INVISIBLE);
			} else {
				storeHolder.rel_store_rate.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		storeHolder.rel_store_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					
					 isInternetPresent = cd.isConnectingToInternet();
		             // check for Internet status
		                if (isInternetPresent) {
		                    // Internet Connection is Present
		                	pos = position;
							type = "Topmarket";
							Variables.myactclass = "yesit";
							Intent in = new Intent(ctx, StoreActivity.class);
							in.putExtra("store_id", pojo.getId());
							ctx.startActivity(in);
							
							Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
								    TrackerName.APP_TRACKER);
								// Build and send an Event.
							
								t.send(new HitBuilders.EventBuilder()
								    .setCategory("Click for store details")
								    .setAction(pojo.getStore_name()+" details view by "+pro_user_name)
								    .setLabel("Store View")
								    .build());
		                } else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(ctx, "No Internet Connection",
		                            "You don't have internet connection.", false);
		                }
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		return store_result;
	}

	/*
	 * store follow service
	 */

	public void store_follow(final String follow, final String stid) {
		/*
		 * Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/" +
		 * article_id + ".json");
		 */
		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
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

		client.post(ctx.getString(R.string.base_url) + "Common/follow/" + stid
				+ ".json", params, new AsyncHttpResponseHandler() {

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
					
				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {

				
			}
		});
	}

	public static class StoreHolder {
		TextView txt_store_name, txt_store_location, txt_lookbook_count,
				txt_review_count, txt_image_count, txt_store_rate;
		RelativeLayout rel_rate_box, rel_lookbook, rel_review, rel_photo,
				rel_store_click, rel_store_rate;
		ImageView img_follow;

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
	            	
	            dialog.cancel();
	            }
	        });
		 
		        // Showing Alert Message
		        alertDialog.show();
		    }

}
