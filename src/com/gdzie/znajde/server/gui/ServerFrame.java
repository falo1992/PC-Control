package com.gdzie.znajde.server.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.gdzie.znajde.server.VolumeManagement;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame {
	static private JTextArea textArea = new JTextArea(14,40);
	static private JScrollPane scrollPane = new JScrollPane();
	static private JPanel panel = new JPanel();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public ServerFrame() {
		this(500,300);
	}
	
	public ServerFrame(int x, int y) {
		super("PC-Control");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				VolumeManagement.removeProgram();
				System.exit(0);
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
		
	}
	
	public static void log(String msg) {
		textArea.append(msg+"\n");
	}
}
