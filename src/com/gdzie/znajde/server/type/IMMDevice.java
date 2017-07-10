package com.gdzie.znajde.server.type;

import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.jnacom.IID;
import com.sun.jna.platform.win32.jnacom.IUnknown;
import com.sun.jna.platform.win32.jnacom.VTID;
import com.sun.jna.ptr.PointerByReference;

@IID("{D666063F-1587-4E43-81F1-B948E807363F}")
public interface IMMDevice extends IUnknown {
	@VTID(2)
	HRESULT Activate(GUID iid,int dwClsCtx,long pActivationParams,PointerByReference ppv);
	@VTID(3)
	HRESULT GetID(PointerByReference id);
}
