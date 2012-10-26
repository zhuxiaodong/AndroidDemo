/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.dndlist.R;

public class DragAndDropListView extends ListView {

	static private final String TAG = "DragAndDropListView";

	private ImageView mDragView;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;

	private boolean mDragging = false;
	private int mSrcDragPos;
	private int mUpperBound;
	private int mLowerBound;
	private int mHeight;
	private Rect mTempRect = new Rect();
	private Bitmap mDragBitmap;
	private int mTouchSlop;

	private AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {

		public boolean onItemLongClick(AdapterView<?> parent, View v,
				int position, long id) {
			Log.d(TAG, "onLongClick(View arg0)" + parent.toString());
			itemLongClick(parent, v, position);
			return false;
		}
	};

	private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {

			if (mDragging) {
				int y = (int) event.getY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Log.d(TAG, "onTouch(View v, MotionEvent event) ACTION_UP");
					stopDragging();
					break;
				case MotionEvent.ACTION_MOVE:

					dragView(y);

					int itemnum2 = getItemForPosition(y - mDragView.getHeight()
							/ 2);

					if (itemnum2 >= 0) {

						if (mSrcDragPos != itemnum2) {
							((DragAndDropListAdapter) DragAndDropListView.this
									.getAdapter()).shift(itemnum2);
							mSrcDragPos = itemnum2;
						}

						int speed = 0;
						adjustScrollBounds(y);

						if (y > mLowerBound) {
							// scroll the list up a bit
							if (getLastVisiblePosition() < getCount() - 1) {
								speed = y > (mHeight + mLowerBound) / 2 ? 16
										: 4;
							} else {
								speed = 1;
							}
						} else if (y < mUpperBound) {
							// scroll the list down a bit
							speed = y < mUpperBound / 2 ? -16 : -4;
							if (getFirstVisiblePosition() == 0
									&& getChildAt(0).getTop() >= getPaddingTop()) {
								// if we're already at the top, don't try to
								// scroll,
								// because
								// it causes the framework to do some extra
								// drawing
								// that messes
								// up our animation
								speed = 0;
							}
						}
						if (speed != 0) {
							smoothScrollBy(speed, 30);
						}
					}
					return true;
					// break;
				}
			}
			return false;
		}

	};

	public DragAndDropListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DragAndDropListView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		init();
	}

	public DragAndDropListView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mTouchSlop = ViewConfiguration.get(this.getContext())
				.getScaledTouchSlop();
		setOnTouchListener(mTouchListener);
		setOnItemLongClickListener(mItemLongClickListener);
	}

	private int getItemForPosition(int y) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			child.getHitRect(mTempRect);
			if (mTempRect.contains(0, y)) {
				return getFirstVisiblePosition() + i;
			}
		}
		return INVALID_POSITION;
	}

	private void adjustScrollBounds(int y) {
		if (y >= mHeight / 3) {
			mUpperBound = mHeight / 3;
		}
		if (y <= mHeight * 2 / 3) {
			mLowerBound = mHeight * 2 / 3;
		}
	}

	private void startDragging(Bitmap bm, int x, int y) {
		Log.d(TAG, "startDragging");

		stopDragging();

		mDragging = true;

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowParams.x = x;
		mWindowParams.y = y;
		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		Context context = getContext();
		ImageView v = new ImageView(context);

		v.setBackgroundResource(R.drawable.ic_launcher);
		v.setPadding(0, 0, 0, 0);
		v.setImageBitmap(bm);
		mDragBitmap = bm;
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(v, mWindowParams);

		mDragView = v;
	}

	private void dragView(int y) {
		mWindowParams.y = y;
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}

	private void stopDragging() {
		if (mDragView != null) {
			mDragView.setVisibility(GONE);
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}

		((DragAndDropListAdapter) DragAndDropListView.this.getAdapter())
				.stopGrag();
		mDragging = false;
	}

	private boolean itemLongClick(AdapterView<?> parent, View v, int position) {
		Log.d(TAG, "onLongClick(View arg0)" + parent.toString());

		v.setDrawingCacheEnabled(true);

		// Create a copy of the drawing cache so that it does not
		// get recycled
		// by the framework when the list tries to clean up memory
		int y = v.getBottom();

		Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());

		int[] location = new int[2];
		v.getLocationInWindow(location);

		startDragging(bitmap, 0, location[1]);
		((DragAndDropListAdapter) DragAndDropListView.this.getAdapter())
				.startGrag(position);

		mSrcDragPos = position;

		mHeight = getHeight();

		mUpperBound = Math.min(y - mTouchSlop, mHeight / 3);
		mLowerBound = Math.max(y + mTouchSlop, mHeight * 2 / 3);

		return false;
	}

}