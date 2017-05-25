package com.gdzie.znajde.server;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface IUser32 extends Library {
	boolean ExitWindowsEx(long flags ,long reason);
}
