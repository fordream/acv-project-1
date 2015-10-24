package com.acv.rouge.rouge;

import z.base.Fonts;
import z.base.RougeUtils;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.acv.rouge.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class RougeApplication extends Application implements ActivityLifecycleCallbacks {

	public RougeApplication() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Fonts.getIntance(this);
		
		// Parse.initialize(this, "ti9upIBXM7Ywrjd6njs6OlQMIQsdNCfFppeBwXiR",
		// "PgvGOej2RqgR58eEaaHrFRfz6RuUSItzxVfEMpWW");

		// Parse.initialize(this, "hyUjoxJfK9JwPLDVuyWu24PTJlzujfbFrNg8cw7y",
		// "MJ1JVJyOOp5uO10LjnQcCiQbikgAGJlMxndWtSxe");

		String Parse_app_key = getResources().getString(R.string.parse_app_key);
		String Parse_client_key = getResources().getString(R.string.parse_client_key);

		if (RougeUtils.ISDEBUG_PUSH) {
			Parse_app_key = getResources().getString(R.string.debug_parse_app_key);
			Parse_client_key = getResources().getString(R.string.debug_parse_client_key);
		}
		Parse.initialize(this, Parse_app_key, Parse_client_key);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
				} else {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
	}

	@Override
	public void onActivityPaused(Activity activity) {
	}

	@Override
	public void onActivityResumed(Activity activity) {
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	}

	@Override
	public void onActivityStarted(Activity activity) {
	}

	@Override
	public void onActivityStopped(Activity activity) {
	}
}