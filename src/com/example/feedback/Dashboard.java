package com.example.feedback;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.FeedbackDashboardAdapter;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.DashboardModel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Dashboard extends Activity {

	DBHelper db = new DBHelper(this);
	Button syncStat;
	ListView dashLV;
	List<DashboardModel> dataList;
	ArrayList<String> custIds = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dashboard);

		/*
		 * db.createConnection();
		 * 
		 * Cursor res = db.getDashboardData();
		 * 
		 * while (res.moveToNext()) { Toast.makeText( this, "id:" +
		 * res.getString(0) + " mob:" + res.getString(1) + " pol:" +
		 * res.getString(2) + " em:" + res.getString(3) + " name:" +
		 * res.getString(4) + " sync:" + res.getInt(5), Toast.LENGTH_LONG)
		 * .show();
		 * 
		 * }// while
		 */
		// Toast.makeText(getBaseContext(), "Dont kw y",
		// Toast.LENGTH_LONG).show();

		dashLV = (ListView) findViewById(R.id.listView_dashboard_main);
		addDataToList();
		FeedbackDashboardAdapter fda = new FeedbackDashboardAdapter(this, 0,
				dataList);
		fda.setNotifyOnChange(true);
		dashLV.setAdapter(fda);

		registerForContextMenu(dashLV); // dashLV.setContextClickable(true);

	}// onCreate()

	/*
	 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
	 * ContextMenuInfo menuInfo) {
	 * 
	 * super.onCreateContextMenu(menu, v, menuInfo);
	 * 
	 * AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	 * 
	 * Toast.makeText(getApplicationContext(), "" +
	 * dashLV.getItemAtPosition(info.position), Toast.LENGTH_LONG) .show();
	 * 
	 * if (v.getId() == R.id.listView_dashboard_main) {
	 * menu.setHeaderTitle("Sync this data..."); menu.add(0, v.getId(), 0,
	 * "YES"); menu.add(0, v.getId(), 0, "NO");
	 * Toast.makeText(getApplicationContext(), "Not adirst",
	 * Toast.LENGTH_LONG).show(); }// if
	 * 
	 * }// onCreateContextMenu()
	 * 
	 * @Override public boolean onContextItemSelected(MenuItem item) {
	 * 
	 * Toast.makeText(getApplicationContext(), "Not asdasv Syncing",
	 * Toast.LENGTH_LONG).show(); if (item.getTitle() == "YES") {
	 * Toast.makeText(getApplicationContext(), "Trying to Sync",
	 * Toast.LENGTH_LONG).show(); } else if (item.getTitle() == "NO") {
	 * Toast.makeText(getApplicationContext(), "Not Syncing",
	 * Toast.LENGTH_LONG).show(); } else { return false; } return true; }//
	 * onContextItemSelected()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDataToList() {
		db.createConnection();

		Cursor res = db.getDashboardData();
		DashboardModel dbm;
		dataList = new ArrayList();
		dataList.clear();

		while (res.moveToNext()) {
			dbm = new DashboardModel(res.getString(0), res.getString(1),
					res.getString(2), res.getString(3), res.getString(4),
					res.getString(5));
			custIds.add(res.getString(0));
			dataList.add(dbm);
		}// while
	}// addDataToList

	// ASYNC CLASS FOR To Sync All Data
	@SuppressWarnings("unused")
	private class AsyncSyncAllData extends AsyncTask<String, String, String> {

		private final String SOAP_ACTION_URL = "";
		private final String NAMESPACE = "http://tempuri.org";
		private final String SOAP_ACTION_FUNCTION_NAME = "";
		ProgressDialog custValProgDiag;

		@Override
		protected String doInBackground(String... params) {
			String responseStatus = "";
			try {
				SoapObject request = new SoapObject(NAMESPACE,
						SOAP_ACTION_FUNCTION_NAME);

				// request.addProperty("empID", lmo.getEmp_id());
				// request.addProperty("empPwd", lmo.getEmp_password());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				WebServiceContents.allowAllSSL();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHTTPTransport = new HttpTransportSE(
						SOAP_ACTION_URL);

				try {
					androidHTTPTransport.call(SOAP_ACTION_URL, envelope);
					responseStatus = envelope.getResponse().toString();

				} catch (Exception e) {
					Log.e("Class AsyncEmployeeLogin, inside catch", e
							.getStackTrace().toString());
				}// try-catch
			} catch (Exception e) {
				Log.e("Class AsyncEmployeeLogin, Main Try-Catch", e
						.getStackTrace().toString());
			}// main try-catch

			return responseStatus;
		}// doInBackground()

		@Override
		protected void onPostExecute(String result) {

			if (result.equals("1")) {
				Intent sel = new Intent(Dashboard.this, OptionSelector.class);
				startActivity(sel);
			} else {
				Toast.makeText(Dashboard.this, "Invalid Credentials!",
						Toast.LENGTH_SHORT).show();
			}// if-else
		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			custValProgDiag = new ProgressDialog(Dashboard.this);

			String msg = "Please Wait while we check your credentials...";

			custValProgDiag.setMessage(msg);
			custValProgDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			custValProgDiag.setCancelable(false);
			custValProgDiag.setCanceledOnTouchOutside(false);

			custValProgDiag.setButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							custValProgDiag.dismiss();
						}// onClick()
					});// setOnClickListener()

			custValProgDiag.setMax(100);
			custValProgDiag.show();

		}// onPreExecute()

	}// ASYNC class

}// class
