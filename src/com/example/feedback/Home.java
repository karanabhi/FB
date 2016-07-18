package com.example.feedback;

import com.example.blc.LogoutMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		Button next = (Button) findViewById(R.id.button_home_next);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent auth = new Intent(Home.this, CustomerSelector.class);
					// TabHost.TabSpec spec;
					// spec =
					// OptionSelector.host.newTabSpec("").setContent(auth);
					// OptionSelector.host.addTab(spec);
					startActivity(auth);
				} catch (Exception e) {
					Log.e("Log_Tag", e.getStackTrace().toString());
				}

			}// onclick()
		});// setonClickListener()

	}// onCreate()

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class
