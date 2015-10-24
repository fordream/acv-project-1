package com.acv.rouge.push;

import org.json.JSONException;
import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.LogUtils;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.services.RougeService;

//com.acv.rouge.push.PushBroadcastReceiver
public class PushBroadcastReceiver extends com.parse.ParsePushBroadcastReceiver {
	@Override
	protected Notification getNotification(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		// Set<String> keys = extras.keySet();
		// for (String key : keys) {
		// }

		String alert = CommonAndroid.getStringJsonString(extras.getString("com.parse.Data"), "data");
		String message = CommonAndroid.getStringJsonString(extras.getString("com.parse.Data"), "alert");
		if (!CommonAndroid.isBlank(alert) && new SettingShareReferentDB(context).isEnablePush()) {
			RougeService.pushChangeStatusOrder(context, "nopaincrossfit@gmail.com", "USA338603", alert,message);
		}
		return null;
		// return super.getNotification(context, intent);
	}

}