package ch.epfl.xblast.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class TestsStepTwo {


    Block __ = Block.FREE;
    Block XX = Block.INDESTRUCTIBLE_WALL;
    Block xx = Block.DESTRUCTIBLE_WALL;
    Block yy = null;
    
    @Test
    public void checkListMirrored(){
        Random r = new Random();
        List <Integer> n = new ArrayList();
        for(int i = 0; i < 30; i++){
            n.add(r.nextInt(4000));
        }
        //Check the number of elemenents is odd
        List mirrored = Lists.mirrored(n);
        assertEquals(mirrored.size()%2, 1, 0);
        for(int i = 0; i < mirrored.size()/2;i++){
            assertEquals(mirrored.get(i), mirrored.get(mirrored.size()-1-i));
        }
    }
   
    @Test
   public void checkContentMirrored(){
       Board board = Board.ofQuadrantNWBlocksWalled(
               Arrays.asList(
                       Arrays.asList(__, __, __, __, __, xx, __),
                       Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                       Arrays.asList(__, xx, __, __, __, xx, __),
                       Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                       Arrays.asList(__, xx, __, xx, __, __, __),
                       Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
       
       for(int i = 0; i < Cell.COUNT/2;i++){
           assertEquals(board.blockAt(Cell.ROW_MAJOR_ORDER.get(i)), board.blockAt(Cell.ROW_MAJOR_ORDER.get(Cell.COUNT-1-i)));
       }
   }
  
    @Test
    public void checkContentNotNull(){
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        for(int i = 0; i < Cell.COUNT;i++){
            assertNotNull(board.blockAt(Cell.ROW_MAJOR_ORDER.get(i)));
        }
    } 
    
    @Test
    public void checkContentWalls(){
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        //Check first row
        for(int i = 0; i < Cell.COLUMNS;i++){
            assertEquals(board.blockAt(Cell.ROW_MAJOR_ORDER.get(i)), Block.INDESTRUCTIBLE_WALL);
        }
        //Check first and last element each row
        for(int i = 1; i < Cell.ROWS-1; i++){
            assertEquals(board.blockAt(Cell.ROW_MAJOR_ORDER.get(i*Cell.COLUMNS)), Block.INDESTRUCTIBLE_WALL);
            assertEquals(board.blockAt(Cell.ROW_MAJOR_ORDER.get((i+1)*Cell.COLUMNS-1)), Block.INDESTRUCTIBLE_WALL);
        }
        //Check last row
        for(int i = 0; i < Cell.COLUMNS;i++){
            assertEquals(board.blockAt(Cell.ROW_MAJOR_ORDER.get(Cell.COUNT-Cell.COLUMNS+i)), Block.INDESTRUCTIBLE_WALL);
        }
    } 
    
   @Test(expected = NullPointerException.class)
   public void checkNullList(){
       Board b = new Board(null);
   }
   @Test
   public void checkSameContent(){
       Board board1 = Board.ofQuadrantNWBlocksWalled(
               Arrays.asList(
                       Arrays.asList(__, __, __, __, __, xx, __),
                       Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                       Arrays.asList(__, xx, __, __, __, xx, __),
                       Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                       Arrays.asList(__, xx, __, xx, __, __, __),
                       Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
       Board board2 = Board.ofRows(
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
       for(int i = 0; i < Cell.COUNT;i++){
           assertEquals(board1.blockAt(Cell.ROW_MAJOR_ORDER.get(i)), board2.blockAt(Cell.ROW_MAJOR_ORDER.get(i)));
       }
   }
   @Test(expected = NullPointerException.class)
   public void checkLookContentNull(){
       Board board = Board.ofQuadrantNWBlocksWalled(
               Arrays.asList(
                       Arrays.asList(__, __, __, __, __, xx, __),
                       Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                       Arrays.asList(__, xx, __, __, __, xx, __),
                       Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                       Arrays.asList(__, xx, __, xx, __, __, __),
                       Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
       board.blockAt(null);
   }
   
   @Test(expected = NullPointerException.class)
   public void checkLookContentOutside(){
       Board board = Board.ofQuadrantNWBlocksWalled(
               Arrays.asList(
                       Arrays.asList(__, __, __, __, __, xx, __),
                       Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                       Arrays.asList(__, xx, __, __, __, xx, __),
                       Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                       Arrays.asList(__, xx, __, xx, __, __, __),
                       Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
       board.blocksAt(null);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void checkWrongNumColumns(){
       Board board = Board.ofQuadrantNWBlocksWalled(
               Arrays.asList(
                       Arrays.asList(__, __, __, __, __, xx),
                       Arrays.asList(__, XX, xx, XX, xx, XX),
                       Arrays.asList(__, xx, __, __, __, xx),
                       Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                       Arrays.asList(__, xx, __, xx, __, __, __),
                       Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
   }
   
   @Test(expected = NullPointerException.class)
   public void checkNullContent(){
       Board board = Board.ofQuadrantNWBlocksWalled(
               Arrays.asList(
                       Arrays.asList(__, __, __, __, __, yy, __),
                       Arrays.asList(__, XX, xx, XX, xx, yy, xx),
                       Arrays.asList(__, xx, __, __, __, xx, __),
                       Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                       Arrays.asList(__, xx, __, xx, __, __, __),
                       Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
   }
 
   
}
