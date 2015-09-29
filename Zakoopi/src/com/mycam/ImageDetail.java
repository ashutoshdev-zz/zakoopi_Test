package com.mycam;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cam.imagedatabase.DBHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.image.effects.HorizontalListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.activity.ProfileDrafts;
import com.zakoopi.activity.SearchStoreActivity;
import com.zakoopi.helper.ProgressHUD;
import com.zakoopi.helper.Variables;
import com.zakoopi.lookbookView.LookbookRecent;
import com.zakoopi.lookbookView.LookbookTagStore;
import com.zakoopi.lookbookView.RecentLookBookCardShow;
import com.zakoopi.lookbookView.RecentLookbookMain;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.PopularAdapter1.ReviewHolder;
import com.zakoopi.utils.UILApplication.TrackerName;

public class ImageDetail extends FragmentActivity implements OnCancelListener {

	ImageView detail_image;
	Button addmore;
	HorizontalListView imagelist;
	ArrayList<byte[]> imgbyte = new ArrayList<byte[]>();
	ArrayList<String> myidd = new ArrayList<String>();
	ArrayList<String> store_list = new ArrayList<String>();
	ArrayList<String> store_slug_list = new ArrayList<String>();
	ArrayList<String> store_id_list = new ArrayList<String>();
	ImageAdapter adapter;
	EditText tag_name;
	TextView textcomplete;
	private Bitmap bitmap, bitmap1;
	public static String addmoreimg = "noone";
	RelativeLayout rel_error;
	String store_id = "";
	String store_slug = "";
	String store = "";
	ArrayList<String> storenames = new ArrayList<String>();
	ArrayList<File> files = new ArrayList<File>();
	private SQLiteDatabase db;
	public static final String DBTABLE = "lookbook";
	public static final String DBTABLE1 = "Stores";
	private SQLiteStatement stm;
	long count;
	long idd;
	String st1 = "";
	String imgpath = "no";
	byte imageInByte[];
	TextView txt_img_detail, txt_picture_description, txt_store_tag,
			txt_add_photo, txt_error, txt_photostream, txt_tag_hint;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;

	RelativeLayout rel_back;
	String image_effect;
	private ImageView lastSelectedView = null;
	private String POPULAR_REST_URL;
	AsyncHttpClient client;
	private SharedPreferences pro_user_pref;
	private String pro_user_name;
	private String user_email, user_id;
	private String user_password;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	ProgressHUD mProgressHUD;
	boolean bool = false;
	TextView txt_sub;
	int update_pos = -1;
	String upid;
	String upimgpath = "no";
	String upstoreid = "";
	byte[] upphoto;
	String tagname = "";
	String desc;
	String chktag = "";
	int mSelectedItem;
	boolean chk_update = false;
	public static String myview = "success";
	public static String lookbook_title;
	String mystoreslug = "";
	static boolean bool_up = false;
	String chk_condition;
	GridView grid_tag;

