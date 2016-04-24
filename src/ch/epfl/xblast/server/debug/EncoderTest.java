package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.BoardPainter;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Player;

public class EncoderTest {

	public static void main(String[] args) {
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
	        
	        String s = GameStateSerializer.serialize(BoardPainter.defaultBoardPainter(), gameState).toString();
	        String toCompare = Arrays.asList(-50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3,
	 1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1,
	 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
	 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
	 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
	 1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2,
	 3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2).toString();
	        System.out.println(s.equals(toCompare));
	        System.out.println(s);
	        System.out.println(toCompare);
	}

}
