package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.activity.UserStoreAndExperiences;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.searchResult.MarketResult;
import com.zakoopi.searchResult.StoreDetail;
import com.zakoopi.searchResult.TopExperiences;
import com.zakoopi.searchResult.TopExperiencesAdapter;
import com.zakoopi.searchResult.TopExperiencesImages;
import com.zakoopi.searchResult.TopExperiencesPojo;
import com.zakoopi.searchResult.TopExperiencesUser;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class ExperiencesListFragment extends Fragment {

	private TopExperiencesAdapter adapter;
	private static String TOP_MARKET_STORE_REST_URL = " ";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	public static int page = 1;
	private SharedPreferences pro_user_pref, pref_location;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password, city_name;
	Typeface typeface_semibold, typeface_bold;
	private EndlessListView endlessListView;
	private boolean mHaveMoreDataToLoad;
	public static ArrayList<TopExperiencesPojo> pojolist_exp1 = new ArrayList<TopExperiencesPojo>();
	public static ArrayList<Integer> colorlist = new ArrayList<Integer>();
	public static ArrayList<Integer> colorlist1 = new ArrayList<Integer>();
	public static Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	public static ArrayList<TopExperiencesPojo> pojolist_exp = null;
	int expr_feed_size;
	View include_view;
	LinearLayout store_linear,exp_linear;
	RelativeLayout rel_stores, rel_exp;
	TextView txt_store, txt_exp;//, txt_popular1, txt_recent1;
	View view_store, view_exp;//, view_popular1, view_recent1;
	
	Dialog diaog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		View view = inflater.inflate(R.layout.top_market_results, null);
		client = ClientHttp.getInstance(getActivity());
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
		
		if (UserStoreAndExperiences.search_slug.equals("discover")) {
			TOP_MARKET_STORE_REST_URL = getString(R.string.base_url)
					+ "Common/searchByLocationArticles/"
					+ UserStoreAndExperiences.product_slug + "-in-"
					+ UserStoreAndExperiences.area_slug + ".json?page=";
		} else if (UserStoreAndExperiences.search_slug.equals("top_mark")) {
			TOP_MARKET_STORE_REST_URL = getString(R.string.base_url)
					+ "Common/searchByLocationArticles/all-products-in-"
					+ UserStoreAndExperiences.market_slug + ".json?page=";
		} else {
			TOP_MARKET_STORE_REST_URL = getString(R.string.base_url)
					+ "Common/trend/" + UserStoreAndExperiences.trend_slug
					+ ".json?page=";

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
		
		txt_exp.setTextColor(Color.parseColor("#4d4d49"));
		txt_store.setTextColor(Color.parseColor("#acacac"));
		view_exp.setBackgroundColor(Color.parseColor("#26B3AD"));
		view_store.setBackgroundColor(Color.parseColor("#acacac"));
		txt_store.setTypeface(typeface_semibold);
		txt_exp.setTypeface(typeface_semibold);
		
		endlessListView = (EndlessListView) view
				.findViewById(R.id.endlessListView);

		try {

			endlessListView.addHeaderView(feed_header_sto);
			//experience_store_feed(1);
			/*adapter = new TopExperiencesAdapter(getActivity(),
					TopMarketResultsFrag.pojolist_exp,
					TopMarketResultsFrag.colorlist);
			endlessListView.setAdapter(adapter);*/
			header_click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if ((TopExperiencesAdapter.type.equals("Experiences")) || (pojolist_exp1.size() > 0)) {

			adapter = new TopExperiencesAdapter(getActivity(),pojolist_exp1,colorlist1);
			endlessListView.setAdapter(adapter);
		//	page = StoreActivity.mypage;

			//Log.e("hellobbbbbbbb", page + "===" + pojolist_exp1.size());
		}  else {
			//Log.e("hellohh", page + "");
			//loadDialog();
			experience_store_feed(1);
		}
	}
	
	public void header_click() {

		rel_stores.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pojolist_exp.clear();
				store_linear.setVisibility(View.VISIBLE);
				exp_linear.setVisibility(View.GONE);
				txt_store.setTextColor(Color.parseColor("#4d4d49"));
				txt_exp.setTextColor(Color.parseColor("#acacac"));
				view_store.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_exp.setBackgroundColor(Color.parseColor("#acacac"));
			//	Log.e("STORE", "STORE");
				/**
				 * Add Fragment
				 */
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				fm.beginTransaction();
				Fragment storeFrag = new StoreListFragment();
				Bundle arguments = new Bundle();
				arguments.putBoolean("shouldYouCreateAChildFragment", false);
				storeFrag.setArguments(arguments);
				ft.replace(R.id.lin_all_store, storeFrag);
				ft.commit();
				
				Tracker t = ((UILApplication)getActivity().getApplication()).getTracker(
					    TrackerName.APP_TRACKER);
					// Build and send an Event.
				
					t.send(new HitBuilders.EventBuilder()
					    .setCategory("Click on Store Feed")
					    .setAction("View Store Feed by "+pro_user_name)
					    .setLabel("Experience Feed")
					    .build());
					
			}
		});

	}
	
	public void experience_store_feed(int page) {
		long time = System.currentTimeMillis();
		Log.e("url", TOP_MARKET_STORE_REST_URL + page + "&_=" + time);
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		
		client.get(TOP_MARKET_STORE_REST_URL + page + "&_=" + time,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// called before request is started

						// bar.setVisibility(View.VISIBLE);
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
								//Log.e("Success", "-----" + text);
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
						Log.e("FAIL", "" + e.getMessage());
						endlessListView.loadMoreCompleat();
						 diaog.dismiss();
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

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
			List<TopExperiences> exp_feed = sd.getArticles();
			pojolist_exp = new ArrayList<TopExperiencesPojo>();
			pojolist_exp.clear();

			try {
				expr_feed_size = Integer.parseInt(sd.getArticles_count());
			} catch (Exception e) {
				// TODO: handle exception
			}

			for (int j = 0; j < exp_feed.size(); j++) {

				TopExperiences te = exp_feed.get(j);

				try {
					TopExperiencesUser user = te.getUser();
					
					String user_id = user.getId();
					String user_name = user.getFirst_name() + " "
							+ user.getLast_name();
					String user_img = user.getAndroid_api_img();

					ArrayList<TopExperiencesImages> images = te
							.getArticle_images();

					if (images.size() >= 3) {
						TopExperiencesImages ccll = images.get(0);
						TopExperiencesImages ccll1 = images.get(1);
						TopExperiencesImages ccll2 = images.get(2);
						String lookimg = ccll.getMedium_img();
						String img1 = ccll1.getMedium_img();
						String img2 = ccll2.getMedium_img();
						String medium_img_w = ccll.getMedium_img_w();
						String medium_img_h = ccll.getMedium_img_h();
						String likes = te.getLikes_count();
						String hits = te.getHits_text();
					//	Log.e("HITS3_1", hits);
					//	Log.e("HITS3_2", te.getHits_text());
						String title = te.getTitle();
						String description = te.getDescription();
						String is_new = te.getIs_new();
						String idd = te.getId();
						String is_liked = te.getIs_liked();
						String slug = te.getSlug();
						String userid = user.getId();
						String comment_count = te.getArticle_comment_count();

						TopExperiencesPojo tep = new TopExperiencesPojo(
								user_name, user_img, lookimg, img1, img2,
								likes, hits, title, String.valueOf(images
										.size()), idd, description, is_new,
								is_liked, slug, userid, medium_img_w,
								medium_img_h, comment_count);
						pojolist_exp.add(tep);
						pojolist_exp1.add(tep);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
						colorlist1.add(colors[rnd]);
					} else if (images.size() == 2) {
						TopExperiencesImages ccll = images.get(0);
						TopExperiencesImages ccll1 = images.get(1);

						String lookimg = ccll.getLarge_img();
						String img1 = ccll1.getMedium_img();
						String img2 = "na";
						String medium_img_w = ccll.getMedium_img_w();
						String medium_img_h = ccll.getMedium_img_h();
						String likes = te.getLikes_count();
						String hits = te.getHits_text();
					//	Log.e("HITS2_1", hits);
					//	Log.e("HITS2_2", te.getHits_text());
						String title = te.getTitle();
						String description = te.getDescription();
						String is_new = te.getIs_new();
						String idd = te.getId();
						String is_liked = te.getIs_liked();
						String slug = te.getSlug();
						String userid = user.getId();
						String comment_count = te.getArticle_comment_count();

						TopExperiencesPojo tep = new TopExperiencesPojo(
								user_name, user_img, lookimg, img1, img2,
								likes, hits, title, String.valueOf(images
										.size()), idd, description, is_new,
								is_liked, slug, userid, medium_img_w,
								medium_img_h, comment_count);
						pojolist_exp.add(tep);
						pojolist_exp1.add(tep);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
						colorlist1.add(colors[rnd]);
					} else {
						TopExperiencesImages ccll = images.get(0);

						String lookimg = ccll.getLarge_img();
						String img1 = "na";
						String img2 = "na";
						String medium_img_w = ccll.getMedium_img_w();
						String medium_img_h = ccll.getMedium_img_h();
						String likes = te.getLikes_count();
						String hits = te.getHits_text();
					//	Log.e("HITS1_1", hits);
					//	Log.e("HITS1_2", te.getHits_text());
						String title = te.getTitle();
						String description = te.getDescription();
						String is_new = te.getIs_new();
						String idd = te.getId();
						String is_liked = te.getIs_liked();
						String slug = te.getSlug();
						String userid = user.getId();
						String comment_count = te.getArticle_comment_count();

						TopExperiencesPojo tep = new TopExperiencesPojo(
								user_name, user_img, lookimg, img1, img2,
								likes, hits, title, String.valueOf(images
										.size()), idd, description, is_new,
								is_liked, slug, userid, medium_img_w,
								medium_img_h, comment_count);
						pojolist_exp.add(tep);
						pojolist_exp1.add(tep);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
						colorlist1.add(colors[rnd]);
					}
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
				adapter = new TopExperiencesAdapter(getActivity(),pojolist_exp,colorlist);
				endlessListView.setAdapter(adapter);
			//	Log.e("ONPOST", "ONPOST");
				
				diaog.dismiss();

			//	header_view.setVisibility(View.GONE);
				endlessListView.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	
	
}
