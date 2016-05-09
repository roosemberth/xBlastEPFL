package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Images.BlockImage;

public final class Level {
	
	public final static Level DEFAULT_LEVEL = new Level(defaultBoardPainter(), defaultGameState());//BOARD?
	private final BoardPainter bp;
	private final GameState gs;
	
	public Level(BoardPainter bp, GameState gs){
		this.bp = Objects.requireNonNull(bp);
		this.gs = Objects.requireNonNull(gs);
	}
	
	public BoardPainter getBp(){
		return bp;
	}
	
	public GameState getGs(){
		return gs;
	}
	
	public static GameState defaultGameState(){
	    Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        
        Board board = Board.ofRows(
                Arrays.asList(
                        Arrays.asList(XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX),
                        Arrays.asList(XX, __, __, __, __, __, xx, __, xx, __, __, __, __, __, XX),
                        Arrays.asList(XX, __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx, XX, __, XX),
                        Arrays.asList(XX, __, xx, __, __, __, xx, __, xx, __, __, __, xx, __, XX),
                        Arrays.asList(XX, xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __, XX, xx, XX),
                        Arrays.asList(XX, __, xx, __, xx, __, __, __, __, __, xx, __, xx, __, XX),
                        Arrays.asList(XX, xx, XX, xx, XX, xx, XX, __, XX, xx, XX, xx, XX, xx, XX),
                        Arrays.asList(XX, __, xx, __, xx, __, __, __, __, __, xx, __, xx, __, XX),
                        Arrays.asList(XX, xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __, XX, xx, XX),
                        Arrays.asList(XX, __, xx, __, __, __, xx, __, xx, __, __, __, xx, __, XX),
                        Arrays.asList(XX, __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx, XX, __, XX),
                        Arrays.asList(XX, __, __, __, __, __, xx, __, xx, __, __, __, __, __, XX),
                        Arrays.asList(XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX)));
        
        List<Player> players = new ArrayList<>();
        players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1,1),2, 3));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13,1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13,11), 2, 3));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1,11), 2, 3));
        
        GameState gameState = new GameState(board, players);
        return gameState;
	}

	public static BoardPainter defaultBoardPainter(){
        Map<Block,BlockImage> blockPalette = new HashMap<>();
        for(Block b : Block.values()){
            blockPalette.put(b, BlockImage.values()[b.ordinal() > 0 ? b.ordinal()+1 : b.ordinal()]);
        }
        BlockImage blockShadowed = BlockImage.IRON_FLOOR_S;
        return new BoardPainter(blockPalette, blockShadowed);
    }
}
