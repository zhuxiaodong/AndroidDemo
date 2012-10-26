package com.example.dd;

import android.widget.ListAdapter;

public interface DragAndDropListAdapter extends ListAdapter {

	public void shift(int position);

	public void startGrag(int position);

	public void stopGrag();

}
