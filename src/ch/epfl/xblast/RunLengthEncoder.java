package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunLengthEncoder {
    private RunLengthEncoder() { }
    
    public static List<Byte> encode(List<Byte> toEncode){
        if (toEncode.size()==0)
            throw new IllegalArgumentException("Can't encode a null-size stream!");
        // Discard useless cases
        if (toEncode.size()==1 || toEncode.size()==2)
            return new ArrayList<>(toEncode);

        List<Byte> ret = new ArrayList<>();
        Iterator<Byte> it = toEncode.iterator();

        Byte currByte = it.next();
        while (it.hasNext()){
            Byte lastByte = currByte;
            int byteMultiplicity = 1;
            while (it.hasNext()){
                currByte = it.next();
                if (!currByte.equals(lastByte))
                    break;
                byteMultiplicity++;
                currByte = null;
            } 
            while (byteMultiplicity>130){
                // Max Separation Encoding
                ret.add((byte) -128); ret.add(new Byte(lastByte)); byteMultiplicity -= 130;
            }
            if (byteMultiplicity < 3){
                for (int i = 0; i<byteMultiplicity; ++i)
                    ret.add(new Byte(lastByte));
            } else {
                byte encodedMultiplicity = (byte) -(byteMultiplicity-2);
                ret.add(encodedMultiplicity); ret.add(new Byte(lastByte));
            }
        }
        // Here currByte is still holding the last byte (unless null by sequence), add if to the queue.
        if (currByte!=null) ret.add(new Byte(currByte));
        return ret;
    }
    public static List<Byte> decode(List<Byte> encoded){
        List<Byte> ret = new ArrayList<>();
        Iterator<Byte> it = encoded.iterator();
        while (it.hasNext()){
            int currentByteMultiplicity = 1;
            byte currentByte = it.next();
            if (currentByte < 0){
                currentByteMultiplicity = Math.abs(currentByte) + 2;
                currentByte = it.next();
                check(currentByte>=0, "Two consecutive negative numbers");
            }
            for (int i = 0; i<currentByteMultiplicity; ++i)
                ret.add(new Byte(currentByte));
        }
        return ret;
    }
    
    private static void check(boolean condition, String msg){
        if (!condition) throw new IllegalArgumentException(msg);
    }
}
