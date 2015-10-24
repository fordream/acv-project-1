package com.acv.meetmarket.view;

import z.base.CommonAndroid;
import z.base.ImageLoader;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.meetmarket.R;
import com.acv.meetmarket.db.HomeTable;

public class HomeItemView extends LinearLayout {

	public HomeItemView(Context context) {
		super(context);
		init();
	}

	public HomeItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HomeItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.new_home_item, this);
		new_home_item_text_1 = CommonAndroid.getView(this, R.id.new_home_item_text_1);
		new_home_item_text_2 = CommonAndroid.getView(this, R.id.new_home_item_text_2);
		new_home_item_text_3 = CommonAndroid.getView(this, R.id.new_home_item_text_3);
		newhome_icon = CommonAndroid.getView(this, R.id.newhome_icon);
	}

	private ImageView newhome_icon;
	private TextView new_home_item_text_1, new_home_item_text_2, new_home_item_text_3;

	public void initData(Cursor cursor) {
		new_home_item_text_3.setVisibility(View.VISIBLE);
		CommonAndroid.setText(new_home_item_text_1, cursor, HomeTable.name);
		// ađ location text
		String location = CommonAndroid.getString(cursor, HomeTable.location);
		location = String.format("at <b><font color='#bf2f3b'>%s</font><b>", location);
		new_home_item_text_2.setText(Html.fromHtml(location));
		// add date text
		String _to = CommonAndroid.getString(cursor, HomeTable.to);
		String _from = CommonAndroid.getString(cursor, HomeTable.from);

		String time = "";
		// if (!_to.toLowerCase().contains("today")) {
		time = String.format("%s", _to);
		// } else {
		// time = String.format("<b>%s - %s</b>", _from, _to);
		// }

		new_home_item_text_3.setText(Html.fromHtml(time));
		String avatar = CommonAndroid.getString(cursor, HomeTable.avatar);

		newhome_icon.setScaleType(ScaleType.FIT_CENTER);
		int padding = (int) getContext().getResources().getDimension(R.dimen.dimen_15dp);
		int paddingleft = (int) getContext().getResources().getDimension(R.dimen.dimen_5dp);
		newhome_icon.setPadding(paddingleft, padding, padding, padding);
		ImageLoader.getInstance(getContext()).displayImageHome(avatar, newhome_icon);
	}
}
