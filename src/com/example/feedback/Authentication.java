package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.*;
import com.example.blc.*;
import com.example.dataaccess.DBHelper;

public class Authentication extends Activity {

	AuthenticationModel amo;
	AuthenticationMaster am = new AuthenticationMaster();
	DBHelper db = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		Button validate = (Button) findViewById(R.id.button_authentication_validate);
		db.createConnection();
		validate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText text_mobile = (EditText) findViewById(R.id.editText_authentication_mobile);
				EditText text_policy = (EditText) findViewById(R.id.editText_authentication_policy);

				String mobile_number = text_mobile.getText().toString();
				String policy_number = text_policy.getText().toString();

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
								Toast.makeText(Authentication.this,
										"Validation Successfull!",
										Toast.LENGTH_SHORT).show();
								Intent fedtype = new Intent(
										Authentication.this, FeedbackType.class);
								startActivity(fedtype);
							}
						}// if-else

					}// IF-ELSE-IF
				}

			}// onClick()
		});// setOnclickLister()

	}// onCreate()

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
				"Invalid Credentials. Please try again.", Toast.LENGTH_SHORT)
				.show();
	}// invalidCredentials()

	public boolean checkCredentials(String mob, String pol) {
		if ((mob.isEmpty() && pol.isEmpty())
				|| (!mob.isEmpty() && !pol.isEmpty())) {

			Toast.makeText(Authentication.this, "Please Enter any one field.",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}// checkCredentials()

	public boolean checkNull(String number) {
		if (number.isEmpty()) {
			return true;
		}
		return false;
	}

}// class
