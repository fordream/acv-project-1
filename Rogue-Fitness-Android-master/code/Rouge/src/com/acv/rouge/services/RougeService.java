package com.acv.rouge.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.RougeUtils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;

import com.acv.rouge.R;
import com.acv.rouge.db.PackagesTable;
import com.acv.rouge.db.SettingShareReferentDB;
import com.acv.rouge.db.TrackYourOrderTable;
import com.acv.rouge.rouge.MainActivity;
import com.acv.rouge.services.api.ProductRestClientCallBack;
import com.acv.rouge.services.api.RequestMethod;
import com.acv.rouge.services.api.RestClient;
import com.acv.rouge.services.api.TrackOrderRestClientCallBack;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

public class RougeService extends Service {
	public enum SERVICE_TYPE {
		NETWORK_ENABLE, API, SETTINGPUSHCHANGE, CHANGELANGUAGE, PUSHNOTIFICATION, CLICKPUSHNOTIFICATION, UPDATETRACKREGISTERTOPARSE, GETLINKYOUTOBE, LOADORDER, PRODUCT_FEED, UPLOADVERSIONTOPUSH
	};

	public static class KEY {
		public static final String SERVICE_TYPE = "SERVICE_TYPE";
	}

	private List<String> ordersConnected = new ArrayList<String>();

	@Override
	public void onCreate() {
		super.onCreate();
		iBinder = new RougeServiceBinder(this);
		runGetLink = false;
		ordersConnected.clear();

	}

	private IBinder iBinder;

