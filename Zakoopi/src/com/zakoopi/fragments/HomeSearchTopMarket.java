package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
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

import com.cam.imagedatabase.DBHelper;
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
import com.zakoopi.activity.UserStoreAndExperiences;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.ExpandableHeightGridView;
import com.zakoopi.helper.TopMarketPojoDisPage;
import com.zakoopi.roundimagelib.SelectableRoundedImageView;
import com.zakoopi.search.MarketData;
import com.zakoopi.search.TopMarket;
import com.zakoopi.search.marketsTop;
import com.zakoopi.searchResult.TopExperiencesAdapter;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.TopMarketStoreAdapter;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class HomeSearchTopMarket extends Fragment {
	// private static final String ALL_TOP_MARKET_REST_URL =
	// "http://v3.zakoopi.com/api/markets/top.json";
	private static String ALL_TOP_MARKET_REST_URL = " ";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	// ArrayList<marketsTop> marketsTop = new ArrayList<marketsTop>();
	ArrayList<TopMarketPojoDisPage> top_market_pojolist = null;
	// MaterialProgressBar progressBar, bar;
	ExpandableHeightGridView grid;
	Typeface typeface_semibold;
	private SharedPreferences pref_location;
	private String city_name;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	String market_name, url_slug, market_image;

	private SQLiteDatabase db;
	public static final String DBTABLE3 = "TopMarket";
	private SQLiteStatement stm;
	private BroadcastReceiver br;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_search_market_trend, null);

		try {
			DBHelper hp = new DBHelper(getActivity());
			db = hp.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		stm = db.compileStatement("insert into " + DBTABLE3
				+ " (market_name,market_img,url_slug) " + "values (?, ?, ?)");

		cd = new ConnectionDetector(getActivity());
		// marketsTop.clear();
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home Search Top Market");
		t.send(new HitBuilders.AppViewBuilder().build());

		// TopMarketResultsFrag.mList.clear();
		TopMarketStoreAdapter.type = "no";

		pref_location = getActivity().getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "123");
		client = ClientHttp.getInstance(getActivity());

		grid = (ExpandableHeightGridView) view.findViewById(R.id.grid_view);
		grid.setFocusable(false);
		
		ALL_TOP_MARKET_REST_URL = getString(R.string.base_url)
				+ "markets/top.json";

		try {

			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = getActivity()
					.getSharedPreferences("User_detail", 0);
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
			
			click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

		}
		checkInternetConnection();
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
				/*	Log.d("TEST Internet",
							info.toString() + " " + state.toString());*/

					if (state == State.CONNECTED) {

						/*Toast.makeText(getActivity(),
								"Internet connection is on", Toast.LENGTH_LONG)
								.show();*/

						try {
							DBHelper hp = new DBHelper(getActivity());
							db = hp.getWritableDatabase();
							db.delete(DBTABLE3, null, null);
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						top_markets();

					} else {

						/*Toast.makeText(getActivity(),
								"Internet connection is Off", Toast.LENGTH_LONG)
								.show();*/

						top_market_pojolist = new ArrayList<TopMarketPojoDisPage>();
						top_market_pojolist.clear();

						new localUpload().execute();

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

	private class localUpload extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Cursor c = db.rawQuery(" select * from " + DBTABLE3, null);
				if (c != null) {
					while (c.moveToNext()) {

						try {
							String market_name_db = c.getString(c
									.getColumnIndex("market_name"));
							String market_img_db = c.getString(c
									.getColumnIndex("market_img"));
							String url_slug_db = c.getString(c
									.getColumnIndex("url_slug"));

							TopMarketPojoDisPage disPage = new TopMarketPojoDisPage(
									market_name_db, url_slug_db, market_img_db);
							top_market_pojolist.add(disPage);
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				}
			} catch (SQLiteException s) {
				// TODO: handle exception
				s.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			grid.setAdapter(new GridTopMarketAdapter(getActivity(),
					top_market_pojolist));
			grid.setExpanded(true);
		}

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

	/**
	 * {@code} click()
	 * 
	 * @return void
	 */
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
						StoreListFragment.mList.clear();
						TopMarketStoreAdapter.type = "no";
						ExperiencesListFragment.pojolist_exp1.clear();
						ExperiencesListFragment.colorlist1.clear();
						TopExperiencesAdapter.type = "no";
						HomeDiscoverFragment.market_string = top_market_pojolist
								.get(position).getMarket_name();
						// String market_slug =
						// marketsTop.get(position).getUrl_slug();
						Intent intent = new Intent(getActivity(),
								UserStoreAndExperiences.class);
						intent.putExtra("search", "top_mark");
						intent.putExtra("market_slug",
								top_market_pojolist.get(position).getUrl_slug());
						// intent.putExtra("market_slug", "market_slug");
						startActivity(intent);

						Tracker t = ((UILApplication) getActivity()
								.getApplication())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Click on Top Market")
								.setAction(
										"View Top market by " + pro_user_name)
								.setLabel("Top Market").build());
					} else {
						// Internet connection is not present
						// Ask user to connect to Internet
						showAlertDialog(getActivity(),
								"No Internet Connection",
								"You don't have internet connection.", false);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	/**
	 * {@code} top_markets()
	 * 
	 * @return void
	 */
	public void top_markets() {

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		//Log.e("URL", ALL_TOP_MARKET_REST_URL);
		client.get(ALL_TOP_MARKET_REST_URL, new AsyncHttpResponseHandler() {

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
					//Log.e("RESPO", text);
					topMarket_showData(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				//Log.e("FAIL", "FAIL " + arg0);
			}
		});
	}

	/**
	 * @return void
	 * @topMarket_showData data
	 */
	@SuppressWarnings("unchecked")
	public void topMarket_showData(String data) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);

		MarketData top_data = gson.fromJson(jsonReader, MarketData.class);
		TopMarket top_market = top_data.getData();
		List<marketsTop> marketsTop_list = top_market.getTopMarket();
		new MyApp().execute(marketsTop_list);
	}

	private class MyApp extends AsyncTask<List<marketsTop>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(List<marketsTop>... params) {
			// TODO Auto-generated method stub
			top_market_pojolist = new ArrayList<TopMarketPojoDisPage>();
			top_market_pojolist.clear();

			for (int i = 0; i < params[0].size(); i++) {
				marketsTop marketsTop_do = params[0].get(i);
				market_name = marketsTop_do.get_market_name();
				url_slug = marketsTop_do.getUrl_slug();
				market_image = marketsTop_do.get_android_api_image();
				TopMarketPojoDisPage topMarketPojoDisPage = new TopMarketPojoDisPage(
						market_name, url_slug, market_image);
				top_market_pojolist.add(topMarketPojoDisPage);
				stm.bindString(1, market_name);
				stm.bindString(2, market_image);
				stm.bindString(3, url_slug);
				stm.executeInsert();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			grid.setAdapter(new GridTopMarketAdapter(getActivity(),
					top_market_pojolist));
			grid.setExpanded(true);
		}

	}

	/**
	 * Class GridTopMarketAdapter
	 * 
	 * @author ZakoopiUser
	 * 
	 */
	public class GridTopMarketAdapter extends BaseAdapter {
		DisplayImageOptions options;
		Context ctx;
		public List<TopMarketPojoDisPage> mList;

		public GridTopMarketAdapter(Context context,
				List<TopMarketPojoDisPage> list) {
			super();
			ctx = context;
			mList = list;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.color.maroon).delayBeforeLoading(500)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

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

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Log.e("TopM", "TopM");
			View view = convertView;
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			view = inflater.inflate(R.layout.home_search_grid_item, null);

			typeface_semibold = Typeface.createFromAsset(view.getContext()
					.getAssets(), "fonts/SourceSansPro-Semibold.ttf");
			SelectableRoundedImageView img_background = (SelectableRoundedImageView) view
					.findViewById(R.id.img_background);
			TextView txt_market_name = (TextView) view
					.findViewById(R.id.txt_grid_item);
			txt_market_name.setText(mList.get(position).getMarket_name());
			txt_market_name.setTypeface(typeface_semibold);
			int color = Color.parseColor("#66000000");
			img_background.setColorFilter(color);

			try {

				ImageLoader.getInstance().displayImage(
						mList.get(position).getMarket_image(), img_background,
						options);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return view;
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

						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
