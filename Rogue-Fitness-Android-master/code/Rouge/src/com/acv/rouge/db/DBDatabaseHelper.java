package com.acv.rouge.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DBDatabaseHelper extends SQLiteOpenHelper {
	private List<RougeTable> list = new ArrayList<RougeTable>();
	private static final String DATABASE_NAME = "midb";

	private static final int DATABASE_VERSION = 4;

	public DBDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		addTable(new QuestionTable(context));
		addTable(new ProductFeedTable(context));
		addTable(new TrackYourOrderTable(context));
		addTable(new ItemsTable(context));
		addTable(new PackagesTable(context));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (RougeTable skypeTable : list) {
			db.execSQL(skypeTable.createDbTable());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		for (RougeTable skypeTable : list) {
			db.execSQL("DROP TABLE IF EXISTS " + skypeTable.getTableName());
		}

		onCreate(db);
	}

	public void addTable(RougeTable idol) {
		list.add(idol);
	}

	public void addUriMatcher(UriMatcher uriMatcher, String providerName) {

		for (RougeTable skypeTable : list) {
			skypeTable.addUriMatcher(uriMatcher, providerName);
		}

	}

	public Uri insert(int match, SQLiteDatabase db, Uri uri, ContentValues values) {
		for (RougeTable skypeTable : list) {
			Uri uri2 = skypeTable.insert(match, db, uri, values);
			if (uri2 != null) {
				return uri2;
			}
		}
		return null;
	}

	public Cursor query(int match, SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		for (RougeTable skypeTable : list) {
			Cursor uri2 = skypeTable.query(match, db, uri, projection, selection, selectionArgs, sortOrder);
			if (uri2 != null) {
				return uri2;
			}
		}

		return null;
	}

	public int delete(int match, SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs) {
		for (RougeTable skypeTable : list) {
			int count = skypeTable.delete(match, db, uri, selection, selectionArgs);
			if (count != -2) {
				return count;
			}
		}

		return -2;
	}

	public int update(int match, SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		for (RougeTable skypeTable : list) {
			int count = skypeTable.update(match, db, uri, values, selection, selectionArgs);
			if (count != -2) {
				return count;
			}
		}

		return -2;

	}

	public void getType(Map<Integer, String> mMap) {
		for (RougeTable skypeTable : list) {
			skypeTable.getType(mMap);
		}
	}
}