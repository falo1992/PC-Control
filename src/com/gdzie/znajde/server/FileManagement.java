package com.gdzie.znajde.server;

import java.io.*;
import java.net.Socket;

public class FileManagement {
	
	private static Socket socket = null;
	FileManagement(Socket socket){
		FileManagement.socket = socket;
	}
	
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
	
	public static boolean downloadFile(String filePath) {
		try{
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			int count = 0;
			while( (count = in.read(buffer)) > 0 ) {
				out.write(buffer, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			return true;
		}catch (IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean uploadFile(String filePath){
		try{
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
			int count = 0;
			while( (count = in.read(buffer)) > 0){
				out.write(buffer, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			return true;
		} catch (IOException e){
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
	
	public static void main(String [] args){
		System.out.print(listFolder("."));
		System.out.print(goToFolder(".settings"));
		System.out.print(goToPrevious());
		System.out.println(user);
	}
}
