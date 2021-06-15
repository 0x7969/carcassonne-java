package fop.model;

import java.util.LinkedList;
import java.util.List;

import fop.controller.GameController;

public class Game {

	private LinkedList<Player> players;
	private int playerCount;

	public static final int MAX_PLAYERS = 6;

	public Game(GameController gc) {
		players = new LinkedList<Player>();
		playerCount = 0;
	}
	
	public boolean addPlayer() {
		if (playerCount >= Game.MAX_PLAYERS)
			return false;
		else {
			players.add(new Player(null, null));
			playerCount++;
			return true;
		}
	}
	
	public boolean removePlayer(Player p) {
		if (playerCount > 2) {
			if (players.remove(p)) {
				playerCount--;
				return true;
			} else
				return false;
		} else
			return false;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public int getPlayerCount() {
		return playerCount;
	}
}