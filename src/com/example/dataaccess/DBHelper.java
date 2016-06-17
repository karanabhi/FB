package com.example.dataaccess;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "feedback";

	public static final String TABLE_USER_MASTER = "user_master";

	public static final String COL_USER_ID = "user_id";
	public static final String COL_USER_MOBILE_NO = "user_mobile_number";
	public static final String COL_USER_POLICY_NO = "user_policy_number";
	public static final String COL_USER_EMAIL = "user_email";
	public static final String COL_USER_NAME = "user_name";
	public static final String COL_USER_PAN = "user_pan";
	public static final String COL_USER_DOB = "user_dob";
	public static final String COL_USER_PURPOSE = "user_purpose";
	public static final String COL_USER_RATING = "user_rating";
	public static final String COL_USER_RATING_COMMENTS = "user_rating_comments";
	public static final String COL_USER_STATUS = "user_status";
	public static final String COL_USER_DEL_FLAG = "del_flag";

	SQLiteDatabase db = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}// Constructor

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_USER_MASTER + " ( "
				+ " " + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_USER_MOBILE_NO + " TEXT, " + COL_USER_POLICY_NO
				+ " TEXT, " + " " + COL_USER_EMAIL + " TEXT, " + COL_USER_NAME
				+ " TEXT, " + COL_USER_PAN + " TEXT, " + COL_USER_DOB
				+ " TEXT, " + COL_USER_PURPOSE + " TEXT, " + COL_USER_RATING
				+ " TEXT, " + COL_USER_RATING_COMMENTS + " TEXT, "
				+ COL_USER_STATUS + " TEXT, " + COL_USER_DEL_FLAG
				+ " INTEGER DEFAULT 0  );  ");

		db.execSQL("insert into "
				+ TABLE_USER_MASTER
				+ "("
				+ COL_USER_MOBILE_NO
				+ ","
				+ COL_USER_POLICY_NO
				+ ","
				+ COL_USER_EMAIL
				+ ","
				+ COL_USER_NAME
				+ ","
				+ COL_USER_PAN
				+ ","
				+ COL_USER_DOB
				+ ","
				+ COL_USER_PURPOSE
				+ ","
				+ COL_USER_RATING
				+ ","
				+ COL_USER_RATING_COMMENTS
				+ ","
				+ COL_USER_STATUS
				+ ","
				+ COL_USER_DEL_FLAG
				+ " ) values('2222222222','123asdzxcqw','asd@asd.op','zxcqwe','AVD2PASS3E','12-12-2012','Claims','4','YOLO!','1',0 );");

	}// onCreate()

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_MASTER + " ");

	}// onUpdrade()

	public void createConnection() {
		db = this.getWritableDatabase();
	}// createConnection()

	public Cursor getDummyData() {
		Cursor res = db.rawQuery("select * from user_master", null);
		return res;
	}

}// class
