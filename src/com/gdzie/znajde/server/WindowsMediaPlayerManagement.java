package com.gdzie.znajde.server;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

import com.gdzie.znajde.server.gui.ServerFrame;
import com.gdzie.znajde.server.type.IUser32;
import com.gdzie.znajde.server.type.IWMPControls;
import com.gdzie.znajde.server.type.IWMPPlayer;
import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Ole32Util;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;

public class WindowsMediaPlayerManagement {
	private static HWND handle;
	private static IUser32 user32;
	private static String wmplayerExe = null;
	
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
	
	public static void init() {
		user32 = (IUser32) Native.loadLibrary("user32",IUser32.class); 
		handle = user32.FindWindowA("WMPlayerApp", "Windows Media Player");
		
		HRESULT hr = Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
		if (hr.equals(W32Errors.S_OK)) {
			System.out.println("Udalo siê");
		}

		IWMPPlayer wmp = new IWMPPlayer(null, false);

//		wmp.put_url("C:\\Users\\Public\\Music\\Sample Music\\Kalimba.mp3");
//		System.out.println(wmp.get_controls());
//		IWMPControls wmpcontrols = new IWMPControls(null, true);
//		wmpcontrols.play();
//		wmp.openPlayer("C:\\Users\\Public\\Music\\Sample Music\\Sleep Away.mp3");
//		wmp.close();
	}
	
	public static boolean playPause() {
		return user32.SendMessageA(handle, IUser32.WM_COMMAND, 0x00004978, 0x00000000);
	}
	
	public static void main(String[] args) {
//		if (!isRunning()) {
//			runWMPExe();			
//		}
		init();
//		Scanner scanner = new Scanner(System.in);
//		
//		while(true) {
//			String line = scanner.nextLine();
//			if(line.equals("1")) {
//				playPause();
//			}
//		}
	}
}
