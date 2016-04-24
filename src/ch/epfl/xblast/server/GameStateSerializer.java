package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;

public final class GameStateSerializer {
	private GameStateSerializer() { }
	public static List<Byte> serialize(BoardPainter bp, GameState gs){
		//Related to board
		List<Cell> spiralList = Cell.SPIRAL_ORDER;
		List<Byte> message = new ArrayList<>();
		List<Byte> plainBoard = new ArrayList<>();
		for(Cell c : spiralList){
			plainBoard.add(bp.byteForCell(gs.board(), c));
		}
		//message.add((byte)spiralList.size());
		message.addAll(RunLengthEncoder.encode(plainBoard));
		
		return message;
	}
}
