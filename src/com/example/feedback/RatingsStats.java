package com.example.feedback;

import java.util.ArrayList;

import com.example.model.RatingsModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class RatingsStats extends Activity {

	private RatingBar ratingBar, ratingBarReview;
	private double ratings = 0.0;
	private double avg_rating = 4.2;
	private int total_count = 359348, count_5_stars = 65, count_4_stars = 10,
			count_3_stars = 5, count_2_stars = 8, count_1_stars = 12;

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

			}// onRatingChanged()
		});// setOnratingChanged()

		// Average Review
		TextView avg_rate = (TextView) findViewById(R.id.textView_rating_stats_avg_rating);
		avg_rate.setText("" + avg_rating);

		// Review Rating Bar
		ratingBarReview = (RatingBar) findViewById(R.id.ratingBar_rating_stats_avg_rating_bar);
		ratingBarReview.setRating((float) avg_rating);
		ratingBarReview.setEnabled(false);

		// Total Count
		TextView tot_count = (TextView) findViewById(R.id.textView_ratings_stats_total_count);
		tot_count.setText("" + total_count);

		// Graphs
		ProgressBar bar_5 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_5_star);
		ProgressBar bar_4 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_4_star);
		ProgressBar bar_3 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_3_star);
		ProgressBar bar_2 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_2_star);
		ProgressBar bar_1 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_1_star);
		bar_5.setProgress(count_5_stars);
		bar_5.getProgressDrawable().setColorFilter(Color.RED,
				android.graphics.PorterDuff.Mode.SRC_IN);

		bar_4.setProgress(count_4_stars);
		bar_4.getProgressDrawable().setColorFilter(Color.GREEN,
				android.graphics.PorterDuff.Mode.SRC_IN);

		bar_3.setProgress(count_3_stars);
		bar_3.getProgressDrawable().setColorFilter(Color.BLUE,
				android.graphics.PorterDuff.Mode.SRC_IN);

		bar_2.setProgress(count_2_stars);
		bar_2.getProgressDrawable().setColorFilter(Color.YELLOW,
				android.graphics.PorterDuff.Mode.SRC_IN);

		bar_1.setProgress(count_1_stars);
		bar_1.getProgressDrawable().setColorFilter(Color.BLACK,
				android.graphics.PorterDuff.Mode.SRC_IN);

		// ListViews
		ListView list_reviews = (ListView) findViewById(R.id.listView_ratings_stats_user_review);
		ArrayList<String> arraylist_review = new ArrayList<String>();

		arraylist_review.add("Important Tool");
		arraylist_review
				.add("This is an important tool. Please use it extensively.");
		arraylist_review.add("Excellent experience");
		arraylist_review.add("We are happy with SBI Life services.");

		ArrayAdapter<String> adapter_reviews = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arraylist_review);
		list_reviews.setAdapter(adapter_reviews);

	}// onCreate()

	private void completionMessage() {

		if (ratings >= 1.0 && ratings <= 5.0) {
			// RatingsModel em = new RatingsModel(ratings, comments);

			Dialog thanks = new Dialog(this);
			thanks.setContentView(R.layout.dialog_thanks);
			thanks.setTitle("Feedback completed");
			TextView text_comment = (TextView) thanks
					.findViewById(R.id.textView_dialog_thanks_comments);
			text_comment
					.setText("Thank You for your valuable feedback.\n\n\nHave a wonderful day ahead.");
			Button btn_ok = (Button) thanks
					.findViewById(R.id.button_dialog_thanks_ok);
			btn_ok.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent home = new Intent(RatingsStats.this, Home.class);
					startActivity(home);

				}// onClick()
			});// onCLickListener()

			thanks.show();

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