package ch.epfl.xblast.server;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Images.BlockImage;

public final class BoardPainter {
    
    private static final Map<Block,BlockImage> blockToImage = new HashMap<>();
    private static final byte blockShadowed = (byte)BlockImage.IRON_FLOOR_S.ordinal();
 

    private BoardPainter(){
        // A FAIRE... REMPLIR MAP
    }
    
    public static byte byteForCell(Board b, Cell c){
      
       return isShadowed(b,c) ? blockShadowed : (byte)blockToImage.get(b.blockAt(c)).ordinal();
    }
    private static boolean isShadowed(Board b, Cell c){
        Block block = b.blockAt(c);
        if(block.isFree()){
            if(c.x() > 0){
               Block neighbor = b.blockAt(c.neighbor(Direction.W));
               if(neighbor.castsShadow())
                   return true;
            }
            return false;
        }
        return false;
    }
}