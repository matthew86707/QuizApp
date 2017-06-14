package com.jtl.quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jnetwork.TCPConnection;

public class Player implements Serializable {

	private static final long serialVersionUID = 1523530914808653470L;

	// Graphics
	public static final int STATIC_OFFSET_Y = 100;
	public static final int OFFSET_Y = 75;

	public static final int STARTING_HEARTS = 4;
	public static List<Player> allPlayers = new ArrayList<Player>();

	public transient TCPConnection con;
	public String name;
	public int place;
	public List<Heart> hearts = new ArrayList<Heart>();

	public Player() {
		for (int i = 0; i < STARTING_HEARTS; i++) {
			hearts.add(new Heart(1));
		}
	}

	public static void resetAllPlaces() {
		for (Player player : allPlayers) {
			player.place = -1;
		}
	}
}
