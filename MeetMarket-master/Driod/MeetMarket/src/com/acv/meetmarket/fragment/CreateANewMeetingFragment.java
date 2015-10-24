package com.acv.meetmarket.fragment;

import java.util.Calendar;

import z.base.CommonAndroid;
import z.base.LogUtils;
import z.base.MeetMarketBaseFragment;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.acv.meetmarket.MainActivity;
import com.acv.meetmarket.R;
import com.acv.meetmarket.dialog.DateTimeCreateDialog;
import com.acv.meetmarket.dialog.DateTimeCreateDialog.CreateType;
import com.google.android.gms.internal.ho;
import com.google.android.gms.internal.la;

public class CreateANewMeetingFragment extends MeetMarketBaseFragment {

	@Override
	public void onShowData() {
		super.onShowData();

		getView().setVisibility(View.VISIBLE);
		createmeeting_location.setText("");
		createmeeting_date.setText("");
		createmeeting_date_start.setText("");
		createmeeting_date_to.setText("");

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minus = calendar.get(Calendar.MINUTE);
		hour1 = calendar.get(Calendar.HOUR_OF_DAY);
		minus1 = calendar.get(Calendar.MINUTE);
		updateTime();
	}

	private void updateTime() {
		Calendar calendar = Calendar.getInstance();
		if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
			createmeeting_date.setText(R.string.text_24);
		} else {
			createmeeting_date.setText(String.format("%s:%s:%s", year, (month + 1) > 9 ? (month + 1) : ("0" + (month + 1)), (day) > 9 ? (day) : ("0" + (day))));
		}

		if (hour > 11) {
			createmeeting_date_start.setText(String.format("%s:%s PM", (hour - 12) > 10 ? (hour - 12) : ("0" + (hour - 12)), minus > 10 ? minus : ("0" + minus)));
		} else {
			createmeeting_date_start.setText(String.format("%s:%s AM", hour > 10 ? hour : ("0" + hour), minus > 10 ? minus : ("0" + minus)));
		}

		if (hour1 > 11) {
			createmeeting_date_to.setText(String.format("%s:%s PM", (hour1 - 12) > 10 ? (hour1 - 12) : ("0" + (hour1 - 12)), minus1 > 10 ? minus1 : ("0" + minus1)));
		} else {
			createmeeting_date_to.setText(String.format("%s:%s AM", hour1 > 10 ? hour1 : ("0" + hour1), minus1 > 10 ? minus1 : ("0" + minus1)));
		}
	}

	private int year, month, day, hour, minus, hour1, minus1;

	@Override
	public void init(View view) {
		super.init(view);
		view.setVisibility(View.GONE);
		createnewmeeting_cancel = CommonAndroid.getView(view, R.id.createnewmeeting_cancel);
		createmeeting_date = CommonAndroid.getView(view, R.id.createmeeting_date);
		createmeeting_date_start = CommonAndroid.getView(view, R.id.createmeeting_date_start);
		createmeeting_date_to = CommonAndroid.getView(view, R.id.createmeeting_date_to);
		createmeeting_location = CommonAndroid.getView(view, R.id.createmeeting_location);
		createmeetting_group = CommonAndroid.getView(view, R.id.createmeetting_group);
		createmeeting_send_invite = CommonAndroid.getView(view, R.id.createmeeting_send_invite);
		layout_main_onscroll = CommonAndroid.getView(view, R.id.layout_main_onscroll);
		createnewmeeting_cancel.setOnClickListener(this);
		createmeeting_send_invite.setOnClickListener(this);
		createmeeting_date.setOnClickListener(this);
		createmeeting_date_start.setOnClickListener(this);
		createmeeting_date_to.setOnClickListener(this);
		createmeeting_location.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int top = 0;
		FrameLayout.LayoutParams f = (FrameLayout.LayoutParams) layout_main_onscroll.getLayoutParams();
		top = f.topMargin;
		if (v.getId() == R.id.createnewmeeting_cancel) {
			getActivity().onBackPressed();
		} else if (v.getId() == R.id.createmeeting_send_invite) {

			CommonAndroid.showDialogOkcancel(getActivity(), getActivity().getString(R.string.text_23), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
		} else if (v.getId() == R.id.createmeeting_date) {
			// TODO

			top = top + (int) getActivity().getResources().getDimension(R.dimen.dimen_200dp);
			String date = createmeeting_date.getText().toString();
			new DateTimeCreateDialog(getActivity(), CreateType.DAY, top, year, month, day, hour, minus) {
				public void callBack(int year2, int month2, int day2, int hour2, int minus2) {
					year = year2;
					month = month2;
					day = day2;
					updateTime();
				};
			}.show();
		} else if (v.getId() == R.id.createmeeting_date_start) {
			// TODO
			top = top + (int) getActivity().getResources().getDimension(R.dimen.dimen_200dp);
			String time = createmeeting_date_start.getText().toString();
			new DateTimeCreateDialog(getActivity(), CreateType.TIME_START, top, year, month, day, hour, minus) {
				public void callBack(int year2, int month2, int day2, int hour2, int minus2) {
					hour = hour2;
					minus = minus2;
					updateTime();
				};
			}.show();
		} else if (v.getId() == R.id.createmeeting_date_to) {
			top = top + (int) getActivity().getResources().getDimension(R.dimen.dimen_200dp);
			String time = createmeeting_date_to.getText().toString();
			new DateTimeCreateDialog(getActivity(), CreateType.TIME_END, top, year, month, day, hour1, minus1) {
				public void callBack(int year2, int month2, int day2, int hour2, int minus2) {
					hour1 = hour2;
					minus1 = minus2;
					updateTime();
				};
			}.show();
		} else if (v.getId() == R.id.createmeeting_location) {

			((MainActivity) getActivity()).openMap(xlat, xlng, createmeeting_location.getText().toString());
		}
	}

	@Override
	public int getLayout() {
		return R.layout.createnewmeeting;
	}

	private LinearLayout layout_main_onscroll;
	private TextView createnewmeeting_cancel;
	private TextView createmeeting_date;
	private TextView createmeeting_date_start;
	private TextView createmeeting_date_to;
	private TextView createmeeting_location;
	private RadioGroup createmeetting_group;
	private TextView createmeeting_send_invite;

	private double xlat, xlng;

	public void addLocation(double xlat, double xlng, String mapText) {
		createmeeting_location.setText(mapText);
		this.xlat = xlat;
		this.xlng = xlng;
	}
}