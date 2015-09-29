package com.zakoopi.searchResult;

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
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.activity.ArticleView;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.activity.OtherUserProfile;
import com.zakoopi.helper.ConnectionDetector;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.UILApplication;
import com.zakoopi.utils.UILApplication.TrackerName;

@SuppressWarnings("deprecation")
public class TopExperiencesAdapter extends ArrayAdapter<TopExperiencesPojo> {

	private List<TopExperiencesPojo> mList;
	public static String type = "no";
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
	String user_id;
	int displayWidth;
	private SharedPreferences pref_location;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	private String city_name;
Boolean isInternetPresent = false;
    
    // Connection detector class
    ConnectionDetector cd;

	public TopExperiencesAdapter(Context context,
			ArrayList<TopExperiencesPojo> list,ArrayList<Integer> color) {
		super(context, 0, list);
		ctx = context;
		mList = list;
this.colorlist = color;
		client = ClientHttp.getInstance(ctx);

		try {
			 cd = new ConnectionDetector(ctx);
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
		
		Point size = new Point();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getSize(size);
		displayWidth = size.x;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		listApp = showAllShareApp();

		View article_result = convertView;
		final ArticleHolder article_holder;

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(colorlist.get(position))
				.showImageForEmptyUri(R.drawable.placeholder_photo)
				.showImageOnFail(R.drawable.placeholder_photo).cacheInMemory(true)
				.delayBeforeLoading(100).cacheOnDisk(true).build();

		if (article_result == null) {
			LayoutInflater inf = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			article_result = inf.inflate(
					R.layout.article_feed_item, parent, false);
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

			article_holder.like_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					ImageView img = (ImageView) v;
					TopExperiencesPojo ppp = (TopExperiencesPojo) img.getTag();
					String string_like;
					String post_mode = "Articles";
					String article_iddd = ppp.getIdd();
					String get_likes = ppp.getLikes();
					if (ppp.getIs_liked().equals("true")) {

						article_holder.like_image
								.setImageResource(R.drawable.home_like_inactive);
						long count = Long.parseLong(ppp.getLikes()) - 1;
						article_holder.lookbook_like.setText("" + count);
						ppp.setIs_liked("false");
						ppp.setLikes("" + count);
						string_like = "0";
						article_like(article_iddd, string_like, post_mode);
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Unlike Article on Experiences Feed")
								.setAction("Unliked Article by " + pro_user_name)
								.setLabel("Experiences Feed").build());

					} else {

						article_holder.like_image
								.setImageResource(R.drawable.home_like_active);
						long count = Long.parseLong(ppp.getLikes()) + 1;
						article_holder.lookbook_like.setText("" + count);
						ppp.setIs_liked("true");
						ppp.setLikes("" + count);
						string_like = "1";
						article_like(article_iddd, string_like, post_mode);
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory("Like Article on Experiences Feed")
								.setAction("Liked Article by " + pro_user_name)
								.setLabel("Experiences Feed").build());
					}

				}
			});

		} else {
			article_holder = (ArticleHolder) article_result.getTag();
		}

		final TopExperiencesPojo article_pojo = mList.get(position);
		
		try { final int width1 = displayWidth; final int height1 =
				  Integer.parseInt(article_pojo.getMedium_img_h());
				  
				  final int set_height = (int) (((width1 * 1.0) / Integer
				  .parseInt(article_pojo.getMedium_img_w())) * height1);
				  
				  article_holder.look_image.getLayoutParams().height = set_height;
				  article_holder.look_image.getLayoutParams().width = width1; } catch
				  (Exception e) { // TODO: handle exception
				  e.printStackTrace(); }
		
		Log.e("TopExperiencesPojo", "" + mList.size());
		try {
			if (Integer.parseInt(article_pojo.getImage_count()) >= 3) {
				/*ImageLoader.getInstance().displayImage(
						article_pojo.getLookimg(), article_holder.look_image,
						options, animateFirstListener);*/
				
				
				
				Picasso.with(ctx).load(article_pojo.getLookimg())
				.placeholder(colorlist.get(position))
				.into(article_holder.look_image);
				
				article_holder.img1.setVisibility(View.VISIBLE);
				article_holder.img2.setVisibility(View.VISIBLE);

				ImageLoader.getInstance().displayImage(article_pojo.getImg1(),
						article_holder.img1, options, animateFirstListener);
				ImageLoader.getInstance().displayImage(article_pojo.getImg2(),
						article_holder.img2, options, animateFirstListener);

				if (Integer.parseInt(article_pojo.getImage_count()) == 3) {

					article_holder.last_text.setVisibility(View.GONE);
				} else {
					article_holder.last_text.setVisibility(View.VISIBLE);
					article_holder.image_count.setText("+"
							+ article_pojo.getImage_count());
				}

			}

			else if (Integer.parseInt(article_pojo.getImage_count()) == 2) {

				/*ImageLoader.getInstance().displayImage(
						article_pojo.getLookimg(), article_holder.look_image,
						options, animateFirstListener);*/
				
				Picasso.with(ctx).load(article_pojo.getLookimg())
				.placeholder(colorlist.get(position))
				.into(article_holder.look_image);
				
				ImageLoader.getInstance().displayImage(article_pojo.getImg1(),
						article_holder.img2, options, animateFirstListener);
				article_holder.img2.setVisibility(View.VISIBLE);
				article_holder.img1.setVisibility(View.GONE);
				article_holder.last_text.setVisibility(View.GONE);
			} else {

				/*ImageLoader.getInstance().displayImage(
						article_pojo.getLookimg(), article_holder.look_image,
						options, animateFirstListener);*/
				
				Picasso.with(ctx).load(article_pojo.getLookimg())
				.placeholder(colorlist.get(position))
				.into(article_holder.look_image);
				
				article_holder.img1.setVisibility(View.GONE);
				article_holder.img2.setVisibility(View.GONE);

				article_holder.last_text.setVisibility(View.GONE);
			}

			if (article_pojo.getIs_liked().equals("true")) {

				article_holder.like_image
						.setImageResource(R.drawable.home_like_active);
			} else {

				article_holder.like_image
						.setImageResource(R.drawable.home_like_inactive);
			}

			article_holder.user_name.setText(article_pojo.getUsername());

			ImageLoader.getInstance().displayImage(article_pojo.getUserimg(),
					article_holder.user_image, options);

			article_holder.lookbook_view.setText(article_pojo.getHits());
			article_holder.title.setText(article_pojo.getTitle());
			Log.e("TITLE", article_pojo.getTitle());
			article_holder.lookbook_like.setText(article_pojo.getLikes());
			user_id = article_pojo.getUserid();

			article_holder.rel1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Intent user = new Intent(ctx, OtherUserProfile.class);
						user.putExtra("user_id", user_id);
						ctx.startActivity(user);
						
						type = "Experiences";
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Profile view on Experiences feed")
								.setAction(
										"Clicked on Profile by "
												+ pro_user_name)
								.setLabel("Experiences Feed")
								.build());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			article_holder.title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {

						Intent in = new Intent(ctx, ArticleView.class);
						in.putExtra("article_id", article_pojo.getIdd());
						in.putExtra("username", article_pojo.getUsername());
						in.putExtra("userpicurl", article_pojo.getUserimg());
						in.putExtra("title", article_pojo.getTitle());
						in.putExtra("hits", article_pojo.getHits());
						in.putExtra("likes", article_pojo.getLikes());
						in.putExtra("description",
								article_pojo.getDescription());
						in.putExtra("is_new", article_pojo.getIs_new());
						in.putExtra("comments", article_pojo.getComment_count());
						ctx.startActivity(in);
						
						type = "Experiences";
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Article view on Experiences Feed")
								.setAction(
										"Clicked on Article Title by "
												+ pro_user_name)
								.setLabel("Experiences Feed")
								.build());

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});

			article_holder.look_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {

						Intent in = new Intent(ctx, ArticleView.class);
						in.putExtra("article_id", article_pojo.getIdd());
						in.putExtra("username", article_pojo.getUsername());
						in.putExtra("userpicurl", article_pojo.getUserimg());
						in.putExtra("title", article_pojo.getTitle());
						in.putExtra("hits", article_pojo.getHits());
						in.putExtra("likes", article_pojo.getLikes());
						in.putExtra("description",
								article_pojo.getDescription());
						in.putExtra("is_new", article_pojo.getIs_new());
						in.putExtra("comments", article_pojo.getComment_count());

						ctx.startActivity(in);
						
						type = "Experiences";
						
						Tracker t = ((UILApplication) ctx.getApplicationContext())
								.getTracker(TrackerName.APP_TRACKER);
						// Build and send an Event.

						t.send(new HitBuilders.EventBuilder()
								.setCategory(
										"Article view on Experiences Feed")
								.setAction(
										"Clicked on Article Cover by "
												+ pro_user_name)
								.setLabel("Experiences Feed")
								.build());

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
							
							Intent in = new Intent(ctx, ArticleView.class);
							in.putExtra("article_id", article_pojo.getIdd());
							in.putExtra("username", article_pojo.getUsername());
							in.putExtra("userpicurl", article_pojo.getUserimg());
							in.putExtra("title", article_pojo.getTitle());
							in.putExtra("hits", article_pojo.getHits());
							in.putExtra("likes", article_pojo.getLikes());
							in.putExtra("description",
									article_pojo.getDescription());
							in.putExtra("is_new", article_pojo.getIs_new());
							in.putExtra("comments", article_pojo.getComment_count());
							ctx.startActivity(in);
							
							type = "Experiences";
							
							Tracker t = ((UILApplication) ctx.getApplicationContext())
									.getTracker(TrackerName.APP_TRACKER);
							// Build and send an Event.

							t.send(new HitBuilders.EventBuilder()
									.setCategory(
											"Article view on Experiences Feed")
									.setAction(
											"Clicked on Article Image Count by "
													+ pro_user_name)
									.setLabel("Experiences Feed")
									.build());
							
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
										"articles", article_pojo.getSlug());
							}
							
							Tracker t = ((UILApplication)ctx.getApplicationContext()).getTracker(
								    TrackerName.APP_TRACKER);
								// Build and send an Event.
							
								t.send(new HitBuilders.EventBuilder()
								    .setCategory("Share on Experiences Feed")
								    .setAction("Shared Article by "+pro_user_name)
								    .setLabel("Experiences Feed")
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
		Log.e("user_ID", user_id);
		Log.e("TIME", String.valueOf(System.currentTimeMillis()));
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
					Log.e("URLLL", ctx.getString(R.string.base_url)
							+ "Common/like/" + article_id + ".json" + "---"
							+ string_like);
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {

						text2 = text2 + text1;
					}
					Log.e("RES", text2);
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
					Log.e("RES+_FAIl", text21);
				} catch (Exception ee) {

				}
				Log.e("FAIL", "FAIL");
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

					Toast.makeText(ctx, "Image not found", 5000).show();
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

			try {
			
			ResolveInfo appInfo = listApp.get(position);
			holder.ivLogo.setImageDrawable(appInfo.loadIcon(pm));
			holder.tvAppName.setText("Share on " + appInfo.loadLabel(pm));
			
			} catch (Exception e) {
				// TODO: handle exception
			}
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
		Log.e("HEIGHT", "" + MainActivity.height + "----" + height1);
		dd.setContentView(R.layout.custom_gridview);
		Window window = dd.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, height1);
		window.setGravity(Gravity.BOTTOM);
		// final GridView ggg = (GridView) dd.findViewById(R.id.gridView1);
		final ListView ggg = (ListView) dd.findViewById(R.id.listView1);
		dd.show();

		try {
			ggg.setAdapter(new MyAdapter());
		} catch (Exception e) {
			// TODO: handle exception
		}
		

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
