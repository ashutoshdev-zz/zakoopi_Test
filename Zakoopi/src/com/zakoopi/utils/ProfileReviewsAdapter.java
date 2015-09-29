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
import android.util.Log;
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
import com.zakoopi.activity.MainActivity;
import com.zakoopi.userfeed.model.UserFeedPojo;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class ProfileReviewsAdapter extends BaseAdapter {

	public static List<UserFeedPojo> mList;
	public static int pos = 0;
	Context ctx;
	//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	//private DisplayImageOptions options;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password, pro_user_id;
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

	public ProfileReviewsAdapter(Context context, List<UserFeedPojo> list,
			ArrayList<Integer> color) {
		ctx = context;
		mList = list;
		this.colorlist = color;

		try {

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
			pro_user_id = pro_user_pref.getString("user_id", "adajfh");
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
			View review_result = convertView;
			final ReviewHolder review_holder;

			/*options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();*/

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

								try {
									
								
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
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory("Unlike Review on Login User Profile")
											.setAction("Unliked Review by " + pro_user_name)
											.setLabel("Login User Profile").build());

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
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory("Like Review on Login User Profile")
											.setAction("Liked Review by " + pro_user_name)
											.setLabel("Login User Profile").build());
								}
								} catch (Exception e) {
									// TODO: handle exception
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
				//review_holder.lookbook_view.setText(review_pojo.getHits());
				
				int hits_count = Integer.parseInt(review_pojo.getHits_text());
				if (hits_count >= 100 ) {
					review_holder.lookbook_view.setVisibility(View.VISIBLE);
					review_holder.lookbook_view.setText(review_pojo.getHits());
					review_holder.img_view.setVisibility(View.VISIBLE);
				} else {
					review_holder.lookbook_view.setVisibility(View.GONE);
					review_holder.img_view.setVisibility(View.GONE);
				}
				
				
				/*review_holder.review.setText(Html.fromHtml(review_pojo
						.getReview()));*/
				
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
										"Review view on Login User Profile")
								.setAction(
										"Clicked on Review by "
												+ pro_user_name)
								.setLabel("Login User Profile")
								.build());

					}
				});

				review_holder.rel_store_rate
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								try {

									Intent in = new Intent(ctx,
											StoreActivity.class);
									in.putExtra("store_id",
											review_pojo.getStore_id());
									ctx.startActivity(in);
									
									Tracker t = ((UILApplication) ctx.getApplicationContext())
											.getTracker(TrackerName.APP_TRACKER);
									// Build and send an Event.

									t.send(new HitBuilders.EventBuilder()
											.setCategory(
													"Store view on Login User Profile")
											.setAction(
													"Clicked on Store View by "
															+ pro_user_name)
											.setLabel("Login User Profile")
											.build());

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
									String share_text = review_pojo.getStore_name()+", "+review_pojo.getStore_location()+" by "+ pro_user_name;
									if (listApp != null) {

										customDialog(review_holder.user_image,
												"StoreReviews",
												review_pojo.getSlug(),share_text, review_pojo.getIdd(), pro_user_id);
									}

									Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
										    TrackerName.APP_TRACKER);
										// Build and send an Event.
									
										t.send(new HitBuilders.EventBuilder()
										    .setCategory("Share on Login User Profile")
										    .setAction("Shared Review by "+pro_user_name)
										    .setLabel("Login User Profile")
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
		if (getCount() != 0)
			return getCount();

		return 1;

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

		if (pojo.getMode().equals("StoreReviews")) {
			mode_position = 0;
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
			final String mode1, final String slug1, final String share_text1, String mode_id, String user_id) {

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
				share(listApp.get(position), img, mode, slug,share_text,mode_id,user_id);
				dd.cancel();
			}
		});

	}
	
	
