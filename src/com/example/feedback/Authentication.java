package com.example.feedback;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.*;
import com.example.blc.*;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;

public class Authentication extends Activity {

	AuthenticationModel amo;
	AuthenticationMaster am = new AuthenticationMaster();
	String policy_number;
	DBHelper db = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		db.createConnection();

		// try {
		// Cursor res = db.getDummyData();
		// res.moveToFirst();
		// String str = "";
		// while (res.isAfterLast() == false) {
		// str = res.getInt(0) + "  " + res.getString(1) + "   "
		// + res.getString(2) + "   " + res.getString(3) + "   "
		// + res.getString(4) + "   " + res.getString(5) + "   "
		// + res.getString(6) + "   " + res.getString(7) + "   "
		// + res.getString(8) + "   " + res.getString(9) + "   "
		// + res.getString(10) + "   " + res.getInt(11);
		// res.moveToNext();
		// Toast.makeText(this, str, Toast.LENGTH_LONG).show();
		// }
		// } catch (Exception e) {
		// Log.e("Class Authentication Data Retrival", e.getStackTrace()
		// .toString());
		// }

		Button validate = (Button) findViewById(R.id.button_authentication_validate);
		db.createConnection();
		validate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText text_mobile = (EditText) findViewById(R.id.editText_authentication_mobile);
				EditText text_policy = (EditText) findViewById(R.id.editText_authentication_policy);

				String mobile_number = text_mobile.getText().toString();
				policy_number = text_policy.getText().toString();

				amo = new AuthenticationModel(mobile_number, policy_number);

				if (checkCredentials(mobile_number, policy_number)) {

					if (!checkNull(mobile_number)
							&& checkDigitsCount(mobile_number, 1)) {

						if (!am.checkMobileNumber(amo)) {
							invalidCredentials();
						} else {
							if (!am.validatePolicyNumber()) {
								Toast.makeText(Authentication.this,
										"Validation Successfull!",
										Toast.LENGTH_SHORT).show();
								Intent regUser = new Intent(
										Authentication.this, RegisterUser.class);
								startActivity(regUser);
							} else {
								Toast.makeText(Authentication.this,
										"Validation Successfull!",
										Toast.LENGTH_SHORT).show();
								Intent fedtype = new Intent(
										Authentication.this, FeedbackType.class);
								startActivity(fedtype);
							}

						}// if-else

					} else if (!checkNull(policy_number)
							&& checkDigitsCount(policy_number, 2)) {

						if (!am.checkPolicyNumber(amo)) {
							invalidCredentials();
						} else {
							if (!am.validateMobileNumber()) {
								Toast.makeText(Authentication.this,
										"Validation Successfull!",
										Toast.LENGTH_SHORT).show();
								Intent rcrdmob = new Intent(
										Authentication.this, RecordMobile.class);
								startActivity(rcrdmob);
							} else {
								AsyncCustomerValidation acv = new AsyncCustomerValidation();
								acv.execute();

							}
						}// if-else

					}// IF-ELSE-IF
				}

			}// onClick()
		});// setOnclickLister()

	}// onCreate()

	// ASYNC CLASS FOR Customer Validation
	private class AsyncCustomerValidation extends
			AsyncTask<Void, String, String> {

		private final String SOAP_ACTION_CheckPolicyNo_cust = "http://tempuri.org/CheckPolicyNo_cust";
		private final String NAMESPACE = "http://tempuri.org/";
		private final String URL = "http://125.18.9.109:84/Service.asmx?wsdl";
		private final String FUNCTION_NAME = "CheckPolicyNo_cust";
		ProgressDialog custValProgDiag;
		String inputcustlist;

		@Override
		protected String doInBackground(Void... params) {

			String strAuthUserErrorCode;
			String nm = "empty", mob = "empty", pol = "empty", dob = "empty";

			try {
				SoapObject request = new SoapObject(NAMESPACE, FUNCTION_NAME);

				request.addProperty("strPolicyNo", amo.getPolicy_number());

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

					androidHTTPTransport.call(SOAP_ACTION_CheckPolicyNo_cust,
							envelope);

					SoapPrimitive sa = null;

					try {
						sa = (SoapPrimitive) envelope.getResponse();

						inputcustlist = sa.toString();

						ParseXML prsObj = new ParseXML();
						inputcustlist = prsObj.parseXmlTag(inputcustlist,
								"PolicyDetails");

						inputcustlist = prsObj.parseXmlTag(inputcustlist,
								"ScreenData");
						strAuthUserErrorCode = inputcustlist;

						// if no ErrCode
						if (strAuthUserErrorCode == null) {
							inputcustlist = sa.toString();
							inputcustlist = prsObj.parseXmlTag(inputcustlist,
									"PolicyDetails");
							inputcustlist = prsObj.parseXmlTag(inputcustlist,
									"Table");
							mob = prsObj
									.parseXmlTag(inputcustlist, "PR_MOBILE");
							pol = prsObj.parseXmlTag(inputcustlist,
									"PL_POL_NUM");
							dob = prsObj.parseXmlTag(inputcustlist, "PR_DOB");

							nm = prsObj
									.parseXmlTag(inputcustlist, "PR_FULL_NM");

							new RecordMobileModel(pol, dob, "", mob, "");
							new RegisterUserModel(nm, mob);

							// Check IF mobile Number is registered
							if (mob != null) {
								return "1";
							} else if (mob == null) {
								return "2";
							}
						} else {
							strAuthUserErrorCode = prsObj.parseXmlTag(
									inputcustlist, "ErrCode");
							if (strAuthUserErrorCode.equals("1")) {
								invalidCredentials();
							} else {

								Log.e("Class AsyncEmployeeLogin, inside inner catch",
										"Something went wrong!!!Please try again...");

							}// if-else

						}// outer-ifelse

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

			return "0";

		}// doInBackground()

		@Override
		protected void onPostExecute(String result) {

			custValProgDiag.dismiss();

			if (result.equals("1")) {
				Intent sel = new Intent(Authentication.this, FeedbackType.class);
				startActivity(sel);
			} else if (result.equals("0")) {
				Toast.makeText(Authentication.this, "Invalid Credentials!",
						Toast.LENGTH_LONG).show();
			} else if (result.equals("2")) {
				Intent sel = new Intent(Authentication.this, RecordMobile.class);
				startActivity(sel);
			}// if else
		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			custValProgDiag = new ProgressDialog(Authentication.this);

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

	public Boolean checkDigitsCount(String num, int stat) {
		String regex_mob = "^[0-9]{10}$", regex_pol = "^[a-zA-Z0-9]{11}$";
		if (stat == 1) {
			if (!num.matches(regex_mob)) {
				Toast.makeText(this, "Please Enter a valid Mobile Number.",
						Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		} else if (stat == 2) {
			if (!num.matches(regex_pol)) {
				Toast.makeText(this, "Please Enter a valid Policy Number.",
						Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		return false;

	}// checkDigitsCount()

	public void invalidCredentials() {
		Toast.makeText(Authentication.this,
				"Invalid Credentials. Please try again.", Toast.LENGTH_LONG)
				.show();
	}// invalidCredentials()

	public boolean checkCredentials(String mob, String pol) {
		try {
			if ((mob.isEmpty() && pol.isEmpty())
					|| (!mob.isEmpty() && !pol.isEmpty())) {

				Toast.makeText(Authentication.this,
						"Please Enter any one field.", Toast.LENGTH_SHORT)
						.show();

				return false;
			}
		} catch (Exception e) {
			Log.e("Class Authentication checkCredentials()", e.getStackTrace()
					.toString());
		}

		return true;
	}// checkCredentials()

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
