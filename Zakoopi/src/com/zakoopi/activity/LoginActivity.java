package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.StoreActivity;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zakoopi.R;
import com.zakoopi.BrowseSearchModel.IntentDataSearchModel;
import com.zakoopi.BrowseSearchModel.MainIntentDataSearchModel;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.ProgressHUD;
import com.zakoopi.helper.Variables;
import com.zakoopi.search.AllArea;
import com.zakoopi.search.Area;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.user.model.ClientLocationModel;
import com.zakoopi.user.model.MainClient;
import com.zakoopi.user.model.User;
import com.zakoopi.user.model.UserDetails;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class LoginActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnCancelListener {

	CallbackManager callbackmanager;
	String str_id;
	TextView title, login_with, line1, line2;
	ImageView img1, img2, img3;// , img4;
	private ViewFlipper viewFlipper;
	private float lastX;
	ImageView fb, gp;
	ProgressHUD mProgressHUD;
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	// private static final String LOOKBOOK_URL =
	// "http://v3.zakoopi.com/api/users/androidlogin.json";
	private static String LOOKBOOK_URL = "";
	String password;
	// for g+ login
	private static final int RC_SIGN_IN = 0;
	private static final String TAG = "MainActivity";
	private static final int PROFILE_PIC_SIZE = 400;
	private GoogleApiClient mGoogleApiClient;
	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	private SharedPreferences pref_location,prefs_gcm;
	private String city_name, city_name_user;
	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	HomeSearchAllAreaDatabaseHandler home_search_area_db;
	private static String ALL_AREA_REST_URL = "";
	String market_name;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	String data, gcm_id;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.landing);

		  cd = new ConnectionDetector(getApplicationContext());
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Login on Zakoopi");
		t.send(new HitBuilders.AppViewBuilder().build());

		Intent i = getIntent();
		data = i.getStringExtra("data");
		
		MainActivity.mycolorlist.clear();
		MainActivity.mypojolist.clear();
		MainActivity.mypage = 1;

		MainActivity.mycolorlist1.clear();
		MainActivity.mypojolist1.clear();
		MainActivity.mypage1 = 1;

		pref_location = getSharedPreferences("location", 1);
		 prefs_gcm = getSharedPreferences("GCM", 0);
		 gcm_id = prefs_gcm.getString("REG_ID", "asgdj");
		city_name = pref_location.getString("city", "123");
		
		client = ClientHttp.getInstance(LoginActivity.this);
		
		// Initializing google plus api client
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		city_name_user = SplashScreen.city;

		title = (TextView) findViewById(R.id.tiltle);
		login_with = (TextView) findViewById(R.id.textView1);
		line1 = (TextView) findViewById(R.id.textView2);
		line2 = (TextView) findViewById(R.id.textView3);
		img1 = (ImageView) findViewById(R.id.imageView2);
		img2 = (ImageView) findViewById(R.id.imageView3);
		img3 = (ImageView) findViewById(R.id.imageView4);
		// img4 = (ImageView) findViewById(R.id.imageView5);

		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		viewFlipper.setDisplayedChild(2);
		fb = (ImageView) findViewById(R.id.imageView6);
		gp = (ImageView) findViewById(R.id.imageView7);

		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(
				LoginActivity.this.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface
				.createFromAsset(LoginActivity.this.getAssets(),
						"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(
				LoginActivity.this.getAssets(), "fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface
				.createFromAsset(LoginActivity.this.getAssets(),
						"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(
				LoginActivity.this.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		try {

			title.setTypeface(typeface_regular);
			login_with.setTypeface(typeface_regular);
			line1.setTypeface(typeface_regular);
			line2.setTypeface(typeface_regular);
		} catch (Exception e) {
			// TODO: handle exception
		}
		LOOKBOOK_URL = getString(R.string.base_url) + "users/androidlogin.json";

		fb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				  isInternetPresent = cd.isConnectingToInternet();
		             // check for Internet status
		                if (isInternetPresent) {
		                    // Internet Connection is Present
		                	onFblogin();
		                } else {
		                    // Internet connection is not present
		                    // Ask user to connect to Internet
		                    showAlertDialog(LoginActivity.this, "No Internet Connection",
		                            "You don't have internet connection.", false);
		                }
		                
		                
				

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Login via Facebook on Zakoopi")
						.setAction("Facebook Login")
						.setLabel("Login via Facebook").build());
			}
		});

		gp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				isInternetPresent = cd.isConnectingToInternet();
	             // check for Internet status
	                if (isInternetPresent) {
	                    // Internet Connection is Present
	                	signInWithGplus();
	                } else {
	                    // Internet connection is not present
	                    // Ask user to connect to Internet
	                    showAlertDialog(LoginActivity.this, "No Internet Connection",
	                            "You don't have internet connection.", false);
	                }
	                
				

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Login via Google+ on Zakoopi")
						.setAction("Google+ Login")
						.setLabel("Login via Google+").build());
			}
		});

		all_area();
		getLocation();

	}

	// for g+
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
		GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStart(
				this);
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
		GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStart(
				this);
	}

	// Using the following method, we will handle all screen swaps.
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {

		case MotionEvent.ACTION_DOWN:
			lastX = touchevent.getX();
			break;
		case MotionEvent.ACTION_UP:
			float currentX = touchevent.getX();

			// Handling left to right screen swap.
			if (lastX < currentX) {

				// If there aren't any other children, just break.
				if (viewFlipper.getDisplayedChild() == (viewFlipper
						.getChildCount() - 1))
					break;

				// Next screen comes in from left.
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
				// Current screen goes out from right.
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

				// Display next screen.
				viewFlipper.showNext();

				if (viewFlipper.getDisplayedChild() == 2) {

					img1.setImageResource(R.drawable.slider_dot_blue);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_grey);
					// img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title1));

				} else if (viewFlipper.getDisplayedChild() == 1) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_blue);
					img3.setImageResource(R.drawable.slider_dot_grey);
					// img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title2));

				} else if (viewFlipper.getDisplayedChild() == 0) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_blue);
					// img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title3));

				} /*
				 * else if (viewFlipper.getDisplayedChild() == 0) {
				 * 
				 * img1.setImageResource(R.drawable.slider_dot_grey);
				 * img2.setImageResource(R.drawable.slider_dot_grey);
				 * img3.setImageResource(R.drawable.slider_dot_grey);
				 * img4.setImageResource(R.drawable.slider_dot_blue);
				 * title.setText(getString(R.string.title4)); }
				 */

			}

			// Handling right to left screen swap.
			if (lastX > currentX) {

				// If there is a child (to the left), kust break.
				if (viewFlipper.getDisplayedChild() == 0)
					break;

				// Next screen comes in from right.
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
				// Current screen goes out from left.
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

				// Display previous screen.
				viewFlipper.showPrevious();
				try {

					if (viewFlipper.getDisplayedChild() == 2) {

						img1.setImageResource(R.drawable.slider_dot_blue);
						img2.setImageResource(R.drawable.slider_dot_grey);
						img3.setImageResource(R.drawable.slider_dot_grey);
						// img4.setImageResource(R.drawable.slider_dot_grey);
						title.setText(getString(R.string.title1));

					} else if (viewFlipper.getDisplayedChild() == 1) {

						img1.setImageResource(R.drawable.slider_dot_grey);
						img2.setImageResource(R.drawable.slider_dot_blue);
						img3.setImageResource(R.drawable.slider_dot_grey);
						// img4.setImageResource(R.drawable.slider_dot_grey);
						title.setText(getString(R.string.title2));

					} else if (viewFlipper.getDisplayedChild() == 0) {

						img1.setImageResource(R.drawable.slider_dot_grey);
						img2.setImageResource(R.drawable.slider_dot_grey);
						img3.setImageResource(R.drawable.slider_dot_blue);
						// img4.setImageResource(R.drawable.slider_dot_grey);
						title.setText(getString(R.string.title3));

					}/*
					 * else if (viewFlipper.getDisplayedChild() == 0) {
					 * 
					 * img1.setImageResource(R.drawable.slider_dot_grey);
					 * img2.setImageResource(R.drawable.slider_dot_grey);
					 * img3.setImageResource(R.drawable.slider_dot_grey);
					 * img4.setImageResource(R.drawable.slider_dot_blue);
					 * title.setText(getString(R.string.title4));
					 * 
					 * }
					 */

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			break;
		}
		return false;
	}

	private void onFblogin() {
		callbackmanager = CallbackManager.Factory.create();

		// Set permissions
		LoginManager.getInstance().logInWithReadPermissions(this,
				Arrays.asList("email", "public_profile"));

		LoginManager.getInstance().registerCallback(callbackmanager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {

						GraphRequest.newMeRequest(loginResult.getAccessToken(),
								new GraphRequest.GraphJSONObjectCallback() {

									@Override
									public void onCompleted(JSONObject json,
											GraphResponse response) {
										// TODO Auto-generated method stub

										if (response.getError() != null) {
											// handle error
											System.out.println("ERROR");
										} else {
											System.out.println("Success");
											try {

												String jsonresult = String
														.valueOf(json);
												System.out
														.println("JSON Result"
																+ jsonresult);
											//	Log.e("RESU", jsonresult);
												String str_email = json
														.getString("email");
												str_id = json.getString("id");
												String str_firstname = json
														.getString("first_name");
												String str_lastname = json
														.getString("last_name");

												/*
												 * String str_location =
												 * json.getString("location");
												 */

												String str_gender = json
														.getString("gender");
												//Log.e("GEND", str_gender);
												if (str_gender.equals("male")) {
													int personGender = 0;
													getUserPic(str_id,
															jsonresult,
															personGender,
															str_email);
												} else {
													int personGender = 1;
													getUserPic(str_id,
															jsonresult,
															personGender,
															str_email);
												}

											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}

								}).executeAsync();

					}

					@Override
					public void onCancel() {

					}

					@Override
					public void onError(FacebookException error) {
						// Log.d("TAG_ERROR", error.toString());

					}
				});
	}

	public void getUserPic(String userID, String result, int gender,
			String email) {
		String imageURL = "";
		imageURL = "http://graph.facebook.com/" + userID
				+ "/picture?type=large";

	//	Log.e("GETPIC", imageURL);

		
		  isInternetPresent = cd.isConnectingToInternet();
          // check for Internet status
             if (isInternetPresent) {
                 // Internet Connection is Present
            	 mProgressHUD = ProgressHUD.show(LoginActivity.this, "Processing...",
         				true, true, LoginActivity.this);
         		mProgressHUD.setCancelable(false);
         		upload(result, gender, imageURL, "facebook", email, city_name_user);
             } else {
                 // Internet connection is not present
                 // Ask user to connect to Internet
                 showAlertDialog(LoginActivity.this, "No Internet Connection",
                         "You don't have internet connection.", false);
             }
		
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		try {
			if (mConnectionResult.hasResolution()) {
				try {
					mIntentInProgress = true;
					mConnectionResult
							.startResolutionForResult(this, RC_SIGN_IN);

				} catch (SendIntentException e) {
					mIntentInProgress = false;
					mGoogleApiClient.connect();				

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();

			}
		} else {

			callbackmanager.onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();

	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				// Log.e("RESP",""+mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				int personGender = currentPerson.getGender();
				// String personGender =
				// String.valueOf(currentPerson.getGender());
				// int age = currentPerson.getAgeRange().getMin();
				String personDOB = currentPerson.getBirthday();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				if (personGender == 0) {
					String person_Gender = "Male";
					/*
					 * Log.e(TAG, "Name: " + personName + ", plusProfile: " +
					 * personGooglePlusProfile + ", age: " +
					 * currentPerson.getBirthday() + ", Gender: " +
					 * person_Gender + ", City: "+ city_name_user);
					 */
				} else {
					String person_Gender = "Female";
					/*
					 * Log.e(TAG, "Name: " + personName + ", plusProfile: " +
					 * personGooglePlusProfile + ", age: " + personDOB +
					 * ", Gender: " + person_Gender + ", City: "+
					 * city_name_user);
					 */
				}
				/*
				 * Log.e(TAG, "Name: " + personName + ", plusProfile: " +
				 * personGooglePlusProfile + ", age: " + age + ", Image: " +
				 * currentPerson.getGender());
				 */
				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;
				
				isInternetPresent = cd.isConnectingToInternet();
		          // check for Internet status
		             if (isInternetPresent) {
		            	 mProgressHUD = ProgressHUD.show(LoginActivity.this,
		 						"Processing...", true, true, LoginActivity.this);
		 				mProgressHUD.setCancelable(false);
		 				upload(currentPerson.toString(), personGender, personPhotoUrl,
		 						"google", email, city_name_user);
		             } else {
		                 // Internet connection is not present
		                 // Ask user to connect to Internet
		                 showAlertDialog(LoginActivity.this, "No Internet Connection",
		                         "You don't have internet connection.", false);
		             }
				
				
				
				
				

			} else {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * "Person information is null", Toast.LENGTH_LONG).show();
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();

	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
		//	Log.e("G++++", "G+");
			resolveSignInError();
		}
	}

	@SuppressWarnings("deprecation")
	public void upload(String result, int personGender, String imgurl,
			String type, String email, String city_name) {
		// client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		password = telephonyManager.getDeviceId();
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("vendor", type);
		params.put("alldata", result);
		params.put("gender", personGender);
		params.put("imgurl", imgurl);
		params.put("email", email);
		params.put("location", city_name);
		params.put("deviceid", password);
		params.put("device_token", gcm_id);

		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;
						// Log.e("success", "success");

					}
				//	Log.e("RES_LOGIN", st1);
					show_user(st1);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
			//	Log.e("FAIL", "FAIl" + statusCode);
				mProgressHUD.dismiss();

			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void show_user(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		// Log.e("SHOW", "SHOW");
		User main_user1 = gson.fromJson(jsonReader, User.class);
		// UserDetails main_user = gson.fromJson(jsonReader, UserDetails.class);
		// Log.e("SHOW1", "SHOW1");
		UserDetails main_user = main_user1.getUser();

		new MyApp1().execute(main_user);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		// dialog.dismiss();
	}

	private class MyApp1 extends AsyncTask<UserDetails, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(UserDetails... params) {
			UserDetails main_user = params[0];
			SharedPreferences preferences = getSharedPreferences("User_detail",
					0);
			Editor editor = preferences.edit();

			try {
//Log.e("LOCA", ""+main_user.getLocation());
				editor.putString("user_id", main_user.getId());
				editor.putString("user_email", main_user.getEmail());
				editor.putString("user_firstName", main_user.getFirst_name());
				editor.putString("user_lastName", main_user.getLast_name());
				editor.putString("user_location", main_user.getLocation());
				editor.putString("user_uid", main_user.getUid());
				editor.putString("user_age", main_user.getAge());
				editor.putString("user_gender", main_user.getGender());
				editor.putString("user_rewardPoints", main_user.getPoints());
				editor.putString("user_reviewCount",
						main_user.getPro_review_count());
				editor.putString("user_likeCount",
						main_user.getPro_likes_count());
				editor.putString("user_fbLink", main_user.getFb_link());
				editor.putString("user_twitterLink",
						main_user.getTwitter_link());
				editor.putString("user_otherWebsite",
						main_user.getOther_website());
				editor.putString("user_image", main_user.getAndroid_api_img());
				editor.putString("about", main_user.getAbout());
				editor.putString("password", password);
			//	Log.e("USER_IMG", main_user.getAndroid_api_img());
				editor.commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			try {
				mProgressHUD.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!data.equals("no")) {
				
				getAllData(data);
			} else {
			Intent main_activity = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(main_activity);
			finish();
			}
		}

	}

	/**
	 * getLocation()
	 */

	public void getLocation() {
		String MAIN_WEBSERVICE_REST_URL = "";
		MAIN_WEBSERVICE_REST_URL = getString(R.string.base_url)
				+ "start/getClientLocation.json";
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

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;
						// Log.e("success", "success");

					}
					// Log.e("RES", st1);
					setCitySlug(st1);
				} catch (Exception e) {
					e.printStackTrace();
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

	public void setCitySlug(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		MainClient main_user1 = gson.fromJson(jsonReader, MainClient.class);
		ClientLocationModel client = main_user1.getClientLocation();

		SharedPreferences clientcity = getSharedPreferences("clientcity", 10);
		Editor edit = clientcity.edit();
		edit.putString("id", client.getId());
		edit.putString("city", client.getCity_name());
		edit.putString("cityslug", client.getSlug());
		edit.commit();

	}

	/**
	 * {@code} all_area()
	 * 
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	public void all_area() {
		ALL_AREA_REST_URL = getString(R.string.base_url) + "markets.json";

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
					allArea_showData(text2);
					// Log.e("AREA", text2);
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
	public void allArea_showData(String data1) {

		markets.clear();
		home_search_area_db.allDelete();
		home_search_area_db.addAllArea(new AllArea("All Areas", "All Areas"));
		Gson gson1 = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data1));
		jsonReader.setLenient(true);

		Area area1 = gson1.fromJson(jsonReader, Area.class);
		markets = area1.getMarkets();
		for (int i = 0; i < markets.size(); i++) {
			market_name = markets.get(i).get_market_name();
			String slug_name = markets.get(i).getUrl_slug();
			home_search_area_db.addAllArea(new AllArea(market_name, slug_name));
		}
		Variables.areaList.clear();
		Variables.areaList = home_search_area_db.getAllAreas1();

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
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zakoopi.activity.SplashScreen.getAllData()
	 */

	public void getAllData(String data1) {
		String myintenturl = getString(R.string.base_url)
				+ "Common/androidIntent.json";
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);

		try {
			params.put("url", data1);

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
					getIntentData(text);
					

				} catch (Exception e) {

				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			
			}
		});
	}

	public void getIntentData(String data) {

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

	//	Log.e("daat", entity + "==" + code + "===" + slug + "==" + id);
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
		if (code.equals("0")) {

			if (entity.equals("STR")) {

				
					Intent in = new Intent(LoginActivity.this,
							StoreActivity.class);
					in.putExtra("store_id", id);
					startActivity(in);
					finish();
				

			} else if (entity.equals("ART")) {

				
					Intent in = new Intent(LoginActivity.this, ArticleView.class);
					in.putExtra("article_id", id);
					in.putExtra("username", "nouser");
					startActivity(in);
					finish();
				

			} else if (entity.equals("LBK")) {

				
					Intent in = new Intent(LoginActivity.this,
							LookbookView1.class);
					in.putExtra("lookbook_id", id);
					in.putExtra("username", "nouser");
					startActivity(in);
					finish();

				
			} else if (entity.equals("MEM")) {

				
					Intent in = new Intent(LoginActivity.this,
							OtherUserProfile.class);
					in.putExtra("user_id", id);
					startActivity(in);
					finish();

				
			} else if (entity.equals("SRC")) {

				
					Intent in = new Intent(LoginActivity.this,
							UserStoreAndExperiences.class);
					in.putExtra("search", "top_mark");
					in.putExtra("market_slug", slug);
					startActivity(in);
					finish();
				
			}

		} else {

		}
		}else {
			showAlertDialog(LoginActivity.this,
					"No Internet Connection",
					"You don't have internet connection.", false);
		}
	}

}