package com.gdzie.znajde.server.type;

import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.COM.COMBindingBaseObject;

public class IWMPPlayer extends COMBindingBaseObject{

	public IWMPPlayer(CLSID clsid, boolean useActiveInstance) {
		super("WMPlayer.OCX.7", useActiveInstance);
	}
	
	public void openPlayer(String path) {
		this.oleMethod(OleAuto.DISPATCH_METHOD, null, this.getIDispatch(), "openPlayer", new VARIANT[]{new VARIANT(path)});
	}
}