	String mystore_id = "";
	String mystore_slug = "";
	byte[] myimg_byte = null;
	String myimgpath = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail);

		client = ClientHttp.getInstance(ImageDetail.this);
		imgpath = ImageEffects.imgpath;
		imageInByte = ImageEffects.imageInByte;

		/**
		 * 
		 * Comment Edit
		 */
		POPULAR_REST_URL = getString(R.string.base_url) + "lookbooks/view/"
				+ ProfileDrafts.idd + ".json";

		Intent i = getIntent();
		
		image_effect = i.getStringExtra("ImageEffects");
		/**
		 * Save Lookbook data in database
		 */

		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = this.getSharedPreferences("User_detail", 0);

		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		user_id = pro_user_pref.getString("user_id", "asds");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Image Detail");
		t.send(new HitBuilders.AppViewBuilder().build());

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

		grid_tag = (GridView) findViewById(R.id.grid_tag);
		rel_error = (RelativeLayout) findViewById(R.id.error);
		addmore = (Button) findViewById(R.id.button1);
		imagelist = (HorizontalListView) findViewById(R.id.listview);
		detail_image = (ImageView) findViewById(R.id.imageView2);
		tag_name = (EditText) findViewById(R.id.edt_description);
		txt_img_detail = (TextView) findViewById(R.id.txt_img_detail);
		txt_picture_description = (TextView) findViewById(R.id.txt_picture_description);
		txt_store_tag = (TextView) findViewById(R.id.txt_store_tag);
		txt_add_photo = (TextView) findViewById(R.id.txt_add_photo);
		txt_error = (TextView) findViewById(R.id.txt_error);
		txt_photostream = (TextView) findViewById(R.id.txt_photostream);
		textcomplete = (TextView) findViewById(R.id.autoCompleteTextView1);
		txt_tag_hint = (TextView) findViewById(R.id.textView1);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_sub = (TextView) findViewById(R.id.txt_sub);
		// textcomplete.setText("Tag Stores");
		txt_sub.setTypeface(typeface_bold);
		tag_name.setTypeface(typeface_regular);
		txt_img_detail.setTypeface(typeface_bold);
		txt_picture_description.setTypeface(typeface_bold);
		txt_store_tag.setTypeface(typeface_bold);
		txt_add_photo.setTypeface(typeface_regular);
		txt_error.setTypeface(typeface_regular);
		txt_photostream.setTypeface(typeface_semibold);
		textcomplete.setTypeface(typeface_regular);
		txt_tag_hint.setTypeface(typeface_regular);

		if (image_effect.equals("ImageEffects")) {

			new MessagePooling().execute();

		} else {

			popular_feed();
		}

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		
		
		textcomplete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if (store_list.size() <= 5) {
					if (tag_name.getText().toString().equals("")) {
						tag_name.setError("Please add a	photo discription.");
					} else {
						Intent in = new Intent(ImageDetail.this,
								SearchStoreActivity.class);
						in.putExtra("SearchStore", "lookbook");
						startActivityForResult(in, 100);
					}

					rel_error.setVisibility(View.GONE);
					addmoreimg = "none";
				} else {
					Toast.makeText(ImageDetail.this, "you can tag max 6 stores", Toast.LENGTH_SHORT).show();
				}
				
				

			}
		});

		tag_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				tag_name.setError(null);
				addmoreimg = "none";

				String st = tag_name.getText().toString();
				/*
				 * if (st.length() < 25) {
				 * tag_name.setError("Minimum 25 character"); } else {
				 * 
				 * }
				 */
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				try {
					DBHelper hp = new DBHelper(ImageDetail.this);
					db = hp.getWritableDatabase();
					String st = tag_name.getText().toString();
					if (update_pos > -1) {
						if (tag_name.getText().toString().equals(st)) {

							String st1 = "";

							if (tagname.equals("tag")) {

								st1 = "tag";

							} else {

								st1 = tagname;
							}

							String st2 = st;

							if (tag_name.getText().toString().equals("")) {

								tag_name.setError("Please add a photo discription.");

							} else {

								ContentValues cv = new ContentValues();
								cv.put("id", upid);
								cv.put("photo", upphoto);
								cv.put("tag", st1);
								cv.put("desc", st2);
								cv.put("imagepath", upimgpath);
								cv.put("storeid", upstoreid);
								cv.put("slug", mystoreslug);
								db.update(DBTABLE, cv, "id " + " = " + upid,
										null);

							}

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		((RelativeLayout) findViewById(R.id.rel_next))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						String st = tag_name.getText().toString();
						String st1 = "";
						bool_up = true;
						try {
							DBHelper hp = new DBHelper(ImageDetail.this);
							db = hp.getWritableDatabase();
							Cursor c = db.rawQuery(" select * from " + DBTABLE
									+ " where id = " + upid, null);
							if (c != null) {
								if (c.moveToFirst()) {

									st1 = c.getString(c.getColumnIndex("tag"));
									store_slug = c.getString(c
											.getColumnIndex("slug"));
									store_id = c.getString(c
											.getColumnIndex("storeid"));

								
								}

							}

							if (chk_condition.equals("Next")) {

								if (tag_name.getText().toString().equals("")) {
									// Log.e("Log 1",
									// tag_name.getText().toString());
									tag_name.setError("Please add a photo discription.");

								} else if (st.length() == 1 || st.length() < 25) {
									// Log.e("Log 2", ""+st.length());
									tag_name.setError("Minimum 25 characters");
								} else {

									ContentValues cv = new ContentValues();
									cv.put("id", upid);
									cv.put("photo", imageInByte);
									cv.put("tag", st1);
									cv.put("desc", st);
									cv.put("imagepath", imgpath);
									cv.put("storeid", store_id);
									cv.put("slug", store_slug);
									db.update(DBTABLE, cv,
											"id " + " = " + upid, null);

									Intent in = new Intent(ImageDetail.this,
											Lookbookpublish.class);
									in.putExtra("image_effect", image_effect);
									in.putExtra("lookbook_title",
											lookbook_title);
									startActivity(in);
								}

							} else {

								if (tag_name.getText().toString().equals("")) {

									tag_name.setError("Please add a photo discription.");

								} else if (st.length() == 1 || st.length() < 25) {

									tag_name.setError("Minimum 25 characters");
								} else {

									uploadDialog();
								}

							}

							Tracker t = ((UILApplication) getApplication())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory("Image Detail")
									.setAction("Image Detail Done")
									.setLabel("Image Detail").build());

						} catch (SQLiteException s) {
							s.printStackTrace();
						}
					}
				});

		// Reading all contacts from database

		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Log.e("coung", count + "");
				bool_up = true;
				if (count <= 0) {
					addmoreimg = "addmore";
					Intent in = new Intent(ImageDetail.this,
							LookBookTabsActivity.class);
					startActivity(in);
					finish();

				} else {
					

					String st2 = tag_name.getText().toString();

					if (tag_name.getText().toString().equals("")) {
						tag_name.setError("Please add a photo discription.");
					} else {

						try {
							DBHelper hp = new DBHelper(ImageDetail.this);
							db = hp.getWritableDatabase();
							Cursor c = db.rawQuery(" select * from " + DBTABLE + " where id = "
									+ upid, null);
							if (c != null) {
								if (c.moveToFirst()) {

									st1 = c.getString(c.getColumnIndex("tag"));
									mystore_id = c.getString(c.getColumnIndex("storeid"));
									mystore_slug = c.getString(c.getColumnIndex("slug"));
									myimg_byte = c.getBlob(c.getColumnIndex("photo"));
									myimgpath = c.getString(c.getColumnIndex("imagepath"));

								}

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						
						if (chk_update == false) {

							ContentValues cv = new ContentValues();
							cv.put("id", upid);
							cv.put("photo", myimg_byte);
							cv.put("tag", st1);
							cv.put("desc", st2);
							cv.put("imagepath", myimgpath);
							cv.put("storeid", mystore_id);
							cv.put("slug", mystore_slug);
							db.update(DBTABLE, cv, "id " + " = " + upid, null);
						}
						if (count < 6) {
							addmoreimg = "addmore";
							Intent in = new Intent(ImageDetail.this,
									LookBookTabsActivity.class);
							startActivity(in);
							finish();
						} else {

						}
					}

				}

				Tracker t = ((UILApplication) getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder().setCategory("Add Images")
						.setAction("Click on Add More Images")
						.setLabel("Image Detail").build());

			}
		});

	}

	@SuppressWarnings("deprecation")
	public void upload(final JSONObject arr) {
		String LOOKBOOK_URL = getResources().getString(R.string.base_url)
				+ "lookbooks/add.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("jsonData", arr);
		try {
			for (int i = 0; i < files.size(); i++) {
				params.put("files[" + files.get(i).getName() + "]",
						files.get(i));
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				mProgressHUD = ProgressHUD.show(ImageDetail.this,
						"Processing...", true, true, ImageDetail.this);
				mProgressHUD.setCancelable(false);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				MainActivity.img_detail = "success";

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;

					}

					Variables.showdrafts = "drafts";

				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent main = new Intent(ImageDetail.this, MainActivity.class);
				startActivity(main);
				finish();

				mProgressHUD.dismiss();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				mProgressHUD.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public JSONObject getData(String title) {

		JSONArray jsonArray = new JSONArray();
		JSONObject finalobject = new JSONObject();
		try {
			DBHelper hp = new DBHelper(ImageDetail.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(" select * from " + DBTABLE, null);
			count = c.getCount();

			if (c != null) {
				while (c.moveToNext()) {

					JSONObject obj = new JSONObject();
					int id = c.getInt(c.getColumnIndex("id"));
					String st = c.getString(c.getColumnIndex("slug"));
					String st1 = c.getString(c.getColumnIndex("desc"));
					String path = c.getString(c.getColumnIndex("imagepath"));
					String storeid = c.getString(c.getColumnIndex("storeid"));
					byte[] arr = c.getBlob(c.getColumnIndex("photo"));
					try {
						files.add(new File(path));
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {

						obj.put("storeid", storeid);
						obj.put("storetag", st);
						obj.put("description", st1);
						obj.put("photo_path", path);
						jsonArray.put(obj);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				try {

					JSONObject myobj = new JSONObject();
					myobj.put("title", title);
					myobj.put("draft", "1");
					myobj.put("user_id", user_id);
					myobj.put("cards", jsonArray);
					finalobject.put("lookbook", myobj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return finalobject;

	}

	/**
	 * 
	 * POP_FEEd
	 * 
	 * @param event
	 * @return
	 */

	public void popular_feed() {
		long time = System.currentTimeMillis();
		client.setBasicAuth(user_email, user_password);

		client.get(POPULAR_REST_URL + "?&_=" + time,
		// client.get("http://v3dev.zakoopi.com/api/Start/getClientLocation.json",
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// called before request is started

						try {
							mProgressHUD = ProgressHUD.show(ImageDetail.this,
									"Processing...", true, true,
									ImageDetail.this);
							mProgressHUD.setCancelable(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						// called when response HTTP status is "200 OK"

						try {
							String line = "";
							String text = "";
							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							while ((line = br.readLine()) != null) {

								text = text + line;
							}

							showData(text);
							// Log.e("Success_draft", "-----" + text);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {

						mProgressHUD.dismiss();
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}

	/**
	 * Popular Feed Show Data
	 * 
	 * @showData data
	 */
	@SuppressWarnings("unchecked")
	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		RecentLookbookMain ppp = gson
				.fromJson(reader, RecentLookbookMain.class);
		LookbookRecent recent = ppp.getLookbook();

		lookbook_title = recent.getTitle();

		List<RecentLookBookCardShow> feeds = recent.getRecentCards();
		// mProgressHUD.dismiss();
		new MyApp().execute(feeds);

	}

	/**
	 * MyApp extends AsyncTask<List<popularfeed>, Void, Void> for Showdata(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp extends
			AsyncTask<List<RecentLookBookCardShow>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<RecentLookBookCardShow>... params) {

			DBHelper hp2 = new DBHelper(ImageDetail.this);
			db = hp2.getWritableDatabase();
			//db.delete(DBTABLE, " id = " + id.get(index), null);
			db.delete(DBTABLE, null, null);
			List<RecentLookBookCardShow> feeds = params[0];

			for (int i = 0; i < feeds.size(); i++) {
				RecentLookBookCardShow cs = feeds.get(i);
				String discrp = cs.getdescription();
				String cards = cs.getPic();
				String pic_path = cs.getPhoto_path();
				String card_id = cs.getId();
				try {
					URL url = new URL(cards);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					myBitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
					byte[] byteArray = stream.toByteArray();
					imageInByte = byteArray;

				} catch (IOException e) {
					e.printStackTrace();

				}

				ArrayList<LookbookTagStore> stores = cs.getStores();
				for (int j = 0; j < stores.size(); j++) {

					store_id += stores.get(j).getId() + ",";
					store_slug += stores.get(j).getSlug() + ",";

					store += stores.get(j).getStore_name() + ",";

				}

				// change
				try {

					DBHelper hp = new DBHelper(ImageDetail.this);
					db = hp.getWritableDatabase();
					stm = db.compileStatement("insert into  "
							+ DBTABLE
							+ " (photo,tag,desc,imagepath,storeid,mypath,card_id,slug) values (?, ?, ?, ?, ?,?,?,?)");

					stm.bindBlob(1, imageInByte);
					stm.bindString(2, store);
					stm.bindString(3, discrp);
					stm.bindString(4, "no");
					stm.bindString(5, store_id);
					stm.bindString(6, pic_path);
					stm.bindString(7, card_id);
					stm.bindString(8, store_slug);
					stm.executeInsert();
					store = "";
					store_slug = "";
					store_id = "";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			try {
				DBHelper hp = new DBHelper(ImageDetail.this);
				db = hp.getWritableDatabase();
				Cursor c = db.rawQuery(" select * from " + DBTABLE, null);

				while (c.moveToNext()) {

					String path = c.getString(c.getColumnIndex("imagepath"));

				}
				if (c != null) {
					if (c.moveToLast()) {

						idd = c.getInt(c.getColumnIndex("id"));
						upid = String.valueOf(idd);
						String desc = c.getString(c.getColumnIndex("desc"));
						String tag = c.getString(c.getColumnIndex("tag"));
						String tag_store_id = c.getString(c
								.getColumnIndex("storeid"));
						String tag_store_slug = c.getString(c
								.getColumnIndex("slug"));
						if (tag.equals("tag")) {
							// textcomplete.setHint("Tag Stores");
						} else {
							textcomplete.setText(tag);

							String[] arr_store_slug = tag_store_slug.split(",");
							String[] arr_store_id = tag_store_id.split(",");
							String[] arr_store_name = tag.split(",");

							store_list.clear();
							store_slug_list.clear();
							store_id_list.clear();

							for (int i = 0; i < arr_store_name.length; i++) {

								store_list.add(arr_store_name[i]);
							}
							for (int j = 0; j < arr_store_slug.length; j++) {

								store_slug_list.add(arr_store_slug[j]);
							}

							for (int k = 0; k < arr_store_id.length; k++) {

								store_id_list.add(arr_store_id[k]);
							}

							grid_tag.setAdapter(new GridTagStoreAdapter(
									ImageDetail.this, store_list,
									store_slug_list, store_id_list));

						}

						tag_name.setText(desc);

						try {

							byte[] arr = c.getBlob(c.getColumnIndex("photo"));
							byte[] outImage = arr;
							ByteArrayInputStream imageStream = new ByteArrayInputStream(
									outImage);
							final Bitmap theImage = BitmapFactory
									.decodeStream(imageStream);
							new BlurImages(detail_image).execute(theImage);

						} catch (Exception e) {
							e.printStackTrace();

						}

					}

				}

			} catch (SQLiteException s) {
				s.printStackTrace();
			} finally {
				if (db != null) {
					db.close();
				}
			}
			bool_up = true;
			new BlurImagesTask().execute();

			mProgressHUD.dismiss();

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(ImageDetail.this).reportActivityStart(this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(ImageDetail.this).reportActivityStop(this);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		try {
			
			

			DBHelper hp = new DBHelper(this);
			db = hp.getWritableDatabase();

			/*
			 * DBHelper dhp = new DBHelper(this); db = hp.getWritableDatabase();
			 */
			// lastSelectedView = holder.img;
			Cursor cc1 = db.rawQuery("select * from " + DBTABLE
					+ " where id = " + upid, null);

			if (cc1 != null) {
				if (cc1.moveToFirst()) {

					store = cc1.getString(cc1.getColumnIndex("tag"));
					store_id = cc1.getString(cc1.getColumnIndex("storeid"));
					store_slug = cc1.getString(cc1.getColumnIndex("slug"));

					if (store.equals("tag")) {
						store = "";
						store_id = "";
						store_slug = "";
					}

				}
			}

			store_slug += arg2.getStringExtra("resultslug") + ",";
			store_id += arg2.getStringExtra("resultid") + ",";
			store += arg2.getStringExtra("result") + ",";
			textcomplete.setText(store);

			String[] arr_store_slug = store_slug.split(",");
			String[] arr_store_id = store_id.split(",");
			String[] arr_store_name = store.split(",");

			store_list.clear();
			store_slug_list.clear();
			store_id_list.clear();

			for (int i = 0; i < arr_store_name.length; i++) {

				store_list.add(arr_store_name[i]);
			}
			for (int j = 0; j < arr_store_slug.length; j++) {

				store_slug_list.add(arr_store_slug[j]);
			}

			for (int k = 0; k < arr_store_id.length; k++) {

				store_id_list.add(arr_store_id[k]);
			}

			grid_tag.setAdapter(new GridTagStoreAdapter(ImageDetail.this,
					store_list, store_slug_list, store_id_list));

			if (update_pos > -1) {
				chktag = "";
				String st1 = "tag";

				if (textcomplete.getText().toString().equals("")) {

					st1 = "tag";

				} else {

					st1 = textcomplete.getText().toString().trim();
				}

				String st2 = tag_name.getText().toString();

				if (tag_name.getText().toString().equals("")) {

					tag_name.setError("Please add a photo discription.");

				} else {

					ContentValues cv = new ContentValues();
					cv.put("id", Integer.parseInt(upid));
					cv.put("photo", upphoto);
					cv.put("tag", st1);
					cv.put("desc", st2);
					cv.put("imagepath", upimgpath);
					cv.put("storeid", upstoreid);
					cv.put("slug", store_slug);
					db.update(DBTABLE, cv,
							"id " + " = " + Integer.parseInt(upid), null);

				}

				// for checking all lookbooks have tagged stores
				Cursor cc = db.rawQuery("select * from " + DBTABLE, null);
				if (cc != null) {
					while (cc.moveToNext()) {

						String tagname = cc.getString(cc.getColumnIndex("tag"));

						if (tagname.equals("tag") || tagname.equals("")) {

							chktag = "tag";
						}
					}

				}

				if (chktag.equals("tag")) {
					chk_condition = "Save as Draft";
					// txt_sub.setText("Save as Draft");
				} else {
					chk_condition = "Next";
					// txt_sub.setText("Next");
				}

			} else {

				String st1 = "";
				chktag = "";
				if (textcomplete.getText().toString().equals("")) {
					st1 = "tag";
				} else {
					st1 = textcomplete.getText().toString().trim();
				}

				String st2 = tag_name.getText().toString();

				if (tag_name.getText().toString().equals("")) {
					tag_name.setError("Please add a photo discription.");
				} else {

					ContentValues cv = new ContentValues();
					cv.put("id", upid);
					cv.put("photo", imageInByte);
					cv.put("tag", store);
					cv.put("desc", st2);
					cv.put("imagepath", imgpath);
					cv.put("storeid", store_id);
					cv.put("slug", store_slug);
					db.update(DBTABLE, cv, "id " + " = " + upid, null);

				}

				// for checking all lookbooks have tagged stores
				Cursor cc = db.rawQuery("select * from " + DBTABLE, null);
				if (cc != null) {
					while (cc.moveToNext()) {

						String tagname = cc.getString(cc.getColumnIndex("tag"));

						if (tagname.equals("tag") || tagname.equals("")) {
							chktag = "tag";
						}
					}

				}

				if (chktag.equals("tag")) {
					chk_condition = "Save as Draft";
					// txt_sub.setText("Save as Draft");
				} else {
					chk_condition = "Next";
					// txt_sub.setText("Next");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class BlurImagesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... param) {

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			String st = "false";

			try {
				DBHelper hp = new DBHelper(ImageDetail.this);
				db = hp.getWritableDatabase();
				Cursor c = db.rawQuery(
						" select id,photo,tag,desc,imagepath from " + DBTABLE,
						null);
				count = c.getCount();
				while (c.moveToNext()) {

					String tag = c.getString(c.getColumnIndex("tag"));
					String imagepath = c.getString(c
							.getColumnIndex("imagepath"));
					
					if (tag.equals("tag") || tag.equals("")) {

						st = "false";

						break;

					} else {

						st = "true";

					}
				}

				if (st.equals("false")) {
					chk_condition = "Save as Draft";
				} else {
					chk_condition = "Next";
				}

				if (c != null) {
					if (c.moveToFirst()) {
						do {
							int id = c.getInt(c.getColumnIndex("id"));

							byte[] arr = c.getBlob(c.getColumnIndex("photo"));
							imgbyte.add(arr);
							myidd.add(String.valueOf(id));

						} while (c.moveToNext());
					}
				}
			} catch (SQLiteException s) {
				s.printStackTrace();
			}

			adapter = new ImageAdapter(ImageDetail.this, imgbyte, myidd);
			imagelist.setAdapter(adapter);

		}
	}

	public class ImageAdapter extends BaseAdapter {
		ArrayList<byte[]> imglist = new ArrayList<byte[]>();
		Context ctyx;
		LayoutInflater inf;
		ArrayList<String> id = new ArrayList<String>();
		private SQLiteDatabase db;
		public static final String DBTABLE = "lookbook";

		public ImageAdapter(Context ctx, ArrayList<byte[]> list,
				ArrayList<String> id) {
			this.ctyx = ctx;
			imglist = list;
			this.id = id;
			inf = (LayoutInflater) ctyx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imglist.size();
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

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			View view = arg1;
			if (view == null) {
				holder = new ViewHolder();
				view = inf.inflate(R.layout.image_detail_list_item, null);
				holder.img = (ImageView) view.findViewById(R.id.imageView1);
				holder.del = (ImageView) view.findViewById(R.id.imageView2);
				view.setTag(holder);

			} else {

				holder = (ViewHolder) view.getTag();
			}

			holder.del.setTag(arg0);
			holder.img.setTag(arg0);
			byte[] outImage = imglist.get(arg0);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(
					outImage);
			final Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			new BlurImages(arg0, holder.img).execute(theImage);

			holder.img.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (tag_name.getText().toString().length() < 25) {
						tag_name.setError("Please fill photo description");
					} else {
						String pic_desc = tag_name.getText().toString();
						if (bool_up == false) {
							bool_up = true;

							DBHelper hp = new DBHelper(ImageDetail.this);
							db = hp.getWritableDatabase();

							ContentValues cv = new ContentValues();
							cv.put("id", upid);
							cv.put("photo", imageInByte);
							cv.put("tag", store);
							cv.put("desc", pic_desc);
							cv.put("imagepath", imgpath);
							cv.put("storeid", store_id);
							cv.put("slug", store_slug);
							db.update(DBTABLE, cv, "id " + " = " + upid, null);
						}

						Integer index = (Integer) v.getTag();
						// bool = false;
						DBHelper hp = new DBHelper(ctyx);
						db = hp.getWritableDatabase();
						// lastSelectedView = holder.img;
						Cursor cc = db.rawQuery("select * from " + DBTABLE
								+ " where id = " + id.get(index), null);
						if (cc != null) {
							if (cc.moveToFirst()) {

								tag_name.setText("");
								textcomplete.setText("");
								tagname = cc.getString(cc.getColumnIndex("tag"));
								desc = cc.getString(cc.getColumnIndex("desc"));

								upid = cc.getString(cc.getColumnIndex("id"));
								upimgpath = cc.getString(cc
										.getColumnIndex("imagepath"));

								upstoreid = cc.getString(cc
										.getColumnIndex("storeid"));
								upphoto = cc.getBlob(cc.getColumnIndex("photo"));
								mystoreslug = cc.getString(cc
										.getColumnIndex("slug"));

								tag_name.setText(desc);
								ByteArrayInputStream imageStream = new ByteArrayInputStream(
										upphoto);
								final Bitmap theImage = BitmapFactory
										.decodeStream(imageStream);

								new BlurImages(index, detail_image)
										.execute(theImage);
								chk_update = true;
								update_pos = index;
								// idd = Integer.parseInt(upid);
								imageInByte = upphoto;
								imgpath = upimgpath;
								if (tagname.equals("tag") || tagname.equals("")) {
									store_list.clear();
									store_slug_list.clear();
									store_id_list.clear();

									// textcomplete.setHint("tag store");
									store = "";
									store_id = "";
									store_slug = "";

								} else {

									String[] arr_store_name = tagname
											.split(",");
									String[] arr_store_id = upstoreid
											.split(",");
									String[] arr_store_slug = mystoreslug
											.split(",");

									store_list.clear();
									store_slug_list.clear();
									store_id_list.clear();

									for (int i = 0; i < arr_store_name.length; i++) {

										store_list.add(arr_store_name[i]);
									}

									for (int j = 0; j < arr_store_slug.length; j++) {

										store_slug_list.add(arr_store_slug[j]);
									}

									for (int k = 0; k < arr_store_id.length; k++) {

										store_id_list.add(arr_store_id[k]);
									}

									grid_tag.setAdapter(new GridTagStoreAdapter(
											ImageDetail.this, store_list,
											store_slug_list, store_id_list));
									// textcomplete.setHint(tagname);
									store = "";
									store = tagname;
									store_id = "";
									store_id = upstoreid;
									store_slug = "";
									store_slug = mystoreslug;
								}

							}
						}
					}
				}

			});

			holder.del.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// bool = true;
					Integer index = (Integer) arg0.getTag();
					update_pos = -1;
					if ((int) index == imglist.size() - 1) {

						detail_image.setImageDrawable(null);
					}

					DBHelper hp = new DBHelper(ctyx);
					db = hp.getWritableDatabase();
					db.delete(DBTABLE, " id = " + id.get(index), null);
					imglist.remove(index.intValue());
					id.remove(index.intValue());

					grid_tag.setAdapter(null);
					store_list.clear();

					count--;

					if (count == 0) {
						tag_name.setText("");
						textcomplete.setText("");
						addmoreimg = "none";
						Intent in = new Intent(ImageDetail.this,
								LookBookTabsActivity.class);
						startActivity(in);
						finish();
					} else {

						notifyDataSetChanged();

						Cursor cc = db.rawQuery("select * from " + DBTABLE,
								null);
						if (cc != null) {
							if (cc.moveToLast()) {
								tag_name.setText("");
								textcomplete.setText("");
								tagname = cc.getString(cc.getColumnIndex("tag"));
								desc = cc.getString(cc.getColumnIndex("desc"));

								upid = cc.getString(cc.getColumnIndex("id"));
								upimgpath = cc.getString(cc
										.getColumnIndex("imagepath"));

								upstoreid = cc.getString(cc
										.getColumnIndex("storeid"));
								upphoto = cc.getBlob(cc.getColumnIndex("photo"));
								mystoreslug = cc.getString(cc
										.getColumnIndex("slug"));
								tag_name.setText(desc);
								textcomplete.setText(tagname);

								String[] arr_store_name = tagname.split(",");
								String[] arr_store_id = upstoreid.split(",");
								String[] arr_store_slug = mystoreslug
										.split(",");
								for (int i = 0; i < arr_store_name.length; i++) {

									store_list.add(arr_store_name[i]);
								}

								for (int i = 0; i < arr_store_id.length; i++) {

									store_id_list.add(arr_store_id[i]);
								}

								for (int i = 0; i < arr_store_slug.length; i++) {

									store_slug_list.add(arr_store_slug[i]);
								}
								grid_tag.setAdapter(new GridTagStoreAdapter(
										ImageDetail.this, store_list,
										store_slug_list, store_id_list));

								ByteArrayInputStream imageStream = new ByteArrayInputStream(
										upphoto);
								final Bitmap ttheImage = BitmapFactory
										.decodeStream(imageStream);

								new BlurImages((int) count, detail_image)
										.execute(ttheImage);

							}
						}

					}

				}
			});

			return view;
		}

		class ViewHolder {

			ImageView img;
			ImageView del;
		}

	}

	public class BlurImages extends AsyncTask<Bitmap, Void, Bitmap> {

		private ImageView roundedImageView;
		int position;

		public BlurImages(int pos, ImageView imageView) {
			this.position = pos;

			this.roundedImageView = imageView;
		}

		public BlurImages(ImageView imageView) {

			this.roundedImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Bitmap... param) {
			Bitmap bitmap = param[0];

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			roundedImageView.setImageBitmap(result);

		}

	}

	public class MessagePooling extends AsyncTask<Void, String, Void> implements
			OnCancelListener {

		// ProgressDialog bar;
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(ImageDetail.this, "Processing...",
					true, true, this);
			mProgressHUD.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				DBHelper hp = new DBHelper(ImageDetail.this);
				db = hp.getWritableDatabase();
				Cursor c = db.rawQuery(" select id,imagepath from " + DBTABLE,
						null);
				if (c != null) {
					if (c.moveToLast()) {

						idd = c.getInt(c.getColumnIndex("id"));
						upid = String.valueOf(idd);
					}

				}
			} catch (SQLiteException s) {
				s.printStackTrace();
			} finally {
				if (db != null) {
					db.close();
				}
			}
			bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory
					.decodeByteArray(Variables.imgarr, 0,
							Variables.imgarr.length), 100, 100, 0);
			new BlurImages(detail_image).execute(bitmap);
			new BlurImagesTask().execute();

			mProgressHUD.dismiss();
			// bar.dismiss();
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			this.cancel(true);
			mProgressHUD.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	public void uploadDialog() {

		final Dialog dd = new Dialog(ImageDetail.this);
		// dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.lookbook_upload_asking_dialog);
		dd.show();

		RelativeLayout continuetags = (RelativeLayout) dd
				.findViewById(R.id.rel_upload1);
		RelativeLayout savedraft = (RelativeLayout) dd
				.findViewById(R.id.rel_upload);
		TextView txt_upload1 = (TextView) dd.findViewById(R.id.txt_upload1);
		TextView txt_upload = (TextView) dd.findViewById(R.id.txt_upload);

		final String st = tag_name.getText().toString();
		txt_upload1.setTypeface(typeface_bold);
		txt_upload.setTypeface(typeface_bold);

		try {
			DBHelper hp = new DBHelper(ImageDetail.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(" select * from " + DBTABLE + " where id = "
					+ upid, null);
			if (c != null) {
				if (c.moveToFirst()) {

					st1 = c.getString(c.getColumnIndex("tag"));
					mystore_id = c.getString(c.getColumnIndex("storeid"));
					mystore_slug = c.getString(c.getColumnIndex("slug"));
					myimg_byte = c.getBlob(c.getColumnIndex("photo"));
					myimgpath = c.getString(c.getColumnIndex("imagepath"));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		continuetags.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tag_name.getText().toString().equals("")) {
					tag_name.setError("Please add a photo discription.");
				} /*
				 * else { Intent in = new Intent(ImageDetail.this,
				 * SearchStoreActivity.class); in.putExtra("SearchStore",
				 * "lookbook"); startActivityForResult(in, 100); }
				 */

				rel_error.setVisibility(View.GONE);
				addmoreimg = "none";
				dd.cancel();
			}
		});

		savedraft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ContentValues cv = new ContentValues();
				cv.put("id", upid);
				cv.put("photo", myimg_byte);
				cv.put("tag", st1);
				cv.put("desc", st);
				cv.put("imagepath", myimgpath);
				cv.put("storeid", mystore_id);
				cv.put("slug", mystore_slug);
				db.update(DBTABLE, cv, "id " + " = " + upid, null);
				dd.cancel();
				customDailog(lookbook_title);

			}
		});

	}

	public void customDailog(String tl) {

		final Dialog dd = new Dialog(ImageDetail.this);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.title_dialog);
		dd.show();
		final EditText txt = (EditText) dd.findViewById(R.id.editText1);
		txt.setText(tl);
		RelativeLayout rel_upload = (RelativeLayout) dd
				.findViewById(R.id.rel_upload);
		txt.setTypeface(typeface_regular);
		txt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				txt.setError(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		rel_upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String title = txt.getText().toString().trim();
				if (title.length() < 20) {

					txt.setError("Minimum 20 character");

				} else {
					// getData(title);
					
					
					//upload1(getData1(title));
					if (Variables.draftVal.equals("draft")) {
						upload1(getData1(title));
					} else {
						upload(getData(title));
						}
					

					dd.cancel();
				}

			}
		});

	}

	public class GridTagStoreAdapter extends BaseAdapter {
		Context ctx;
		public List<String> mList;
		public List<String> idList;
		public List<String> slugList;

		public GridTagStoreAdapter(Context context, List<String> list,
				List<String> sluglist, List<String> idlist) {
			super();
			ctx = context;
			mList = list;
			this.idList = idlist;
			this.slugList = sluglist;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// Log.e("TopM", "TopM");
			View view = convertView;
			final ViewHolder store_holder;

			if (view == null) {

				LayoutInflater inflater = LayoutInflater.from(ctx);
				view = inflater.inflate(R.layout.store_tag_lookbook_item, null);

				store_holder = new ViewHolder();
				store_holder.img_delete = (ImageView) view
						.findViewById(R.id.img_del);
				store_holder.txt_tag_store = (TextView) view
						.findViewById(R.id.txt_store);

				view.setTag(store_holder);
			} else {
				store_holder = (ViewHolder) view.getTag();
			}
			
			store_holder.txt_tag_store.setTypeface(typeface_regular);
			store_holder.img_delete.setTag(position);
			store_holder.txt_tag_store.setText(mList.get(position));
			
			if (mList.size() == 0) {
				txt_tag_hint.setHint("Tag a store");
			} else if ((mList.size() > 0) && (mList.size() < 6) ){
				txt_tag_hint.setHint("Tag more stores");
			} else if (mList.size() == 6) {
				txt_tag_hint.setHint("Tag maximum 6 stores");
			}

			store_holder.img_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Integer index = (Integer) v.getTag();
					mList.remove(index.intValue());
					idList.remove(index.intValue());
					slugList.remove(index.intValue());
					notifyDataSetChanged();

					if (mList.size() <= 0) {
						chk_condition = "Save as Draft";
						// txt_sub.setText("Save as Draft");
					} else {
						chk_condition = "Next";
						// txt_sub.setText("Next");
					}

					String store1 = "";
					String store_id1 = "";
					String store_slug1 = "";
					for (int i = 0; i < mList.size(); i++) {
						store1 += mList.get(i) + ",";
					}

					for (int i = 0; i < idList.size(); i++) {
						store_id1 += idList.get(i) + ",";
					}

					for (int i = 0; i < slugList.size(); i++) {
						store_slug1 += slugList.get(i) + ",";
					}
					
					if (mList.size() == 0) {
						txt_tag_hint.setHint("Tag a store");
					} else if ((mList.size() > 0) && (mList.size() < 6) ){
						txt_tag_hint.setHint("Tag more stores");
					} else if (mList.size() == 6) {
						txt_tag_hint.setHint("Tag maximum 6 stores");
					}

					String pic_desc1 = tag_name.getText().toString();
					DBHelper hp = new DBHelper(ImageDetail.this);
					db = hp.getWritableDatabase();
					ContentValues cv = new ContentValues();
					cv.put("id", upid);
					cv.put("photo", imageInByte);
					cv.put("tag", store1);
					cv.put("desc", pic_desc1);
					cv.put("imagepath", imgpath);
					cv.put("storeid", store_id1);
					cv.put("slug", store_slug1);
					db.update(DBTABLE, cv, "id " + " = " + upid, null);

					// lastSelectedView = holder.img;
					Cursor cc = db.rawQuery("select * from " + DBTABLE
							+ " where id = " + upid, null);

					if (cc != null) {
						while (cc.moveToNext()) {

							tagname = cc.getString(cc.getColumnIndex("tag"));
							desc = cc.getString(cc.getColumnIndex("desc"));

							upid = cc.getString(cc.getColumnIndex("id"));
							upimgpath = cc.getString(cc
									.getColumnIndex("imagepath"));

							upstoreid = cc.getString(cc
									.getColumnIndex("storeid"));
							upphoto = cc.getBlob(cc.getColumnIndex("photo"));
							mystoreslug = cc.getString(cc
									.getColumnIndex("slug"));

						}
					}

				}
			});

			return view;
		}

	}

	public class ViewHolder {

		TextView txt_tag_store;
		ImageView img_delete;
	}

	
	
	/**
	 * 
	 * Draft Lookbook
	 */

	public JSONObject getData1(String title) {

		JSONArray jsonArray = new JSONArray();
		JSONObject finalobject = new JSONObject();
		try {
			DBHelper hp = new DBHelper(ImageDetail.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(
					" select id,photo,tag,desc,imagepath,mypath,card_id,storeid,slug from "
							+ DBTABLE, null);
			count = c.getCount();

			if (c != null) {
				while (c.moveToNext()) {
					JSONObject obj = new JSONObject();
					int id = c.getInt(c.getColumnIndex("id"));
					String st = c.getString(c.getColumnIndex("tag"));
					String st1 = c.getString(c.getColumnIndex("desc"));
					String path = c.getString(c.getColumnIndex("imagepath"));
					String mypath = c.getString(c.getColumnIndex("mypath"));
					String card_id = c.getString(c.getColumnIndex("card_id"));
					String storeid = c.getString(c.getColumnIndex("storeid"));
					String storeslug = c.getString(c.getColumnIndex("slug"));
					byte[] arr = c.getBlob(c.getColumnIndex("photo"));
                    if(path==null||path.equals("no")){
						files.add(null);
						
					} else {

						files.add(new File(path));
						
					}

					if (path.equals("no")) {
						try {
							obj.put("id", card_id);
							obj.put("storetag", storeslug);
							obj.put("description", st1);
							obj.put("photo_path", mypath);
							obj.put("storeid", storeid);
							jsonArray.put(obj);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {

						try {
							// obj.put("id", id);
							obj.put("storetag", storeslug);
							obj.put("description", st1);
							obj.put("photo_path", path);
							jsonArray.put(obj);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {

					JSONObject myobj = new JSONObject();
					myobj.put("title", title);
					myobj.put("draft", "0");
					myobj.put("user_id", user_id);
					myobj.put("cards", jsonArray);
					/**
					 * 
					 * Comment Edit
					 */
					myobj.put("id", ProfileDrafts.idd);

					finalobject.put("lookbook", myobj);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return finalobject;

	}

	@SuppressWarnings("deprecation")
	public void upload1(final JSONObject arr) {

		/**
		 * 
		 * Comment Edit
		 */
		String LOOKBOOK_URL = getResources().getString(R.string.base_url)
				+ "Lookbooks/edit/" + ProfileDrafts.idd + ".json";
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("jsonData", arr);

		
			for (int i = 0; i < files.size(); i++) {
			
				if (files.get(i) == null) {
				
					
					params.put("files[nochange"+i+"][jpg][error]", 4);
					
				} else {
					
					
					try {
						params.put("files[" + files.get(i).getName() + "]",
								files.get(i));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}

				
			}


		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {

				mProgressHUD = ProgressHUD.show(ImageDetail.this,
						"Processing...", true, true, ImageDetail.this);
				mProgressHUD.setCancelable(false);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

			MainActivity.img_detail = "success";

			try {

				BufferedReader br = new BufferedReader(
						new InputStreamReader(new ByteArrayInputStream(
								response)));
				String st = "";
				String st1 = "";
				while ((st = br.readLine()) != null) {

					st1 = st1 + st;

				}

				Variables.showdrafts = "drafts";

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Intent main = new Intent(ImageDetail.this, MainActivity.class);
			startActivity(main);
			finish();
				mProgressHUD.dismiss();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				mProgressHUD.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}
	
}
