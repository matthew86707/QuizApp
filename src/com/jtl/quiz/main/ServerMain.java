package com.jtl.quiz.main;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jnetwork.ClientData;
import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;
import org.jnetwork.TCPConnectionCallback;
import org.jnetwork.TCPServer;

import com.jtl.quiz.model.Player;

public class ServerMain {
	private static TCPServer server;
	private static final Object LOCK = new Object();
	private static ArrayList<File> toSend = new ArrayList<>();
	private static int answered = 0;
	private static int answeredCorrect = 0;
	private static int clients = 0;
	private static String[] answers;
	private static ServerState currentState = ServerState.AWAITING_ANSWERS;
	static Thread mainServerThread;

	public static void main(String[] args) {
		server = new TCPServer(1337, new TCPConnectionCallback() {
			@Override
			public void clientConnected(ClientData event) {
				TCPConnection client = (TCPConnection) event.getConnection();
				clients++;
				try {
					
					Player newPlayer = (Player) client.readObject();
					System.out.println(newPlayer.name + " connected.");
					newPlayer.con = client;
					Player.allPlayers.add(newPlayer);
					
					while (!event.getConnection().isClosed()) {
						synchronized (LOCK) {
							LOCK.wait();
						}
						client.getOutputStream().writeInt(toSend.size());
						for (File file : toSend) {
							client.getOutputStream().writeFile(file);
						}
						boolean isCorrect = false;
						DataPackage back = (DataPackage) client.getInputStream().readObject();
						Player p = (Player) back.getObjects()[0];
						for(Player ply : Player.allPlayers){
							if(ply.name.equals(p.name)){
								if(checkAnswer(back.getObjects()[1].toString().toUpperCase())){
									
									answeredCorrect = answeredCorrect + 1;

									isCorrect = true;
									ply.place = ServerMain.answeredCorrect;
								}
							}
						}
						answered++;
						System.out.println(p.name + ": " + back.getObjects()[1] + (isCorrect?" <- Correct, Place : " + answeredCorrect : ""));
						isCorrect = false;
					}
				} catch (EOFException | SocketException e) {
					return;
				} catch (InterruptedException | IOException | ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					clients--;
				}
			}
		});
		
		mainServerThread = new Thread(new Runnable() {
			@Override
			public void run(){
			while(true){
				//TODO : Investigate this
				try {
					Thread.sleep(0);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(currentState == ServerState.AWAITING_ANSWERS && clients > 0){
					//Hold thread in this state until everyone has answered, then switch the state to AWAITING_SELECTION for the next time around the loop
					if (answered >= clients) {
						try {
							//TODO : Sometimes, becuase this is checkign so often, it will see that all the answers have been in and reset the correct variable
							//before all the places can be calculated, causing the last person in to get 1st place because the var is reset to 0
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						answered = 0;
						answeredCorrect = 0;
						//TODO : Add back in, commented out for testing on windows...
						//Runtime.getRuntime().exec("say \"Hey Phil, wake up!\"");
						System.out.println("Everyone answered!");
						System.out.println();
						currentState = ServerState.AWAITING_SELECTION;
					}
					
					
				}else if(currentState == ServerState.AWAITING_SELECTION){
					sendPlayersInfo();
					for(int i = 1; i <= ((3 > clients)?clients:3); i++){
						for(Player player :  Player.allPlayers){
							if(player.place == i){
								try {
									for(Player p :  Player.allPlayers){
									p.con.writeObject(player);
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									Player toSave = (Player) player.con.getInputStream().readObject();
									for(int j = 0; j < Player.allPlayers.size(); j++){
										if(Player.allPlayers.get(j).name.equals(toSave.name)){
											TCPConnection conTemp = Player.allPlayers.get(j).con;
											toSave.con = conTemp;
											toSave.place = Player.allPlayers.get(j).place;
											Player.allPlayers.set(j, toSave);
										}
									}
								} catch (ClassNotFoundException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								sendPlayersInfo();
								break;
							}
						}
					}
					
					for(Player player :  Player.allPlayers){

						try {
							player.con.getOutputStream().writeObject(new String("Done"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					Player.resetAllPlaces();
					
					currentState = ServerState.AWAITING_ANSWERS;
				}
			}
			}
		
		});
		mainServerThread.start();

		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					JFileChooser chooser = new JFileChooser();
					chooser.setMultiSelectionEnabled(true);

					Scanner in = new Scanner(System.in);
					while (!Thread.currentThread().isInterrupted()) {
						String line = in.nextLine();
						if (line.equalsIgnoreCase("image")) {
							if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
								toSend.clear();
								for (File file : chooser.getSelectedFiles()) {
									toSend.add(file);
								}
								System.out.print("Before we send the question, please input the correct answers : ");
								answers = in.nextLine().toUpperCase().split("");
								synchronized (LOCK) {
									LOCK.notifyAll();
								}
							}
						}
					}
					in.close();
				}
			});
			thread.start();
			server.start();
			server.waitUntilClose();
			server.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendPlayersInfo(){
		Player[] players = new Player[Player.allPlayers.size()];
		for(int i = 0; i < players.length; i++){
			//System.out.println("" + Player.allPlayers.get(i).name + "...Hearts = " + Player.allPlayers.get(i).hearts.size());
			players[i] = Player.allPlayers.get(i);
		}
		DataPackage dp = new DataPackage(players);
		for(Player p : Player.allPlayers){
			try{
			p.con.getOutputStream().writeObject(dp);
			}catch (IOException e){
				
			}
		}
	}
	
	public static boolean checkAnswer(String answer){
		List<String> correctLetters = new ArrayList<>();
		List<String> allLetters = new ArrayList<>();
		allLetters.addAll(Arrays.asList(answer.split("")));
		correctLetters.addAll(Arrays.asList(answers));
		for(int i = 0; i < allLetters.size(); i++){
			for(int j = 0; j < correctLetters.size(); j++){
				if(allLetters.get(i).equals(correctLetters.get(j))){
					allLetters.remove(i);
					correctLetters.remove(j);
					i--;
					j--;
					break;
				}
			}
		}
		if(allLetters.size() == 0 && correctLetters.size() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	private enum ServerState{
		AWAITING_ANSWERS,
		AWAITING_SELECTION
	}
}
