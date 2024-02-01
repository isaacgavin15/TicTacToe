package com.example.model;

import org.springframework.stereotype.Component;

@Component
public class Game {
	
	private Board board;
	private Player p1;
	private Player p2;
	
	private final char p1_sign = 'x';
	private final char p2_sign = 'o';
	
	public Game(int size, String p1, String p2) {
		this.board = new Board(size);
		this.p1 = new Player(p1, p1_sign);
		this.p2 = new Player(p2, p2_sign);
	}
	
	public void Move(Player player, int x, int y) {
		char[][] temp = board.getGrid();
		temp[x][y] = player.getSign();
		board.setGrid(temp);
	}

	public Board getBoard() {
		return board;
	}

	public Player getP1() {
		return p1;
	}

	public Player getP2() {
		return p2;
	}
	
}
