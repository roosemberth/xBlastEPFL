package ch.epfl.xblast.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.drawer.BlockImage;

public class Defaults {
	public static final Map<Block, BlockImage> DEFAULT_PALETTE = buildDefaultPalette();
	public static final Board DEFAULT_BOARD = buildDefaultBoard();
	public static final List<Player> DEFAULT_PLAYERS = buildDefaultPlayers();
	
    private static Board buildDefaultBoard() {
        Block E0 = Block.FREE; // Not Shadowed
        Block E1 = Block.FREE; // Shadowed
        Block E2 = Block.INDESTRUCTIBLE_WALL;
        Block E3 = Block.DESTRUCTIBLE_WALL;

		// Do not modify this code: Automatically generated
        return Board.ofRows(
                Arrays.asList(
                        Arrays.asList(E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2),
                        Arrays.asList(E2,E1,E0,E0,E0,E0,E3,E1,E3,E1,E0,E0,E0,E0,E2),
                        Arrays.asList(E2,E1,E2,E3,E2,E3,E2,E3,E2,E3,E2,E3,E2,E1,E2),
                        Arrays.asList(E2,E1,E3,E1,E0,E0,E3,E1,E3,E1,E0,E0,E3,E1,E2),
                        Arrays.asList(E2,E3,E2,E1,E2,E2,E2,E2,E2,E2,E2,E1,E2,E3,E2),
                        Arrays.asList(E2,E1,E3,E1,E3,E1,E0,E0,E0,E0,E3,E1,E3,E1,E2),
                        Arrays.asList(E2,E3,E2,E3,E2,E3,E2,E1,E2,E3,E2,E3,E2,E3,E2),
                        Arrays.asList(E2,E1,E3,E1,E3,E1,E0,E0,E0,E0,E3,E1,E3,E1,E2),
                        Arrays.asList(E2,E3,E2,E1,E2,E2,E2,E2,E2,E2,E2,E1,E2,E3,E2),
                        Arrays.asList(E2,E1,E3,E1,E0,E0,E3,E1,E3,E1,E0,E0,E3,E1,E2),
                        Arrays.asList(E2,E1,E2,E3,E2,E3,E2,E3,E2,E3,E2,E3,E2,E1,E2),
                        Arrays.asList(E2,E1,E0,E0,E0,E0,E3,E1,E3,E1,E0,E0,E0,E0,E2),
                        Arrays.asList(E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2,E2)
                        )
                );
/* vim: autoload script converter::datainput
°1°°2°°3°°4°°5°°6°°7°°8°°9°10°11°12°13°14°15°
---------------------------------------------
 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
 1, 0, 0, 0, 0, 3, 1, 3, 1, 0, 0, 0, 0,
 1, 1, 3, 1, 3, 1, 3, 1, 1, 0,
 0, 0, 0, 1, 3, 1, 3, 0, 0, 0, 0, 1,
 1, 1, 3, 1, 3, 1, 3, 1, 1,
 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
 3, 2, 3, 2, 3, 2, 3, 2,
 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
 3, 2, 3, 2, 3, 2, 3,
 1, 0, 0, 3, 1, 3, 1, 0, 0,
 1, 1, 3, 1, 1, 0,
 0, 1, 3, 1, 3, 0, 0, 1,
 1, 1, 3, 1, 1,
 2, 2, 2, 2, 2, 2, 2,
 3, 2, 3, 2,
 2, 2, 2, 2, 2, 2,
 3, 2, 3,
 1, 0, 0, 0, 0,
 3, 0,
 0, 0, 0, 1,
 3, 2, 1, 2,
 */
    }
    private static List<Player> buildDefaultPlayers() {
	    final Cell POS_NW = new Cell( 1,  1);
        final Cell POS_NE = new Cell(-2,  1);
        final Cell POS_SE = new Cell(-2, -2);
        final Cell POS_SW = new Cell( 1, -2);
        return createPlayers(3, 2, 3, POS_NW, POS_NE, POS_SE, POS_SW);
    }

    private static List<Player> createPlayers(int lives, int maxBombs, int bombRange, Cell p1, Cell p2, Cell p3, Cell p4) {
        return Arrays.asList(
                new Player(PlayerID.PLAYER_1, lives, p1, maxBombs, bombRange),
                new Player(PlayerID.PLAYER_2, lives, p2, maxBombs, bombRange),
                new Player(PlayerID.PLAYER_3, lives, p3, maxBombs, bombRange),
                new Player(PlayerID.PLAYER_4, lives, p4, maxBombs, bombRange)
                );
    }

    private static Map<Block, BlockImage> buildDefaultPalette() {
        Map<Block, BlockImage> ret = new HashMap<>(7);
        ret.put(Block.FREE               ,BlockImage.IRON_FLOOR  );
        ret.put(Block.INDESTRUCTIBLE_WALL,BlockImage.DARK_BLOCK  );
        ret.put(Block.DESTRUCTIBLE_WALL  ,BlockImage.EXTRA       );
        ret.put(Block.CRUMBLING_WALL     ,BlockImage.EXTRA_O     );
        ret.put(Block.BONUS_BOMB         ,BlockImage.BONUS_BOMB  );
        ret.put(Block.BONUS_RANGE        ,BlockImage.BONUS_RANGE );
        return ret;
    }
}
