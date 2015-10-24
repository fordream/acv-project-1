package com.acv.rouge.services.api;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.LogUtils;

import com.acv.rouge.db.ProductFeedTable;

import android.content.Context;

public class ProductRestClientCallBack extends RestClientCallBack {
	private String store;

	public ProductRestClientCallBack(Context context, String store) {
		super(context);
		this.store = store;
	}

	public void onSucssesOnBackground(int responseCode, String responseMessage, String response) {
		super.onSucssesOnBackground(responseCode, responseMessage, response);
		ProductFeedTable productFeedTable = new ProductFeedTable(getContext());
		try {
			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject productFeed = array.getJSONObject(i);
				productFeedTable.addProductFeed(store, productFeed);
			}
		} catch (Exception e) {
		}
	};

}
