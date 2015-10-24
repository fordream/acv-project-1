package com.acv.rouge.view;

import org.json.JSONObject;

import z.base.CommonAndroid;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.rouge.R;

public class PushItemView extends LinearLayout {

	public PushItemView(Context context) {
		super(context);
		CommonAndroid.getView(getContext(), R.layout.push_item, this);
	}

	public void setItem(JSONObject json) {
		((TextView) findViewById(R.id.text_1)).setText(CommonAndroid.getString(json, "ordernumber"));

		String ordernumber = CommonAndroid.getString(json, "ordernumber");
		String message = "";

		try {
			JSONObject packages = json.getJSONObject("packages");
			java.util.Iterator<String> keys = packages.keys();
			int count = 0;
			String lastKey = "";
			while (keys.hasNext()) {
				count++;
				lastKey = keys.next();
			}

			String status = CommonAndroid.getString(packages, lastKey);
			if (count == 1) {
				message += String.format(getContext().getString(R.string.message_push_update_single_package), ordernumber, status);
			} else {
				message += String.format(getContext().getString(R.string.message_push_update_multiple_package), ordernumber);
			}
		} catch (Exception exception) {

		}
		((TextView) findViewById(R.id.text_1)).setText(message);
	}
}
