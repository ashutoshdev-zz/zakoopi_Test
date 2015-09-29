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
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.activity.SearchStoreActivity;
import com.zakoopi.activity.UserStoreAndExperiences;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.database.HomeSearchAllProductDatabaseHandler;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.HomeSearchSpinnerSectionStructure;
import com.zakoopi.helper.Variables;
import com.zakoopi.search.AllArea;
import com.zakoopi.search.AllProduct;
import com.zakoopi.search.Area;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.searchResult.TopExperiencesAdapter;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.TopMarketStoreAdapter;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class HomeDiscoverFragment extends Fragment {
	LinearLayout lin1, lin2;
	RelativeLayout rel2_pop, rel3_rec, rel_first, rel_search_second;
	TextView txt_market, txt_trends,  some_txt, some_txt1;
	View view_market, view_trends;
	TextView edt_search;
	// private static String ALL_AREA_REST_URL =
	// "http://v3.zakoopi.com/api/markets.json";
	private static String ALL_AREA_REST_URL = "";
	private static String LAST_UPDATE_REST_URL = "";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	String text = "";
	String line = "";
	String productcat;
	List<AllProduct> list = new ArrayList<AllProduct>();
	HomeSearchAllProductDatabaseHandler home_search_product_db;
	HomeSearchAllAreaDatabaseHandler home_search_area_db;
	public static String product_string, area_string, market_string,
			trend_string;
	// MaterialProgressBar bar;
	String name, category, market_name;
	Spinner search_spinner_area;
	Spinner search_spinner_product;
	String[] sectionHeader = { "All Products", "Women", "Men", "Kids" };

	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	ArrayList<HomeSearchSpinnerSectionStructure> sectionList = new ArrayList<HomeSearchSpinnerSectionStructure>();
	HomeSearchSpinnerSectionStructure spinner_section_strcture;

	List<String> productList = new ArrayList<String>();
	ArrayList<String> productSlugList = new ArrayList<String>();
	ArrayList<String> allproduct = new ArrayList<String>();
	List<String> all_area = new ArrayList<String>();
	String productslug;
	int sp_position;
	View view;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;

	Button rel_search_btn;
	String area_slug;
	boolean pro_bool = false;
	public static boolean top_mar = false;
	private SharedPreferences pref_location;
	private String city_name;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	private BroadcastReceiver br;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_search_frag_main, null);
		client = ClientHttp.getInstance(getActivity());
		cd = new ConnectionDetector(getActivity());
		// TopMarketResultsFrag.mList.clear();
		TopMarketStoreAdapter.type = "no";
		findId();

		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getActivity().getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Home Discover");
		t.send(new HitBuilders.AppViewBuilder().build());

		try {

			home_search_product_db = new HomeSearchAllProductDatabaseHandler(
					getActivity());
			home_search_area_db = new HomeSearchAllAreaDatabaseHandler(
					getActivity());

			lin1.setVisibility(View.VISIBLE);
			lin2.setVisibility(View.GONE);

			rel_first.setFocusable(true);

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

			pref_location = getActivity().getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "delhi");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		setFont();

		Variables.areaList = home_search_area_db.getAllAreas1();
		if (Variables.areaList.size() > 0) {

		} else {
			all_area_city();
		}

		pro_bool = true;
		list.clear();
		list = home_search_product_db.getAllProducts();
		new Allproduct().execute();

		if (city_name.equals("Mumbai")) {
			rel3_rec.setVisibility(View.GONE);
		} else {
			rel3_rec.setVisibility(View.VISIBLE);
		}

		tab_click();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment top_market = new HomeSearchTopMarket();
		Bundle bundle = new Bundle();
		bundle.putBoolean("market", false);
		top_market.setArguments(bundle);
		ft.replace(R.id.lin_top_market, top_market);
		ft.commit();

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

	@Override
	public void onResume() {
		super.onResume();
		try {

			isInternetPresent = cd.isConnectingToInternet();
			if (isInternetPresent) {
				// countDownTimer = new MyCountDownTimer(startTime, interval);
				// countDownTimer.start();
			}

			checkInternetConnection();

		} catch (Exception e) {
			// TODO: handle exception
		}
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

					if (state == State.CONNECTED) {

						/*Toast.makeText(getActivity(),
								"Internet connection is onDIS",
								Toast.LENGTH_LONG).show();*/

						all_area_city();

					} else {

						/*Toast.makeText(getActivity(),
								"Internet connection is Off", Toast.LENGTH_LONG)
								.show();*/

						AreaAdapterClass adp = new AreaAdapterClass(
								getActivity(),
								home_search_area_db.getAllAreas1());
						search_spinner_area.setAdapter(adp);
						adp.notifyDataSetChanged();

					}
				}
			};

			final IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			getActivity().registerReceiver((BroadcastReceiver) br, intentFilter);

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

	/**
	 * Find ID's
	 * 
	 * @return void
	 */

	public void findId() {
		rel2_pop = (RelativeLayout) view.findViewById(R.id.rel2_pop);
		rel3_rec = (RelativeLayout) view.findViewById(R.id.rel3_rec);
		rel_first = (RelativeLayout) view.findViewById(R.id.rel_first);
		rel_search_second = (RelativeLayout) view
				.findViewById(R.id.rel_search_second);
		txt_market = (TextView) view.findViewById(R.id.text_market);
		txt_trends = (TextView) view.findViewById(R.id.text_trends);
		some_txt = (TextView) view.findViewById(R.id.some_txt);
		some_txt1 = (TextView) view.findViewById(R.id.some_txt1);
		edt_search = (TextView) view.findViewById(R.id.edt_search);
		view_market = (View) view.findViewById(R.id.view_market);
		view_trends = (View) view.findViewById(R.id.view_trends);
		search_spinner_product = (Spinner) view
				.findViewById(R.id.spinner_all_products);
		search_spinner_area = (Spinner) view
				.findViewById(R.id.spinner_all_areas);
		lin1 = (LinearLayout) view.findViewById(R.id.lin_top_market);
		lin2 = (LinearLayout) view.findViewById(R.id.lin_top_trends);
		rel_search_btn = (Button) view.findViewById(R.id.rel_search_btn);
		edt_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent in = new Intent(getActivity(), SearchStoreActivity.class);
				in.putExtra("SearchStore", "Discover");
				startActivity(in);

				Tracker t = ((UILApplication) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Seach by store name")
						.setAction("Search store by " + pro_user_name)
						.setLabel("Home Discover").build());

			}
		});

		rel_search_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					Tracker t = ((UILApplication) getActivity()
							.getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Seach using product & market")
							.setAction(
									"Search using product & market by "
											+ pro_user_name)
							.setLabel("Home Discover").build());

					try {

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

						if (productcat.equals("Women")
								|| productcat.equals("Men")
								|| productcat.equals("Kids")) {


						} else {
							SharedPreferences clientcity = getActivity()
									.getSharedPreferences("clientcity", 10);
							String cityslug = clientcity.getString("cityslug",
									"delhi-ncr");

							if (area_slug.equals("All Areas")
									&& productslug.equals("All Products")) {

								// add stuff here
								Intent i = new Intent(getActivity(),
										UserStoreAndExperiences.class);
								i.putExtra("search", "discover");
								i.putExtra("product_slug", "all-products");

								i.putExtra("area_slug", cityslug);

								startActivity(i);

							} else if (area_slug.equals("All Areas")
									&& !productslug.equals("All Products")) {

								// add stuff here
								Intent i = new Intent(getActivity(),
										UserStoreAndExperiences.class);
								i.putExtra("search", "discover");
								i.putExtra("product_slug", productslug);
								i.putExtra("area_slug", cityslug);

								startActivity(i);

							} else if (!area_slug.equals("All Areas")
									&& productslug.equals("All Products")) {

								// add stuff here
								Intent i = new Intent(getActivity(),
										UserStoreAndExperiences.class);
								i.putExtra("search", "discover");
								i.putExtra("product_slug", "all-products");
								i.putExtra("area_slug", area_slug);

								startActivity(i);

							}

							else {

								// add stuff here
								Intent i = new Intent(getActivity(),
										UserStoreAndExperiences.class);
								i.putExtra("search", "discover");
								i.putExtra("product_slug", productslug);
								i.putExtra("area_slug", area_slug);

								startActivity(i);
							}
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(getActivity(), "No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		});

	}


	/**
	 * Set Font on TextView
	 */

	public void setFont() {

			txt_market.setTypeface(typeface_semibold);
			txt_trends.setTypeface(typeface_semibold);
			some_txt.setTypeface(typeface_regular);
			some_txt1.setTypeface(typeface_regular);
			edt_search.setTypeface(typeface_semibold);
		
	}

	/**
	 * tab_click()
	 * 
	 * @return void
	 */
	public void tab_click() {

		rel3_rec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					isInternetPresent = cd.isConnectingToInternet();
					// check for Internet status
					if (isInternetPresent) {
						// Internet Connection is Present
						lin1.setVisibility(View.GONE);
						lin2.setVisibility(View.VISIBLE);

						txt_trends.setTextColor(Color.parseColor("#4d4d49"));
						txt_market.setTextColor(Color.parseColor("#acacac"));
						view_trends.setBackgroundColor(Color
								.parseColor("#26B3AD"));
						view_market.setBackgroundColor(Color
								.parseColor("#acacac"));

						FragmentManager fm = getFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Fragment top_trends = new HomeSearchTopTrends();
						Bundle bundle = new Bundle();
						bundle.putBoolean("trend", false);
						top_trends.setArguments(bundle);
						ft.replace(R.id.lin_top_trends, top_trends);
						ft.commit();

						Tracker t = ((UILApplication) getActivity()
								.getApplication())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Click on Top Trends")
								.setAction(
										"View Top Trends by " + pro_user_name)
								.setLabel("Home Discover").build());
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

		rel2_pop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					lin1.setVisibility(View.VISIBLE);
					lin2.setVisibility(View.GONE);

					txt_market.setTextColor(Color.parseColor("#4d4d49"));
					txt_trends.setTextColor(Color.parseColor("#acacac"));
					view_market.setBackgroundColor(Color.parseColor("#26B3AD"));
					view_trends.setBackgroundColor(Color.parseColor("#acacac"));

					FragmentManager fm = getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					Fragment top_market = new HomeSearchTopMarket();
					Bundle bundle = new Bundle();
					bundle.putBoolean("market", false);
					top_market.setArguments(bundle);
					ft.replace(R.id.lin_top_market, top_market);
					ft.commit();

					Tracker t = ((UILApplication) getActivity()
							.getApplication())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Click on Top Market")
							.setAction("View Top Market by " + pro_user_name)
							.setLabel("Home Discover").build());

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		
	}

	public class Allproduct extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				productList.clear();
				productSlugList.clear();
				allproduct.clear();
				sectionList.clear();

				for (int i = 0; i < sectionHeader.length; i++) {

					productList = home_search_product_db
							.listDataByCategory(sectionHeader[i]);

					for (int j = -1; j < productList.size(); j++) {
						spinner_section_strcture = new HomeSearchSpinnerSectionStructure();

						if (j == -1) {

							spinner_section_strcture
									.setSectionName(sectionHeader[i]);
							spinner_section_strcture.setSectionValue("");
							sectionList.add(spinner_section_strcture);
							productSlugList.add("All Products");
							allproduct.add(sectionHeader[i]);
						} else {

							// Log.e("product", productList.get(j));
							spinner_section_strcture.setSectionName("");
							spinner_section_strcture
									.setSectionValue(productList.get(j));
							sectionList.add(spinner_section_strcture);
							productSlugList
									.add(HomeSearchAllProductDatabaseHandler.slug_list
											.get(j));
							allproduct.add(productList.get(j));
						}

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {

				search_spinner_product
						.setAdapter(new AdapterClass(sectionList));

				new AdapterClass(sectionList).notifyDataSetChanged();

				search_spinner_product
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(
									AdapterView<?> parentView,
									View selectedItemView, int position, long id) {
								// your code here
								productcat = allproduct.get(position);
								productslug = productSlugList.get(position);
								product_string = allproduct.get(position);

							}

							@Override
							public void onNothingSelected(
									AdapterView<?> parentView) {
								// your code here
							}

						});
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	/**
	 * 
	 * {@code} class AdapterClass extends BaseAdapter
	 * 
	 * @return view
	 */
	public class AdapterClass extends BaseAdapter {

		ArrayList<HomeSearchSpinnerSectionStructure> sectionList1 = new ArrayList<HomeSearchSpinnerSectionStructure>();
		LayoutInflater inf;

		public AdapterClass(ArrayList<HomeSearchSpinnerSectionStructure> list) {
			super();
			sectionList1 = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sectionList1.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint({ "ResourceAsColor", "ViewHolder" })
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			View vi = arg1;
			inf = LayoutInflater.from(getActivity());
			vi = inf.inflate(R.layout.home_search_spinner_item, null);
			TextView textView = (TextView) vi.findViewById(R.id.txt_product);
			RelativeLayout relativeLayout = (RelativeLayout) vi
					.findViewById(R.id.rel_1);

			try {

				if (sectionList1.get(arg0).getSectionValue() != null
						&& sectionList1.get(arg0).getSectionValue()
								.equalsIgnoreCase("")) {
					textView.setText(sectionList1.get(arg0).getSectionName());
					relativeLayout.setBackgroundColor(Color.TRANSPARENT);
					textView.setTextColor(Color.BLACK);
					textView.setTypeface(typeface_semibold);
					if (textView.getText().equals("All Products")) {
						textView.setTextColor(Color.GRAY);
						relativeLayout.setBackgroundColor(Color.WHITE);
					}

				} else {
					textView.setText(sectionList1.get(arg0).getSectionValue());
					relativeLayout.setBackgroundColor(Color.WHITE);
					textView.setTypeface(typeface_regular);
					textView.setTextColor(Color.GRAY);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return vi;
		}
	}

	/**
	 * class AreaAdapterClass extends BaseAdapter
	 * 
	 * @author ZakoopiUser
	 * @return view
	 */

	class AreaAdapterClass extends BaseAdapter {

		Context ctx;
		List<String> list;
		LayoutInflater inf;

		public AreaAdapterClass(Context ctx, List<String> list) {

			this.ctx = ctx;
			this.list = list;
			inf = (LayoutInflater) this.ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {

				convertView = inf.inflate(R.layout.home_search_spinner_item,
						null);
				holder = new ViewHolder();

				holder.txt_area = (TextView) convertView
						.findViewById(R.id.txt_product);

				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();

			}

			try {

				holder.txt_area.setText(list.get(position));
				holder.txt_area.setTypeface(typeface_regular);
				holder.txt_area.setTextColor(Color.GRAY);

				if (holder.txt_area.getText().equals("All Areas")) {
					holder.txt_area.setTextColor(Color.GRAY);
					holder.txt_area.setTypeface(typeface_semibold);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return convertView;
		}

		class ViewHolder {

			TextView txt_area;
		}

	}

	/**
	 * {@code} all_area()
	 * 
	 * @return void
	 */
	public void all_area_city() {
		ALL_AREA_REST_URL = getString(R.string.base_url) + "markets.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_AREA_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
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
					allArea_showData_city(text2);

				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

			}
		});
	}

	/**
	 * 
	 * @allArea_showData data1
	 * @return void
	 */
	public void allArea_showData_city(String data1) {

		markets.clear();
		home_search_area_db.allDelete();
		home_search_area_db.addAllArea(new AllArea("All Areas", "All Areas"));
		Gson gson1 = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data1));
		jsonReader.setLenient(true);

		Area area1 = gson1.fromJson(jsonReader, Area.class);
		markets = area1.getMarkets();
		try {

			for (int i = 0; i < markets.size(); i++) {

				market_name = markets.get(i).get_market_name();
				String slug_name = markets.get(i).getUrl_slug();
				home_search_area_db.addAllArea(new AllArea(market_name,
						slug_name));

			}

			Variables.areaList.clear();
			Variables.areaList = home_search_area_db.getAllAreas1();

		
			AreaAdapterClass adp = new AreaAdapterClass(getActivity(),
					home_search_area_db.getAllAreas1());
			
			search_spinner_area.setAdapter(adp);
			adp.notifyDataSetChanged();

			search_spinner_area
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parentView,
								View selectedItemView, int position, long id) {

							area_slug = HomeSearchAllAreaDatabaseHandler.area_slug_list
									.get(position);
							area_string = Variables.areaList.get(position);

						}

						@Override
						public void onNothingSelected(AdapterView<?> parentView) {
							// your code here
						}

					});

		} catch (Exception e) {
			// TODO: handle exception
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
