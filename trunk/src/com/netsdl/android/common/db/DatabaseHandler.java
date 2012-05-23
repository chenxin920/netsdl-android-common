package com.netsdl.android.common.db;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "netsdlDB";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("DatabaseHandler", "new Database:" + DATABASE_NAME + " version:"
				+ DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL(getCreateTableSql());
		super.onOpen(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(getTableName(), "onCreate");
		db.execSQL(getCreateTableSql());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(getTableName(), "onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS " + getTableName());
		onCreate(db);
	}

	public void clear() {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + getTableName());
			db.execSQL(getCreateTableSql());
		} finally {
			if (db != null)
				db.close();
		}
	}

	public Map<String, Object> parserCSV(String[] datas) {
		return DatabaseHelper.parserCSV(datas, getColumns(), getTypes());
	}

	public Map<String, Object> parserCSV(String data) {
		String[] datas = data.split(",");
		return parserCSV(datas);
	}

	private void insert(Map<String, Object> mapData) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			String[] COLUMNS = getColumns();
			for (int i = 0; i < COLUMNS.length; i++) {
				Object obj = mapData.get(COLUMNS[i]);
				if (obj == null) {
					values.put(COLUMNS[i], "");
				} else if (obj instanceof String) {
					values.put(COLUMNS[i], obj.toString());
				} else if (obj instanceof Integer) {
					values.put(COLUMNS[i], (Integer) obj);
				} else if (obj instanceof BigDecimal) {
					values.put(COLUMNS[i], ((BigDecimal) obj).toString());
				}
			}
			// Inserting Row
			// db.insert(getTableName(), null, values);
			db.replace(getTableName(), null, values);
		} finally {
			if (db != null)
				db.close();
		}

	}

	public void insert(String data) {
		Map<String, Object> mapData = parserCSV(data);
		insert(mapData);
	}

	public void insert(String[] datas) {
		Map<String, Object> mapData = parserCSV(datas);
		insert(mapData);
	}

	public String getGroupByString(String[] strs) {
		if (strs == null || strs.length == 0)
			return null;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			if (i > 0)
				sb.append(" , ");
			sb.append(strs[i]);
		}
		return sb.toString();
	}

	public String getOrderByString(String[] strs) {
		return getOrderByString(strs, true);
	}

	public String getOrderByString(String[] strs, boolean isASC) {
		if (strs == null || strs.length == 0)
			return null;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			if (i > 0)
				sb.append(" , ");
			sb.append(strs[i]);
		}

		sb.append(isASC ? " ASC" : " DESC");
		return sb.toString();
	}

	public void deleteByKey(String data) {
		ContentValues values = new ContentValues();
		Map<String, Object> mapData = parserCSV(data);

		String[] COLUMNS = getColumns();

		for (int i = 0; i < COLUMNS.length; i++) {
			Object obj = mapData.get(COLUMNS[i]);
			if (obj == null) {
				values.put(COLUMNS[i], "");
			} else if (obj instanceof String) {
				values.put(COLUMNS[i], obj.toString());
			} else if (obj instanceof Integer) {
				values.put(COLUMNS[i], (Integer) obj);
			} else if (obj instanceof BigDecimal) {
				values.put(COLUMNS[i], ((BigDecimal) obj).toString());
			}
		}

		String[] KEYS = getKeys();
		String[] whereArgs = new String[KEYS.length];
		for (int i = 0; i < KEYS.length; i++) {
			whereArgs[i] = values.getAsString(KEYS[i]);
		}
		deleteByKey(whereArgs);
	}

	public void deleteByKey(String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(getTableName(), DatabaseHelper.getWhereClause(getKeys()),
					whereArgs);
		} finally {
			if (db != null)
				db.close();
		}
	}

	// Getting single
	public Object[] getSingleColumn(Object[] selectionArgs) {
		return getSingleColumn(selectionArgs, null);

	}

	// Getting single
	public Object[] getSingleColumn(Object[] selectionArgs, String[] whereClause) {
		String[] strs = new String[selectionArgs.length];

		for (int i = 0; i < selectionArgs.length; i++) {
			strs[i] = selectionArgs[i].toString();
		}
		return getSingleColumn(strs, whereClause);
	}

	// Getting single
	public Object[] getSingleColumn(String[] selectionArgs, String[] whereClause) {
		String[] COLUMNS = getColumns();
		Class<?>[] TYPES = getTypes();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			cursor = db.query(getTableName(), COLUMNS,
					DatabaseHelper.getWhereClause(whereClause, getKeys()),
					selectionArgs, null, null, null, null);
			return DatabaseHelper.getSingleColumn(cursor, COLUMNS, TYPES);

		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}

	}

	// Getting multi
	public Object[][] getMultiColumn(String[] selectionArgs,
			String[] whereClause, String[] groupBy, String having,
			String[] orderBy, String limit) {
		return getMultiColumn(selectionArgs, whereClause, groupBy, having,
				orderBy, limit, true);

	}

	// Getting multi
	public Object[][] getMultiColumn(String[] selectionArgs,
			String[] whereClause, String[] groupBy, String having,
			String[] orderBy, String limit, boolean isASC) {
		String[] COLUMNS = getColumns();
		Class<?>[] TYPES = getTypes();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			cursor = db.query(getTableName(), COLUMNS,
					DatabaseHelper.getWhereClause(whereClause, getKeys()),
					selectionArgs, getGroupByString(groupBy), having,
					getOrderByString(orderBy, isASC), null);
			return DatabaseHelper.getMultiColumn(cursor, COLUMNS, TYPES);
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}

	}

	public Object getColumnValue(Object[] objs, String column) {
		return DatabaseHelper.getColumnValue(objs, column, getColumns());
	}

	public abstract String getTableName();

	public abstract String[] getColumns();

	public abstract String[] getKeys();

	public abstract Class<?>[] getTypes();

	public abstract Class<?>[] getKeyTypes();

	public abstract String getCreateTableSql();

}
