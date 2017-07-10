package com.gdzie.znajde.server;

import com.gdzie.znajde.server.type.IMMDeviceCollection;
import com.gdzie.znajde.server.type.IMMDeviceEnumerator;
import com.sun.corba.se.impl.ior.ObjectAdapterIdNumber;
import com.sun.jna.Callback;
import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Ole32Util;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.jnacom.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import sun.font.CreatedFontTracker;


//FIXME Moze zmienic na wywolywanie klawiszy funkcyjnych
class Volume {

	public static final String CLSID_MMDeviceEnumerator = "{bcde0395-e52f-467c-8e3d-c4579291692e}";
	public static final String IID_IMMDeviceEnumerator = "{a95664d2-9614-4f35-a746-de8db63617e6}";
	public static final String IID_IAudioClient = "{1cb9ad4c-dbfa-4c32-b178-c2f568a703b2}";
	private static final int Collection_Methods = 5;
	private static final int ActivateMethod = 5;
	private static final int GetIdMethod = 6;
	private static final int GetStateMethod = 7;
	private static final int OpenPropertyStoreMethod = 8;
	private static final int eRender = 0;
	private static final int eCapture = 1;
	private static final int eAll = 2;
	private static final int eConsole = 0;
	private static final int eMultimedia = 1;
	private static final int eCommunications = 2;
	private static final int DEVICE_STATE_ACTIVE = 0x1;
	private static final int IMMDeviceCollection_GetCount = 6;
	private static final int IMMDeviceCollection_Item = 1;
	
	public interface GetDefaultCallback extends Callback {

	}

	public static boolean setVolume(int level) {
		Ole32 ole32 = (Ole32) Native.loadLibrary("ole32", Ole32.class);
		PointerByReference MMDeviceEnumerator = new PointerByReference();
		PointerByReference MMDevice = new PointerByReference();
		PointerByReference iMMDeviceCollection = new PointerByReference();
		PointerByReference MMAudioClient = new PointerByReference();
		PointerByReference MMDevice_ID = new PointerByReference();
		IntByReference count = new IntByReference();
		GUID GUID_MMDeviceEnumerator = Ole32Util.getGUIDFromString(CLSID_MMDeviceEnumerator);
		GUID GUID_IMMDeviceEnumerator = Ole32Util.getGUIDFromString(IID_IMMDeviceEnumerator);
		GUID GUID_IAudioClient = Ole32Util.getGUIDFromString(IID_IAudioClient);
		HRESULT hr;
		Pointer reserved = null;
		Pointer IMMDeviceCollectionPointer;
		Function Activate;
		Function GetId;
		Function GetState;
		Function OpenPropertyStore;
		Function GetCount;
		Function Item;
		
		hr = ole32.CoInitializeEx(reserved, Ole32.COINIT_MULTITHREADED);
		hr = ole32.CoCreateInstance(GUID_MMDeviceEnumerator, null, WTypes.CLSCTX_ALL, GUID_IMMDeviceEnumerator,
				MMDeviceEnumerator);
		COMUtils.checkRC(hr);

		if (!hr.equals(W32Errors.S_OK)) {
			System.out.println("Nie udalo sie");
			System.out.println((hr.getClass()));
			return false;
		}
		IUnknown iu = ComObject.wrapNativeInterface(MMDeviceEnumerator.getValue(), IUnknown.class);
		IMMDeviceEnumerator au = iu.queryInterface(IMMDeviceEnumerator.class);
		//hr = au.GetDefaultAudioEndpoint(2, 1, MMDevice);
		//hr = au.EnumAudioEndpoints(eRender,DEVICE_STATE_ACTIVE, iMMDeviceCollection);
		hr = au.EnumAudioEndpoints(eRender, DEVICE_STATE_ACTIVE, iMMDeviceCollection);
		if (!hr.equals(W32Errors.S_OK)) {
			System.out.println("Nie udalo sie");
			System.out.println((hr.getClass()));
			return false;
		}
		System.out.println(iMMDeviceCollection.getValue());
        IUnknown iu2 = ComObject.wrapNativeInterface(iMMDeviceCollection.getValue(), IUnknown.class);
        IMMDeviceCollection devcoll = iu2.queryInterface(IMMDeviceCollection.class);
        //hr = devcoll.GetCount(count);
		//IMMDeviceCollectionPointer = iMMDeviceCollection.getValue();
		//Pointer vTablePointer = IMMDeviceCollectionPointer.getPointer(0);
		//Pointer[] vTable = new Pointer[Collection_Methods];
		//vTablePointer.read(0,vTable,0,vTable.length);
		//for(int i=0;i<21;i++)
		//System.out.println(vTable[i]);
		//GetCount = Function.getFunction(vTable[4]);
		//GetCount.invokeInt(new Object[]{IMMDeviceCollectionPointer,count});
		//GetCount.invokeInt(new Object[]{});
		//Item = Function.getFunction(vTable[2]);
		//int j = Item.invokeInt(new Object[]{IMMDeviceCollectionPointer,0,MMDevice});
		//System.out.println(count.getValue());
		/*Activate = Function.getFunction(vTable[ActivateMethod],Function.ALT_CONVENTION);
		GetId = Function.getFunction(vTable[GetIdMethod],Function.ALT_CONVENTION);
		GetState = Function.getFunction(vTable[GetStateMethod],Function.ALT_CONVENTION);
		*///hr = au2.Activate(GUID_IAudioClient, WTypes.CLSCTX_ALL, 0, MMAudioClient);
		
		return true;
	}

	public static void main(String[] args) {
		System.out.println(setVolume(0));
	}
}
