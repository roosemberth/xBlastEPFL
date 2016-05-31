/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * RunLengthEncoder
 *
 * Custom implementation of a RunLengthEncoder.
 * See {@link https://en.wikipedia.org/wiki/Run-length_encoding}
 * {@link https://web.archive.org/web/20160531225031/http://cs108.epfl.ch/p/08_state-serialization.html}
 *
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class RunLengthEncoder {
    /**
     * Private Constructor.
     * This class is not instanciable.
     */
    private RunLengthEncoder(){}

    /**
     * Encodes a byte stream using a custom version of a Run Length Encoder.
     * @see RunLengthEncoder#decode(List<Byte>);
     * @return encoded representation of the argument.
     */
    public static List<Byte> encode(List<Byte> plain){
        List<Byte> encoded = new ArrayList<>();

        Iterator<Byte> it = plain.iterator();
        Byte lastByte = 0;
        int numRepetitions = 0;
        while(it.hasNext()){
            Byte curByte = it.next();
            if(curByte < 0) throw new IllegalArgumentException("Can't encode negative numbers");

            if(numRepetitions != 0 ){
                if(lastByte != curByte){
                    addToList(numRepetitions, encoded, lastByte);
                    numRepetitions = 0;
                }
            }
            lastByte = curByte;
            numRepetitions++;
        }
        addToList(numRepetitions, encoded, lastByte);
        return encoded;
    }

    /**
     * Encodes n repetitions of byte b into the specified list.
     * @param nRepetitions  Number of repetitions of b.
     * @param b             Byte to be repeated.
     * @param list          List where to add the encoded repetition of b.
     */
    private static void addToList(int nRepetitions, List<Byte> list, byte b){
        while(nRepetitions > 0){
            switch(nRepetitions){
            case 2:
                list.add(b);
                nRepetitions--;
            case 1:
                list.add(b);
                nRepetitions--;
                break;
            default:
                if(nRepetitions <= 130)
                    list.add((byte)(-((nRepetitions)-2)));
                else
                    list.add((byte)-128);
                list.add(b);
                nRepetitions -= 130;
                break;
            }
        }
    }

    /**
     * Decodes a byte stream using a custom version of a Run Length Decoder.
     * @see RunLengthEncoder#encode(List<Byte>);
     * @return decoded representation of the argument.
     */
    public static List<Byte> decode(List<Byte> encoded){
        List<Byte> decoded = new ArrayList<>();
        Iterator<Byte> it = encoded.iterator();
        while(it.hasNext()){
            Byte curByte = it.next();
            int numToAdd = 1;
            //Check next element exists if negative or assume its correct?
            if(curByte<0){
                numToAdd = Math.abs(curByte)+2;
                curByte = it.next();
            }
            for(int i = 0; i < numToAdd; i++){
                decoded.add(curByte);
            }
        }
        return decoded;
    }
}
