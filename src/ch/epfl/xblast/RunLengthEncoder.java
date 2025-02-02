package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RunLengthEncoder {
	private RunLengthEncoder(){}
	
	
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
