package com.acv.rouge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

//com.acv.rouge.view.TrackYourOrderListView
public class TrackYourOrderListView extends ListView {

	public TrackYourOrderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean enableScroll = false;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!enableScroll) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void sendEnableScroll(boolean b) {
		enableScroll = b;
	}
}