package ch.epfl.xblast.tests;

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
        for(int i = 0; i < 4; i++)
            lP.add(new Player(PlayerID.values()[i], 5,new Cell(1+(i%2 == 1 ? 12 : 0),1+(i>1 ? 10: 0)),2,2));
        GameState gs = new GameState(board, lP);

        RandomEventGenerator rand = new RandomEventGenerator(2016, 30,100);
        
        while(!gs.isGameOver()){
            gs = gs.next(rand.randomSpeedChangeEvents(),rand.randomBombDropEvents());
            GameStatePrinter.printGameState(gs);
            Thread.sleep(50);
        }
        
    }
}
