package com.gdzie.znajde.server;


import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.security.KeyStore;
import java.security.SecureRandom;

import com.gdzie.znajde.server.gui.ServerFrame;

import javax.net.ssl.*;

public class Server implements Runnable {
	Server(){
		serverPort = 2000;
		clientThread = new ClientThread[max];
	}
	
	private static AudioObservable aov;
	private static VideoObservable vov;
	private SSLServerSocketFactory serverSocketFactory;
	private SSLSocket socket;
	private SSLServerSocket serverSocket;
	private SSLServerSocket audioServerSocket;
	private SSLServerSocket videoServerSocket;
	private ClientThread clientThread[];
	private int serverPort;
	private static int max = 20;
	
	public void run(){
		EventQueue.invokeLater(new Runnable() {	
			public void run() {
				new ServerFrame();
			}
		});
		KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("JKS");
            ServerFrame.log("Initializing KeyStore");
            InputStream keystoreStream = ClassLoader.getSystemResourceAsStream("serverKeyStore");
            keyStore.load(keystoreStream, "changeit".toCharArray());
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keyStore, "changeit".toCharArray());
            KeyManager[] keymanagers =  kmfactory.getKeyManagers();

            TrustManagerFactory tmf=TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());

            tmf.init(keyStore);

            SSLContext sslContext=SSLContext.getInstance("TLS");

            sslContext.init(keymanagers, tmf.getTrustManagers(), new SecureRandom());
            ServerFrame.log("Setting SSL context");

            serverSocketFactory=sslContext.getServerSocketFactory();
			ServerFrame.log("Starting server");
            
			serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(serverPort);
			audioServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(3000);
			videoServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(4000);
			ServerFrame.log("Server listening on port " + serverPort);

        }catch(Exception e){
        	e.printStackTrace();
        }
        
        aov = new AudioObservable();
        vov = new VideoObservable();
        
        new Thread(aov).start();
        new Thread(vov).start();
        
		while(true){
			try{
				socket = (SSLSocket) serverSocket.accept();
				ServerFrame.log("Client connected from " + socket.getInetAddress());
				for(int i=0;i<max;i++){
					if(clientThread[i] == null){
						clientThread[i] = new ClientThread(socket, audioServerSocket, videoServerSocket ,i);
						clientThread[i].start();
						break;
					}
				}
			}catch(SocketException e){
				ServerFrame.log(e.getMessage());
			}catch(IOException e){
				ServerFrame.log(e.getMessage());
			}
		}
	}
	
	public static AudioObservable getAov() {
		return aov;
	}

	public static VideoObservable getVov() {
		return vov;
	}
}
