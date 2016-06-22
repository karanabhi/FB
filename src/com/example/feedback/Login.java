package com.example.feedback;

import com.example.blc.LoginMaster;
import com.example.model.LoginModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button validate = (Button) findViewById(R.id.button_login_validate);
		validate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText text_login = (EditText) findViewById(R.id.editText_login_emp_id);
				EditText text_password = (EditText) findViewById(R.id.editText_login_password);

				String emp_id = text_login.getText().toString();
				String password = text_password.getText().toString();

				LoginModel lmo = new LoginModel(emp_id, password);
				LoginMaster lm = new LoginMaster();

				if (lm.validateEmployee(lmo)) {
					Intent sel = new Intent(Login.this, OptionSelector.class);
					startActivity(sel);
				} else {
					Toast.makeText(Login.this, "Invalid Credentials!",
							Toast.LENGTH_SHORT).show();
				}// if-else

			}// onClick()
		});// setOnclickLister()

	}
}// onCreate()