public void fullReviewDialog(){
		
		final Dialog dd=new Dialog(ctx,android.R.style.Theme_Translucent_NoTitleBar);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setContentView(R.layout.full_review);
		dd.show();
		final TextView txt_user_name = (TextView) dd.findViewById(R.id.user_name);
		final TextView lookbook_view = (TextView) dd.findViewById(R.id.user_view);
		final TextView lookbook_like = (TextView) dd.findViewById(R.id.txt_like_count);
		final ImageView img_user_image = (ImageView) dd.findViewById(R.id.img_profile);
		final RelativeLayout rel_rated_box = (RelativeLayout) dd.findViewById(R.id.rel_rated_box);
		final ImageView like_image = (ImageView) dd.findViewById(R.id.img_like);
		final RelativeLayout rel_store_rate = (RelativeLayout) dd.findViewById(R.id.rel_rate);
		final ImageView share_image = (ImageView) dd.findViewById(R.id.img_share);
		final TextView review = (TextView) dd.findViewById(R.id.txt_review);
		final TextView txt_store_name = (TextView) dd.findViewById(R.id.txt_store_name);
		final TextView txt_store_address = (TextView) dd.findViewById(R.id.txt_store_location);
		final TextView txt_store_rate = (TextView) dd.findViewById(R.id.txt_rate);
		final TextView txt_rated = (TextView) dd.findViewById(R.id.txt_rated);
		final RelativeLayout rel1 = (RelativeLayout) dd.findViewById(R.id.rel1);
		final RelativeLayout rel_back = (RelativeLayout) dd.findViewById(R.id.rel_back);
		final TextView txt_back = (TextView) dd.findViewById(R.id.txt);

		img_user_image.setVisibility(View.GONE);
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
		final UserFeedPojo pojo = ProfileReviewsAdapter.mList.get(ProfileReviewsAdapter.pos);
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
				String share_text = pojo.getStore_name()+", "+pojo.getStore_location()+" by "+ pojo.getUser_name();
				if (listApp != null) {

					customDialog(img_user_image, "StoreReviews",pojo.getSlug(), share_text, pojo.getIdd(), pro_user_id);
					//dd.cancel();
					}
				
				Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
					    TrackerName.APP_TRACKER);
					// Build and send an Event.
				
					t.send(new HitBuilders.EventBuilder()
					    .setCategory("Share on Login User Profile Full Review")
					    .setAction("Shared Review by "+pro_user_name)
					    .setLabel("Login User Profile Full Review")
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
					
					if (pojo.getIs_liked().equals("true")) {

						like_image
								.setImageResource(R.drawable.home_like_inactive);
						long count = Long.parseLong(lookbook_like.getText().toString()) - 1;
						lookbook_like.setText("" + count);
						
						string_like = "0";
						pojo.setIs_liked("false");
						pojo.setLikes("" + count);
						article_like(article_iddd, string_like, post_mode);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Unlike Review on Login User Profile Full Review")
								.setAction("Unliked Review by " + pro_user_name)
								.setLabel("Login User Profile Full Review").build());

					} else {

						like_image
								.setImageResource(R.drawable.home_like_active);
						long count = Long.parseLong(lookbook_like.getText().toString()) + 1;
						lookbook_like.setText("" + count);
						
						string_like = "1";
						pojo.setIs_liked("true");
						pojo.setLikes("" + count);
						article_like(article_iddd, string_like, post_mode);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Like Review on Login User Profile Full Review")
								.setAction("Liked Review by " + pro_user_name)
								.setLabel("Login User Profile Full Review").build());

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				//dd.cancel();
				notifyDataSetChanged();
			}
		});
		
		
		try {
			txt_user_name.setText("Reviewed");
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
			
		/*	ImageLoader.getInstance().displayImage(pojo.getUser_img(), img_user_image,
					options);*/

			if (pojo.getIs_liked().equals("true")) {

				like_image.setImageResource(R.drawable.home_like_active);
			} else {

				like_image.setImageResource(R.drawable.home_like_inactive);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
						 Log.e("SHARE", text);
					} catch (Exception e) {

					}

				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {

					 Log.e("FAIL", "FAIL" + arg0);
				}
			});
}
}
