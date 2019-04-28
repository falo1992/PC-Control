package com.olaf.majer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

/**
 * Class responsible for volume management.
 * 
 * @author Olaf Majer
 *
 */
public class VolumeManagement {
	private final static Logger logger = Logger.getLogger(VolumeManagement.class);
	
	/**
	 * Sets master volume.
	 * 
	 * @param value Value to set. Must be between 0 and 1.
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean setVolume(double value) {
		if (value > 1) value = 1;
		if (value < 0) value = 0;
		
		return executeCommand("s " + value);
	}
	
	/**
	 * Returns current master volume.
	 * 
	 * @return Current master volume.
	 */
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
	
	/**
	 * Mutes master volume.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean setVolumeMute() {
		if (executeCommand("m")) {
			Server.isMute = true;
			return true;
			
		} else {
			return false;
		}
	}
	
	/**
	 * Unmutes master volume.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean setVolumeUnmute() {
		if (executeCommand("u")) {
			Server.isMute = false;
			return true;
			
		} else {
			return false;
		}
	}
	
	/**
	 * Increases volume by 5%.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean setVolumeUp() {
		Double volume = getVolume() > 0.95 ? 0.95 : getVolume();
		
		return executeCommand("s " + (volume + 0.05));
	}
	
	/**
	 * Decreases volume by 5%.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean setVolumeDown() {
		Double volume = getVolume() < 0.05 ? 0.05 : getVolume();
		
		return executeCommand("s " + (volume - 0.05));
	}
	
	/**
	 * Returns TRUE if master volume is muted, FALSE otherwise.
	 * 
	 * @return TRUE if master volume is muted, FALSE otherwise.
	 */
	public static boolean getMute() {
		return Server.isMute;
	}
	

	/**
	 * Executes command in volumeControl program.
	 * 
	 * @param command Command to be executed.
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	private static boolean executeCommand(String command) {
		try{
			Process p = Runtime.getRuntime().exec("volumeControl.exe " + command);
			InputStream is = p.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			return Boolean.parseBoolean(bufferedReader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
