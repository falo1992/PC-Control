package com.gdzie.znajde.server.type;

import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.COM.COMBindingBaseObject;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.Variant.VARIANT;

public class IWMPControls extends COMBindingBaseObject{

	public IWMPControls(CLSID clsid, boolean useActiveInstance) {
		super(new CLSID(GUID.fromString("{74C09E02-F828-11D2-A74B-00A0C905F36E}")), useActiveInstance);
		// TODO Auto-generated constructor stub
	}
	
	public void play() {
		this.oleMethod(OleAuto.DISPATCH_METHOD, null, this.getIDispatch(), "play");
	}

}
