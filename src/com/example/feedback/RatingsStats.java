package com.example.feedback;

import com.example.model.RatingsModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class RatingsStats extends Activity {

	private RatingBar ratingBar;
	private double ratings = 0.0;
	TextView show_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ratings_stats);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar_ratings_stats);
		showRatings(ratingBar.getRating());

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				ratings = rating;
				showRatings(rating);
				Button submit = (Button) findViewById(R.id.button_ratings_stats_submit);
				submit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						EditText edit_comments = (EditText) findViewById(R.id.editText_ratings_stats_comments);
						String comments = edit_comments.getText().toString();

						if (ratings >= 1.0 && ratings <= 5.0) {
							RatingsModel em = new RatingsModel(ratings,
									comments);

							new AlertDialog.Builder(RatingsStats.this)
									.setTitle("Feedback completed")
									.setMessage(
											"Thank You for your valuable feedback.")
									.setPositiveButton(
											android.R.string.ok,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													Intent home = new Intent(
															RatingsStats.this,
															Home.class);
													startActivity(home);
												}
											}).show();

						} else {
							Toast.makeText(RatingsStats.this,
									"Please Rate your Experience!!!",
									Toast.LENGTH_SHORT).show();
						}

					}// onClick()
				});// setOnclickListener()

			}// onRatingChanged()
		});// setOnratingChanged()

	}// onCreate()

	private void showRatings(double rat) {
		show_data = (TextView) findViewById(R.id.textView_ratings_stats_show_star_title);

		if (rat == 1.0) {
			show_data.setText("Worst!");
		} else if (rat == 2.0) {
			show_data.setText("Bad!");
		} else if (rat == 3.0) {
			show_data.setText("Descent!");
		} else if (rat == 4.0) {
			show_data.setText("Good!");
		} else if (rat == 5.0) {
			show_data.setText("Best!");
		}

	}// showRatings

}// class
