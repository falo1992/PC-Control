package com.gdzie.znajde.server.type;

import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.jnacom.IID;
import com.sun.jna.platform.win32.jnacom.IUnknown;
import com.sun.jna.platform.win32.jnacom.VTID;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

@IID("{0BD7A1BE-7A1A-44DB-8397-CC5392387B5E}")
public interface IMMDeviceCollection extends IUnknown {
	@VTID(5)
	HRESULT GetCount(IntByReference ibr);
	@VTID(6)
	HRESULT Item(int i, PointerByReference ppv);
}
