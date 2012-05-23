package com.netsdl.android.common.db;

import android.content.Context;

public class PosTable extends DatabaseHandler {
	public static final String FLG_I = "I";
	public static final String FLG_P = "P";

	// Contacts Table Columns names
	public static final String COLUMN_UUID = "uuid";
	public static final String COLUMN_IP_FLG = "ip_flg";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_COUNT = "count";
	public static final String COLUMN_TIMPSTAMP = "timestamp";

	public static final String[] COLUMNS = { COLUMN_UUID, COLUMN_IP_FLG,
			COLUMN_ID, COLUMN_COUNT, COLUMN_TIMPSTAMP };

	public static final String[] KEYS = { COLUMN_UUID, COLUMN_IP_FLG,
			COLUMN_ID };

	private static final Class<String> TYPE_UUID = String.class;
	private static final Class<String> TYPE_IP_FLG = String.class;
	private static final Class<Integer> TYPE_ID = Integer.class;
	private static final Class<Integer> TYPE_COUNT = Integer.class;
	private static final Class<String> TYPE_TIMPSTAMP = String.class;

	public static final Class<?>[] TYPES = { TYPE_UUID, TYPE_IP_FLG, TYPE_ID,
			TYPE_COUNT, TYPE_TIMPSTAMP };

	public static final Class<?>[] KEY_TYPES = { TYPE_UUID, TYPE_IP_FLG,
			TYPE_ID };

	public PosTable(Context context) {
		super(context);
	}

	// Contacts table name
	public static final String TABLE_NAME = "pos_table";

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public String[] getColumns() {
		return COLUMNS;
	}

	@Override
	public String[] getKeys() {
		return KEYS;
	}

	@Override
	public Class<?>[] getTypes() {
		return TYPES;
	}

	@Override
	public Class<?>[] getKeyTypes() {
		return KEY_TYPES;
	}

	@Override
	public String getCreateTableSql() {
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ COLUMN_UUID + " varchar(256) NOT NULL " + "," + COLUMN_IP_FLG
				+ " varchar(256) NOT NULL " + "," + COLUMN_ID
				+ " int NOT NULL " + "," + COLUMN_COUNT + " int " + ","
				+ COLUMN_TIMPSTAMP + " varchar(256) " + ", primary key ( "
				+ COLUMN_UUID + " ," + COLUMN_IP_FLG + " , " + COLUMN_ID
				+ " ) " + ")";
		return CREATE_TABLE;
	}
}
