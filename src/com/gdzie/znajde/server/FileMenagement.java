package com.gdzie.znajde.server;

import java.io.*;

public class FileMenagement {
	
	private static String previousPath;
	private static String currentPath;
	
	public static String listFolder(String path){
		currentPath = new String(path);
		File folder = new File(path);
		File[] folderList = folder.listFiles();
		StringBuilder result = new StringBuilder();
		for(File file: folderList){
			if(file.isDirectory()){
				result.append("Folder "+file.getName()+"\n");
			}else{
				result.append("Plik "+file.getName()+"\n");
			}
		}
		
		return result.toString();
	}
	
	public static String goToFolder(String folder){
		previousPath = new String(currentPath);
		currentPath = new String(currentPath+"\\"+folder);
		
		return listFolder(currentPath);
	}
	
	public static String goToPrevious(){
		return listFolder(previousPath);
	}
	
	public static boolean downloadFile(){
		return false;
	}
	
	public static boolean uploadFile(){
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
	}
}
