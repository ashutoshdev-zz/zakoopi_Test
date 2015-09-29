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
import com.mycam.ImageDetail;
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
import com.zakoopi.activity.ProfileDrafts;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.Variables;
import com.zakoopi.userfeed.model.UserFeedPojo;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class ProfileDraftAdapter extends BaseAdapter {

	public static List<UserFeedPojo> mList;
	public static int pos = 0;
	Context ctx;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
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
	
Boolean isInternetPresent = false;
    
    // Connection detector class
    ConnectionDetector cd;

	public ProfileDraftAdapter(Context context, List<UserFeedPojo> list,
			ArrayList<Integer> color) {
		ctx = context;
		mList = list;
		this.colorlist = color;

		try {
			cd = new ConnectionDetector(ctx);
			pref_location = ctx.getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "123");
		} catch (Exception e) {
			// TODO: handle exception
		}
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
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");

		
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

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).delayBeforeLoading(100)
					.cacheOnDisk(true).build();

			if (result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				result = inf.inflate(R.layout.lookbook_feed_item,
						parent, false);
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

				holder.share_image = (ImageView) result
						.findViewById(R.id.img_share);
				holder.img1 = (ImageView) result.findViewById(R.id.post_img1);
				holder.img2 = (ImageView) result.findViewById(R.id.post_img2);
				holder.last_text = (RelativeLayout) result
						.findViewById(R.id.rel_post_img_count);
				holder.image_count = (TextView) result
						.findViewById(R.id.txt_count);
				holder.img_view = (ImageView) result
						.findViewById(R.id.img_view);
				holder.rel_img_count = (RelativeLayout) result
						.findViewById(R.id.rel_113);
				 holder.img_edt = (ImageView) result
						.findViewById(R.id.img_edt);
				holder.user_name.setTypeface(typeface_bold);
				holder.lookbook_view.setTypeface(typeface_bold);
				holder.title.setTypeface(typeface_semibold);
				holder.image_count.setTypeface(typeface_bold);
				holder.lookbook_like.setTypeface(typeface_bold);

				result.setTag(holder);
				holder.lookbook_view.setVisibility(View.GONE);
				holder.img_view.setVisibility(View.GONE);
				holder.img_edt.setVisibility(View.VISIBLE);
				holder.user_image.setVisibility(View.GONE);
				holder.like_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {
							
						
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
							
							Tracker t = ((UILApplication) ctx.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory("Unlike Lookbook on Login User Profile")
									.setAction("Unliked Lookbook by " + pro_user_name)
									.setLabel("Login User Profile").build());

						} else {
							
							holder.like_image
									.setImageResource(R.drawable.home_like_active);
							long count = Long.parseLong(ppp.getLikes()) + 1;
							holder.lookbook_like.setText("" + count);
							ppp.setIs_liked("true");
							ppp.setLikes("" + count);
							string_like = "1";
							article_like(article_iddd, string_like, post_mode);
							
							Tracker t = ((UILApplication) ctx.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
							.setCategory("Like Lookbook on Login User Profile")
							.setAction("Liked Lookbook by " + pro_user_name)
							.setLabel("Login User Profile").build());
						}
						} catch (Exception e) {
							// TODO: handle exception
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
				} else {

					holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				}

				try {
					
				
				if (Integer.parseInt(pojo.getImage_count()) >= 3) {

					/*ImageLoader.getInstance().displayImage(pojo.getLookimg(),
							holder.look_image, options, animateFirstListener);*/
					
					Picasso.with(ctx).load(pojo.getLookimg())
					.placeholder(colorlist.get(position))
					.into(holder.look_image);

					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(pojo.getImg1(),
							holder.img1, options, animateFirstListener);

					ImageLoader.getInstance().displayImage(pojo.getImg2(),
							holder.img2, options, animateFirstListener);

					if (Integer.parseInt(pojo.getImage_count()) == 3) {

						holder.last_text.setVisibility(View.GONE);
					} else {
						holder.last_text.setVisibility(View.VISIBLE);
						holder.image_count.setText("+" + pojo.getImage_count());
					}

				}

				else if (Integer.parseInt(pojo.getImage_count()) == 2) {

					/*ImageLoader.getInstance().displayImage(pojo.getLookimg(),
							holder.look_image, options, animateFirstListener);*/
					
					Picasso.with(ctx).load(pojo.getLookimg())
					.placeholder(colorlist.get(position))
					.into(holder.look_image);
					
					ImageLoader.getInstance().displayImage(pojo.getImg1(),
							holder.img2, options, animateFirstListener);
					holder.img2.setVisibility(View.VISIBLE);
					holder.img1.setVisibility(View.GONE);
					holder.last_text.setVisibility(View.GONE);

				} else {

					/*ImageLoader.getInstance().displayImage(pojo.getLookimg(),
							holder.look_image, options, animateFirstListener);*/
					
					Picasso.with(ctx).load(pojo.getLookimg())
					.placeholder(colorlist.get(position))
					.into(holder.look_image);
					
					holder.img2.setVisibility(View.GONE);
					holder.img1.setVisibility(View.GONE);
					holder.last_text.setVisibility(View.GONE);

				}

				holder.user_name.setText("Draft");

				holder.lookbook_view.setText(pojo.getHits());
				holder.title.setText(pojo.getTitle());
				holder.lookbook_like.setText(pojo.getLikes());
				} catch (Exception e) {
					// TODO: handle exception
				}
				/*holder.look_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								try {
                                  pos = position;
                                  Variables.myact = "myact";
									Intent in = new Intent(ctx,
											LookbookView1.class);
									in.putExtra("lookbook_id", pojo.getIdd());
									in.putExtra("username",
											OtherUserProfile.user_name);
									in.putExtra("userpicurl",
											OtherUserProfile.url_img);
									in.putExtra("title", pojo.getTitle());
									in.putExtra("hits", pojo.getHits());
									in.putExtra("likes", pojo.getLikes());
									in.putExtra("comments", pojo.getComment_count());
									
									in.putExtra("liked", pojo.getIs_liked());
									ctx.startActivity(in);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Lookbook view on Login User Profile")
											.setAction(
													"Clicked on Lookbook Cover by "
															+ pro_user_name)
											.setLabel("Login User Profile")
											.build());

								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						});*/
				
				
				holder.img_edt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					ProfileDrafts.idd = pojo.getIdd();
						Intent mainIntent = new Intent(ctx,
								ImageDetail.class);
						mainIntent.putExtra("ImageEffects", "Profile");
						Variables.draftVal = "draft";
						ctx.startActivity(mainIntent);

					}
				});
				

				/*holder.title.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						try {
							 pos = position;
							 Variables.myact = "myact";
							Intent in = new Intent(ctx, LookbookView1.class);
							in.putExtra("lookbook_id", pojo.getIdd());
							in.putExtra("username", OtherUserProfile.user_name);
							in.putExtra("userpicurl", OtherUserProfile.url_img);
							in.putExtra("title", pojo.getTitle());
							in.putExtra("hits", pojo.getHits());
							in.putExtra("likes", pojo.getLikes());
							in.putExtra("comments", pojo.getComment_count());
							
							in.putExtra("liked", pojo.getIs_liked());
							ctx.startActivity(in);
							
							Tracker t = ((UILApplication) ctx.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory(
											"Lookbook view on Login User Profile")
									.setAction(
											"Clicked on Lookbook Title by "
													+ pro_user_name)
									.setLabel("Login User Profile")
									.build());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				//
				holder.rel_img_count
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								try {
									 pos = position;
									 Variables.myact = "myact";
									Intent in = new Intent(ctx,
											LookbookView1.class);
									in.putExtra("lookbook_id", pojo.getIdd());
									in.putExtra("username",
											OtherUserProfile.user_name);
									in.putExtra("userpicurl",
											OtherUserProfile.url_img);
									in.putExtra("title", pojo.getTitle());
									in.putExtra("hits", pojo.getHits());
									in.putExtra("likes", pojo.getLikes());
									in.putExtra("comments", pojo.getComment_count());
									
									in.putExtra("liked", pojo.getIs_liked());
									ctx.startActivity(in);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Lookbook view on Login User Profile")
											.setAction(
													"Clicked on Lookbook Image Count by "
															+ pro_user_name)
											.setLabel("Login User Profile")
											.build());

								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});*/

				/*holder.share_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {

							if (listApp != null) {

								customDialog(holder.look_image, "lookbooks",
										pojo.getSlug());
							}
							
							Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
								    TrackerName.APP_TRACKER);
								// Build and send an Event.
							
								t.send(new HitBuilders.EventBuilder()
								    .setCategory("Share on  Login User Profile")
								    .setAction("Shared Lookbook by "+pro_user_name)
								    .setLabel("Login User Profile")
								    .build());

						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;

		
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
		ImageView like_image, share_image,img_view;
		
		ImageView img1, img2,img_edt;
		RelativeLayout last_text, rel_img_count;
		TextView image_count;

	}

	

	@Override
	public int getItemViewType(int position) {

		final UserFeedPojo pojo = mList.get(position);

		if (pojo.getMode().equals("Lookbooks")) {
			mode_position = 0;
		}/* else if (pojo.getMode().equals("Articles")) {
			mode_position = 1;
		} else if (pojo.getMode().equals("StoreReviews")) {
			mode_position = 2;
		}*/

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
	
	

}
