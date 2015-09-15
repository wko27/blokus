package game;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a single Block (possibly rotated)
 */
public class Block {
	public final List<Coord> coords;
	public final Set<Block> rotations;
	
	private Block(List<Coord> coords, boolean needsRotation) {
		this.coords = coords;
		
		if (needsRotation) {
			this.rotations = new HashSet<>();
			rotations.add(this);
			for (Block b = rotate(); !b.equals(this); b = b.rotate()) {
				rotations.add(b);
			}
		} else {
			this.rotations = null;
		}
	}
	
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		int maxX = Coord.getMaxX(coords);
		int maxY = Coord.getMaxY(coords);
		for (int x = -1; x <= maxX + 1; x++) {
			for (int y = -1; y <= maxY + 1; y++) {
				Coord coord = Coord.of(x, y);
				boolean isBlock = coords.stream().filter(c -> c.equals(coord)).findAny().isPresent();
				sb.append(isBlock ? "X" : " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static Block of(Coord ... coords) {
		return new Block(Arrays.asList(coords), true);
	}
	
	/**
	 * @return {@link Block} representing this {Block} rotated 90 degrees clockwise,
	 * 			then transforms so the minimum x and y offset is at (0, 0)
	 */
	private Block rotate() {
		List<Coord> rawRotate = coords.stream()
				.map(c -> Coord.of(c.y, -c.x))
				.collect(Collectors.toList());
		int minX = Coord.getMinX(rawRotate);
		int minY = Coord.getMinY(rawRotate);
		List<Coord> shifted = rawRotate.stream()
				.map(c -> Coord.of(c.x - minX, c.y - minY))
				.collect(Collectors.toList());
		
		return new Block(shifted, false);
	}
	
	@Override public boolean equals(Object obj) {
		if (!(obj instanceof Block))
			return false;
		Block other = (Block)obj;
		return coords.equals(other.coords);
	}
	
	@Override public int hashCode() {
		return coords.hashCode();
	}
}