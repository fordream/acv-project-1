package com.acv.meetmarket.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import z.base.CommonAndroid;
import z.base.LogUtils;
import z.base.MeetMarketUtils;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.acv.meetmarket.MainActivity;
import com.acv.meetmarket.R;
import com.acv.meetmarket.db.DataStore;
import com.acv.meetmarket.db.HomeTable;
import com.acv.meetmarket.db.SkillsTable;
import com.acv.meetmarket.db.UserTable;
//import com.google.android.gms.internal.ex;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MeetMarketService extends Service {
	private UserTable userTable;
	private SkillsTable skillsTable;

	public enum TypeMeetMarketService {
		HOME, GETINFOR, PUSH_RECIVER, PUSH_CLICK, UPDATEINTORTOPARSE
	};

	@Override
	public void onCreate() {
		super.onCreate();
		DataStore.getInstance().init(this);
		userTable = new UserTable(this);
		skillsTable = new SkillsTable(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void loadUserFromParse(final String linkedInId, final String pictureUrl, final JSONArray skills) {
		new Thread() {
			public void run() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
				query.whereEqualTo("linkedinid", linkedInId);
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> commentList, ParseException e) {

						if (commentList != null && commentList.size() >= 1) {
							ParseObject object = commentList.get(0);
							objectId = object.getObjectId();
							DataStore.getInstance().init(MeetMarketService.this);
							DataStore.getInstance().save("pictureUrl", object.getString("icon"));
							DataStore.getInstance().save("skills", object.getString("skills"));
							DataStore.getInstance().save("about", object.getString("about"));
							DataStore.getInstance().save("cover", object.getString("cover"));
							loginResponse(true);
						} else {
							saveFirstTime(linkedInId, pictureUrl, skills);
						}
					}
				});
			};
		}.start();

	}

	private void loadObjectId(final String linkedInId) {
		new Thread() {
			public void run() {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
				query.whereEqualTo("linkedinid", linkedInId);
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> commentList, ParseException e) {

						if (commentList != null && commentList.size() >= 1) {
							ParseObject object = commentList.get(0);
							objectId = object.getObjectId();
							loginResponse(true);
						} else {
							loginResponse(false);
						}
					}
				});
			};
		}.start();
	}

	private void saveFirstTime(final String linkedInId, String pictureUrl, JSONArray skills) {
		final ParseObject group = new ParseObject("user");
		group.put("linkedinid", linkedInId);
		group.put("icon", pictureUrl);
		group.put("cover", pictureUrl);
		group.put("skills", skills.toString());
		group.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException arg0) {
				loadObjectId(linkedInId);
			}
		});
	}

	private void pushVersionToParse(final String linkedInId) {
		new Thread() {
			public void run() {
				String Parse_app_key = getResources().getString(R.string.parse_app_key);
				String Parse_client_key = getResources().getString(R.string.parse_client_key);
				Parse.initialize(MeetMarketService.this, Parse_app_key, Parse_client_key);
				ParseInstallation.getCurrentInstallation().put("linkedinid", linkedInId);
				ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
						} else {
						}
					}
				});
			};
		}.start();

	}

	private String objectId = "";

	private void updateToParse() {

		new Thread() {
			public void run() {
				if (!CommonAndroid.isBlank(objectId)) {
					final ParseObject group = new ParseObject("user");
					group.setObjectId(objectId);
					group.put("linkedinid", DataStore.getInstance().get("linkedinid", new UserTable(MeetMarketService.this).getUserId()));
					group.put("icon", DataStore.getInstance().get("pictureUrl", ""));
					group.put("cover", DataStore.getInstance().get("cover", ""));
					group.put("skills", DataStore.getInstance().get("skills", ""));
					group.put("about", DataStore.getInstance().get("about", ""));
					group.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
						}
					});
				}
			};
		}.start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(MeetMarketUtils.KEY.TYPE)) {
			TypeMeetMarketService type = (TypeMeetMarketService) intent.getExtras().getSerializable(MeetMarketUtils.KEY.TYPE);
			if (type == TypeMeetMarketService.HOME) {
				getHome();
			} else if (type == TypeMeetMarketService.GETINFOR) {
				getInfor();
			} else if (TypeMeetMarketService.PUSH_RECIVER == type) {
				push(true);
			} else if (type == TypeMeetMarketService.PUSH_CLICK) {
				push(false);
			} else if (type == TypeMeetMarketService.UPDATEINTORTOPARSE) {
				updateToParse();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void push(boolean isReciver) {
		if (CommonAndroid.checkApplicationRunning(this)) {
			sendBroadCastHaveUpdate();
		} else if (isReciver) {
			CommonAndroid.mGetNotification(this);
		} else {
			Intent intentActivity = new Intent(this, MainActivity.class);
			intentActivity.putExtra("push", "push");
			intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentActivity);
		}
	}

	private void sendBroadCastHaveUpdate() {
		Intent intent = new Intent(MeetMarketUtils.ACTION.UPDATEUIBYPUSH);
		sendBroadcast(intent);
	}

	private void getInfor() {
		StringBuilder url = new StringBuilder();
		url.append("https://api.linkedin.com/v1/people/~");
		url.append(":(");
		url.append("id");
		url.append(",first-name");
		url.append(",last-name");
		url.append(",picture-url");
		url.append(",skills");
		url.append(",headline");
		url.append(",publications");
		url.append(",last-modified-timestamp");
		url.append(",proposal-comments");
		url.append(",associations");
		url.append(",interests");
		url.append(",patents");
		url.append(",languages");
		url.append(",certifications");
		url.append(",educations");
		url.append(",date-of-birth");
		url.append(")");
		APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());

		apiHelper.getRequest(MeetMarketService.this, url.toString(), new ApiListener() {

			@Override
			public void onApiSuccess(ApiResponse response) {
				JSONObject object = response.getResponseDataAsJson();
				String headline = CommonAndroid.getString(object, "headline");
				String lastName = CommonAndroid.getString(object, "lastName");
				String firstName = CommonAndroid.getString(object, "firstName");
				String linkedInId = CommonAndroid.getString(object, "id");
				String pictureUrl = CommonAndroid.getString(object, "pictureUrl");

				DataStore.getInstance().save("headline", CommonAndroid.getString(object, "headline"));
				DataStore.getInstance().save("lastName", CommonAndroid.getString(object, "lastName"));
				DataStore.getInstance().save("firstName", CommonAndroid.getString(object, "firstName"));
				DataStore.getInstance().save("id", CommonAndroid.getString(object, "id"));

				if (userTable.has(String.format("%s = '%s'", UserTable.userloginId, linkedInId))) {
					userTable.updateUser(linkedInId, headline, lastName, firstName, pictureUrl, CommonAndroid.getAccessToken(MeetMarketService.this));
				} else {
					userTable.updateUser(linkedInId, headline, lastName, firstName, pictureUrl, CommonAndroid.getAccessToken(MeetMarketService.this));
				}

				JSONArray skills = new JSONArray();
				try {
					skills = object.getJSONObject("skills").getJSONArray("values");
					for (int i = 0; i < skills.length(); i++) {
						JSONObject json = skills.getJSONObject(i);
						String idSkill = CommonAndroid.getString(json, "id");
						String nameSkill = CommonAndroid.getString(json.getJSONObject("skill"), "name");
						skillsTable.updateSkill(linkedInId, idSkill, nameSkill, "0", false);
					}
				} catch (Exception e) {
				}

				pushVersionToParse(linkedInId);
				loadUserFromParse(linkedInId, pictureUrl, skills);
			}

			@Override
			public void onApiError(LIApiError error) {
				loginResponse(false);
			}
		});
	}

	private void loginResponse(boolean isSuccess) {
		Intent intent = new Intent(MeetMarketUtils.ACTION.ACTION_LOGIN);
		Bundle extras = new Bundle();
		extras.putBoolean("login", isSuccess);
		intent.putExtras(extras);
		sendBroadcast(intent);

	}

	private void getHome() {
		new Thread(new Runnable() {
			public void run() {
				new HomeTable(MeetMarketService.this).addExample();

				Intent intent = new Intent(MeetMarketUtils.ACTION.UPDATE_HOME);
				sendBroadcast(intent);
			}
		}) {
		}.start();
	}
}