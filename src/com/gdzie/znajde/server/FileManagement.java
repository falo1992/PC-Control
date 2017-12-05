package com.gdzie.znajde.server;

import java.io.*;
import java.net.Socket;

public class FileManagement {
	public final static String HOME      = System.getProperty("user.home");
	public final static String DESKTOP   = HOME + "\\Desktop";
	public final static String DOWNLOADS = HOME + "\\Downloads";
	
	private static byte[] buffer = new byte[8192];
	private static String previousPath = new String();
	private static String currentPath = new String();
	
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
	
	public static String[][] goToFolder(String folder){
		previousPath = currentPath;
		currentPath = currentPath+"\\"+folder;
		return listFolder(currentPath);
	}
	
	public static String[][] goToPrevious(){
		return listFolder(previousPath);
	}
	
	public static boolean uploadServer(String filePath, Socket socket){
		return false;
	}
	
	public static boolean downloadServer(String filePath, Socket socket){
		return false;
	}
	
	public static boolean uploadClient(String filePath, Socket socket){
		return false;
	}
	
	public static boolean downloadClient(String filePath, Socket socket){
		return false;
	}
	
	public static boolean uploadFile(String filePath, ObjectOutputStream oos, Socket socket) {
		try{
			long fileSize = new File(filePath).length();
			oos.writeObject(fileSize);
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			int count = 0;
			while( (count = in.read(buffer)) > 0 ) {
				out.write(buffer, 0, count);
			}
			out.flush();
			in.close();
			System.out.println("done");
			return true;
		}catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean downloadFile(String filePath, Socket socket){
		try{
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			Object fileSizeObj = ois.readObject();
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
			long fileSize = fileSizeObj instanceof Long ? (Long)fileSizeObj : null;
			int count = 0;
			while( (count = in.read(buffer)) > 0){
				out.write(buffer, 0, count);
				fileSize -= count;
				if(fileSize == 0){
					break;
				}
				System.out.print("#");
			}
			out.flush();
			out.close();
			System.out.println("done");
			return true;
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean downloadFolder(){
		return false;
	}
	
	public static boolean uploadFolder(){
		return false;
	}
}
