package com.acv.rouge.view;

import z.base.ChangeSizeView;
import z.base.ChangeSizeView.ChangLayoutSizeListener;
import z.base.CommonAndroid;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.services.RougeService;

//com.acv.rouge.view.SettingView
public class SettingView extends LinearLayout implements OnClickListener, OnItemClickListener {

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CommonAndroid.getView(getContext(), R.layout.settingview, this);
		init(this);
	}

	public SettingView(Context context) {
		super(context);

		CommonAndroid.getView(getContext(), R.layout.settingview, this);
		init(this);
	}

	@SuppressWarnings("unused")
	private RelativeLayout rl_2, rl_1;
	private ListView setting_list;
	private Switch checkbox_1;
	private ImageView img_1;
	private SettingShareReferentDB settingShareReferentDB;
	private TextView text_2;
	private int location = 0;
	private int curentLocation = 0;

	public void init(View view) {
		setVisibility(GONE);
		view.findViewById(R.id.header_setting_control_left).setOnClickListener(this);
		view.findViewById(R.id.header_setting_control_right).setOnClickListener(this);
		view.findViewById(R.id.ll_1).setOnClickListener(null);

		settingShareReferentDB = new SettingShareReferentDB(getContext());
		location = settingShareReferentDB.getPositionLocation();
		curentLocation = settingShareReferentDB.getPositionLocation();

		text_2 = CommonAndroid.getView(view, R.id.text_2);
		setting_list = CommonAndroid.getView(view, R.id.setting_list);
		rl_2 = CommonAndroid.getView(view, R.id.rl_2);
		rl_1 = CommonAndroid.getView(view, R.id.rl_1);
		img_1 = CommonAndroid.getView(view, R.id.img_1);
		updateLanguage();

		checkbox_1 = CommonAndroid.getView(view, R.id.switch1);
		checkbox_1.setOnCheckedChangeListener(null);
		checkbox_1.setChecked(settingShareReferentDB.isEnablePush());

		checkbox_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settingShareReferentDB.setEnablePush(checkbox_1.isChecked());
				pushChangeNotificationSetting();
			}
		});

		setting_list.setOnItemClickListener(this);
		rl_2.setOnClickListener(this);

		View header = CommonAndroid.getView(getContext(), R.layout.setting_item, null);
		header.findViewById(R.id.img_1).setVisibility(View.GONE);
		header.findViewById(R.id.text_1).setVisibility(View.VISIBLE);
		header.findViewById(R.id.text_2).setVisibility(View.GONE);
		header.findViewById(R.id.img_2).setVisibility(View.GONE);
		header.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClick(null, null, 1, 0);
			}
		});

		setting_list.addHeaderView(header);
		setting_list.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = CommonAndroid.getView(parent.getContext(), R.layout.setting_item, null);
				}

				if (settingShareReferentDB == null) {
					settingShareReferentDB = new SettingShareReferentDB(parent.getContext());
				}

				ImageView img_1 = CommonAndroid.getView(convertView, R.id.img_1);
				img_1.setImageResource(settingShareReferentDB.icons()[position]);

				TextView text_2 = CommonAndroid.getView(convertView, R.id.text_2);
				CommonAndroid.getView(convertView, R.id.text_1).setVisibility(View.GONE);
				text_2.setText(settingShareReferentDB.strs()[position]);
				text_2.setVisibility(View.VISIBLE);
				if (position == getCount() - 1) {
					convertView.findViewById(R.id.line_1).setVisibility(View.GONE);
				} else {
					convertView.findViewById(R.id.line_1).setVisibility(View.VISIBLE);
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return settingShareReferentDB.icons()[position];
			}

			@Override
			public int getCount() {
				return settingShareReferentDB.icons().length;
			}
		});

	}

	private void updateLanguage() {
		img_1.setImageResource(settingShareReferentDB.icons()[location]);
		text_2.setText(settingShareReferentDB.strs()[location]);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.rl_2) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.ll_1).getLayoutParams();
			params.height = (int) getContext().getResources().getDimension(R.dimen.dimen_232dp);

			findViewById(R.id.ll_1).setBackgroundResource(R.drawable.rogue_settingsbglarge);
			findViewById(R.id.ll_1).setLayoutParams(params);

			findViewById(R.id.rl_1).setVisibility(View.GONE);
			findViewById(R.id.rl_2).setVisibility(View.GONE);
			findViewById(R.id.setting_list).setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.header_setting_control_left) {
			closeSetting();
		} else if (v.getId() == R.id.header_setting_control_right) {
			closeSetting();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		location = position - 1;
		settingShareReferentDB.saveLocation(location);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.ll_1).getLayoutParams();
		params.height = (int) getContext().getResources().getDimension(R.dimen.dimen_102dp);
		findViewById(R.id.ll_1).setBackgroundResource(R.drawable.rogue_settingsbgsmall);
		findViewById(R.id.ll_1).setLayoutParams(params);

		findViewById(R.id.rl_1).setVisibility(View.VISIBLE);
		findViewById(R.id.rl_2).setVisibility(View.VISIBLE);
		findViewById(R.id.setting_list).setVisibility(View.GONE);
		updateLanguage();
	}

	private void pushChangeNotificationSetting() {
		RougeService.pushChangeNotificationSetting(getContext());
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);

		if (visibility == GONE) {
			if (settingListener != null) {
				settingListener.close();
			}
		} else {
			if (settingListener != null) {
				settingListener.open();
				location = new SettingShareReferentDB(getContext()).getPositionLocation();
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.ll_1).getLayoutParams();
				params.height = (int) getContext().getResources().getDimension(R.dimen.dimen_102dp);

				findViewById(R.id.ll_1).setBackgroundResource(R.drawable.rogue_settingsbgsmall);
				findViewById(R.id.ll_1).setLayoutParams(params);

				findViewById(R.id.rl_1).setVisibility(View.VISIBLE);
				findViewById(R.id.rl_2).setVisibility(View.VISIBLE);
				findViewById(R.id.setting_list).setVisibility(View.GONE);

				updateLanguage();
			}
		}
	}

	private SettingListener settingListener;

	public void setSettingListener(SettingListener settingListener) {
		this.settingListener = settingListener;
	}

	public interface SettingListener {
		public void open();

		public void close();
	}

	public void closeSetting() {

		// int toY = (int)
		// getContext().getResources().getDimension(R.dimen.dimen_102dp);
		View ll_1 = CommonAndroid.getView(this, R.id.ll_1);
		int toY = ll_1.getLayoutParams().height;
		int fromY = (int) getContext().getResources().getDimension(R.dimen.dimen_20dp);
		ChangeSizeView.getInstance().startChangLayoutSize(toY, fromY, ll_1, 2, new ChangLayoutSizeListener() {

			@Override
			public void onSuccess() {
				setVisibility(View.GONE);
			}

			@Override
			public void onStart() {

			}
		});

	}

	public void showSetting() {
		setVisibility(View.VISIBLE);

		int toY = (int) getContext().getResources().getDimension(R.dimen.dimen_102dp);
		View ll_1 = CommonAndroid.getView(this, R.id.ll_1);
		ChangeSizeView.getInstance().startChangLayoutSize(0, toY, ll_1, 2, null);
	}

	public boolean isShownSetting() {
		return getVisibility() == View.VISIBLE;
	}
}
