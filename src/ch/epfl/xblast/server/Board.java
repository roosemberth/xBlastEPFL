package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

public final class Board {
    private List<Sq<Block>> blocks;
    
    public Board(List<Sq<Block>> blocks){
        if(blocks.size() != Cell.COUNT) throw new IllegalArgumentException("Board passed has unvalid number of elements");
        this.blocks = new ArrayList<>(blocks);
    }
    public static Board ofRows(List<List<Block>> rows){
        List<Sq<Block>> board = new ArrayList();
        if(rows.size() != Cell.ROWS)  throw new IllegalArgumentException("Wrong number of rows");
        for(List<Block> l : rows){
            if(l.size() != Cell.COUNT)  throw new IllegalArgumentException("Wrong number of columns for row");
            for(Block b : l){
                board.add(Sq.constant(b));
            }
        }
        return new Board(board);
    }

    public static  Board ofInnerBlocksWalled(List<List<Block>> innerBlocks){
        List<Sq<Block>> board = new ArrayList();
        board.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        if(innerBlocks.size() != (Cell.ROWS-2))  throw new IllegalArgumentException("Wrong number of rows");
        for(List<Block> l : innerBlocks){
            board.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
            if(l.size() != (Cell.COUNT-2))  throw new IllegalArgumentException("Wrong number of column");
            for(Block b : l){
                board.add(Sq.constant(b));
            }
            board.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
        }
        board.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        return new Board(board);
    }   
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks){
        List<Sq<Block>> board = new ArrayList();
        board.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        for(List<Block> l : quadrantNWBlocks){
            board.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
            List<Sq<Block>> temp = new ArrayList<>();
            for(Block b : l){
                temp.add(Sq.constant(b));
            }
            board.addAll(Lists.mirrored(temp));
            board.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
        }
        board = Lists.mirrored(board.subList(0, board.size()-Cell.COLUMNS/2));
        return new Board(board);
    }
    
    public Sq<Block> blocksAt(Cell c){
        return blocks.get(c.rowMajorIndex());
    }
    
    public Block blockAt(Cell c){
        return blocks.get(c.rowMajorIndex()).head();
    }
    private void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns){
        
    }
}
