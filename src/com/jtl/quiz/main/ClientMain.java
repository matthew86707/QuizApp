package com.jtl.quiz.main;

import java.awt.Desktop;
import java.awt.font.GraphicAttribute;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;

import com.jtl.quiz.graphics.GraphicsManager;
import com.jtl.quiz.graphics.GraphicsPanel;
import com.jtl.quiz.graphics.MouseHandler;
import com.jtl.quiz.graphics.SpinPanel;
import com.jtl.quiz.graphics.SpinWindow;
import com.jtl.quiz.model.Heart;
import com.jtl.quiz.model.Option;
import com.jtl.quiz.model.OptionSet;
import com.jtl.quiz.model.Player;

public class ClientMain {
	public static boolean goodSpin = false;
	public static OptionSet currentOptions;
	public static Option currentOption;
	public static Option finalOption;
	public static boolean hasOptions = false;
	public static TCPConnection client;
	public static Player changed = null;

	public static void main(String[] args) {
		GraphicsManager.init();
		try {
			//TCPConnection client = new TCPConnection("192.168.7.88", 1337);
			String ip = JOptionPane.showInputDialog("Ip : ");
			client = new TCPConnection(ip, 1337);
			
			// Get user name
			Scanner in = new Scanner(System.in);
			System.out.print("Enter your username: ");
			String username = in.nextLine();
			
			// Construct client's player object
			Player me = new Player();
			me.name = username;
			
			// Update graphics to display Playing As : name
			GraphicsManager.setCurrentPlayer(me.name);
			GraphicsManager.getCurrentWindow().repaint();
			
			// Give server our constructed object
			client.getOutputStream().writeObject(me);

			//Main client loop
			while (!client.isClosed()) {
				
				//Read image file and display it
				int toRead = client.getInputStream().readInt();
				for (int i = 0; i < toRead; i++) {
					File temp = new File("tmp_" + i + ".png");
					temp.delete();
					temp.createNewFile();

					client.getInputStream().readFile(temp);
					Desktop.getDesktop().open(temp);
				}
				
				//Collect answers and send them in a data package with our current player object
				System.out.print("Enter your answers: ");
				String answers = in.nextLine();
				client.getOutputStream().writeObject(
						new DataPackage(me, answers));

				// Get servers version of all clients for rendering
				updatePlayersInfo();

				// Wait for server to respond with who's turn it is...
				Object mssg = client.getInputStream().readObject();
				String mssgCastString;
				try {
					mssgCastString = (String) mssg;
				} catch (ClassCastException e) {
					mssgCastString = "";
				}
				// TODO : Investigate
				try {
					Thread.sleep(0);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int place = 0;
				while (!((mssgCastString).equals("Done"))) {

					Player player = (Player) mssg;
					place++;
					player.place = place;
					if (player.name.equals(me.name)) {

						// Populate current options list based on what place the server says your in

						if (player.place == 3) {
							currentOptions = new OptionSet();
							currentOptions.options.add(new Option(-1, 1.0f,
									"-1 Heart"));
							GraphicsManager.getCurrentWindow().repaint();
						}

						if (player.place == 2) {
							currentOptions = new OptionSet();
							currentOptions.options.add(new Option(1, 0.5f,
									"50% Chance +1 Heart"));
							currentOptions.options.add(new Option(-1, 1.0f,
									"-1 Heart"));
							GraphicsManager.getCurrentWindow().repaint();
						}
						if (player.place == 1) {
							currentOptions = new OptionSet();
							currentOptions.options.add(new Option(1, 1.0f,
									"+1 Heart"));
							currentOptions.options.add(new Option(-2, 1.0f,
									"-2 Hearts"));
							GraphicsManager.getCurrentWindow().repaint();
						}

						//Alert the player it is their turn for selection
						GraphicsManager.setStatus("Make Your Selection, "
								+ player.name + "...");
						GraphicsManager.getCurrentWindow().repaint();
						
						//Will refactor to use an object lock later, for now it waits until you 
						//have chosen a player's data to change on your turn and set a reference to
						//that player you wish to change in the changed var
						while (changed == null) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//Tells the server your requested change, the server will update its data accordingly
						client.getOutputStream().writeObject(changed);
						SpinPanel.isFinished = false;
					} else {
						currentOption = null;
						currentOptions = null;
						finalOption = null;
						changed = null;
						GraphicsManager.setStatus("Waiting for " + player.name
								+ "...");
						GraphicsManager.getCurrentWindow().repaint();
					}

					//Update everyone after you have finished your selection
					updatePlayersInfo();

					mssg = client.getInputStream().readObject();
					try {
						mssgCastString = (String) mssg;
					} catch (ClassCastException e) {
						mssgCastString = "";
					}
				}

				place = 0;

				currentOption = null;
				currentOptions = null;
				finalOption = null;
				changed = null;

				GraphicsManager.setStatus("Next Question...");
				GraphicsManager.getCurrentWindow().repaint();
			}
			in.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void updatePlayersInfo() throws ClassNotFoundException,
			IOException {
		DataPackage dp = (DataPackage) (client.readObject());
		int length = dp.getObjects().length;
		Player[] players = new Player[length];
		for (int i = 0; i < players.length; i++) {
			players[i] = (Player) dp.getObjects()[i];
		}
		boolean isInList = false;
		for (Player p : players) {
			isInList = false;
			for (int j = 0; j < Player.allPlayers.size(); j++) {
				if (Player.allPlayers.get(j).name.equals(p.name)) {
					Player.allPlayers.set(j, p);
					isInList = true;
				}
			}
			if (!isInList) {
				Player.allPlayers.add(p);
			}
		}
		GraphicsManager.getCurrentWindow().repaint();
	}
}
