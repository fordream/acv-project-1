package com.acv.rouge.db;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareReferentDB {
	@SuppressWarnings("unused")
	private Context context;
	private SharedPreferences preferences;

	public ShareReferentDB(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences(getNameDB(), 0);
	}

	public final String getNameDB() {
		return getClass().getName();
	}

	public boolean get(String string, boolean b) {
		return preferences.getBoolean(string, b);
	}

	public String get(String string, String b) {
		return preferences.getString(string, b);
	}

	public int get(String string, int i) {
		return preferences.getInt(string, i);
	}

	public void save(String string, int i) {
		preferences.edit().putInt(string, i).commit();
	}

	public void save(String string, boolean i) {
		preferences.edit().putBoolean(string, i).commit();
	}

	public void save(String string, String i) {
		preferences.edit().putString(string, i).commit();
	}
}