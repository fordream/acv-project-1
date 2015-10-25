package com.acv.meetmarket.db;

//import com.google.android.gms.internal.cm;

import z.base.CommonAndroid;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class HomeTable extends MeetTable {

	public HomeTable(Context context) {
		super(context);
		addColumns(avatar);
		addColumns(location);
		addColumns(from);
		addColumns(to);
		addColumns(name);
		addColumns(id);
		addColumns(companyname);
		addColumns(companyposition);
		addColumns(type);
		addColumns(logtime);
		addColumns(status);
		addColumns(lat);
		addColumns(lng);
		addColumns(day);
		addColumns(fromUserId);
	}

	public static final String id = "id";
	public static final String fromUserId = "fromUserId";
	public static final String name = "name";
	public static final String companyname = "companyname";
	public static final String companyposition = "companyposition";

	public static final String avatar = "avatar";
	public static final String location = "location";
	public static final String from = "_from";
	public static final String to = "_to";
	public static final String day = "day";
	public static final String lat = "lat";
	public static final String lng = "lng";

	// add
	public static final String logtime = "logtime";

	/**
	 * created, new, pushcancel, pushinterested, push
	 */
	public static final String status = "status";

	/**
	 * coffee, drinks, ameal
	 */
	public static final String type = "type";

	public Cursor querryAllDataOfUser(String email) {
		String where = String.format("%s ='%s'", userloginId, email);
		return querry(where);
	}

	public void addExample() {
		String userEmail = "truongvv@sinhvu.com";
		if (!CommonAndroid.isBlank(userEmail)) {
			String avatar = "http://genk2.vcmedia.vn/DlBlzccccccccccccE5CT3hqq3xN9o/Image/2013/10/Avatar2-27666.jpg";
			String location = "The Greenhouse";
			String to = "4:30PM Today";
			String from = "02:30";
			addExample("1", "David Buttsmith", userEmail, avatar, location, to, from);
			addExample("2", "Josh Harnem", userEmail, avatar, location, to, from);
			addExample("3", "Josh Harnem", userEmail, avatar, location, to, from);
			addExample("4", "Josh Harnem", userEmail, avatar, location, to, from);
			to = "4:30PM on 06 Dec";
			from = "10:30 AM";
			addExample("5", "Josh Harnem", userEmail, avatar, location, to, from);
		}
	}

	private void addExample(String _id, String _name, String userEmail, String avatar2, String location2, String to2, String from2) {
		ContentValues values = new ContentValues();
		values.put(id, _id);
		values.put(name, _name);
		values.put(userloginId, userEmail);
		values.put(avatar, avatar2);
		values.put(location, location2);
		values.put(from, from2);
		values.put(to, to2);

		String where = String.format("%s = '%s'", id, _id);

		if (!has(where)) {
			getContext().getContentResolver().insert(getContentUri(), values);
		}

	}

	public void add(String userId, String fromUseridlinkin, String name2, String avatar2, String company2, String companyposition2, String type2, String lat2, String lng2, String starttime,
			String endtime, String day2, String area, String status2) {

		ContentValues values = new ContentValues();
		values.put(userloginId, userId);
		values.put(fromUserId, fromUseridlinkin);
		values.put(name, name2);
		values.put(avatar, avatar2);
		values.put(companyname, company2);
		values.put(companyposition, companyposition2);
		values.put(type, type2);
		values.put(lat, lat2);
		values.put(lng, lng2);
		values.put(from, starttime);
		values.put(to, endtime);
		values.put(day, day2);
		values.put(status, status2);
		values.put(logtime, System.currentTimeMillis() + "");
		values.put(location, area);

		getContext().getContentResolver().insert(getContentUri(), values);

	}

	public String getLastPushItem(String userId) {
		Cursor cursor = querry(String.format("%s ='%s' and %s='%s'", userloginId, userId, status, "push"), String.format("%s desc", logtime));
		String _id = "";
		if (cursor != null) {
			if (cursor.moveToNext()) {
				_id = CommonAndroid.getString(cursor, _ID);
			}
			cursor.close();
		}
		return _id;
	}

	public void nothanks(String _id) {
		if (!CommonAndroid.isBlank(_id)) {
			ContentValues values = new ContentValues();
			values.put(status, "pushcancel");
			getContext().getContentResolver().update(getContentUri(), values, String.format("%s = '%s'", _ID, _id), null);
		}
	}

	public void pushOk(String _id) {
		if (!CommonAndroid.isBlank(_id)) {
			ContentValues values = new ContentValues();
			values.put(status, "pushinterested");
			getContext().getContentResolver().update(getContentUri(), values, String.format("%s = '%s'", _ID, _id), null);
		}
	}
}