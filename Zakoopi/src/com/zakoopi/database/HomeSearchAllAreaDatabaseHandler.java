package com.zakoopi.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.zakoopi.search.AllArea;

public class HomeSearchAllAreaDatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "allAreaDatabase";
	private static final String TABLE_AREA = "area";

	private static final String KEY_ID = "id";
	private static final String KEY_AREA_NAME = "area_name";
	private static final String KEY_AREA_SLUG = "slug_name";
	public static ArrayList<String>area_slug_list;
	//private static final String KEY_PRODUCT_CATEGORY = "product_category";

	public HomeSearchAllAreaDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CRETAE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_AREA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_AREA_NAME
				+ " TEXT," + KEY_AREA_SLUG
				+ " TEXT" +")";
		db.execSQL(CRETAE_PRODUCT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA);
		onCreate(db);
	}

	public void addAllArea(AllArea allArea) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_AREA_NAME, allArea.getMarketName());
		values.put(KEY_AREA_SLUG, allArea.getMyslug());
		db.insert(TABLE_AREA, null, values);

	}

	public List<AllArea> getAllAreas() {
		List<AllArea> arealist1 = new ArrayList<AllArea>();

		String selectQuery = "SELECT area_name FROM " + TABLE_AREA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				AllArea allArea = new AllArea();
				allArea.setID(Integer.parseInt(cursor.getString(0)));
				allArea.setMarketName(cursor.getString(1));
				//allProduct.setProductCategory(cursor.getString(2));

				arealist1.add(allArea);
			//	Log.e("DB_AREA", ""+arealist1);
			} while (cursor.moveToNext());
		}
		return arealist1;

	}
	
	
	
	public ArrayList<String> getAllAreas1() {
		ArrayList<String> arealist1 = new ArrayList<String>();
		area_slug_list = new ArrayList<String>();
		String selectQuery = "SELECT * FROM " + TABLE_AREA;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				
				String name = cursor.getString(cursor.getColumnIndex("area_name"));
				arealist1.add(name);
				String slug = cursor.getString(cursor.getColumnIndex("slug_name"));
				area_slug_list.add(slug);
			} while (cursor.moveToNext());
		}
		return arealist1;

	}
	
	
	/*public List<String> listDataByName(String name){
		ArrayList<String>arealist = new ArrayList<String>();
		String q="SELECT * FROM "+TABLE_AREA+" WHERE area_name = '" + name +"'";
	    SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = null;
	        cursor = db.rawQuery(q, null);
	        if (cursor.moveToFirst()) {
	            do {
	            	String itemname =  cursor.getString(cursor.getColumnIndex("product_name"));
	            	productlist.add(itemname);
	            } while (cursor.moveToNext());

	        }
			return productlist;
	}*/

	public int getAreaCount() {
		String countQuery = "SELECT  * FROM " + TABLE_AREA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();

		// return count
		return cursor.getCount();
	}

	public void allDelete() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_AREA, null, null);
		//db.close();
	}
}
