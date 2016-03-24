package ch.epfl.xblast.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        RandomEventGenerator rand = new RandomEventGenerator(2016, 30,100);
        
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Block yy = null;
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        List<Player> lP = new ArrayList<Player>();
        
        //Initialize players
        lP.add(new Player(PlayerID.values()[0], 3,new Cell(1,1),2,3));
        lP.add(new Player(PlayerID.values()[1], 3,new Cell(13,1),2,3));
        lP.add(new Player(PlayerID.values()[2], 3,new Cell(13,11),2,3));
        lP.add(new Player(PlayerID.values()[3], 3,new Cell(1,11),2,3));
        
        GameState gs = new GameState(board, lP);

        while(!gs.isGameOver()){

            GameStatePrinter.printGameState(gs);
            gs = gs.next(rand.randomSpeedChangeEvents(),rand.randomBombDropEvents());
            try {
                System.in.read();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        GameStatePrinter.printGameState(gs);
        
    }
}
