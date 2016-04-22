package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RunLengthEncoder {
	private RunLengthEncoder(){}
	
	public List<Byte> encode(List<Byte> plain){
		List<Byte> encoded = new ArrayList<>();
		
		Iterator<Byte> it = plain.iterator();
		Byte lastByte = 0;
		int numRepetitions = 0;
		while(it.hasNext()){
			Byte curByte = it.next();
			if(curByte < 0) throw new IllegalArgumentException("Can't encode negative numbers");
			
			if(numRepetitions != 0){
				if(lastByte != curByte){
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
	
	/*public List<Byte> decode(List<Byte> encoded){
		
	}*/
}
