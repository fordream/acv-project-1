package com.acv.rouge.view;

import z.base.CommonAndroid;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.view.SettingView.SettingListener;

public class HeaderView extends LinearLayout {
	private SettingView settingView;

	public enum HeaderViewType {
		SETING, TRACKYOURORDER, ORDER, CONTACTUS_FORM
	}

	public HeaderView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.header, this);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(getContext(), R.layout.header, this);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		CommonAndroid.getView(getContext(), R.layout.header, this);
		init();
	}

	private void init() {
		settingView = CommonAndroid.getView(this, R.id.settingview);
	}

	public void setType(HeaderViewType type) {
		if (type == HeaderViewType.SETING) {
			findViewById(R.id.header_control_left).setVisibility(VISIBLE);
			findViewById(R.id.text_logo).setVisibility(GONE);
			findViewById(R.id.text_1).setVisibility(VISIBLE);
			((TextView) findViewById(R.id.text_1)).setText(R.string.settings);
		} else if (type == HeaderViewType.TRACKYOURORDER) {
			findViewById(R.id.header_control_left).setVisibility(VISIBLE);
			findViewById(R.id.text_logo).setVisibility(GONE);
			findViewById(R.id.text_1).setVisibility(VISIBLE);
			((TextView) findViewById(R.id.text_1)).setText(R.string.trackyourorder);
		}
	}

	public void setType(HeaderViewType type, String orderId) {
		if (type == HeaderViewType.ORDER) {
			findViewById(R.id.header_control_left).setVisibility(VISIBLE);
			findViewById(R.id.text_logo).setVisibility(GONE);
			findViewById(R.id.text_1).setVisibility(VISIBLE);
			((TextView) findViewById(R.id.text_1)).setText(String.format("Order # %s", orderId));
		} else if (type == HeaderViewType.CONTACTUS_FORM) {
			findViewById(R.id.header_control_left).setVisibility(VISIBLE);
			findViewById(R.id.text_logo).setVisibility(GONE);
			findViewById(R.id.text_1).setVisibility(VISIBLE);
			((TextView) findViewById(R.id.text_1)).setText(String.format("Order # %s", orderId));
		}
	}

	public void showSetting() {
		settingView.showSetting();

	}

	public boolean isShownSetting() {
		return settingView.isShownSetting();
	}

	public void closeSetting() {
		settingView.closeSetting();
	}

	public void setSettingListener(SettingListener settingListener) {
		settingView.setSettingListener(settingListener);
	}
}