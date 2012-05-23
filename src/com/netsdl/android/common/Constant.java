package com.netsdl.android.common;

import com.netsdl.android.common.db.StoreMaster;

import android.net.Uri;

public class Constant {
	public static final String URL = "url";
	public static final String VERSION = "version";
	public static final String ROWS = "rows";
	public static final String PROVIDER_AUTHORITY = "com.netsdl.android.init.provider.Provider";
	public static final String PROVIDER_URI = "content://" + PROVIDER_AUTHORITY+ "/";
	public static final Uri PROVIDER_URI_STORE_MASTER = Uri.parse(PROVIDER_URI + StoreMaster.TABLE_NAME);
}
