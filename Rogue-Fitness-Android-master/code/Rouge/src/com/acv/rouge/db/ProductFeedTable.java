package com.acv.rouge.db;

import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.LogUtils;

import com.acv.rouge.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ProductFeedTable extends RougeTable {
	public ProductFeedTable(Context context) {
		super(context);
		addColumns(url);
		addColumns(sort_order);
		addColumns(small_image);
		addColumns(image);
		addColumns(date_modified);
		addColumns(id);
	}

	public static final String id = "id";
	public static final String url = "url";
	public static final String sort_order = "sort_order";
	public static final String small_image = "small_image";
	public static final String image = "image";
	public static final String date_modified = "date_modified";

	public Cursor querryProductFeed() {
		return querry(String.format("%s = '%s'", storeSettingOnApp, getNowPositionLocation()), date_modified);
	}

	public Cursor querryProductFeed(String store) {
		return querry(String.format("%s = '%s'", storeSettingOnApp, store), date_modified);
	}

	public void addProductFeed(String store, JSONObject productFeed) {
		ContentValues values = new ContentValues();
		String mid = CommonAndroid.getString(productFeed, id);
		values.put(id, mid);
		values.put(sort_order, CommonAndroid.getString(productFeed, sort_order));
		values.put(small_image, CommonAndroid.getString(productFeed, small_image));
		values.put(image, CommonAndroid.getString(productFeed, image));
		values.put(date_modified, CommonAndroid.getString(productFeed, date_modified));
		values.put(url, CommonAndroid.getString(productFeed, url));
		values.put(storeSettingOnApp, store);

		String where = String.format("%s = '%s' and %s = '%s' ", id, mid, storeSettingOnApp, store);
		if (!has(where)) {
			getContext().getContentResolver().insert(getContentUri(), values);
		} else {
			getContext().getContentResolver().update(getContentUri(), values, where, null);
		}
	}
}