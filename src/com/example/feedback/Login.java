package com.example.feedback;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.ParseXML;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.feedback.R.layout;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	DBHelper db = new DBHelper(this);

	LoginModel lmo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_custom_titlebar);

		Button validate = (Button) findViewById(R.id.button_login_validate);
		validate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText text_login = (EditText) findViewById(R.id.editText_login_emp_id);
				EditText text_password = (EditText) findViewById(R.id.editText_login_password);

				String emp_id = text_login.getText().toString();
				String password = text_password.getText().toString();

				if (!checkNull(emp_id) && !checkNull(password)) {
					lmo = new LoginModel(emp_id.toUpperCase(), password);
					AsyncEmployeeLogin ael = new AsyncEmployeeLogin();
					ael.execute();
					// Intent sel = new Intent(Login.this,
					// OptionSelector.class);
					// startActivity(sel);

				} else {
					valMsg("Please enter the credentials!");
				}
			}// onClick()
		});// setOnclickLister()
	}// onCreate()

	// ASYNC CLASS FOR EMPLOYEE LOGIN
	private class AsyncEmployeeLogin extends AsyncTask<Void, String, String> {

		private static final String SOAP_ACTION_AUTHAGENT = "http://tempuri.org/authAgent";
		private final String NAMESPACE = "http://tempuri.org/";
		private final String URL = "http://125.18.9.109:84/Service.asmx?wsdl";
		private final String FUNCTION_NAME = "authAgent";
		ProgressDialog loginProgDiag;
		String loginemplist, msg;

		@Override
		protected String doInBackground(Void... params) {

			String strAuthUserErrorCode;

			try {
				SoapObject request = new SoapObject(NAMESPACE, FUNCTION_NAME);

				request.addProperty("userId", LoginModel.getEmp_id()
						.toUpperCase());
				request.addProperty("userPwd", lmo.getEmp_password());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				WebServiceContents.allowAllSSL();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHTTPTransport = new HttpTransportSE(URL);

				try {

					androidHTTPTransport.call(SOAP_ACTION_AUTHAGENT, envelope);

					SoapPrimitive sa = null;

					try {
						sa = (SoapPrimitive) envelope.getResponse();

						loginemplist = sa.toString();

						ParseXML prsObj = new ParseXML();
						loginemplist = prsObj.parseXmlTag(loginemplist,
								"CustomerDetails");
						loginemplist = prsObj.parseXmlTag(loginemplist,
								"ScreenData");
						strAuthUserErrorCode = prsObj.parseXmlTag(loginemplist,
								"ErrCode");
						msg = prsObj.parseXmlTag(loginemplist, "ErrorMsg");

						// ErrCode 0=success,1=fail
						if (strAuthUserErrorCode.contentEquals("1")) {
							return "1";
						} else if (strAuthUserErrorCode.contentEquals("0")) {
							return "0";
						} else {
							Log.e("Class AsyncEmployeeLogin, inside inner catch",
									"Something went wrong!!!Please try again...");
						}// if-else

					} catch (Exception e) {
						Log.e("Class AsyncEmployeeLogin, inside inner catch", e
								.getStackTrace().toString());
					}// try-catch inner

				} catch (Exception e) {
					Log.e("Class AsyncEmployeeLogin, inside catch", e
							.getStackTrace().toString());
				}// try-catch
			} catch (Exception e) {
				Log.e("Class AsyncEmployeeLogin, Main Try-Catch", e
						.getStackTrace().toString());
			}// main try-catch

			return "1";
		}// doInBackground()

		@Override
		protected void onPostExecute(String result) {
			loginProgDiag.dismiss();
			if (result.equals("0")) {
				valMsg(msg);
				Intent sel = new Intent(Login.this, OptionSelector.class);
				startActivity(sel);
			} else if (result.equals("1")) {
				valMsg(msg);
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

	public void valMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}// errMsg()

	public boolean checkNull(String number) {
		try {
			if (number.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			Log.e("Class Authentication checkNull()", e.getStackTrace()
					.toString());
		}
		return false;
	}

}// class

