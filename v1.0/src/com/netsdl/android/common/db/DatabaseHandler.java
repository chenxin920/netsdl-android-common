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

	public void insert(Map<String, Object> mapData)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			DatabaseHelper.insert(db, mapData, getClass());
		} finally {
			if (db != null)
				db.close();
		}

	}

	public void insert(String data) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException {
		Map<String, Object> mapData = parserCSV(data);
		insert(mapData);
	}

	public void insert(String[] datas) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException {
		Map<String, Object> mapData = parserCSV(datas);
		insert(mapData);
	}
	
	public void update(Map<String, Object> mapData,String[] whereArgs, String[] whereClause)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			DatabaseHelper.update(db, mapData,whereArgs,whereClause, getClass());
		} finally {
			if (db != null)
				db.close();
		}

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

	public Object[] getSingleColumn() throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException {
		return getSingleColumn(null);
	}

	public Object[] getSingleColumn(Object[] selectionArgs)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		return getSingleColumn(selectionArgs, null);
	}

	public Object[] getSingleColumn(Object[] selectionArgs, String[] whereClause)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		SQLiteDatabase db = null;
		try {
			db = getReadableDatabase();
			return DatabaseHelper.getSingleColumn(db, selectionArgs,
					whereClause, getClass());
		} finally {
			if (db != null)
				db.close();
		}
	}

	// Getting multi
	public Object[][] getMultiColumn(String[] groupBy, String having,
			String[] orderBy, String limit) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException {
		return getMultiColumn(new String[] {}, new String[] {}, groupBy,
				having, orderBy, limit, true);
	}

	// Getting multi
	public Object[][] getMultiColumn(String[] selectionArgs,
			String[] whereClause, String[] groupBy, String having,
			String[] orderBy, String limit) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, NoSuchFieldException {
		return getMultiColumn(selectionArgs, whereClause, groupBy, having,
				orderBy, limit, true);
	}

	// Getting multi
	public Object[][] getMultiColumn(String[] selectionArgs,
			String[] whereClause, String[] groupBy, String having,
			String[] orderBy, String limit, boolean isASC)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		String[] COLUMNS = getColumns();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			cursor = db.query(getTableName(), COLUMNS,
					DatabaseHelper.getWhereClause(whereClause, getKeys()),
					selectionArgs, DatabaseHelper.getGroupByString(groupBy),
					having, DatabaseHelper.getOrderByString(orderBy, isASC),
					limit);
			return DatabaseHelper.getMultiColumn(cursor, getClass());
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}

	}

	public abstract String getTableName();

	public abstract String[] getColumns();

	public abstract String[] getKeys();

	public abstract Class<?>[] getTypes();

	public abstract Class<?>[] getKeyTypes();

	public abstract String getCreateTableSql();

}
