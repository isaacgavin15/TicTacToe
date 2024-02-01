package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Game;
import com.example.model.Player;

@Controller
@RequestMapping("/tictac")
public class TicTacToeController {
	
	private Game game;

	@GetMapping("/home")
	public String home(Model model) {
		return "home";
	}
	
	@GetMapping("/create")
	public String createBoard(@RequestParam("size") int size, @RequestParam("p1") String p1, 
			@RequestParam("p2") String p2 , Model model) {
		
		game = new Game(size, p1, p2);
		
		model.addAttribute("boardSize", size);
		model.addAttribute("currBoard", game.getBoard().getGrid());
		model.addAttribute("currPlayer", game.getP1());
		
		return "game";
	}
	
	@PostMapping("/move")
	public String createBoard(@ModelAttribute("currPlayer") Player currPlayer, @RequestParam("x") int x, 
			@RequestParam("y") int y , Model model) {

		//turns did not change if move is not valid
		if(isNotValid(x,y)) {
			model.addAttribute("message", "Invalid coordinate");
			model.addAttribute("boardSize", game.getBoard().getSize());
			model.addAttribute("currBoard", game.getBoard().getGrid());
			model.addAttribute("currPlayer", currPlayer);
			return "game";
		}
		
		if(currPlayer.getName().equals(game.getP1().getName())) {
			game.Move(game.getP1(), x, y);
			model.addAttribute("currPlayer", game.getP2());
		} else {
			game.Move(game.getP2(), x, y);
			model.addAttribute("currPlayer", game.getP1());
		}
		
		if(won()) {
			model.addAttribute("message", "Player : " + currPlayer.getName() + " Win!");
			// state == 1 -> Finished
			// state == 0 -> Draw
			model.addAttribute("state", "1");
			model.addAttribute("boardSize", game.getBoard().getSize());
			model.addAttribute("currBoard", game.getBoard().getGrid());
			model.addAttribute("currPlayer", currPlayer);
			return "game";
		} 
		else if (boardFull()) {
			model.addAttribute("message", "Draw!, Board is Full");
			// state == 1 -> Finished
			// state == 0 -> Draw
			model.addAttribute("state", "0");
			model.addAttribute("boardSize", game.getBoard().getSize());
			model.addAttribute("currBoard", game.getBoard().getGrid());
			model.addAttribute("currPlayer", currPlayer);
			return "game";
		}
		
		model.addAttribute("message", "Player : " + currPlayer.getName() + " fill this coordinate : (" + x + ", " + y + ")");
		model.addAttribute("boardSize", game.getBoard().getSize());
		model.addAttribute("currBoard", game.getBoard().getGrid());
		
		return "game";
	}
	
	public boolean isNotValid(int x, int y) {
		
		if((x < 0 || x >= game.getBoard().getSize()) || (y < 0 || y >= game.getBoard().getSize())) {
			return true;
		}
		
		char[][] grid = game.getBoard().getGrid();
		
		return grid[x][y] != ' ';
	}
	
	public boolean won() {
		
		if(checkRow() || checkCol() || checkDiag()) {
			return true;
		}
		
		return false;
	}
	
	public boolean boardFull(){
		
		int size = game.getBoard().getSize();
		char[][] board = game.getBoard().getGrid();
		
		for(int a = 0 ; a < size ; a++) {
			for(int b = 0 ; b < size ; b++) {
				if(board[a][b] == ' ') {
					return false;
				}
			}
		}
		
		return true;
	}
	public boolean checkRow() {
		int size = game.getBoard().getSize();
		char[][] board = game.getBoard().getGrid();
		
		for(int row = 0 ; row < size ; row++ ) {
			int count = 1;
			char firstChar = board[row][0];
			
			for(int col = 1 ; col < size ; col++) {
				char currCell = board[row][col];
				if(' ' != firstChar && (firstChar == currCell)) {
					count++;
					if(count == 3) return true;
				}
				else {
					//change of current symbol, reset counter
					count = 1;
					firstChar = currCell;
				}
			}
		}
		return false;
	}
	
	public boolean checkCol() {
		int size = game.getBoard().getSize();
		char[][] board = game.getBoard().getGrid();
		
		for(int col = 0 ; col < size ; col++ ) {
			int count = 1;
			char firstChar = board[0][col];
			
			for(int row = 1 ; row < size ; row++) {
				char currCell = board[row][col];
				if(' ' != firstChar && (firstChar == currCell)) {
					count++;
					if(count == 3) return true;
				}
				else {
					//change of current symbol, reset counter
					count = 1;
					firstChar = currCell;
				}
			}
		}
		return false;
	}
	
	public boolean checkDiag() {
		
		int size = game.getBoard().getSize();
		
		// Check all diagonals starting from the first row
        for (int col = 0; col < size; col++) {
            if (checkDiagonals(0, 1, col, 1, size)) {
                return true;
            }
        }

        // Check all diagonals starting from the first column
        for (int row = 1; row < size; row++) {
            if (checkDiagonals(row, 1, 0, 1, size)) {
                return true;
            }
        }

        // Check all diagonals starting from the last column
        for (int row = 0; row < size; row++) {
            if (checkDiagonals(row, 1, size - 1, -1, size)) {
                return true;
            }
        }
		
		return false;
	}
	
	public boolean checkDiagonals(int startRow, int rowIncr, int startCol, int colIncr, int size ) {
		int count = 1;
		char[][] board = game.getBoard().getGrid();
		char firstChar = board[startRow][startCol];
		
		for (int a = 1; a < size; a++) {
            int currentRow = startRow + a * rowIncr;
            int currentCol = startCol + a * colIncr;

            if (currentRow < 0 || currentRow >= size || currentCol < 0 || currentCol >= size) {
                return false;  // Out of bounds, not a winning diagonal
            }

            if (' ' != firstChar && (firstChar == board[currentRow][currentCol])) {
                count++;
                if(count == 3) return true;
            }
            else {
            	count = 1;
            	firstChar = board[currentRow][currentCol];
            }
        }
		
		return false;
	}
}
