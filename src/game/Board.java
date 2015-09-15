package game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Game board
 * <p>
 * Responsible for validating and tracking player moves
 * <p>
 * Not responsible for whose turn it is or game-specific logic
 */
public class Board {
	public final int width;
	public final int height;
	private final int[][] board;
	
	private Board(int maxX, int maxY) {
		this.width = maxX;
		this.height = maxY;
		this.board = new int[maxX][maxY];
	}
	
	private boolean isInBoard(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	public boolean placeInitial(int player, Block b, Coord boardCoord) {
		List<Coord> coords2check = b.coords.stream().map(blockCoord -> Coord.of(blockCoord.x + boardCoord.x, blockCoord.y + boardCoord.y)).collect(Collectors.toList());
		if (!coords2check.stream().allMatch(c -> isInBoard(c.x, c.y)))
			return false;
		
		if (!coords2check.stream().anyMatch(c -> (c.x == 0 || c.x == width - 1) && (c.y == 0 || c.y == height - 1)))
			return false;
		
		for (Coord c : coords2check) {
			if (board[c.x][c.y] != 0)
				return false;
		}
		
		for (Coord c : coords2check) {
			board[c.x][c.y] = player;
		}
		
		return true;
	}
	
	/** 
	 * Attempts to place the player's block at the coordinate
	 * @return True if it was a valid move (upon which the board will be updated), false otherwise
	 */
	public boolean tryPlace(int player, Block b, Coord boardCoord) {
		boolean foundDiag = false;
		for (Coord blockCoord : b.coords) {
			int xOnBoard = boardCoord.x + blockCoord.x;
			int yOnBoard = boardCoord.y + blockCoord.y;
			if (!isInBoard(xOnBoard, yOnBoard))
				return false;
			
			// Ensure all positions are open
			if (board[xOnBoard][yOnBoard] != 0)
				return false;
			
			// Ensure not adjacent to a player's existing block
			for (Coord adj : Coord.ADJACENT) {
				int x2check = xOnBoard + adj.x;
				int y2check = yOnBoard + adj.y;
				if (isInBoard(x2check, y2check) && board[x2check][y2check] == player)
					return false;
			}
			
			// Ensure it is diagonal to a player's existing block
			for (Coord diag : Coord.DIAGONAL) {
				int x2check = xOnBoard + diag.x;
				int y2check = yOnBoard + diag.y;
				if (isInBoard(x2check, y2check) && board[x2check][y2check] == player)
					foundDiag = true;
			}
		}
		
		if (!foundDiag)
			return false;
		
		for (Coord blockCoord: b.coords)
			board[boardCoord.x + blockCoord.x][boardCoord.y + blockCoord.y] = player;
		
		return true;
	}
	
	/** Unset the player's block from the coordinate */
	public void unplace(int player, Block b, Coord boardCoord) {
		for (Coord coords : b.coords) {
			int x2check = boardCoord.x + coords.x;
			int y2check = boardCoord.y + coords.y;
			if (board[x2check][y2check] != player)
				throw new AssertionError(String.format(
						"Expected to find player %s, but found %s at (%s, %s)",
						player,
						board[x2check][y2check],
						x2check,
						y2check
					));
			board[x2check][y2check] = 0;
		}
	}
	
	/** @return All squres taken by the player's existing blocks */
	public List<Coord> getExistingSquares(int player) {
		List<Coord> possibleMoves = new ArrayList<>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (board[x][y] == player) {
					possibleMoves.add(Coord.of(x, y));
				}
			}
		}
		return possibleMoves;
	}
	
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				sb.append(board[x][y]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/** @return Board with the given dimensions */
	public static Board create(int x, int y) {
		return new Board(x, y);
	}
}
