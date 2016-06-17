package com.example.feedback;

import com.example.model.RatingsModel;
import com.example.model.RegisterUserModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OtherFeedbackType extends Activity {

	private RadioGroup radioPurposeGroup;
	private RadioButton radioPurposeButton;
	EditText editOther;
	String purpose = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_feedback_type);

		TextView showGreetings = (TextView) findViewById(R.id.textView_other_feedback_type_greetings);

		String name = "abc";
		String line1 = "Greetings! Mr./Ms./Mrs. " + (name)
				+ ", Welcome to SBI Life. We are honored to serve you.";
		showGreetings.setText(line1);

		editOther = (EditText) findViewById(R.id.editText_other_feedback_type_others);
		editOther.setVisibility(View.GONE);

		radioPurposeGroup = (RadioGroup) findViewById(R.id.radioGroup_other_feedback_type_purpose);
		radioPurposeGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						radioPurposeButton = (RadioButton) findViewById(checkedId);
						int pos = radioPurposeGroup
								.indexOfChild(findViewById(checkedId));
						purpose = radioPurposeButton.getText().toString();

						switch (pos) {
						case 0:
						case 1:
						case 2:
							editOther.setVisibility(View.GONE);
							break;
						case 3:
							editOther.setVisibility(View.VISIBLE);
							break;
						default:
							editOther.setVisibility(View.GONE);
							break;
						}// switch

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
				RatingsModel rm = new RatingsModel(0.0, "", purpose);
				Intent ratStat = new Intent(OtherFeedbackType.this,
						RatingsStats.class);
				ratStat.putExtra("purpose", purpose);
				startActivity(ratStat);

			}// onClick()
		});// OnclickListener()

	}// onCreate()
}// class
