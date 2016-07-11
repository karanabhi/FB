package com.example.blc;

import java.util.List;

import com.example.feedback.R;
import com.example.model.RatingsCommentModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class RatingsCommentAdapter extends ArrayAdapter {

	List<RatingsCommentModel> lst;
	TextView nm, date, comm;
	RatingBar rate_comm;

	// int comment_count = 0, sumOfRatings = 0;

	@SuppressWarnings("unchecked")
	public RatingsCommentAdapter(Context context, int resource,
			List<RatingsCommentModel> objects) {
		super(context, resource, objects);
		lst = objects;
	}// constructor

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(
					R.layout.layout_custom_rating_comments_listview, null);
			nm = (TextView) row
					.findViewById(R.id.textView_custom_ratecomm_name);
			date = (TextView) row
					.findViewById(R.id.textView_custom_ratecomm_date);
			comm = (TextView) row
					.findViewById(R.id.textView_custom_ratecomm_comments);
			rate_comm = (RatingBar) row
					.findViewById(R.id.ratingBar_custom_ratecomm_individual);

			nm.setText(lst.get(position).getCust_name() == null ? "" : lst.get(
					position).getCust_name());
			date.setText(lst.get(position).getCreated_date() == null ? "" : lst
					.get(position).getCreated_date());
			comm.setText(lst.get(position).getApp_comment() == null ? "" : lst
					.get(position).getApp_comment());
			rate_comm
					.setRating((float) (lst.get(position).getApp_rate() == null ? 0.0
							: Float.parseFloat(lst.get(position).getApp_rate())));
			rate_comm.setEnabled(false);
			// sumOfRatings +=
			// Integer.parseInt(lst.get(position).getApp_rate());
			// comment_count++;
		}

		return row;
	}// getView()
}// class
