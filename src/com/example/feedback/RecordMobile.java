package com.example.feedback;

import java.util.Calendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.LogoutMaster;
import com.example.blc.ParseXML;
import com.example.blc.RecordMobileMaster;
import com.example.dataaccess.WebServiceContents;
import com.example.model.RecordMobileModel;
import com.example.model.RegisterUserModel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RecordMobile extends Activity {

	RecordMobileModel rmmo;
	RecordMobileMaster rmm;
	String mobile_number = "", email = "", dob = "", policy_number = "",
			pan = "", webServiceDate, nm;
	Dialog register;
	int day, month, yr;
	Calendar selectedDate = Calendar.getInstance();
	Button select_dob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_record_mobile);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_custom_titlebar);

		// Date Setting
		select_dob = (Button) findViewById(R.id.button_record_mobile_dob);
		select_dob.setText("SELECT DOB");
		select_dob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar newCal = Calendar.getInstance();

				DatePickerDialog.OnDateSetListener dpl = new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						day = dayOfMonth;
						month = monthOfYear;
						yr = year;
						dob = String.valueOf(day) + "/"
								+ String.valueOf(month + 1) + "/"
								+ String.valueOf(yr);
						selectedDate.set(year, monthOfYear, dayOfMonth);
						select_dob.setText("Selected DOB:  " + dob);
						webServiceDate = String.valueOf(month + 1) + "/"
								+ String.valueOf(day) + "/"
								+ String.valueOf(yr);

					}// onDateSet()
				};// OnDateSetListener()

				DatePickerDialog dpd = new DatePickerDialog(RecordMobile.this,
						dpl, newCal.get(Calendar.YEAR), newCal
								.get(Calendar.MONTH), newCal
								.get(Calendar.DAY_OF_MONTH));
				dpd.setTitle("Select DOB");
				dpd.show();

			}// onClick()
		});// OnClickListener()

		EditText edit_pan = (EditText) findViewById(R.id.editText_recorcd_mobile_pan);
		edit_pan.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				dob = "";
				select_dob.setText("Select DOB");

			}
		});
		edit_pan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dob = "";
				select_dob.setText("Select DOB");

			}// onClick()
		});// onClickListener()

		// Validate Called

		Button valButton = (Button) findViewById(R.id.button_record_mob_validate);
		valButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText edit_policy = (EditText) findViewById(R.id.editText_record_mobile_policy_number);
				EditText edit_pan = (EditText) findViewById(R.id.editText_recorcd_mobile_pan);

				policy_number = edit_policy.getText().toString();
				pan = edit_pan.getText().toString();

				rmmo = new RecordMobileModel(policy_number, webServiceDate,
						pan, mobile_number, email);
				rmm = new RecordMobileMaster();

				if (checkCredentials(pan, policy_number)
						&& validAge(selectedDate.get(Calendar.DAY_OF_MONTH),
								selectedDate.get(Calendar.MONTH),
								selectedDate.get(Calendar.YEAR))) {
					if (!rmm.validateMobile()) {
						invalidCredentials();
					} else {
						AsyncRecordMobile arm = new AsyncRecordMobile();
						arm.execute();

						/*
						 * Dialog Box register = new Dialog(RecordMobile.this);
						 * register.setContentView(R.layout.dialog_register);
						 * register.setTitle("Validation Successful!");
						 * register.setCancelable(false);
						 * register.setCanceledOnTouchOutside(false);
						 * register.show();
						 * 
						 * Button submitButton = (Button) register
						 * .findViewById(R.id.button_dialog_register_submit);
						 * submitButton .setOnClickListener(new
						 * View.OnClickListener() {
						 * 
						 * @Override public void onClick(View v) {
						 * 
						 * EditText edit_mob = (EditText) register
						 * .findViewById(R.id.editText_dialog_register_mob);
						 * EditText edit_email = (EditText) register
						 * .findViewById(R.id.editText_dialog_register_email);
						 * 
						 * mobile_number = edit_mob.getText() .toString(); email
						 * = edit_email.getText().toString(); policy_number =
						 * RecordMobileModel .getPolicy_number(); dob =
						 * RecordMobileModel.getDob(); pan =
						 * RecordMobileModel.getPan_number(); rmmo = new
						 * RecordMobileModel( policy_number, dob, pan,
						 * mobile_number, email); new RegisterUserModel(nm,
						 * mobile_number);
						 * 
						 * if (validateCredentials(mobile_number, email)) {
						 * Intent Othfedtype = new Intent( RecordMobile.this,
						 * OtherFeedbackType.class); startActivity(Othfedtype);
						 * }// IF
						 * 
						 * }// onClick() });// onClickListener()
						 */
					}// else
				}// outer-IF

			} // onClick
		});// setOnClickListener()

	}// onCreate()

	// ASYNC CLASS To Record Mobile Number and Email
	private class AsyncRecordMobile extends AsyncTask<Void, String, String> {

		private final String SOAP_ACTION_CheckPolicyNo_DOBorPAN_cust = "http://tempuri.org/CheckPolicyNo_DOBorPAN_cust";
		private final String NAMESPACE = "http://tempuri.org/";
		private final String URL = "http://125.18.9.109:84/Service.asmx?wsdl";
		private final String FUNCTION_NAME = "CheckPolicyNo_DOBorPAN_cust";
		ProgressDialog custValProgDiag;
		String validatecustlist;

		@Override
		protected String doInBackground(Void... params) {

			String strAuthUserErrorCode;

			try {
				SoapObject request = new SoapObject(NAMESPACE, FUNCTION_NAME);

				request.addProperty("strPolicyNo",
						RecordMobileModel.getPolicy_number());
				request.addProperty("strDOB", webServiceDate);
				request.addProperty("strPAN", RecordMobileModel.getPan_number());

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

					androidHTTPTransport.call(
							SOAP_ACTION_CheckPolicyNo_DOBorPAN_cust, envelope);

					SoapPrimitive sa = null;

					try {
						sa = (SoapPrimitive) envelope.getResponse();

						validatecustlist = sa.toString();

						ParseXML prsObj = new ParseXML();
						validatecustlist = prsObj.parseXmlTag(validatecustlist,
								"PolicyDetails");

						validatecustlist = prsObj.parseXmlTag(validatecustlist,
								"ScreenData");
						strAuthUserErrorCode = validatecustlist;

						// if no ErrCode
						if (strAuthUserErrorCode == null) {
							validatecustlist = sa.toString();
							validatecustlist = prsObj.parseXmlTag(
									validatecustlist, "PolicyDetails");
							validatecustlist = prsObj.parseXmlTag(
									validatecustlist, "Table");
							nm = prsObj.parseXmlTag(validatecustlist,
									"PR_FULL_NM");

							return "1";
						} else {
							strAuthUserErrorCode = prsObj.parseXmlTag(
									validatecustlist, "ErrCode");
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

				// Dialog Box
				register = new Dialog(RecordMobile.this);
				register.setContentView(R.layout.dialog_register);
				register.setTitle("Validation Successful!");
				register.setCancelable(false);
				register.setCanceledOnTouchOutside(false);
				register.show();

				Button submitButton = (Button) register
						.findViewById(R.id.button_dialog_register_submit);
				submitButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						EditText edit_mob = (EditText) register
								.findViewById(R.id.editText_dialog_register_mob);
						EditText edit_email = (EditText) register
								.findViewById(R.id.editText_dialog_register_email);

						mobile_number = edit_mob.getText().toString();
						email = edit_email.getText().toString();
						// Toast.makeText(
						// RecordMobile.this,
						// RecordMobileModel
						// .getPolicy_number()
						// + "hurray",
						// Toast.LENGTH_LONG).show();
						policy_number = RecordMobileModel.getPolicy_number();
						dob = RecordMobileModel.getDob();
						pan = RecordMobileModel.getPan_number();
						rmmo = new RecordMobileModel(policy_number, dob, pan,
								mobile_number, email);
						new RegisterUserModel(nm, mobile_number);

						if (validateCredentials(mobile_number, email)) {
							Intent Othfedtype = new Intent(RecordMobile.this,
									OtherFeedbackType.class);
							startActivity(Othfedtype);
						}// IF

					}// onClick()
				});// onClickListener()
			} else {
				Toast.makeText(RecordMobile.this, "Invalid Credentials!",
						Toast.LENGTH_LONG).show();
			}// if-else
		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			custValProgDiag = new ProgressDialog(RecordMobile.this);

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

	public Boolean validAge(int day, int month, int year) {
		try {
			Calendar currDate = Calendar.getInstance();
			int y, m, d, age;
			y = currDate.get(Calendar.YEAR);
			m = currDate.get(Calendar.MONTH);
			d = currDate.get(Calendar.DAY_OF_MONTH);

			age = y - year;
			if (m < month || ((m == month) && (d < day))) {
				age--;
			}

			if (age >= 18) {
				return true;
			} else {
				Toast.makeText(RecordMobile.this,
						"Age must be greater than 18 years.",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (Exception e) {
			Log.e("Class RecordMobile validAge()", e.getStackTrace().toString());
		}// try-catch
		return false;

	}// validAge()

	public void invalidCredentials() {
		Toast.makeText(RecordMobile.this,
				"Invalid Credentials. Please try again.", Toast.LENGTH_SHORT)
				.show();
	}// invalidCredentials()

	public boolean checkCredentials(String pan, String pol) {
		String regex_policy = "^[a-zA-Z0-9]{11}$", regex_pan = "^[A-Z]{3}[P]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}$";

		// validate policy number
		if (checkNull(pol)) {
			Toast.makeText(RecordMobile.this,
					"Please enter your Policy Number.", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!pol.matches(regex_policy)) {
			Toast.makeText(RecordMobile.this,
					"Please enter a valid Policy Number.", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		try {
			// validate nulls of dob and pan
			if ((checkNull(dob) && checkNull(pan))
					|| (!checkNull(dob) && !checkNull(pan))) {

				Toast.makeText(RecordMobile.this, "Please enter DOB or PAN.",
						Toast.LENGTH_SHORT).show();
				return false;
			} else if (checkNull(pol) && checkNull(pan) && checkNull(dob)) {
				Toast.makeText(RecordMobile.this,
						"Please enter Policy Number and Date of Birth/ PAN.",
						Toast.LENGTH_SHORT).show();
				return false;
			}

			if (checkNull(dob) && !checkNull(pan)) {
				// Pan Validation
				if (!pan.matches(regex_pan)) {
					Toast.makeText(RecordMobile.this,
							"Please enter a PAN Number.", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
			} else if (!checkNull(dob) && checkNull(pan)) {

				// date validation
				Calendar currentDate = Calendar.getInstance();
				if (currentDate.before(selectedDate)) {
					Toast.makeText(RecordMobile.this,
							"Please enter a valid Birth Date.",
							Toast.LENGTH_SHORT).show();
					return false;
				}// if-else

			}// else-if
		} catch (Exception e) {
			Log.e("Class RecordMobile checkCredentials()", e.getStackTrace()
					.toString());
		}// try-catch
		return true;
	}// checkCredentials()

	public boolean validateCredentials(String mob, String email) {
		String regex_mob = "^[0-9]{10}$";

		if (checkNull(mob)) {
			Toast.makeText(RecordMobile.this,
					"Please enter Mobile Number to Register.",
					Toast.LENGTH_SHORT).show();
			return false;

		} else if (checkNull(email)) {
			Toast.makeText(RecordMobile.this,
					"Please enter Email ID to Register.", Toast.LENGTH_SHORT)
					.show();
			return false;

		} else if (checkNull(mob) && checkNull(email)) {

			Toast.makeText(RecordMobile.this,
					"Please enter Email ID and Mobile Number to Register.",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!mob.matches(regex_mob)) {
			Toast.makeText(RecordMobile.this,
					"Please enter a valid Mobile Number.", Toast.LENGTH_SHORT)
					.show();
			return false;

		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
				.matches()) {
			Toast.makeText(RecordMobile.this, "Please enter a valid Email ID.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}// validateCredentials()

	public boolean checkNull(String number) {
		try {
			if (number.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			Log.e("Class RecordMobile checkNull()", e.getStackTrace()
					.toString());
		}
		return false;
	}// checkNull()

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class
