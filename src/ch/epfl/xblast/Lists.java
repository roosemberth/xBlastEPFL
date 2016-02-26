package ch.epfl.xblast;

import java.util.Collections;
import java.util.List;

public final class Lists {
    private Lists(){}
    
    <T> List<T> mirrored(List<T> l){
        if(l.isEmpty()) throw new IllegalArgumentException("The list you tryed to mirror is empty");
        List<T> mirrored = l.subList(0, l.size());
        Collections.reverse(mirrored);
        return mirrored;
    }
}
