package com.example.feedback;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

		Cursor res = db.getDashboardData();

		while (res.moveToNext()) {

			dataList.add(res.getString(0));
			dataList.add(res.getString(1));
			dataList.add(res.getString(2));
			dataList.add(res.getString(3));
			dataList.add(res.getString(4));
			if (res.getString(5).equals("1")) {
				dataList.add("YES");
			} else {
				dataList.add("NO");
			}

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

	// ASYNC CLASS FOR To Sync All Data
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
