package com.zakoopi.gcm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mystores.StoreActivity;
import com.zakoopi.R;
import com.zakoopi.activity.ArticleView;
import com.zakoopi.activity.LookbookView1;
import com.zakoopi.activity.MainActivity;

public class MSGService extends IntentService {

	SharedPreferences prefs;
	NotificationCompat.Builder notification;
	NotificationManager manager;

	public MSGService() {
		super("MSGService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);


		prefs = getSharedPreferences("GCM", 0);

		if (!extras.isEmpty()) {

			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {

			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				try {
					//Log.e("TAG", "Received: " + extras.getString("message"));
					sendNotification(extras.getString("message"),
							extras.getString("cover_img"),
							extras.getString("model"), extras.getString("key"));
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
		// MSGReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg, String cover_img, String model,
			String key) {

		Bundle args = new Bundle();
		args.putString("message", msg);
		args.putString("cover_img", cover_img);
		args.putString("model", model);
		args.putString("key", key);

		if (cover_img.equals("") || cover_img == null) {

			if (model.equals("Lookbooks")) {
				Intent resultIntent = new Intent(this, LookbookView1.class);
				resultIntent.putExtra("lookbook_id", key);
				resultIntent.putExtra("username", "nouser");

				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				resultIntent.putExtra("NOTIFICATION_ID", 1);

				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				Notification.Builder mBuilder = new Notification.Builder(this)

				.setSmallIcon(R.drawable.zakoopi).setContentTitle(msg)
						.setAutoCancel(true);

				NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());

			} else if (model.equals("Articles")) {

				Intent resultIntent = new Intent(this, ArticleView.class);
				resultIntent.putExtra("article_id", key);
				resultIntent.putExtra("username", "nouser");

				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				resultIntent.putExtra("NOTIFICATION_ID", 1);

				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				Notification.Builder mBuilder = new Notification.Builder(this)

				.setSmallIcon(R.drawable.zakoopi).setContentTitle(msg)
						.setAutoCancel(true);

				NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
			} else {

				Intent resultIntent = new Intent(this, StoreActivity.class);
				resultIntent.putExtra("store_id", key);
				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				resultIntent.putExtra("NOTIFICATION_ID", 1);

				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				Notification.Builder mBuilder = new Notification.Builder(this)

				.setSmallIcon(R.drawable.zakoopi).setContentTitle(msg)
						.setAutoCancel(true);

				NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
			}
		} else {

			if (model.equals("Lookbooks")) {

				Intent resultIntent = new Intent(this, LookbookView1.class);
				resultIntent.putExtra("lookbook_id", key);
				resultIntent.putExtra("username", "nouser");

				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				resultIntent.putExtra("NOTIFICATION_ID", 1);

				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
						R.drawable.zakoopi);

				Notification.Builder mBuilder = new Notification.Builder(this)

						.setSmallIcon(R.drawable.zakoopi)
						.setContentTitle(msg)
						.setLargeIcon(icon)
						.setContentIntent(pendingIntent)

						.setStyle(
								new Notification.BigPictureStyle()
										.bigPicture(getBitmapFromURL(cover_img)))
						.setAutoCancel(true);

				NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());

			} else if (model.equals("Articles")) {

				Intent resultIntent = new Intent(this, ArticleView.class);
				resultIntent.putExtra("article_id", key);
				resultIntent.putExtra("username", "nouser");

				resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				resultIntent.putExtra("NOTIFICATION_ID", 1);

				PendingIntent pendingIntent = PendingIntent.getActivity(this,
						1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
						R.drawable.zakoopi);

				Notification.Builder mBuilder = new Notification.Builder(this)

						.setSmallIcon(R.drawable.zakoopi)
						.setContentTitle(msg)
						.setLargeIcon(icon)
						.setContentIntent(pendingIntent)

						.setStyle(
								new Notification.BigPictureStyle()
										.bigPicture(getBitmapFromURL(cover_img)))
						.setAutoCancel(true);

				NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
			}
		}
	}

	public Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}