package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {
    private Lists(){}
    
    public static <T> List<T> mirrored(List<T> l){
        if(l.isEmpty()) throw new IllegalArgumentException(
                "The list you tryed to mirror is empty");
        List<T> mirrored = new ArrayList(l.subList(0, l.size()));
        Collections.reverse(mirrored);
        List<T> result = new ArrayList(l.subList(0, l.size()-1));
        result.addAll(mirrored);
        return result;
    }
    
}
