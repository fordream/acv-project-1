package com.acv.rouge.db;

import org.json.JSONObject;

import z.base.CommonAndroid;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TrackYourOrderTable extends RougeTable {
	public TrackYourOrderTable(Context context) {
		super(context);
		addColumns(address);
		addColumns(type);
		addColumns(postalCode);
		addColumns(country);
		addColumns(email);
		addColumns(orderNumber);
		addColumns(date_update);
		addColumns(sourceOrderId);
		addColumns(ordersearch);
	}

	public static final String ordersearch = "ordersearch";
	public static final String sourceOrderId = "sourceOrderId";
	public static final String address = "address";
	public static final String postalCode = "postalCode";
	public static final String country = "country";
	public static final String email = "email";
	public static final String orderNumber = "orderNumber";
	public static final String date_update = "date_update";

	/**
	 * 0 delete 1 normal 2 ?
	 */
	public static final String type = "type";

	public Cursor querryNoDelete() {
		// %s = '%s' and storeSettingOnApp, getNowPositionLocation() + "",
		String where = String.format("(%s = '1' or %s = '2')", type, type, String.format("%s desc", date_update));
		return querry(where);
	}

	/**
	 * 
	 * @param xid
	 */
	public void removeTrackOrder(String orderNumber, String ordersearch, String location, String email) {
		ContentValues values = new ContentValues();
		// values.put(TrackYourOrderTable.orderNumber, orderNumber);
		values.put(type, "0");
		getContext().getContentResolver().update(getContentUri(), values, String.format("%s = '%s' and %s = '%s'", TrackYourOrderTable.orderNumber, orderNumber, storeSettingOnApp, location), null);
	}

	/**
	 * 
	 * @param email2
	 * @param orderNumber2
	 * @param positionLocation
	 * @return
	 */
	public String get_IdTrackOrder(String email2, String orderNumber2, String positionLocation) {
		String where = String.format("%s ='%s' and %s ='%s' and %s ='%s'", orderNumber, orderNumber2, email, email2, storeSettingOnApp, positionLocation);
		String _id = null;
		Cursor cursor = querry(where);

		if (cursor != null) {
			if (cursor.moveToNext())
				_id = CommonAndroid.getString(cursor, _ID);
			cursor.close();
		}
		return _id;
	}

	public String get_IdTrackOrderNowNoDelete(String email2, String orderNumber2) {
		String where = String.format("%s ='%s' and %s ='%s' and %s ='%s' and (%s = '2' or %s = '1') ", orderNumber, orderNumber2, email, email2, storeSettingOnApp, getNowPositionLocation(), type,
				type);
		String _id = null;
		Cursor cursor = querry(where);

		if (cursor != null) {
			if (cursor.moveToNext())
				_id = CommonAndroid.getString(cursor, _ID);
			cursor.close();
		}
		return _id;
	}

	/**
	 * 
	 * @param email2
	 * @param orderNumber2
	 * @param ordersearch2
	 * @param location
	 * @return
	 */
	public String get_IdTrackOrderNowNoDelete(String email2, String orderNumber2, String ordersearch2, String location) {
		String where = String.format("(%s ='%s' or %s ='%s') and %s ='%s' and %s ='%s' and (%s = '2' or %s = '1') ", orderNumber, orderNumber2, ordersearch, ordersearch2, email, email2,
				storeSettingOnApp, location, type, type);
		String _id = null;
		Cursor cursor = querry(where);

		if (cursor != null) {
			if (cursor.moveToNext())
				_id = CommonAndroid.getString(cursor, _ID);
			cursor.close();
		}
		return _id;
	}

	/**
	 * 
	 * @param order
	 * @param xstoreSettingOnApp
	 * @param orderSearch2
	 */
	public void addOrder(JSONObject order, String xstoreSettingOnApp, String orderSearch2) {
		String orderNumber2 = CommonAndroid.getString(order, "orderNumber");
		String email2 = CommonAndroid.getString(order, "email");
		String address2 = CommonAndroid.getString(order, "address");
		String postalCode2 = CommonAndroid.getString(order, "postalCode");
		String country2 = CommonAndroid.getString(order, "country");
		String sourceOrderId2 = CommonAndroid.getString(order, sourceOrderId);

		ContentValues values = new ContentValues();
		values.put(ordersearch, orderSearch2);
		values.put(sourceOrderId, sourceOrderId2);
		values.put(orderNumber, orderNumber2);
		values.put(email, email2);
		values.put(address, address2);
		values.put(postalCode, postalCode2);
		values.put(country, country2);
		values.put(type, "1");
		values.put(storeSettingOnApp, xstoreSettingOnApp);
		values.put(date_update, System.currentTimeMillis() + "");

		String where = String.format("%s ='%s' and %s ='%s' and %s = '%s'", orderNumber, orderNumber2, email, email2, storeSettingOnApp, xstoreSettingOnApp);
		if (!has(where)) {
			getContext().getContentResolver().insert(getContentUri(), values);
		} else {
			getContext().getContentResolver().update(getContentUri(), values, where, null);
		}
	}

	public String getOrderSearch(String email2, String order_id, String store) {
		String where = String.format("%s ='%s' and %s ='%s' and %s = '%s'", orderNumber, order_id, email, email2, storeSettingOnApp, store);
		String orderSearch = "";
		Cursor cursor = getContext().getContentResolver().query(getContentUri(), null, where, null, null);
		if (cursor != null)
			if (cursor.moveToNext()) {
				orderSearch = CommonAndroid.getString(cursor, TrackYourOrderTable.ordersearch);
			}
		cursor.close();
		return orderSearch;
	}
}