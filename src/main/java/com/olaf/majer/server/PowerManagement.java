package com.olaf.majer.server;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;
import com.olaf.majer.server.type.IAdvapi32;
import com.olaf.majer.server.type.IUser32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HANDLEByReference;
import com.sun.jna.platform.win32.WinNT.TOKEN_PRIVILEGES;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;

/**
 * Class responsible for power management.
 * 
 * @author Olaf Majer
 *
 */
public class PowerManagement {
	private final static Logger logger = Logger.getLogger(PowerManagement.class);
	
	private static final int LOGOUT_COMMAND         = 0x00000000;
	private static final int SHUTDOWN_COMMAND       = 0x00000001;
	private static final int FORCE_SHUTDOWN_COMMAND = 0x00000001 | 0x00000010;
	private static final int FORCE_RESTART_COMMAND  = 0x00000002 | 0x00000010;
	private static final int RESTART_COMMAND        = 0x00000002;
	private static final int PLANNED_REASON         = 0x80000000;
	
	private static IUser32 user32;
	private static boolean initialized = false;
	
	/**
	 * Initializes User32 NativeLibrary.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	private static boolean init() {
		HANDLEByReference hToken = new HANDLEByReference();
		TOKEN_PRIVILEGES tkp     = new TOKEN_PRIVILEGES(1);
		WinNT.LUID luid          = new WinNT.LUID();
		IAdvapi32 advapi32       = (IAdvapi32) Native.loadLibrary("advapi32",IAdvapi32.class);
		
		if (!advapi32.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(),WinNT.TOKEN_ADJUST_PRIVILEGES | WinNT.TOKEN_QUERY , hToken)) {
			return (false);
		}
		
		advapi32.LookupPrivilegeValueA(null, WinNT.SE_SHUTDOWN_NAME, luid);
		tkp.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(luid, new WinDef.DWORD(WinNT.SE_PRIVILEGE_ENABLED));
		if (!advapi32.AdjustTokenPrivileges(hToken.getValue(), false, tkp, 0, null, null)) {
			return false;
		}
		
		user32 = (IUser32) Native.loadLibrary("user32",IUser32.class);
		
		return initialized = true;
	}
	
	/**
	 * Issues soft restart.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean restart() {
		if(! initialized) {
			init();
		}
		
		return user32.ExitWindowsEx(RESTART_COMMAND, PLANNED_REASON);
	}
	
	/**
	 * Issues soft shutdown.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean shutdown() {
		if(! initialized) {
			init();
		}
		
		return user32.ExitWindowsEx(SHUTDOWN_COMMAND, PLANNED_REASON);
	}
	
	/**
	 * Forces shutdown.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean forceShutdown() {
		if(! initialized) {
			init();
		}
		
		return user32.ExitWindowsEx(FORCE_SHUTDOWN_COMMAND, PLANNED_REASON);
	}
	
	/**
	 * Forces restart.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean forceRestart() {
		if(! initialized) {
			init();
		}
		
		return user32.ExitWindowsEx(FORCE_RESTART_COMMAND, PLANNED_REASON);
	}
	
	/**
	 * Logouts current user.
	 * 
	 * @return TRUE if succeeded, FALSE otherwise.
	 */
	public static boolean logout() {
		if(! initialized) {
			init();
		}
		
		return user32.ExitWindowsEx(LOGOUT_COMMAND, PLANNED_REASON);
	}
	
	private static void log(String message) {
		logger.debug(message);
		ServerFrame.log(message);
	}
}
