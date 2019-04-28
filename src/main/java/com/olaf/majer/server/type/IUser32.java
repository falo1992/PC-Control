package com.olaf.majer.server.type;

import com.sun.jna.Library;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * JNA wrapper for IUser32 Library.
 * 
 * @author Olaf Majer
 *
 */
public interface IUser32 extends Library {
	int WM_COMMAND = 0x111;
	
	boolean ExitWindowsEx(long flags ,long reason);
	HWND FindWindowA (String strClassName, String strWindowName);
	HWND FindWindowExA (HWND hwndParent, HWND hwndChildAfter, String strClassName, String strWindowName);
	boolean SendMessageA (HWND hWnd, int Msg, int wParam, int lParam);
}
