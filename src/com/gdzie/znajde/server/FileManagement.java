package com.gdzie.znajde.server;

import java.io.*;
import java.net.Socket;

public class FileManagement {
	private static byte[] buffer = new byte[8192];
	private static String previousPath = new String();
	private static String currentPath = new String();
	private static String user = System.getProperty("user.name");
	
	public static String listFolder(String path){
		currentPath = new String(path);
		File folder = new File(path);
		File[] folderList = folder.listFiles();
		StringBuilder result = new StringBuilder();
		for(File file: folderList){
			if(file.isDirectory()){
				result.append("Folder\t"+file.getName()+"\n");
			}else{
				result.append("Plik\t"+file.getName()+"\n");
			}
		}
		
		return result.toString();
	}
	
	public static String goToFolder(String folder){
		previousPath = currentPath;
		currentPath = currentPath+"\\"+folder;
		return listFolder(currentPath);
	}
	
	public static String goToPrevious(){
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
	
	public static boolean uploadFile(String filePath, Socket socket) {
		try{
			long fileSize = new File(filePath).length();
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
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
	
	public static void main(String[] args){
		
	}
}
