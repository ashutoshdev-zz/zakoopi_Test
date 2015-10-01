package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.StoreActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zakoopi.R;
import com.zakoopi.BrowseSearchModel.IntentDataSearchModel;
import com.zakoopi.BrowseSearchModel.MainIntentDataSearchModel;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.database.HomeSearchAllProductDatabaseHandler;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.HomeSearchSpinnerSectionStructure;
import com.zakoopi.location.model.VersionModel;
import com.zakoopi.search.AllProduct;
import com.zakoopi.search.Product;
import com.zakoopi.search.SearchUpdate;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.search.offerings;
import com.zakoopi.search.updateSearch;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.GPSTracker;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class SplashScreen extends Activity {

	
	 private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	    String SENDER_ID = "529314510718";
	    static final String TAG = "L2C";
	    GoogleCloudMessaging gcm;
	    SharedPreferences prefs_gcm;
	    Context context;
	    String regid;
	    
	private static String ALL_PRODUCT_REST_URL = "";
	private static String LAST_UPDATE_REST_URL = "";
	private static String MAIN_WEBSERVICE_REST_URL = "";

	AsyncHttpClient client;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	final static int DEFAULT_TIMEOUT = 20 * 1000;
	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	HomeSearchAllAreaDatabaseHandler home_search_area_db;
	boolean bool = true;
	GPSTracker gps;
	String market_name;
	String text = "";
	String line = "";
	List<AllProduct> list = new ArrayList<AllProduct>();
	HomeSearchAllProductDatabaseHandler home_search_product_db;
	String name, category;
	String[] sectionHeader = { "Men", "Women", "Kids" };
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	ArrayList<HomeSearchSpinnerSectionStructure> sectionList = new ArrayList<HomeSearchSpinnerSectionStructure>();
	HomeSearchSpinnerSectionStructure spinner_section_strcture;
	List<String> productList = new ArrayList<String>();
	ArrayList<updateSearch> updateSearch = new ArrayList<updateSearch>();

	SharedPreferences prefs, prefs1, pref_location;
	Editor editor, editor_loc;

	String save_model_trends, save_time_trends;
	public static String city;
	String model, time;
	String userid, city_name;
	String version;
	String data = "no";
	RelativeLayout rel_bt,rel_pro,rel_net;
	TextView txt_try;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash_screen);

		// creating connection detector class instance
		cd = new ConnectionDetector(SplashScreen.this);
		
		

		rel_bt = (RelativeLayout) findViewById(R.id.rel_bt);
		rel_pro = (RelativeLayout) findViewById(R.id.rel_pro);
		rel_net = (RelativeLayout) findViewById(R.id.rel_net);
		txt_try = (TextView) findViewById(R.id.txt_try);
		
		txt_try.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reTry();
			}
		});
		try {

			home_search_product_db = new HomeSearchAllProductDatabaseHandler(
					getApplicationContext());

			

			prefs = getSharedPreferences("PREF", MODE_PRIVATE);
			prefs1 = getSharedPreferences("User_detail", 0);
			pref_location = getSharedPreferences("location", 1);
			editor = prefs.edit();
			editor_loc = pref_location.edit();

			model = prefs.getString("model", "NA");
			time = prefs.getString("time", "N/A");
			save_model_trends = prefs.getString("model_trends", "ghft");
			save_time_trends = prefs.getString("time_trends", "ghfty");
			userid = prefs1.getString("user_id", "NA");

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
			e.printStackTrace();
		}

		ALL_PRODUCT_REST_URL = getString(R.string.base_url) + "offerings.json";
		// onNewIntent(getIntent());
		
		/**
		 * 
		 * GCM CODE
		 */
		
		 prefs_gcm = getSharedPreferences("GCM", 0);
	        context = getApplicationContext();

	        if(checkPlayServices()){

	            new Register().execute();

	        }else{
	          //  Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
	        }
	        
	}
	
	private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
               // Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;

    }
	
	private class Register extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    regid = gcm.register(SENDER_ID);
