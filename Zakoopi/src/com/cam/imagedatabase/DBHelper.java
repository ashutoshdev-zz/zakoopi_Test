package com.cam.imagedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DBNAME = "MAINDATABASE2";
	public static final String DBTABLE = "lookbook";
	public static final String DBTABLE1 = "Stores";
	public static final String DBTABLE2 = "HomeFeed";
	public static final String DBTABLE3 = "TopMarket";
	public static final String DBTABLE4 = "LoginUserInfo";
	public static final String DBTABLE5 = "LoginUserAct";
	public static final int version = 1;

	public DBHelper(Context context) {
		super(context, DBNAME, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("create table  "
				+ DBTABLE
				+ " (id integer primary key autoincrement,photo blob,tag text,desc text,imagepath text,storeid text,mypath text, card_id text,slug text)");

		db.execSQL("create table  " + DBTABLE1
				+ " (id text primary key ,market text)");
		
		db.execSQL("create table  "
				+ DBTABLE2
				+ " (id integer primary key autoincrement ,mode text,type text,username text,userimg text,lookimg text,img1 text,img2 text,"
				+ " likes text, hits text, title text, store_name text, store_location text,store_rate text, review text,"
				+ " image_count text, idd text, description text, is_new text, is_liked text, slug text, rated_color text,"
				+ " rated text, userid text, comment_count text, img_width text, img_height text, feed_id text, status text ,"
				+ " color text, hits_count text, store_id text)");

		db.execSQL("create table  "
				+ DBTABLE3
				+ " (id integer primary key autoincrement ,market_name text,market_img text,url_slug text)");
		
		db.execSQL("create table  "
				+ DBTABLE4
				+ " (id integer primary key autoincrement ,user_name text,url_img text,user_gender text,user_age text,user_city text,zakoopi_points text,lookbook_count text,"
				+ " like_count text,review_count text,fb_link text,twitter_link text,website_link text,lookbook_draft_count text,uid text)");
		
		db.execSQL("create table  "
				+ DBTABLE5
				+ " (id integer primary key autoincrement ,mode text,type text,username text,userimg text,lookimg text,img1 text,img2 text,"
				+ " likes text, hits text, title text, store_name text, store_location text, review text,"
				+ " image_count text, idd text, description text, is_new text, is_liked text, slug text, rated_color text,"
				+ " rated text, userid text, comment_count text, img_width text, img_height text, status text , color text, hits_view text, hits_count text, store_id text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
