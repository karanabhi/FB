package com.example.feedback;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class OptionSelector extends TabActivity implements OnTabChangeListener {

	TabHost host;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_option_selector);

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

		host.getTabWidget().getChildAt(0)
				.setBackgroundColor(Color.parseColor("#1382b0"));
		host.getTabWidget().getChildAt(1).setBackgroundColor(Color.WHITE);
		TextView tv = (TextView) host.getTabWidget().getChildAt(1)
				.findViewById(android.R.id.title);
		tv.setTextColor(Color.BLACK);

	}// onCreate

	@Override
	public void onTabChanged(String tabId) {

	}
}// class
