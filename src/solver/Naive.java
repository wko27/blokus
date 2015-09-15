package solver;
import game.Block;
import game.Board;
import game.Coord;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Solver which attempts to do a naive DFS straight down the game tree */
public class Naive extends BaseSolver {
	private static final int BOARD_SIZE = 6;
	
	private static final Block[] BLOCKS = {
		Block.of(Coord.of(0, 0), Coord.of(0, 1), Coord.of(1, 0)),
		Block.of(Coord.of(0, 0), Coord.of(0, 1), Coord.of(0, 2)),
		Block.of(Coord.of(0, 0), Coord.of(0, 1)),
		Block.of(Coord.of(0, 0)),
	};
	
	public static void main(String[] args) {
		int boardWidth = BOARD_SIZE;
		int boardHeight = BOARD_SIZE;
		
		Board board = Board.create(boardWidth, boardHeight);
		
		BaseSolver.check(board, BLOCKS, 4);
		
		// Set up 4 players with all blocks
		Map<Integer, List<Block>> pieces = new LinkedHashMap<>();
		for (int i = 1; i <= 4; i++) {
			pieces.put(i, new LinkedList<>(Arrays.asList(BLOCKS.clone())));
		}
		
		boolean b = move(board, pieces, 1);
		System.out.println("Can be solved? " + b);
		if (b) {
			System.out.println("Final board:");
			System.out.println(board);
		}
	}
	
	static int moveCounter = 0;
	static int playerCounter = 0;
	
	private static boolean move(Board board, Map<Integer, List<Block>> pieces, int player) {
		playerCounter++;
		
		if (pieces.values().stream().allMatch(p -> p.isEmpty()))
			return true;

		List<Coord> existingSquares = board.getExistingSquares(player);
		
		List<Block> remainingPieces = pieces.get(player);
		boolean isInitialMove = remainingPieces.size() == BLOCKS.length;
		
		if (playerCounter == 12)
			System.out.println(playerCounter);
		
		for (Block originalBlock : remainingPieces) {
			for (Block block : originalBlock.rotations) {
				Set<Coord> possibleMoves = new HashSet<>();
				if (isInitialMove) {
					for (int x = 0; x < BOARD_SIZE; x++) {
						for (int y = 0; y < BOARD_SIZE; y++) {
							possibleMoves.add(Coord.of(x, y));
						}
					}
				} else {
					for (Coord blockCoord : block.coords) {
						for (Coord diag : Coord.DIAGONAL) {
							for (Coord square : existingSquares) {
								possibleMoves.add(Coord.of(
										square.x + diag.x - blockCoord.x,
										square.y + diag.y - blockCoord.y
									));
							}
						}
					}
				}
				
				for (Coord move : possibleMoves) {
					moveCounter ++;
					
//					if (moveCounter == 1849)
//						System.out.println("hmm");
//					
//					if (playerCounter == 16)
//					if (move.equals(Coord.of(8, 6)) || move.equals(Coord.of(6, 8)))
//						System.out.println("eh");
					
					boolean validMove = isInitialMove
						? board.placeInitial(player, block, move)
						: board.tryPlace(player, block, move);
					
					if (validMove) {
						out("Player %s plays at %s, block:\n%s\n\nBoard is now:\n%s",
								player,
								move,
								block,
								board
							);
						
						// Move to next player's turn
						List<Block> removed = new LinkedList<>(remainingPieces);
						removed.remove(originalBlock);
						pieces.put(player, removed);
						
						if (move(board, pieces, player == 4 ? 1 : player + 1))
							return true;
						
						// Roll back this turn
						pieces.put(player, remainingPieces);
						board.unplace(player, block, move);
						
						out("Undoing move from player %s at %s, block:\n%s\n\nBoard is now:\n%s",
								player,
								move,
								block,
								board
							);
					}
				}
			}
		}
		
		if (playerCounter % 1000 == 0)
			System.out.println(playerCounter);
		
		// No more valid positions		
		out("No more valid moves found for player %s with %s remaining pieces, board is:\n%s\n\nRemaining pieces are:\n",
				player,
				remainingPieces.size(),
				board
			);
		for (Block b : remainingPieces) {
			out(b + "\n\n");
		}
		
		return false;
	}
}
