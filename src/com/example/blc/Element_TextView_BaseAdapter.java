package com.example.blc;

import java.util.Arrays;
import java.util.List;
import com.example.feedback.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Element_TextView_BaseAdapter extends BaseAdapter {

	public static class ViewHolder {
		TextView c1;
	}

	private List<String> allElementDetails;
	private LayoutInflater mInflater;

	//
	// private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
	// Color.parseColor("#E8E8E8") };

	public Element_TextView_BaseAdapter(Context context, List<String> results) {
		allElementDetails = results;
		mInflater = LayoutInflater.from(context);
	}

	public Element_TextView_BaseAdapter(Context context, String[] results) {
		allElementDetails = Arrays.asList(results);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return allElementDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return allElementDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getPosition(String resource_type) {
		// TODO Auto-generated method stub
		return allElementDetails.indexOf(resource_type);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.textview_default, parent,
					false);
			holder = new ViewHolder();
			holder.c1 = (TextView) convertView.findViewById(R.id.tv_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.c1.setText(String.valueOf(allElementDetails.get(position)));

		return convertView;
	}

}
