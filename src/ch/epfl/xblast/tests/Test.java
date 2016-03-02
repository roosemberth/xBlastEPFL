package ch.epfl.xblast.tests;

import java.util.Arrays;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class Test {

    public static void main(String[] args) {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        for(int y = 0; y < Cell.ROWS; y++){
            for(int x = 0; x < Cell.COLUMNS; x++){
                System.out.print(board.blockAt(new Cell(x,y)).ordinal() + " ");
            }
            System.out.println();
        }
    }

}
