package com.jtl.quiz.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SpinPanel extends JPanel {
	private static final long serialVersionUID = 8775719387211214866L;
	static SpinPanel current;
	AffineTransform identity = new AffineTransform();
	float speed = 100.0f;
	Timer timer = new Timer();

	public static BufferedImage spinner;
	public static BufferedImage arrow;

	public static float MIN_SPIN_SPEED = 0.5f;

	public static boolean isFinished = false;
	public static boolean spinValue = false;

	public static Object SPIN_LOCK = new Object();

	public SpinPanel() {
		super();
		current = this;
	}

	public boolean init() {
		SpinPanel.isFinished = false;
		SpinPanel.spinValue = false;
		speed = new Random().nextInt(550) + 80f;
		SpinPanel.spinValue = calculateValue(speed);
		try {
			spinner = ImageIO.read(new File("res/spinner.png"));
			arrow = ImageIO.read(new File("res/arrow.png"));
			identity.scale(9, 9);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				SpinWindow.current.repaint();
				if (speed < 0.25f) {
					SpinPanel.isFinished = true;
					SpinWindow.current.dispose();
					this.cancel();
				}
			}
		}, 2000, 50);

		return true;

	}

	public boolean calculateValue(float speed) {
		float total = 0f;
		while (speed >= MIN_SPIN_SPEED) {
			speed = speed / 1.03f;
			total += speed;
		}
		float div = (total / 360f);
		if (div - Math.floor(div) > 0.5f) {
			return true;
		}
		return false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		speed = speed / 1.03f;
		identity.rotate(Math.toRadians(speed), spinner.getWidth() / 2, spinner.getHeight() / 2);
		g2d.drawImage(spinner, identity, this);
		g2d.drawImage(arrow, 139, 0, 32, 32, this);
	}

}
