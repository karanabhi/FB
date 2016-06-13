package com.example.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "feedback";
	SQLiteDatabase db = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}// Constructor

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS ");

	}

}// class
