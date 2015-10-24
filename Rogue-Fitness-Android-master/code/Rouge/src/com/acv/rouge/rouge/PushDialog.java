package com.acv.rouge.rouge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import z.base.CommonAndroid;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.acv.rouge.R;
import com.acv.rouge.view.PushItemView;

public abstract class PushDialog extends Dialog implements android.view.View.OnClickListener, OnItemClickListener {
	private TextView push_message;
	private ListView list;
	private String order_id;
	private String email;
	private String alert;
	private JSONArray array = new JSONArray();

	public PushDialog(Context context, String order_id, String email, String alert) {
		super(context);
		this.order_id = order_id;
		this.email = email;
		this.alert = alert;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		dismiss();
		JSONObject object = (JSONObject) parent.getItemAtPosition(position);
		openOrder(CommonAndroid.getString(object, "ordernumber"), CommonAndroid.getString(object, "email"), CommonAndroid.getString(object, "store"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pushdialog);
		list = (ListView) findViewById(R.id.list);

		try {
			array = new JSONArray(alert);
		} catch (JSONException e) {
		}

		if (array != null) {
			BaseAdapter adapter = new BaseAdapter() {

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = new PushItemView(parent.getContext());
					}

					((PushItemView) convertView).setItem((JSONObject) getItem(position));
					return convertView;
				}

				@Override
				public long getItemId(int position) {
					return 0;
				}

				@Override
				public Object getItem(int position) {
					try {
						return array.get(position);
					} catch (JSONException e) {
						return null;
					}
				}

				@Override
				public int getCount() {
					return array.length();
				}
			};

			list.setAdapter(adapter);

			if (adapter.getCount() < 5) {
				ViewGroup.LayoutParams params = list.getLayoutParams();
				params.height = adapter.getCount() * (int) getContext().getResources().getDimension(R.dimen.dimen_46dp);
				list.setAdapter(adapter);
			} else {
				ViewGroup.LayoutParams params = list.getLayoutParams();
				params.height = 5 * (int) getContext().getResources().getDimension(R.dimen.dimen_46dp);
				list.setAdapter(adapter);
			}
		}
		list.setOnItemClickListener(this);
		push_message = (TextView) findViewById(R.id.push_message);
		push_message.setText(String.format(getContext().getString(R.string.format_push_order), order_id));
		findViewById(R.id.push_btn_cancel).setOnClickListener(this);
		findViewById(R.id.push_btn_ok).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v.getId() == R.id.push_btn_cancel) {

		} else {
			openOrder(order_id, email, "");
		}
	}

	/**
	 * 
	 * @param order_id
	 */
	public abstract void openOrder(String order_id, String email, String store);
}
