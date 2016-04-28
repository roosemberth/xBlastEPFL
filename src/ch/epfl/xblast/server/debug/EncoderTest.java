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
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Player;

public class EncoderTest {

	public static void main(String[] args) {
		  
	        
	        String s = GameStateSerializer.serialize(Level.DEFAULT_LEVEL.getBp(),Level.DEFAULT_LEVEL.getGs()).toString();
	        String toCompare = Arrays.asList(121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3,
	                1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1,
	                3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
	                2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
	                3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
	                1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2,
	                3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2,

	                4, -128, 16, -63, 16,

	                3, 24, 24, 6,
	                3, -40, 24, 26,
	                3, -40, -72, 46,
	                3, 24, -72, 66,

	                60).toString();
	        System.out.println(s.equals(toCompare));
	        System.out.println(s);
	        System.out.println(toCompare);
	        List<Byte> testRepetitions = new ArrayList<>();
	        for(int i = 0; i < 390; i++){
	            testRepetitions.add((byte)0);
	        }
	        System.out.println(RunLengthEncoder.encode(testRepetitions));
	}

}
