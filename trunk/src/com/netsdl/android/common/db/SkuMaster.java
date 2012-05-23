package com.netsdl.android.common.db;

import java.math.BigDecimal;

import android.content.Context;

public class SkuMaster extends DatabaseHandler {

	// Contacts Table Columns names
	public static final String COLUMN_SKU_ID = "Sku_id";
	public static final String COLUMN_CAT2 = "Cat2";
	public static final String COLUMN_CAT2_NAME = "Cat2_name";
	public static final String COLUMN_CAT3 = "Cat3";
	public static final String COLUMN_CAT3_NAME = "Cat3_name";
	public static final String COLUMN_ITEM_CD = "Item_cd";
	public static final String COLUMN_ORIG_ITEM_CD = "Orig_item_cd";
	public static final String COLUMN_ITEM_NAME = "Item_name";
	public static final String COLUMN_ITEM_CAT = "Item_cat";
	public static final String COLUMN_ITEM_CAT_NAME = "Item_cat_name";
	public static final String COLUMN_SKU_PROP_1 = "Sku_prop_1";
	public static final String COLUMN_SKU_PROP_1_NAME = "Sku_prop_1_name";
	public static final String COLUMN_PL1_DISP_INDEX = "Pl1_disp_index";
	public static final String COLUMN_SKU_PROP_2 = "Sku_prop_2";
	public static final String COLUMN_SKU_PROP_2_NAME = "Sku_prop_2_name";
	public static final String COLUMN_PL2_DISP_INDEX = "Pl2_disp_index";
	public static final String COLUMN_SKU_PROP_3 = "Sku_prop_3";
	public static final String COLUMN_SKU_PROP_3_NAME = "Sku_prop_3_name";
	public static final String COLUMN_PL3_DISP_INDEX = "Pl3_disp_index";
	public static final String COLUMN_SKU_CD = "Sku_cd";
	public static final String COLUMN_SKU_NAME = "Sku_name";
	public static final String COLUMN_BAR_CODE = "Bar_code";
	public static final String COLUMN_BAR_CODE_2 = "Bar_code_2";
	public static final String COLUMN_ITEM_COST = "Item_cost";
	public static final String COLUMN_ITEM_STD_PRICE = "Item_std_price";
	public static final String COLUMN_ITEM_PRICE = "Item_price";
	public static final String[] COLUMNS = { COLUMN_SKU_ID, COLUMN_CAT2,
			COLUMN_CAT2_NAME, COLUMN_CAT3, COLUMN_CAT3_NAME, COLUMN_ITEM_CD,
			COLUMN_ORIG_ITEM_CD, COLUMN_ITEM_NAME, COLUMN_ITEM_CAT,
			COLUMN_ITEM_CAT_NAME, COLUMN_SKU_PROP_1, COLUMN_SKU_PROP_1_NAME,
			COLUMN_PL1_DISP_INDEX, COLUMN_SKU_PROP_2, COLUMN_SKU_PROP_2_NAME,
			COLUMN_PL2_DISP_INDEX, COLUMN_SKU_PROP_3, COLUMN_SKU_PROP_3_NAME,
			COLUMN_PL3_DISP_INDEX, COLUMN_SKU_CD, COLUMN_SKU_NAME,
			COLUMN_BAR_CODE, COLUMN_BAR_CODE_2, COLUMN_ITEM_COST,
			COLUMN_ITEM_STD_PRICE, COLUMN_ITEM_PRICE };

	public static final String[] KEYS = { COLUMN_SKU_ID };

