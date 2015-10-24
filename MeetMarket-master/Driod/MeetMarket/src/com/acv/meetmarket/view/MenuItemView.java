package com.acv.meetmarket.view;

import z.base.CommonAndroid;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.meetmarket.R;

//com.acv.meetmarket.view.MenuView
public class MenuItemView extends LinearLayout {

	public MenuItemView(Context context) {
		super(context);
		init();
	}

	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MenuItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.menu_item, this);
		menu_item_text = CommonAndroid.getView(this, R.id.menu_item_text);
		menuitem_icon = CommonAndroid.getView(this, R.id.menuitem_icon);
	}

	private ImageView menuitem_icon;
	private TextView menu_item_text;

	public void initData(int res, int str) {
		menuitem_icon.setBackgroundResource(res);
		menu_item_text.setText(str);
	}
}