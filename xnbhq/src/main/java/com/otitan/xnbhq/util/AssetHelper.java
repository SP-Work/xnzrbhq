package com.otitan.xnbhq.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import com.titan.baselibrary.util.ProgressDialogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件复制工具类
 */
public class AssetHelper {
	
	// A mapping from assets database file to SQLiteDatabase object
	private static Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();

	// Context of application
	private static Context context = null;

	// Singleton Pattern
	private static AssetHelper mInstance = null;
	
	/**
	 * Initialize AssetsDatabaseManager
	 * 
	 * @param context
	 *            , context of application
	 */
	public static void initManager(Context context) {
		if (mInstance == null) {
			mInstance = new AssetHelper(context);
		}
	}

	/**
	 * Get a AssetsDatabaseManager object
	 * 
	 * @return, if success return a AssetsDatabaseManager object, else return
	 *          null
	 */
	public static AssetHelper getManager() {
		return mInstance;
	}

	@SuppressWarnings("static-access")
	private AssetHelper(Context context) {
		this.context = context;
	}

	/**
	 * @param ctx
	 * @throws IOException
	 */
	static public void CopyAsset(Context ctx, String path, String filename,String newname)throws IOException {
		AssetManager assetManager = ctx.getAssets();
		InputStream in = null;
		OutputStream out = null;

		// Copy files from asset folder to application folder
		try {
			in = assetManager.open(filename);
			File file = new File(path + "/" +newname);
			out = new FileOutputStream(file);
			copyFile(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (in != null) {
				in.close();
				in = null;
			}
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
			ProgressDialogUtil.stopProgressDialog(ctx);
		}
	}

	static private void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		// Copy from input stream to output stream
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@SuppressWarnings("unused")
	private static boolean copyAssetsToFilesystem(String assetsSrc, String des) {
		InputStream istream = null;
		OutputStream ostream = null;
		try {
			AssetManager am = context.getAssets();
			istream = am.open(assetsSrc);
			ostream = new FileOutputStream(des);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = istream.read(buffer)) > 0) {
				ostream.write(buffer, 0, length);
			}
			istream.close();
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (istream != null)
					istream.close();
				if (ostream != null)
					ostream.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
	 * Close all assets database
	 */
	@SuppressWarnings("static-access")
	static public void closeAllDatabase() {
		if (mInstance != null) {
			for (int i = 0; i < mInstance.databases.size(); ++i) {
				if (mInstance.databases.get(i) != null) {
					mInstance.databases.get(i).close();
				}
			}
			mInstance.databases.clear();
		}
	}

	/**
	 * Close assets database
	 * 
	 * @param dbfile the assets file which will be closed soon
	 * @return, the status of this operating
	 */
	static boolean closeDatabase(String dbfile) {
		if (databases.get(dbfile) != null) {
			SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
			db.close();
			databases.remove(dbfile);
			return true;
		}
		return false;
	}

}
