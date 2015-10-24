package com.acv.meetmarket.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.acv.meetmarket.R;

public class DateTimeCreateDialog extends Dialog {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private TextView createmeeting_date_start, createmeeting_date_to, createmeeting_date;
	private LinearLayout layout_header;
	private int top;
	private int year, month, day, hour, minus;

	public enum CreateType {
		DAY, TIME_START, TIME_END
	};

	private CreateType type = CreateType.DAY;

	public DateTimeCreateDialog(Context context, CreateType type, int top, int year, int month, int day, int hour, int minus) {
		// super(context, R.style.AppTheme);
		super(context, android.R.style.Theme_Holo_Dialog);
		this.type = type;
		this.top = top;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minus = minus;
	}

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_choose_time);
		setContentView(R.layout.date_time_layout);
		createmeeting_date_start = (TextView) findViewById(R.id.createmeeting_date_start);
		createmeeting_date_to = (TextView) findViewById(R.id.createmeeting_date_to);
		createmeeting_date = (TextView) findViewById(R.id.createmeeting_date);

		createmeeting_date_start.setText("");
		createmeeting_date_to.setText("");
		createmeeting_date.setText("");
		layout_header = (LinearLayout) findViewById(R.id.layout_header);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_header.getLayoutParams();
		params.topMargin = top;
		layout_header.setLayoutParams(params);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);

		datePicker.init(year, month, day, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				DateTimeCreateDialog.this.year = year;
				DateTimeCreateDialog.this.month = monthOfYear;
				DateTimeCreateDialog.this.day = dayOfMonth;

				updateTime();
			}
		});

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

				hour = hourOfDay;
				minus = minute;
				updateTime();
			}
		});

		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minus);

		if (type == CreateType.DAY) {
			timePicker.setVisibility(View.GONE);
			findViewById(R.id.layout_time).setVisibility(View.GONE);
		} else {
			datePicker.setVisibility(View.GONE);
			findViewById(R.id.layout_date).setVisibility(View.GONE);

			if (type == CreateType.TIME_END) {
				findViewById(R.id.layout_start).setVisibility(View.GONE);
			} else {
				findViewById(R.id.layout_end).setVisibility(View.GONE);
			}
		}
	
		updateTime();
		
		// TODO
		findViewById(R.id.layout_time).setVisibility(View.GONE);
		findViewById(R.id.layout_time).setVisibility(View.GONE);
	}

	@Override
	public void dismiss() {
		callBack(year, month, day, hour, minus);
		super.dismiss();
	}

	public void callBack(int year2, int month2, int day2, int hour2, int minus2) {

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

		if (hour > 11) {
			createmeeting_date_to.setText(String.format("%s:%s PM", (hour - 12) > 10 ? (hour - 12) : ("0" + (hour - 12)), minus > 10 ? minus : ("0" + minus)));
		} else {
			createmeeting_date_to.setText(String.format("%s:%s AM", hour > 10 ? hour : ("0" + hour), minus > 10 ? minus : ("0" + minus)));
		}
	}
}
