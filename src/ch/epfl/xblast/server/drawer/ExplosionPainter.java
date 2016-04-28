package ch.epfl.xblast.server.drawer;

import ch.epfl.xblast.server.Bomb;

public final class ExplosionPainter {
    public static final byte BYTE_FOR_BOMB_B = 20;
    public static final byte BYTE_FOR_BOMB_W = 21;
    
    public static final byte BYTE_FOR_EMPTY = 16;
    
    private ExplosionPainter(){}
    
    public static byte byteForBomb(Bomb b){
        return (Integer.bitCount(b.fuseLength())==1)?BYTE_FOR_BOMB_W:BYTE_FOR_BOMB_B;
    }

    public static byte byteForBlast(boolean N, boolean E, boolean S, boolean W){
        return (byte) ((N?1:0) << 3 +
                       (E?1:0) << 2 +
                       (S?1:0) << 1 +
                       (W?1:0) << 0);
    }
}
