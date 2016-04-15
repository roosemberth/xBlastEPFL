package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * Represents the current state of the game board, y compris les sequences d'interaction de chaque cell.
 */
public final class Board {
    private List<Sq<Block>> blocks;
    
    /**
     * Creates a Game Board from a List of Cell Sequences
     * @param blocks
     */
    public Board(List<Sq<Block>> blocks){
        if(blocks.size() != Cell.COUNT) 
            throw new IllegalArgumentException("Board passed has unvalid number of elements");
        this.blocks = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(blocks)));
    }
    
    /**
     * Creates a GameBoard from A list of cell rows
     * @param rows
     * @return
     */
    public static Board ofRows(List<List<Block>> rows){
        List<Sq<Block>> board = new ArrayList();
        if(rows.size() != Cell.ROWS) 
            throw new IllegalArgumentException("Wrong number of rows");
        for(List<Block> l : rows){
            if(l.size() != Cell.COLUMNS)  
                throw new IllegalArgumentException("Wrong number of columns for row");
            for(Block b : l){
                board.add(Sq.constant(Objects.requireNonNull(b)));
            }
        }
        return new Board(board);
    }
    
    /**
     * Creates a Walled Game board from a list of internal rows
     * @param innerBlocks
     * @return
     */
    public static  Board ofInnerBlocksWalled(List<List<Block>> innerBlocks){
        List<Sq<Block>> board = new ArrayList();
        board.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        if(innerBlocks.size() != (Cell.ROWS-2))  
            throw new IllegalArgumentException("Wrong number of rows");
        for(List<Block> l : innerBlocks){
            board.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
            if(l.size() != (Cell.COLUMNS-2))  
                throw new IllegalArgumentException("Wrong number of columns");
            for(Block b : l){
                board.add(Sq.constant(Objects.requireNonNull(b)));
            }
            board.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
        }
        board.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        return new Board(board);
    }   
    
    /**
     * Creates a Walled Game board from a list the first quadrant rows
     * @param quadrantNWBlocks
     * @return
     */
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks){
        List<List<Block>> board = new ArrayList();
        if(quadrantNWBlocks.size() != (int)Math.ceil((Cell.ROWS-2)/2.0)) 
            throw new IllegalArgumentException("Wrong number of rows");
        for(List<Block> l : quadrantNWBlocks){
            if(l.size() != (int)Math.ceil((Cell.COLUMNS-2)/2.0))  
                throw new IllegalArgumentException("Wrong number of columns");
            List<Block> temp = new ArrayList<>();
            for(Block b : l){
                temp.add(b);
            }
            board.add(Lists.mirrored(temp));
        }
        for(int i = board.size()-2; i >=0; i--){
            board.add(board.get(i));
        }
        return ofInnerBlocksWalled(board);
    }    
    
    /**
     * @param  Target Cell
     * @return Returns Sequence of Blocks at Target Cell
     */
    public Sq<Block> blocksAt(Cell c){
        return blocks.get(c.rowMajorIndex());  
    }
    
    /**
     * @param  Target Cell
     * @return Block at Target Cell
     */
    public Block blockAt(Cell c){
        return blocks.get(c.rowMajorIndex()).head();
    }
    
    /**
     * Checks that the rows of the same matrix block have the same size
     * @param matrix
     * @param rows
     * @param columns
     */
    @SuppressWarnings("unused")
    private void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns){
        if(matrix.size() != Cell.ROWS) 
            throw new IllegalArgumentException("Wrong number of rows");
        for(List<Block> l : matrix){
            if(l.size() != Cell.COUNT) 
                throw new IllegalArgumentException("Wrong number of columns for row");
        }
        
    }
    
}
