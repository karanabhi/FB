package com.example.feedback;

import com.example.model.RecordMobileModel;
import com.example.model.RegisterUserModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FeedbackType extends Activity {

	private RadioGroup radioPurposeGroup;
	private RadioButton radioPurposeButton;
	EditText editOther;
	String purpose = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_type);

		TextView showGreetings = (TextView) findViewById(R.id.textView_feedback_type_dynamic_greeting);
		String line1 = "Greetings! Mr./Ms./Mrs. "
				+ (RegisterUserModel.getName())
				+ ", Welcome to SBI Life. We are honored to serve you.";
		String line2 = "Your Policy No. is "
				+ (RecordMobileModel.getPolicy_number());
		String line3 = "";
		if (!RecordMobileModel.getEmail().isEmpty()) {
			line3 = ", Email ID is " + RecordMobileModel.getEmail();
		}
		String line4 = ", Mobile No. is "
				+ (RegisterUserModel.getMobile_number()) + "";
		showGreetings.setText(line1 + line2 + line3 + line4);

		editOther = (EditText) findViewById(R.id.editText_feedback_type_others);
		editOther.setVisibility(View.GONE);

		radioPurposeGroup = (RadioGroup) findViewById(R.id.radioGroup_feedback_type_purpose);
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
							case 3:
								editOther.setVisibility(View.GONE);
								break;
							case 4:
								editOther.setVisibility(View.VISIBLE);
								break;
							default:
								editOther.setVisibility(View.GONE);
								break;
							}// switch
						} catch (Exception e) {
							Log.e("Class FeedbackType onCheckedChanged()", e
									.getStackTrace().toString());
						}// try-catch

					}// onCheckedChanged()
				});// setOnCheckedChangeListener()

		Button submitButton = (Button) findViewById(R.id.button_feedback_type_submit);
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!editOther.getText().toString().isEmpty()) {
					/*
					 * set value to RatingsModel with 0,1 and also set the
					 * string with with others
					 */
					purpose = editOther.getText().toString();

				} else {
					/*
					 * set value to RatingsModel with 0,0
					 */
				}

				Intent ratStat = new Intent(FeedbackType.this,
						RatingsStats.class);
				ratStat.putExtra("purpose", purpose);
				startActivity(ratStat);

			}// onClick()
		});// OnclickListener()

	}// onCreate()
}// class
