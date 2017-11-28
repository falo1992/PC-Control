package com.gdzie.znajde.server.type;

import com.sun.jna.Library;

public interface IUser32 extends Library {
	boolean ExitWindowsEx(long flags ,long reason);
}
