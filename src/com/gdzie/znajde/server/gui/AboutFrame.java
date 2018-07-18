package com.gdzie.znajde.server.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class AboutFrame extends JFrame {
	
	private JLabel textLabel = new JLabel();
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Scanner scanner;

	public AboutFrame() {
		this(500,300);
	}
	
	public AboutFrame(int x, int y) {
		super("About PC-Control");
		scanner = new Scanner(ClassLoader.getSystemResourceAsStream("About.txt"));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				AboutFrame.this.setVisible(false);
			}
		});
		setPreferredSize(new Dimension(x, y));
		add(textLabel);
		pack();
		setLocationRelativeTo(null);
		readFile();
		setVisible(false);
		setLocation(screenSize.width/2 - this.getSize().width/2, screenSize.height/2 - this.getSize().height/2);
	}
	
	private void readFile() {
		while(scanner.hasNextLine()) {
			textLabel.setText(textLabel.getText() + scanner.nextLine());
		}
	}
}
