package ch.epfl.xblast.server;

public final class ExplosionPainter {
    private ExplosionPainter(){}    
    
    public final static byte BYTE_FOR_EMPTY = (byte)16;
    
    public static byte byteForBomb(Bomb b){
        if(Integer.bitCount(b.fuseLength()) == 1){
            return (byte)1;
        }
        return (byte)0;
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
