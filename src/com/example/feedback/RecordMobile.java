package com.example.feedback;

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

		if (checkNull(pol)) {
			Toast.makeText(RecordMobile.this,
					"Please enter your Policy Number.", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ((checkNull(dob) && checkNull(pan))
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
		return true;
	}// checkCredentials()

	public boolean validateCredentials(String mob, String email) {

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
