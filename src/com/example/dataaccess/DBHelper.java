package com.example.dataaccess;

import com.example.model.LoginModel;
import com.example.model.RatingsModel;
import com.example.model.RecordMobileModel;
import com.example.model.RegisterUserModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "feedback";
	private static final String TABLE_USER_MASTER = "user_master";
	private static final String COL_ID = "id";
	private static final String COL_USER_ID = "user_id";
	private static final String COL_USER_MOBILE_NO = "user_mobile_number";
	private static final String COL_USER_POLICY_NO = "user_policy_number";
	private static final String COL_USER_EMAIL = "user_email";
	private static final String COL_USER_NAME = "user_full_name";
	private static final String COL_USER_PAN = "user_pan_number";
	private static final String COL_USER_DOB = "user_dob";
	private static final String COL_USER_PURPOSE = "user_purpose_of_visit";
	private static final String COL_USER_RATING = "user_rating";
	private static final String COL_USER_RATING_COMMENTS = "user_rating_comments";
	private static final String COL_USER_STATUS = "syncstatus";
	private static final String COL_USER_DEL_FLAG = "delflag";
	private static final String COL_USER_CREATED_BY = "createdby";
	private static final String COL_USER_CREATED_DATE = "createddate";
	private static final String COL_USER_MODIFIED_BY = "modifiedby";
	private static final String COL_USER_MODIFIED_DATE = "modifydate";
	private static final String COL_USER_FLAG1 = "flag1";
	private static final String COL_USER_FLAG2 = "flag2";
	private static final String COL_USER_FLAG3 = "flag3";
	private static final String COL_USER_FLAG4 = "flag4";

	SQLiteDatabase db = null;
	Cursor res = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}// Constructor

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_USER_MASTER
					+ " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COL_USER_ID + " TEXT NOT NULL, " + COL_USER_MOBILE_NO
					+ " TEXT, " + COL_USER_POLICY_NO + " TEXT, "
					+ COL_USER_EMAIL + " TEXT, " + COL_USER_NAME + " TEXT, "
					+ COL_USER_PAN + " TEXT, " + COL_USER_DOB + " TEXT, "
					+ COL_USER_PURPOSE + " TEXT, " + COL_USER_RATING
					+ " TEXT, " + COL_USER_RATING_COMMENTS + " TEXT, "
					+ COL_USER_CREATED_BY + " TEXT," + COL_USER_CREATED_DATE
					+ " TEXT," + COL_USER_MODIFIED_BY + " TEXT,"
					+ COL_USER_MODIFIED_DATE + " TEXT," + COL_USER_STATUS
					+ " INTEGER DEFAULT 0, " + COL_USER_DEL_FLAG
					+ " INTEGER DEFAULT 0," + COL_USER_FLAG1 + "INTEGER,"
					+ COL_USER_FLAG2 + "INTEGER," + COL_USER_FLAG3 + "INTEGER,"
					+ COL_USER_FLAG4 + "INTEGER);  ");

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
					+ ","
					+ COL_USER_CREATED_DATE
					+ ","
					+ COL_USER_ID
					+ "  ) values('2222222222','123asdzxcqw','asd@asd.op','zxcqwe','AVD2PASS3E','12-12-2012','Claims','4','YOLO!','1',0,'06-17-2016','admin' );");
		} catch (Exception e) {
			Log.e("Class DBHelper onCreate(", e.getStackTrace().toString());
		}

	}// onCreate()

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_MASTER + "; ");
		} catch (Exception e) {
			Log.e("Class DBHelper onUpgrade()", e.getStackTrace().toString());
		}

	}// onUpdrade()

	public void createConnection() {
		db = this.getWritableDatabase();
	}// createConnection()

	public Boolean insertData() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_USER_ID, LoginModel.getEmp_id());
		contentValues.put(COL_USER_MOBILE_NO,
				RecordMobileModel.getMobile_number());
		contentValues.put(COL_USER_POLICY_NO,
				RecordMobileModel.getPolicy_number());
		contentValues.put(COL_USER_EMAIL, RecordMobileModel.getEmail());
		contentValues.put(COL_USER_NAME, RegisterUserModel.getName());
		contentValues.put(COL_USER_PAN, RecordMobileModel.getPan_number());
		contentValues.put(COL_USER_DOB, RecordMobileModel.getDob());
		contentValues.put(COL_USER_PURPOSE, RatingsModel.getPurpose());
		contentValues.put(COL_USER_RATING, RatingsModel.getRatings());
		contentValues.put(COL_USER_RATING_COMMENTS, RatingsModel.getComments());
		contentValues.put(COL_USER_STATUS, "0");
		contentValues.put(COL_USER_DEL_FLAG, "0");

		long id = 0;

		try {
			id = db.insert(TABLE_USER_MASTER, null, contentValues);
			if (id <= 1) {
				return false;
			}
		} catch (Exception e) {
			Log.e("Class DBHelper insertData()", e.getStackTrace().toString());
			return false;
		}// try-catch

		RatingsModel.setCust_id((int) id);

		return true;

	}// insertData()

	public Cursor getDummyData() {
		try {
			res = db.rawQuery("select * from user_master", null);
		} catch (Exception e) {
			Log.e("Class DBHelper getDummyData()", e.getStackTrace().toString());
		}// try-catch
		return res;
	}// getDummyData()

	public Cursor getDashboardData() {

		try {
			res = db.rawQuery("select " + COL_ID + "," + COL_USER_MOBILE_NO
					+ "," + COL_USER_POLICY_NO + "," + COL_USER_NAME + ","
					+ COL_USER_EMAIL + "," + COL_USER_STATUS + " FROM "
					+ TABLE_USER_MASTER + " where " + COL_USER_ID + "='"
					+ LoginModel.getEmp_id() + "' ", null);
		} catch (Exception e) {
			Log.e("Class DBHelper getDashboardData()", e.getStackTrace()
					.toString());
		}// try-catch

		return res;
	}// getDashboardData()

	public Boolean updateSyncStatus() {

		ContentValues contentValues = new ContentValues();

		contentValues.put(COL_USER_STATUS, 1);

		int numRows = db.update(TABLE_USER_MASTER, contentValues, COL_ID + "="
				+ RatingsModel.getCust_id(), null);

		if (numRows != 1) {
			return false;
		}
		return true;
	}// updateSyncStatus()

	public Cursor getDataForSyncing() {
		try {
			res = db.rawQuery("select " + COL_USER_ID + ","
					+ COL_USER_MOBILE_NO + "," + COL_USER_POLICY_NO + ","
					+ COL_USER_EMAIL + "," + COL_USER_NAME + "," + COL_USER_PAN
					+ "," + COL_USER_DOB + "," + COL_USER_PURPOSE + ","
					+ COL_USER_RATING + "," + COL_USER_RATING_COMMENTS + ","
					+ COL_USER_STATUS + "," + COL_USER_DEL_FLAG + ","
					+ COL_USER_CREATED_BY + "," + COL_USER_CREATED_DATE + ","
					+ COL_USER_MODIFIED_BY + "," + COL_USER_MODIFIED_DATE
					+ " FROM " + TABLE_USER_MASTER + " where " + COL_ID + "="
					+ RatingsModel.getCust_id() + " ", null);
		} catch (Exception e) {
			Log.e("Class DBHelper getDataForSyncing()", e.getStackTrace()
					.toString());
		}// try-catch

		return res;
	}// getDataForSyncing()

	public Cursor getDataFromCustId(int cid) {
		try {
			res = db.rawQuery("select " + COL_USER_MOBILE_NO + ","
					+ COL_USER_POLICY_NO + "," + COL_USER_EMAIL + ","
					+ COL_USER_NAME + "," + COL_USER_STATUS + " from "
					+ TABLE_USER_MASTER + " where " + COL_ID + "=" + cid + ";",
					null);
		} catch (Exception e) {
			Log.e("Class DBHelper getDataFromCustId()", e.getStackTrace()
					.toString());
		}// try-catch
		return res;
	}// getDataForCustId

}// class
