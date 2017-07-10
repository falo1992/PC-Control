package com.gdzie.znajde.server;

import org.jitsi.service.neomedia.BasicVolumeControl;
import org.jitsi.service.neomedia.VolumeControl;


public class Volume3 {

	public static boolean setVolume(){
		VolumeControl control = new BasicVolumeControl(VolumeControl.PLAYBACK_VOLUME_LEVEL_PROPERTY_NAME);
		control.setMute(true);
		return true;
	}
	
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		System.out.println(setVolume());

	}

}
