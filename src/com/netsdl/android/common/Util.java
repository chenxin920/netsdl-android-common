package com.netsdl.android.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.netsdl.android.common.db.SkuMaster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Util {
	private static final String INIT_URL = "http://cyr.dip.jp/init.txt";

	public static Map<String, String> getInitInfo() throws URISyntaxException,
			ClientProtocolException, IOException {
		BufferedReader in = null;
		try {
			in = null;
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(INIT_URL));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			String line = in.readLine();
			Map<String, String> map = new HashMap<String, String>();
			while (line != null) {
				if (line.trim().length() == 0)
					continue;
				String[] strs = line.split(",");
				map.put(strs[0], strs[1]);
				line = in.readLine();
			}

			return map;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Map<String, String> getInfo(String url)
			throws URISyntaxException, ClientProtocolException, IOException {
		BufferedReader in = null;
		try {
			in = null;
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			Map<String, String> map = new HashMap<String, String>();
			String line = in.readLine();
			map.put(Constant.URL, line);
			line = in.readLine();
			map.put(Constant.VERSION, line);
			line = in.readLine();
			map.put(Constant.ROWS, line);
			return map;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void insertSqliteFromCSV(SkuMaster skuMaster, String url) {
		if (url == null)
			return;
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), "UTF-8"));

			String line = in.readLine();
			while (line != null) {
				if (line.trim().length() == 0)
					continue;

				skuMaster.deleteByKey(line);
				skuMaster.insert(line);

				line = in.readLine();
			}
			Log.d("initSqlite", "End");

		} catch (ClientProtocolException e) {
		} catch (URISyntaxException e) {
		} catch (IOException e) {
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (NoSuchFieldException e) {
		} finally {

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	boolean isTableExists(SQLiteDatabase db, String tableName) {
		if (tableName == null || db == null || !db.isOpen()) {
			return false;
		}
		Cursor cursor = null;
		try {
			cursor = db
					.rawQuery(
							"SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
							new String[] { "table", tableName });
			if (!cursor.moveToFirst()) {
				return false;
			}
			int count = cursor.getInt(0);
			cursor.close();
			return count > 0;
		} finally {
			if (cursor != null)
				cursor.close();
		}

	}

	public static String getMD5(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

}
