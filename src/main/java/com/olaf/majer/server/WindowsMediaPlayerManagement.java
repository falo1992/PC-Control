package com.olaf.majer.server;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;
import com.olaf.majer.server.type.IUser32;
import com.olaf.majer.server.type.IWMPPlayer;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Class responsible for communication with Windows Media Player.
 * 
 * @author Olaf Majer
 *
 */
public class WindowsMediaPlayerManagement {
	private final static Logger logger = Logger.getLogger(WindowsMediaPlayerManagement.class);
	
	private static HWND       handle;
	private static IUser32    user32;
	private static IWMPPlayer wmp;
	
	private static final int LIBRARY_VIEW_COMMAND = 0x0001495C;
	private static final int PLAYER_VIEW_COMMAND  = 0x00004968;
	private static final int FULLSCREEN_COMMAND   = 0x0000495E;
	private static final int STOP_COMMAND         = 0x00014979;
	private static final int REPEAT_COMMAND       = 0x0001499B;
	private static final int SHUFFLE_COMMAND      = 0x0001499A;
	private static final int PREVIOUS_COMMAND     = 0x0000497A;
	private static final int MUTE_COMMAND         = 0x00014981;
//	private static final int CLOSE_COMMAND        = 0x0000E102;
	private static final int CLOSE_COMMAND        = 0x0010;
	private static final int NEXT_COMMAND         = 0x0000497B;
	private static final int VOLUME_DOWN_COMMAND  = 0x00014980;
	private static final int VOLUME_UP_COMMAND    = 0x0001497F;
	private static final int PLAY_PAUSE_COMMAND   = 0x00004978;
	
	/**
	 * Initializes Ole32 Library. 
	 */
	private static void CoInitialize() {
		user32 = (IUser32) Native.loadLibrary("user32",IUser32.class); 
		
		Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
	}
	
	/**
	 * Finds active Windows Media Player window.
	 */
	private static void findWindow() {
		CoInitialize();
		
		handle = user32.FindWindowA("WMPlayerApp", "Windows Media Player");
	}
	
	/**
	 * Opens given MP3 file in Windows Media Player.
	 * 
	 * @param filePath Path to MP3 file.
	 */
	public static void openWMP(String filePath) {
		CoInitialize();
		
		wmp = new IWMPPlayer(null, false);
		wmp.openPlayer(filePath);
		
		while(handle == null) {
			findWindow();
		}
	}
	
	/**
	 * Sends command to Window Media Player window.
	 * 
	 * @param command Command to be executed by Windows Media Player.
	 */
	public static void sendMessage(int command) {
		findWindow();
		
		user32.SendMessageA(handle, IUser32.WM_COMMAND, command, 0x00000000);
	}
	
	/**
	 * Toggles Play/Pause.
	 */
	public static void playPause() {
		sendMessage(PLAY_PAUSE_COMMAND);
	}
	
	/**
	 * Increases volume.
	 */
	public static void volumeUp() {
		sendMessage(VOLUME_UP_COMMAND);
	}
	
	/**
	 * Decreases volume.
	 */
	public static void volumeDown() {
		sendMessage(VOLUME_DOWN_COMMAND);
	}
	
	/**
	 * Toggles mute.
	 */
	public static void mute() {
		sendMessage(MUTE_COMMAND);
	}
	
	/**
	 * Closes Windows Media Player.
	 */
	public static void close() {
		findWindow();
		
		user32.SendMessageA(handle, CLOSE_COMMAND, 0x00000000, 0x00000000);
	}
	
	/**
	 * Changes song on next.
	 */
	public static void next() {
		sendMessage(NEXT_COMMAND);
	}
	
	/**
	 * Changes song on previous.
	 */
	public static void previous() {
		sendMessage(PREVIOUS_COMMAND);
	}
	
	/**
	 * Toggles shuffle option.
	 */
	public static void shuffle() {
		sendMessage(SHUFFLE_COMMAND);
	}
	
	/**
	 * Toggles repeat option.
	 */
	public static void repeat() {
		sendMessage(REPEAT_COMMAND);
	}
	
	/**
	 * Stops playing.
	 */
	public static void stop() {
		sendMessage(STOP_COMMAND);
	}
	
	/**
	 * Toggles fullscreen mode.
	 */
	public static void fullscreen() {
		sendMessage(FULLSCREEN_COMMAND);
	}
	
	/**
	 * Opens library view.
	 */
	public static void libraryView() {
		sendMessage(LIBRARY_VIEW_COMMAND);
	}
	
	/**
	 * Opens player view.
	 */
	public static void playerView() {
		sendMessage(PLAYER_VIEW_COMMAND);
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
