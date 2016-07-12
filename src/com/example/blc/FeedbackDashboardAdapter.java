package com.example.blc;

import java.util.List;
import com.example.feedback.R;
import com.example.model.DashboardModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class FeedbackDashboardAdapter extends ArrayAdapter {

	List<DashboardModel> lst;
	// used to keep selected position in ListView
	private int selectedPos = -1; // init value for not-selected

	TextView custID, mobNo, polNo, emailID, name, syncStat;

	@SuppressWarnings("unchecked")
	public FeedbackDashboardAdapter(Context context, int textViewResourceId,
			List<DashboardModel> objects) {
		super(context, textViewResourceId, objects);
		lst = objects;

	}// Constructor

	public void setSelectedPosition(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public int getSelectedPosition() {
		return selectedPos;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.layout_custom_dashboard_list_view,
					null);

			custID = (TextView) row
					.findViewById(R.id.textView_custom_dashboard_lv_custid);
			mobNo = (TextView) row
					.findViewById(R.id.textView_custom_dashboard_lv_mobile_no);
			polNo = (TextView) row
					.findViewById(R.id.textView_custom_dashboard_lv_policy_no);
			emailID = (TextView) row
					.findViewById(R.id.textView_custom_dashboard_lv_email);
			name = (TextView) row
					.findViewById(R.id.textView_custom_dashboard_lv_name);
			syncStat = (TextView) row
					.findViewById(R.id.textView_custom_dashboard_lv_sync_status);

			custID.setText(lst.get(position).getCust_id() == null ? "" : lst
					.get(position).getCust_id());
			mobNo.setText(lst.get(position).getMob_no() == null ? "" : lst.get(
					position).getMob_no());
			polNo.setText(lst.get(position).getPol_no() == null ? "" : lst.get(
					position).getPol_no());
			emailID.setText(lst.get(position).getEmail() == null ? "" : lst
					.get(position).getEmail());
			name.setText(lst.get(position).getName() == null ? "" : lst.get(
					position).getName());
			syncStat.setText(lst.get(position).getSync_status() == "0" ? "Not Synced"
					: "Synced");
		}

		return row;
	}// getView()
}// class FeedbackDashboardAdapter
