package com.zakoopi.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zakoopi.search.AllProduct;

public class HomeSearchAllProductDatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "allProductDatabase";
	private static final String TABLE_PRODUCT = "product";

	private static final String KEY_ID = "id";
	private static final String KEY_PRODUCT_NAME = "product_name";
	private static final String KEY_PRODUCT_CATEGORY = "product_category";
	private static final String KEY_PRODUCT_SLUG = "product_slug";
    public static ArrayList<String>slug_list;
    ArrayList<String> productlist;
    
	public HomeSearchAllProductDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CRETAE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAME
				+ " TEXT," + KEY_PRODUCT_CATEGORY + " TEXT," + KEY_PRODUCT_SLUG + " TEXT"+ ")";
		db.execSQL(CRETAE_PRODUCT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
		onCreate(db);
	}

	public void addAllProduct(AllProduct allProduct) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_PRODUCT_NAME, allProduct.getProductName());
		values.put(KEY_PRODUCT_CATEGORY, allProduct.getProductCategory());
		values.put(KEY_PRODUCT_SLUG, allProduct.getSlug());
		db.insert(TABLE_PRODUCT, null, values);
		// db.close();
	}

	public List<AllProduct> getAllProducts() {
		List<AllProduct> productlist = new ArrayList<AllProduct>();

		String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				AllProduct allProduct = new AllProduct();
				allProduct.setID(Integer.parseInt(cursor.getString(0)));
				allProduct.setProductName(cursor.getString(1));
				allProduct.setProductCategory(cursor.getString(2));
				allProduct.setSlug(cursor.getString(3));
				productlist.add(allProduct);
			} while (cursor.moveToNext());
		}
		return productlist;

	}

	public List<String> getAllProducts1() {
		List<String> productlist = new ArrayList<String>();

		String selectQuery = "SELECT product_name FROM " + TABLE_PRODUCT;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor
						.getColumnIndex("product_name"));

				productlist.add(name);

			} while (cursor.moveToNext());
		}
		return productlist;

	}

	public List<String> listDataByCategory(String catogery) {
		productlist = new ArrayList<String>();
		slug_list=new ArrayList<String>();
		String q = "SELECT * FROM " + TABLE_PRODUCT
				+ " WHERE product_category = '" + catogery + "'";
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery(q, null);
		if (cursor.moveToFirst()) {
			do {
				String itemname = cursor.getString(cursor
						.getColumnIndex("product_name"));
				
				String itemslug = cursor.getString(cursor
						.getColumnIndex("product_slug"));
				
			
				slug_list.add(itemslug);
				productlist.add(itemname);
	} while (cursor.moveToNext());

		}
		return productlist;
	}

	public int getProductCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRODUCT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		// cursor.close();

		// return count
		return cursor.getCount();
	}

	public void allDelete() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.delete(TABLE_PRODUCT, null, null);
		// db.close();
	}
}
