package com.acv.rouge.db;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PackagesTable extends RougeTable {
	public PackagesTable(Context context) {
		super(context);
		addColumns(carrier);
		addColumns(carrier_url);
		addColumns(deliveryDate);
		addColumns(packedDate);
		addColumns(pickUpDate);
		addColumns(processedDate);
		addColumns(qualityDate);
		addColumns(receivedDate);
		addColumns(status);
		addColumns(_idOrder);
		addColumns(trackingNumber);
		addColumns(id);
		addColumns(isDropShip);
		addColumns(isBackorder);
		addColumns(estimateDeliveryDate);

	}

	public static final String isDropShip = "isDropShip";
	public static final String isBackorder = "isBackorder";
	public static final String estimateDeliveryDate = "estimateDeliveryDate";
	public static final String id = "id";
	public static final String _idOrder = "_idOrder";
	public static final String carrier = "carrier";
	public static final String carrier_url = "carrier_url";
	public static final String deliveryDate = "deliveryDate";
	public static final String packedDate = "packedDate";
	public static final String pickUpDate = "pickUpDate";
	public static final String processedDate = "processedDate";
	public static final String qualityDate = "qualityDate";
	public static final String receivedDate = "receivedDate";

	public static final String trackingNumber = "trackingNumber";
	/**
	 * received processed packaged quality shipped delivered
	 * 
	 */
	public static final String status = "status";

	public void addPackage(String idOrder, JSONObject _package, String _id, String xstoreSettingOnApp) {
		ContentValues values = new ContentValues();
		values.put(carrier, CommonAndroid.getString(_package, carrier));
		values.put(isDropShip, CommonAndroid.getString(_package, isDropShip));
		values.put(isBackorder, CommonAndroid.getString(_package, isBackorder));
		values.put(estimateDeliveryDate, CommonAndroid.getString(_package, estimateDeliveryDate));
		values.put(carrier_url, CommonAndroid.getString(_package, carrier_url));
		values.put(deliveryDate, CommonAndroid.getString(_package, deliveryDate));
		values.put(packedDate, CommonAndroid.getString(_package, packedDate));
		values.put(pickUpDate, CommonAndroid.getString(_package, pickUpDate));
		values.put(processedDate, CommonAndroid.getString(_package, processedDate));
		values.put(qualityDate, CommonAndroid.getString(_package, qualityDate));
		values.put(receivedDate, CommonAndroid.getString(_package, receivedDate));
		values.put(status, CommonAndroid.getString(_package, status));
		values.put(_idOrder, idOrder);
		values.put(id, _id);
		values.put(storeSettingOnApp, xstoreSettingOnApp);
		try {
			// values.put(trackingNumber, strtrackingNumber);
			if (_package.has("trackingNumber")) {
				values.put(trackingNumber, _package.get("trackingNumber").toString());
			} else {
				values.put(trackingNumber, "");
			}
		} catch (Exception exception) {

		}

		String where = String.format("%s = '%s' and %s = '%s' and %s = '%s'", _idOrder, idOrder, id, _id, storeSettingOnApp, xstoreSettingOnApp);

		if (!has(where)) {
			getContext().getContentResolver().insert(getContentUri(), values);
		} else {
			getContext().getContentResolver().update(getContentUri(), values, where, null);
		}
	}

	public String get_IdPackageNow(String _idTrackOrder, String xid) {
		String where = String.format("%s ='%s' and %s ='%s' and %s ='%s' ", _idOrder, _idTrackOrder, id, xid, storeSettingOnApp, getNowPositionLocation());
		String _id = null;
		Cursor cursor = querry(where);

		if (cursor != null) {
			if (cursor.moveToNext())
				_id = CommonAndroid.getString(cursor, _ID);
			cursor.close();
		}
		return _id;
	}

	public String get_IdPackage(String _idTrackOrder, String xid, String positionLocation) {
		String where = String.format("%s ='%s' and %s ='%s' and %s ='%s' ", _idOrder, _idTrackOrder, id, xid, storeSettingOnApp, positionLocation);
		String _id = null;
		Cursor cursor = querry(where);

		if (cursor != null) {
			if (cursor.moveToNext())
				_id = CommonAndroid.getString(cursor, _ID);
			cursor.close();
		}
		return _id;
	}

	public Cursor loadPackage(String idTrackOrder) {
		String where = String.format("%s = '%s' and %s = '%s'", PackagesTable._idOrder, idTrackOrder + "", PackagesTable.storeSettingOnApp, getNowPositionLocation());
		return querry(where);
	}

	public Cursor loadPackage(String idTrackOrder, String location) {
		String where = String.format("%s = '%s' and %s = '%s'", PackagesTable._idOrder, idTrackOrder + "", PackagesTable.storeSettingOnApp, location);
		return querry(where);
	}
}