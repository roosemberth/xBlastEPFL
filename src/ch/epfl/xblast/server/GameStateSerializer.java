package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

public final class GameStateSerializer {
	private GameStateSerializer() { }
	public static List<Byte> serialize(BoardPainter bp, GameState gs){
		//Related to board
		List<Byte> message = new ArrayList<>();
		List<Byte> plainBoard = new ArrayList<>();
		for(Cell c : Cell.SPIRAL_ORDER){
			plainBoard.add(bp.byteForCell(gs.board(), c));
		}
		List<Byte> encodedBoard = RunLengthEncoder.encode(plainBoard);
		message.add((byte)encodedBoard.size());
		message.addAll(encodedBoard);
		//Related to bombs and explosions
		List<Byte> plainBombs = new ArrayList<>();
		for(Cell c : Cell.ROW_MAJOR_ORDER){
		    if(gs.bombedCells().containsKey(c))
		        plainBombs.add(ExplosionPainter.byteForBomb(gs.bombedCells().get(c)));
		    else if(gs.blastedCells().contains(c)&&gs.board().blockAt(c).canHostPlayer()){
		        plainBombs.add(ExplosionPainter.byteForBlast(
		                gs.blastedCells().contains(c.neighbor(Direction.N)), 
		                gs.blastedCells().contains(c.neighbor(Direction.E)), 
		                gs.blastedCells().contains(c.neighbor(Direction.S)), 
		                gs.blastedCells().contains(c.neighbor(Direction.W))));
		    }
		    else
		        plainBombs.add(ExplosionPainter.BYTE_FOR_EMPTY);
        }
		List<Byte> encodedBombs = RunLengthEncoder.encode(plainBombs);
		message.add((byte)encodedBombs.size());
		message.addAll(encodedBombs);		
		
		//Related to players
		for(Player p : gs.players()){
		    message.add((byte)p.lives());
            message.add((byte)p.position().x());
            message.add((byte)p.position().y());
            message.add((byte)PlayerPainter.byteForPlayer(gs.ticks(),p));
	    }
		//Related to ticks
		message.add((byte)Math.ceil(gs.remainingTime()/2.0));
		return message;
	}
}