Log.e("Rig", regid);
                    SharedPreferences.Editor edit = prefs_gcm.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();
                    
                }

                return  regid;

            } catch (IOException ex) {
                return "Fails";

            }
        }
        @Override
        protected void onPostExecute(String json) {

        }
    }

	protected void onNewIntent(Intent intent) {
		String action = intent.getAction();
		data = intent.getDataString();
		if (Intent.ACTION_VIEW.equals(action) && data != null) {

			/*
			 * Toast.makeText(getApplicationContext(), " app launched = " +
			 * data, Toast.LENGTH_SHORT).show();
			 */
			client = ClientHttp.getInstance(SplashScreen.this);
			getAllData(data);

		} else {
			
			city_name = pref_location.getString("city", "123");

			if (!city_name.equals("123")) {

				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {

					client = ClientHttp.getInstance(SplashScreen.this);
					last_play();
				} else {
					
					rel_bt.setVisibility(View.VISIBLE);
					rel_pro.setVisibility(View.GONE);
					rel_net.setVisibility(View.VISIBLE);
				}

			}
		}
	}

	
	@Override
	protected void onResume() {

		super.onResume();
		isInternetPresent = cd.isConnectingToInternet();
		// check for Internet status
		if (isInternetPresent) {
			// create class object
			gps = new GPSTracker(SplashScreen.this);

			// check if GPS enabled
			if (gps.canGetLocation()) {
				
				  double latitude = gps.getLatitude(); 
				  double longitude = gps.getLongitude();
				
				  
				  /**
				   * Mumbai Lat-LONG
				   */
				/*double latitude = 22.572646;
				double longitude = 88.363895;*/

				try {
					Geocoder geocoder = new Geocoder(SplashScreen.this,
							Locale.getDefault());
					List<Address> addresses = geocoder.getFromLocation(
							latitude, longitude, 1);
					city = addresses.get(0).getLocality();
					CountDownTimer countDownTimer = new MyCountDownTimer(5*1000, 1000);
					countDownTimer.start();
					
					Log.e("CITY", city);
					if (city.contains("Mumbai")) {
						editor_loc.putString("city", "Mumbai");
						editor_loc.commit();

					} else {

						editor_loc.putString("city", "Delhi");
						editor_loc.commit();

					}
					rel_bt.setVisibility(View.VISIBLE);
					rel_pro.setVisibility(View.VISIBLE);
					rel_net.setVisibility(View.GONE);
					onNewIntent(getIntent());

				} catch (Exception e) {

				}

			} else {
			Log.e("CITY", "LOG_GPS1");
				city_name = pref_location.getString("city", "123");

				if (city_name.equals("123")) {
					rel_bt.setVisibility(View.GONE);
					
					showSettingsAlert();

				} else {
					rel_bt.setVisibility(View.VISIBLE);
					rel_pro.setVisibility(View.VISIBLE);
					rel_net.setVisibility(View.GONE);
					onNewIntent(getIntent());

				}
			}
		} else {
			
			if (userid.equals("NA")) {

				Intent login_activity1 = new Intent(
						getApplicationContext(), LoginActivity.class);
				login_activity1.putExtra("data", "no");
				startActivity(login_activity1);
				finish();

			} else {

				Intent main_activity4 = new Intent(
						getApplicationContext(), MainActivity.class);
				startActivity(main_activity4);
				finish();

			}
		}
	}

	public void last_update() {
		LAST_UPDATE_REST_URL = getString(R.string.base_url)
				+ "lastUpdates.json";

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(LAST_UPDATE_REST_URL, new AsyncHttpResponseHandler() {

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
					while ((line = bufferedReader.readLine()) != null) {

						text = text + line;
					}
					all_update(text);

				} catch (Exception e) {

				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				 rel_bt.setVisibility(View.VISIBLE);
					rel_pro.setVisibility(View.GONE);
					rel_net.setVisibility(View.VISIBLE);
				// Log.e("FAIL", "FAIL");
			}
		});
	}

	public void all_update(String data) {
		String timeStamp = "";
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		SearchUpdate searchUpdate = gson.fromJson(jsonReader,
				SearchUpdate.class);
		updateSearch = searchUpdate.getUpdate();

		for (int i = 0; i < updateSearch.size(); i++) {
			updateSearch ser = updateSearch.get(i);

			try {

				if (ser.get_model().equals("Trends")) {
					timeStamp = ser.get_timestamp();
					if (!time.equals(ser.get_timestamp())) {
						editor.putString("model", ser.get_model());
						editor.putString("time", ser.get_timestamp());
						editor.commit();

					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		if (time.equals(timeStamp)) {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					if (userid.equals("NA")) {

						Intent login_activity1 = new Intent(
								getApplicationContext(), LoginActivity.class);
						login_activity1.putExtra("data", "no");
						startActivity(login_activity1);
						finish();

					} else {

						Intent main_activity1 = new Intent(
								getApplicationContext(), MainActivity.class);
						startActivity(main_activity1);
						finish();

					}
				}
			}, 100);
		} else {
			// Log.e("ffff", "hello");
			all_product();
		}
	}

	public void all_product() {
		// Log.e("pro-url", ALL_PRODUCT_REST_URL);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_PRODUCT_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String txt1 = "";
					String txt2 = "";
					while ((txt1 = br.readLine()) != null) {

						txt2 = txt2 + txt1;
					}
					allProduct_showData(txt2);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				 rel_bt.setVisibility(View.VISIBLE);
					rel_pro.setVisibility(View.GONE);
					rel_net.setVisibility(View.VISIBLE);
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});
	}

	public void allProduct_showData(String data) {

		home_search_product_db.allDelete();

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		Product ppp = gson.fromJson(jsonReader, Product.class);
		List<offerings> offerings = ppp.getOfferings();

		try {

			for (int i = 0; i < offerings.size(); i++) {

				name = offerings.get(i).get_name();
				category = offerings.get(i).get_category();
				String slu = offerings.get(i).getSlug();
				// Log.e("name", name+"=="+category+"=="+slu);
				home_search_product_db.addAllProduct(new AllProduct(name,
						category, slu));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (userid.equals("NA")) {

			Intent login_activity2 = new Intent(getApplicationContext(),
					LoginActivity.class);
			login_activity2.putExtra("data", "no");
			startActivity(login_activity2);
			finish();

		} else {
			Intent main_activity2 = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(main_activity2);
			finish();

		}
	}

	/**
	 * MainWebService()
	 */

	public void MainWebservice() {
		// Log.e("CITY", city_name);

		MAIN_WEBSERVICE_REST_URL = getString(R.string.base_url)
				+ "start/setClientLocation/" + city_name + ".json";

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(MAIN_WEBSERVICE_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				if (bool == true) {
					bool = false;
					MainWebservice();
				} else {

					last_update();
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

	public void last_play() {
		String LAST_UPDATE_PLAY_REST_URL = getString(R.string.base_url)
				+ "Common/appversionPopup.json";

	//	Log.e("LAST_PLAY", LAST_UPDATE_PLAY_REST_URL);
		String android_id = Secure.getString(
				SplashScreen.this.getContentResolver(), Secure.ANDROID_ID);

		String model = android.os.Build.MODEL;
		String manuf = android.os.Build.MANUFACTURER;
		String brand = android.os.Build.BRAND;
		String version = android.os.Build.VERSION.RELEASE;

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("version", "1.5");
		params.put("android_id", android_id);
		params.put("device_info", manuf + " " + model + " " + brand + " "
				+ version);
		params.put("device_id", telephonyManager.getDeviceId());
		client.setTimeout(DEFAULT_TIMEOUT);
		client.post(LAST_UPDATE_PLAY_REST_URL, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {

						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						try {
							String text = "";
							String line = "";
							BufferedReader bufferedReader = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							while ((line = bufferedReader.readLine()) != null) {

								text = text + line;
							}
							all_play(text);
							 //Log.e("sUCCESS", text);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {

						 Log.e("FAIL", "FAIL" + arg0);
						 rel_bt.setVisibility(View.VISIBLE);
							rel_pro.setVisibility(View.GONE);
							rel_net.setVisibility(View.VISIBLE);
					}
				});
	}

	public void all_play(String data) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		VersionModel ver = gson.fromJson(jsonReader, VersionModel.class);

		version = ver.getVersion();

		if (version.equals("0")) {

			last_update();

		} else {

			customDailog();

		}

	}

	public void customDailog() {

		final Dialog dd = new Dialog(SplashScreen.this);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dd.setContentView(R.layout.playstore_popup);
		dd.show();

		final TextView txt_message = (TextView) dd
				.findViewById(R.id.txt_message);

		final RelativeLayout rel_go_home = (RelativeLayout) dd
				.findViewById(R.id.rel_go_home);
		/*final RelativeLayout rel_not_now = (RelativeLayout) dd
				.findViewById(R.id.rel_not_now);*/
		final TextView txt_go_home = (TextView) dd
				.findViewById(R.id.txt_go_home);
		/*final TextView txt_not_now = (TextView) dd
				.findViewById(R.id.txt_not_now);*/
		
		txt_message.setTypeface(typeface_semibold);
		txt_go_home.setTypeface(typeface_bold);
	//	txt_not_now.setTypeface(typeface_bold);

		/*rel_not_now.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				last_update();
				dd.cancel();
			}
		});*/
		
		rel_go_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dd.dismiss();
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=com.zakoopi"));
					startActivity(intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

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
						finish();
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zakoopi.activity.SplashScreen.getAllData()
	 */

	public void getAllData(final String data) {
		String myintenturl = getString(R.string.base_url)
				+ "Common/androidIntent.json";

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {
			params.put("url", data);

		} catch (Exception e) {
			e.printStackTrace();
		}

		client.post(myintenturl, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				super.onStart();
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

					getIntentData(text, data);
					//Log.e("BEO", text+"--"+data);

				} catch (Exception e) {

				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}

	public void getIntentData(String data, String data1) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		MainIntentDataSearchModel searchdata = gson.fromJson(jsonReader,
				MainIntentDataSearchModel.class);

		IntentDataSearchModel getdata = searchdata.getData();
		String entity = getdata.getEntity();
		String code = getdata.getCode();
		String slug = getdata.getSlug();
		String id = getdata.getId();

		// Log.e("daat", entity + "==" + code + "===" + slug + "==" + id);
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

			if (code.equals("0")) {

				if (entity.equals("STR")) {

					if (userid.equals("NA")) {

						Intent login_activity1 = new Intent(
								getApplicationContext(), LoginActivity.class);
						login_activity1.putExtra("data", data1);
						startActivity(login_activity1);
						finish();

					} else {
						Intent in = new Intent(SplashScreen.this,
								StoreActivity.class);
						in.putExtra("store_id", id);
						startActivity(in);
						finish();
					}

				} else if (entity.equals("ART")) {

					if (userid.equals("NA")) {

						Intent login_activity1 = new Intent(
								getApplicationContext(), LoginActivity.class);
						login_activity1.putExtra("data", data1);
						startActivity(login_activity1);
						finish();

					} else {
						Intent in = new Intent(SplashScreen.this,
								ArticleView.class);
						in.putExtra("article_id", id);
						in.putExtra("username", "nouser");
						startActivity(in);
						finish();
					}

				} else if (entity.equals("LBK")) {

					if (userid.equals("NA")) {

						Intent login_activity1 = new Intent(
								getApplicationContext(), LoginActivity.class);
						login_activity1.putExtra("data", data1);
						startActivity(login_activity1);
						finish();

					} else {
						Intent in = new Intent(SplashScreen.this,
								LookbookView1.class);
						in.putExtra("lookbook_id", id);
						in.putExtra("username", "nouser");
						startActivity(in);
						finish();

					}
				} else if (entity.equals("MEM")) {

					if (userid.equals("NA")) {

						Intent login_activity1 = new Intent(
								getApplicationContext(), LoginActivity.class);
						login_activity1.putExtra("data", data1);
						startActivity(login_activity1);
						finish();

					} else {
						Intent in = new Intent(SplashScreen.this,
								OtherUserProfile.class);
						in.putExtra("user_id", id);
						startActivity(in);
						finish();

					}
				} else if (entity.equals("SRC")) {

					if (userid.equals("NA")) {

						Intent login_activity1 = new Intent(
								getApplicationContext(), LoginActivity.class);
						login_activity1.putExtra("data", data1);
						startActivity(login_activity1);
						finish();

					} else {
						Intent in = new Intent(SplashScreen.this,
								UserStoreAndExperiences.class);
						in.putExtra("search", "top_mark");
						in.putExtra("market_slug", slug);
						startActivity(in);
						finish();
					}
				}

			} else {

				if (userid.equals("NA")) {

					Intent login_activity1 = new Intent(
							getApplicationContext(), LoginActivity.class);
					login_activity1.putExtra("data", data1);
					startActivity(login_activity1);
					finish();

				} else {
					Intent in = new Intent(SplashScreen.this,
							MainActivity.class);
					startActivity(in);
					finish();
				}
			}
		} else {

			showAlertDialog(SplashScreen.this, "No Internet Connection",
					"You don't have internet connection.", false);
		}
	}
	
	
	public void reTry() {
		 rel_bt.setVisibility(View.VISIBLE);
			rel_pro.setVisibility(View.VISIBLE);
			rel_net.setVisibility(View.GONE);
		client = ClientHttp.getInstance(SplashScreen.this);
		last_play();
		
	}
	
	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {

		}

		@Override
		public void onTick(long millisUntilFinished) {
			Log.e("TIME", ""+millisUntilFinished/1000);
		}
	}
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreen.this);
   	 alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle("Zakoopi needs your city location");
 
        // Setting Dialog Message
        alertDialog.setMessage("Please Enable location.");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	startActivity(intent);
            	 dialog.cancel();
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
            	//((Activity) mContext).finish();
            	/*pref_location = mContext.getSharedPreferences("location", 1);
    			editor_loc = pref_location.edit();
    			*/
            	editor_loc.putString("city", "Delhi");
				editor_loc.commit();
				Intent login_activity1 = new Intent(
						SplashScreen.this, LoginActivity.class);
				login_activity1.putExtra("data", "no");
				startActivity(login_activity1);
				finish();
            	
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}
}
