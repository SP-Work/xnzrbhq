package com.otitan.xnbhq.util;


import com.file.zip.ZipEntry;
import com.file.zip.ZipFile;
import com.file.zip.ZipOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 压缩文件操作工具类
 */
public class ZIPUtil {

	public ZIPUtil() {

	}

	/**
	 * file      待解压文件
	 * dir       解压后文件的存放目录
	 */
	public static void unzip(String filepath, String dir) throws IOException {
		ZipFile zipFile = new ZipFile(new File(filepath), "GBK");//设置压缩文件的编码方式为GBK
		Enumeration<ZipEntry> entris = zipFile.getEntries();
		ZipEntry zipEntry = null;
		File tmpFile = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		byte[] buf = new byte[1024];
		int len = 0;
		while (entris.hasMoreElements()) {
			zipEntry = entris.nextElement();
			// 不进行文件夹的处理,些为特殊处理
			tmpFile = new File(dir + zipEntry.getName());
			if (zipEntry.isDirectory()) {//当前文件为目录
				if (tmpFile.exists()) {
					continue;
				}
				tmpFile.mkdir();
			} else {
				if(tmpFile.exists()){
					continue;
				}
				tmpFile.createNewFile();
				is = zipFile.getInputStream(zipEntry);
				bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
				while ((len = is.read(buf)) > 0) {
					bos.write(buf, 0, len);
				}
				bos.flush();
				bos.close();
			}
		}
	}

	/**
	 * DeCompress the ZIP to the path
	 * @param zipFileString name of ZIP
	 * @param outPathString path to be unZIP
	 */
	public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = (ZipEntry) inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				System.out.println(szName);
				File folder = new File(outPathString + File.separator + szName);
				if(folder.exists()){
					continue;
				}
				folder.mkdirs();
			} else {

				File file = new File(outPathString + File.separator + szName);
				System.out.println(szName);
				if(file.exists()){
					continue;
				}
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}

	/**
	 * Compress file and folder
	 *
	 * @param srcFileString file or folder to be Compress
	 * @param zipFileString the path name of result ZIP
	 */
	public static void ZipFolder(String srcFileString, String zipFileString)
			throws Exception {
		// create ZIP
		ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(
				zipFileString));
		// create the file
		File file = new File(srcFileString);
		// compress
		ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
		// finish and close
		outZip.finish();
		outZip.close();
	}

	/**
	 * compress files
	 *
	 * @param folderString 文件夹名
	 * @param fileString 文件名
	 * @param zipOutputSteam 文件流
	 * @throws Exception
	 */
	private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
		if (zipOutputSteam == null)
			return;
		File file = new File(folderString + fileString);
		if (file.isFile()) {
			ZipEntry zipEntry = new ZipEntry(fileString);
			FileInputStream inputStream = new FileInputStream(file);
			zipOutputSteam.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[4096];
			while ((len = inputStream.read(buffer)) != -1) {
				zipOutputSteam.write(buffer, 0, len);
			}
			zipOutputSteam.closeEntry();
		} else {
			// folder
			String fileList[] = file.list();
			// no child file and compress
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
				zipOutputSteam.putNextEntry(zipEntry);
				zipOutputSteam.closeEntry();
			}
			// child files and recursion
			for (int i = 0; i < fileList.length; i++) {
				ZipFiles(folderString, fileString + File.separator
						+ fileList[i], zipOutputSteam);
			}// end of for
		}
	}

	/**
	 * return the InputStream of file in the ZIP
	 *
	 * @param zipFileString name of ZIP
	 * @param fileString  name of file in the ZIP
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream UpZip(String zipFileString, String fileString)
			throws Exception {
		ZipFile zipFile = new ZipFile(zipFileString);
		ZipEntry zipEntry = zipFile.getEntry(fileString);
		return zipFile.getInputStream(zipEntry);
	}

	/**
	 * return files list(file and folder) in the ZIP
	 *
	 * @param zipFileString ZIP name
	 * @param bContainFolder contain folder or not
	 * @param bContainFile contain file or not
	 * @throws Exception
	 */
	public static List<File> GetFileList(String zipFileString,
										 boolean bContainFolder, boolean bContainFile) throws Exception {
		List<File> fileList = new ArrayList<File>();
		ZipInputStream inZip = new ZipInputStream(new FileInputStream(
				zipFileString));
		ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = (ZipEntry) inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(szName);
				if (bContainFolder) {
					fileList.add(folder);
				}

			} else {
				File file = new File(szName);
				if (bContainFile) {
					fileList.add(file);
				}
			}
		}
		inZip.close();
		return fileList;
	}
}
