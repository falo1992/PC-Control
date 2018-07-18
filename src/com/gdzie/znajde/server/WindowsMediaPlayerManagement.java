package com.gdzie.znajde.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.gdzie.znajde.server.gui.ServerFrame;
import com.gdzie.znajde.server.type.IUser32;
import com.gdzie.znajde.server.type.IWMPPlayer;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class WindowsMediaPlayerManagement {
	public static HWND handle;
	private static IUser32 user32;
	private static String wmplayerExe = null;
	static IWMPPlayer wmp;
	
	static class Finder extends SimpleFileVisitor<Path> {
		private final PathMatcher matcher;
		
		Finder(String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }
		
		boolean find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                return true;
            }
            return false;
        }
		
		@Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if(find(file)) {
            	WindowsMediaPlayerManagement.wmplayerExe = file.toString();
            	return FileVisitResult.TERMINATE;
            }
            return FileVisitResult.CONTINUE;
        }
		
		@Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            find(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println(exc);
            return FileVisitResult.CONTINUE;
        }
	}
	
	public static boolean isRunning() {
		try {
			String line;
			Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
			BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((line = input.readLine()) != null) {
				if(line.contains("wmplayer.exe")) {
					return true;
				}
			}
			
			input.close();
		} catch (IOException e) {
			ServerFrame.log("Failed to start task manager.");
		}

		
		return false;
	}
	
	public static boolean runWMPExe() {
		try{
			Finder finder = new Finder("wmplayer.exe");
			Files.walkFileTree(Paths.get("C:/"), finder);
			Runtime.getRuntime().exec(wmplayerExe);
		} catch(IOException e) {
			ServerFrame.log(e.getMessage());
		}
		return isRunning();
	}
	
	public static void CoInitialize() {
		user32 = (IUser32) Native.loadLibrary("user32",IUser32.class); 
		
		Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
	}
	
	public static void findWindow() {
		CoInitialize();
		
		handle = user32.FindWindowA("WMPlayerApp", "Windows Media Player");
	}
	
	public static void init(String filePath) {
		CoInitialize();
		
		wmp = new IWMPPlayer(null, false);
		wmp.openPlayer(filePath);
		
		while(handle == null) {
			findWindow();
		}
	}
	
	public static void sendMessage(int command) {
		if (handle == null) {
			findWindow();
		}
		user32.SendMessageA(handle, IUser32.WM_COMMAND, command, 0x00000000);
	}
	
	public static void playPause() {
		sendMessage(0x00004978);
	}
	
	public static void volumeUp() {
		sendMessage(0x0001497F);
	}
	
	public static void volumeDown() {
		sendMessage(0x00014980);
	}
	
	public static void mute() {
		sendMessage(0x00014981);
	}
	
	public static void close() {
		sendMessage(0x0000E102);
	}
	
	public static void next() {
		sendMessage(0x0000497B);
	}
	
	public static void previous() {
		sendMessage(0x0000497A);
	}
	
	public static void shuffle() {
		sendMessage(0x0001499A);
	}
	
	public static void repeat() {
		sendMessage(0x0001499B);
	}
	
	public static void stop() {
		sendMessage(0x00014979);
	}
	
	public static void fullscreen() {
		sendMessage(0x00004968);
		sendMessage(0x0000495E);
	}
	
	public static void libraryView() {
		sendMessage(0x0001495C);
	}
	
	public static void playerView() {
		sendMessage(0x00004968);
	}
}
