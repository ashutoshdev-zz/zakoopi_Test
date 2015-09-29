package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.article.model.Article;
import com.zakoopi.article.model.ArticleComment;
import com.zakoopi.article.model.ArticleCommentUser;
import com.zakoopi.article.model.ArticleImage;
import com.zakoopi.article.model.ArticleMain;
import com.zakoopi.article.model.ArticleUser;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

public class ArticleView extends Activity {

	// ImageView img_menu;
	RelativeLayout rel_back, rel_edt_profile;
	TextView txt_head;
	CircleImageView pro_user_pic;
	LinearLayout lin_main;
	static AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	private static String ARTICLE_DETAIL_URL = "";
	static byte[] res;
	TextView txt_comment_count;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password, pro_user_f_name, pro_user_l_name, pro_user_id;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;

	String article_id, user_name, user_pic_url, article_title, view_count,
			like_count, comment_count, description, is_new, mode_id,
			comment_counttt, is_liked, slug, article_user_id, string_like;
	private DisplayImageOptions options;
	ArrayList<ArticleImage> url_list = new ArrayList<ArticleImage>();
	ArrayList<ArticleComment> comment_list = new ArrayList<ArticleComment>();
	private ViewPager pager;
	String comment, user_img, user_first_name, user_last_name;
	List<ArticleComment> lis_com = new ArrayList<ArticleComment>();
	int com_count;
	ProgressBar progressBar1;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.article_view_main);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.VISIBLE);
		cd = new ConnectionDetector(ArticleView.this);
		client = ClientHttp.getInstance(ArticleView.this);

		/**
		 * Google Analystics
		 */
		Tracker t = ((UILApplication) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Article View");
		t.send(new HitBuilders.AppViewBuilder().build());
		try {

			Intent i = getIntent();
			article_id = i.getStringExtra("article_id");
			/*
			 * user_name = i.getStringExtra("username"); user_pic_url =
			 * i.getStringExtra("userpicurl"); article_title =
			 * i.getStringExtra("title"); view_count = i.getStringExtra("hits");
			 * like_count = i.getStringExtra("likes"); description =
			 * i.getStringExtra("description"); is_new =
			 * i.getStringExtra("is_new"); comment_count =
			 * i.getStringExtra("comments"); com_count =
			 * Integer.parseInt(comment_count); liked =
			 * i.getStringExtra("liked");
			 */

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
		} catch (Exception e) {
			// TODO: handle exception
		}
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

		ARTICLE_DETAIL_URL = getString(R.string.base_url) + "articles/view/"
				+ article_id + ".json";

		findId();
		// setMenuData();
		click();

		getArticleDetails();
		/*
		 * try {
		 * 
		 * if (user_name != null) { lin_main.addView(setHeader()); }
		 * 
		 * if (is_new.equals("false")) { lin_main.addView(addOldImage());
		 * article_view(); } else { article_view(); //
		 * lin_main.addView(setArticleData()); //
		 * lin_main.addView(viewComment(user_first_name, user_last_name, //
		 * user_img, comment)); // lin_main.addView(addComment());
		 * 
		 * }
		 * 
		 * if (comment_count == null) { lin_main.addView(viewComment()); }
		 * 
		 * } catch (Exception e) { // TODO: handle exception }
		 */
	}

	public void findId() {

		// img_menu = (ImageView) findViewById(R.id.img_menu);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_head = (TextView) findViewById(R.id.txt);
		lin_main = (LinearLayout) findViewById(R.id.article_lin);
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
		txt_head.setText("Article");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(ArticleView.this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(ArticleView.this).reportActivityStop(this);
	}

	public void click() {

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
			txt_user_name.setTypeface(typeface_semibold);
			txt_title.setTypeface(typeface_semibold);
			txt_view_count.setTypeface(typeface_regular);
			txt_comment_count.setTypeface(typeface_regular);
			txt_like_count.setTypeface(typeface_regular);

			txt_user_name.setText(user_name);
			txt_title.setText(article_title);
			txt_comment_count.setText(comment_count);
			txt_like_count.setText(like_count);
			txt_view_count.setText(view_count);

			Picasso.with(ArticleView.this).load(user_pic_url)
					.placeholder(R.drawable.profile_img_3).into(user_pic);

			/*
			 * ImageLoader.getInstance().displayImage(user_pic_url, user_pic,
			 * options);
			 */
		} catch (Exception e) {
			// TODO: handle exception
		}

		img_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String post_mode = "Articles";
				if (is_liked.equals("true")) {
					img_like.setImageResource(R.drawable.home_like_inactive);
					long count = Long.parseLong(like_count) - 1;
					txt_like_count.setText("" + count);
					is_liked = "false";
					string_like = "0";
					article_like(mode_id, string_like, post_mode);

					Tracker t = ((UILApplication) ArticleView.this
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

					Tracker t = ((UILApplication) ArticleView.this
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

	private View setArticleData() {

		View view_article = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_show, null);

		WebView webview = (WebView) view_article
				.findViewById(R.id.article_webview);
		try {

			webview.setWebViewClient(new myWebClient());
			webview.getSettings().setJavaScriptEnabled(true);

			String css = description
					+ "<style type=\"text/css\"> img {width:100% !important; height: auto !important;}"
					+ "@font-face {font-family: SourceSans;src: url(\"file:///android_asset/fonts/SourceSansPro-Regular.ttf\")}"
					+ "* {font-family: SourceSans}" + "</style>";
			webview.loadDataWithBaseURL(getString(R.string.base_url), css,
					"text/html", "UTF-8", "");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return view_article;
	}

	private View viewComment(String user_f_name, String user_l_name,
			String user_img_pic, String comment, final String user_id_com) {

		View view_comment = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_view_comment, null);
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

			/*
			 * ImageLoader.getInstance().displayImage(user_img_pic, user_pic,
			 * options);
			 */

			rel_user.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					try {

						isInternetPresent = cd.isConnectingToInternet();
						// check for Internet status
						if (isInternetPresent) {

							if (!user_id_com.equals(pro_user_id)) {

								Intent i = new Intent(ArticleView.this,
										OtherUserProfile.class);
								i.putExtra("user_id", user_id_com);
								startActivity(i);

							} else {
								MainActivity.pager.setCurrentItem(2, true);
							}

						} else {
							showAlertDialog(ArticleView.this,
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
						edt_comment.setText("");
						edt_comment.setHint("Add a comment....");
						addOwnComment(article_id, edt_comment.getText()
								.toString(), "ART");

						Tracker t = ((UILApplication) getApplication())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("submit comment on Article View")
								.setAction("submit comment by " + pro_user_name)
								.setLabel("Article View").build());

					} else {

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		return add_comment;
	}

	private View addOldImage() {

		View old_image = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.article_old_image, null);
		pager = (ViewPager) old_image.findViewById(R.id.old_image);

		return old_image;
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

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			new MyApp().execute(comment_list);
		}

	}

	public class ViewPagerAdapter extends PagerAdapter {
		// Declare Variables
		Context context;
		LayoutInflater inflater;
		ArrayList<ArticleImage> list;
		DynamicImageView imgflag;

		// ProgressBar prog;

		public ViewPagerAdapter(Context context, ArrayList<ArticleImage> list) {
			this.context = context;
			this.list = list;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((RelativeLayout) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// Declare Variables

			// position=Integer.parseInt(pos);

			View itemView = inflater.inflate(R.layout.zoom_items, container,
					false);

			// Locate the ImageView in viewpager_item.xml
			imgflag = (DynamicImageView) itemView.findViewById(R.id.imageView2);
			// prog = (ProgressBar) itemView.findViewById(R.id.progressBar1);

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.placeholder_photo)
					.showImageOnFail(R.drawable.placeholder_photo)
					.showImageOnLoading(R.color.maroon).build();

			ImageLoader.getInstance().displayImage(
					list.get(position).getAndroid_api_img(), imgflag, options,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// prog.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// prog.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// prog.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// prog.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							// prog.setVisibility(View.GONE);
						}
					});

			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Remove viewpager_item.xml from ViewPager
			((ViewPager) container).removeView((RelativeLayout) object);

		}
	}

	public void article_view() {

		try {
			PagerAdapter pagerAdapter = new ViewPagerAdapter(ArticleView.this,
					url_list);
			if (is_new.equals("false")) {
				pager.setAdapter(pagerAdapter);

				lin_main.addView(setArticleData());
				progressBar1.setVisibility(View.GONE);
				// new MyApp().execute(comment_list);
			} else {
				lin_main.addView(setArticleData());
				progressBar1.setVisibility(View.GONE);
				// new MyApp().execute(comment_list);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private class MyApp extends AsyncTask<List<ArticleComment>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(List<ArticleComment>... params) {
			lis_com = params[0];
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for (int i = 0; i < lis_com.size(); i++) {
				ArticleComment ac = lis_com.get(i);

				comment = ac.getComment();

				ArticleCommentUser cm_user = ac.getUser();
				String user_id_com = cm_user.getId();
				user_img = cm_user.getAndroid_api_img();
				user_first_name = cm_user.getFirst_name();
				user_last_name = cm_user.getLast_name();
				try {

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

		// lin_main.addView(addComment());
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
							int com_count = Integer.parseInt(comment_count) + 1;
							txt_comment_count.setText("" + com_count);

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

	public void article_like(final String article_id, final String string_like,
			final String post_mode) {
		/*
		 * Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/" +
		 * article_id + ".json");
		 */
		pro_user_pref = ArticleView.this.getSharedPreferences("User_detail", 0);
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

		client.post(ArticleView.this.getString(R.string.base_url)
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

	public void getArticleDetails() {

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		client.get(ARTICLE_DETAIL_URL, new AsyncHttpResponseHandler() {

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

					showArticleData(text);

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

	public void showArticleData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);

		ArticleMain article_main = gson.fromJson(reader, ArticleMain.class);
		Article art = article_main.getArticle();
		ArticleUser art_user = art.getUser();
		article_title = art.getTitle();
		view_count = art.getHits_text();
		like_count = art.getLikes_count();
		comment_count = art.getArticle_comment_count();
		description = art.getDescription();
		is_new = art.getIs_new();
		mode_id = art.getId();
		is_liked = art.getIs_liked();
		slug = art.getSlug();
		user_name = art_user.getFirst_name() + " " + art_user.getLast_name();
		user_pic_url = art_user.getAndroid_api_img();
		article_user_id = art_user.getId();

		url_list = art.getArticle();
		comment_list = art.getArticle_comments();
		comment_counttt = comment_count;

		try {

			if (user_name != null) {
				lin_main.addView(setHeader());
			}

			if (is_new.equals("false")) {
				lin_main.addView(addOldImage());
				article_view();
			} else {
				article_view();
				// lin_main.addView(setArticleData());
				// lin_main.addView(viewComment(user_first_name, user_last_name,
				// user_img, comment));
				// lin_main.addView(addComment());

			}
			/*
			 * if (comment_count == null) { lin_main.addView(viewComment()); }
			 */
		} catch (Exception e) {
			// TODO: handle exception
		}

		/*
		 * PagerAdapter pagerAdapter = new ViewPagerAdapter(ArticleView.this,
		 * url_list);
		 */

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
