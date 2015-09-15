package game;
import java.util.Collection;

/** Represents a coordinate */
public class Coord {
	/** Offsets for diagonal blocks */
	public static final Coord[] DIAGONAL = {
		of(-1, -1),
		of(1, -1),
		of(-1, 1),
		of(1, 1),
	};
	
	/** Offsets for adjacent blocks */
	public static final Coord[] ADJACENT = {
		of(-1, 0),
		of(0, -1),
		of(0, 1),
		of(1, 0),
	};
	
	public final int x;
	public final int y;
	
	private Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord plus(Coord other) {
		return Coord.of(x + other.x, y + other.y);
	}
	
	public Coord minus(Coord other) {
		return Coord.of(x - other.x, y - other.y);
	}
	
	@Override public boolean equals(Object obj) {
		if (!(obj instanceof Coord))
			return false;
		Coord other = (Coord)obj;
		return x == other.x && y == other.y;
	}
	
	@Override public int hashCode() {
		return x + 31 * y;
	}
	
	@Override public String toString() {
		return String.format("(%s, %s)", x, y);
	}
	
	public static Coord of(int x, int y) {
		return new Coord(x, y);
	}
	
	static int getMaxX(Collection<Coord> coords) {
		return coords.stream().mapToInt(c -> c.x).max().getAsInt();
	}
	
	static int getMaxY(Collection<Coord> coords) {
		return coords.stream().mapToInt(c -> c.y).max().getAsInt();
	}
	
	static int getMinX(Collection<Coord> coords) {
		return coords.stream().mapToInt(c -> c.x).min().getAsInt();
	}
	
	static int getMinY(Collection<Coord> coords) {
		return coords.stream().mapToInt(c -> c.y).min().getAsInt();
	}
}