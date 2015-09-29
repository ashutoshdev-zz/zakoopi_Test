package com.mycam;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.view.Window;

import com.cam.imagedatabase.DBHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.tabbar.cam.SlidingTabLayout;
import com.tabbar.cam.ViewPagerAdapter;
import com.zakoopi.R;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.helper.CustomViewPager;
import com.zakoopi.helper.ImageItem;
import com.zakoopi.helper.MyService;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class LookBookTabsActivity extends FragmentActivity {

	CustomViewPager pager;
	ViewPagerAdapter adapter;
	SlidingTabLayout tabs;
	CharSequence Titles[] = { "GALLERY", "CAMERA" };
	int Numboftabs = 2;
	private SQLiteDatabase db;
	public static final String DBTABLE1 = "Stores";
	private SQLiteStatement stm;
	ArrayList<String> storenames = new ArrayList<String>();
	int page = 1;
	static AsyncHttpClient client = new AsyncHttpClient();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	ProgressDialog bar;
	String text = "";
	String line = "";
	SharedPreferences pref;
	Editor editor;
	public static ArrayList<ImageItem> images = new ArrayList<ImageItem>();
	public static boolean once = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lookbook_tabs);
		

		/*if (once == true) {
			once = false;
			// Intent for service starting
			Intent i = new Intent();
			i.setClass(LookBookTabsActivity.this, MyService.class);
			LookBookTabsActivity.this.startService(i);

		}*/

		
		
		/**
		* Google Analystics
		*/

		Tracker t = ((UILApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Lookbook Tabs Activity");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		pref = getApplicationContext().getSharedPreferences("lastupdate",
				MODE_PRIVATE);
		editor = pref.edit();
		DBHelper hp = new DBHelper(LookBookTabsActivity.this);
		db = hp.getWritableDatabase();
		stm = db.compileStatement("insert into  " + DBTABLE1
				+ " (id,market) values (?, ?)");
		
		adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
				Numboftabs);
		pager = (CustomViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);

		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true);

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});
		
		new setData().execute();
		
		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(pager);
		pager.setCurrentItem(1);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(LookBookTabsActivity.this).reportActivityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(LookBookTabsActivity.this).reportActivityStop(this);
	}

	
	
	private class setData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
		//	Log.e("gggggsize", "h  "+images.size());
			if(images.size()<=0){
			//	Log.e("ggggg", "here");
				getImageFromGallery();
				
			}
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			

		}

	}

	public void getImageFromGallery() {

		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		@SuppressWarnings("deprecation")
		Cursor imagecursor1 = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");
		//Cursor imagecursor1 = loader.loadInBackground();
		
		int image_column_index = imagecursor1
				.getColumnIndex(MediaStore.Images.Media._ID);

		for (int i = 0; i < imagecursor1.getCount(); i++) {

			imagecursor1.moveToPosition(i);

			int id = imagecursor1.getInt(image_column_index);
			ImageItem imageItem = new ImageItem();
			imageItem.id = id;

			int dataColumnIndex = imagecursor1
					.getColumnIndex(MediaStore.Images.Media.DATA);
			imageItem.path = imagecursor1.getString(dataColumnIndex);
			imageItem.thumb_path = getThumbnailPathForLocalFile(imageItem.id);// imagecursor1.getString(dataColumnIndex);
			images.add(imageItem);
			//Log.e("dddddddd", images+"");*/
		}

	}
	
	String getThumbnailPathForLocalFile(int fileId) {
		Cursor thumbCursor = null;
		
		try {

			thumbCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
					LookBookTabsActivity.this.getContentResolver(), fileId,
					MediaStore.Images.Thumbnails.MINI_KIND, null);

			if (thumbCursor.moveToFirst()) {
				// the path is stored in the DATA column
				int dataIndex = thumbCursor
						.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				String thumbnailPath = thumbCursor.getString(dataIndex);
				return thumbnailPath;
			}
		} finally {
			if (thumbCursor != null) {
				thumbCursor.close();
			}
		}

		return null;
	}
}
