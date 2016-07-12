package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blc.LogoutMaster;
import com.example.model.*;

public class RegisterUser extends Activity {

	RegisterUserModel rum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register_user);

		Button submit = (Button) findViewById(R.id.button_register_user_submit);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText edit_text_name = (EditText) findViewById(R.id.editText_register_user_name);
				EditText edit_text_mobile_number = (EditText) findViewById(R.id.editText_register_user_mobile);

				String name = edit_text_name.getText().toString();
				String mobile_number = edit_text_mobile_number.getText()
						.toString();

				if (checkCredentials(name, mobile_number)) {
					rum = new RegisterUserModel(name, mobile_number);
					RecordMobileModel.setMobile_number(mobile_number);

					// Assuming the user is registered
					Intent ofeedbk = new Intent(RegisterUser.this,
							OtherFeedbackType.class);
					startActivity(ofeedbk);
				}

			}// onClick()
		});// setOnclickListerner()

	}// onCreate()

	public boolean checkCredentials(String name, String mob) {
		String regex_name = "^[a-zA-Z]+[ '.a-zA-Z]*$", regex_mob = "^[0-9]{10}$";
		if (name.isEmpty() && mob.isEmpty()) {

			Toast.makeText(RegisterUser.this,
					"Please Enter Name and Registered Mobile Number.",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!name.matches(regex_name)) {
			Toast.makeText(RegisterUser.this, "Please Enter a valid Name.",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!mob.matches(regex_mob)) {
			Toast.makeText(RegisterUser.this,
					"Please Enter a valid Mobile Number.", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		return true;
	}// checkCredentials()

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class
