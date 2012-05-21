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

	public Object convertStringToOjbect(Class<?> type, String str) {
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

	public Map<String, Object> parserCSV(String[] datas) {
		String[] COLUMNS = getColumns();
		Class<?>[] TYPES = getTypes();

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < COLUMNS.length; i++) {
			map.put(COLUMNS[i], convertStringToOjbect(TYPES[i], datas[i]));
		}
		return map;
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

	public String getWhereClause() {
		return getWhereClause(null);
	}

	public String getWhereClause(String[] strs) {
		StringBuffer sb = new StringBuffer();
		String[] KEYS = null;
		if (strs == null)
			KEYS = getKeys();
		else
			KEYS = strs;
		for (int i = 0; i < KEYS.length; i++) {
			if (i > 0)
				sb.append(" and ");
			sb.append(KEYS[i]);
			sb.append(" = ?");
		}
		return sb.toString();
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
			db.delete(getTableName(), getWhereClause(), whereArgs);
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
		// Class<?>[] KEY_TYPES = getKeyTypes();
		// for (int i = 0; i < selectionArgs.length; i++) {
		// if (KEY_TYPES[i].equals(String.class)) {
		// strs[i] = selectionArgs[i].toString();
		// } else if (KEY_TYPES[i].equals(Integer.class)) {
		// strs[i] = selectionArgs[i].toString();
		// } else if (KEY_TYPES[i].equals(BigDecimal.class)) {
		// strs[i] = selectionArgs[i].toString();
		// }else{
		// strs[i] = selectionArgs[i].toString();
		// }
		//
		// }
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
					getWhereClause(whereClause), selectionArgs, null, null,
					null, null);
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
					getWhereClause(whereClause), selectionArgs,
					getGroupByString(groupBy), having,
					getOrderByString(orderBy, isASC), null);
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
		} finally {
			if (cursor != null)
				cursor.close();
			if (db != null)
				db.close();
		}

	}

	public int getColumnIndex(String str) {
		String[] COLUMNS = getColumns();
		for (int i = 0; i < COLUMNS.length; i++) {
			if (COLUMNS[i].equals(str))
				return i;
		}
		return -1;
	}

	public Class<?> getColumnType(String column) {
		int index = getColumnIndex(column);
		if (index < 0)
			return null;
		Class<?>[] TYPES = getTypes();
		return TYPES[index];
	}

	public Object getColumnValue(Object[] objs, String column) {
		if (objs == null)
			return null;
		int index = getColumnIndex(column);
		if (index < 0)
			return null;
		if (index >= objs.length)
			return null;
		return objs[index];

	}

	// public List<SkuMasterDatabaseHandler> getAllContacts() {
	// List<SkuMasterDatabaseHandler> skuMasterList = new
	// ArrayList<SkuMasterDatabaseHandler>();
	//
	// String selectQuery = "SELECT * FROM " + getTableName();
	//
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// SkuMasterDatabaseHandler skuMaster = new SkuMasterDatabaseHandler();
	// skuMaster.setSkuId(cursor
	// .getString(DatabaseHandler.INDEX_SKU_ID));
	// skuMaster.setCat2(cursor.getString(DatabaseHandler.INDEX_CAT2));
	// skuMaster.setCat2Name(cursor
	// .getString(DatabaseHandler.INDEX_CAT2_NAME));
	// skuMaster.setCat3(cursor.getString(DatabaseHandler.INDEX_CAT3));
	// skuMaster.setCat3Name(cursor
	// .getString(DatabaseHandler.INDEX_CAT3_NAME));
	// skuMaster.setItemCd(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_CD));
	// skuMaster.setOrigItemCd(cursor
	// .getString(DatabaseHandler.INDEX_ORIG_ITEM_CD));
	// skuMaster.setItemName(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_NAME));
	// skuMaster.setItemCat(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_CAT));
	// skuMaster.setItemCatName(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_CAT_NAME));
	// skuMaster.setSkuProp1(cursor
	// .getString(DatabaseHandler.INDEX_SKU_PROP_1));
	// skuMaster.setSkuProp1Name(cursor
	// .getString(DatabaseHandler.INDEX_SKU_PROP_1_NAME));
	// skuMaster.setPl1DispIndex(cursor
	// .getString(DatabaseHandler.INDEX_PL1_DISP_INDEX));
	// skuMaster.setSkuProp2(cursor
	// .getString(DatabaseHandler.INDEX_SKU_PROP_2));
	// skuMaster.setSkuProp2Name(cursor
	// .getString(DatabaseHandler.INDEX_SKU_PROP_2_NAME));
	// skuMaster.setPl2DispIndex(cursor
	// .getString(DatabaseHandler.INDEX_PL2_DISP_INDEX));
	// skuMaster.setSkuProp3(cursor
	// .getString(DatabaseHandler.INDEX_SKU_PROP_3));
	// skuMaster.setSkuProp3Name(cursor
	// .getString(DatabaseHandler.INDEX_SKU_PROP_3_NAME));
	// skuMaster.setPl3DispIndex(cursor
	// .getString(DatabaseHandler.INDEX_PL3_DISP_INDEX));
	// skuMaster.setSkuCd(cursor
	// .getString(DatabaseHandler.INDEX_SKU_CD));
	// skuMaster.setSkuName(cursor
	// .getString(DatabaseHandler.INDEX_SKU_NAME));
	// skuMaster.setBarCode(cursor
	// .getString(DatabaseHandler.INDEX_BAR_CODE));
	// skuMaster.setBarCode2(cursor
	// .getString(DatabaseHandler.INDEX_BAR_CODE_2));s
	// skuMaster.setItemCost(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_COST));
	// skuMaster.setItemStdPrice(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_STD_PRICE));
	// skuMaster.setItemPrice(cursor
	// .getString(DatabaseHandler.INDEX_ITEM_PRICE));
	// // Adding contact to list
	// skuMasterList.add(skuMaster);
	// } while (cursor.moveToNext());
	// }
	//
	// // return contact list
	// return skuMasterList;
	//
	// }

	public abstract String getTableName();

	public abstract String[] getColumns();

	public abstract String[] getKeys();

	public abstract Class<?>[] getTypes();

	public abstract Class<?>[] getKeyTypes();

	public abstract String getCreateTableSql();

}
