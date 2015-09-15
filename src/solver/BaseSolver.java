package solver;

import game.Block;
import game.Board;

import java.util.Arrays;

/** Base helper class for any solver */
abstract class BaseSolver {
	
	static boolean DEBUG = true;
	
	static void check(Board board, Block[] blocks, int numPlayers) {
		int blockSizeTotal = Arrays.asList(blocks).stream().mapToInt(b -> b.coords.size()).sum();
		if (4 * blockSizeTotal > board.width * board.height) {
			throw new AssertionError(String.format("Player pieces take %s spots, but board only has %s spots",
					4 * blockSizeTotal,
					board.width * board.height
				));
		}
	}

	static void out(String format, Object ... args) {
		if (DEBUG)
			System.out.println(String.format(format, args));
	}
}
