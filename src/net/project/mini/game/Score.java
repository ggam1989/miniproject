package net.project.mini.game;

import java.awt.Color;

import javax.swing.JFrame;

public class Score extends JFrame {

	JFrame f;
	
	public Score() {
		
		f = new JFrame("Score");

		f.setBounds(200, 200, 500, 500);
		f.setBackground(new Color(255,0,0));
		f.setLayout(null);
		
		
		f.setVisible(true);
	
	}
}
