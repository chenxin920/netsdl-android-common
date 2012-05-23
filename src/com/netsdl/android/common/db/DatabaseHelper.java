package com.netsdl.android.common.db;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {

	public static Map<String, Object> parserCSV(String[] datas,
			String[] COLUMNS, Class<?>[] TYPES) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < COLUMNS.length; i++) {
			map.put(COLUMNS[i], convertStringToOjbect(TYPES[i], datas[i]));
		}
		return map;
	}

	public static Object convertStringToOjbect(Class<?> type, String str) {
		if (str == null)
			return null;
		if (type.equals(String.class)) {
			return str;
		} else if (type.equals(Integer.class)) {
			if ("NULL".equals(str)) {
				return null;
			} else {
				try {
					Integer iTemp = Integer.valueOf(str);
					return iTemp;
				} catch (NumberFormatException nfe) {
					return null;
				}
			}
		} else if (type.equals(BigDecimal.class)) {
			return new BigDecimal(str);
		}
		return str;
	}

	// Getting single
	public static Object[] getSingleColumn(SQLiteDatabase db,
			Object[] selectionArgs, String table, String[] COLUMNS,
			Class<?>[] TYPES, String[] KEYS) {
		return getSingleColumn(db, selectionArgs, null, table, COLUMNS, TYPES,
				KEYS);
	}

	// Getting single
	public static Object[] getSingleColumn(SQLiteDatabase db,
			Object[] selectionArgs, String[] whereClause, String table,
			String[] COLUMNS, Class<?>[] TYPES, String[] KEYS) {
		String[] strs = new String[selectionArgs.length];

		for (int i = 0; i < selectionArgs.length; i++) {
			strs[i] = selectionArgs[i].toString();
		}
		return getSingleColumn(db, strs, whereClause, table, COLUMNS, TYPES,
				KEYS);
	}

	// Getting single
	public static Object[] getSingleColumn(SQLiteDatabase db,
			String[] selectionArgs, String[] whereClause, String table,
			String[] COLUMNS, Class<?>[] TYPES, String[] KEYS) {
		Cursor cursor = null;
		try {
			cursor = db.query(table, COLUMNS,
					getWhereClause(whereClause, KEYS), selectionArgs, null,
					null, null, null);
			return getSingleColumn(cursor, COLUMNS, TYPES);

		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}

	}

	// Getting single
	public static Object[] getSingleColumn(Cursor cursor, String[] COLUMNS,
			Class<?>[] TYPES) {
		if (cursor != null && cursor.getCount() > 0)
			cursor.moveToFirst();
		else {
			return null;
		}

		Object[] objs = new Object[COLUMNS.length];
		for (int i = 0; i < COLUMNS.length; i++) {
			objs[i] = convertStringToOjbect(TYPES[i], cursor.getString(i));
		}
		return objs;
	}

	public static Object[][] getMultiColumn(Cursor cursor, String[] COLUMNS,
			Class<?>[] TYPES) {

		if (cursor != null && cursor.getCount() > 0)
			cursor.moveToFirst();
		else {
			return null;
		}

		Object[][] objss = new Object[cursor.getCount()][COLUMNS.length];
		int i = 0;
		do {
			objss[i] = new Object[COLUMNS.length];
			for (int j = 0; j < COLUMNS.length; j++) {
				objss[i][j] = convertStringToOjbect(TYPES[j],
						cursor.getString(j));
			}
			i++;
		} while (cursor.moveToNext());
		return objss;
	}

	public static String getWhereClause(String[] KEYS) {
		return getWhereClause(null, KEYS);
	}

	public static String getWhereClause(String[] strs, String[] KEYS) {
		StringBuffer sb = new StringBuffer();
		if (strs != null)
			KEYS = strs;
		for (int i = 0; i < KEYS.length; i++) {
			if (i > 0)
				sb.append(" and ");
			sb.append(KEYS[i]);
			sb.append(" = ?");
		}
		return sb.toString();
	}

	public static int getColumnIndex(String str, String[] COLUMNS) {
		for (int i = 0; i < COLUMNS.length; i++) {
			if (COLUMNS[i].equals(str))
				return i;
		}
		return -1;
	}

	public static Class<?> getColumnType(String column, String[] COLUMNS,
			Class<?>[] TYPES) {
		int index = getColumnIndex(column, COLUMNS);
		if (index < 0)
			return null;
		return TYPES[index];
	}

	public static Object getColumnValue(Object[] objs, String column,
			String[] COLUMNS) {
		if (objs == null)
			return null;
		int index = getColumnIndex(column, COLUMNS);
		if (index < 0)
			return null;
		if (index >= objs.length)
			return null;
		return objs[index];
	}

}
