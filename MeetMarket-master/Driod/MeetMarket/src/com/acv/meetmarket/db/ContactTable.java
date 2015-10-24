package com.acv.meetmarket.db;

import android.content.Context;

public class ContactTable extends MeetTable {

	public ContactTable(Context context) {
		super(context);

		addColumns(headline);
		addColumns(firstName);
		addColumns(id);
		addColumns(lastName);
		addColumns(pictureUrl);
		addColumns(skills);
	}

	public static final String headline = "headline";
	public static final String firstName = "firstName";
	public static final String id = "id";
	public static final String lastName = "lastName";
	public static final String pictureUrl = "pictureUrl";
	public static final String skills = "skills";
	/*
	 * status = 1 is login status = 0 not login
	 */
	public static final String status = "status";
}