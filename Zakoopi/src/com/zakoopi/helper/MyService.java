package com.zakoopi.helper;

import java.util.ArrayList;

import com.zakoopi.helper.ImageItem;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

public class MyService extends Service {

	public static ArrayList<ImageItem> images = new ArrayList<ImageItem>();
	String tag = "TestService";
	

	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.i(tag, "Service created...");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		getImageFromGallery(intent);

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	//	Toast.makeText(this, "Service destroyed...", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void getImageFromGallery(Intent inrr) {
		
		images.clear();
		int count = 0;
		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		CursorLoader loader = new CursorLoader(MyService.this,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");
		Cursor imagecursor1 = loader.loadInBackground();
		count = imagecursor1.getCount();
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

			if (images.size() >= count) {

				this.stopService(inrr);
				this.stopSelf();
			}
		}

	}

	String getThumbnailPathForLocalFile(int fileId) {
		Cursor thumbCursor = null;
		
		try {

			thumbCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
					MyService.this.getContentResolver(), fileId,
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