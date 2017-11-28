package com.gdzie.znajde.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class VolumeManagement {
	private static boolean mute = false;
	
	public static boolean setVolume(double value) {
		if(value > 1) value = 1;
		if(value < 0) value = 0;
		try{
			Process p = Runtime.getRuntime().exec("volumeControl.exe s " + value);
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static double getVolume() {
		try{
			Process p = Runtime.getRuntime().exec("volumeControl.exe g");
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Double.parseDouble(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static boolean setVolumeMute() {
		try{
			Process p = Runtime.getRuntime().exec("volumeControl.exe m");
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean setVolumeUnmute() {
		try{
			Process p = Runtime.getRuntime().exec("volumeControl.exe u");
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean setVolumeUp() {
		try{
			Double volume = getVolume() > 0.95 ? 0.95 : getVolume();
			Process p = Runtime.getRuntime().exec("volumeControl.exe s " + (volume + 0.05));
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean setVolumeDown() {
		try{
			Double volume = getVolume() < 0.05 ? 0.05 : getVolume();
			Process p = Runtime.getRuntime().exec("volumeControl.exe s " + (volume - 0.05));
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean getMute() {
		return mute;
	}
	public static void setMute() {
		if(mute) {
			mute = false;
		}else{
			mute = true;
		}
	}
	
	public static void extractProgram() {
		try {
			URL url = ClassLoader.getSystemResource("volumeControl.exe");
			InputStream is = url.openStream();
			FileOutputStream fos = new FileOutputStream("volumeControl.exe");
			byte[] buffer = new byte[4096];
			int bytesRead = is.read(buffer);
			while(bytesRead != -1) {
				fos.write(buffer, 0, bytesRead);
				bytesRead = is.read(buffer);
			}
			
			is.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeProgram() {
		File program = new File("volumeControl.exe");
		program.delete();
	}
}
