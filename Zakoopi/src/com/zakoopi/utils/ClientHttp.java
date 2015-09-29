package com.zakoopi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

@SuppressWarnings("deprecation")
public class ClientHttp {
	public static AsyncHttpClient client;
	static Context ctx;
	private static SharedPreferences pref_location;
	private static String city_name;

	
	public static AsyncHttpClient getInstance(Context ctx1) {
		if (client == null) {
			
			
			client = new AsyncHttpClient();
		

		}
		try {
			
		
		ctx=ctx1;
		pref_location = ctx.getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "delhi");
		//Log.e("CITY_NAME", city_name);
		client.addHeader("loc", city_name);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return client;
	}
	
	
	
	
}
