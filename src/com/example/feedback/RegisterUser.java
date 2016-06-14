package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

				rum = new RegisterUserModel(name, mobile_number);

				// Assuming the user is registered
				Intent ofeedbk = new Intent(RegisterUser.this,
						OtherFeedbackType.class);
				
				startActivity(ofeedbk);

			}// onClick()
		});// setOnclickListerner()

	}// onCreate()
}// class
