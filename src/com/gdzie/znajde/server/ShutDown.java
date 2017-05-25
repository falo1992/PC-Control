package com.gdzie.znajde.server;

import com.sun.jna.Native;

public class ShutDown {
	public static boolean shutDown(){
		IUser32 user32 = (IUser32) Native.loadLibrary("user32",IUser32.class);
		boolean restart = user32.ExitWindowsEx(0x00000008, 0x80000000);
		return restart;
	}
}
