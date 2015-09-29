package com.zakoopi.userfeed.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.StoreActivity;
import com.navdrawer.SimpleSideDrawer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.activity.ArticleView;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.activity.OtherUserProfile;
import com.zakoopi.activity.SplashScreen;
import com.zakoopi.article.model.Article;
import com.zakoopi.article.model.ArticleComment;
import com.zakoopi.article.model.ArticleCommentUser;
import com.zakoopi.article.model.ArticleImage;
import com.zakoopi.article.model.ArticleMain;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.FlowLayout;
import com.zakoopi.helper.Variables;
import com.zakoopi.lookbookView.LookbookRecent;
import com.zakoopi.lookbookView.LookbookTagStore;
import com.zakoopi.lookbookView.RecentLookBookCardShow;
import com.zakoopi.lookbookView.RecentLookBookComment;
import com.zakoopi.lookbookView.RecentLookCommentUser;
import com.zakoopi.lookbookView.RecentLookbookMain;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class OtherUserFeedAdapter extends BaseAdapter {

	List<UserFeedPojo> mList;
	int pos = 0;
	Context ctx;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// private DisplayImageOptions options;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password, pro_user_id, pro_user_f_name, pro_user_l_name;
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;
	SharedPreferences pro_user_pref;
	String article_color;
	ArrayList<Integer> colorlist;
	private List<ResolveInfo> listApp;
	int mode_position;
	String like;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	private SharedPreferences pref_location;
	int displayWidth;
	private String city_name;
	String url_img, user_name;
	String comment_counttt;
	String st, statu;
	/**
	 * 
	 * 
	 */

	RelativeLayout rel_back, rel_edt_profile;
	TextView txt_head;
	SimpleSideDrawer slide_me;
	CircleImageView pro_user_pic;
	LinearLayout lin_main;
	Dialog dd;
	TextView txt_comment_count;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;

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
	List<ArticleComment> lis_com = new ArrayList<ArticleComment>();
	ArrayList<ArticleImage> url_list = new ArrayList<ArticleImage>();
	ArrayList<ArticleComment> comment_list = new ArrayList<ArticleComment>();
	private ViewPager pager;
	String art_id;
	ProgressBar progressBar1;
	
Boolean isInternetPresent = false;
    
    // Connection detector class
    ConnectionDetector cd;

	public OtherUserFeedAdapter(Context context, List<UserFeedPojo> list,
			ArrayList<Integer> color, String url_img, String user_name) {
		ctx = context;
		mList = list;
		this.colorlist = color;
		this.url_img = url_img;
		this.user_name = user_name;

		try {
			cd = new ConnectionDetector(ctx);
			pref_location = ctx.getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "123");
			client = ClientHttp.getInstance(ctx);

			/**
			 * User Login SharedPreferences
			 */
			pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
			pro_user_pic_url = pro_user_pref.getString("user_image", "123");
			pro_user_name = pro_user_pref.getString("user_firstName", "012")
					+ " " + pro_user_pref.getString("user_lastName", "458");
			pro_user_location = pro_user_pref
					.getString("user_location", "4267");
			pro_user_f_name = pro_user_pref.getString("user_firstName", "012");
			pro_user_l_name = pro_user_pref.getString("user_lastName", "458");
			pro_user_id = pro_user_pref.getString("user_id", "adajfh");
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");

			Point size = new Point();
			((Activity) ctx).getWindowManager().getDefaultDisplay()
					.getSize(size);
			displayWidth = size.x;

		} catch (Exception e) {
			// TODO: handle exception
		}
		/**
		 * Typeface
		 */

		typeface_semibold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

	}

	public void addItems(List<UserFeedPojo> newItems) {
		if (null == newItems || newItems.size() <= 0) {
			return;
		}

		if (null == mList) {
			mList = new ArrayList<UserFeedPojo>();
		}

		mList.addAll(newItems);
		notifyDataSetChanged();
	}

	public void addColors(List<Integer> newItems) {
		if (null == newItems || newItems.size() <= 0) {
			return;
		}

		if (null == mList) {
			colorlist = new ArrayList<Integer>();
		}

		colorlist.addAll(newItems);

	}

	@Override
	public int getCount() {
		if (null == mList) {
			return 0;
		}

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		listApp = showAllShareApp();
		int type = this.getItemViewType(position);

		switch (type) {

		case 0:
			View result = convertView;
			final LookbookHolder holder;


			if (result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				result = inf
						.inflate(R.layout.lookbook_feed_item, parent, false);
				holder = new LookbookHolder();
				holder.user_name = (TextView) result
						.findViewById(R.id.user_name);
				holder.lookbook_view = (TextView) result
						.findViewById(R.id.user_view);
				holder.img_view = (ImageView) result
						.findViewById(R.id.img_view);
				holder.lookbook_like = (TextView) result
						.findViewById(R.id.txt_like_count);
				holder.user_image = (ImageView) result
						.findViewById(R.id.img_profile);
				holder.title = (TextView) result.findViewById(R.id.txt_title);
				holder.look_image = (ImageView) result
						.findViewById(R.id.img_flash);
				holder.like_image = (ImageView) result
						.findViewById(R.id.img_like);

				holder.share_image = (ImageView) result
						.findViewById(R.id.img_share);
				holder.img1 = (ImageView) result.findViewById(R.id.post_img1);
				holder.img2 = (ImageView) result.findViewById(R.id.post_img2);
				holder.last_text = (RelativeLayout) result
						.findViewById(R.id.rel_post_img_count);
				holder.image_count = (TextView) result
						.findViewById(R.id.txt_count);

				holder.rel_img_count = (RelativeLayout) result
						.findViewById(R.id.rel_113);

				holder.user_name.setTypeface(typeface_bold);
				holder.lookbook_view.setTypeface(typeface_bold);
				holder.title.setTypeface(typeface_semibold);
				holder.image_count.setTypeface(typeface_bold);
				holder.lookbook_like.setTypeface(typeface_bold);

				result.setTag(holder);

				holder.user_image.setVisibility(View.GONE);
				holder.like_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						ImageView img = (ImageView) v;
						UserFeedPojo ppp = (UserFeedPojo) img.getTag();
						String string_like;
						String post_mode = "Lookbooks";
						String article_iddd = ppp.getIdd();
						String get_likes = ppp.getLikes();
						if (ppp.getIs_liked().equals("true")) {

							holder.like_image
									.setImageResource(R.drawable.home_like_inactive);
							long count = Long.parseLong(ppp.getLikes()) - 1;
							holder.lookbook_like.setText("" + count);
							ppp.setIs_liked("false");
							ppp.setLikes("" + count);
							string_like = "0";

							article_like(article_iddd, string_like, post_mode);

							Tracker t = ((UILApplication) ctx
									.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory(
											"Unlike Lookbook on Other User Profile")
									.setAction(
											"Unliked Lookbook by "
													+ pro_user_name)
									.setLabel("Other User Profile").build());

						} else {

							holder.like_image
									.setImageResource(R.drawable.home_like_active);
							long count = Long.parseLong(ppp.getLikes()) + 1;
							holder.lookbook_like.setText("" + count);
							ppp.setIs_liked("true");
							ppp.setLikes("" + count);
							string_like = "1";
							article_like(article_iddd, string_like, post_mode);

							Tracker t = ((UILApplication) ctx
									.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory(
											"Like Lookbook on Other User Profile")
									.setAction(
											"Liked Lookbook by "
													+ pro_user_name)
									.setLabel("Other User Profile").build());
						}

					}
				});

			} else {
				holder = (LookbookHolder) result.getTag();
			}

			final UserFeedPojo pojo = mList.get(position);
			holder.like_image.setTag(pojo);

			try {
				final int width1 = displayWidth;
				final int height1 = Integer.parseInt(pojo.getMedium_img_h());

				final int set_height = (int) (((width1 * 1.0) / Integer
						.parseInt(pojo.getMedium_img_w())) * height1);

				holder.look_image.getLayoutParams().height = set_height;
				holder.look_image.getLayoutParams().width = width1;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			try {

				if (pojo.getIs_liked().equals("true")) {

					holder.like_image
							.setImageResource(R.drawable.home_like_active);
					holder.lookbook_like.setText(pojo.getLikes());
				} else {

					holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
					holder.lookbook_like.setText(pojo.getLikes());
				}

				if (Integer.parseInt(pojo.getImage_count()) >= 3) {

					Picasso.with(ctx).load(pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(holder.look_image);

					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);

					Picasso.with(ctx).load(pojo.getImg1())
							.placeholder(colorlist.get(position))
							.into(holder.img1);

					Picasso.with(ctx).load(pojo.getImg2())
							.placeholder(colorlist.get(position))
							.into(holder.img2);
				

					if (Integer.parseInt(pojo.getImage_count()) == 3) {

						holder.last_text.setVisibility(View.GONE);
					} else {
						holder.last_text.setVisibility(View.VISIBLE);
						holder.image_count.setText("+" + pojo.getImage_count());
					}

				}

				else if (Integer.parseInt(pojo.getImage_count()) == 2) {

					Picasso.with(ctx).load(pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(holder.look_image);

					Picasso.with(ctx).load(pojo.getImg1())
							.placeholder(colorlist.get(position))
							.into(holder.img2);

				
					holder.img2.setVisibility(View.VISIBLE);
					holder.img1.setVisibility(View.GONE);
					holder.last_text.setVisibility(View.GONE);

				} else {

					Picasso.with(ctx).load(pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(holder.look_image);

					holder.img2.setVisibility(View.GONE);
					holder.img1.setVisibility(View.GONE);
					holder.last_text.setVisibility(View.GONE);

				}

				holder.user_name.setText("Added a lookbook");

				int hits_count = Integer.parseInt(pojo.getView_count());
				if (hits_count >= 100 ) {
					holder.lookbook_view.setVisibility(View.VISIBLE);
					holder.lookbook_view.setText(pojo.getHits());
					holder.img_view.setVisibility(View.VISIBLE);
				} else {
					holder.lookbook_view.setVisibility(View.GONE);
					holder.img_view.setVisibility(View.GONE);
				}
				
				holder.title.setText(pojo.getTitle());

				holder.look_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								try {

									// pos = position;
									Variables.myact = "otherLook";

									LookbookDialog(ctx, pojo.getIdd(),
											user_name, url_img,
											pojo.getTitle(), pojo.getHits(),
											pojo.getLikes(),
											pojo.getComment_count(),
											pojo.getIs_liked(), position);

									 

								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						});

				holder.title.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						try {
							isInternetPresent = cd.isConnectingToInternet();
				             // check for Internet status
				                if (isInternetPresent) {
				                    // Internet Connection is Present
				                	LookbookDialog(ctx, pojo.getIdd(), user_name,
											url_img, pojo.getTitle(), pojo.getHits(),
											pojo.getLikes(), pojo.getComment_count(),
											pojo.getIs_liked(), position);

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Lookbook view on Other User Profile")
											.setAction(
													"Clicked on Lookbook Title by "
															+ pro_user_name)
											.setLabel("Other User Profile").build());
				                } else {
				                    // Internet connection is not present
				                    // Ask user to connect to Internet
				                    showAlertDialog(ctx, "No Internet Connection",
				                            "You don't have internet connection.", false);
				                }

							
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				
				holder.rel_img_count
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								try {
									
									isInternetPresent = cd.isConnectingToInternet();
						             // check for Internet status
						                if (isInternetPresent) {
						                    // Internet Connection is Present
						                	LookbookDialog(ctx, pojo.getIdd(),
													user_name, url_img,
													pojo.getTitle(), pojo.getHits(),
													pojo.getLikes(),
													pojo.getComment_count(),
													pojo.getIs_liked(), position);

											Tracker t = ((UILApplication) ctx
													.getApplicationContext())
													.getTracker(TrackerName.APP_TRACKER);
											// Build and send an Event.

											t.send(new HitBuilders.EventBuilder()
													.setCategory(
															"Lookbook view on Other User Profile")
													.setAction(
															"Clicked on Lookbook Count by "
																	+ pro_user_name)
													.setLabel("Other User Profile")
													.build());
						                } else {
						                    // Internet connection is not present
						                    // Ask user to connect to Internet
						                    showAlertDialog(ctx, "No Internet Connection",
						                            "You don't have internet connection.", false);
						                }

									

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});

				holder.share_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {

							if (listApp != null) {

								customDialog(holder.look_image, "Lookbooks",
										pojo.getSlug(), pojo.getTitle(), pojo.getIdd(), pro_user_id);
							}

							Tracker t = ((UILApplication) ctx
									.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory("Share on  Other User Profile")
									.setAction(
											"Shared Lookbook by "
													+ pro_user_name)
									.setLabel("Other User Profile").build());

						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		case 1:
			View article_result = convertView;
			final ArticleHolder article_holder;


			if (article_result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				article_result = inf.inflate(R.layout.article_feed_item,
						parent, false);
				article_holder = new ArticleHolder();
				article_holder.user_name = (TextView) article_result
						.findViewById(R.id.user_name);
				article_holder.lookbook_view = (TextView) article_result
						.findViewById(R.id.user_view);
				article_holder.img_view = (ImageView) article_result
						.findViewById(R.id.img_view);
				article_holder.lookbook_like = (TextView) article_result
						.findViewById(R.id.txt_like_count);
				article_holder.user_image = (ImageView) article_result
						.findViewById(R.id.img_profile);
				article_holder.title = (TextView) article_result
						.findViewById(R.id.txt_title);
				article_holder.look_image = (ImageView) article_result
						.findViewById(R.id.img_flash);
				article_holder.like_image = (ImageView) article_result
						.findViewById(R.id.img_like);

				article_holder.share_image = (ImageView) article_result
						.findViewById(R.id.img_share);
				article_holder.img1 = (ImageView) article_result
						.findViewById(R.id.post_img1);
				article_holder.img2 = (ImageView) article_result
						.findViewById(R.id.post_img2);
				article_holder.last_text = (RelativeLayout) article_result
						.findViewById(R.id.rel_post_img_count);
				article_holder.image_count = (TextView) article_result
						.findViewById(R.id.txt_count);

				article_holder.rel_img_count = (RelativeLayout) article_result
						.findViewById(R.id.rel_113);

				article_holder.user_name.setTypeface(typeface_bold);
				article_holder.lookbook_view.setTypeface(typeface_bold);
				article_holder.title.setTypeface(typeface_semibold);
				article_holder.image_count.setTypeface(typeface_bold);
				article_holder.lookbook_like.setTypeface(typeface_bold);

				article_result.setTag(article_holder);
				article_holder.user_image.setVisibility(View.GONE);
				article_holder.like_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								ImageView img = (ImageView) v;
								UserFeedPojo ppp = (UserFeedPojo) img.getTag();
								String string_like;
								String post_mode = "Articles";
								String article_iddd = ppp.getIdd();
								String get_likes = ppp.getLikes();
								if (ppp.getIs_liked().equals("true")) {

									article_holder.like_image
											.setImageResource(R.drawable.home_like_inactive);
									long count = Long.parseLong(ppp.getLikes()) - 1;
									article_holder.lookbook_like.setText(""
											+ count);
									ppp.setIs_liked("false");
									ppp.setLikes("" + count);
									string_like = "0";
									article_like(article_iddd, string_like,
											post_mode);

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Unlike Article on Other User Profile")
											.setAction(
													"Unliked Article by "
															+ pro_user_name)
											.setLabel("Other User Profile")
											.build());

								} else {

									article_holder.like_image
											.setImageResource(R.drawable.home_like_active);
									long count = Long.parseLong(ppp.getLikes()) + 1;
									article_holder.lookbook_like.setText(""
											+ count);
									ppp.setIs_liked("true");
									ppp.setLikes("" + count);
									string_like = "1";
									article_like(article_iddd, string_like,
											post_mode);

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Like Article on Other User Profile")
											.setAction(
													"Liked Article by "
															+ pro_user_name)
											.setLabel("Other User Profile")
											.build());
								}

							}
						});

			} else {
				article_holder = (ArticleHolder) article_result.getTag();
			}

			final UserFeedPojo article_pojo = mList.get(position);

			try {
				final int width1 = displayWidth;
				final int height1 = Integer.parseInt(article_pojo
						.getMedium_img_h());

				final int set_height = (int) (((width1 * 1.0) / Integer
						.parseInt(article_pojo.getMedium_img_w())) * height1);

				article_holder.look_image.getLayoutParams().height = set_height;
				article_holder.look_image.getLayoutParams().width = width1;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			try {
				if (Integer.parseInt(article_pojo.getImage_count()) >= 3) {

					Picasso.with(ctx).load(article_pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(article_holder.look_image);

					article_holder.img1.setVisibility(View.VISIBLE);
					article_holder.img2.setVisibility(View.VISIBLE);

					Picasso.with(ctx).load(article_pojo.getImg1())
							.placeholder(colorlist.get(position))
							.into(article_holder.img1);

					Picasso.with(ctx).load(article_pojo.getImg2())
							.placeholder(colorlist.get(position))
							.into(article_holder.img2);
				

					if (Integer.parseInt(article_pojo.getImage_count()) == 3) {

						article_holder.last_text.setVisibility(View.GONE);
					} else {
						article_holder.last_text.setVisibility(View.VISIBLE);
						article_holder.image_count.setText("+"
								+ article_pojo.getImage_count());
					}

				}

				else if (Integer.parseInt(article_pojo.getImage_count()) == 2) {

					Picasso.with(ctx).load(article_pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(article_holder.look_image);

					Picasso.with(ctx).load(article_pojo.getImg1())
							.placeholder(colorlist.get(position))
							.into(article_holder.img2);

					
					article_holder.img2.setVisibility(View.VISIBLE);
					article_holder.img1.setVisibility(View.GONE);
					article_holder.last_text.setVisibility(View.GONE);
				} else {

					Picasso.with(ctx).load(article_pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(article_holder.look_image);

					article_holder.img1.setVisibility(View.GONE);
					article_holder.img2.setVisibility(View.GONE);
					article_holder.last_text.setVisibility(View.GONE);
				}

				try {

					if (article_pojo.getIs_liked().equals("true")) {

						article_holder.like_image
								.setImageResource(R.drawable.home_like_active);
						article_holder.lookbook_like.setText(article_pojo
								.getLikes());
						
					} else {

						article_holder.like_image
								.setImageResource(R.drawable.home_like_inactive);
						article_holder.lookbook_like.setText(article_pojo
								.getLikes());
						
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				article_holder.user_name.setText("Added a article");

				int hits_count = Integer.parseInt(article_pojo.getHits_text());
				if (hits_count >= 100 ) {
					article_holder.lookbook_view.setVisibility(View.VISIBLE);
					article_holder.lookbook_view.setText(article_pojo.getHits());
					article_holder.img_view.setVisibility(View.VISIBLE);
				} else {
					article_holder.lookbook_view.setVisibility(View.GONE);
					article_holder.img_view.setVisibility(View.GONE);
				}
				
				article_holder.title.setText(article_pojo.getTitle());
				article_holder.lookbook_like.setText(article_pojo.getLikes());

				article_holder.title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {
							
							isInternetPresent = cd.isConnectingToInternet();
				             // check for Internet status
				                if (isInternetPresent) {
				                    // Internet Connection is Present
				                	ArticleDialog(ctx, article_pojo.getIdd(),
											user_name, url_img,
											article_pojo.getTitle(),
											article_pojo.getHits(),
											article_pojo.getLikes(),
											article_pojo.getDescription(),
											article_pojo.getIsnew(),
											article_pojo.getComment_count(),
											article_pojo.getIs_liked(), position);

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Article view on Other User Profile")
											.setAction(
													"Clicked on Article Title by "
															+ pro_user_name)
											.setLabel("Other User Profile").build());
				                } else {
				                    // Internet connection is not present
				                    // Ask user to connect to Internet
				                    showAlertDialog(ctx, "No Internet Connection",
				                            "You don't have internet connection.", false);
				                }

							

						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});

				article_holder.look_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								try {
									
									isInternetPresent = cd.isConnectingToInternet();
						             // check for Internet status
						                if (isInternetPresent) {
						                    // Internet Connection is Present
						                	ArticleDialog(ctx, article_pojo.getIdd(),
													user_name, url_img,
													article_pojo.getTitle(),
													article_pojo.getHits(),
													article_pojo.getLikes(),
													article_pojo.getDescription(),
													article_pojo.getIsnew(),
													article_pojo.getComment_count(),
													article_pojo.getIs_liked(),
													position);

											Tracker t = ((UILApplication) ctx
													.getApplicationContext())
													.getTracker(TrackerName.APP_TRACKER);
											// Build and send an Event.

											t.send(new HitBuilders.EventBuilder()
													.setCategory(
															"Article view on Other User Profile")
													.setAction(
															"Clicked on Article Cover by "
																	+ pro_user_name)
													.setLabel("Other User Profile")
													.build());
						                } else {
						                    // Internet connection is not present
						                    // Ask user to connect to Internet
						                    showAlertDialog(ctx, "No Internet Connection",
						                            "You don't have internet connection.", false);
						                }

									

								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						});

				article_holder.rel_img_count
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								try {
									
									isInternetPresent = cd.isConnectingToInternet();
						             // check for Internet status
						                if (isInternetPresent) {
						                    // Internet Connection is Present
						                	ArticleDialog(ctx, article_pojo.getIdd(),
													user_name, url_img,
													article_pojo.getTitle(),
													article_pojo.getHits(),
													article_pojo.getLikes(),
													article_pojo.getDescription(),
													article_pojo.getIsnew(),
													article_pojo.getComment_count(),
													article_pojo.getIs_liked(),
													position);

											Tracker t = ((UILApplication) ctx
													.getApplicationContext())
													.getTracker(TrackerName.APP_TRACKER);
											// Build and send an Event.

											t.send(new HitBuilders.EventBuilder()
													.setCategory(
															"Article view on Other User Profile")
													.setAction(
															"Clicked on Article Count by "
																	+ pro_user_name)
													.setLabel("Other User Profile")
													.build());
						                } else {
						                    // Internet connection is not present
						                    // Ask user to connect to Internet
						                    showAlertDialog(ctx, "No Internet Connection",
						                            "You don't have internet connection.", false);
						                }

									

								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						});

				article_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								try {

									if (listApp != null) {

										customDialog(article_holder.look_image,
												"Articles",
												article_pojo.getSlug(), article_pojo.getTitle(), article_pojo.getIdd(), pro_user_id);
									}

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Share on Other User Profile")
											.setAction(
													"Shared Article by "
															+ pro_user_name)
											.setLabel("Other User Profile")
											.build());
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});

				article_holder.like_image.setTag(article_pojo);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return article_result;

		case 2:
			View review_result = convertView;
			final ReviewHolder review_holder;


			if (review_result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				review_result = inf.inflate(R.layout.review_feed_item, parent,
						false);
				review_holder = new ReviewHolder();
				review_holder.user_name = (TextView) review_result
						.findViewById(R.id.user_name);
				review_holder.lookbook_view = (TextView) review_result
						.findViewById(R.id.user_view);
				review_holder.img_view = (ImageView) review_result
						.findViewById(R.id.img_view);
				review_holder.lookbook_like = (TextView) review_result
						.findViewById(R.id.txt_like_count);
				review_holder.user_image = (ImageView) review_result
						.findViewById(R.id.img_profile);
				review_holder.rel_rated_box = (RelativeLayout) review_result
						.findViewById(R.id.rel_rated_box);
				review_holder.like_image = (ImageView) review_result
						.findViewById(R.id.img_like);
				review_holder.rel_store_rate = (RelativeLayout) review_result
						.findViewById(R.id.rel_rate);
				review_holder.share_image = (ImageView) review_result
						.findViewById(R.id.img_share);
				review_holder.review = (TextView) review_result
						.findViewById(R.id.txt_review);
				review_holder.store_name = (TextView) review_result
						.findViewById(R.id.txt_store_name);
				review_holder.store_address = (TextView) review_result
						.findViewById(R.id.txt_store_location);
				review_holder.store_rate = (TextView) review_result
						.findViewById(R.id.txt_rate);
				review_holder.txt_rated = (TextView) review_result
						.findViewById(R.id.txt_rated);

				review_holder.user_name.setTypeface(typeface_bold);
				review_holder.lookbook_view.setTypeface(typeface_bold);
				review_holder.review.setTypeface(typeface_regular);
				review_holder.store_name.setTypeface(typeface_bold);
				review_holder.store_address.setTypeface(typeface_regular);
				review_holder.store_rate.setTypeface(typeface_bold);
				review_holder.txt_rated.setTypeface(typeface_bold);
				review_holder.lookbook_like.setTypeface(typeface_bold);

				review_result.setTag(review_holder);
				review_holder.user_image.setVisibility(View.GONE);
				review_holder.like_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								ImageView img = (ImageView) v;
								UserFeedPojo ppp = (UserFeedPojo) img.getTag();
								String string_like;
								String post_mode = "StoreReviews";
								String article_iddd = ppp.getIdd();
								String get_likes = ppp.getLikes();
								if (ppp.getIs_liked().equals("true")) {

									review_holder.like_image
											.setImageResource(R.drawable.home_like_inactive);
									long count = Long.parseLong(ppp.getLikes()) - 1;
									review_holder.lookbook_like.setText(""
											+ count);
									ppp.setIs_liked("false");
									ppp.setLikes("" + count);

									string_like = "0";
									article_like(article_iddd, string_like,
											post_mode);

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Unlike Review on Other User Profile")
											.setAction(
													"Unliked Review by "
															+ pro_user_name)
											.setLabel("Other User Profile")
											.build());

								} else {

									review_holder.like_image
											.setImageResource(R.drawable.home_like_active);
									long count = Long.parseLong(ppp.getLikes()) + 1;
									review_holder.lookbook_like.setText(""
											+ count);
									ppp.setIs_liked("true");
									ppp.setLikes("" + count);
									string_like = "1";
									article_like(article_iddd, string_like,
											post_mode);

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Like Review on Other User Profile")
											.setAction(
													"Liked Review by "
															+ pro_user_name)
											.setLabel("Other User Profile")
											.build());
								}

							}
						});

			} else {
				review_holder = (ReviewHolder) review_result.getTag();
			}

			final UserFeedPojo review_pojo = mList.get(position);
			review_holder.like_image.setTag(review_pojo);
			try {

				if (review_pojo.getIs_liked().equals("true")) {

					review_holder.like_image
							.setImageResource(R.drawable.home_like_active);
				} else {

					review_holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				}

				review_holder.user_name.setText("Reviewed");

				review_holder.lookbook_like.setText(review_pojo.getLikes());
				
				int hits_count = Integer.parseInt(review_pojo.getHits_text());
				if (hits_count >= 100 ) {
					review_holder.lookbook_view.setVisibility(View.VISIBLE);
					review_holder.lookbook_view.setText(review_pojo.getHits());
					review_holder.img_view.setVisibility(View.VISIBLE);
				} else {
					review_holder.lookbook_view.setVisibility(View.GONE);
					review_holder.img_view.setVisibility(View.GONE);
				}
			

				String review_data = String.valueOf(Html.fromHtml(review_pojo
						.getReview()));

				if (review_data.length() > 200) {
					String upToNCharacters = review_data.substring(0,
							Math.min(review_data.length(), 200));
					String more = "<font color='#A29F9F'>...(more)</font>";
					review_holder.review.setText(Html.fromHtml(upToNCharacters
							+ more));
				} else {
					review_holder.review.setText(Html.fromHtml(review_pojo
							.getReview()));
				}

				review_holder.store_name.setText(review_pojo.getStore_name());
				review_holder.store_address.setText(review_pojo
						.getStore_location());

				review_holder.rel_rated_box
						.setBackgroundResource(R.drawable.rating_box_0);
				GradientDrawable drawable = (GradientDrawable) review_holder.rel_rated_box
						.getBackground();

				if (review_pojo.getRated().equals("0")) {
					review_holder.store_rate.setText("-");
					drawable.setColor(Color.parseColor(review_pojo
							.getRated_color()));
				} else {
					review_holder.store_rate.setText(review_pojo.getRated());
					drawable.setColor(Color.parseColor(review_pojo
							.getRated_color()));
				}

				review_holder.review.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						
						pos = position;
						fullReviewDialog();

						Tracker t = ((UILApplication) ctx
								.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Review view on Other User Profile")
								.setAction(
										"Clicked on Review by " + pro_user_name)
								.setLabel("Other User Profile").build());

					}
				});

				review_holder.rel_store_rate
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								try {

									
									isInternetPresent = cd.isConnectingToInternet();
						             // check for Internet status
						                if (isInternetPresent) {
						                    // Internet Connection is Present
						                	Intent in = new Intent(ctx,
													StoreActivity.class);
											in.putExtra("store_id",
													review_pojo.getStore_id());
											ctx.startActivity(in);

											Tracker t = ((UILApplication) ctx
													.getApplicationContext())
													.getTracker(TrackerName.APP_TRACKER);
											// Build and send an Event.

											t.send(new HitBuilders.EventBuilder()
													.setCategory(
															"Store view on Other User Profile")
													.setAction(
															"Clicked on Store View by "
																	+ pro_user_name)
													.setLabel("Other User Profile")
													.build());
						                } else {
						                    // Internet connection is not present
						                    // Ask user to connect to Internet
						                    showAlertDialog(ctx, "No Internet Connection",
						                            "You don't have internet connection.", false);
						                }
						                
						                
									

								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						});

				review_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								try {
									String share_text = review_pojo
											.getStore_name()
											+ ", "
											+ review_pojo.getStore_location()
											+ " by "
											+ review_pojo.getUser_name();
									
									if (listApp != null) {

										customDialog(review_holder.user_image,
												"StoreReviews",
												review_pojo.getSlug(), share_text, review_pojo.getIdd(), pro_user_id);
									}

									Tracker t = ((UILApplication) ctx
											.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Share on Other User Profile")
											.setAction(
													"Shared Review by "
															+ pro_user_name)
											.setLabel("Other User Profile")
											.build());

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return review_result;

		default:

		}
		return null;

	}

	public static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 250);
					displayedImages.add(imageUri);
				}
			}
		}

	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		/*
		 * if (getCount() != 0) return getCount();
		 */

		return 3;

	}

	public static class LookbookHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		ImageView look_image,img_view;
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_img_count;
		TextView image_count;

	}

	public static class ArticleHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		ImageView look_image,img_view;
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_img_count;
		TextView image_count;

	}

	public static class ReviewHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like, txt_rated;
		ImageView user_image,img_view;
		ImageView like_image, share_image;
		RelativeLayout rel_store_rate, rel_rated_box;
		TextView review, store_name, store_address, store_rate;

	}

	@Override
	public int getItemViewType(int position) {

		final UserFeedPojo pojo = mList.get(position);

		if (pojo.getMode().equals("Lookbooks")) {
			mode_position = 0;
		} else if (pojo.getMode().equals("Articles")) {
			mode_position = 1;
		} else if (pojo.getMode().equals("StoreReviews")) {
			mode_position = 2;
		}

		return mode_position;
	}

	public void article_like(final String article_id, final String string_like,
			final String post_mode) {
		/*
		 * Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/" +
		 * article_id + ".json");
		 */
		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
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

		client.post(ctx.getString(R.string.base_url) + "Common/like/"
				+ article_id + ".json", params, new AsyncHttpResponseHandler() {

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

				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {
				try {
					BufferedReader bufferedReader1 = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
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

	/*
	 * Add class and methods for sharing image and link on other apps
	 */
	// Returns the URI path to the Bitmap displayed in specified ImageView
	public Uri getLocalBitmapUri(ImageView imageView) {
		// Extract Bitmap from ImageView drawable
		Drawable drawable = imageView.getDrawable();
		Bitmap bmp = null;
		if (drawable instanceof BitmapDrawable) {
			bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		} else {
			return null;
		}
		// Store image to default external storage directory
		Uri bmpUri = null;
		try {
			File filePath = Environment.getExternalStorageDirectory();
			File dir = new File(filePath.getAbsolutePath() + "/Zakoopi/Share");
			dir.mkdirs();

			File outFile = new File(dir, "Sharepic"
					+ System.currentTimeMillis() + ".jpg");

			FileOutputStream out = new FileOutputStream(outFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}

	/*
	 * Add class and methods for sharing image and link on other apps
	 */
	// Returns the URI path to the Bitmap displayed in specified ImageView

	private java.util.List<ResolveInfo> showAllShareApp() {
		java.util.List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
		intent.setType("text/plain");
		PackageManager pManager = ctx.getPackageManager();
		mApps = pManager.queryIntentActivities(intent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}

	private void share(ResolveInfo appInfo, final ImageView img,
			final String mode1, final String slug1, final String share_text1, final String mode_id, final String user_id) {

if (mode1.equals("StoreReviews")) {
			
			SharedPreferences city_loc_pref = ctx.getSharedPreferences(
					"client_location", 0);
			String city_slug = city_loc_pref
					.getString("city_slug", "delhi-ncr");
			String share_url = "https://play.google.com/store/apps/details?id=com.zakoopi&referrer=utm_source%3DzakoopiApp%26utm_medium%3DDirectShare";

			String share_all_text = "Make this - Review of " + "" + share_text1
					+ " | Zakoopi " + System.getProperty("line.separator");

			share_all_text = share_all_text + "http://www.zakoopi.com/"
					+ city_slug + "/" + slug1
					+ System.getProperty("line.separator")
					+ System.getProperty("line.separator");

			share_all_text = share_all_text
					+ "Get the Zakoopi app from Google Play - " + share_url;
			
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, share_all_text);

			if (appInfo != null) {
				sendIntent.setComponent(new ComponentName(
						appInfo.activityInfo.packageName,
						appInfo.activityInfo.name));
			}
			sendIntent.setType("text/plain");
			ctx.startActivity(sendIntent);
			
			if (appInfo.activityInfo.packageName
					.contains("com.facebook.katana")) {
								
				share_play(mode1, mode_id,"fb",user_id);
				
			} else if (appInfo.activityInfo.packageName
					.contains("com.google.android.apps.plus")) {
				share_play(mode1, mode_id,"gp",user_id);
				
			} else if (appInfo.activityInfo.packageName
					.contains("com.whatsapp")) {
				share_play(mode1, mode_id,"wp",user_id);
				
			} else if (appInfo.activityInfo.packageName
					.contains("com.twitter.android")) {
				share_play(mode1, mode_id,"tw",user_id);
				
			} else {
				
				Uri bmpUri = getLocalBitmapUri(img);
				if (bmpUri != null) {
					Intent sendIntent1 = new Intent();
					sendIntent1.setAction(Intent.ACTION_SEND);
					sendIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
					sendIntent1.putExtra(Intent.EXTRA_TEXT, share_all_text);
					if (appInfo != null) {
						sendIntent1.setComponent(new ComponentName(
								appInfo.activityInfo.packageName,
								appInfo.activityInfo.name));
					}
					sendIntent1.setType("image/png");
					ctx.startActivity(sendIntent1);
				} else {
					
				}
			}
		} else {
			String share_url = "https://play.google.com/store/apps/details?id=com.zakoopi&referrer=utm_source%3DzakoopiApp%26utm_medium%3DDirectShare";

			String share_all_text = share_text1 + " | Zakoopi"
					+ System.getProperty("line.separator");
			share_all_text = share_all_text + "http://www.zakoopi.com/"
					+ mode1 + "/" + slug1
					+ System.getProperty("line.separator")
					+ System.getProperty("line.separator");
			;

			share_all_text = share_all_text
					+ "Get the Zakoopi app from Google Play - " + share_url;
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			
			sendIntent.putExtra(Intent.EXTRA_TEXT, share_all_text);
			if (appInfo != null) {
				sendIntent.setComponent(new ComponentName(
						appInfo.activityInfo.packageName,
						appInfo.activityInfo.name));
			}
			sendIntent.setType("text/plain");
			ctx.startActivity(sendIntent);
			
			if (appInfo.activityInfo.packageName
					.contains("com.facebook.katana")) {
				share_play(mode1, mode_id,"fb",user_id);
				
			} else if (appInfo.activityInfo.packageName
					.contains("com.google.android.apps.plus")) {
				share_play(mode1, mode_id,"gp",user_id);
				
			} else if (appInfo.activityInfo.packageName
					.contains("com.whatsapp")) {
				share_play(mode1, mode_id,"wp",user_id);
				
			} else if (appInfo.activityInfo.packageName
					.contains("com.twitter.android")) {
				share_play(mode1, mode_id,"tw",user_id);
				
			} else {
				
				Uri bmpUri = getLocalBitmapUri(img);
				if (bmpUri != null) {
					Intent sendIntent1 = new Intent();
					sendIntent1.setAction(Intent.ACTION_SEND);
					sendIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
					sendIntent1.putExtra(Intent.EXTRA_TEXT, share_all_text);
					if (appInfo != null) {
						sendIntent1.setComponent(new ComponentName(
								appInfo.activityInfo.packageName,
								appInfo.activityInfo.name));
					}
					sendIntent1.setType("image/png");
					ctx.startActivity(sendIntent1);
				} else {
					
				}
			}
		}

	}

	class MyAdapter extends BaseAdapter {

		PackageManager pm;

		public MyAdapter() {
			pm = ctx.getPackageManager();
		}

		@Override
		public int getCount() {
			return listApp.size();
		}

		@Override
		public Object getItem(int position) {
			return listApp.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ctx).inflate(R.layout.row,
						parent, false);
				holder.ivLogo = (ImageView) convertView
						.findViewById(R.id.imageView1);
				holder.tvAppName = (TextView) convertView
						.findViewById(R.id.textView1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ResolveInfo appInfo = listApp.get(position);
			holder.ivLogo.setImageDrawable(appInfo.loadIcon(pm));
			holder.tvAppName.setText("Share on " + appInfo.loadLabel(pm));

			return convertView;
		}
	}

	static class ViewHolder {
		ImageView ivLogo;
		TextView tvAppName;

	}

	public void customDialog(final ImageView img, final String mode,
			final String slug, final String share_text, final String mode_id, final String user_id) {

		final Dialog dd = new Dialog(ctx, R.style.DialogSlideAnim);
		// dd = new Dialog(ctx, R.style.DialogSlideAnim);

		int height1 = MainActivity.height / 2;

		dd.setContentView(R.layout.custom_gridview);
		Window window = dd.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, height1);
		window.setGravity(Gravity.BOTTOM);
		// final GridView ggg = (GridView) dd.findViewById(R.id.gridView1);
		final ListView ggg = (ListView) dd.findViewById(R.id.listView1);
		dd.show();

		ggg.setAdapter(new MyAdapter());

		ggg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				share(listApp.get(position), img, mode, slug, share_text, mode_id, user_id);
				dd.cancel();
			}
		});

	}

	public void fullReviewDialog() {

		final Dialog dd = new Dialog(ctx,
				android.R.style.Theme_Translucent_NoTitleBar);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.full_review);
		dd.show();
		final TextView txt_user_name = (TextView) dd
				.findViewById(R.id.user_name);
		final TextView lookbook_view = (TextView) dd
				.findViewById(R.id.user_view);
		final TextView lookbook_like = (TextView) dd
				.findViewById(R.id.txt_like_count);
		final ImageView img_user_image = (ImageView) dd
				.findViewById(R.id.img_profile);
		final RelativeLayout rel_rated_box = (RelativeLayout) dd
				.findViewById(R.id.rel_rated_box);
		final ImageView like_image = (ImageView) dd.findViewById(R.id.img_like);
		final RelativeLayout rel_store_rate = (RelativeLayout) dd
				.findViewById(R.id.rel_rate);
		final ImageView share_image = (ImageView) dd
				.findViewById(R.id.img_share);
		final TextView review = (TextView) dd.findViewById(R.id.txt_review);
		final TextView txt_store_name = (TextView) dd
				.findViewById(R.id.txt_store_name);
		final TextView txt_store_address = (TextView) dd
				.findViewById(R.id.txt_store_location);
		final TextView txt_store_rate = (TextView) dd
				.findViewById(R.id.txt_rate);
		final TextView txt_rated = (TextView) dd.findViewById(R.id.txt_rated);
		final RelativeLayout rel1 = (RelativeLayout) dd.findViewById(R.id.rel1);
		final RelativeLayout rel_back = (RelativeLayout) dd
				.findViewById(R.id.rel_back);
		final TextView txt_back = (TextView) dd.findViewById(R.id.txt);

		txt_user_name.setTypeface(typeface_bold);
		lookbook_view.setTypeface(typeface_bold);
		review.setTypeface(typeface_regular);
		txt_store_name.setTypeface(typeface_bold);
		txt_store_address.setTypeface(typeface_regular);
		txt_store_rate.setTypeface(typeface_bold);
		txt_rated.setTypeface(typeface_bold);
		lookbook_like.setTypeface(typeface_bold);
		txt_back.setTypeface(typeface_semibold);
		txt_back.setText("Review");

		final UserFeedPojo pojo = mList.get(pos);
		rel_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dd.cancel();

			}
		});

		share_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String share_text = pojo.getStore_name() + ", "
						+ pojo.getStore_location() + " by "
						+ pojo.getUser_name();
				
				if (listApp != null) {

					customDialog(img_user_image, "StoreReviews", pojo.getSlug(), share_text, pojo.getIdd(), pro_user_id);
					// dd.cancel();
				}

				Tracker t = ((UILApplication) ctx.getApplicationContext())
						.getTracker(TrackerName.APP_TRACKER);
				// Build and send an Event.

				t.send(new HitBuilders.EventBuilder()
						.setCategory("Share on Login User Profile Full Review")
						.setAction("Shared Review by " + pro_user_name)
						.setLabel("Login User Profile Full Review").build());
			}
		});

		like_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					ImageView img = (ImageView) v;
					// POJO pp = (POJO) img.getTag();

					String string_like;
					String post_mode = "StoreReviews";
					String article_iddd = pojo.getIdd();

					if (pojo.getIs_liked().equals("true")) {

						like_image
								.setImageResource(R.drawable.home_like_inactive);
						long count = Long.parseLong(lookbook_like.getText()
								.toString()) - 1;
						lookbook_like.setText("" + count);

						string_like = "0";
						pojo.setIs_liked("false");
						pojo.setLikes("" + count);
						article_like(article_iddd, string_like, post_mode);

						Tracker t = ((UILApplication) ctx
								.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Unlike Review on Login User Profile Full Review")
								.setAction("Unliked Review by " + pro_user_name)
								.setLabel("Login User Profile Full Review")
								.build());

					} else {

						like_image
								.setImageResource(R.drawable.home_like_active);
						long count = Long.parseLong(lookbook_like.getText()
								.toString()) + 1;
						lookbook_like.setText("" + count);

						string_like = "1";
						pojo.setIs_liked("true");
						pojo.setLikes("" + count);
						article_like(article_iddd, string_like, post_mode);

						Tracker t = ((UILApplication) ctx
								.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Like Review on Login User Profile Full Review")
								.setAction("Liked Review by " + pro_user_name)
								.setLabel("Login User Profile Full Review")
								.build());

					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				// dd.cancel();
				notifyDataSetChanged();
			}
		});

		try {
			txt_user_name.setText(pojo.getUser_name());
			lookbook_like.setText(pojo.getLikes());
			lookbook_view.setText(pojo.getHits());
			review.setText(Html.fromHtml(pojo.getReview()));

			txt_store_address.setText(pojo.getStore_location());
			txt_store_name.setText(pojo.getStore_name());
			rel_rated_box.setBackgroundResource(R.drawable.rating_box_0);
			GradientDrawable drawable = (GradientDrawable) rel_rated_box
					.getBackground();

			if (pojo.getRated().equals("0")) {
				txt_store_rate.setText("-");
				drawable.setColor(Color.parseColor(pojo.getRated_color()));
			} else {
				txt_store_rate.setText(pojo.getRated());
				drawable.setColor(Color.parseColor(pojo.getRated_color()));
			}

			Picasso.with(ctx).load(pojo.getUser_img())
			.placeholder(R.drawable.profile_img_3).resize(50, 50)
			.into(img_user_image);
			
			/*ImageLoader.getInstance().displayImage(pojo.getUser_img(),
					img_user_image);*/

			if (pojo.getIs_liked().equals("true")) {

				like_image.setImageResource(R.drawable.home_like_active);
			} else {

				like_image.setImageResource(R.drawable.home_like_inactive);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 
	 * LookBook Dialog
	 * 
	 * @param ctxx
	 * @param lookbook_id
	 * @param user_name
	 * @param user_pic_url
	 * @param article_title
	 * @param view_count
	 * @param like_count
	 * @param comment_count
	 * @param isLiked
	 * @param pos
	 */

	public void LookbookDialog(Context ctxx, String lookbook_id,
			String user_name, String user_pic_url, String article_title,
			String view_count, String like_count, String comment_count,
			String isLiked, int pos) {
		
		int displayWidth;

		dd = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.lookbook_view_main);
		dd.show();

		progressBar1 = (ProgressBar) dd.findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.VISIBLE);

		client = ClientHttp.getInstance(ctx);

		

		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		Point size = new Point();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getSize(size);
		displayWidth = size.x;

		rel_back = (RelativeLayout) dd.findViewById(R.id.rel_back);
		txt_head = (TextView) dd.findViewById(R.id.txt);
		lin_main = (LinearLayout) dd.findViewById(R.id.lookbook_lin);
		pro_user_pic = (CircleImageView) dd.findViewById(R.id.img_profile_pic);
		side_user_name = (TextView) dd.findViewById(R.id.txt_user_name);
		side_about = (TextView) dd.findViewById(R.id.txt_about);
		side_con_ac = (TextView) dd.findViewById(R.id.txt_con_ac);
		side_edt_profile = (TextView) dd.findViewById(R.id.txt_edit);
		side_noti_settings = (TextView) dd.findViewById(R.id.txt_noti);
		side_rate = (TextView) dd.findViewById(R.id.txt_rate);
		side_sign_out = (TextView) dd.findViewById(R.id.txt_sign_out);
		side_sug_store = (TextView) dd.findViewById(R.id.txt_sug_store);
		rel_edt_profile = (RelativeLayout) dd
				.findViewById(R.id.rel_edt_profile);
		txt_head.setTypeface(typeface_semibold);
		txt_head.setText("Lookbook");

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dd.dismiss();

			}
		});

		if (user_name != null) {
			lin_main.addView(setHeader(ctx, lookbook_id, user_name,
					user_pic_url, article_title, view_count, like_count,
					comment_count, isLiked, pos));
			lookbookView(lookbook_id, comment_count);
		}

	}

	private View setHeader(final Context ctxx, final String lookbook_id,
			final String user_name, final String user_pic_url,
			final String article_title, final String view_count,
			final String like_count, final String comment_count,
			final String isLiked, final int pos) {

		st = isLiked;
		View view_header = LayoutInflater.from(ctxx).inflate(
				R.layout.article_view_header, null);

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

			if (st.equals("true")) {

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
			txt_title.setText(article_title);
			txt_comment_count.setText(comment_count);
			txt_like_count.setText(like_count);
			txt_view_count.setText(view_count);

			Picasso.with(ctxx).load(user_pic_url)
					.placeholder(R.drawable.profile_img_3).resize(50, 50).into(user_pic);

			// ImageLoader.getInstance().displayImage(user_pic_url, user_pic,
			// options);

		} catch (Exception e) {
			// TODO: handle exception
		}

		img_like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				UserFeedPojo pojo1 = mList.get(pos);
				if (st.equals("true")) {

					txt_like_count.setText(String.valueOf(Integer
							.parseInt(like_count) - 1));
					img_like.setImageResource(R.drawable.notif_like);
					st = "false";
					pojo1.setIs_liked("false");

					pojo1.setLikes(String.valueOf(Integer.parseInt(pojo1
							.getLikes()) - 1));

					notifyDataSetChanged();
					
					article_like(lookbook_id, "0", "Lookbooks");

				} else {

					txt_like_count.setText(String.valueOf(Integer
							.parseInt(like_count) + 1));
					img_like.setImageResource(R.drawable.home_like_active);
					st = "true";
					pojo1.setIs_liked("true");

					pojo1.setLikes(String.valueOf(Integer.parseInt(pojo1
							.getLikes()) + 1));
					notifyDataSetChanged();
					
					article_like(lookbook_id, "1", "Lookbooks");
				}

			}
		});

		return view_header;
	}

	@SuppressLint("ResourceAsColor")
	private View addLookbook(String description, String image,
			ArrayList<LookbookTagStore> store_del, String medium_img_w,
			String medium_img_h) {

		View add_lookbook = LayoutInflater.from(ctx).inflate(
				R.layout.lookbook_view, null);

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

			Picasso.with(ctx).load(image).placeholder(R.color.maroon)
					.into(img_lookbook);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {

			desc.setTypeface(typeface_regular);
			txt_store_tag.setTypeface(typeface_semibold);

			// ImageLoader.getInstance().displayImage(image, img_lookbook,
			// options);
			desc.setText(description);
			// String split[] = store_tag.split(",");

			for (int i = 0; i < store_del.size(); i++) {
				final TextView t = new TextView(ctx);
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
						try {

							isInternetPresent = cd.isConnectingToInternet();
							// check for Internet status
							if (isInternetPresent) {
								Intent in = new Intent(ctx, StoreActivity.class);
								in.putExtra("store_id", store_iddddd);
								ctx.startActivity(in);
							} else {
								// Internet connection is not present
								// Ask user to connect to Internet
								showAlertDialog(ctx, "No Internet Connection",
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

		view_comment = LayoutInflater.from(ctx).inflate(
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
			// ImageLoader.getInstance().displayImage(user_img_pic, user_pic,
			// options);
			Picasso.with(ctx).load(user_img_pic)
					.placeholder(R.drawable.profile_img_3).resize(50, 50).into(user_pic);

			rel_user.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {

						isInternetPresent = cd.isConnectingToInternet();
						// check for Internet status
						if (isInternetPresent) {
							if (!user_id_com.equals(pro_user_id)) {
								Variables.myact = "noway";
								Intent i = new Intent(ctx, OtherUserProfile.class);
								i.putExtra("user_id", user_id_com);
								ctx.startActivity(i);

							} else {
								dd.cancel();
								MainActivity.pager.setCurrentItem(2, true);
							}
						} else {
							// Internet connection is not present
							// Ask user to connect to Internet
							showAlertDialog(ctx, "No Internet Connection",
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

	private View addComment(final String comment_count) {

		View add_comment = LayoutInflater.from(ctx).inflate(
				R.layout.article_footer, null);

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
				
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					
				
				try {

					int view_count = lin_main.getChildCount();

					if (!edt_comment.getText().toString().equals("")) {
						lin_main.addView(viewComment(pro_user_f_name + " "
								+ pro_user_l_name, pro_user_pic_url,
								edt_comment.getText().toString(),
								String.valueOf(view_count - 1), pro_user_id));

						addOwnComment(mode_id,
								edt_comment.getText().toString(), "LBK",
								comment_count);

						edt_comment.setText("");
						edt_comment.setHint("Add a comment....");

					} else {

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				}else {
					//Toast.makeText(ctx, "Please connect to internet", Toast.LENGTH_SHORT).show();
				}
			}

		});

		return add_comment;
	}

	public void lookbookView(String lookbook_id, final String comment_count) {

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		
		client.get(ctx.getString(R.string.base_url) + "lookbooks/view/"
				+ lookbook_id + ".json", new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {
						text2 = text2 + text1;

					}

					show_lookbook(text2, comment_count);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void show_lookbook(String data, String comment_count) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		RecentLookbookMain main = gson.fromJson(jsonReader,
				RecentLookbookMain.class);

		LookbookRecent lookbookRecent = main.getLookbook();

		lookbook_card_list = lookbookRecent.getRecentCards();

		lookbook_comment_list = lookbookRecent.getLookbookcomments();
		comment_counttt = comment_count;
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
			/*
			 * colorlist = new ArrayList<Integer>(); colorlist.clear();
			 */

		//	Log.e("lookbook_card_list_1", lookbook_card_list_1.size() + "");

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

				//	Log.e("looksize", lookbookTagStores.size() + "");
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
				lin_main.addView(addComment(comment_counttt));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	public void addOwnComment(String look_art_id, String msg, String mode,
			final String comment_count) {

		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
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
		client.post(ctx.getString(R.string.base_url)
				+ "Common/commentOnLbkAndArt/" + user_id + ".json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						try {
							/*
							 * Log.e("URLLL", this.getString(R.string.base_url)
							 * + "Common/commentOnLbkAndArt/" + article_id +
							 * ".json"+"---"+string_like);
							 */
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

	/**
	 * 
	 * Article Dialog
	 * 
	 * @param ctx
	 * @param article_id
	 * @param user_name
	 * @param url_img
	 * @param article_title
	 * @param view_count
	 * @param like_count
	 * @param description
	 * @param is_new
	 * @param comment_count
	 * @param liked
	 * @param pos
	 */

	public void ArticleDialog(Context ctx, String article_id, String user_name,
			String url_img, String article_title, String view_count,
			String like_count, String description, String is_new,
			String comment_count, String liked, int pos) {

		int displayWidth;
		// ProgressBar progressBar1;

		dd = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.lookbook_view_main);
		dd.show();

		progressBar1 = (ProgressBar) dd.findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.VISIBLE);

		client = ClientHttp.getInstance(ctx);

		typeface_semibold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		Point size = new Point();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getSize(size);
		displayWidth = size.x;

		rel_back = (RelativeLayout) dd.findViewById(R.id.rel_back);
		txt_head = (TextView) dd.findViewById(R.id.txt);
		lin_main = (LinearLayout) dd.findViewById(R.id.lookbook_lin);
		pro_user_pic = (CircleImageView) dd.findViewById(R.id.img_profile_pic);
		side_user_name = (TextView) dd.findViewById(R.id.txt_user_name);
		side_about = (TextView) dd.findViewById(R.id.txt_about);
		side_con_ac = (TextView) dd.findViewById(R.id.txt_con_ac);
		side_edt_profile = (TextView) dd.findViewById(R.id.txt_edit);
		side_noti_settings = (TextView) dd.findViewById(R.id.txt_noti);
		side_rate = (TextView) dd.findViewById(R.id.txt_rate);
		side_sign_out = (TextView) dd.findViewById(R.id.txt_sign_out);
		side_sug_store = (TextView) dd.findViewById(R.id.txt_sug_store);
		rel_edt_profile = (RelativeLayout) dd
				.findViewById(R.id.rel_edt_profile);
		txt_head.setTypeface(typeface_semibold);
		txt_head.setText("Article");

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * if (Variables.user_id_list.size() > 1) {
				 * Variables.user_id_list.remove(Variables.user_id_list.size() -
				 * 1); Log.e("LOOOG", "" + Variables.user_id_list); }
				 */
				dd.dismiss();

			}
		});

		try {

			if (user_name != null) {
				lin_main.addView(setHeaderArticle(ctx, article_id, user_name,
						url_img, article_title, view_count, like_count,
						comment_count, liked, pos));
			}

			if (is_new.equals("false")) {
				lin_main.addView(addOldImage());
				article_view(article_id, comment_count, is_new, description);
			} else {
				article_view(article_id, comment_count, is_new, description);

			}

		} catch (Exception e) {

		}

	}

	private View setHeaderArticle(final Context ctxx, final String article_id,
			final String user_name, final String user_pic_url,
			final String article_title, final String view_count,
			final String like_count, final String comment_count,
			final String isLiked, final int pos) {
		statu = isLiked;
		View view_header = LayoutInflater.from(ctxx).inflate(
				R.layout.article_view_header, null);

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

			if (statu.equals("true")) {

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

			Picasso.with(ctxx).load(user_pic_url)
					.placeholder(R.drawable.profile_img_3).resize(50, 50).into(user_pic);

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

				UserFeedPojo pojo2 = mList.get(pos);
				if (statu.equals("true")) {

					txt_like_count.setText(String.valueOf(Integer
							.parseInt(like_count) - 1));
					img_like.setImageResource(R.drawable.notif_like);
					statu = "false";
					pojo2.setIs_liked("false");
					pojo2.setLikes(String.valueOf(Integer.parseInt(pojo2
							.getLikes()) - 1));

				//	Log.e("POJO2_IF_IS", pojo2.getIs_liked().toString());
				//	Log.e("POJO2_IF_LIKES", pojo2.getLikes().toString());

					article_like(article_id, "0", "Articles");
					notifyDataSetChanged();

				} else {

					txt_like_count.setText(String.valueOf(Integer
							.parseInt(like_count) + 1));
					img_like.setImageResource(R.drawable.home_like_active);
					statu = "true";
					pojo2.setIs_liked("true");
					pojo2.setLikes(String.valueOf(Integer.parseInt(pojo2
							.getLikes()) + 1));
					notifyDataSetChanged();
					//Log.e("POJO2_ELSE_IS", pojo2.getIs_liked().toString());
				//	Log.e("POJO2_ELSE_LIKES", pojo2.getLikes().toString());
					article_like(article_id, "1", "Articles");

				}

			}
		});

		return view_header;
	}

	private View setArticleData(String description) {

		View view_article = LayoutInflater.from(ctx).inflate(
				R.layout.article_show, null);

		WebView webview = (WebView) view_article
				.findViewById(R.id.article_webview);
		try {

			webview.setWebViewClient(new myWebClient());
			webview.getSettings().setJavaScriptEnabled(true);

			String css = description
					+ "<style type=\"text/css\"> img {width:100% !important; height: auto !important;}"
					+ "@font-face {font-family: SourceSans;src: url(\"file:///android_asset/fonts/SourceSansPro-Regular.ttf\")}"
					+ "* {font-family: SourceSans}" + "</style>";
			webview.loadDataWithBaseURL(ctx.getString(R.string.base_url), css,
					"text/html", "UTF-8", "");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return view_article;
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

	private View viewCommentArticle(String user_f_name, String user_l_name,
			String user_img_pic, String comment, final String user_id_com) {

		View view_comment = LayoutInflater.from(ctx).inflate(
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

			Picasso.with(ctx).load(user_img_pic)
					.placeholder(R.drawable.profile_img_3).resize(50, 50).into(user_pic);

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
								Variables.myact = "noway";
								Intent i = new Intent(ctx, OtherUserProfile.class);
								i.putExtra("user_id", user_id_com);
								ctx.startActivity(i);

							} else {
								dd.cancel();
								MainActivity.pager.setCurrentItem(2, true);
							}
						} else {
							// Internet connection is not present
							// Ask user to connect to Internet
							showAlertDialog(ctx, "No Internet Connection",
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

	private View addCommentArticle(final String article_id,
			final String comment_count) {

		View add_comment = LayoutInflater.from(ctx).inflate(
				R.layout.article_footer, null);

		final EditText edt_comment = (EditText) add_comment
				.findViewById(R.id.edt_comment);

		RelativeLayout rel_submit = (RelativeLayout) add_comment
				.findViewById(R.id.rel_submit);
		edt_comment.setTypeface(typeface_regular);

		rel_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					
				try {

					int view_count = lin_main.getChildCount();

					if (!edt_comment.getText().toString().equals("")) {
						lin_main.addView(viewCommentArticle(pro_user_f_name + " "
								+ pro_user_l_name, pro_user_pic_url,
								edt_comment.getText().toString(),
								String.valueOf(view_count - 1), pro_user_id));
						edt_comment.setText("");
						edt_comment.setHint("Add a comment....");
						addOwnCommentArticle(article_id, edt_comment.getText()
								.toString(), "ART", comment_count);

						/*
						 * Tracker t = ((UILApplication) getApplication())
						 * .getTracker(TrackerName.APP_TRACKER); // Build and
						 * send an Event.
						 * 
						 * t.send(new HitBuilders.EventBuilder()
						 * .setCategory("submit comment on Article View")
						 * .setAction("submit comment by " + pro_user_name)
						 * .setLabel("Article View").build());
						 */

					} else {

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}else {
				//Toast.makeText(ctx, "Please connect to internet", Toast.LENGTH_SHORT).show();
			}
			}
		});
		return add_comment;
	}

	private View addOldImage() {

		View old_image = LayoutInflater.from(ctx).inflate(
				R.layout.article_old_image, null);
		pager = (ViewPager) old_image.findViewById(R.id.old_image);

		return old_image;
	}

	public void article_view(final String article_id,
			final String comment_count, final String is_new,
			final String description) {

		/*
		 * Log.e("URL", getString(R.string.base_url) + "articles/view/" +
		 * article_id + ".json");
		 */
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		client.get(ctx.getString(R.string.base_url) + "articles/view/"
				+ article_id + ".json", new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {
						text2 = text2 + text1;
					}
					show_article(text2, comment_count, is_new, description,
							article_id);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {

			}
		});
	}

	/**
	 * 
	 * @show_lookbook show
	 */
	@SuppressWarnings("unchecked")
	public void show_article(String show, String comment_count, String is_new,
			String description, String article_id) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		ArticleMain main = gson.fromJson(jsonReader, ArticleMain.class);
		Article article_sub = main.getArticle();
		url_list = article_sub.getArticle();
		comment_list = article_sub.getArticle_comments();
		comment_counttt = comment_count;
		art_id = article_id;
		PagerAdapter pagerAdapter = new ViewPagerAdapter(ctx, url_list);

		try {

			if (is_new.equals("false")) {
				pager.setAdapter(pagerAdapter);

				lin_main.addView(setArticleData(description));
				progressBar1.setVisibility(View.GONE);
				// new MyApp().execute(comment_list);
			} else {
				lin_main.addView(setArticleData(description));
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

					lin_main.addView(viewCommentArticle(user_first_name,
							user_last_name, user_img, comment, user_id_com));

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			try {

				lin_main.addView(addCommentArticle(art_id, comment_counttt));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void addOwnCommentArticle(String look_art_id, String msg,
			String mode, final String comment_count) {

		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
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
		client.post(ctx.getString(R.string.base_url)
				+ "Common/commentOnLbkAndArt/" + user_id + ".json", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						try {
							/*
							 * Log.e("URLLL", this.getString(R.string.base_url)
							 * + "Common/commentOnLbkAndArt/" + article_id +
							 * ".json"+"---"+string_like);
							 */
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
	
	
	 @SuppressWarnings("deprecation")
		public void showAlertDialog(final Context context, String title, String message, Boolean status) {
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
	   	 
	        // Setting Dialog Title
	        alertDialog.setTitle("No Internet Connection!");
	 
	        // Setting Dialog Message
	        alertDialog.setMessage("Enable Internet Connection.");
	 
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	Intent intent = new Intent(Settings.ACTION_SETTINGS);
	            	context.startActivity(intent);
	            	 dialog.cancel();
	            }
	        });
	 
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	
	            dialog.cancel();
	            }
	        });
		 
		        // Showing Alert Message
		        alertDialog.show();
		    }
	 
	 public void share_play(String model, String model_id, String share_mode,
				String user_id) {

			String SHARE_PLAY_REST_URL = ctx.getString(R.string.base_url)
					+ "Common/add_share.json";
			long time = System.currentTimeMillis();

			client.getHttpClient().getParams()
					.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			RequestParams params = new RequestParams();
			params.setForceMultipartEntityContentType(true);
			params.put("model", model);
			params.put("timestamp", time);
			params.put("device", "Android");
			params.put("key", model_id);
			params.put("mode", share_mode);
			params.put("user_id", user_id);
			
			client.setTimeout(DEFAULT_TIMEOUT);
			client.post(SHARE_PLAY_REST_URL, params,
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
								//all_play(text);
								// Log.e("SHARE", text);
							} catch (Exception e) {

							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2,
								Throwable arg3) {

							// Log.e("FAIL", "FAIL" + arg0);
						}
					});
		}

}
