/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class Main {
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
        XBlastComponent xc = new XBlastComponent();
        GameState gameState = new GameState(board, players);
        RandomEventGenerator reg = new RandomEventGenerator(2016,30,100);
        while(!gameState.isGameOver())
        {
            try {
                System.in.read();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            System.out.print("\u001b[2J\u001b[1;1H");
            gameState = gameState.next(reg.randomSpeedChangeEvents(), reg.randomBombDropEvents());
            GameStatePrinter.printGameState(gameState);            
        }
        
        
    }

}
