package ch.epfl.xblast.server.drawer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public final class BoardPainter {
	public static final Byte BYTE_IRON_FLOOR   = 000;
	public static final Byte BYTE_IRON_FLOOR_S = 001;
	public static final Byte BYTE_DARK_BLOCK   = 002;
	public static final Byte BYTE_EXTRA        = 003;
	public static final Byte BYTE_EXTRA_O      = 004;
	public static final Byte BYTE_BOMB         = 005;
	public static final Byte BYTE_RANGE        = 006;

    
    final public Map<Block, BlockImage> palette;
    public BoardPainter(Map<Block, BlockImage> palette, BlockImage shadowedBlock) {
        this.palette = Collections.unmodifiableMap(new HashMap<>(palette));
    }
    
    /**
     * @return The byte corresponding to the given Cell on the given Board
     */
    public Byte byteForCell(Board board, Cell cell){
        Block b = board.blockAt(cell);
        switch (b){
        case BONUS_BOMB:
            return BYTE_BOMB;
        case BONUS_RANGE:
            return BYTE_RANGE;
        case CRUMBLING_WALL:
            return BYTE_EXTRA_O;
        case DESTRUCTIBLE_WALL:
            return BYTE_EXTRA;
        case FREE:
            boolean shaded = false;
//            for (Direction dir : Direction.values())
            Direction dir = Direction.W;
                shaded |= board.blockAt(cell.neighbor(dir)).castsShadow(); // FIXME: Unsafe operation! OutOfBounds?
            if (shaded) return BYTE_IRON_FLOOR_S;
            return BYTE_IRON_FLOOR;
        case INDESTRUCTIBLE_WALL:
            return BYTE_DARK_BLOCK;
        default:
            throw new RuntimeException("Framework Error");
        }
    }
}
