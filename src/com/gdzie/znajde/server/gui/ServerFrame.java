package com.gdzie.znajde.server.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
