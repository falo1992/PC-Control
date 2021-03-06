package com.olaf.majer.server.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.Logger;

import com.olaf.majer.server.VolumeManagement;

/**
 * GUI class used to display logs.
 * 
 * @author Olaf Majer
 *
 */
@SuppressWarnings("serial")
public class ServerFrame extends JFrame {
	private static JTextArea textArea = new JTextArea(14,40);
	private static JScrollPane scrollPane = new JScrollPane();
	private static JPanel panel = new JPanel();
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public ServerFrame() {
		this(500,300);
	}
	
	public ServerFrame(int x, int y) {
		super("PC-Control");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				ServerFrame.this.setVisible(false);
			}
		});
		setPreferredSize(new Dimension(x, y));
		panel.setBorder( new TitledBorder( new EtchedBorder(), "Log"));
		scrollPane.getViewport().setView(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);
		add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setLocation(screenSize.width/2 - this.getSize().width/2, screenSize.height/2 - this.getSize().height/2);
		initTray();
	}
	
	public void initTray() {
		Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("remote.png"));
		TrayIcon trayIcon = new TrayIcon(image);
		SystemTray tray = SystemTray.getSystemTray();
		PopupMenu popup = new PopupMenu();
		
        MenuItem showWindow = new MenuItem("Show");
        MenuItem exitItem = new MenuItem("Exit");
		
        popup.addSeparator();
        popup.add(showWindow);
        popup.addSeparator();
        popup.add(exitItem);
        
        exitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
        showWindow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerFrame.this.setVisible(true);
			}
		});
        
        trayIcon.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount() == 2) {
					ServerFrame.this.setVisible(true);
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
        
        trayIcon.setPopupMenu(popup);
        
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String msg) {
		textArea.append(msg+"\n");
	}
}
