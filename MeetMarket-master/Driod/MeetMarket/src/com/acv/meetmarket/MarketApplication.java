package com.acv.meetmarket;

import z.base.Fonts;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class MarketApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Fonts.getIntance(this);

		String Parse_app_key = getResources().getString(R.string.parse_app_key);
		String Parse_client_key = getResources().getString(R.string.parse_client_key);

		Parse.initialize(this, Parse_app_key, Parse_client_key);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
				} else {
				}
			}
		});
	}
}
