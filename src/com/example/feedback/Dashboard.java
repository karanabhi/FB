package com.example.feedback;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.FeedbackDashboardAdapter;
import com.example.blc.LogoutMaster;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.DashboardModel;
import com.example.model.RatingsModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Dashboard extends Activity {

	DBHelper db = new DBHelper(this);
	Button syncStat;
	ListView dashLV = null;
	List<DashboardModel> dataList;
	ArrayList<Integer> custIds = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dashboard);

		db.createConnection();

		// Cursor res = db.getDashboardData();
		//
		// while (res.moveToNext()) { Toast.makeText( this, "id:" +
		// res.getString(0) + " mob:" + res.getString(1) + " pol:" +
		// res.getString(2) + " em:" + res.getString(3) + " name:" +
		// res.getString(4) + " sync:" + res.getInt(5), Toast.LENGTH_LONG)
		// .show();
		//
		// }// while

		// Toast.makeText(getBaseContext(), "asdas: " + LoginModel.getEmp_id(),
		// Toast.LENGTH_LONG).show();

		dashLV = (ListView) findViewById(R.id.listView_dashboard_main);
		addDataToList();
		FeedbackDashboardAdapter fda = new FeedbackDashboardAdapter(this, 0,
				dataList);

		dashLV.setAdapter(fda);

		registerForContextMenu(dashLV); // dashLV.setContextClickable(true);

		// SyncAll Button
		Button syncAllBtn = (Button) findViewById(R.id.button_dashboard_syncAll);
		syncAllBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (dataList.isEmpty()) {
					Toast.makeText(getBaseContext(), "No Records to Sync!",
							Toast.LENGTH_LONG).show();

				} else {

					Cursor res = db.checkSyncStatus();
					Log.e("cnt:" + res.getCount(), "count status");

					if (res.getCount() == 0) {
						Toast.makeText(getBaseContext(),
								"All Records are Synced!", Toast.LENGTH_LONG)
								.show();
					} else {

						custIds.clear();

						while (res.moveToNext()) {
							custIds.add(res.getInt(0));
						}// while

						if (!custIds.isEmpty()) {
							if (checkConnection()) {

								AsyncSyncAllData asad = new AsyncSyncAllData(
										custIds);
								asad.execute();
							} else {
								Toast.makeText(getBaseContext(),
										"No Internet Connection Found!",
										Toast.LENGTH_LONG).show();
							}// checkConnection
						} else {
							Toast.makeText(getBaseContext(),
									"All Records not Synced!",
									Toast.LENGTH_LONG).show();
						}// inner if-else

					}// if-else
				}// outer if-else

			}// onClick()
		});// setOnclickListener()

	}// onCreate()

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
			custIds.add(res.getInt(0));
			dataList.add(dbm);
		}// while
	}// addDataToList

	// ASYNC CLASS FOR To Sync All Data
	private class AsyncSyncAllData extends AsyncTask<Void, String, String> {

		private static final String SOAP_ACTION_SAVECUSTOMERFEEDBACK = "http://tempuri.org/saveCustomerFeedback";
		private final String NAMESPACE = "http://tempuri.org/";
		private final String URL = "http://125.18.9.109:84/Service.asmx?wsdl";
		private final String FUNCTION_NAME = "saveCustomerFeedback";
		private ArrayList<Integer> cids;
		ProgressDialog syncProgDiag;

		public AsyncSyncAllData(ArrayList<Integer> cid) {
			cids = cid;
		}

		@Override
		protected String doInBackground(Void... params) {
			String responseStatus = "";
			for (int cid : cids) {

				try {

					RatingsModel.setCust_id(cid);

					Cursor res = db.getDataForSyncing();
					res.moveToFirst();
					SoapObject request = new SoapObject(NAMESPACE,
							FUNCTION_NAME);
					request.addProperty("USER_ID", res.getString(0));
					request.addProperty("CUST_MOBILENO", res.getString(1));
					request.addProperty("CUST_POLICYNO", res.getString(2));
					request.addProperty("CUST_EMAILID", res.getString(3));
					request.addProperty("CUST_FULL_NAME", res.getString(4));
					request.addProperty("CUST_PANCARDNO", res.getString(5));
					request.addProperty("CUST_DOB", res.getString(6));
					request.addProperty("CUST_PURPOSE_OF_VISIT",
							res.getString(7));
					request.addProperty("CUST_APPS_RATING", res.getString(8));
					request.addProperty("CUST_APPS_RATING_COMMENTS",
							res.getString(9));

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);

					WebServiceContents.allowAllSSL();

					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					HttpTransportSE androidHTTPTransport = new HttpTransportSE(
							URL);

					try {
						androidHTTPTransport.call(
								SOAP_ACTION_SAVECUSTOMERFEEDBACK, envelope);
						responseStatus = envelope.getResponse().toString();

						RatingsModel.setCust_id(cid);
						if (responseStatus.equals("1")) {
							db.updateSyncStatus();
						} else {
							return "2";
						}
					} catch (Exception e) {
						Log.e("Class ASYNCInsertFeedback, inside catch", e
								.getStackTrace().toString());
					}// try-catch
				} catch (Exception e) {
					Log.e("Class ASYNCInsertFeedback, Main Try-Catch", e
							.getStackTrace().toString());
				}// main try-catch

			}// for

			return "1";

		}// doInBackground()

		@Override
		protected void onPostExecute(String result) {

			syncProgDiag.dismiss();
			if (result.equals("1")) {

				dashLV = null;
				dataList.clear();
				dashLV = (ListView) findViewById(R.id.listView_dashboard_main);
				addDataToList();
				FeedbackDashboardAdapter fda = new FeedbackDashboardAdapter(
						getBaseContext(), 0, dataList);
				dashLV.setAdapter(fda);
				registerForContextMenu(dashLV);
				Toast.makeText(getBaseContext(), "All Records are Synced!",
						Toast.LENGTH_LONG).show();
			} else if (result.equals("2")) {
				Toast.makeText(getBaseContext(),
						"There was problem in updating sync flag!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getBaseContext(),
						"There was some problem, please try again!",
						Toast.LENGTH_LONG).show();
			}// if-else if
		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			syncProgDiag = new ProgressDialog(Dashboard.this);

			String msg = "Please Wait while the data is syncing...";

			syncProgDiag.setMessage(msg);
			syncProgDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			syncProgDiag.setCancelable(false);
			syncProgDiag.setCanceledOnTouchOutside(false);

			syncProgDiag.setButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							syncProgDiag.dismiss();
						}// onClick()
					});// setOnClickListener()

			syncProgDiag.setMax(100);
			syncProgDiag.show();

		}// onPreExecute()

	}// ASYNC class

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

	public boolean checkConnection() {

		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}// checkConnection()

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

}// class
