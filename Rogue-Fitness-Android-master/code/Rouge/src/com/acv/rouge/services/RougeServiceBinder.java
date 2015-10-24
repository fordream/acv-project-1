package com.acv.rouge.services;

import android.os.Binder;

public class RougeServiceBinder extends Binder {
	private RougeService vnpService;

	public RougeServiceBinder(RougeService vnpService) {
		this.vnpService = vnpService;
	}

	public RougeService getVnpService() {
		return vnpService;
	}
}