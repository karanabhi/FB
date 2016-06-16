package com.example.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "feedback";

	public static final String TABLE_PURPOSE = "purpose";
	public static final String TABLE_DISSATISFACTION = "dissatisfaction";
	public static final String TABLE_USER_MASTER = "user_master";
	public static final String TABLE_PURPOSE_MASTER = "purpose_master";
	public static final String TABLE_DISSATISFACTION_MASTER = "dissatisfaction_master";
	public static final String TABLE_DISSATISFACTION_COMMENTS_MASTER = "dissatisfaction_comments_master";

	public static final String COL_PURPOSE_ID = "purpose_id";
	public static final String COL_PURPOSE_NAME = "purpose_name";
	public static final String COL_DISSATISFACTION_ID = "dissatisfaction_id";
	public static final String COL_DISSATISFACTION_NAME = "dissatisfaction_name";
	public static final String COL_USER_ID = "user_id";
	public static final String COL_USER_MOBILE_NO = "user_mobile_number";
	public static final String COL_USER_POLICY_NO = "user_policy_number";
	public static final String COL_USER_EMAIL = "user_email";
	public static final String COL_USER_NAME = "user_name";
	public static final String COL_USER_PAN = "user_pan";
	public static final String COL_USER_DOB = "user_DOB";
	public static final String COL_USER_PURPOSE = "user_dob";
	public static final String COL_USER_RATING = "user_rating";
	public static final String COL_USER_RATING_COMMENTS = "user_rating_comments";
	public static final String COL_USER_STATUS = "user_status";
	public static final String COL_ID = "id";
	public static final String COL_PURPOSE_COMMENTS = "purpose_comments";
	public static final String COL_DISSATISFACTION_COMMENTS = "dissatisfaction_comments";

	SQLiteDatabase db = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}// Constructor

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_PURPOSE + "(" + " "
				+ COL_PURPOSE_ID + " INTEGER PRIMARY KEY, " + COL_PURPOSE_NAME
				+ " TEXT); ");
		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_DISSATISFACTION + "("
				+ " " + COL_DISSATISFACTION_ID + " INTEGER PRIMARY KEY, "
				+ COL_DISSATISFACTION_NAME + " TEXT); ");
		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_USER_MASTER + " ( "
				+ " " + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_USER_MOBILE_NO + " TEXT, " + COL_USER_POLICY_NO
				+ " TEXT, " + " " + COL_USER_EMAIL + " TEXT, " + COL_USER_NAME
				+ " TEXT, " + COL_USER_PAN + " TEXT, " + COL_USER_DOB
				+ " TEXT, " + COL_USER_PURPOSE + " TEXT, " + " "
				+ COL_USER_RATING + " TEXT, " + COL_USER_RATING_COMMENTS
				+ " TEXT, " + COL_USER_STATUS + " TEXT);  ");
		db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_PURPOSE_MASTER
				+ " ( " + " " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_USER_ID + " INTEGER, " + COL_PURPOSE_COMMENTS
				+ " TEXT); ");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DISSATISFACTION_MASTER
				+ " ( " + " " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_USER_ID + " INTEGER, " + COL_DISSATISFACTION_ID
				+ " INTEGER); ");
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_DISSATISFACTION_COMMENTS_MASTER + " ( " + " " + COL_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_DISSATISFACTION_ID + " INTEGER, "
				+ COL_DISSATISFACTION_COMMENTS + " TEXT);");

		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Renewal Premium Payment') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Policy Alterations/ Corrections/ Change') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Payout') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Claims') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Others') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Wish to know about insurance plan') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Wish to become Insurance Agent') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Wish to make a career in SBI Life') ");
		db.execSQL("INSERT INTO " + TABLE_PURPOSE + "(" + COL_PURPOSE_NAME
				+ ") VALUES ('Others') ");

		db.execSQL("INSERT INTO " + TABLE_DISSATISFACTION + " ("
				+ COL_DISSATISFACTION_NAME
				+ " ) VALUES('Service not Received at all.')  ");
		db.execSQL("INSERT INTO "
				+ TABLE_DISSATISFACTION
				+ " ("
				+ COL_DISSATISFACTION_NAME
				+ " ) VALUES('Serviving Representative/ Branch Staff is not helpful/ courteous')  ");
		db.execSQL("INSERT INTO "
				+ TABLE_DISSATISFACTION
				+ " ("
				+ COL_DISSATISFACTION_NAME
				+ " ) VALUES('Procedure defined for service is not okay and can be improved.')  ");
		db.execSQL("INSERT INTO " + TABLE_DISSATISFACTION + " ("
				+ COL_DISSATISFACTION_NAME
				+ " ) VALUES('Service is not delivered in time.')  ");
		db.execSQL("INSERT INTO " + TABLE_DISSATISFACTION + " ("
				+ COL_DISSATISFACTION_NAME
				+ " ) VALUES('Branch ambiance is not up to the mark.')  ");
		db.execSQL("INSERT INTO " + TABLE_DISSATISFACTION + " ("
				+ COL_DISSATISFACTION_NAME + " ) VALUES('Any Others.')  ");

	}// onCreate()

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS TABLE_PURPOSE");
		db.execSQL("DROP TABLE IF EXISTS TABLE_DISSATISFACTION");
		db.execSQL("DROP TABLE IF EXISTS TABLE_USER_MASTER");
		db.execSQL("DROP TABLE IF EXISTS TABLE_DISSATISFACTION_MASTER");
		db.execSQL("DROP TABLE IF EXISTS TABLE_DISSATISFACTION_COMMENTS_MASTER");

	}// onUpdrade()

	public void createConnection() {
		db = this.getWritableDatabase();
	}// createConnection()

}// class
