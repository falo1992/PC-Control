package com.gdzie.znajde.server.type;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Ole32Util;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant.VARIANT;
import com.sun.jna.platform.win32.COM.COMBindingBaseObject;

public class IWMPPlayer extends COMBindingBaseObject{

	public IWMPPlayer(CLSID clsid, boolean useActiveInstance) {
		super("WMPlayer.OCX.7", useActiveInstance);
		// TODO Auto-generated constructor stub
	}
	
	public boolean get_stretchToFit() {
		VARIANT.ByReference pvResult = new VARIANT.ByReference();
		this.oleMethod(OleAuto.DISPATCH_PROPERTYGET, pvResult, this.getIDispatch(), "stretchToFit");
		return pvResult.booleanValue();
	}
	
	public void put_stretchToFit(boolean value) {
		this.oleMethod(OleAuto.DISPATCH_PROPERTYPUT, null, this.getIDispatch(), "stretchToFit", new VARIANT(value));
	}
	
	public boolean get_enabled() {
		VARIANT.ByReference pvResult = new VARIANT.ByReference();
		this.oleMethod(OleAuto.DISPATCH_PROPERTYGET, pvResult, this.getIDispatch(), "enabled");
		return pvResult.booleanValue();
	}
	
	public void put_enabled(boolean value) {
		this.oleMethod(OleAuto.DISPATCH_PROPERTYPUT, null, this.getIDispatch(), "enabled", new VARIANT(value));
	}
	
//	public boolean get_fullScreen() {
//		VARIANT.ByReference pvResult = new VARIANT.ByReference();
//		this.oleMethod(OleAuto.DISPATCH_PROPERTYGET, pvResult, this.getIDispatch(), "fullScreen");
//		return pvResult.booleanValue();
//	}
//	
//	public void put_fullScreem(boolean value) {
//		this.oleMethod(OleAuto.DISPATCH_PROPERTYPUT, null, this.getIDispatch(), "fullScreen", new VARIANT(value));
//	}
	
	public String get_versionInfo() {
		VARIANT.ByReference pvResult = new VARIANT.ByReference();
		this.oleMethod(OleAuto.DISPATCH_PROPERTYGET, pvResult, this.getIDispatch(), "versionInfo");
		return pvResult.stringValue();
	}
	
	public void close() {
		this.oleMethod(OleAuto.DISPATCH_METHOD, null, this.getIDispatch(), "closePlayer", new VARIANT[]{});
	}
	
	public void put_url(String path) {
		this.oleMethod(OleAuto.DISPATCH_PROPERTYPUT, null, this.getIDispatch(), "url", new VARIANT[]{new VARIANT(path)});
	}
	
	public String get_url() {
		VARIANT.ByReference pvResult = new VARIANT.ByReference();
		this.oleMethod(OleAuto.DISPATCH_PROPERTYGET, pvResult, this.getIDispatch(), "url");
		
		return pvResult.stringValue();
	}
	
	public void openPlayer(String path) {
		this.oleMethod(OleAuto.DISPATCH_METHOD, null, this.getIDispatch(), "openPlayer", new VARIANT[]{new VARIANT(path)});
	}
	
	public Pointer get_controls() {
		VARIANT.ByReference pvResult = new VARIANT.ByReference();
		this.oleMethod(OleAuto.DISPATCH_PROPERTYGET, pvResult, this.getIDispatch(), "url");
		
		return pvResult.getPointer();
	}
	

}
