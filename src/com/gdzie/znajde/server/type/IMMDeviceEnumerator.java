package com.gdzie.znajde.server.type;

import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.jnacom.IID;
import com.sun.jna.platform.win32.jnacom.IUnknown;
import com.sun.jna.platform.win32.jnacom.VTID;
import com.sun.jna.ptr.PointerByReference;

@IID("{a95664d2-9614-4f35-a746-de8db63617e6}")
public interface IMMDeviceEnumerator extends IUnknown {
	@VTID(1)
	HRESULT GetDefaultAudioEndpoint(int dataflow, int role, PointerByReference ppv);
	@VTID(4)
	HRESULT EnumAudioEndpoints(int dataFlow, int mask, PointerByReference ppv);
}
