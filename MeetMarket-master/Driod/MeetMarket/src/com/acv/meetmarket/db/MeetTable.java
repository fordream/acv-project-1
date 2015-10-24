package com.acv.meetmarket.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import z.base.CommonAndroid;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public abstract class MeetTable {
	public static final String _ID = "_id";
	private Context context;
	private String urlString;// = "content://" + DBProvider.PROVIDER_NAME + "/"
								// +
	// tableName;
	private Uri contentUri;// = Uri.parse(URL);

	// matcher
	private int USER_MATCHER = 1;
	private int USER_MATCHER_ID = 2;

	private void init(Context context) {
		this.context = context;

		USER_MATCHER = getIndex() * 10 + 1;
		USER_MATCHER_ID = getIndex() * 10 + 2;
		urlString = "content://" + DBProvider.PROVIDER_NAME + "/" + getTableName();
		contentUri = Uri.parse(urlString);

		addColumns(_ID);
		addColumns(userloginId);
	}

	public static final String userloginId = "userloginId";

	public MeetTable(Context context) {
		init(context);
	}

	public Context getContext() {
		return context;
	}

	public String getUrlString() {
		return urlString;
	}

	public Uri getContentUri() {
		return contentUri;
	}

	private Map<String, String> map = new HashMap<String, String>();

	public void addColumns(String columnName) {
		if (!CommonAndroid.isBlank(columnName) && !map.containsKey(columnName)) {
			if (columnName.equals("_id")) {
				map.put(columnName, " INTEGER PRIMARY KEY AUTOINCREMENT ");
			} else {
				map.put(columnName, " TEXT ");
			}
		}
	}

	public final String createDbTable() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE ").append(getTableName());
		builder.append("(");

		Set<String> keys = map.keySet();
		boolean first = true;

		for (String key : keys) {
			if (first) {
				builder.append(key).append(map.get(key));
			} else {
				builder.append(",").append(key).append(map.get(key));
			}

			first = false;
		}
		builder.append(")");

		return builder.toString();

	}

	public void addUriMatcher(UriMatcher uriMatcher, String PROVIDER_NAME) {
		uriMatcher.addURI(PROVIDER_NAME, getTableName(), USER_MATCHER);
		uriMatcher.addURI(PROVIDER_NAME, getTableName() + "/#", USER_MATCHER_ID);
	}

	public void getType(Map<Integer, String> mMap) {
		mMap.put(USER_MATCHER, "vnd.android.cursor.dir/com.acv.cheerz");
		mMap.put(USER_MATCHER_ID, "vnd.android.cursor.item/com.acv.cheerz");
	}

	public int update(int match, SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		if (USER_MATCHER == match) {
			return db.update(getTableName(), values, selection, selectionArgs);
		} else if (USER_MATCHER_ID == match) {
			return db.update(getTableName(), values, "_id = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
		} else {
			return -2;
		}
	}

	public int delete(int match, SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs) {
		if (USER_MATCHER == match) {
			return db.delete(getTableName(), selection, selectionArgs);
		} else if (USER_MATCHER_ID == match) {
			String id = uri.getPathSegments().get(1);
			return db.delete(getTableName(), "_id = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
		} else {
			return -2;
		}
	}

	public Cursor query(int match, SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(getTableName());
		if (USER_MATCHER == match) {
			return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		} else if (USER_MATCHER_ID == match) {
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			return null;
		}
	}

	public Uri insert(int match, SQLiteDatabase db, Uri uri, ContentValues values) {
		if (USER_MATCHER == match) {
			long rowID = db.insert(getTableName(), "", values);
			if (rowID > 0) {
				Uri _uri = ContentUris.withAppendedId(getContentUri(), rowID);
				return _uri;
			}
		} else if (USER_MATCHER_ID == match) {
			long rowID = db.insert(getTableName(), "", values);
			if (rowID > 0) {
				Uri _uri = ContentUris.withAppendedId(getContentUri(), rowID);
				return _uri;
			}
		}

		return null;
	}

	public String getTableName() {
		return getClass().getSimpleName();
	}

	/**
	 * get index of table
	 * 
	 * @return
	 */
	public final int getIndex() {
		SharedPreferences preferences = getContext().getSharedPreferences("skydb", 0);
		int max = preferences.getInt("maxOfTable", 0);
		int index = preferences.getInt(getTableName(), 0);

		if (index == 0) {
			max = max + 1;
			index = max;
		}

		preferences.edit().putInt("maxOfTable", max).commit();
		preferences.edit().putInt(getTableName(), index).commit();
		return index;// Math.abs(CommonAndroid.StringToInt(getTableName()));
	}

	public boolean has(String where) {
		boolean has = false;
		Cursor cursor = querry(where);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				has = true;
			}
			cursor.close();
		}
		return has;
	}

	public Cursor querry(String where) {
		return context.getContentResolver().query(getContentUri(), null, where, null, null);
	}

	public Cursor querry(String where, String sortOrder) {
		return context.getContentResolver().query(getContentUri(), null, where, null, sortOrder);
	}

}
