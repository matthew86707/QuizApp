package com.jtl.quiz.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsManager {
	
	//Window stuff
	private static final String WINDOW_TITLE = "JTL Quiz App";
	private static final int WINDOW_SIZE_X = 800;
	private static final int WINDOW_SIZE_Y = 600;
	private static JFrame window;
	
	//Graphics Panel stuff
	private static GraphicsPanel panel;
	
	//Text
	private static String status = "Awaiting First Question";
	private static String currentPlayer = "Undefined";
	private static String currentOptions = "";
	
	//Confirm button
	public static final int CONFIRM_X = 600;
	public static final int CONFIRM_Y = 500;
	public static final int CONFIRM_SIZE_X = 100;
	public static final int CONFIRM_SIZE_Y = 30;
	
	//Bitmaps
	public static BufferedImage heart;
	public static BufferedImage confirm;

	    
	
	public static void init(){
		loadBitmaps();
		createWindow();
		setupWindow();
	}
	
	public static void createWindow(){
		window = new JFrame(WINDOW_TITLE);
		window.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	public static void setupWindow(){
		panel = new GraphicsPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.addMouseListener(new MouseHandler());
		window.setContentPane(panel);
		panel.revalidate();
		panel.repaint();
	}
	
	public static void loadBitmaps(){
	        try {
	            heart = ImageIO.read(new File("res/heart.png"));
	            confirm = ImageIO.read(new File("res/confirm.png"));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public static JFrame getCurrentWindow(){
		return window;
	}
	
	public static String getStatus(){
		return status;
	}
	
	public static void setStatus(String st){
		status = st;
	}
	
	public static String getCurrentPlayer(){
		return currentPlayer;
	}
	
	public static void setCurrentPlayer(String name){
		currentPlayer = name;
	}
}
