package com.example.feedback;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.RatingsModel;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
	double ratings = 0.0;
	private double avg_rating = 4.2;
	private int total_count = 359348, count_5_stars = 65, count_4_stars = 10,
			count_3_stars = 5, count_2_stars = 8, count_1_stars = 12;

	String result, remarks = "", purpose = "";

	DBHelper db = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ratings_stats);

		Bundle extras = getIntent().getExtras();
		purpose = extras.getString("purpose");

		db.createConnection();

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

	// ASYNC CLASS TO SYNC THE DATA
	@SuppressWarnings("unused")
	private class AsyncInsertFeedback extends AsyncTask<String, String, String> {

		private final String SOAP_ACTION_URL = "";
		private final String NAMESPACE = "http://tempuri.org";
		private final String SOAP_ACTION_FUNCTION_NAME = "";
		ProgressDialog syncProgDiag;

		@Override
		protected String doInBackground(String... params) {
			String responseStatus = "";
			try {
				Cursor res = db.getDataForSyncing();
				res.moveToFirst();

				SoapObject request = new SoapObject(NAMESPACE,
						SOAP_ACTION_FUNCTION_NAME);
				request.addProperty("userID", res.getString(0));
				request.addProperty("mobNo", res.getString(1));
				request.addProperty("polNo", res.getString(2));
				request.addProperty("emailID", res.getString(3));
				request.addProperty("fullName", res.getString(4));
				request.addProperty("panNo", res.getString(5));
				request.addProperty("dob", res.getString(6));
				request.addProperty("purpose", res.getString(7));
				request.addProperty("rating", res.getString(8));
				request.addProperty("ratingComments", res.getString(9));
				request.addProperty("syncStatus", res.getString(10));
				request.addProperty("delFlag", res.getString(11));
				request.addProperty("createdBy", res.getString(12));
				request.addProperty("createdDate", res.getString(13));
				request.addProperty("modifiedBy", res.getString(14));
				request.addProperty("modifiedDate", res.getString(15));

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				WebServiceContents.allowAllSSL();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHTTPTransport = new HttpTransportSE(
						SOAP_ACTION_URL);

				try {
					androidHTTPTransport.call(SOAP_ACTION_URL, envelope);
					responseStatus = envelope.getResponse().toString();

				} catch (Exception e) {
					Log.e("Class ASYNCInsertFeedback, inside catch", e
							.getStackTrace().toString());
				}// try-catch
			} catch (Exception e) {
				Log.e("Class ASYNCInsertFeedback, Main Try-Catch", e
						.getStackTrace().toString());
			}// main try-catch

			return responseStatus;
		}// doInBackground()

		@Override
		protected void onPostExecute(String result) {

			if (result.equals("1")) {
				Boolean stat = db.updateSyncStatus();
				if (stat) {

					Dialog thanks = new Dialog(RatingsStats.this);

					thanks.setContentView(R.layout.dialog_thanks);
					thanks.getWindow().setBackgroundDrawable(
							new ColorDrawable(R.color.Aqua));
					thanks.setTitle("Feedback completed");
					thanks.setCancelable(false);
					thanks.setCanceledOnTouchOutside(false);
					TextView text_comment = (TextView) thanks
							.findViewById(R.id.textView_dialog_thanks_comments);
					text_comment
							.setText("Thank You for your valuable feedback.\n\n\nHave a wonderful day ahead.");
					Button btn_ok = (Button) thanks
							.findViewById(R.id.button_dialog_thanks_ok);
					btn_ok.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent home = new Intent(RatingsStats.this,
									OptionSelector.class);
							startActivity(home);

						}// onClick()
					});// onCLickListener()

					thanks.show();

				} else {
					Toast.makeText(RatingsStats.this,
							"There was a problem in Updating sync Flag.",
							Toast.LENGTH_SHORT).show();
				}// Inner if-else
			} else {
				Toast.makeText(RatingsStats.this,
						"Webservice Response did not returned 1",
						Toast.LENGTH_SHORT).show();
			}// outer if-else

		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			syncProgDiag = new ProgressDialog(RatingsStats.this);

			String msg = "Please Wait...";

			syncProgDiag.setMessage(msg);
			syncProgDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			syncProgDiag.setCancelable(false);
			syncProgDiag.setCanceledOnTouchOutside(false);

			syncProgDiag.setButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							syncProgDiag.dismiss();
						}// onClick()
					});// setOnClickListener()

			syncProgDiag.setMax(100);
			syncProgDiag.show();

		}// onPreExecute()

	}// ASYNC class

	private void completionMessage() {

		if (ratings >= 1.0 && ratings <= 5.0) {

			@SuppressWarnings("unused")
			RatingsModel rm = new RatingsModel(ratings, remarks, purpose);

			Boolean stat = db.insertData();
			if (stat) {

				// AsyncInsertFeedback aif = new AsyncInsertFeedback();
				// aif.execute();

				Dialog thanks = new Dialog(RatingsStats.this);

				thanks.setContentView(R.layout.dialog_thanks);
				thanks.getWindow().setBackgroundDrawable(
						new ColorDrawable(R.color.Aqua));
				thanks.setTitle("Feedback completed");
				thanks.setCancelable(false);
				thanks.setCanceledOnTouchOutside(false);
				TextView text_comment = (TextView) thanks
						.findViewById(R.id.textView_dialog_thanks_comments);
				text_comment
						.setText("Thank You for your valuable feedback.\n\n\nHave a wonderful day ahead.");
				Button btn_ok = (Button) thanks
						.findViewById(R.id.button_dialog_thanks_ok);
				btn_ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent home = new Intent(RatingsStats.this,
								OptionSelector.class);
						startActivity(home);

					}// onClick()
				});// onCLickListener()

				thanks.show();

			} else {
				Toast.makeText(
						RatingsStats.this,
						"There was a problem in inserting into DB. Please try again...",
						Toast.LENGTH_SHORT).show();

			}// inner IF_ELSE
		} else {
			Toast.makeText(RatingsStats.this, "Please Rate your Experience!!!",
					Toast.LENGTH_SHORT).show();

		}// outer if-else

	}// completionMessage()

	private void showRatings(double rat) {

		if (rat == 1.0) {

			dialog();
		} else if (rat == 2.0) {

			dialog();
		} else if (rat == 3.0) {

			dialog1();
		} else if (rat == 4.0) {

			dialog2();
		} else if (rat == 5.0) {

			dialog3();
		}

	}// showRatings

	public void dialog() {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));

		d.setContentView(R.layout.layout_dialog_feedback1_2);
		d.setTitle("Feedback");
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
		remarks = spnr_ratings_stats.getSelectedItem().toString();
		button_any_others_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks = editText_ratings_any_other.getText().toString();
				d.dismiss();
				completionMessage();
			}
		});

		d.show();

	}

	public void dialog1() {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));

		d.setContentView(R.layout.layout_dialog_feedback_3);
		d.setTitle("Feedback");
		Button button_3_stars_submit = (Button) d
				.findViewById(R.id.button_3_stars_submit);
		final EditText editText_3_stars_suggestion = (EditText) d
				.findViewById(R.id.editText_3_stars_suggestion);
		button_3_stars_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks = editText_3_stars_suggestion.getText().toString();
				d.dismiss();
				completionMessage();

			}
		});
		d.show();
	}

	public void dialog2() {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));
		d.setContentView(R.layout.layout_dialog_feedback_4);
		d.setTitle("Feedback");
		final EditText editText_4_stars_suggestion = (EditText) d
				.findViewById(R.id.editText_4_stars_suggestion);
		Button button_4_stars_submit = (Button) d
				.findViewById(R.id.button_4_stars_submit);
		button_4_stars_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks = editText_4_stars_suggestion.getText().toString();
				d.dismiss();
				completionMessage();
			}
		});

		d.show();
	}

	public void dialog3() {
		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));
		d.setContentView(R.layout.layout_dialog_feedback_5);
		d.setTitle("Feedback");
		final EditText editText_5_stars_suggestion = (EditText) d
				.findViewById(R.id.editText_5_stars_suggestion);
		Button button_5_stars_submit = (Button) d
				.findViewById(R.id.button_5_stars_submit);
		button_5_stars_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				remarks = editText_5_stars_suggestion.getText().toString();
				d.dismiss();
				completionMessage();
			}
		});

		d.show();

	}
}// class