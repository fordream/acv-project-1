package com.acv.meetmarket.db;

import z.base.CommonAndroid;
import android.content.ContentValues;
import android.content.Context;

public class SkillsTable extends MeetTable {

	public SkillsTable(Context context) {
		super(context);
		addColumns(id);
		addColumns(name);
		addColumns(status);
		addColumns(updateTime);
	}

	public static final String id = "id";
	public static final String name = "name";
	public static final String updateTime = "updateTime";
	/*
	 * status = 0 new create, 1 modified, 2 delete
	 */
	public static final String status = "status";

	public void updateSkill(String idUser, String idSkill, String nameSkill, String status2, boolean needUpdate) {
		String where = String.format("%s = '%s' and %s ='%s'", userloginId, idUser, id, idSkill);
		ContentValues values = new ContentValues();
		values.put(userloginId, idUser);
		values.put(id, idSkill);
		values.put(name, nameSkill);
		values.put(updateTime, System.currentTimeMillis() + "");
		if (has(where)) {
			if (needUpdate) {
				if (!CommonAndroid.isBlank(status2)) {
					values.put(status, status2);
				}
				getContext().getContentResolver().update(getContentUri(), values, where, null);
			}
		} else {
			values.put(status, "0");
			getContext().getContentResolver().insert(getContentUri(), values);
		}
	}
}