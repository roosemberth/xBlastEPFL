package ch.epfl.xblast.server;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Images.BlockImage;

public final class BoardPainter {
    
    private final Map<Block,BlockImage> blockPalette;
    private final BlockImage blockShadowed;
 

    private BoardPainter(Map<Block,BlockImage> blockPalette, BlockImage blockShadowed){
        this.blockPalette = blockPalette;
        this.blockShadowed = blockShadowed;
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
    
    public static BoardPainter defaultBoardPainter(){
    	Map<Block,BlockImage> blockPalette = new HashMap<>();
    	for(Block b : Block.values()){
    		blockPalette.put(b, BlockImage.values()[b.ordinal() > 0 ? b.ordinal()+1 : b.ordinal()]);
    	}
    	BlockImage blockShadowed = BlockImage.IRON_FLOOR_S;
    	return new BoardPainter(blockPalette, blockShadowed);
    }
}