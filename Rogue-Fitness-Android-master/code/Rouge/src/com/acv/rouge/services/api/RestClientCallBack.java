package com.acv.rouge.services.api;

import com.acv.rouge.R;
import com.acv.rouge.db.SettingShareReferentDB;

import android.app.ProgressDialog;
import android.content.Context;

public class RestClientCallBack {
	private Context context;
	private String storeSettingOnApp = "0";

	public String getStoreSettingOnApp() {
		return storeSettingOnApp;
	}

	public Context getContext() {
		return context;
	}

	public RestClientCallBack(Context context) {
		this.context = context;
		storeSettingOnApp = new SettingShareReferentDB(getContext()).getPositionLocation() + "";
	}

	private ProgressDialog dialog;

	public void onStart() {
		if (useProgressDiablog) {
			dialog = ProgressDialog.show(context, null, context.getString(R.string.loading));
		}
	}

	/**
	 * 
	 * @param responseCode
	 * @param responseMessage
	 * @param response
	 */
	public void onSucsses(int responseCode, String responseMessage, String response) {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	/**
	 * 
	 * @param responseCode
	 * @param responseMessage
	 * @param response
	 */
	public void onSucssesOnBackground(int responseCode, String responseMessage, String response) {

	}

	private boolean useProgressDiablog;

	public void setUseProgressDiablog(boolean useProgressDiablog) {
		this.useProgressDiablog = useProgressDiablog;
	}

	public final String getString(int res) {
		return context.getString(res);
	}
}