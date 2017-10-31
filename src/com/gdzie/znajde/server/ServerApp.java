package com.gdzie.znajde.server;

public class ServerApp {
	private static Server server = new Server();
	
	public static void main(String[] args){
		System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\ZWAC0_000\\eclipse\\jee-neon\\eclipse\\jre\\lib\\security\\server.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
		server.run();
	}
}
