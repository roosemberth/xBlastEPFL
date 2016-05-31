/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Images.BlockImage;

/**
 * BoardPainter
 *
 * Paints each block with the associated palette.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class BoardPainter {
    private final Map<Block,BlockImage> blockPalette;
    private final BlockImage blockShadowed;

    /**
     * Constructor
     * @param blockPalette a map between blocks and block images
     * @param blockShadowed the image used for the shadowed blocks. @see Block.castsShadow().
     */
    public BoardPainter(Map<Block,BlockImage> blockPalette, BlockImage blockShadowed){
        this.blockPalette = Objects.requireNonNull(blockPalette);
        this.blockShadowed = Objects.requireNonNull(blockShadowed);
    }

    /**
     * @return the byte for the given cell on the given board.
     * @param b Game Board
     * @param c Specific cell
     */
    public byte byteForCell(Board b, Cell c){  
       byte byte1 = isShadowed(b,c) ? (byte)blockShadowed.ordinal() : (byte)blockPalette.get(b.blockAt(c)).ordinal();
       return byte1;
    }

    /**
     * @return whether the specified cell is shadowed on the given board.
     * @param b Game Board
     * @param c Specific cell
     */
    private static boolean isShadowed(Board b, Cell c){
        Block block = b.blockAt(c);
        if(block.isFree()){
            if(c.x() > 0){
               Block neighbor = b.blockAt(c.neighbor(Direction.W));
               return neighbor.castsShadow();
            }
            return false;
        }
        return false;
    }


}
