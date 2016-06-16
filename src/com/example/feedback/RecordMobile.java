package com.example.feedback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.blc.RecordMobileMaster;
import com.example.model.RecordMobileModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RecordMobile extends Activity {

	RecordMobileModel rmmo;
	RecordMobileMaster rmm;
	String mobile_number = "", email = "";
	Dialog register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_mobile);

		Button valButton = (Button) findViewById(R.id.button_record_mob_validate);
		valButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText edit_policy = (EditText) findViewById(R.id.editText_record_mobile_policy_numer);
				EditText edit_pan = (EditText) findViewById(R.id.editText_recorcd_mobile_pan);

				String policy_number = edit_policy.getText().toString();
				String pan = edit_pan.getText().toString();

				EditText edit_date = (EditText) findViewById(R.id.editText_record_mob_dob);

				String dob = edit_date.getText().toString();

				rmmo = new RecordMobileModel(policy_number, dob, pan,
						mobile_number, email);
				rmm = new RecordMobileMaster();

				if (checkCredentials(pan, policy_number, dob)) {
					if (!rmm.validateMobile()) {
						invalidCredentials();
					} else {

						// Dialog Box
						register = new Dialog(RecordMobile.this);
						register.setContentView(R.layout.dialog_register);
						register.setTitle("Validation Successfull!");

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

			}// onClick()
		});// OnClickListener()

	}// onCreate()

	public void invalidCredentials() {
		Toast.makeText(RecordMobile.this,
				"Invalid Credentials. Please try again.", Toast.LENGTH_SHORT)
				.show();
	}// invalidCredentials()

	public boolean checkCredentials(String pan, String pol, String dob) {
		String regex_policy = "^[a-zA-Z0-9]{11}$", regex_pan = "^[A-Z]{3}[P]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}$";

		int day = 01, month = 01, year = 1990;
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
				Toast.makeText(RecordMobile.this, "Please enter a PAN Number.",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} else if (!checkNull(dob) && checkNull(pan)) {

			// date validation
			if (dob.length() != 10) {
				Toast.makeText(RecordMobile.this,
						"Please enter a valid Birth Date.", Toast.LENGTH_SHORT)
						.show();
				return false;
			} else {
				day = Integer.parseInt((dob.charAt(0) + "" + dob.charAt(1)));
				month = Integer.parseInt((dob.charAt(3) + "" + dob.charAt(4)));
				year = Integer.parseInt((dob.charAt(6) + "" + dob.charAt(7)
						+ "" + dob.charAt(8) + "" + dob.charAt(9)));
				textView_date = day + "-" + month + "-" + year;

				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String currentDate = sdf.format(new Date());
				Date curdate;

				try {
					Date givenDate = sdf.parse(textView_date);
					curdate = sdf.parse(currentDate);
					if (curdate.before(givenDate)) {
						Toast.makeText(RecordMobile.this,
								"Please enter a valid Birth Date.",
								Toast.LENGTH_SHORT).show();
						return false;
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}// try-catch

			}// else
		}// else-if

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
		if (number.isEmpty()) {
			return true;
		}
		return false;
	}

}// class
