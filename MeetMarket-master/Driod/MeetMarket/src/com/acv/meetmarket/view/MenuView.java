package com.acv.meetmarket.view;

import z.base.CommonAndroid;
import z.base.ImageLoader;
import z.base.LogUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.acv.meetmarket.R;
import com.acv.meetmarket.db.DataStore;

//com.acv.meetmarket.view.MenuView
public class MenuView extends LinearLayout {

	public MenuView(Context context) {
		super(context);
		init();
	}

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private BaseAdapter adapter = new BaseAdapter() {
		private int res[] = new int[] { R.drawable.menu_1, R.drawable.menu_2, R.drawable.menu_3, R.drawable.menu_4 };
		private int str[] = new int[] { R.string.text_7, R.string.text_8, R.string.text_9, R.string.text_10 };

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new MenuItemView(parent.getContext());
			}

			((MenuItemView) convertView).initData(res[position], str[position]);

			convertView.findViewById(R.id.line).setVisibility(position == getCount() - 1 ? View.VISIBLE : View.GONE);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return res.length;
		}
	};

	private void init() {
		CommonAndroid.getView(getContext(), R.layout.menu_right, this);
		menu_name = CommonAndroid.getView(this, R.id.menu_name);
		menu_icon = CommonAndroid.getView(this, R.id.menu_icon);
		menu_icon_bg = CommonAndroid.getView(this, R.id.menu_icon_bg);
		menu_rightlist = CommonAndroid.getView(this, R.id.menu_rightlist);
		menu_right_close = CommonAndroid.getView(this, R.id.menu_right_close);

		menu_rightlist.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	private LinearLayout menu_right_close;
	private ImageView menu_icon, menu_icon_bg;
	private TextView menu_name;
	private ListView menu_rightlist;

	public void initData(OnItemClickListener onitemclick, OnClickListener onClickListener) {
		DataStore.getInstance().init(getContext());
		menu_name.setText(DataStore.getInstance().get("lastName", "") + " " + DataStore.getInstance().get("firstName", ""));
		String avatar = DataStore.getInstance().get("pictureUrl", "");
		if (CommonAndroid.isBlank(avatar)) {
			avatar = "https://static.licdn.com/scds/common/u/images/themes/katy/ghosts/person/ghost_person_200x200_v1.png";
		}

		ImageLoader.getInstance(getContext()).displayAvatar(avatar, menu_icon);
		ImageLoader.getInstance(getContext()).displayAvatarBG(avatar, menu_icon_bg);
		menu_rightlist.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		menu_rightlist.setOnItemClickListener(onitemclick);
		menu_icon.setOnClickListener(onClickListener);
		menu_name.setOnClickListener(onClickListener);
	}

	public void setOnClickListenerClose(OnClickListener l) {
		menu_right_close.setOnClickListener(l);
	}

}