package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		Button next = (Button) findViewById(R.id.button_home_next);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent auth = new Intent(Home.this, Authentication.class);
				startActivity(auth);

			}// onclick()
		});// setonClickListener()

	}// onCreate()
}// class