	private static final Class<Integer> TYPE_SKU_ID = Integer.class;
	private static final Class<String> TYPE_CAT2 = String.class;
	private static final Class<String> TYPE_CAT2_NAME = String.class;
	private static final Class<String> TYPE_CAT3 = String.class;
	private static final Class<String> TYPE_CAT3_NAME = String.class;
	private static final Class<String> TYPE_ITEM_CD = String.class;
	private static final Class<String> TYPE_ORIG_ITEM_CD = String.class;
	private static final Class<String> TYPE_ITEM_NAME = String.class;
	private static final Class<String> TYPE_ITEM_CAT = String.class;
	private static final Class<String> TYPE_ITEM_CAT_NAME = String.class;
	private static final Class<String> TYPE_SKU_PROP_1 = String.class;
	private static final Class<String> TYPE_SKU_PROP_1_NAME = String.class;
	private static final Class<Integer> TYPE_PL1_DISP_INDEX = Integer.class;
	private static final Class<String> TYPE_SKU_PROP_2 = String.class;
	private static final Class<String> TYPE_SKU_PROP_2_NAME = String.class;
	private static final Class<Integer> TYPE_PL2_DISP_INDEX = Integer.class;
	private static final Class<String> TYPE_SKU_PROP_3 = String.class;
	private static final Class<String> TYPE_SKU_PROP_3_NAME = String.class;
	private static final Class<Integer> TYPE_PL3_DISP_INDEX = Integer.class;
	private static final Class<String> TYPE_SKU_CD = String.class;
	private static final Class<String> TYPE_SKU_NAME = String.class;
	private static final Class<String> TYPE_BAR_CODE = String.class;
	private static final Class<String> TYPE_BAR_CODE_2 = String.class;
	private static final Class<BigDecimal> TYPE_ITEM_COST = BigDecimal.class;
	private static final Class<BigDecimal> TYPE_ITEM_STD_PRICE = BigDecimal.class;
	private static final Class<BigDecimal> TYPE_ITEM_PRICE = BigDecimal.class;
	public static final Class<?>[] TYPES = { TYPE_SKU_ID, TYPE_CAT2,
			TYPE_CAT2_NAME, TYPE_CAT3, TYPE_CAT3_NAME, TYPE_ITEM_CD,
			TYPE_ORIG_ITEM_CD, TYPE_ITEM_NAME, TYPE_ITEM_CAT,
			TYPE_ITEM_CAT_NAME, TYPE_SKU_PROP_1, TYPE_SKU_PROP_1_NAME,
			TYPE_PL1_DISP_INDEX, TYPE_SKU_PROP_2, TYPE_SKU_PROP_2_NAME,
			TYPE_PL2_DISP_INDEX, TYPE_SKU_PROP_3, TYPE_SKU_PROP_3_NAME,
			TYPE_PL3_DISP_INDEX, TYPE_SKU_CD, TYPE_SKU_NAME, TYPE_BAR_CODE,
			TYPE_BAR_CODE_2, TYPE_ITEM_COST, TYPE_ITEM_STD_PRICE,
			TYPE_ITEM_PRICE };

	public static final Class<?>[] KEY_TYPES = { TYPE_SKU_ID };

	public SkuMaster(Context context) {
		super(context);
	}

	// Contacts table name
	public static final String TABLE_NAME = "Sku_master";

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
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " (Sku_id int PRIMARY KEY  NOT NULL ,Cat2 varchar(32) ,Cat2_name varchar(256) ,Cat3 varchar(32) ,Cat3_name varchar(256) ,Item_cd varchar(32) ,Orig_item_cd varchar(32) ,Item_name varchar(128) ,Item_cat varchar(32) ,Item_cat_name varchar(256) ,Sku_prop_1 varchar(16) ,Sku_prop_1_name varchar(64) ,Pl1_disp_index int ,Sku_prop_2 varchar(16) ,Sku_prop_2_name varchar(64) ,Pl2_disp_index int ,Sku_prop_3 varchar(16) ,Sku_prop_3_name varchar(64) ,Pl3_disp_index int ,Sku_cd varchar(32) ,Sku_name varchar(128) ,Bar_code varchar(32) ,Bar_code_2 varchar(32) ,Item_cost decimal(16, 2) ,Item_std_price decimal(16, 2) ,Item_price decimal(16, 2))";
		return CREATE_TABLE;
	}

}
