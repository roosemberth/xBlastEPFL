package ch.epfl.xblast.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;

public class Test {

    public static void main(String[] args) {

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
        for(int i = 0; i < 4; i++)
            lP.add(new Player(PlayerID.values()[i], 1,new Cell(1+(i%2 == 1 ? 12 : 0),1+(i>1 ? 10: 0)),10,5));
        GameState gs = new GameState(board, lP);
        GameStatePrinter.printGameState(gs);
        Set<PlayerID> newBombs = new HashSet<>();
        newBombs.add(PlayerID.PLAYER_1);
        System.out.println();
        for(int i = 0; i < 5; i++){
            gs = gs.next(null,newBombs);
            GameStatePrinter.printGameState(gs);
        }
        System.out.println();
//        Sq<Integer> s = Sq.iterate(0, a->a+1).limit(3);
//        System.out.println(s.head());
//        s = s.tail();
//        System.out.println(s.head());
//        s = s.tail();
//        System.out.println(s.head());
//        s = s.tail();
//        if(s.isEmpty()) System.out.println("hey");
    }

}
