package ch.epfl.xblast.server;

import java.util.List;
import java.util.Map;

import ch.epfl.xblast.server.drawer.BlockImage;
import ch.epfl.xblast.server.drawer.BoardPainter;

public class Level {
	private final BoardPainter boardPainter;
	private final GameState gameState;
	private static final Map<Block, BlockImage> DEFAULT_PALETTE = Defaults.DEFAULT_PALETTE;
	private static final Board					DEFAULT_BOARD   = Defaults.DEFAULT_BOARD;
	private static final List<Player>			DEFAULT_PLAYERS = Defaults.DEFAULT_PLAYERS;

	public static final Level DEFAULT_LEVEL = new Level(
	                new BoardPainter(DEFAULT_PALETTE, BlockImage.IRON_FLOOR_S),
	                new GameState(DEFAULT_BOARD, DEFAULT_PLAYERS)
	                );
	
	public Level(BoardPainter bp, GameState gs) {
        this.boardPainter = bp;
        this.gameState = gs;
    }
	
	public BoardPainter getBoardPainter() { return boardPainter; }
	public GameState    getGameState()    { return gameState; }

}
