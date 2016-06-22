package com.example.feedback;

import java.util.ArrayList;
import java.util.List;

import com.example.dataaccess.DBHelper;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Dashboard extends Activity {

	DBHelper db = new DBHelper(this);
	RelativeLayout dashRL;
	GridView dashGV;
	List<String> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		dashRL = (RelativeLayout) findViewById(R.id.layout_relative_dashboard);
		dashGV = (GridView) findViewById(R.id.gridView_dashboard_main);
		dataList = new ArrayList<String>();

		db.createConnection();

		dataList.add("Customer ID");
		dataList.add("Mobile Number");
		dataList.add("Policy Number");
		dataList.add("Name");
		dataList.add("Email");
		dataList.add("Sync Status");

		Cursor res = db.getDashboardData();

		while (res.moveToNext()) {

			dataList.add(res.getString(0));
			dataList.add(res.getString(1));
			dataList.add(res.getString(2));
			dataList.add(res.getString(3));
			dataList.add(res.getString(4));
			dataList.add("" + res.getInt(5));

			// Toast.makeText(
			// this,
			// "id:" + res.getString(0) + " mob:" + res.getString(1)
			// + " pol:" + res.getString(2) + " em:"
			// + res.getString(3) + " name:" + res.getString(4)
			// + " sync:" + res.getInt(5), Toast.LENGTH_LONG)
			// .show();

		}// while
		db.close();

		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, dataList);
		dashGV.setBackgroundColor(Color.BLUE);
		dashGV.setAdapter(adp);

	}// onCreate()
}// class
