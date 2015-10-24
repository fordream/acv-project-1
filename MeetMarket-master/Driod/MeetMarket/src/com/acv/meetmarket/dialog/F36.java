package com.acv.meetmarket.dialog;

import java.util.Calendar;

import z.base.CommonAndroid;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.meetmarket.R;
import com.acv.meetmarket.db.HomeTable;

public class F36 extends Dialog implements android.view.View.OnClickListener {
	private ImageView menu_icon;
	private TextView f36_name;
	private TextView f36_company;
	private TextView f36_company_positon;
	private TextView f36_infor;
	private TextView f36_infor_ok;
	private TextView f36_infor_cancel;
	private LinearLayout f36_tab_1, f36_tab_2;
	private TextView f36_infor_comfirm_ok;
	private TextView f36_comfirm_infor;

	public F36(Context context) {
		super(context, R.style.AppTheme);
	}

	HomeTable homeTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
		setContentView(R.layout.f36);
		menu_icon = (ImageView) findViewById(R.id.menu_icon);
		f36_name = (TextView) findViewById(R.id.f36_name);
		f36_company = (TextView) findViewById(R.id.f36_company);
		f36_company_positon = (TextView) findViewById(R.id.f36_company_positon);
		f36_infor = (TextView) findViewById(R.id.f36_infor);
		f36_infor_ok = (TextView) findViewById(R.id.f36_infor_ok);
		f36_infor_cancel = (TextView) findViewById(R.id.f36_infor_cancel);
		f36_tab_1 = (LinearLayout) findViewById(R.id.f36_tab_1);
		f36_tab_2 = (LinearLayout) findViewById(R.id.f36_tab_2);

		f36_comfirm_infor = (TextView) findViewById(R.id.f36_comfirm_infor);
		f36_tab_2.setVisibility(View.GONE);
		f36_infor_comfirm_ok = (TextView) findViewById(R.id.f36_infor_comfirm_ok);
		findViewById(R.id.f36_infor_ok).setOnClickListener(this);
		findViewById(R.id.f36_infor_cancel).setOnClickListener(this);
		findViewById(R.id.f36_infor_comfirm_ok).setOnClickListener(this);
		/**
		 * 
		 */
		f36_name.setText("");
		f36_company.setText("");
		f36_company_positon.setText("");

		// StringBuilder builder = new StringBuilder();
		// builder.append("<font >");
		// builder.append(" Is free to meet for ");
		// builder.append("<font color='#d6402b'>Coffee</font>");
		// builder.append("<br>around<br>");
		// builder.append("<font color='#d6402b'><b>Perth, CBD</b></font>");
		// builder.append("<br>between<br>");
		// builder.append("<font color='#d6402b'><b>1pm and 2pm today</b></font>");
		// builder.append("</font>");
		f36_infor.setText(Html.fromHtml(""));
		
		 StringBuilder builderx = new StringBuilder();
		 builderx.append("<font >");
		 builderx.append("<font><b>Awesome,</b></font><br>");
		 builderx.append("You've been placed into the invite pool.<br>");
		 builderx.append("We'll <font color='#e18177'>randomly</font> draw a name soon,<br>");
		 builderx.append("if you're <font color='#e18177'>selected</font> we'll let you know<br>");
		 builderx.append("ASAP");
		 builderx.append("</font>");
		f36_comfirm_infor.setText(Html.fromHtml(builderx.toString()));

		z.base.ImageLoader.getInstance(getContext()).displayAvatar("http://vignette3.wikia.nocookie.net/avatar/images/2/2e/Toph.png/revision/latest?cb=20130125015342", menu_icon);

		homeTable = new HomeTable(getContext());
		Cursor cursor = homeTable.querry(String.format("%s ='%s'", HomeTable._ID, _id));
		if (cursor != null) {
			if (cursor.moveToNext()) {
				f36_name.setText(CommonAndroid.getString(cursor, HomeTable.name));
				f36_company.setText(CommonAndroid.getString(cursor, HomeTable.companyname));
				f36_company_positon.setText(CommonAndroid.getString(cursor, HomeTable.companyposition));
			}

			String start = CommonAndroid.getString(cursor, HomeTable.from);
			String to = CommonAndroid.getString(cursor, HomeTable.to);
			String day = CommonAndroid.getString(cursor, HomeTable.day);

			Calendar calendar = Calendar.getInstance();

			String dayx = String.format("%s:%s:%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
			StringBuilder builder = new StringBuilder();
			builder.append("<font >");
			builder.append(" Is free to meet for ");
			builder.append("<font color='#d6402b'>").append(CommonAndroid.getString(cursor, HomeTable.type)).append("</font>");
			builder.append("<br>around<br>");
			builder.append("<font color='#d6402b'><b>").append(CommonAndroid.getString(cursor, HomeTable.location)).append("</b></font>");
			builder.append("<br>between<br>");
			builder.append(String.format("<font color='#d6402b'><b>%s and %s %s", start, to, day));
			builder.append("</b></font>");
			builder.append("</font>");
			f36_infor.setText(Html.fromHtml(builder.toString()));

			cursor.close();
		}

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.f36_infor_cancel) {
			onClickCancel();
			dismiss();
		} else if (v.getId() == R.id.f36_infor_ok) {
			onClickOkie(_id);

			f36_tab_1.setVisibility(View.GONE);
			f36_tab_2.setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.f36_infor_comfirm_ok) {
			dismiss();
		}

	}

	public void onClickOkie(String _id2) {
		homeTable.nothanks(_id);
	}

	public void onClickCancel() {
		homeTable.pushOk(_id);
	}

	private String _id;

	public void updateId(String _id) {
		this._id = _id;
	}

}
