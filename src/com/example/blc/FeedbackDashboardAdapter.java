package com.example.blc;

import java.util.List;

import com.example.feedback.R;
import com.example.model.DashboardModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.TextView;

public class FeedbackDashboardAdapter extends BaseAdapter {

	List<DashboardModel> lst = null;
	Context mContext;
	// used to keep selected position in ListView
	private int selectedPos = -1;

	public FeedbackDashboardAdapter(Context context, int textViewResourceId,
			List<DashboardModel> objects) {
		this.mContext = context;
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

		ViewHolder v = null;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					R.layout.layout_custom_dashboard_list_view, null);

			v = new ViewHolder();

			v.custID = (TextView) convertView
					.findViewById(R.id.textView_custom_dashboard_lv_custid);
			v.mobNo = (TextView) convertView
					.findViewById(R.id.textView_custom_dashboard_lv_mobile_no);
			v.polNo = (TextView) convertView
					.findViewById(R.id.textView_custom_dashboard_lv_policy_no);
			v.emailID = (TextView) convertView
					.findViewById(R.id.textView_custom_dashboard_lv_email);
			v.name = (TextView) convertView
					.findViewById(R.id.textView_custom_dashboard_lv_name);
			v.syncStat = (TextView) convertView
					.findViewById(R.id.textView_custom_dashboard_lv_sync_status);

			convertView.setTag(v);

		} else {
			v = (ViewHolder) convertView.getTag();
		}

		v.custID.setText(lst.get(position).getCust_id() == null ? "" : lst.get(
				position).getCust_id());
		v.mobNo.setText(lst.get(position).getMob_no() == null ? "" : lst.get(
				position).getMob_no());
		v.polNo.setText(lst.get(position).getPol_no() == null ? "" : lst.get(
				position).getPol_no());
		v.emailID.setText(lst.get(position).getEmail() == null ? "" : lst.get(
				position).getEmail());
		v.name.setText(lst.get(position).getName() == null ? "" : lst.get(
				position).getName());
		v.syncStat
				.setText(lst.get(position).getSync_status().equals("0") ? "Not Synced!"
						: "Synced!");

		return convertView;
	}// getView()

	public class ViewHolder {
		TextView custID, mobNo, polNo, emailID, name, syncStat;
	}// ViewHolder class

	@Override
	public int getCount() {

		return lst.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}// class FeedbackDashboardAdapter
