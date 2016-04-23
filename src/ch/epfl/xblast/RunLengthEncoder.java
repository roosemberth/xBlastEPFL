package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RunLengthEncoder {
	private RunLengthEncoder(){}
	
	public static List<Byte> encode(List<Byte> plain){
		List<Byte> encoded = new ArrayList<>();
		
		Iterator<Byte> it = plain.iterator();
		Byte lastByte = 0;
		int numRepetitions = 0;
		while(it.hasNext()){
			Byte curByte = it.next();
			if(curByte < 0) throw new IllegalArgumentException("Can't encode negative numbers");
			
			if(numRepetitions != 0 ){
				if(lastByte != curByte || !it.hasNext()){
					if(!it.hasNext()) 
						numRepetitions++;
					switch(numRepetitions){
						case 2:
							encoded.add(lastByte);
						case 1:
							encoded.add(lastByte);
							break;
						default:
							encoded.add((byte)(-(numRepetitions-2)));
							encoded.add(lastByte);
							break;
					}
					numRepetitions = 0;
				}
			}
			lastByte = curByte;
			numRepetitions++;
		}
		
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
