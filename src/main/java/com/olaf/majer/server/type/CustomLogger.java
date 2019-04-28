package com.olaf.majer.server.type;

import org.apache.log4j.Logger;

import com.olaf.majer.server.gui.ServerFrame;

public class CustomLogger extends Logger {

	protected CustomLogger(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void debug(Object message) {
		ServerFrame.log((String) message);
		
		super.debug(message);
	}
}
