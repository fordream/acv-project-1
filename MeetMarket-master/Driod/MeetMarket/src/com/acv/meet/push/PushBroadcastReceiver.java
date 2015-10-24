package com.acv.meet.push;

import org.json.JSONObject;

import com.acv.meetmarket.db.HomeTable;
import com.acv.meetmarket.db.UserTable;
import com.acv.meetmarket.service.MeetMarketService;
import com.acv.meetmarket.service.MeetMarketService.TypeMeetMarketService;
import com.google.android.gms.internal.om;

import z.base.CommonAndroid;
import z.base.LogUtils;
import z.base.MeetMarketUtils;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

//com.acv.meet.push.PushBroadcastReceiver
public class PushBroadcastReceiver extends com.parse.ParsePushBroadcastReceiver {
	@Override
	protected Notification getNotification(Context context, Intent intent) {
		Bundle extras = intent.getExtras();

		String message = CommonAndroid.getStringJsonString(extras.getString("com.parse.Data"), "alert");
		/**
		  { "name":"jacob", "idlinkin":"xxxx", "avatar":
		  "http://k14.vcmedia.vn/UZ6rra687dP37yYjwT3phKBWfbD/Image/2012/05/3rd/120514cinetbo09_cf197.jpg"
		  , "company":"Usol-V", "company-position":"developer",
		  "type":"Coffee", "area":"Perth", "lat":"-1000", "lng":"-1000",
		  "start-time":"1pm", "end-time":"2pm", "day":"2015:04:03" } //
		 * 
		 * */

		try {
			JSONObject object = new JSONObject(message);
			String idlinkin = CommonAndroid.getString(object, "idlinkin");
			String name = CommonAndroid.getString(object, "name");
			String avatar = CommonAndroid.getString(object, "avatar");
			String company = CommonAndroid.getString(object, "company");
			String type = CommonAndroid.getString(object, "type");
			String companyposition = CommonAndroid.getString(object, "company-position");
			String lat = CommonAndroid.getString(object, "lat");
			String lng = CommonAndroid.getString(object, "lng");
			String starttime = CommonAndroid.getString(object, "start-time");
			String endtime = CommonAndroid.getString(object, "end-time");
			String day = CommonAndroid.getString(object, "day");
			String userId = new UserTable(context).getUserId();
			String area =   CommonAndroid.getString(object, "area"); 
			if (!CommonAndroid.isBlank(userId)) {
				HomeTable homeTable = new HomeTable(context);
				homeTable.add(userId, idlinkin, name, avatar, company, companyposition, type, lat, lng, starttime, endtime, day,area, "push");
				
				Intent intentServicePush = new Intent(context, MeetMarketService.class);
				Bundle extrasPush = new Bundle();
				extrasPush.putSerializable(MeetMarketUtils.KEY.TYPE, TypeMeetMarketService.PUSH_RECIVER);
				intentServicePush.putExtras(extrasPush);
				context.startService(intentServicePush);
			}
		} catch (Exception e) {
			LogUtils.es("pushx",e);
		}
		return null;
	}
}