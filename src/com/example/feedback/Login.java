package com.example.feedback;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.LoginModel;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	DBHelper db = new DBHelper(this);

	LoginModel lmo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button validate = (Button) findViewById(R.id.button_login_validate);
		validate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText text_login = (EditText) findViewById(R.id.editText_login_emp_id);
				EditText text_password = (EditText) findViewById(R.id.editText_login_password);

				String emp_id = text_login.getText().toString();
				String password = text_password.getText().toString();

				lmo = new LoginModel(emp_id, password);

				if (emp_id.equals("admin") && password.equals("admin")) {
					Intent sel = new Intent(Login.this, OptionSelector.class);
					startActivity(sel);
				} else {
					Toast.makeText(Login.this, "Invalid Credentials!",
							Toast.LENGTH_SHORT).show();
				}// if-else

				// AsyncEmployeeLogin ael = new AsyncEmployeeLogin();
				// ael.execute();

			}// onClick()
		});// setOnclickLister()

	}// onCreate()

	// ASYNC CLASS FOR EMPLOYEE LOGIN
	private class AsyncEmployeeLogin extends AsyncTask<String, String, String> {

		private final String SOAP_ACTION_URL = "";
		private final String NAMESPACE = "http://tempuri.org";
		private final String SOAP_ACTION_FUNCTION_NAME = "";
		ProgressDialog loginProgDiag;

		@Override
		protected String doInBackground(String... params) {
			String responseStatus = "";
			try {
				SoapObject request = new SoapObject(NAMESPACE,
						SOAP_ACTION_FUNCTION_NAME);

				request.addProperty("empID", lmo.getEmp_id());
				request.addProperty("empPwd", lmo.getEmp_password());

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
				Intent sel = new Intent(Login.this, OptionSelector.class);
				startActivity(sel);
			} else {
				Toast.makeText(Login.this, "Invalid Credentials!",
						Toast.LENGTH_SHORT).show();
			}// if-else
		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			loginProgDiag = new ProgressDialog(Login.this);

			String msg = "Please Wait while we check your credentials...";

			loginProgDiag.setMessage(msg);
			loginProgDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			loginProgDiag.setCancelable(false);
			loginProgDiag.setCanceledOnTouchOutside(false);

			loginProgDiag.setButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							loginProgDiag.dismiss();
						}// onClick()
					});// setOnClickListener()

			loginProgDiag.setMax(100);
			loginProgDiag.show();

		}// onPreExecute()

	}// ASYNC class

}// class

