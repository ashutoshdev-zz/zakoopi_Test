package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.about.model.AboutDetailModel;
import com.zakoopi.about.model.AboutMainModel;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class AboutUs extends Activity {

	private WebView web;
	
	AsyncHttpClient client;
	private SharedPreferences pro_user_pref;
	private String user_email;
	private String user_password;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	RelativeLayout rel_back;
	TextView txt_back;
	Typeface typeface_semibold;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutus);
		pro_user_pref = getSharedPreferences("User_detail", 0);
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
		
		client = ClientHttp.getInstance(AboutUs.this);
		
		/**
		 * Google Analystics
		 */
		Tracker t = ((UILApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("About Us");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		
		
		rel_back = (RelativeLayout) findViewById(R.id.rel1);
		txt_back = (TextView) findViewById(R.id.textView1);
		txt_back.setTypeface(typeface_semibold);
		txt_back.setText("About Us");
		
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		aboutUs();

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(AboutUs.this).reportActivityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(AboutUs.this).reportActivityStop(this);
	}

	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			view.loadUrl(url);
			
			return true;

		}
	}

	/*
	 * Upload data for editing user profile
	 */

	@SuppressWarnings("deprecation")
	public void aboutUs() {
		String profile_URL = getResources().getString(R.string.base_url)
				+ "Common/aboutus.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		client.post(profile_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

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
					show_user(st1);
					//Log.e("RES", st1);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				//Log.e("RES", "failed" + "   " + e.getMessage());

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
		AboutMainModel main_user1 = gson.fromJson(jsonReader,
				AboutMainModel.class);
		AboutDetailModel main_user = main_user1.getData();
		
		web = (WebView) findViewById(R.id.webView1);
		try {
			
		
		web.setWebViewClient(new myWebClient());
		web.getSettings().setJavaScriptEnabled(true);
		web.loadData(main_user.getContent(), "text/html", "utf-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
