package com.example.feedback;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.LogoutMaster;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.AuthenticationModel;
import com.example.model.DashboardModel;
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
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class RatingsFeedback extends Activity {

	DBHelper db = new DBHelper(this);
	int ratings = 0;
	String result, remarks = "", purpose = "";
	private RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_ratings_feedback);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_custom_titlebar);

		db.createConnection();

		Bundle extras = getIntent().getExtras();
		purpose = extras.getString("purpose");

		ratingBar = (RatingBar) findViewById(R.id.ratingBar_ratings_stats);
		showRatings(ratingBar.getRating());

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				ratings = (int) rating;
				showRatings(rating);

			}// onRatingChanged()
		});// setOnratingChanged()

		// Highlights button
		Button stats = (Button) findViewById(R.id.button_ratings_fb_view_stats);
		stats.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent auth = new Intent(RatingsFeedback.this,
							RatingsStats.class);
					startActivity(auth);
				} catch (Exception e) {
					Log.e("Log_Tag", e.getStackTrace().toString());
				}

			}// onclick()
		});// setonClickListener()

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

					new AuthenticationModel("", "");
					new DashboardModel("", "", "", "", "", "");
					new RatingsCommentModel("", "", "", "");
					new RatingsModel(0.0, "", "");
					new RecordMobileModel("", "", "", "", "");
					new RegisterUserModel("", "");
					/*
					 * RecordMobileModel.setMobile_number("");
					 * RecordMobileModel.setPolicy_number("");
					 * RecordMobileModel.setEmail("");
					 * RegisterUserModel.setName("");
					 * RecordMobileModel.setPan_number("");
					 * RecordMobileModel.setDob("");
					 * RatingsModel.setPurpose("");
					 * RatingsModel.setComments("");
					 */
					Dialog thanks = new Dialog(RatingsFeedback.this);

					thanks.setContentView(R.layout.dialog_thanks);
					thanks.getWindow().setBackgroundDrawable(
							new ColorDrawable(R.color.Aqua));
					thanks.setTitle("Feedback completed");
					thanks.setCancelable(false);
					thanks.setCanceledOnTouchOutside(false);
					TextView text_comment = (TextView) thanks
							.findViewById(R.id.textView_dialog_thanks_comments);
					text_comment
							.setText("Thank You for your valuable feedback.\n\nHave a wonderful day ahead.");
					Button btn_ok = (Button) thanks
							.findViewById(R.id.button_dialog_thanks_ok);
					btn_ok.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent home = new Intent(RatingsFeedback.this,
									OptionSelector.class);
							startActivity(home);

						}// onClick()
					});// onCLickListener()

					thanks.show();

				} else {
					Toast.makeText(RatingsFeedback.this,
							"There was a problem in Updating sync Flag.",
							Toast.LENGTH_SHORT).show();
				}// Inner if-else
			} else {
				Toast.makeText(RatingsFeedback.this,
						"Webservice Response error", Toast.LENGTH_SHORT).show();
			}// outer if-else

		}// onPostExecute()

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {

			syncProgDiag = new ProgressDialog(RatingsFeedback.this);

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
						RatingsFeedback.this,
						"There was a problem in inserting into DB. Please try again...",
						Toast.LENGTH_SHORT).show();

			}// inner IF_ELSE
		} else {
			Toast.makeText(RatingsFeedback.this,
					"Please Rate your Experience!!!", Toast.LENGTH_SHORT)
					.show();

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

		spnr_ratings_stats
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View view, int position, long id) {

						result = adapter.getItemAtPosition(position).toString();
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

	}// dialog3()

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class
