package com.example.feedback;

import com.example.blc.LogoutMaster;
import com.example.model.RegisterUserModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OtherFeedbackType extends Activity {

	private RadioGroup radioPurposeGroup;
	private RadioButton radioPurposeButton;
	EditText editOther;
	String purpose = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_other_feedback_type);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_custom_titlebar);

		TextView showGreetings = (TextView) findViewById(R.id.textView_other_feedback_type_greetings);

		String line1 = "Greetings " + (RegisterUserModel.getName()) + "!"
				+ "\nWelcome to SBI Life. We are honored to serve you.";
		showGreetings.setText(line1);

		editOther = (EditText) findViewById(R.id.editText_other_feedback_type_others);
		editOther.setVisibility(View.GONE);

		radioPurposeGroup = (RadioGroup) findViewById(R.id.radioGroup_other_feedback_type_purpose);
		radioPurposeGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						try {
							radioPurposeButton = (RadioButton) findViewById(checkedId);
							int pos = radioPurposeGroup
									.indexOfChild(findViewById(checkedId));
							purpose = radioPurposeButton.getText().toString();

							switch (pos) {
							case 0:
							case 1:
							case 2:
								editOther.setVisibility(View.GONE);
								editOther.setFocusable(true);
								break;
							case 3:
								editOther.setVisibility(View.VISIBLE);
								editOther.setFocusable(true);
								break;
							default:
								editOther.setVisibility(View.GONE);
								editOther.setFocusable(true);
								break;
							}// switch

						} catch (Exception e) {
							Log.e("Class OtherFeedbackType onCheckedChanged()",
									e.getStackTrace().toString());
						}// try-catch
					}// onCheckedChanged()
				});// setOnCheckedChangeListener()

		Button submitButton = (Button) findViewById(R.id.button_other_feedback_type_submit);
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!editOther.getText().toString().isEmpty()) {
					/*
					 * set value to RatingsModel with 1,1 and also set the
					 * string with with others
					 */
					purpose = editOther.getText().toString();
				} else {
					/*
					 * set value to RatingsModel with 1,0
					 */
				}
				Intent ratStat = new Intent(OtherFeedbackType.this,
						RatingsFeedback.class);
				ratStat.putExtra("purpose", purpose);
				startActivity(ratStat);

			}// onClick()
		});// OnclickListener()

	}// onCreate()

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class
