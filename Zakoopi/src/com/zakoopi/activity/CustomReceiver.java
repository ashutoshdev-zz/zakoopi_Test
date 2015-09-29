package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zakoopi.R;
import com.zakoopi.utils.ClientHttp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class CustomReceiver extends BroadcastReceiver {
	
	private static String ADD_REFFER_REST_URL = "";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	Context ctx;
	@Override
	public void onReceive(Context context, Intent intent) {
ctx = context;
		/**
		 * https://play.google.com/store/apps/details?id=com.zakoopi&referrer=utm_source%3DzakoopiApp%26utm_medium%3DDirectShare
		 * 
		 * Ad Network : custom Package Name: com.zakoopi Campaign Source:
		 * zakoopiApp Campaign Medium:DirectShare
		 * 
		 * also send device id
		 */
		client = ClientHttp.getInstance(ctx);
		add_reffer();
		Bundle extras = intent.getExtras();
		String referrerString = extras.getString("referrer");
		new CampaignTrackingReceiver().onReceive(ctx, intent);
	}
	
	public void add_reffer() {

		ADD_REFFER_REST_URL = ctx.getString(R.string.base_url)
				+ "Common/add_referrer.json";
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("referrer_string", "ZakoopiApp"+", "+"DirectShare");
		params.put("device_id", telephonyManager.getDeviceId());
		
		
		client.setTimeout(DEFAULT_TIMEOUT);
		client.post(ADD_REFFER_REST_URL, params,
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
						} catch (Exception e) {

						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {

					}
				});
	}

}