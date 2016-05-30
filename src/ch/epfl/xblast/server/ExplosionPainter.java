/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

public final class ExplosionPainter {
    private ExplosionPainter(){}    
    
    public final static byte BYTE_FOR_EMPTY = (byte)16;
    
    public static byte byteForBomb(Bomb b){
        if(Integer.bitCount(b.fuseLength()) == 1){
            return (byte)21;
        }
        return (byte)20;
    }
    
    public static byte byteForBlast(boolean n, boolean e, boolean s, boolean w){
        byte maskNorth = n ? (byte)8 : (byte)0; 
        byte maskEast  = e ? (byte)4 : (byte)0; 
        byte maskSouth = s ? (byte)2 : (byte)0; 
        byte maskWest  = w ? (byte)1 : (byte)0; 
        
        byte result = (byte) (maskNorth | maskEast | maskSouth | maskWest);
        
        return result;
    }
}
