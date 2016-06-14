package com.example.feedback;

import com.example.model.RatingsModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class RatingsStats extends Activity {

	private RatingBar ratingBar;
	private double ratings = 0.0;

	String result;
	String remarks_1_2;
	String remarks_3;
	String remarks_4;
	String remarks_5;

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

				/*
				 * Button submit = (Button)
				 * findViewById(R.id.button_ratings_stats_submit);
				 * submit.setOnClickListener(new View.OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { EditText
				 * edit_comments = (EditText)
				 * findViewById(R.id.editText_ratings_stats_comments); String
				 * comments = edit_comments.getText().toString();
				 * 
				 * if (ratings >= 1.0 && ratings <= 5.0) { RatingsModel em = new
				 * RatingsModel(ratings, comments);
				 * 
				 * new AlertDialog.Builder(RatingsStats.this)
				 * .setTitle("Feedback completed") .setMessage(
				 * "Thank You for your valuable feedback.") .setPositiveButton(
				 * android.R.string.ok, new DialogInterface.OnClickListener() {
				 * public void onClick( DialogInterface dialog, int which) {
				 * Intent home = new Intent( RatingsStats.this, Home.class);
				 * startActivity(home); } }).show();
				 * 
				 * } else { Toast.makeText(RatingsStats.this,
				 * "Please Rate your Experience!!!", Toast.LENGTH_SHORT).show();
				 * }
				 * 
				 * }// onClick() });// setOnclickListener()
				 */

			}// onRatingChanged()
		});// setOnratingChanged()

	}// onCreate()

	private void completionMessage() {

		if (ratings >= 1.0 && ratings <= 5.0) {
			// RatingsModel em = new RatingsModel(ratings, comments);

			new AlertDialog.Builder(RatingsStats.this)
					.setTitle("Feedback completed")
					.setMessage("Thank You for your valuable feedback.")
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent home = new Intent(RatingsStats.this,
											Home.class);
									startActivity(home);
								}
							}).show();

		} else {
			Toast.makeText(RatingsStats.this, "Please Rate your Experience!!!",
					Toast.LENGTH_SHORT).show();
		}

	}// completionMessage()

	private void showRatings(double rat) {

		if (rat == 1.0) {

			dialog("Needs Improvement");
		} else if (rat == 2.0) {

			dialog("Average");
		} else if (rat == 3.0) {

			dialog1("Good");
		} else if (rat == 4.0) {

			dialog2("Very Good");
		} else if (rat == 5.0) {

			dialog3("Excellent");
		}

	}// showRatings

	public void dialog(String parameter) {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));

		d.setContentView(R.layout.layout_dialog_feedback1_2);
		d.setTitle(parameter);
		Spinner spnr_ratings_stats = (Spinner) d
				.findViewById(R.id.spnr_ratings_stats);
		final EditText editText_ratings_any_other = (EditText) d
				.findViewById(R.id.editText_ratings_any_other);
		Button button_any_others_submit = (Button) d
				.findViewById(R.id.button_any_others_submit);
		// ArrayAdapter<String> ratingsAdapter= new
		// ArrayAdapter<String>(getApplicationContext(), , )
		spnr_ratings_stats
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						result = adapter.getItemAtPosition(position).toString();
						// if(position!=0){
						// d.dismiss();
						// }else
						if (position == 6) {
							editText_ratings_any_other
									.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		button_any_others_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks_1_2 = editText_ratings_any_other.getText().toString();
				d.dismiss();
				completionMessage();
			}
		});

		d.show();

	}

	public void dialog1(String parameter) {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));

		d.setContentView(R.layout.layout_dialog_feedback_3);
		d.setTitle(parameter);
		Button button_3_stars_submit = (Button) d
				.findViewById(R.id.button_3_stars_submit);
		final EditText editText_3_stars_suggestion = (EditText) d
				.findViewById(R.id.editText_3_stars_suggestion);
		button_3_stars_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks_3 = editText_3_stars_suggestion.getText().toString();
				d.dismiss();
				completionMessage();

			}
		});
		d.show();
	}

	public void dialog2(String parameter) {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));
		d.setContentView(R.layout.layout_dialog_feedback_4);
		d.setTitle(parameter);
		final EditText editText_4_stars_suggestion = (EditText) d
				.findViewById(R.id.editText_4_stars_suggestion);
		Button button_4_stars_submit = (Button) d
				.findViewById(R.id.button_4_stars_submit);
		button_4_stars_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks_4 = editText_4_stars_suggestion.getText().toString();
				d.dismiss();
				completionMessage();
			}
		});

		d.show();
	}

	public void dialog3(String parameter) {
		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));
		d.setContentView(R.layout.layout_dialog_feedback_5);
		d.setTitle(parameter);
		final EditText editText_5_stars_suggestion = (EditText) d
				.findViewById(R.id.editText_5_stars_suggestion);
		Button button_5_stars_submit = (Button) d
				.findViewById(R.id.button_5_stars_submit);
		button_5_stars_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks_5 = editText_5_stars_suggestion.getText().toString();
				d.dismiss();
				completionMessage();
			}
		});

		d.show();

	}
}// class