package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.Feature;
import com.mystores.StoreActivity;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;
import com.store.model.StoreLookbookPojo;
import com.zakoopi.R;
import com.zakoopi.activity.ArticleView.ViewPagerAdapter;
import com.zakoopi.article.model.Article;
import com.zakoopi.article.model.ArticleMain;
import com.zakoopi.article.model.ArticleUser;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.FlowLayout;
import com.zakoopi.helper.POJO;
import com.zakoopi.helper.Variables;
import com.zakoopi.lookbookView.LookbookRecent;
import com.zakoopi.lookbookView.LookbookTagStore;
import com.zakoopi.lookbookView.RecentLookBookCardShow;
import com.zakoopi.lookbookView.RecentLookBookComment;
import com.zakoopi.lookbookView.RecentLookCommentUser;
import com.zakoopi.lookbookView.RecentLookbookMain;
import com.zakoopi.user.model.UserDetails;
import com.zakoopi.userfeed.model.OtherUserFeedAdapter;
import com.zakoopi.userfeed.model.UserFeedAdapter;
import com.zakoopi.userfeed.model.UserFeedPojo;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.PopularAdapter1;
import com.zakoopi.utils.ProfileLookbooksAdapter;
import com.zakoopi.utils.RecentAdapter1;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class LookbookView1 extends Activity {

	RelativeLayout rel_back, rel_edt_profile;
	TextView txt_head;
	SimpleSideDrawer slide_me;
	CircleImageView pro_user_pic;
	LinearLayout lin_main;
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	private static String LOOKBOOK_DETAIL_URL = "";
	static byte[] res;
	TextView txt_comment_count;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password, pro_user_f_name, pro_user_l_name, pro_user_id;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	String lookbook_id, user_name, user_pic_url, lookbook_title, view_count,
			like_count, comment_count, comment_counttt, look_user_id, is_liked,
			slug;

	String description, storeId, storeName, marketName, picture, mode_id,
			medium_img_w, medium_img_h;
	String comment, user_img, user_first_name, user_last_name;
	int com_count;
	ArrayList<RecentLookBookCardShow> lookbook_card_list = new ArrayList<RecentLookBookCardShow>();
	ArrayList<RecentLookBookComment> lookbook_comment_list = new ArrayList<RecentLookBookComment>();
	ArrayList<RecentLookBookCardShow> lookbook_card_list_1 = new ArrayList<RecentLookBookCardShow>();
	ArrayList<RecentLookBookComment> lookbook_comment_list_1 = new ArrayList<RecentLookBookComment>();
	ArrayList<LookbookTagStore> lookbookTagStores = new ArrayList<LookbookTagStore>();
	ArrayList<LookbookTagStore> store_del = new ArrayList<LookbookTagStore>();
	View view_comment;
	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.olive,
			R.color.maroon };
	private String string_like;
	public static int mypage1 = 1;
	int displayWidth;
	ProgressBar progressBar1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.lookbook_view_main);
		cd = new ConnectionDetector(LookbookView1.this);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.VISIBLE);
		/**
		 * Google Analystics
		 */

		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Lookbook View");
		t.send(new HitBuilders.AppViewBuilder().build());

		try {

			Intent i = getIntent();
			lookbook_id = i.getStringExtra("lookbook_id");
			/*
			 * user_name = i.getStringExtra("username"); user_pic_url =
			 * i.getStringExtra("userpicurl"); lookbook_title =
			 * i.getStringExtra("title"); view_count = i.getStringExtra("hits");
			 * like_count = i.getStringExtra("likes"); comment_count =
			 * i.getStringExtra("comments"); com_count =
			 * Integer.parseInt(comment_count); liked =
			 * i.getStringExtra("liked");
			 */

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {

			client = ClientHttp.getInstance(LookbookView1.this);

			/**
			 * User Login SharedPreferences
			 */

			pro_user_pref = getSharedPreferences("User_detail", 0);
			pro_user_id = pro_user_pref.getString("user_id", "hgjk");
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_f_name = pro_user_pref.getString("user_firstName", "012");
			pro_user_l_name = pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");

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

			LOOKBOOK_DETAIL_URL = getString(R.string.base_url)
					+ "lookbooks/view/" + lookbook_id + ".json";
			Point size = new Point();
			((Activity) LookbookView1.this).getWindowManager()
					.getDefaultDisplay().getSize(size);
			displayWidth = size.x;

		} catch (Exception e) {
			// TODO: handle exception
		}
		findId();
		click();

		getLookbookDetails();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void findId() {
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_head = (TextView) findViewById(R.id.txt);
		lin_main = (LinearLayout) findViewById(R.id.lookbook_lin);
		pro_user_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
		side_user_name = (TextView) findViewById(R.id.txt_user_name);
		side_about = (TextView) findViewById(R.id.txt_about);
		side_con_ac = (TextView) findViewById(R.id.txt_con_ac);
		side_edt_profile = (TextView) findViewById(R.id.txt_edit);
		side_noti_settings = (TextView) findViewById(R.id.txt_noti);
		side_rate = (TextView) findViewById(R.id.txt_rate);
		side_sign_out = (TextView) findViewById(R.id.txt_sign_out);
		side_sug_store = (TextView) findViewById(R.id.txt_sug_store);
		rel_edt_profile = (RelativeLayout) findViewById(R.id.rel_edt_profile);
		txt_head.setTypeface(typeface_semibold);
		txt_head.setText("Lookbook");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(LookbookView1.this).reportActivityStart(
				this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(LookbookView1.this)
				.reportActivityStop(this);
	}

	public void click() {

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
	}

	private View setHeader() {

		View view_header = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_view_header, null);

		TextView txt_user_name, txt_title, txt_view_count;
		final TextView txt_like_count;
		CircleImageView user_pic;

		txt_user_name = (TextView) view_header.findViewById(R.id.txt_user_name);
		user_pic = (CircleImageView) view_header
				.findViewById(R.id.img_user_profile);
		txt_title = (TextView) view_header.findViewById(R.id.txt_title);
		txt_view_count = (TextView) view_header.findViewById(R.id.txt_view);
		txt_comment_count = (TextView) view_header
				.findViewById(R.id.txt_comment);
		txt_like_count = (TextView) view_header.findViewById(R.id.txt_like);
		final ImageView img_like = (ImageView) view_header
				.findViewById(R.id.img_like);

		try {

			if (is_liked.equals("true")) {

				img_like.setImageResource(R.drawable.home_like_active);
			} else {

				img_like.setImageResource(R.drawable.notif_like);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			txt_user_name.setTypeface(typeface_bold);
			txt_title.setTypeface(typeface_semibold);
			txt_view_count.setTypeface(typeface_regular);
			txt_comment_count.setTypeface(typeface_regular);
			txt_like_count.setTypeface(typeface_regular);
			txt_user_name.setText(user_name);
			txt_title.setText(lookbook_title);
			txt_comment_count.setText(comment_count);
			txt_like_count.setText(like_count);
			txt_view_count.setText(view_count);

			Picasso.with(LookbookView1.this).load(user_pic_url)
					.placeholder(R.drawable.profile_img_3).into(user_pic);

		} catch (Exception e) {
			// TODO: handle exception
		}

		img_like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String post_mode = "Lookbooks";
				if (is_liked.equals("true")) {
					img_like.setImageResource(R.drawable.home_like_inactive);
					long count = Long.parseLong(like_count) - 1;
					txt_like_count.setText("" + count);
					is_liked = "false";
					string_like = "0";
					article_like(mode_id, string_like, post_mode);

					Tracker t = ((UILApplication) LookbookView1.this
							.getApplicationContext())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Unlike Lookbook on Featured Feed")
							.setAction("Unliked Lookbook by " + pro_user_name)
							.setLabel("Featured Feed").build());

				} else {
					img_like.setImageResource(R.drawable.home_like_active);
					long count = Long.parseLong(like_count) + 1;
					txt_like_count.setText("" + count);
					is_liked = "true";
					string_like = "1";
					article_like(mode_id, string_like, post_mode);

					Tracker t = ((UILApplication) LookbookView1.this
							.getApplicationContext())
							.getTracker(TrackerName.APP_TRACKER);
					// Build and send an Event.

					t.send(new HitBuilders.EventBuilder()
							.setCategory("Like Lookbook on Featured Feed")
							.setAction("Liked Lookbook by " + pro_user_name)
							.setLabel("Featured Feed").build());
				}

			}
		});

		return view_header;
	}

	public void article_like(final String article_id, final String string_like,
			final String post_mode) {
		/*
		 * Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/" +
		 * article_id + ".json");
		 */
		pro_user_pref = LookbookView1.this.getSharedPreferences("User_detail",
				0);
		String user_id = pro_user_pref.getString("user_id", "0");

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();

		params.put("user_id", user_id);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("like", string_like);
		params.put("entity", post_mode);

		client.post(LookbookView1.this.getString(R.string.base_url)
				+ "Common/like/" + article_id + ".json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						try {

							BufferedReader bufferedReader = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String text1 = "";
							String text2 = "";
							while ((text1 = bufferedReader.readLine()) != null) {

								text2 = text2 + text1;
							}

						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] response, Throwable e) {
						try {
							BufferedReader bufferedReader1 = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String text11 = "";
							String text21 = "";
							while ((text11 = bufferedReader1.readLine()) != null) {

								text21 = text21 + text11;
							}

						} catch (Exception ee) {

						}

					}
				});
	}

	@SuppressLint("ResourceAsColor")
	private View addLookbook(String description, String image,
			ArrayList<LookbookTagStore> store_del, String medium_img_w,
			String medium_img_h) {

		View add_lookbook = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.lookbook_view, null);

		ImageView img_lookbook = (ImageView) add_lookbook
				.findViewById(R.id.img_lookbook);
		TextView desc = (TextView) add_lookbook.findViewById(R.id.txt_has_tag);
		FlowLayout flowLayout = (FlowLayout) add_lookbook
				.findViewById(R.id.flow_layout);
		TextView txt_store_tag = (TextView) add_lookbook
				.findViewById(R.id.txt_store);

		try {

			final int width1 = displayWidth;
			final int height1 = Integer.parseInt(medium_img_h);

			final int set_height = (int) (((width1 * 1.0) / Integer
					.parseInt(medium_img_w)) * height1);

			img_lookbook.getLayoutParams().height = set_height;
			img_lookbook.getLayoutParams().width = width1;

			Picasso.with(LookbookView1.this).load(image)
					.placeholder(R.color.maroon).into(img_lookbook);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {

			desc.setTypeface(typeface_regular);
			txt_store_tag.setTypeface(typeface_semibold);
			desc.setText(description);

			for (int i = 0; i < store_del.size(); i++) {
				final TextView t = new TextView(LookbookView1.this);
				final String store_iddddd = store_del.get(i).getId();

				t.setText(store_del.get(i).getStore_name() + ", "
						+ store_del.get(i).getMarket());
				t.setBackgroundResource(R.drawable.green_store_tag);
				t.setPadding(20, 8, 20, 8);
				t.setTextSize(12);
				t.setTextColor(Color.WHITE);
				t.setSingleLine(true);
				t.setTag("" + i);

				t.setTextSize((float) 13.33);
				t.setTypeface(typeface_semibold);
				t.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {

							isInternetPresent = cd.isConnectingToInternet();
							// check for Internet status
							if (isInternetPresent) {
								Intent in = new Intent(LookbookView1.this,
										StoreActivity.class);
								in.putExtra("store_id", store_iddddd);
								startActivity(in);
							} else {
								// Internet connection is not present
								// Ask user to connect to Internet
								showAlertDialog(LookbookView1.this,
										"No Internet Connection",
										"You don't have internet connection.",
										false);
							}

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				flowLayout.addView(t, new FlowLayout.LayoutParams(20, 10));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return add_lookbook;
	}

	private View viewComment(String user_f_name, String user_l_name,
			String user_img_pic, String comment, final String user_id_com) {

		view_comment = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.article_view_comment, null);

		RelativeLayout rel_user = (RelativeLayout) view_comment
				.findViewById(R.id.rel_user);
		CircleImageView user_pic = (CircleImageView) view_comment
				.findViewById(R.id.img_user_profile);
		TextView txt_name = (TextView) view_comment
				.findViewById(R.id.txt_user_name);
		TextView txt_review = (TextView) view_comment
				.findViewById(R.id.txt_comment);

		try {

			txt_review.setText(comment);
			txt_name.setText(user_f_name + " " + user_l_name);
			txt_name.setTypeface(typeface_bold);
			txt_review.setTypeface(typeface_regular);
			Picasso.with(getApplicationContext()).load(user_img_pic)
					.placeholder(R.drawable.profile_img_3).into(user_pic);

			rel_user.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						isInternetPresent = cd.isConnectingToInternet();
						// check for Internet status
						if (isInternetPresent) {
							if (!user_id_com.equals(pro_user_id)) {
								Variables.myact = "noway";
								Intent i = new Intent(LookbookView1.this,
										OtherUserProfile.class);
								i.putExtra("user_id", user_id_com);
								startActivity(i);

							} else {
								MainActivity.pager.setCurrentItem(2, true);
							}
						} else {
							// Internet connection is not present
							// Ask user to connect to Internet
							showAlertDialog(LookbookView1.this,
									"No Internet Connection",
									"You don't have internet connection.",
									false);
						}

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}

		return view_comment;
	}

	private View addComment() {

		View add_comment = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_footer, null);

		final EditText edt_comment = (EditText) add_comment
				.findViewById(R.id.edt_comment);
		TextView txt_submit = (TextView) add_comment
				.findViewById(R.id.txt_submit);
		txt_submit.setTypeface(typeface_semibold);
		RelativeLayout rel_submit = (RelativeLayout) add_comment
				.findViewById(R.id.rel_submit);

		edt_comment.setTypeface(typeface_regular);

		rel_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					int view_count = lin_main.getChildCount();

					if (!edt_comment.getText().toString().equals("")) {
						lin_main.addView(viewComment(pro_user_f_name + " "
								+ pro_user_l_name, pro_user_pic_url,
								edt_comment.getText().toString(),
								String.valueOf(view_count - 1), pro_user_id));

						addOwnComment(mode_id,
								edt_comment.getText().toString(), "LBK");

						edt_comment.setText("");
						edt_comment.setHint("Add a comment....");

						Tracker t = ((UILApplication) getApplication())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("submit comment on Lookbook View")
								.setAction("submit comment by " + pro_user_name)
								.setLabel("Lookbook View").build());

					} else {

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		});

		return add_comment;
	}

	public void lookbookView() {

		new MyCard().execute(lookbook_card_list);

	

	}

	private class MyCard extends
			AsyncTask<ArrayList<RecentLookBookCardShow>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(
				ArrayList<RecentLookBookCardShow>... params) {

			lookbook_card_list_1 = params[0];
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < lookbook_card_list_1.size(); i++) {

				RecentLookBookCardShow cs = lookbook_card_list_1.get(i);
				try {

					lookbookTagStores = cs.getStores();
					description = cs.getdescription();
					picture = cs.getPic();
					mode_id = cs.getLookbookId();
					medium_img_w = cs.getMedium_img_w();
					medium_img_h = cs.getMedium_img_h();
					store_del.clear();

					for (int j = 0; j < lookbookTagStores.size(); j++) {

						LookbookTagStore ts = lookbookTagStores.get(j);

						storeId = ts.getId();
						storeName = ts.getStore_name();
						marketName = ts.getMarket();

						store_del.add(ts);
					}

					// Log.e("HI_WE", medium_img_w +"--"+medium_img_h );
					lin_main.addView(addLookbook(description, picture,
							store_del, medium_img_w, medium_img_h));
					progressBar1.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			new MyComment().execute(lookbook_comment_list);

		}

	}

	private class MyComment extends
			AsyncTask<ArrayList<RecentLookBookComment>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(
				ArrayList<RecentLookBookComment>... params) {

			lookbook_comment_list_1 = params[0];
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for (int i = 0; i < lookbook_comment_list_1.size(); i++) {

				RecentLookBookComment lc = lookbook_comment_list_1.get(i);

				try {

					comment = lc.getComment();

					RecentLookCommentUser lcm_user = lc.getUser();
					String user_id_com = lcm_user.getId();
					user_img = lcm_user.getAndroid_api_img();
					user_first_name = lcm_user.getFirst_name();
					user_last_name = lcm_user.getLast_name();
					lin_main.addView(viewComment(user_first_name,
							user_last_name, user_img, comment, user_id_com));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			try {
				lin_main.addView(addComment());
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public void addOwnComment(String look_art_id, String msg, String mode) {

		pro_user_pref = getSharedPreferences("User_detail", 0);
		String user_id = pro_user_pref.getString("user_id", "0");

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();

		params.put("id", look_art_id);
		params.put("comment", msg);
		params.put("model", mode);

		client.post(this.getString(R.string.base_url)
				+ "Common/commentOnLbkAndArt/" + user_id + ".json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						try {
						
							BufferedReader bufferedReader = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String text1 = "";
							String text2 = "";
							while ((text1 = bufferedReader.readLine()) != null) {

								text2 = text2 + text1;
							}

						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] response, Throwable e) {
						try {
							BufferedReader bufferedReader1 = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							String text11 = "";
							String text21 = "";
							while ((text11 = bufferedReader1.readLine()) != null) {

								text21 = text21 + text11;
							}
							int com_count = Integer.parseInt(comment_count) + 1;
							txt_comment_count.setText("" + com_count);
						} catch (Exception ee) {

						}

					}
				});

	}

	@SuppressWarnings("deprecation")
	public void getLookbookDetails() {

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(LOOKBOOK_DETAIL_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub
				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));

					String line = "";
					String text = "";

					while ((line = br.readLine()) != null) {

						text = text + line;

					}

					showLookbookData(text);

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void showLookbookData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);

		RecentLookbookMain main = gson.fromJson(reader,
				RecentLookbookMain.class);

		LookbookRecent lookbookRecent = main.getLookbook();
		UserDetails user_de = lookbookRecent.getUser();
		mode_id = lookbookRecent.getId();
		user_name = user_de.getFirst_name() + " " + user_de.getLast_name();
		user_pic_url = user_de.getAndroid_api_img();
		lookbook_title = lookbookRecent.getTitle();
		view_count = lookbookRecent.getHits_text();
		like_count = lookbookRecent.getLikes_count();
		comment_count = lookbookRecent.getLookbookcomment_count();
		look_user_id = lookbookRecent.getUser_id();
		is_liked = lookbookRecent.getIs_liked();
		slug = lookbookRecent.getSlug();

		lookbook_card_list = lookbookRecent.getRecentCards();

		lookbook_comment_list = lookbookRecent.getLookbookcomments();
		comment_counttt = comment_count;
		if (user_name != null) {
			lin_main.addView(setHeader());
			lookbookView();
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
