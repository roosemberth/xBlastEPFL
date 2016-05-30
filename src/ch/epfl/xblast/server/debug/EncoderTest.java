/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

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
	        System.out.println("TEST 1:");
	        System.out.println(RunLengthEncoder.decode(RunLengthEncoder.encode(Arrays.asList((byte)3, (byte)3, (byte)3, (byte)3, (byte)3, (byte)2, (byte)1, (byte)2, (byte)2, (byte)2))));
	        System.out.println(s);
	        System.out.println(toCompare);
	        

	        List<Player> players = new ArrayList<>();
	        players.add(new Player(PlayerID.PLAYER_1, 3, new SubCell(1,1),null));
            players.add(new Player(PlayerID.PLAYER_2, 3, new SubCell(1,1),null));
            players.add(new Player(PlayerID.PLAYER_3, 3, new SubCell(1,1),null));
            players.add(new Player(PlayerID.PLAYER_4, 3, new SubCell(1,1),null));
//	        List<Player> sortPlayers = XBlastComponent.sortPlayers(players, PlayerID.PLAYER_3);
	        System.out.println("hey");
	}

}
