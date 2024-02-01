package com.example.model;

import java.util.Iterator;

import org.springframework.stereotype.Component;

@Component
public class Board {
	
 private int size;
 private char[][] grid;
 
 	public Board(int size) {
		this.size = size;
		this.grid = new char[size][size];
		
		for(int a = 0 ; a < size ; a++) {
			for(int b = 0 ; b < size ; b++) {
				grid[a][b] = ' ';
			}
		}
 	}

	public char[][] getGrid() {
		return grid;
	}
	
	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public int getSize() {
		return size;
	}

}
