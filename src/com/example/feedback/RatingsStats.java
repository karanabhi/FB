package com.example.feedback;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.blc.LogoutMaster;
import com.example.blc.ParseXML;
import com.example.blc.RatingsCommentAdapter;
import com.example.dataaccess.DBHelper;
import com.example.dataaccess.WebServiceContents;
import com.example.model.LoginModel;
import com.example.model.RatingsCommentModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.TextView;

public class RatingsStats extends Activity {

	private RatingBar ratingBarReview;
	int ratings = 0;
	ListView list_reviews;
	ArrayList<RatingsCommentModel> arraylist_review;

	DBHelper db = new DBHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_ratings_stats);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.layout_custom_titlebar);

		db.createConnection();

		AsyncFetchRatingStats afrs = new AsyncFetchRatingStats();
		afrs.execute();

	}// onCreate()

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

				// Average Review
				TextView avg_rate = (TextView) findViewById(R.id.textView_rating_stats_avg_rating);
				avg_rate.setText("" + ParseXML.avg_rating);

				// Review Rating Bar
				ratingBarReview = (RatingBar) findViewById(R.id.ratingBar_rating_stats_avg_rating_bar);
				ratingBarReview.setRating((float) ParseXML.avg_rating);
				ratingBarReview.setEnabled(false);

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

			String msg = "Please Wait while we retrive all ratings!";

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

	public void btnLogout(View v) {
		new LogoutMaster();
		Toast.makeText(getBaseContext(), "Successfully Logged out!!!",
				Toast.LENGTH_LONG).show();
		Intent log = new Intent(getBaseContext(), Login.class);
		startActivity(log);
	}// btnLogout()

}// class