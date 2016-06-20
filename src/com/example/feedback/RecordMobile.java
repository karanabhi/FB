package com.example.feedback;

import java.util.Calendar;

import com.example.blc.RecordMobileMaster;
import com.example.model.RecordMobileModel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RecordMobile extends Activity {

	RecordMobileModel rmmo;
	RecordMobileMaster rmm;
	String mobile_number = "", email = "", dob = "";
	Dialog register;
	int day, month, yr;
	Calendar selectedDate = Calendar.getInstance();
	Button select_dob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_mobile);

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
						dob = String.valueOf(day) + "-" + String.valueOf(month)
								+ "-" + String.valueOf(yr);
						selectedDate.set(year, monthOfYear, dayOfMonth);
						select_dob.setText("Selected DOB:  " + dob);

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

				String policy_number = edit_policy.getText().toString();
				String pan = edit_pan.getText().toString();

				rmmo = new RecordMobileModel(policy_number, dob, pan,
						mobile_number, email);
				rmm = new RecordMobileMaster();

				if (checkCredentials(pan, policy_number)
						&& validAge(selectedDate.get(Calendar.DAY_OF_MONTH),
								selectedDate.get(Calendar.MONTH),
								selectedDate.get(Calendar.YEAR))) {
					if (!rmm.validateMobile()) {
						invalidCredentials();
					} else {

						// Dialog Box
						register = new Dialog(RecordMobile.this);
						register.setContentView(R.layout.dialog_register);
						register.setTitle("Validation Successful!");

						register.show();

						Button submitButton = (Button) register
								.findViewById(R.id.button_dialog_register_submit);
						submitButton
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {

										EditText edit_mob = (EditText) register
												.findViewById(R.id.editText_dialog_register_mob);
										EditText edit_email = (EditText) register
												.findViewById(R.id.editText_dialog_register_email);

										mobile_number = edit_mob.getText()
												.toString();
										email = edit_email.getText().toString();

										if (validateCredentials(mobile_number,
												email)) {
											Intent fedtype = new Intent(
													RecordMobile.this,
													FeedbackType.class);
											startActivity(fedtype);
										}// IF

									}// onClick()
								});// onClickListener()

					}// else
				}// outer-IF

			} // onClick
		});// setOnClickListener()

	}// onCreate()

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

		String textView_date = "";

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
	}

}// class
