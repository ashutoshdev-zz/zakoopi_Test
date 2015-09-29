package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zakoopi.R;
import com.zakoopi.activity.SplashScreen;
import com.zakoopi.activity.UserStoreAndExperiences;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.ExpandableHeightGridView;
import com.zakoopi.roundimagelib.SelectableRoundedImageView;
import com.zakoopi.search.TopTrends;
import com.zakoopi.search.trendsTop;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.TopMarketStoreAdapter;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class HomeSearchTopTrends extends Fragment {
//	private static final String ALL_TOP_TRENDS_REST_URL = "http://v3.zakoopi.com/api/trends/top.json";
	private static String ALL_TOP_TRENDS_REST_URL = " ";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email, user_password;
	public static ArrayList<trendsTop> trendsTop = new ArrayList<trendsTop>();
	ExpandableHeightGridView grid;
	//MaterialProgressBar progressBar;
	Typeface typeface_semibold;
	private SharedPreferences pref_location;
	private String city_name;
Boolean isInternetPresent = false;
    
    // Connection detector class
    ConnectionDetector cd;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_search_market_trend, null);
		 cd = new ConnectionDetector(getActivity());
		client = ClientHttp.getInstance(getActivity());
		
		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getActivity().getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home Search Top Trends");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		try {
			
		
		pref_location = getActivity().getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "123");
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		grid = (ExpandableHeightGridView) view.findViewById(R.id.grid_view);
		grid.setFocusable(false);
	/*	progressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setColorSchemeResources(R.color.red, R.color.green,
				R.color.blue, R.color.orange);*/
		
		ALL_TOP_TRENDS_REST_URL = getString(R.string.base_url)+"trends/top.json";
		
		try {
			
		
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
		
		
		if (trendsTop.size() > 0) {
			//progressBar.setVisibility(View.GONE);
			grid.setAdapter(new GridTopTrendsAdapter());
			grid.setExpanded(true);
			new GridTopTrendsAdapter().notifyDataSetChanged();

		} else {

			top_trends();
		}
		
	//	top_trends();
		click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
	}


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}

	
	public void click() {
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				try {
					
					isInternetPresent = cd.isConnectingToInternet();
		             // check for Internet status
		                if (isInternetPresent) {
		                    // Internet Connection is Present
		                	UserStoreAndExperiences.search_slug = "";
							UserStoreAndExperiences.product_slug = "";
							UserStoreAndExperiences.area_slug = "";
							UserStoreAndExperiences.market_slug = "";
							UserStoreAndExperiences.trend_slug = "";
							HomeDiscoverFragment.trend_string = trendsTop.get(position).get_trend_name();		
						//	TopMarketResultsFrag.mList.clear();
							TopMarketStoreAdapter.type = "no";
							
						String trend_slug = trendsTop.get(position).getSlug();
						Intent intent = new Intent(getActivity(),
								UserStoreAndExperiences.class);
						intent.putExtra("search", "top_trend");
						intent.putExtra("trend_slug", trend_slug);
						startActivity(intent);
						
						Tracker t = ((UILApplication)getActivity().getApplication()).getTracker(
							    TrackerName.APP_TRACKER);
							// Build and send an Event.
						
							t.send(new HitBuilders.EventBuilder()
							    .setCategory("Click on Top Trends")
							    .setAction("View Top Trends by "+pro_user_name)
							    .setLabel("Top Trends")
							    .build());
		                } else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(getActivity(), "No Internet Connection",
		                            "You don't have internet connection.", false);
		                }
					
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}
	
	

	/**
	 * {@code} top_trends()
	 * 
	 * @return void
	 */
	public void top_trends() {
		
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_TOP_TRENDS_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub

				try {
					BufferedReader br1 = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = br1.readLine()) != null) {

						text = text + line;
					}
					// Log.e("url", "onSuccessArea");
					topTrends_showData(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 
	 * @topTrends_showData data
	 * @return void
	 * 
	 */
	public void topTrends_showData(String data) {
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		TopTrends top_trends = gson.fromJson(jsonReader, TopTrends.class);
		trendsTop = top_trends.getTopTrends();
		//progressBar.setVisibility(View.GONE);

		try {
			
		
		grid.setAdapter(new GridTopTrendsAdapter());
		grid.setExpanded(true);
		new GridTopTrendsAdapter().notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Class GridTopTrendsAdapter extends BaseAdapter
	 * @author ZakoopiUser
	 *
	 */
	public class GridTopTrendsAdapter extends BaseAdapter {
		DisplayImageOptions options;
		public GridTopTrendsAdapter() {
			super();
			 options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.color.maroon).delayBeforeLoading(100)
			.showImageForEmptyUri(R.color.maroon)
			.showImageOnFail(R.color.maroon).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return trendsTop.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			view = inflater.inflate(R.layout.home_search_grid_item, null);
			typeface_semibold = Typeface.createFromAsset(view.getContext()
					.getAssets(), "fonts/SourceSansPro-Semibold.ttf");
			SelectableRoundedImageView img_background = (SelectableRoundedImageView) view
					.findViewById(R.id.img_background);
			TextView txt_market_name = (TextView) view.findViewById(R.id.txt_grid_item);
			
			try {
				
			
			txt_market_name.setText(trendsTop.get(position).get_trend_name());
			txt_market_name.setTypeface(typeface_semibold);
			ImageLoader.getInstance().displayImage(trendsTop.get(position).get_android_api_image(),
					img_background, options);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return view;
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
            	
            dialog.cancel();
            }
        });
	 
	        // Showing Alert Message
	        alertDialog.show();
	    }
}
