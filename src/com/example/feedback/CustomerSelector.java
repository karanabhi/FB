package com.example.feedback;

import com.example.blc.LogoutMaster;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class CustomerSelector extends TabActivity implements
		OnTabChangeListener {

	TabHost host;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_customer_selector);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_custom_titlebar);

		host = getTabHost();
		host.setOnTabChangedListener(this);
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, Authentication.class);
		spec = host.newTabSpec("Policy Holder").setIndicator("Policy Holder")
				.setContent(intent);
		host.addTab(spec);

		intent = new Intent().setClass(this, RegisterUser.class);
		spec = host.newTabSpec("Prospect User").setIndicator("Prospect User")
				.setContent(intent);
		host.addTab(spec);

		host.getTabWidget().setCurrentTab(0);

		host.getTabWidget().getChildAt(0)
				.setBackgroundColor(Color.parseColor("#1382b0"));
		host.getTabWidget().getChildAt(1).setBackgroundColor(Color.WHITE);
		TextView tv = (TextView) host.getTabWidget().getChildAt(1)
				.findViewById(android.R.id.title);
		tv.setTextColor(Color.BLACK);

	}// onCreate

	@Override
	public void onTabChanged(String tabId) {
		TextView tv = (TextView) host.getTabWidget().getChildAt(0)
				.findViewById(android.R.id.title);
		tv.setTextColor(Color.WHITE);
	}// onTabChanged()

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class
