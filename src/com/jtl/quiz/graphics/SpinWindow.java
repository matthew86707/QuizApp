package com.jtl.quiz.graphics;

import javax.swing.JFrame;

public class SpinWindow extends JFrame {
	private static final long serialVersionUID = 1348725683029402987L;
	public static JFrame current;

	public SpinWindow(int seed) {
		super("Spining...");
		setSize(310, 320);
		setVisible(true);
		current = this;
	}

	public boolean spin() {
		SpinPanel sp = new SpinPanel();
		add(sp);
		return sp.init();
	}
}
