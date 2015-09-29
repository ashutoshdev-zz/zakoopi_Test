package com.zakoopi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.os.Environment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.StoreActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.activity.ArticleView;
import com.zakoopi.activity.LookbookView1;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.activity.OtherUserProfile;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.POJO;
import com.zakoopi.helper.Variables;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class RecentAdapter1 extends BaseAdapter {

	public static List<POJO> mList;
	public static int pos = 0;
	Context ctx;
	//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password,pro_user_id;
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;
	SharedPreferences pro_user_pref;
	String article_color;
	public static ArrayList<Integer> colorlist;
	private List<ResolveInfo> listApp;
	int mode_position;
	String like;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	int displayWidth;
	private SharedPreferences pref_location;

	private String city_name;

	public RecentAdapter1(Context context, List<POJO> list,
			ArrayList<Integer> color) {
		ctx = context;
		mList = list;
		this.colorlist = color;
      try{
		pref_location = ctx.getSharedPreferences("location", 1);
		city_name = pref_location.getString("city", "123");
		client = ClientHttp.getInstance(ctx);
		
		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_id = pro_user_pref.getString("user_id", "adajfh");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");

		Point size = new Point();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getSize(size);
		displayWidth = size.x;

		/**
		 * Typeface
		 */

		typeface_semibold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		
      }catch(Exception e){
    	  e.printStackTrace();
      }


		/**
		 * User Login SharedPreferences
		 */
		

		// PicassoTools.clearCache(Picasso.with(context));

	}

	public void addItems(List<POJO> newItems) {
		if (null == newItems || newItems.size() <= 0) {
			return;
		}

		if (null == mList) {
			mList = new ArrayList<POJO>();
		}
        try{
		mList.addAll(newItems);
		notifyDataSetChanged();
        }catch(Exception e){
        	e.printStackTrace();
        }
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

     try{
		switch (type) {

		case 0:
			View result = convertView;
			final LookbookHolder holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.placeholder_photo)
					.showImageOnFail(R.drawable.placeholder_photo)
					.cacheInMemory(true).delayBeforeLoading(100)
					.cacheOnDisk(true).build();

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
				holder.lookbook_like = (TextView) result
						.findViewById(R.id.txt_like_count);
				holder.user_image = (ImageView) result
						.findViewById(R.id.img_profile);
				holder.title = (TextView) result.findViewById(R.id.txt_title);
				holder.look_image = (ImageView) result
						.findViewById(R.id.img_flash);
				holder.like_image = (ImageView) result
						.findViewById(R.id.img_like);
				holder.rel1 = (RelativeLayout) result.findViewById(R.id.rel1);
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

				holder.like_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						ImageView img = (ImageView) v;
						POJO ppp = (POJO) img.getTag();
						String string_like;
						String post_mode = "Lookbooks";
						String article_iddd = ppp.getIdd();
						String get_likes = ppp.getLikes();

						if (ppp.getis_liked().equals("true")) {
							holder.like_image
									.setImageResource(R.drawable.home_like_inactive);
							long count = Long.parseLong(ppp.getLikes()) - 1;
							holder.lookbook_like.setText("" + count);
							ppp.setis_liked("false");
							ppp.setLikes("" + count);
							string_like = "0";
							article_like(article_iddd, string_like, post_mode);
							
							Tracker t = ((UILApplication) ctx.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory("Unlike Lookbook on Featured Feed")
									.setAction("Unliked Lookbook by " + pro_user_name)
									.setLabel("Featured Feed").build());

						} else {
							holder.like_image
									.setImageResource(R.drawable.home_like_active);
							long count = Long.parseLong(ppp.getLikes()) + 1;
							holder.lookbook_like.setText("" + count);
							ppp.setis_liked("true");
							ppp.setLikes("" + count);
							string_like = "1";
							article_like(article_iddd, string_like, post_mode);
							
							Tracker t = ((UILApplication) ctx.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory("Like Lookbook on Featured Feed")
									.setAction("Liked Lookbook by " + pro_user_name)
									.setLabel("Featured Feed").build());
						}

					}
				});

			} else {
				holder = (LookbookHolder) result.getTag();
			}

			final POJO pojo = mList.get(position);
			holder.like_image.setTag(pojo);

			try {
				final int width1 = displayWidth;
				final int height1 = Integer.parseInt(pojo.getImg_height());

				final int set_height = (int) (((width1 * 1.0) / Integer
						.parseInt(pojo.getImg_width())) * height1);

				holder.look_image.getLayoutParams().height = set_height;
				holder.look_image.getLayoutParams().width = width1;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			try {

				if (pojo.getis_liked().equals("true")) {

					holder.like_image
							.setImageResource(R.drawable.home_like_active);
				} else {

					holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				}

				if (Integer.parseInt(pojo.getImage_count()) >= 3) {

					/*
					 * ImageLoader.getInstance().displayImage(pojo.getLookimg(),
					 * holder.look_image, options, animateFirstListener);
					 */

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

					/*
					 * ImageLoader.getInstance().displayImage(pojo.getLookimg(),
					 * holder.look_image, options, animateFirstListener);
					 */

					Picasso.with(ctx).load(pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(holder.look_image);

					Picasso.with(ctx).load(pojo.getImg1())
					.placeholder(colorlist.get(position))
					.into(holder.img1);

					
					holder.img2.setVisibility(View.VISIBLE);
					holder.img1.setVisibility(View.GONE);
					holder.last_text.setVisibility(View.GONE);
					
				} else {

					/*
					 * ImageLoader.getInstance().displayImage(pojo.getLookimg(),
					 * holder.look_image, options, animateFirstListener);
					 */
					Picasso.with(ctx).load(pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(holder.look_image);

					holder.img1.setVisibility(View.GONE);
					holder.img2.setVisibility(View.GONE);
					holder.last_text.setVisibility(View.GONE);
					

				}

				holder.user_name.setText(pojo.getUsername());

				Picasso.with(ctx).load(pojo.getUserimg())
				.placeholder(R.drawable.profile_img_3)
				.into(holder.user_image);
				
			/*	ImageLoader.getInstance().displayImage(pojo.getUserimg(),
						holder.user_image, options);*/

				holder.lookbook_view.setText(pojo.getHits());
				holder.title.setText(pojo.getTitle());
				holder.lookbook_like.setText(pojo.getLikes());
				

				holder.rel1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (pojo.getUserid().equals(pro_user_id)) {
							MainActivity.pager.setCurrentItem(2, true);
						} else {
							Intent user = new Intent(ctx, OtherUserProfile.class);
							user.putExtra("user_id", pojo.getUserid());
							ctx.startActivity(user);
						}
						
						
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Profile view on Featured feed")
								.setAction(
										"Clicked on Profile by "
												+ pro_user_name)
								.setLabel("Featured Feed")
								.build());

					}
				});
				
				
				holder.look_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								pos= position;
								Variables.myact = "home_recent";
								Intent in = new Intent(ctx, LookbookView1.class);
								in.putExtra("lookbook_id", pojo.getIdd());
								in.putExtra("username", pojo.getUsername());
								in.putExtra("userpicurl", pojo.getUserimg());
								in.putExtra("title", pojo.getTitle());
								in.putExtra("hits", pojo.getHits());
								in.putExtra("likes", pojo.getLikes());
								in.putExtra("comments", pojo.getComment_count());
								in.putExtra("liked", pojo.getis_liked());
								
								ctx.startActivity(in);
								
								Tracker t = ((UILApplication) ctx.getApplicationContext())
										.getTracker(TrackerName.APP_TRACKER);
								// Build and send an Event.

								t.send(new HitBuilders.EventBuilder()
										.setCategory(
												"Lookbook view on Featured Feed")
										.setAction(
												"Clicked on Lookbook Cover by "
														+ pro_user_name)
										.setLabel("Featured Feed")
										.build());

							}
						});

				holder.title.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						pos= position;
						Variables.myact = "home_recent";
						Intent in = new Intent(ctx, LookbookView1.class);
						in.putExtra("lookbook_id", pojo.getIdd());
						in.putExtra("username", pojo.getUsername());
						in.putExtra("userpicurl", pojo.getUserimg());
						in.putExtra("title", pojo.getTitle());
						in.putExtra("hits", pojo.getHits());
						in.putExtra("likes", pojo.getLikes());
						in.putExtra("comments", pojo.getComment_count());
						in.putExtra("liked", pojo.getis_liked());
						
						ctx.startActivity(in);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Lookbook view on Featured Feed")
								.setAction(
										"Clicked on Lookbook title by "
												+ pro_user_name)
								.setLabel("Featured Feed")
								.build());

					}
				});
				//
				holder.rel_img_count
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								pos= position;
								Variables.myact = "home_recent";
								Intent in = new Intent(ctx, LookbookView1.class);
								in.putExtra("lookbook_id", pojo.getIdd());
								in.putExtra("username", pojo.getUsername());
								in.putExtra("userpicurl", pojo.getUserimg());
								in.putExtra("title", pojo.getTitle());
								in.putExtra("hits", pojo.getHits());
								in.putExtra("likes", pojo.getLikes());
								in.putExtra("comments", pojo.getComment_count());
								in.putExtra("liked", pojo.getis_liked());
								
								ctx.startActivity(in);
								
								Tracker t = ((UILApplication) ctx.getApplicationContext())
										.getTracker(TrackerName.APP_TRACKER);
								// Build and send an Event.

								t.send(new HitBuilders.EventBuilder()
										.setCategory(
												"Lookbook view on Featured Feed")
										.setAction(
												"Clicked on Lookbook Image Count by "
														+ pro_user_name)
										.setLabel("Featured Feed")
										.build());

							}
						});

				holder.share_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (listApp != null) {

							customDialog(holder.look_image, "lookbooks",
									pojo.getSlug());
						}
						
						Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
							    TrackerName.APP_TRACKER);
							// Build and send an Event.
						
							t.send(new HitBuilders.EventBuilder()
							    .setCategory("Share on  Featured Feed")
							    .setAction("Shared Lookbook by "+pro_user_name)
							    .setLabel("Featured Feed")
							    .build());

					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		case 1:
			View article_result = convertView;
			final ArticleHolder article_holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.placeholder_photo)
					.showImageOnFail(R.drawable.placeholder_photo)
					.cacheInMemory(true).delayBeforeLoading(100)
					.cacheOnDisk(true).build();

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
				article_holder.rel1 = (RelativeLayout) article_result
						.findViewById(R.id.rel1);
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

				article_holder.like_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								ImageView img = (ImageView) v;
								POJO ppp = (POJO) img.getTag();
								String string_like;
								String post_mode = "Articles";
								String article_iddd = ppp.getIdd();
								String get_likes = ppp.getLikes();
								if (ppp.getis_liked().equals("true")) {

									article_holder.like_image
											.setImageResource(R.drawable.home_like_inactive);
									long count = Long.parseLong(ppp.getLikes()) - 1;
									article_holder.lookbook_like.setText(""
											+ count);
									ppp.setis_liked("false");
									ppp.setLikes("" + count);
									string_like = "0";
									article_like(article_iddd, string_like,
											post_mode);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory("Unlike Article on Featured Feed")
											.setAction("Unliked Article by " + pro_user_name)
											.setLabel("Featured Feed").build());

								} else {

									article_holder.like_image
											.setImageResource(R.drawable.home_like_active);
									long count = Long.parseLong(ppp.getLikes()) + 1;
									article_holder.lookbook_like.setText(""
											+ count);
									ppp.setis_liked("true");
									ppp.setLikes("" + count);
									string_like = "1";
									article_like(article_iddd, string_like,
											post_mode);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory("Like Article on Featured Feed")
											.setAction("Liked Article by " + pro_user_name)
											.setLabel("Featured Feed").build());
								}

							}
						});

			} else {
				article_holder = (ArticleHolder) article_result.getTag();
			}

			final POJO article_pojo = mList.get(position);

			try {
				final int width1 = displayWidth;
				final int height1 = Integer.parseInt(article_pojo
						.getImg_height());

				final int set_height = (int) (((width1 * 1.0) / Integer
						.parseInt(article_pojo.getImg_width())) * height1);

				article_holder.look_image.getLayoutParams().height = set_height;
				article_holder.look_image.getLayoutParams().width = width1;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			try {
				if (Integer.parseInt(article_pojo.getImage_count()) >= 3) {
					/*
					 * ImageLoader.getInstance().displayImage(
					 * article_pojo.getLookimg(), article_holder.look_image,
					 * options, animateFirstListener);
					 */
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

					/*
					 * ImageLoader.getInstance().displayImage(
					 * article_pojo.getLookimg(), article_holder.look_image,
					 * options, animateFirstListener);
					 */
					Picasso.with(ctx).load(article_pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(article_holder.look_image);
				

					Picasso.with(ctx).load(article_pojo.getImg2())
					.placeholder(colorlist.get(position))
					.into(article_holder.img2);
					

					
					article_holder.img2.setVisibility(View.VISIBLE);
					article_holder.img1.setVisibility(View.GONE);
					article_holder.last_text.setVisibility(View.GONE);
				} else {

					/*
					 * ImageLoader.getInstance().displayImage(
					 * article_pojo.getLookimg(), article_holder.look_image,
					 * options, animateFirstListener);
					 */
					Picasso.with(ctx).load(article_pojo.getLookimg())
							.placeholder(colorlist.get(position))
							.into(article_holder.look_image);

					article_holder.img1.setVisibility(View.GONE);
					article_holder.img2.setVisibility(View.GONE);

					article_holder.last_text.setVisibility(View.GONE);
				}

				if (article_pojo.getis_liked().equals("true")) {

					article_holder.like_image
							.setImageResource(R.drawable.home_like_active);
				} else {

					article_holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				}

				article_holder.user_name.setText(article_pojo.getUsername());

				Picasso.with(ctx).load(article_pojo.getUserimg())
				.placeholder(R.drawable.profile_img_3)
				.into(article_holder.user_image);
				
				/*ImageLoader.getInstance().displayImage(
						article_pojo.getUserimg(), article_holder.user_image);*/

				article_holder.lookbook_view.setText(article_pojo.getHits());
				article_holder.title.setText(article_pojo.getTitle());
				article_holder.lookbook_like.setText(article_pojo.getLikes());
				article_holder.rel1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent user = new Intent(ctx, OtherUserProfile.class);
						user.putExtra("user_id", article_pojo.getUserid());
						ctx.startActivity(user);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Profile view on Featured feed")
								.setAction(
										"Clicked on Profile by "
												+ pro_user_name)
								.setLabel("Featured Feed")
								.build());

					}
				});
				article_holder.title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pos= position;
						Variables.myarticle = "home_recent";
						Intent in = new Intent(ctx, ArticleView.class);
						in.putExtra("article_id", article_pojo.getIdd());
						in.putExtra("username", article_pojo.getUsername());
						in.putExtra("userpicurl", article_pojo.getUserimg());
						in.putExtra("title", article_pojo.getTitle());
						in.putExtra("hits", article_pojo.getHits());
						in.putExtra("likes", article_pojo.getLikes());
						in.putExtra("description",
								article_pojo.getDescription());
						in.putExtra("is_new", article_pojo.getIsnew());
						in.putExtra("comments", article_pojo.getComment_count());
						in.putExtra("liked", article_pojo.getis_liked());
						ctx.startActivity(in);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Article view on Featured Feed")
								.setAction(
										"Clicked on Article Title by "
												+ pro_user_name)
								.setLabel("Featured Feed")
								.build());

					}
				});

				article_holder.look_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								pos= position;
								Variables.myarticle = "home_recent";
								Intent in = new Intent(ctx, ArticleView.class);
								in.putExtra("article_id", article_pojo.getIdd());
								in.putExtra("username",
										article_pojo.getUsername());
								in.putExtra("userpicurl",
										article_pojo.getUserimg());
								in.putExtra("title", article_pojo.getTitle());
								in.putExtra("hits", article_pojo.getHits());
								in.putExtra("likes", article_pojo.getLikes());
								in.putExtra("description",
										article_pojo.getDescription());
								in.putExtra("is_new", article_pojo.getIsnew());
								in.putExtra("comments",
										article_pojo.getComment_count());
								in.putExtra("liked", article_pojo.getis_liked());
								ctx.startActivity(in);
								
								Tracker t = ((UILApplication) ctx.getApplicationContext())
										.getTracker(TrackerName.APP_TRACKER);
								// Build and send an Event.

								t.send(new HitBuilders.EventBuilder()
										.setCategory(
												"Article view on Featured Feed")
										.setAction(
												"Clicked on Article Cover by "
														+ pro_user_name)
										.setLabel("Featured Feed")
										.build());

							}
						});

				article_holder.rel_img_count
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								pos= position;
								Variables.myarticle = "home_recent";
								Intent in = new Intent(ctx, ArticleView.class);
								in.putExtra("article_id", article_pojo.getIdd());
								in.putExtra("username",
										article_pojo.getUsername());
								in.putExtra("userpicurl",
										article_pojo.getUserimg());
								in.putExtra("title", article_pojo.getTitle());
								in.putExtra("hits", article_pojo.getHits());
								in.putExtra("likes", article_pojo.getLikes());
								in.putExtra("description",
										article_pojo.getDescription());
								in.putExtra("is_new", article_pojo.getIsnew());
								in.putExtra("comments",
										article_pojo.getComment_count());
								in.putExtra("liked", article_pojo.getis_liked());
								ctx.startActivity(in);
								
								Tracker t = ((UILApplication) ctx.getApplicationContext())
										.getTracker(TrackerName.APP_TRACKER);
								// Build and send an Event.

								t.send(new HitBuilders.EventBuilder()
										.setCategory(
												"Article view on Featured Feed")
										.setAction(
												"Clicked on Article Image Count by "
														+ pro_user_name)
										.setLabel("Featured Feed")
										.build());

							}
						});

				article_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (listApp != null) {

									customDialog(article_holder.look_image,
											"articles", article_pojo.getSlug());
								}
								
								Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
									    TrackerName.APP_TRACKER);
									// Build and send an Event.
								
									t.send(new HitBuilders.EventBuilder()
									    .setCategory("Share on Featured Feed")
									    .setAction("Shared Article by "+pro_user_name)
									    .setLabel("Featured Feed")
									    .build());
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

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.placeholder_photo)
					.showImageOnFail(R.drawable.placeholder_photo)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();

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
				review_holder.rel1 = (RelativeLayout) review_result
						.findViewById(R.id.rel1);

				review_holder.user_name.setTypeface(typeface_bold);
				review_holder.lookbook_view.setTypeface(typeface_bold);
				review_holder.review.setTypeface(typeface_regular);
				review_holder.store_name.setTypeface(typeface_bold);
				review_holder.store_address.setTypeface(typeface_regular);
				review_holder.store_rate.setTypeface(typeface_bold);
				review_holder.txt_rated.setTypeface(typeface_bold);
				review_holder.lookbook_like.setTypeface(typeface_bold);

				review_result.setTag(review_holder);

				review_holder.like_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								ImageView img = (ImageView) v;
								POJO ppp = (POJO) img.getTag();
								String string_like;
								String post_mode = "StoreReviews";
								String article_iddd = ppp.getIdd();
								String get_likes = ppp.getLikes();
								if (ppp.getis_liked().equals("true")) {

									review_holder.like_image
											.setImageResource(R.drawable.home_like_inactive);
									long count = Long.parseLong(ppp.getLikes()) - 1;
									review_holder.lookbook_like.setText(""
											+ count);
									ppp.setis_liked("false");
									ppp.setLikes("" + count);

									string_like = "0";
									article_like(article_iddd, string_like,
											post_mode);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory("Unlike Review on Featured Feed")
											.setAction("Unliked Review by " + pro_user_name)
											.setLabel("Featured Feed").build());

								} else {

									review_holder.like_image
											.setImageResource(R.drawable.home_like_active);
									long count = Long.parseLong(ppp.getLikes()) + 1;
									review_holder.lookbook_like.setText(""
											+ count);
									ppp.setis_liked("true");
									ppp.setLikes("" + count);
									string_like = "1";
									article_like(article_iddd, string_like,
											post_mode);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory("Like Review on Featured Feed")
											.setAction("Liked Review by " + pro_user_name)
											.setLabel("Featured Feed").build());
								}

							}
						});

			} else {
				review_holder = (ReviewHolder) review_result.getTag();
			}

			final POJO review_pojo = mList.get(position);
			review_holder.like_image.setTag(review_pojo);
			try {

				if (review_pojo.getis_liked().equals("true")) {

					review_holder.like_image
							.setImageResource(R.drawable.home_like_active);
				} else {

					review_holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				}

				review_holder.user_name.setText(review_pojo.getUsername());
				Picasso.with(ctx).load(review_pojo.getUserimg())
				.placeholder(R.drawable.profile_img_3)
				.into(review_holder.user_image);
				
				/*ImageLoader.getInstance().displayImage(
						review_pojo.getUserimg(), review_holder.user_image,
						options);*/
				review_holder.lookbook_like.setText(review_pojo.getLikes());
				review_holder.lookbook_view.setText(review_pojo.getHits());

				String review_data = String.valueOf(Html.fromHtml(review_pojo
						.getReview()));
				
				if (review_data.length() > 400) {
					String upToNCharacters = review_data.substring(0,
							Math.min(review_data.length(), 400));
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
				// review_holder.store_rate.setText(review_pojo.getStore_rate());

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

				review_holder.rel1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent user = new Intent(ctx, OtherUserProfile.class);
						user.putExtra("user_id", review_pojo.getUserid());
						ctx.startActivity(user);
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Profile view on Featured feed")
								.setAction(
										"Clicked on Profile by "
												+ pro_user_name)
								.setLabel("Featured Feed")
								.build());

					}
				});

				review_holder.review.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						pos =position;
						fullReviewDialog();
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Review view on featured Feed")
								.setAction(
										"Clicked on Review by "
												+ pro_user_name)
								.setLabel("featured Feed")
								.build());

					}
				});

				review_holder.rel_store_rate
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								Intent in = new Intent(ctx, StoreActivity.class);
								in.putExtra("store_id", review_pojo.getIdd());
								ctx.startActivity(in);
								
								Tracker t = ((UILApplication) ctx.getApplicationContext())
										.getTracker(TrackerName.APP_TRACKER);
								// Build and send an Event.

								t.send(new HitBuilders.EventBuilder()
										.setCategory(
												"Store view on Featured Feed")
										.setAction(
												"Clicked on Store View by "
														+ pro_user_name)
										.setLabel("Featured Feed")
										.build());

							}
						});

				review_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (listApp != null) {

									customDialog(review_holder.user_image,
											"StoreReviews",
											review_pojo.getSlug());
								}
								
								Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
									    TrackerName.APP_TRACKER);
									// Build and send an Event.
								
									t.send(new HitBuilders.EventBuilder()
									    .setCategory("Share on Featured Feed")
									    .setAction("Shared Review by "+pro_user_name)
									    .setLabel("Featured Feed")
									    .build());
							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return review_result;

		case 3:
			View team_result = convertView;
			final TeamHolder team_holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.placeholder_photo)
					.showImageOnFail(R.drawable.placeholder_photo)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();

			if (team_result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				team_result = inf.inflate(R.layout.team_feed_item, null, false);
				team_holder = new TeamHolder();
				team_holder.user_name = (TextView) team_result
						.findViewById(R.id.user_name);
				team_holder.lookbook_view = (TextView) team_result
						.findViewById(R.id.user_view);
				team_holder.lookbook_like = (TextView) team_result
						.findViewById(R.id.txt_like_count);
				team_holder.title = (TextView) team_result
						.findViewById(R.id.txt_title);
				team_holder.look_image = (DynamicImageView) team_result
						.findViewById(R.id.img_flash);

				team_holder.like_image = (ImageView) team_result
						.findViewById(R.id.img_like);

				team_holder.share_image = (ImageView) team_result
						.findViewById(R.id.img_share);

				team_result.setTag(team_holder);

				team_holder.like_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								ImageView img = (ImageView) v;
								POJO ppp = (POJO) img.getTag();
								String string_like;
								String post_mode = "Teams";
								String article_iddd = ppp.getIdd();
								String get_likes = ppp.getLikes();
								if (ppp.getis_liked().equals("true")) {

									team_holder.like_image
											.setImageResource(R.drawable.home_like_inactive);
									long count = Long.parseLong(ppp.getLikes()) - 1;
									team_holder.lookbook_like.setText(""
											+ count);
									ppp.setis_liked("false");
									ppp.setLikes("" + count);
									string_like = "0";
									article_like(article_iddd, string_like,
											post_mode);

								} else {

									team_holder.like_image
											.setImageResource(R.drawable.home_like_active);
									long count = Long.parseLong(ppp.getLikes()) + 1;
									team_holder.lookbook_like.setText(""
											+ count);
									ppp.setis_liked("true");
									ppp.setLikes("" + count);
									string_like = "1";
									article_like(article_iddd, string_like,
											post_mode);
								}

							}
						});

			} else {
				team_holder = (TeamHolder) team_result.getTag();
			}

			final POJO team_pojo = mList.get(position);
			team_holder.like_image.setTag(team_pojo);
			try {

				if (team_pojo.getis_liked().equals("true")) {

					team_holder.like_image
							.setImageResource(R.drawable.home_like_active);
				} else {

					team_holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				}
				team_holder.user_name.setText(team_pojo.getUsername());
				team_holder.lookbook_like.setText(team_pojo.getLikes());
				team_holder.lookbook_view.setText(team_pojo.getHits());
				team_holder.title.setText(team_pojo.getTitle());

				/*ImageLoader.getInstance().displayImage(team_pojo.getLookimg(),
						team_holder.look_image, options, animateFirstListener);*/

				/*
				 * team_holder.share_image .setOnClickListener(new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(View v) {
				 * 
				 * if (listApp != null) {
				 * 
				 * customDialog(team_holder.look_image); } } });
				 */

			} catch (Exception e) {
				e.printStackTrace();
			}

			return team_result;

		default:

		}
     }catch(Exception e){
    	 e.printStackTrace();
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
		if (getCount() != 0)
			return getCount();

		return 1;

	}

	public static class LookbookHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		ImageView look_image;
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_img_count, rel1;
		TextView image_count;

	}

	public static class ArticleHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		ImageView look_image;
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_img_count, rel1;
		TextView image_count;

	}

	public static class ReviewHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		ImageView like_image, share_image;
		RelativeLayout rel_store_rate, rel_rated_box, rel1;
		TextView review, store_name, store_address, store_rate, txt_rated;

	}

	public static class TeamHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		TextView title;
		DynamicImageView look_image;
		ImageView like_image, share_image;

	}

	@Override
	public int getItemViewType(int position) {

		final POJO pojo = mList.get(position);

		if (pojo.getMode().equals("Lookbooks")) {
			mode_position = 0;
		} else if (pojo.getMode().equals("Articles")) {
			mode_position = 1;
		} else if (pojo.getMode().equals("StoreReviews")) {
			mode_position = 2;
		} else if (pojo.getMode().equals("Teams")) {
			mode_position = 3;
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
			final String mode1, final String slug1) {

		if (mode1.equals("StoreReviews")) {
			SharedPreferences city_loc_pref = ctx.getSharedPreferences(
					"client_location", 0);
			String city_slug = city_loc_pref
					.getString("city_slug", "delhi-ncr");

			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.zakoopi.com/"
					+ city_slug + "/" + slug1);

			if (appInfo != null) {
				sendIntent.setComponent(new ComponentName(
						appInfo.activityInfo.packageName,
						appInfo.activityInfo.name));
			}
			sendIntent.setType("text/plain");
			ctx.startActivity(sendIntent);

		} else {
			if (appInfo.activityInfo.packageName
					.contains("com.facebook.katana")
					|| appInfo.activityInfo.packageName
							.contains("com.google.android.apps.plus")
					|| appInfo.activityInfo.packageName
							.contains("com.pinterest")) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						"http://www.zakoopi.com/" + mode1 + "/" + slug1);

				if (appInfo != null) {
					sendIntent.setComponent(new ComponentName(
							appInfo.activityInfo.packageName,
							appInfo.activityInfo.name));
				}
				sendIntent.setType("text/plain");
				ctx.startActivity(sendIntent);

			} else {
				Uri bmpUri = getLocalBitmapUri(img);

				if (bmpUri != null) {

					Intent sendIntent = new Intent();
					sendIntent.setAction(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_TEXT,
							"http://www.zakoopi.com/" + mode1 + "/" + slug1);
					sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
					if (appInfo != null) {
						sendIntent.setComponent(new ComponentName(
								appInfo.activityInfo.packageName,
								appInfo.activityInfo.name));
					}
					sendIntent.setType("image/png");
					ctx.startActivity(sendIntent);
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
			final String slug) {

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
				share(listApp.get(position), img, mode, slug);
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
		//final POJO pojo = PopularAdapter1.mList.get(PopularAdapter1.pos);
		final POJO pojo = mList.get(RecentAdapter1.pos);
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

				if (listApp != null) {

					customDialog(img_user_image, "StoreReviews", pojo.getSlug());
					// dd.cancel();
				}
				
				Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
					    TrackerName.APP_TRACKER);
					// Build and send an Event.
				
					t.send(new HitBuilders.EventBuilder()
					    .setCategory("Share on Featured Feed Full Review")
					    .setAction("Shared Review by "+pro_user_name)
					    .setLabel("Featured Feed Full Review")
					    .build());
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

					if (pojo.getis_liked().equals("true")) {

						like_image
								.setImageResource(R.drawable.home_like_inactive);
						long count = Long.parseLong(lookbook_like.getText()
								.toString()) - 1;
						lookbook_like.setText("" + count);

						string_like = "0";
						pojo.setis_liked("false");
						pojo.setLikes("" + count);
						article_like(article_iddd, string_like, post_mode);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Unlike Review on Featured Feed Full Review")
								.setAction("Unliked Review by " + pro_user_name)
								.setLabel("Featured Feed Full Review").build());

					} else {

						like_image
								.setImageResource(R.drawable.home_like_active);
						long count = Long.parseLong(lookbook_like.getText()
								.toString()) + 1;
						lookbook_like.setText("" + count);

						string_like = "1";
						pojo.setis_liked("true");
						pojo.setLikes("" + count);
						article_like(article_iddd, string_like, post_mode);

						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Like Review on Featured Feed Full Review")
								.setAction("Liked Review by " + pro_user_name)
								.setLabel("Featured Feed Full Review").build());

					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				// dd.cancel();
				notifyDataSetChanged();
			}
		});

		try {
			txt_user_name.setText(pojo.getUsername());
			lookbook_like.setText(pojo.getLikes());
			lookbook_view.setText(pojo.getHits());
			review.setText(Html.fromHtml(pojo.getReview()));

			txt_store_address.setText(pojo.getStore_location());
			txt_store_name.setText(pojo.getStore_name());
			rel_rated_box.setBackgroundResource(R.drawable.rating_box_0);
			GradientDrawable drawable = (GradientDrawable) rel_rated_box
					.getBackground();

			if (pojo.getStore_rate().equals("0")) {
				txt_store_rate.setText("-");
				drawable.setColor(Color.parseColor(pojo.getRated_color()));
			} else {
				txt_store_rate.setText(pojo.getStore_rate());
				drawable.setColor(Color.parseColor(pojo.getRated_color()));
			}

			ImageLoader.getInstance().displayImage(pojo.getUserimg(),
					img_user_image, options);

			if (pojo.getis_liked().equals("true")) {

				like_image.setImageResource(R.drawable.home_like_active);
			} else {

				like_image.setImageResource(R.drawable.home_like_inactive);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
