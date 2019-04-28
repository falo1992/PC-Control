package com.olaf.majer.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class responsible for exchanging files with client.
 * 
 * @author Olaf Majer
 *
 */
public class FileManagement {
	private final static Logger logger = Logger.getLogger(FileManagement.class);
	
	public final static String HOME      = System.getProperty("user.home");
	public final static String DESKTOP   = HOME + File.separator + "Desktop";
	public final static String DOWNLOADS = HOME + File.separator + "Downloads";
	public final static String DOCUMENTS = HOME + File.separator + "Documents";
	
	private static byte[] buffer = new byte[8192];
	private static String currentPath = new String();
	
	private FileManagement() {}
	
	/**
	 * Returns files in given path.
	 * 
	 * @param path Folder to list files for.
	 * @return Array of files and folder in given path.
	 */
	public static String[][] listFolder(String path){
		currentPath = path;
		File folder = new File(path);
		File[] folderList = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !pathname.isHidden();
			}
		});
		
		String[][] result = new String[folderList.length+1][3];
		
		result[0][0] = "0";
		result[0][1] = "..";
		result[0][2] = currentPath.substring(0, currentPath.substring(0, (currentPath.length() - 1)).lastIndexOf("\\")+1);
		
		for(int i = 0; i < folderList.length; i++){
			if(folderList[i].isDirectory()){
				result[i+1][0] = "0";
				result[i+1][1] = folderList[i].getName();
				result[i+1][2] = path + "\\" + result[i+1][1];
			}else{
				result[i+1][0] = "1";
				result[i+1][1] = folderList[i].getName();
				result[i+1][2] = path + "\\" + result[i+1][1];
			}
		}
		
		java.util.Arrays.sort(result, new java.util.Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				
				return o1[0].compareTo(o2[0]);
			}
		});
		
		return result;
	}
	
	/**
	 * Sends file to client.
	 * 
	 * @param filePath Path of file to send.
	 * @param oos Valid ObjectOutputStream.
	 * @param os Valid OutputStream.
	 */
	public static void uploadFile(String filePath, ObjectOutputStream oos, OutputStream os) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					long fileSize = new File(filePath).length();
					oos.writeObject(fileSize);
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
					BufferedOutputStream out = new BufferedOutputStream(os);
					int count = 0;
					while( (count = in.read(buffer)) > 0 ) {
						out.write(buffer, 0, count);
					}
					out.flush();
					in.close();
					System.out.println("done");
				}catch (IOException e){
					log("Upload failed");
					e.printStackTrace();
				}
				log("Upload completed");
			}
		}).run();
	}
	
	/**
	 * Download file from client.
	 * 
	 * @param filePath Path used to save file.
	 * @param is Valid InputStream.
	 */
	public static void downloadFile(String filePath, InputStream is) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					ObjectInputStream ois = new ObjectInputStream(is);
					Object fileSizeObj = ois.readObject();
					BufferedInputStream in = new BufferedInputStream(is);
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
					long fileSize = fileSizeObj instanceof Long ? (Long)fileSizeObj : null;
					int count = 0;
					
					while( (count = in.read(buffer)) > 0) {
						out.write(buffer, 0, count);
						fileSize -= count;
						if(fileSize == 0){
							break;
						}
						System.out.print("#");
					}
					out.flush();
					out.close();
				} catch (Exception e){
					log("Download failed");
					e.printStackTrace();
				}
				log("Download completed");
			}
		}).run();
	}
	
	/**
	 * Downloads folder from client.
	 */
	public static void downloadFolder() {
		// TODO: Zaimplementować
	}
	
	/**
	 * Sends folder to client.
	 */
	public static void uploadFolder() {
		// TODO: Zaimplementować
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
