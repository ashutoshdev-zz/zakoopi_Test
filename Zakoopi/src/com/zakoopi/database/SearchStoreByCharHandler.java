package com.zakoopi.database;

import java.util.ArrayList;
import java.util.List;

import com.zakoopi.searchstore.model.SearchPojo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchStoreByCharHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SearchDB";
	private static final String TABLE_SEARCH = "SEARCHDATA";

	private static final String KEY_ID = "id";
	private static final String KEY_STORE_NAME = "storename";
	private static final String KEY_MARKET_NAME = "marketname";
	private static final String KEY_CITY_NAME = "cityname";
	private static final String KEY_STORE_SLUG = "slug";

	public SearchStoreByCharHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CRETAE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_SEARCH + "("
				+ KEY_ID + " TEXT PRIMARY KEY," + KEY_STORE_NAME + " TEXT,"
				+ KEY_MARKET_NAME + " TEXT, " + KEY_STORE_SLUG + " TEXT, "
				+ KEY_CITY_NAME + " TEXT" + ")";
		db.execSQL(CRETAE_PRODUCT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH);
		onCreate(db);
	}

	public void addAllDATA(SearchPojo alldata) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_ID, alldata.getId());
		values.put(KEY_STORE_NAME, alldata.getStorename());
		values.put(KEY_MARKET_NAME, alldata.getStoreadd());
		values.put(KEY_CITY_NAME, alldata.getStorecity());
		values.put(KEY_STORE_SLUG, alldata.getStorslug());

		db.insert(TABLE_SEARCH, null, values);

	}

	public ArrayList<SearchPojo> getAllDATA() {
		ArrayList<SearchPojo> productlist = new ArrayList<SearchPojo>();

		String selectQuery = "SELECT * FROM " + TABLE_SEARCH;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				SearchPojo allProduct = new SearchPojo();
				allProduct.setId(cursor.getString(0));
				allProduct.setStorename(cursor.getString(1));
				allProduct.setStoreadd(cursor.getString(2));
				allProduct.setStorslug(cursor.getString(3));
				allProduct.setStorecity(cursor.getString(4));
				productlist.add(allProduct);
			} while (cursor.moveToNext());
		}
		return productlist;

	}

}
