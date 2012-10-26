package com.example.dd;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dndlist.R;

public class MyListAdapter implements DragAndDropListAdapter {

	static private final String TAG = "MyListAdapter";

	static private final String ITEMS[] = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20" };
	static private ArrayList<String> mData = new ArrayList<String>();

	static {
		for (String item : ITEMS) {
			mData.add(item);
		}
	}

	DataSetObserver mDataSetObserver = null;
	int mDragPosition = -100;

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(), R.layout.list_item,
					null);
			// convertView.setLongClickable(true);
			// .setOnLongClickListener(mLongClickListener);
		}

		((TextView) convertView.findViewById(R.id.textView1)).setText("item  "
				+ mData.get(position));

		convertView.setVisibility((position == mDragPosition) ? View.INVISIBLE
				: View.VISIBLE);

		return convertView;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObserver = observer;
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObserver = null;
	}

	public boolean areAllItemsEnabled() {
		return false;
	}

	public boolean isEnabled(int position) {
		return false;
	}

	public void shift(int position) {
		Log.d(TAG, "shift" + position);
		int max = Math.max(position, mDragPosition);
		int min = Math.min(position, mDragPosition);

		String strMax = mData.remove(max);
		String strMin = mData.remove(min);

		mData.add(min, strMax);
		mData.add(max, strMin);

		mDragPosition = position;

		mDataSetObserver.onChanged();
	}

	public void startGrag(int position) {
		mDragPosition = position;
	}

	public void stopGrag() {
		mDragPosition = -100;
		mDataSetObserver.onChanged();
	}

}
