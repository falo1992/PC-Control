package com.gdzie.znajde.server;

import com.sun.jna.Library;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.HANDLEByReference;
import com.sun.jna.platform.win32.WinNT.LUID;
import com.sun.jna.platform.win32.WinNT.TOKEN_PRIVILEGES;
import com.sun.jna.ptr.IntByReference;

public interface IAdvapi32 extends Library {
	boolean OpenProcessToken(HANDLE ProcessHandle, int DesiredAccess,HANDLEByReference TokenHandle);
	boolean LookupPrivilegeValueA(String lpSystemName, String lpName, LUID lpLuid);
	boolean AdjustTokenPrivileges(HANDLE TokenHandle, boolean DiableAllPrivileges, TOKEN_PRIVILEGES NewState, int BufferLength, TOKEN_PRIVILEGES PreviousState, IntByReference ReturnLength );
}
