package com.acv.meetmarket.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataStore {
	private static DataStore dataStore;

	private SharedPreferences preferences;

	private DataStore() {
	}

	public static DataStore getInstance() {
		if (dataStore == null) {
			dataStore = new DataStore();
		}

		return dataStore;
	}

	public void init(Context context) {
		if (preferences == null) {
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
	}

	public void save(String key, String value) {
		preferences.edit().putString(key, value).commit();
	}

	public void save(String key, boolean value) {
		preferences.edit().putBoolean(key, value).commit();
	}

	public void save(String key, int value) {
		preferences.edit().putInt(key, value).commit();
	}

	public void save(String key, float value) {
		preferences.edit().putFloat(key, value).commit();
	}

	public void save(String key, long value) {
		preferences.edit().putLong(key, value).commit();
	}

	public String get(String key, String _default) {
		if (preferences == null) {
			return "";
		}
		return preferences.getString(key, _default);
	}

	public boolean get(String key, boolean _default) {
		if (preferences == null) {
			return false;
		}
		return preferences.getBoolean(key, _default);
	}

	public float get(String key, float _default) {
		if (preferences == null) {
			return -1;
		}
		return preferences.getFloat(key, _default);
	}

	public long get(String key, long _default) {
		if (preferences == null) {
			return -1;
		}
		return preferences.getLong(key, _default);
	}

	public int get(String key, int _default) {
		if (preferences == null) {
			return -1;
		}
		return preferences.getInt(key, _default);
	}
}
