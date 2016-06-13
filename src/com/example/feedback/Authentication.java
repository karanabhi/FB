package com.example.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.*;
import com.example.blc.*;

public class Authentication extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		Button validate = (Button) findViewById(R.id.button_authentication_validate);

		validate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText mobile_number = (EditText) findViewById(R.id.editText_authentication_mobile);
				EditText policy_number = (EditText) findViewById(R.id.editText_authentication_policy_number);

				AuthenticationModel amo;
				AuthenticationMaster am;

				if ((mobile_number.getText().toString().equals("") && policy_number
						.getText().toString().equals(""))
						|| (mobile_number.getText().toString() != "" && policy_number
								.getText().toString() != "")) {

					Toast.makeText(Authentication.this,
							"Please Enter any one field.", Toast.LENGTH_SHORT)
							.show();

				} else if (mobile_number.getText().toString() != "") {

					amo = new AuthenticationModel(mobile_number.getText()
							.toString(), 1);
					am = new AuthenticationMaster();

					Toast.makeText(Authentication.this,
							"Authentication Successfull", Toast.LENGTH_SHORT)
							.show();

				} else if (policy_number.getText().toString() != "") {

					amo = new AuthenticationModel(policy_number.getText()
							.toString(), 2);

					if (am.getPolicy_number().equals("abc123")) {
						Toast.makeText(Authentication.this,
								"Authentication Successfull",
								Toast.LENGTH_SHORT).show();

					}// Policy IF
				}// Main IF

			}// onClick()
		});// setOnclickLister()

	}// onCreate()
}// class
