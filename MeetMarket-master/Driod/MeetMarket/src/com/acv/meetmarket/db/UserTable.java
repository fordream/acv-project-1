package com.acv.meetmarket.db;

import z.base.CommonAndroid;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UserTable extends MeetTable {

	public UserTable(Context context) {
		super(context);

		addColumns(headline);
		addColumns(firstName);
		addColumns(lastName);
		addColumns(pictureUrl);
		addColumns(status);
		addColumns(abount);
		addColumns(loaded);
		addColumns(accessToken);
	}

	public static final String lastModifiedTimestamp = "lastModifiedTimestamp";
	public static final String headline = "headline";
	public static final String firstName = "firstName";
	public static final String lastName = "lastName";
	public static final String pictureUrl = "pictureUrl";
	public static final String accessToken = "accessToken";
	/*
	 * status = 1 is login ,status = 0 not login,
	 */
	public static final String status = "status";
	public static final String abount = "abount";
	/**
	 * 1 loaded 0 need load
	 */
	public static final String loaded = "loaded";

	public void logout() {
		ContentValues values = new ContentValues();
		values.put(status, "0");
		getContext().getContentResolver().update(getContentUri(), values, null, null);
	}

	public void updateUser(String id2, String headline2, String lastName2, String firstName2, String pictureUrl2, String accessToken2) {
		logout();
		ContentValues values = new ContentValues();
		values.put(userloginId, id2);
		values.put(headline, headline2);
		values.put(lastName, lastName);
		values.put(firstName, firstName2);
		values.put(pictureUrl, pictureUrl2);
		values.put(accessToken, accessToken2);
		values.put(status, "1");
		if (has(String.format("%s = '%s'", userloginId, id2))) {
			getContext().getContentResolver().update(getContentUri(), values, String.format("%s = '%s'", userloginId, id2), null);
		} else {
			getContext().getContentResolver().insert(getContentUri(), values);
		}
	}

	/**
	 * 
	 * @param accessToken
	 * @param byAccessToken
	 *            false search by id, true search access token
	 * @return
	 */
	public Cursor getUser(String data, boolean byAccessToken) {
		return querry(String.format("%s = '%s'", byAccessToken ? accessToken : userloginId, data));
	}

	public String getUserId() {
		Cursor cursor = querry(String.format("%s ='%s'", status, "1"));
		String userId = "";
		if (cursor != null) {
			if (cursor.moveToNext()) {
				userId = CommonAndroid.getString(cursor, userloginId);
			}
			cursor.close();
		}
		return userId;
	}

}