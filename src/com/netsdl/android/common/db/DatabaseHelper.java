package com.netsdl.android.common.db;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.netsdl.android.common.Constant;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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
	public static Object[] getSingleColumn(ContentResolver contentResolver,
			Object[] selectionArgs, Class<?> clazz)
			throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		return getSingleColumn(contentResolver, selectionArgs, null,
				clazz);
	}

	// Getting single
	public static Object[] getSingleColumn(ContentResolver contentResolver,
			Object[] selectionArgs, String[] whereClause,
			Class<?> clazz) throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		String[] strs = new String[selectionArgs.length];

		for (int i = 0; i < selectionArgs.length; i++) {
			strs[i] = selectionArgs[i].toString();
		}
		return getSingleColumn(contentResolver,  strs, whereClause, clazz);
	}

	// Getting single
	public static Object[] getSingleColumn(ContentResolver contentResolver,
			 String[] selectionArgs, String[] whereClause,
			Class<?> clazz) throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {

		String tableName = (String) clazz.getField(Constant.TABLE_NAME).get(clazz);
		Uri uri = Uri.parse(Constant.PROVIDER_URI + tableName);
		
		String[] COLUMNS = (String[]) clazz.getField(Constant.COLUMNS).get(
				clazz);
		String[] KEYS = (String[]) clazz.getField(Constant.KEYS).get(clazz);

		Cursor cursor = null;
		try {
			cursor = contentResolver.query(uri, COLUMNS,
					getWhereClause(whereClause, KEYS), selectionArgs, null);

			return getSingleColumn(cursor, clazz);

		} finally {
			if (cursor != null)
				cursor.close();
		}

	}

	// Getting single
	public static Object[] getSingleColumn(SQLiteDatabase db,
			Object[] selectionArgs, Class<?> clazz)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		return getSingleColumn(db, selectionArgs, null, clazz);
	}

	// Getting single
	public static Object[] getSingleColumn(SQLiteDatabase db,
			Object[] selectionArgs, String[] whereClause, Class<?> clazz)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		String[] strs = new String[selectionArgs.length];

		for (int i = 0; i < selectionArgs.length; i++) {
			strs[i] = selectionArgs[i].toString();
		}
		return getSingleColumn(db, strs, whereClause, clazz);
	}

	// Getting single
	public static Object[] getSingleColumn(SQLiteDatabase db,
			String[] selectionArgs, String[] whereClause, Class<?> clazz)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		String tableName = (String) clazz.getField(Constant.TABLE_NAME).get(
				clazz);
		String[] COLUMNS = (String[]) clazz.getField(Constant.COLUMNS).get(
				clazz);
		String[] KEYS = (String[]) clazz.getField(Constant.KEYS).get(clazz);

		Cursor cursor = null;
		try {
			cursor = db.query(tableName, COLUMNS,
					getWhereClause(whereClause, KEYS), selectionArgs, null,
					null, null, null);
			return getSingleColumn(cursor, clazz);

		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}

	}

	// Getting single
	public static Object[] getSingleColumn(Cursor cursor, Class<?> clazz)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		String[] COLUMNS = (String[]) clazz.getField(Constant.COLUMNS).get(
				clazz);
		Class<?>[] TYPES = (Class<?>[]) clazz.getField(Constant.TYPES).get(
				clazz);

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
