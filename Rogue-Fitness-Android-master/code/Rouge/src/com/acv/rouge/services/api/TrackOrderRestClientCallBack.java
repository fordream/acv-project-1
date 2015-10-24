package com.acv.rouge.services.api;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.LogUtils;
import android.content.Context;

import com.acv.rouge.db.ItemsTable;
import com.acv.rouge.db.PackagesTable;
import com.acv.rouge.db.TrackYourOrderTable;
import com.acv.rouge.services.RougeService;

public class TrackOrderRestClientCallBack extends RestClientCallBack {
	private String location;
	private String ordersearch = "";

	public TrackOrderRestClientCallBack(Context context, String location, String ordersearch) {
		super(context);
		this.location = location;
		this.ordersearch = ordersearch;

	}

	public void onSucssesOnBackground(int responseCode, String responseMessage, String response) {
		super.onSucssesOnBackground(responseCode, responseMessage, response);
		String storeSettingOnApp = getStoreSettingOnApp();
		if (!CommonAndroid.isBlank(location)) {
			storeSettingOnApp = location;
		}

		if ("true".equals(CommonAndroid.getStringJsonString(response, "success"))) {
			try {
				JSONObject order = new JSONObject(response).getJSONObject("order");

				/**
				 * add Order
				 */
				String orderNumber = CommonAndroid.getString(order, "orderNumber");
				String email = CommonAndroid.getString(order, "email");

				TrackYourOrderTable trackYourOrderTable = new TrackYourOrderTable(getContext());
				trackYourOrderTable.addOrder(order, storeSettingOnApp,ordersearch);

				// -----------------------------------------------
				String _idTrackOrder = trackYourOrderTable.get_IdTrackOrder(email, orderNumber, storeSettingOnApp);
				JSONArray packages = order.getJSONArray("packages");

				for (int i = 0; i < packages.length(); i++) {
					JSONObject _package = packages.getJSONObject(i);

					PackagesTable packagesTable = new PackagesTable(getContext());
					packagesTable.addPackage(_idTrackOrder, _package, i + "", storeSettingOnApp);

					String _idPackage = packagesTable.get_IdPackage(_idTrackOrder, i + "", storeSettingOnApp);

					if (_package.get("items") instanceof JSONArray) {
						new ItemsTable(getContext()).addItems(_idPackage, _package.getJSONArray("items"), storeSettingOnApp);
					} else {
						JSONObject items = _package.getJSONObject("items");
						Iterator<String> keys = items.keys();
						while (keys.hasNext()) {
							String key = keys.next();
							JSONObject object = items.getJSONObject(key);
							new ItemsTable(getContext()).addItem(_idPackage, object, storeSettingOnApp, key);
						}
					}
				}

			} catch (Exception e) {
			}
			RougeService.pushUpdateTrackOrderListToParse(getContext());
		}
	};

}
