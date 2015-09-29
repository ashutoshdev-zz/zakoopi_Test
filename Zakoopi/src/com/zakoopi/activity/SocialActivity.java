package com.zakoopi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.zakoopi.R;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class SocialActivity extends Activity {

	String social_link, name;
	WebView web;
	RelativeLayout rel_back;
	TextView txt_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.social_activity);
		
		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Social Activity");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		try {
			
	
		Intent i = getIntent();
		social_link = i.getStringExtra("social_link");
		name = i.getStringExtra("name");
		
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_name = (TextView) findViewById(R.id.txt);
		txt_name.setText(name);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		web = (WebView) findViewById(R.id.webView1);
		
		try {
			
		
		web.setWebViewClient(new myWebClient());
		web.getSettings().setJavaScriptEnabled(true);
		if (social_link.contains("https://")) {
			web.loadUrl(social_link);	
		} else {
			web.loadUrl("http://"+social_link);	
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(SocialActivity.this).reportActivityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(SocialActivity.this).reportActivityStop(this);
	}
	
	
	 public class myWebClient extends WebViewClient
	    {
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
	 
	    // To handle "Back" key press event for WebView to go back to previous screen.
	   @Override
	   public boolean onKeyDown(int keyCode, KeyEvent event)
	  {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
	        web.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	   }
}
