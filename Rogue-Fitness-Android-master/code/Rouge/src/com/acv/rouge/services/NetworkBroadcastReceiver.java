package com.acv.rouge.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

	public NetworkBroadcastReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable() || mobile.isAvailable()) {
			Intent service = new Intent(context, RougeService.class);
			Bundle extras = new Bundle();
			extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.NETWORK_ENABLE);
			service.putExtras(extras);
			context.startService(service);
		}
	}
}