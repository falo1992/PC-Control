package com.gdzie.znajde.server;

public class ServerApp {
	private static Server server = new Server();
	
	public static void main(String[] args){
		System.setProperty("javax.net.ssl.keyStore", "security\\serverKeyStore");
		System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
		server.run();
	}
}
