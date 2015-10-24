package com.acv.rouge.db;

import android.content.Context;

public class QuestionTable extends RougeTable {
	public QuestionTable(Context context) {
		super(context);
		addColumns(email);
		addColumns(name);
		addColumns(status);
		addColumns(ordernumber);
	}

	public static final String ordernumber = "ordernumber";
	public static final String email = "email";
	public static final String name = "name";

	/**
	 * status = 0 create status = 1 request status = 2 request fail status = 3
	 * request success
	 */
	public static final String status = "status";
}