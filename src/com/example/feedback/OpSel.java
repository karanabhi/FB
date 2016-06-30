package com.example.feedback;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class OpSel extends TabActivity implements OnTabChangeListener {

	TabHost host;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_op_sel);

		host = getTabHost();
		host.setOnTabChangedListener(this);
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, Home.class);
		spec = host.newTabSpec("Customer Feedback")
				.setIndicator("Customer Feedback").setContent(intent);
		host.addTab(spec);

		intent = new Intent().setClass(this, Dashboard.class);
		spec = host.newTabSpec("Dashboard").setIndicator("Dashboard")
				.setContent(intent);
		host.addTab(spec);

		host.getTabWidget().setCurrentTab(0);

	}// onCreate

	@Override
	public void onTabChanged(String tabId) {

	}// onTabChanged()
}// class
