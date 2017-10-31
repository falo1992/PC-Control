package com.gdzie.znajde.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class VolumeManagement {
	
	public static boolean setVolume(double value) {
		if(value > 1) value = 1;
		if(value < 0) value = 0;
		try{
			Process p = Runtime.getRuntime().exec("programs/volumeControl.exe s " + value);
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
			Process p = Runtime.getRuntime().exec("programs/volumeControl.exe g");
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
			Process p = Runtime.getRuntime().exec("programs/volumeControl.exe m");
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
			Process p = Runtime.getRuntime().exec("programs/volumeControl.exe u");
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
			Process p = Runtime.getRuntime().exec("programs/volumeControl.exe s " + (volume + 0.05));
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
			Process p = Runtime.getRuntime().exec("programs/volumeControl.exe s " + (volume - 0.05));
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
