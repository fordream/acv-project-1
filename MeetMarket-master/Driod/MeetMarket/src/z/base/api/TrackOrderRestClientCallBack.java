package z.base.api;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import android.content.Context;

public class TrackOrderRestClientCallBack extends RestClientCallBack {
	private String location;
	private String ordersearch = "";

	public TrackOrderRestClientCallBack(Context context, String location,
			String ordersearch) {
		super(context);
		this.location = location;
		this.ordersearch = ordersearch;

	}

	public void onSucssesOnBackground(int responseCode, String responseMessage,
			String response) {
		super.onSucssesOnBackground(responseCode, responseMessage, response);

	};

}
