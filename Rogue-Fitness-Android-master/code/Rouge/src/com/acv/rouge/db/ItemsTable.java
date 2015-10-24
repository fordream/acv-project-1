package com.acv.rouge.db;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.LogUtils;
import android.content.ContentValues;
import android.content.Context;

public class ItemsTable extends RougeTable {
	public ItemsTable(Context context) {
		super(context);
		addColumns(_idPackage);
		addColumns(itemNumber);
		addColumns(name);
		addColumns(sku);
		addColumns(quantity);
	}

	public static final String _idPackage = "_idPackage";
	public static final String itemNumber = "itemNumber";
	public static final String name = "name";
	public static final String sku = "sku";
	public static final String quantity = "quantity";

	public void addItems(String _idPackage2, JSONArray items, String xstoreSettingOnApp) {
		for (int i = 0; i < items.length(); i++) {
			try {
				JSONObject item = items.getJSONObject(i);

				addItem(_idPackage2, item, xstoreSettingOnApp, i + "");
				// ContentValues values = new ContentValues();
				// values.put(_idPackage, _idPackage2);
				// values.put(name, CommonAndroid.getString(item, name));
				// values.put(sku, CommonAndroid.getString(item, sku));
				// values.put(quantity, CommonAndroid.getString(item,
				// quantity));
				// values.put(storeSettingOnApp, xstoreSettingOnApp);
				// String where =
				// String.format("%s = '%s' and %s ='%s'  and %s ='%s'", sku,
				// CommonAndroid.getString(item, sku), _idPackage, _idPackage2,
				// storeSettingOnApp, xstoreSettingOnApp);
				//
				// if (!has(where)) {
				// getContext().getContentResolver().insert(getContentUri(),
				// values);
				// } else {
				// getContext().getContentResolver().update(getContentUri(),
				// values, where, null);
				// }
			} catch (Exception e) {
			}

		}
	}

	public void addItem(String _idPackage2, JSONObject object, String xstoreSettingOnApp, String key) {
		ContentValues values = new ContentValues();
		values.put(_idPackage, _idPackage2);
		values.put(itemNumber, CommonAndroid.getString(object, itemNumber));
		values.put(name, CommonAndroid.getString(object, name));
		values.put(sku, CommonAndroid.getString(object, sku));
		values.put(quantity, CommonAndroid.getString(object, quantity));
		values.put(storeSettingOnApp, xstoreSettingOnApp);
		String where = String.format("%s = '%s' and %s ='%s'  and %s ='%s' and %s ='%s'", sku, CommonAndroid.getString(object, sku), _idPackage, _idPackage2, storeSettingOnApp, xstoreSettingOnApp,
				itemNumber, CommonAndroid.getString(object, itemNumber));
		if (!has(where)) {
			getContext().getContentResolver().insert(getContentUri(), values);
		} else {
			getContext().getContentResolver().update(getContentUri(), values, where, null);
		}
	}

}