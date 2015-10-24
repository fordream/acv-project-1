package z.base.api;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class ProductRestClientCallBack extends RestClientCallBack {
	private String store;

	public ProductRestClientCallBack(Context context, String store) {
		super(context);
		this.store = store;
	}

	public void onSucssesOnBackground(int responseCode, String responseMessage,
			String response) {
		super.onSucssesOnBackground(responseCode, responseMessage, response);
	};

}
