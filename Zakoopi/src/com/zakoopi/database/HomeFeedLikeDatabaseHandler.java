package com.zakoopi.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.zakoopi.helper.POJO;
import com.zakoopi.utils.MyDbvar;

public class HomeFeedLikeDatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "allLike";
	private static final String TABLE_LIKE = "Likes";

	private static final String KEY_ID = "id";
	private static final String KEY_VIEW_ID = "view_ids";
	private static final String KEY_LIKE_VALUE = "like_status";

	public HomeFeedLikeDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String CRETAE_LIKE_TABLE = "CREATE TABLE " + TABLE_LIKE + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LIKE_VALUE
				+ " TEXT," + KEY_VIEW_ID + " INTEGER" + ")";

		db.execSQL(CRETAE_LIKE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKE);
		onCreate(db);
	}

	public void insertLike(POJO pojo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LIKE_VALUE, pojo.getis_liked());
		values.put(KEY_VIEW_ID, Integer.parseInt(pojo.getIdd()));
		db.insert(TABLE_LIKE, null, values);
	}

	public void updateLike(String view1_id, String like_value) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LIKE_VALUE, like_value);
		values.put(KEY_VIEW_ID, view1_id);

		// updating row
		db.update(TABLE_LIKE, values, KEY_VIEW_ID + " = " + view1_id, null);
		// Log.e("UPDATE", like_value + "----" + view1_id);
	}

	public MyDbvar likeShow() {

		MyDbvar vvv = new MyDbvar();
		String selectQuery = "SELECT id,view_ids,like_status FROM "
				+ TABLE_LIKE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {

				String name = cursor.getString(cursor
						.getColumnIndex("like_status"));
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String viewid = cursor.getString(cursor
						.getColumnIndex("view_ids"));

				vvv.setStatus(name);
				vvv.setId(id);
				vvv.setView_id(viewid);

				// Log.e("LikeShow", "" + name);
			} while (cursor.moveToNext());
		}

		return vvv;
	}

	public ArrayList<String> getAllLike() {
		ArrayList<String> like_list = new ArrayList<String>();
		String selectQuery = "SELECT like_status FROM "
				+ TABLE_LIKE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {

				String name = cursor.getString(cursor
						.getColumnIndex("like_status"));

				like_list.add(name);

				// Log.e("GETALLLike", "" + name);
			} while (cursor.moveToNext());
		}

		return like_list;

	}

	public void allDelete() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_LIKE, null, null);
		// db.close();
	}

}
