package com.gdzie.znajde.server;

import java.net.Socket;

import com.gdzie.znajde.server.type.IAdvapi32;
import com.gdzie.znajde.server.type.IUser32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HANDLEByReference;
import com.sun.jna.platform.win32.WinNT.TOKEN_PRIVILEGES;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef;

public class PowerManagement {
	
	private IUser32 user32;
	private Socket socket;
	
	public PowerManagement(Socket socket) {
		this.socket = socket;
	}
	
	private boolean init() {
		HANDLEByReference hToken = new HANDLEByReference();
		TOKEN_PRIVILEGES tkp = new TOKEN_PRIVILEGES(1);
		WinNT.LUID luid = new WinNT.LUID();
		IAdvapi32 advapi32 = (IAdvapi32) Native.loadLibrary("advapi32",IAdvapi32.class);
		
		if(!advapi32.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(),WinNT.TOKEN_ADJUST_PRIVILEGES | WinNT.TOKEN_QUERY , hToken))
			return (false);
		advapi32.LookupPrivilegeValueA(null, WinNT.SE_SHUTDOWN_NAME, luid);
		tkp.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(luid, new WinDef.DWORD(WinNT.SE_PRIVILEGE_ENABLED));
		if(!advapi32.AdjustTokenPrivileges(hToken.getValue(), false, tkp, 0, null, null))
			return false;
		user32 = (IUser32) Native.loadLibrary("user32",IUser32.class);
		return true;
	}
	
	public boolean restart(){
		if(init()) {
			boolean restart = user32.ExitWindowsEx(0x00000002, 0x80000000);
			return restart;
		} else {
			return false;
		}
		
	}
	
	public boolean shutDown(){
		if(init()) {
			boolean shutDown = user32.ExitWindowsEx(0x00000001, 0x80000000);
			return shutDown;
		} else {
			return false;
		}
	}
}