	@Override
	public IBinder onBind(Intent arg0) {
		return iBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			if (intent.getExtras() != null && intent.getExtras().containsKey(RougeService.KEY.SERVICE_TYPE)) {
				SERVICE_TYPE type = (SERVICE_TYPE) intent.getExtras().getSerializable(RougeService.KEY.SERVICE_TYPE);

				if (type == SERVICE_TYPE.PUSHNOTIFICATION) {
					checkReceiverPush(intent);
				} else if (type == SERVICE_TYPE.CLICKPUSHNOTIFICATION) {
					checkClickNotification(intent);
				} else if (SERVICE_TYPE.UPDATETRACKREGISTERTOPARSE == type) {
					registerUserInfo();
				} else if (SERVICE_TYPE.GETLINKYOUTOBE == type) {
					getlinkYouTobe();
				} else if (SERVICE_TYPE.LOADORDER == type) {
					loadOrderContent(intent);
				} else if (SERVICE_TYPE.PRODUCT_FEED == type) {
					loadProductFeed(intent);
				} else if (SERVICE_TYPE.UPLOADVERSIONTOPUSH == type) {
					pushVersionToParse();
				}
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void pushVersionToParse() {
		String Parse_app_key = RougeService.this.getResources().getString(R.string.parse_app_key);
		String Parse_client_key = RougeService.this.getResources().getString(R.string.parse_client_key);

		if (RougeUtils.ISDEBUG_PUSH) {
			Parse_app_key = RougeService.this.getResources().getString(R.string.debug_parse_app_key);
			Parse_client_key = RougeService.this.getResources().getString(R.string.debug_parse_client_key);
		}
		Parse.initialize(RougeService.this, Parse_app_key, Parse_client_key);
		ParseInstallation.getCurrentInstallation().put("version_code", RougeUtils.VERSION + "_" + android.os.Build.VERSION.SDK_INT + "_" + android.os.Build.MODEL);
		ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
				} else {
				}
			}
		});

	}

	private void loadProductFeed(Intent intent) {

		final String store = intent.getStringExtra(RougeUtils.KEY.STORE);

		ProductRestClientCallBack productRestClientCallBack = new ProductRestClientCallBack(this, store) {
			@Override
			public void onSucsses(int responseCode, String responseMessage, String response) {
				super.onSucsses(responseCode, responseMessage, response);

				/**
				 * update UI
				 */
				Intent intent = new Intent(RougeUtils.ACTION.UPDATEFEED);
				intent.putExtra(RougeUtils.KEY.STORE, store);
				sendBroadcast(intent);
			}
		};
		// https://warmup.roguefitness.com/rogue_api/promo/list/store_code/default?token=7KAh3EPZqXWLxpqgKZ8PmVt
		String api = "promo/list/store_code/default";
		// Store Code => Store
		// default => US
		// apostore => APO
		// canada_eng => Canada
		// euroview => Europe
		// au_eng => Australia

		Map<String, String> apis = new HashMap<String, String>();
		apis.put("0", "promo/list/store_code/default");
		apis.put("1", "promo/list/store_code/canada_eng");
		apis.put("2", "promo/list/store_code/euroview");
		apis.put("3", "promo/list/store_code/au_eng");
		apis.put("4", "promo/list/store_code/apostore");
		if (apis.containsKey(store)) {
			api = apis.get(store);
		}

		RestClient client = new RestClient(api, this);
		// client.addParam("token", "7KAh3EPZqXWLxpqgKZ8PmVt");
		client.execute(RequestMethod.GET, productRestClientCallBack, false);
	}

	private boolean runGetLink = false;

	private void getlinkYouTobe() {

		if (runGetLink == true) {
			return;
		}
		runGetLink = true;
		RTSPUrlTask truitonTask = new RTSPUrlTask() {
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				runGetLink = false;
				SettingShareReferentDB settingShareReferentDB = new SettingShareReferentDB(RougeService.this);
				Intent intent = new Intent("updateLinkYouTobe");
				if (CommonAndroid.isBlank(result)) {
					intent.putExtra("message", getString(R.string.cannot_load_url_youtobe));
					sendBroadcast(intent);
				} else {
					intent.putExtra("message", "");
					sendBroadcast(intent);
					settingShareReferentDB.setLinkYouTobe(result);
				}
			}
		};
		truitonTask.execute("https://www.youtube.com/watch?v=_UfFYjUcIaI");
	}

	private void checkClickNotification(Intent intent) {
		String numberorder = intent.getStringExtra("numberorder");
		String email = intent.getStringExtra("email");
		String alert = intent.getStringExtra("alert");
		if (CommonAndroid.checkApplicationRunning(this)) {
			// send broadcast show dialog
			sendBroadCastHaveUpdate(email, numberorder, alert);
		} else {
			// start new application
			Intent intentActivity = new Intent(this, MainActivity.class);
			intentActivity.putExtra("email", email);
			intentActivity.putExtra("alert", alert);
			intentActivity.putExtra("numberorder", numberorder);
			intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentActivity);
		}
	}

	private void checkReceiverPush(final Intent intent) {

		String alert = intent.getStringExtra("alert");

		/**
		 * Check have a update
		 */

		String numberorder = intent.getStringExtra("numberorder");
		String email = intent.getStringExtra("email");
		String message = intent.getStringExtra("message");
		// update my tracker
		// updateTrackMyOrder();
		/**
		 * show notification
		 */
		// app is run, show dialog
		if (CommonAndroid.checkApplicationRunning(RougeService.this)) {
			// send broadcast show dialog
			sendBroadCastHaveUpdate(email, numberorder, alert);
		} else {
			// if app not run show notification
			// Your Order #1234 Has Been Updated To "Status"
			CommonAndroid.mGetNotification(RougeService.this, email, numberorder, alert, message);
		}
	}

	public void registerUserInfo() {
		new Thread() {
			public void run() {
				try {
					Cursor cursor = new TrackYourOrderTable(RougeService.this).querryNoDelete();
					JSONArray array = new JSONArray();
					while (cursor != null && cursor.moveToNext()) {
						JSONObject object = new JSONObject();

						String orderNumber = CommonAndroid.getString(cursor, TrackYourOrderTable.orderNumber);
						String email = CommonAndroid.getString(cursor, TrackYourOrderTable.email);
						String storeSettingOnApp = CommonAndroid.getString(cursor, TrackYourOrderTable.storeSettingOnApp);
						object.put("orderumber", orderNumber);
						object.put("email", email);
						object.put("store", storeSettingOnApp);

						object.put("packages", getPackages(CommonAndroid.getString(cursor, TrackYourOrderTable._ID), storeSettingOnApp));
						array.put(object);

					}
					try {
						String Parse_app_key = RougeService.this.getResources().getString(R.string.parse_app_key);
						String Parse_client_key = RougeService.this.getResources().getString(R.string.parse_client_key);

						if (RougeUtils.ISDEBUG_PUSH) {
							Parse_app_key = RougeService.this.getResources().getString(R.string.debug_parse_app_key);
							Parse_client_key = RougeService.this.getResources().getString(R.string.debug_parse_client_key);
						}
						Parse.initialize(RougeService.this, Parse_app_key, Parse_client_key);
						ParseInstallation.getCurrentInstallation().put("trackorders", array.toString());
						ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
							@Override
							public void done(ParseException e) {
								if (e == null) {
								} else {
								}
							}
						});

					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} catch (Exception exception) {

				}
			};
		}.start();

	}

	protected JSONArray getPackages(String _idTrackOrder, String storeSettingOnApp) {
		JSONArray array = new JSONArray();
		Cursor cursor = new PackagesTable(this).loadPackage(_idTrackOrder, storeSettingOnApp);
		while (cursor != null && cursor.moveToNext()) {
			String status = CommonAndroid.getString(cursor, PackagesTable.status);
			String trackingNumber = CommonAndroid.getString(cursor, PackagesTable.trackingNumber);
			JSONArray x = null;
			try {
				x = new JSONArray(trackingNumber);
			} catch (Exception exception) {
			}
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("status", status);

				if (x == null)
					jsonObject.put("trackingNumber", trackingNumber);
				else {
					jsonObject.put("trackingNumber", x);
				}
				array.put(jsonObject);
			} catch (Exception exception) {

			}
		}
		if (cursor != null) {
			cursor.close();
		}
		return array;
	}

	private void sendBroadCastHaveUpdate(String email, String numberorder, String alert) {
		Intent intent = new Intent("com.acv.rouge.push.PushBroadcastReceiver.updateUi");
		intent.putExtra("alert", alert);
		intent.putExtra("email", email);
		intent.putExtra("numberorder", numberorder);
		sendBroadcast(intent);
	}

	public static void pushChangeNotificationSetting(Context context) {
		if (context == null) {
			return;
		}
		Intent service = new Intent(context, RougeService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.SETTINGPUSHCHANGE);
		service.putExtras(extras);
		context.startService(service);
	}

	public static void pushChangeLanguage(Context context) {
		if (context == null) {
			return;
		}
		Intent service = new Intent(context, RougeService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.CHANGELANGUAGE);
		service.putExtras(extras);
		context.startService(service);
	}

	public static void pushChangeStatusOrder(Context context, String email, String numberOrder, String alert, String message) {
		if (context == null) {
			return;
		}
		Intent service = new Intent(context, RougeService.class);
		Bundle extras = new Bundle();
		extras.putString("email", email);
		extras.putString("numberorder", numberOrder);
		extras.putString("alert", alert);
		extras.putString("message", message);
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.PUSHNOTIFICATION);
		service.putExtras(extras);
		context.startService(service);
	}

	public static void pushUpdateTrackOrderListToParse(Context context) {
		if (context == null) {
			return;
		}
		Intent service = new Intent(context, RougeService.class);
		Bundle extras = new Bundle();
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.UPDATETRACKREGISTERTOPARSE);
		service.putExtras(extras);
		context.startService(service);
	}

	/**
	 * 
	 * @param context
	 * @param order_number
	 * @param email
	 * @param location
	 */
	public static void loadOrder(Context context, String order_number, String email, String location) {
		Intent service = new Intent(context, RougeService.class);
		Bundle extras = new Bundle();
		extras.putString("order_number", order_number);
		extras.putString("email", email);
		extras.putString("store", location);
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, RougeService.SERVICE_TYPE.LOADORDER);
		service.putExtras(extras);
		context.startService(service);
	}

	/**
	 * 
	 * @param intent
	 */

	private void loadOrderContent(Intent intent) {

		final String order_number = intent.getStringExtra("order_number");
		final String email = intent.getStringExtra("email");
		final String store = intent.getStringExtra("store");

		final String key = order_number + email + store;

		if (ordersConnected.contains(key)) {
			return;
		}

		RestClient client = new RestClient("getInfoByNumberAndEmail", this);
		client.addParam("order_number", order_number);
		client.addParam("email", email);
		// client.addParam("token", "7KAh3EPZqXWLxpqgKZ8PmVt");
		client.execute(RequestMethod.GET, new TrackOrderRestClientCallBack(this, store, order_number) {
			@Override
			public void onStart() {
				super.onStart();
				ordersConnected.add(key);
			}

			public void onSucsses(int responseCode, String responseMessage, String response) {
				super.onSucsses(responseCode, responseMessage, response);
				ordersConnected.remove(key);
				Intent intentUpdateOrders = new Intent(RougeUtils.ACTION.UPDATEORDERS);
				Bundle extras = new Bundle();
				extras.putString("order_number", order_number);
				extras.putString("email", email);
				extras.putString("store", store);
				intentUpdateOrders.putExtras(extras);
				sendBroadcast(intentUpdateOrders);
			};
		}, false);
	}

	public static Bundle createBunde(RougeService.SERVICE_TYPE type) {
		Bundle extras = new Bundle();
		extras.putSerializable(RougeService.KEY.SERVICE_TYPE, type);
		return extras;
	}
}