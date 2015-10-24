package com.acv.rouge.rouge.fragment;

import z.base.BaseFragment;
import z.base.CommonAndroid;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.services.RougeService;
import com.acv.rouge.view.HeaderView;
import com.acv.rouge.view.HeaderView.HeaderViewType;

public class SettingFragment extends BaseFragment {
	@SuppressWarnings("unused")
	private RelativeLayout rl_2, rl_1;
	private ListView list;
	private Switch checkbox_1;
	private ImageView img_1;
	private SettingShareReferentDB settingShareReferentDB;
	private TextView text_2;
	private int location = 0;
	private int curentLocation = 0;

	public SettingFragment() {
	}

	@Override
	public int getLayout() {
		return R.layout.setting;
	}

	@Override
	public void init(View view) {
		view.findViewById(R.id.header_control_left).setOnClickListener(this);
		view.findViewById(R.id.header_control_right).setOnClickListener(this);
		HeaderView mHeader = CommonAndroid.getView(view, R.id.header);
		mHeader.setType(HeaderViewType.SETING);
		view.findViewById(R.id.ll_1).setOnClickListener(null);

		settingShareReferentDB = new SettingShareReferentDB(getActivity());
		location = settingShareReferentDB.getPositionLocation();
		curentLocation = settingShareReferentDB.getPositionLocation();

		text_2 = CommonAndroid.getView(view, R.id.text_2);
		list = CommonAndroid.getView(view, R.id.list);
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

		list.setOnItemClickListener(this);
		rl_2.setOnClickListener(this);

		View header = CommonAndroid.getView(getActivity(), R.layout.setting_item, null);
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

		list.addHeaderView(header);
		list.setAdapter(new BaseAdapter() {

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
	public void onChangeLanguage() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v.getId() == R.id.rl_2) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getView().findViewById(R.id.ll_1).getLayoutParams();
			params.height = (int) getActivity().getResources().getDimension(R.dimen.dimen_282dp);

			getView().findViewById(R.id.ll_1).setBackgroundResource(R.drawable.rogue_settingsbglarge);
			getView().findViewById(R.id.ll_1).setLayoutParams(params);

			getView().findViewById(R.id.rl_1).setVisibility(View.GONE);
			getView().findViewById(R.id.rl_2).setVisibility(View.GONE);
			getView().findViewById(R.id.list).setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.header_control_left) {
		} else if (v.getId() == R.id.header_control_right) {
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		super.onItemClick(arg0, arg1, position, arg3);
		location = position - 1;

		settingShareReferentDB.saveLocation(location);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getView().findViewById(R.id.ll_1).getLayoutParams();
		params.height = (int) getActivity().getResources().getDimension(R.dimen.dimen_102dp);

		getView().findViewById(R.id.ll_1).setBackgroundResource(R.drawable.rogue_settingsbgsmall);
		getView().findViewById(R.id.ll_1).setLayoutParams(params);

		getView().findViewById(R.id.rl_1).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.rl_2).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.list).setVisibility(View.GONE);

		updateLanguage();
	}

	private void pushChangeNotificationSetting() {
		RougeService.pushChangeNotificationSetting(getActivity());
	}
}
