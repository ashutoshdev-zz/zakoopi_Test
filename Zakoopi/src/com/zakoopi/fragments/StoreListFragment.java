package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.R.string;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
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
import com.mystores.StoreActivity;
import com.zakoopi.R;
import com.zakoopi.activity.UserStoreAndExperiences;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.searchResult.MarketResult;
import com.zakoopi.searchResult.StoreDetail;
import com.zakoopi.searchResult.TopMarket;
import com.zakoopi.searchResult.TopMarketPojo;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.TopMarketStoreAdapter;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class StoreListFragment extends Fragment {
	
	TopMarketStoreAdapter storeAdapter;
	private static String TOP_MARKET_STORE_REST_URL = " ";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	public static int page = 1;
	private SharedPreferences pro_user_pref,pref_location;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password,city_name;
	Typeface typeface_semibold, typeface_bold;
	private EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	int store_feed_size;
	ArrayList<TopMarketPojo> pojolist_store = null;
	public static List<TopMarketPojo> mList = new ArrayList<TopMarketPojo>();
	View include_view;
	LinearLayout store_linear,exp_linear;
	RelativeLayout rel_stores, rel_exp;
	TextView txt_store, txt_exp,txt_count;//, txt_popular1, txt_recent1;
	View view_store, view_exp;//, view_popular1, view_recent1;
	ProgressBar progressBar1;
	String store_image, store_featured_count, store_card_image, image_total_count;
	  
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		View view = inflater.inflate(R.layout.top_market_results, null);
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		try {

			pref_location = getActivity().getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "123");
			client = ClientHttp.getInstance(getActivity());

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

			/**
			 * Set Typeface
			 */
			typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/SourceSansPro-Semibold.ttf");

			typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/SourceSansPro-Bold.ttf");
			
			include_view = (View) view.findViewById(R.id.previewLayout);
			store_linear = (LinearLayout) include_view
					.findViewById(R.id.lin_all_store);
			exp_linear = (LinearLayout) include_view
					.findViewById(R.id.lin_all_exp);
			txt_count = (TextView) view.findViewById(R.id.txt_count);
			
			txt_count.setTypeface(typeface_semibold);
			if (UserStoreAndExperiences.search_slug.equals("discover")) {
				TOP_MARKET_STORE_REST_URL = getString(R.string.base_url)
						+ "Common/searchByLocationStores/"
						+ UserStoreAndExperiences.product_slug + "-in-"
						+ UserStoreAndExperiences.area_slug + ".json?page=";
			} else if (UserStoreAndExperiences.search_slug.equals("top_mark")) {
				TOP_MARKET_STORE_REST_URL = getString(R.string.base_url)
						+ "Common/searchByLocationStores/all-products-in-"
						+ UserStoreAndExperiences.market_slug + ".json?page=";
				
				
			} else {
				TOP_MARKET_STORE_REST_URL = getString(R.string.base_url)
						+ "Common/trend/" + UserStoreAndExperiences.trend_slug
						+ ".json?page=";

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		/**
		 * FeedHeader
		 */
		View feed_header_sto = inflater.inflate(R.layout.store_list_header, null);
		rel_stores = (RelativeLayout) feed_header_sto.findViewById(R.id.rel_stores);
		rel_exp = (RelativeLayout) feed_header_sto.findViewById(R.id.rel_exp);
		txt_store = (TextView) feed_header_sto.findViewById(R.id.text);
		txt_exp = (TextView) feed_header_sto.findViewById(R.id.text1);
		view_store = (View) feed_header_sto.findViewById(R.id.view1);
		view_exp = (View) feed_header_sto.findViewById(R.id.view2);
		txt_store.setTypeface(typeface_semibold);
		txt_exp.setTypeface(typeface_semibold);
		
		endlessListView = (EndlessListView) view
				.findViewById(R.id.endlessListView);

		try {

			//endlessListView.addHeaderView(feed_header_sto);
			//mHaveMoreDataToLoad = true;
			endlessListView.setOnLoadMoreListener(loadMoreListener);
			// market_store_feed(page);
			//header_click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return view;
	}
	
	@Override
	public void onResume() {

		if ((TopMarketStoreAdapter.type.equals("Topmarket")) && (mList.size() > 0)) {

			
			storeAdapter.notifyDataSetChanged();
			page = StoreActivity.mypage;
			progressBar1.setVisibility(View.GONE);
			
		}  else {
			
			market_store_feed(page);
		}
		

		super.onResume();
	}
	
	
	public void header_click() {

		rel_exp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			//	lin_all_store.setVisibility(View.GONE);
				exp_linear.setVisibility(View.VISIBLE);
				txt_exp.setTextColor(Color.parseColor("#4d4d49"));
				txt_store.setTextColor(Color.parseColor("#acacac"));
				view_exp.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_store.setBackgroundColor(Color.parseColor("#acacac"));
				txt_exp.setTypeface(typeface_semibold);
				txt_store.setTypeface(typeface_semibold);

			
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Fragment topExpFrag = new ExperiencesListFragment();
				Bundle arguments = new Bundle();
				arguments.putBoolean("shouldYouCreateAChild", false);
				topExpFrag.setArguments(arguments);
				ft.replace(R.id.lin_all_exp, topExpFrag);
				ft.commit();
				
				Tracker t = ((UILApplication)getActivity().getApplication()).getTracker(
					    TrackerName.APP_TRACKER);
					// Build and send an Event.
				
					t.send(new HitBuilders.EventBuilder()
					    .setCategory("Click on Experience Feed")
					    .setAction("View Experience Feed by "+pro_user_name)
					    .setLabel("Store Feed")
					    .build());
			}
		});
	}
	
	/**
	 * @void loadMoreData()
	 */
	private void loadMoreData() {
		page++;
		if (store_feed_size > 20) {
			mHaveMoreDataToLoad = true;
			market_store_loadmoreFeed(page);
		} else {
			mHaveMoreDataToLoad = false;
			endlessListView.loadMoreCompleat();
			
		}

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

	public void market_store_feed(int page) {
		long time = System.currentTimeMillis();
	
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		
		
		client.get(TOP_MARKET_STORE_REST_URL + page + "&_=" + time,
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
						
							endlessListView.loadMoreCompleat();
						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						progressBar1.setVisibility(View.VISIBLE);
						endlessListView.loadMoreCompleat();
						 
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}

	public void market_store_loadmoreFeed(int page) {
		long time = System.currentTimeMillis();
	

		client.setBasicAuth(user_email, user_password);

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(TOP_MARKET_STORE_REST_URL + page + "&_=" + time,
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
						// Log.e("ggggg", e.getMessage() + "vvvvvv   "
						// + statusCode);
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
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());

	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}

	@SuppressWarnings("unchecked")
	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		MarketResult ppp = gson.fromJson(reader, MarketResult.class);
		StoreDetail det = ppp.getData();
		/*
		 * List<TopMarket> store_feed = det.getStores(); List<TopExperiences>
		 */
		new MarketStore().execute(det);

	}

	public class MarketStore extends AsyncTask<StoreDetail, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(StoreDetail... params) {

			StoreDetail sd = params[0];
			List<TopMarket> store_feed = sd.getStores();
			pojolist_store = new ArrayList<TopMarketPojo>();
			pojolist_store.clear();
		
			store_feed_size = Integer.parseInt(sd.getStores_count());
			
			

			for (int i = 0; i < store_feed.size(); i++) {
				TopMarket tm = store_feed.get(i);

				// StoreDetail tm = tm.getStore_detail();
				// Log.e("StoreDetail", ""+tm.getStore_detail());
				try {
					
					// store_image = String.valueOf(tm.getStore_images().size());
					 //store_card_image = String.valueOf(tm.getStore_cards().size());
					 store_image = String.valueOf(tm.getStore_images().size() + tm.getStore_cards().size());
					 store_featured_count = String.valueOf(tm.getRelated_lookbooks().size() + tm.getArticle_stores().size());
					
					
					String store_id = tm.getId();
					String store_name = tm.getStore_name();
					String store_market_name = tm.getMarket_name();
					String store_sub_city = tm.getSub_city();
					String overall_ratings = tm.getOverall_ratings();
					String rated_color = tm.getRated_color();
					String is_followed = tm.getIs_followed();
					String lookbook_count = tm.getLookbooks_count();
					String images_count = tm.getImages_count();
					String review_count = tm.getStore_review_count();
					
					TopMarketPojo marketPojo = new TopMarketPojo(store_id,
							store_name, store_market_name, store_sub_city,
							review_count, overall_ratings, images_count,
							lookbook_count, rated_color, is_followed,store_image,store_featured_count);
					pojolist_store.add(marketPojo);
					mList.add(marketPojo);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {

				storeAdapter = new TopMarketStoreAdapter(getActivity(),
						pojolist_store);
				endlessListView.setAdapter(storeAdapter);
				storeAdapter.notifyDataSetChanged();
				endlessListView.loadMoreCompleat();
				progressBar1.setVisibility(View.GONE);
				mHaveMoreDataToLoad = true;
				
			
					
					
					if (UserStoreAndExperiences.search_slug.equals("discover")) {
						
						if (HomeDiscoverFragment.area_string.equals("All Areas")) {
							
							if (HomeDiscoverFragment.product_string.equals("All Products")) {
								//txt_count.setText(store_feed_size+" stores in "+city_name);
								txt_count.setText(store_feed_size+" stores in "+city_name);
							} else {
								txt_count.setText(store_feed_size+" stores for "+HomeDiscoverFragment.product_string+" in "+HomeDiscoverFragment.area_string+" "+city_name);
							}
							
							
						} else {
							
							txt_count.setText(store_feed_size+" stores for "+HomeDiscoverFragment.product_string+" in "+HomeDiscoverFragment.area_string+" "+city_name);
						}
						
					//	txt_count.setText(store_feed_size+" "+UserStoreAndExperiences.area_slug);
					} else if (UserStoreAndExperiences.search_slug.equals("top_mark")) {
						//txt_count.setText(store_feed_size+" stores in "+HomeDiscoverFragment.market_string+" "+city_name);
						txt_count.setText(store_feed_size+" stores in "+HomeDiscoverFragment.market_string);
						
					} else {
						//txt_count.setText(store_feed_size+" stores "+HomeDiscoverFragment.trend_string+" in "+city_name);
						txt_count.setText(store_feed_size+" stores for "+HomeDiscoverFragment.trend_string+" in "+city_name);

					}
					
					
					
					
				
				
				//header_view.setVisibility(View.GONE);
				endlessListView.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void showmoreData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		MarketResult ppp = gson.fromJson(reader, MarketResult.class);
		StoreDetail sd = ppp.getData();
		List<TopMarket> feeds = sd.getStores();

		// Log.e("popularfeed", "" + feeds);
		new MarketStoreMore().execute(feeds);
	}

	private class MarketStoreMore extends
			AsyncTask<List<TopMarket>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(List<TopMarket>... params) {
			pojolist_store = new ArrayList<TopMarketPojo>();
			pojolist_store.clear();

			for (int i = 0; i < params[0].size(); i++) {
				TopMarket tm = params[0].get(i);
				// StoreDetail tm = tm.getStore_detail();
				try {
				//	store_image = String.valueOf(tm.getStore_images().size());
					store_image = String.valueOf(tm.getStore_images().size() + tm.getStore_cards().size());
					 store_featured_count = String.valueOf(tm.getRelated_lookbooks().size() + tm.getArticle_stores().size());
					String store_id = tm.getId();
					String store_name = tm.getStore_name();
					String store_market_name = tm.getMarket_name();
					String store_sub_city = tm.getSub_city();
					String overall_ratings = tm.getOverall_ratings();
					String rated_color = tm.getRated_color();
					String is_followed = tm.getIs_followed();
					String lookbook_count = tm.getLookbooks_count();
					String images_count = tm.getImages_count();
					String review_count = tm.getStore_review_count();

					TopMarketPojo marketPojo = new TopMarketPojo(store_id,
							store_name, store_market_name, store_sub_city,
							review_count, overall_ratings, images_count,
							lookbook_count, rated_color, is_followed,store_image,store_featured_count);
					pojolist_store.add(marketPojo);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {

				storeAdapter.addItems(pojolist_store);
				endlessListView.loadMoreCompleat();

				endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
				endlessListView.setVerticalScrollBarEnabled(false);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	
}
