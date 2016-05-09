package ch.epfl.xblast.server;

import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Images.BlockImage;

public final class BoardPainter {
    
    private final Map<Block,BlockImage> blockPalette;
    private final BlockImage blockShadowed;
 

    public BoardPainter(Map<Block,BlockImage> blockPalette, BlockImage blockShadowed){
        this.blockPalette = Objects.requireNonNull(blockPalette);
        this.blockShadowed = Objects.requireNonNull(blockShadowed);
    }
    
    public byte byteForCell(Board b, Cell c){  
       byte byte1 = isShadowed(b,c) ? (byte)blockShadowed.ordinal() : (byte)blockPalette.get(b.blockAt(c)).ordinal();
       return byte1;
    }
    private static boolean isShadowed(Board b, Cell c){
        Block block = b.blockAt(c);
        if(block.isFree()){
            if(c.x() > 0){
               Block neighbor = b.blockAt(c.neighbor(Direction.W));
               if(neighbor.castsShadow()){
                   return true;
               }
            }
            return false;
        }
        return false;
    }
    
    
}