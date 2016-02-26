package ch.epfl.xblast.server;

import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;

public final class Board {
    private List<Sq<Block>> blocks;
    
    public Board(List<Sq<Block>> blocks){
        
    }
    public Board ofRows(List<List<Block>> rows){
        return null;
    }

    public Board ofInnerBlocksWalled(List<List<Block>> innerBlocks){
        return null;
    }   
    public Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks){
        return null;
    }
    
    public Sq<Block> blocksAt(Cell c){
        return null;
    }
    
    public Block blockAt(Cell c){
        return null;
    }
    private void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns){
        
    }
}
