package com.example.feedback;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.ParseXML;
import com.example.blc.RatingsCommentAdapter;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.LoginModel;
import com.example.model.RatingsCommentModel;
import com.example.model.RatingsModel;
import com.example.model.RecordMobileModel;
import com.example.model.RegisterUserModel;

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
import android.view.Window;
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
	int ratings = 0;
	String result, remarks = "", purpose = "";
	ListView list_reviews;
	ArrayList<RatingsCommentModel> arraylist_review;

	DBHelper db = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ratings_stats);

		Bundle extras = getIntent().getExtras();
		purpose = extras.getString("purpose");

		db.createConnection();

		AsyncFetchRatingStats afrs = new AsyncFetchRatingStats();
		afrs.execute();

	}// onCreate()

	// ASYNC CLASS TO INSERT FEEDBACK
	private class AsyncInsertFeedback extends AsyncTask<Void, String, String> {

		private static final String SOAP_ACTION_SAVECUSTOMERFEEDBACK = "http://tempuri.org/saveCustomerFeedback";
		private final String NAMESPACE = "http://tempuri.org/";
		private final String URL = "http://125.18.9.109:84/Service.asmx?wsdl";
		private final String FUNCTION_NAME = "saveCustomerFeedback";
		ProgressDialog syncProgDiag;

		@Override
		protected String doInBackground(Void... params) {
			String responseStatus = "";
			try {
				Cursor res = db.getDataForSyncing();
				res.moveToFirst();
				SoapObject request = new SoapObject(NAMESPACE, FUNCTION_NAME);
				request.addProperty("USER_ID", res.getString(0));
				request.addProperty("CUST_MOBILENO", res.getString(1));
				request.addProperty("CUST_POLICYNO", res.getString(2));
				request.addProperty("CUST_EMAILID", res.getString(3));
				request.addProperty("CUST_FULL_NAME", res.getString(4));
				request.addProperty("CUST_PANCARDNO", res.getString(5));
				request.addProperty("CUST_DOB", res.getString(6));
				request.addProperty("CUST_PURPOSE_OF_VISIT", res.getString(7));
				request.addProperty("CUST_APPS_RATING", res.getString(8));
				request.addProperty("CUST_APPS_RATING_COMMENTS",
						res.getString(9));

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				WebServiceContents.allowAllSSL();

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

				HttpTransportSE androidHTTPTransport = new HttpTransportSE(URL);

				try {
					androidHTTPTransport.call(SOAP_ACTION_SAVECUSTOMERFEEDBACK,
							envelope);
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

			syncProgDiag.dismiss();

			if (result.equals("1")) {
				Boolean stat = db.updateSyncStatus();
				if (stat) {

					LoginModel.setEmp_id("");
					RecordMobileModel.setMobile_number("");
					RecordMobileModel.setPolicy_number("");
					RecordMobileModel.setEmail("");
					RegisterUserModel.setName("");
					RecordMobileModel.setPan_number("");
					RecordMobileModel.setDob("");
					RatingsModel.setPurpose("");
					RatingsModel.setComments("");

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
				Toast.makeText(RatingsStats.this, "Webservice Response error",
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

	// ASYNC CLASS TO FETCH RATING STATS
	private class AsyncFetchRatingStats extends AsyncTask<Void, String, String> {

		private static final String SOAP_ACTION_GETAPPRATING = "http://tempuri.org/getAppRating";
		private final String NAMESPACE = "http://tempuri.org/";
		private final String URL = "http://125.18.9.109:84/Service.asmx?wsdl";
		private final String FUNCTION_NAME = "getAppRating";
		ProgressDialog syncProgDiag;

		@Override
		protected String doInBackground(Void... params) {

			String getratingstats = "", strAuthError;

			SoapObject request = new SoapObject(NAMESPACE, FUNCTION_NAME);

			request.addProperty("strUserID", LoginModel.getEmp_id());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			WebServiceContents.allowAllSSL();

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			HttpTransportSE androidHTTPTransport = new HttpTransportSE(URL);

			try {
				androidHTTPTransport.call(SOAP_ACTION_GETAPPRATING, envelope);
				SoapPrimitive sa = null;

				try {
					sa = (SoapPrimitive) envelope.getResponse();
					getratingstats = sa.toString();
					ParseXML prsObj = new ParseXML();
					getratingstats = prsObj.parseXmlTag(getratingstats,
							"NewDataSet");
					strAuthError = getratingstats;

					// if strAuthError==null => ERROR!
					if (strAuthError != null) {
						List<String> Node = prsObj.parseParentNode(
								getratingstats, "Table");
						List<RatingsCommentModel> nodeData = prsObj
								.parseNodeElement(Node);

						arraylist_review = new ArrayList<RatingsCommentModel>();
						arraylist_review.clear();
						for (RatingsCommentModel node : nodeData) {
							arraylist_review.add(node);
						}// for

						return "1";
					} else {
						return "2";
					}// if-else

				} catch (Exception e) {
					Log.e("Class ASYNCInsertFeedback, inside catch", e
							.getStackTrace().toString());
				}// try-catch
			} catch (Exception e) {
				Log.e("Class ASYNCInsertFeedback, Main Try-Catch", e
						.getStackTrace().toString());
			}// main try-catch

			return "0";// webservice error
		}// doInBackground()

		@Override
		protected void onPostExecute(String result) {

			syncProgDiag.dismiss();

			if (result.equals("1")) {

				// ListView
				list_reviews = (ListView) findViewById(R.id.listView_ratings_stats_user_review);
				RatingsCommentAdapter rca = new RatingsCommentAdapter(
						RatingsStats.this, 0, arraylist_review);
				rca.setNotifyOnChange(true);
				list_reviews.setAdapter(rca);

				ratingBar = (RatingBar) findViewById(R.id.ratingBar_ratings_stats);
				showRatings(ratingBar.getRating());

				// Average Review
				TextView avg_rate = (TextView) findViewById(R.id.textView_rating_stats_avg_rating);
				avg_rate.setText("" + ParseXML.avg_rating);

				// Review Rating Bar
				ratingBarReview = (RatingBar) findViewById(R.id.ratingBar_rating_stats_avg_rating_bar);
				ratingBarReview.setRating((float) ParseXML.avg_rating);
				ratingBarReview.setEnabled(false);

				ratingBar
						.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

							@Override
							public void onRatingChanged(RatingBar ratingBar,
									float rating, boolean fromUser) {

								ratings = (int) rating;
								showRatings(rating);

							}// onRatingChanged()
						});// setOnratingChanged()

				// Total Count
				TextView tot_count = (TextView) findViewById(R.id.textView_ratings_stats_total_count);
				tot_count.setText("" + ParseXML.total_count);

				// Graphs
				ProgressBar bar_5 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_5_star);
				ProgressBar bar_4 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_4_star);
				ProgressBar bar_3 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_3_star);
				ProgressBar bar_2 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_2_star);
				ProgressBar bar_1 = (ProgressBar) findViewById(R.id.progressBar_rating_stats_1_star);
				bar_5.setProgress(ParseXML.count_5_stars);
				bar_5.getProgressDrawable().setColorFilter(Color.GREEN,
						android.graphics.PorterDuff.Mode.SRC_IN);

				bar_4.setProgress(ParseXML.count_4_stars);
				bar_4.getProgressDrawable().setColorFilter(
						Color.parseColor("#006a4e"),
						android.graphics.PorterDuff.Mode.SRC_IN);

				bar_3.setProgress(ParseXML.count_3_stars);
				bar_3.getProgressDrawable().setColorFilter(
						Color.parseColor("#1382b0"),
						android.graphics.PorterDuff.Mode.SRC_IN);

				bar_2.setProgress(ParseXML.count_2_stars);
				bar_2.getProgressDrawable().setColorFilter(Color.YELLOW,
						android.graphics.PorterDuff.Mode.SRC_IN);

				bar_1.setProgress(ParseXML.count_1_stars);
				bar_1.getProgressDrawable().setColorFilter(Color.RED,
						android.graphics.PorterDuff.Mode.SRC_IN);

			} else if (result.equals("2")) {
				Toast.makeText(RatingsStats.this, "No Ratings to Show!!!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(RatingsStats.this, "Webservice Response error",
						Toast.LENGTH_LONG).show();
			}// outer if-else

		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			syncProgDiag = new ProgressDialog(RatingsStats.this);

			String msg = "Please Wait while we load your page!";

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

	}// ASYNC class AsyncFetchRatingStats

	private void completionMessage() {

		if (ratings >= 1.0 && ratings <= 5.0) {

			new RatingsModel(ratings, remarks, purpose);

			// Inserting Data IN DB
			Boolean stat = db.insertData();
			if (stat) {
				/*
				 * Cursor res = db.getDataForSyncing(); res.moveToFirst();
				 * Toast.makeText( getBaseContext(), "USER_ID" +
				 * res.getString(0) + "\nCUST_MOBILENO" + res.getString(1) +
				 * "\nCUST_POLICYNO" + res.getString(2) + "\nCUST_EMAILID" +
				 * res.getString(3) + "\nCUST_FULL_NAME" + res.getString(4) +
				 * "\nCUST_PANCARDNO" + res.getString(5) + "\nCUST_DOB" +
				 * res.getString(6) + "\nCUST_PURPOSE_OF_VISIT" +
				 * res.getString(7) + "\nCUST_APPS_RATING" + res.getString(8) +
				 * "CUST_APPS_RATING_COMMENTS" + res.getString(9),
				 * Toast.LENGTH_LONG).show();
				 */
				AsyncInsertFeedback aif = new AsyncInsertFeedback();
				aif.execute();

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

		if (rat == 1) {

			dialog();
		} else if (rat == 2) {

			dialog();
		} else if (rat == 3) {

			dialog1();
		} else if (rat == 4) {

			dialog2();
		} else if (rat == 5) {

			dialog3();
		}

	}// showRatings

	public void dialog() {

		final Dialog d = new Dialog(this);

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.GRAY));

		d.setContentView(R.layout.layout_dialog_feedback1_2);
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