package com.example.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionSelector extends Activity {

	Button dash, custFb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option_selector);

		dash = (Button) findViewById(R.id.button_option_sel_dashboard);
		custFb = (Button) findViewById(R.id.button_option_sel_cust_fb);

		dash.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent dashB = new Intent(OptionSelector.this, Dashboard.class);
				startActivity(dashB);
			}// onclick()
		});// onClickListener()

		custFb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent dashB = new Intent(OptionSelector.this, Home.class);
				startActivity(dashB);
			}// onclick()
		});// onClickListener()
	}// onCreate()
}// class
