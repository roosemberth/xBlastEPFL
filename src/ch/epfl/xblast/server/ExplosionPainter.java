/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

/**
 * ExplosionPainter
 *
 * "Paints" exploded {@link cell}s.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class ExplosionPainter {
    /**
     * Private Constructor
     * This class is not instanciable.
     */
    private ExplosionPainter(){}

    public final static byte BYTE_FOR_EMPTY = (byte)16;

    /**
     * @return the byte corresponding to the specified {@link Bomb}.
     */
    public static byte byteForBomb(Bomb b){
        if(Integer.bitCount(b.fuseLength()) == 1){
            return (byte)21;
        }
        return (byte)20;
    }

    /**
     * @return the byte corresponding to the described explosion.
     * @param n whether it's northern neighbor cell is also exploded.
     * @param e whether it's eastern neighbor cell is also exploded.
     * @param w whether it's western neighbor cell is also exploded.
     * @param s whether it's southern neighbor cell is also exploded.
     */
    public static byte byteForBlast(boolean n, boolean e, boolean s, boolean w){
        byte maskNorth = n ? (byte)8 : (byte)0; 
        byte maskEast  = e ? (byte)4 : (byte)0; 
        byte maskSouth = s ? (byte)2 : (byte)0; 
        byte maskWest  = w ? (byte)1 : (byte)0; 

        byte result = (byte) (maskNorth | maskEast | maskSouth | maskWest);

        return result;
    }
}
