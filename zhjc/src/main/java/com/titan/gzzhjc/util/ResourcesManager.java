package com.titan.gzzhjc.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourcesManager implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String invodstr = "getVolumePaths";

	public static final String ROOT_MAPS = "/maps";
	public static String Code_Path = "";
	private static Context context;
	private static String packageName;
	public static final String image = "/image";
	public static final String otms = "/otms";
	public static final String TXT = "/txt";
	public static final String otitan_map = "/otitan.map";
	public static final String excel = "/excel";
	public static final String sqlite = "/sqlite";
	public static final String otitan = "/otitan";
	public static final String slzylxqc = "/连续清查";
	public static final String qmtp = "/签名图片";
	public static final String ydwzt = "/样地位置图";
	public static final String yindwzt = "/引点位置图";
	public static final String ydtp = "/样地图片";
	public static final String ymtp = "/样木图片";
	private static ResourcesManager resourcesManager;
	public static final String NAME = "NAME"; // 图片名字键
	public static final String BITMAP = "BITMAP"; // 图片BITMAP键


	public static final String SHP = "/shp";
	public static final String DB = "/db";
	public static final String city = "/cityJson";

	public synchronized static ResourcesManager getInstance(Context context){
		if (resourcesManager == null) {
			resourcesManager = new ResourcesManager(context);
		}
		packageName = context.getPackageName();
		Code_Path = ROOT_MAPS + packageName + "/maps";
		return resourcesManager;
	}

	/**
	 * Reset the {@link ResourcesManager}.
	 */
	public static void resetManager() {
		resourcesManager = null;
	}

	private ResourcesManager(Context context) {
		this.context = context;
	}

	/** 获取手机内部存储地址和外部存储地址 */
	public static String[] getMemoryPath() {

		StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		String[] paths = null;
		try {
			paths = (String[]) sm.getClass().getMethod(invodstr, new Class[0]).invoke(sm, new Object[]{});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return paths;
	}

	/** 获取基础地图的本地路径 */
	public String getTitlePath() {
		String name = otitan_map + "/title.tpk";
		return getFilePath(name);
	}

	public String getLayerPath(String name){
		String path = otitan_map + "/"+name;
		return getFilePath(path);
	}

	/** 创建系统中使用的文件夹 */
	public void createFolder() {
		String[] root = getMemoryPath();
		/* 在设备根目录中创建maps文件夹 */
		boolean flag = Util.isRoot();
		if(!flag){
			createFolder(root[0] + ROOT_MAPS);
			createFolder(root[0] + ROOT_MAPS + otitan_map);
			createFolder(root[0] + ROOT_MAPS + otms);
			createFolder(root[0] + ROOT_MAPS + sqlite);
		}else {
			createFolder(root[1] + ROOT_MAPS);
			createFolder(root[1] + ROOT_MAPS + otitan_map);
			createFolder(root[1] + ROOT_MAPS + otms);
			createFolder(root[1] + ROOT_MAPS + sqlite);
		}


	}

	/** 创建文件夹 */
	public static void createFolder(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;
	}

	/** 获取导航数据地址 */
	public String getNavigationDataPath(String path) {
		return getFilePath(path);
	}

	/** 取文件可用地址 */
	public static String getFilePath(String path) {
		String dataPath = "文件可用地址";
		String[] memoryPath = getMemoryPath();
		for (int i = 0; i < memoryPath.length; i++) {
			File file = new File(memoryPath[i] + ROOT_MAPS + path);
			if (file.exists() && file.isFile()) {
				dataPath = memoryPath[i] + ROOT_MAPS + path;
				break;
			}
		}
		return dataPath;
	}

	/*获取Shp文件的地址*/
	public static String getShpPath(String keyword){
		return getFolderPath(SHP+"/"+keyword);
	}
	/*获取Shp文件的地址*/
	public static String getShpPath(){
		return getFolderPath(SHP);
	}
	/*通过关键字获取Shp文件*/
	public static List<File> getShpFiles(String keyword,boolean ff){
		String path = "";
		if(ff){
			path = getShpPath();
		}else{
			path = getShpPath(keyword);
		}
		if(path.equalsIgnoreCase("文件可用地址")){
			return new ArrayList<>();
		}
		File[] files = new File(path).listFiles();
		List<File> list = new ArrayList<>();
		for(int i=0;i<files.length;i++){
			String name = files[i].getName();
			boolean flag = name.endsWith(".shp");
			if(!flag){
				continue;
			}
			if(ff){
				if(name.contains(keyword)){
					list.add(files[i]);
				}
			}else{
				list.add(files[i]);
			}

		}
		return list;
	}

	/** 获取文件夹可用地址 */
	public static String getFolderPath(String path) {
		String dataPath = "文件夹可用地址";
		String[] memoryPath = getMemoryPath();
		for (int i = 0; i < memoryPath.length; i++) {
			File file = new File(memoryPath[i] + ROOT_MAPS + path);
			if (file.exists()) {
				dataPath = memoryPath[i] + ROOT_MAPS + path;
				break;
			} else {
				if (path.equals("")) {
					file.mkdirs();
				}
			}
		}
		return dataPath;
	}
	/**根据设备是否root获取跟目路*/
	public static String getTootPath(){
		String dataPath = "文件夹可用地址";
		String[] memoryPath = getMemoryPath();
		boolean flag = Util.isRoot();
		if(flag){
			File file = new File(memoryPath[1] + "/test");
			file.mkdirs();
			if (file.exists()) {
				dataPath = memoryPath[1];
				file.delete();
			} else {
				dataPath = memoryPath[0];
			}
		}else{
			dataPath = memoryPath[0];
		}
		return dataPath;
	}

	/** 获取文件夹可用地址若不存在则创建文件夹 */
	public static String getFolderPaths(String name) {
		String dataPath = "文件夹可用地址";
		String[] memoryPath = getMemoryPath();
		//[/storage/emulated/0, /storage/extSdCard,
		boolean flag = Util.isRoot();
		if(flag){
			File file = new File(memoryPath[1] + name);
			if (file.exists()) {
				dataPath = file.getPath();
			} else {
				file.mkdirs();
				if(file.exists()){
					dataPath = file.getPath();
				}else{
					dataPath = creatExtSdCard(memoryPath[0] + name);
				}
			}
		}else{
			dataPath = creatExtSdCard(memoryPath[0] + name);
		}
		return dataPath;
	}

	public void creatEmulat(){

	}

	public static String creatExtSdCard(String path){
		File file = new File(path);
		if (file.exists()) {
			return file.getPath();
		} else {
			file.mkdirs();
			return file.getPath();
		}
	}

	/** 获取贵阳市基础底图 */
	public String getArcGISLocalTiledLayerPath() {
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/title.tpk";
		arcGISLocalTiledLayerPath = getFilePath(str);
		return arcGISLocalTiledLayerPath;
	}

	/** 获取贵阳市影像图 */
	public String getArcGISLocalImageLayerPath() {
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/image.tpk";
		arcGISLocalTiledLayerPath = getFilePath(str);
		return arcGISLocalTiledLayerPath;
	}

	/** 获取贵阳市县乡村界 */
	public String getArcGISLocalyyLayerPath() {
		String arcGISLocalTiledLayerPath = "";
		String str = otitan_map + "/xzqh.tpk";
		arcGISLocalTiledLayerPath = getFilePath(str);
		return arcGISLocalTiledLayerPath;
	}

	public static String getDataBase(String filename)
			throws FileNotFoundException {
		File db = null;
		if (filename == null)
			return "";
		db = new File(getFilePath(DB + "/" + filename));
		if (db.exists()) {
			return db.toString();
		}
		throw new FileNotFoundException("文件不存在");
	}



	/** 获取二调数据库 */
	public static String getEDDataBase(Context ctx, String filename)
			throws FileNotFoundException {
		File db = null;
		if (filename == null)
			return "";
		db = new File(getFilePath(otms + "/二调/" + filename));
		if (db.exists()) {
			return db.toString();
		}
		throw new FileNotFoundException("文件不存在");
	}

	/** 获取影像文件列表 */
	public List<File> getImgTitlePath() {
		return getPahts(otitan_map, "image");
	}

	public List<File> getPahts(String path, String keyword) {
		List<File> list = new ArrayList<File>();
		String[] array = getMemoryPath();
		for (int i = 0; i < array.length; i++) {
			File file = new File(array[i] + ROOT_MAPS + path);
			if (file.exists()) {
				for (int m = 0; m < file.listFiles().length; m++) {
					if (file.listFiles()[m].isFile()
							&& file.listFiles()[m].getName().contains(keyword)) {
						list.add(file.listFiles()[m]);
					}
				}
			}
		}
		return list;
	}

	/** 获取otms文件夹下的文件夹 */
	public List<File> getOtmsFolder() {
		String path = otms;
		File[] files = new File(getFolderPath(path)).listFiles();
		List<File> groups = new ArrayList<File>();
		int files_lenght = files.length;
		for (int i = 0; i < files_lenght; i++) {
			if (!files[i].isDirectory()) {
				continue;
			}
			groups.add(files[i]);
		}
		return groups;
	}

	public List<File> getOtmsData(File[] files) {
		List<File> list = new ArrayList<File>();
		if (files.length > 0) {
			for (File file : files) {
				if (file.isDirectory())
					continue;
				if (file.getName().endsWith(".otms")
						|| file.getName().endsWith(".geodatabase")) {
					list.add(file);
				}
			}
		}
		return list;
	}

	public void saveSBH(Context context, String SBH, String XLH) {

		File path = Environment.getExternalStorageDirectory();

		File file = new File(path, SBH + ".PUID");
		if (file.exists())
			return;
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			String msg = new String("璁惧鍙?:" + SBH + "\n" + "搴忓垪鍙凤細" + XLH);
			outputStream.write(msg.getBytes("UTF-8"));// "UTF-8"
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public File[] getImage(String path) {
		String mapPath = "/image/" + path;
		File[] files = new File(getFilePath(mapPath)).listFiles();
		return files;
	}

	public void saveJSon(Context context, String json) {

		File path = Environment.getExternalStorageDirectory();

		File file = new File(path, "test.txt");

		if (file.exists())
			file.delete();
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(json.getBytes("UTF-8"));// "UTF-8"
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** 获取文件夹中图片 */
	public static List<File> getImages(String path, String objectid) {
		List<File> list = new ArrayList<File>();
		File file = new File(path);
		File[] subFile = file.listFiles();
		for (int i = 0; i < subFile.length; i++) {
			// 判断是否为文件夹
			if (!subFile[i].isDirectory()) {
				String tempName = subFile[i].getName();
				if (tempName.trim().toLowerCase().endsWith(".jpg")
						&& tempName.contains(objectid)) {
					File thefile = new File((path + "/" + tempName));
					list.add(thefile);
				}
			}
		}
		return list;
	}

	/** 获取某个目录下图片 */
	public static List<File> getImages(String path) {
		List<File> list = new ArrayList<File>();
		File file = new File(path);
		File[] subFile = file.listFiles();
		for (int i = 0; i < subFile.length; i++) {
			// 判断是否为文件夹
			if (!subFile[i].isDirectory()) {
				String tempName = subFile[i].getName();
				if (tempName.trim().toLowerCase().endsWith(".jpg")) {
					File thefile = new File((path + "/" + tempName));
					list.add(thefile);
				}
			}
		}
		return list;
	}

	/** 获取某个目录下图片 */
	public static List<File> getImages(String path,String[] array) {
		List<File> list = new ArrayList<File>();
		File file = new File(path);
		File[] subFile = file.listFiles();
		for (File file2 : subFile) {
			// 判断是否为文件夹
			if (file2.isDirectory()) {
				continue;
			}
			for (int i = 0; i < array.length; i++) {
				String tempName = file2.getName();
				if (tempName.equals(array[i]) && tempName.trim().toLowerCase().endsWith(".jpg")) {
					File thefile = new File((path + "/" + tempName));
					list.add(thefile);
					continue;
				}
			}
		}
		return list;
	}

	/** 读取本地文件夹下图片bitmap */
	public static Bitmap getImageBitmap(String ydh, String path) {
		// 函数GetTestXlsFileName功能：遍历fileAbsolutePath目录下的所有指定扩展名文件
		// 并将文件名保存在Vector中
		String filepath = "";
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists()) {
			File[] subFile = file.listFiles();
			for (int i = 0; i < subFile.length; i++) {
				// 判断是否为文件夹
				if (!subFile[i].isDirectory()) {
					String tempName = subFile[i].getName();
					// 判断是否为xls或xlsx结尾
					if (tempName.trim().toLowerCase().endsWith(".jpg")
							&& tempName.contains(ydh)) {
						filepath = path + "/" + tempName;
						break;
					}
				}

			}
			if (!"".equals(filepath)) {
				File file1 = new File(filepath);
				if (file1.exists()) {
					bitmap = BitmapFactory.decodeFile(filepath);
				}
			}
		}

		return bitmap;
	}

	/** 获取路径中所有图片bitmap和名称 */
	public static List<HashMap<String, Object>> getImageBitmapAll(String ydh,
																  String path) {
		// 函数GetTestXlsFileName功能：遍历fileAbsolutePath目录下的所有指定扩展名文件
		// 并将文件名保存在Vector中
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String filepath = "";
		File file = new File(path);
		if (file.exists()) {
			File[] subFile = file.listFiles();
			for (int i = 0; i < subFile.length; i++) {
				// 判断是否为文件夹
				if (!subFile[i].isDirectory()) {
					String tempName = subFile[i].getName();
					// 判断是否为xls或xlsx结尾
					if (tempName.trim().toLowerCase().endsWith(".jpg")
							&& tempName.contains(ydh)) {
						Bitmap bitmap = null;
						filepath = path + "/" + tempName;
						if (!"".equals(filepath)) {
							File file1 = new File(filepath);
							if (file1.exists()) {
								try {
									FileInputStream is = new FileInputStream(
											filepath);
									BitmapFactory.Options options = new BitmapFactory.Options();

									options.inJustDecodeBounds = false;

									options.inSampleSize = 10; // width，hight设为原来的十分一

									bitmap = BitmapFactory.decodeStream(is,
											null, options);
									is.close();
									// bitmap =
									// BitmapFactory.decodeFile(filepath);
								} catch (Exception e) {
								}
							}
						}
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(NAME, tempName);
						map.put(BITMAP, bitmap);
						list.add(map);
					}
				}

			}
		}
		return list;
	}

	/**
	 * 获取路径中含有固定样式图片数量
	 *
	 * @param path
	 *            路径
	 * @param content
	 *            图片名字中含有的特征字符串
	 * @return
	 */
	public static int getImageNumber(String path, String content) {
		// 函数GetTestXlsFileName功能：遍历fileAbsolutePath目录下的所有指定扩展名文件
		// 并将文件名保存在Vector中
		int num = 0;
		File file = new File(path);
		if (file.exists()) {
			File[] subFile = file.listFiles();
			for (int i = 0; i < subFile.length; i++) {
				// 判断是否为文件夹
				if (!subFile[i].isDirectory()) {
					String tempName = subFile[i].getName();
					// 判断是否为xls或xlsx结尾
					if (tempName.trim().toLowerCase().endsWith(".jpg")
							&& tempName.contains(content)) {
						num = num + 1;
					}
				}

			}
		}

		return num;

	}

	/**
	 *
	 * @param type
	 *            1 为签名图片，2为样地位置图，3为引点位置图,4为样地图片,5为样木图片
	 * @param  "ydh
	 *            用来区别图片
	 */
	public static String getLxqcImagePath(String type) {
		String str = otms + slzylxqc;
		String dataPath = "文件可用地址";
		String[] memoryPath = getMemoryPath();
		for (int i = 0; i < memoryPath.length; i++) {
			File file = new File(memoryPath[i] + ROOT_MAPS + str);
			if (file.exists()) {
				if ("1".equals(type)) {
					dataPath = memoryPath[i] + ROOT_MAPS + str + qmtp;
				} else if ("2".equals(type)) {
					dataPath = memoryPath[i] + ROOT_MAPS + str + ydwzt;
				} else if ("3".equals(type)) {
					dataPath = memoryPath[i] + ROOT_MAPS + str + yindwzt;
				} else if ("4".equals(type)) {
					dataPath = memoryPath[i] + ROOT_MAPS + str + ydtp;
				} else if ("5".equals(type)) {
					dataPath = memoryPath[i] + ROOT_MAPS + str + ymtp;
				}
				break;
			}
		}
		return dataPath;
	}

	/**
	 * 给拍照图片命名
	 *
	 * @param ydh
	 *            样地号
	 * @param type
	 *            "1"为样木图片，"2"为样地图片
	 * @return
	 */
	public static String getLxqcImagename(String ydh, String type, String ymh) {
		String imagename = "";
		if ("1".equals(type)) {
			imagename = ydh + "_" + String.valueOf(System.currentTimeMillis())
					+ "_样木_" + ymh + ".jpg";
		} else if ("2".equals(type)) {
			imagename = ydh + "_" + String.valueOf(System.currentTimeMillis())
					+ "_样地.jpg";
		}

		return imagename;

	}

	/** 删除指定文件夹下指定图片 */
	public static void deleteImage(String path, String ydh) {
		File f = new File(path);
		if (f.exists()) {
			File[] fl = f.listFiles();
			for (int i = 0; i < fl.length; i++) {
				if (fl[i].toString().endsWith(".jpg")
						&& fl[i].toString().contains(ydh)) {
					fl[i].delete();
				}
			}
		}
	}

	/** 删除指定文件夹下指定名字图片 */
	public static void deleteImageForName(String path, String imagename) {
		File f = new File(path);
		if (f.exists()) {
			File[] fl = f.listFiles();
			for (int i = 0; i < fl.length; i++) {
				if (fl[i].getName().toString().trim().equals(imagename.trim())) {
					fl[i].delete();
				}
			}
		}
	}
}
