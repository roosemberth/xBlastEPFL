package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {
    private Lists(){}
    
    /**
     * returns the list mirrored, the 
     * last element in the original list not being repeated
     * @param l
     * @return
     */
    public static <T> List<T> mirrored(List<T> l){
        if(l.isEmpty()) throw new IllegalArgumentException(
                "The list you tryed to mirror is empty");
        List<T> mirrored = new ArrayList(l.subList(0, l.size()));
        Collections.reverse(mirrored);
        List<T> result = new ArrayList(l.subList(0, l.size()-1));
        result.addAll(mirrored);
        return result;
    }
    //TO DO:CHECK EMPTY LIST
    /**
     * returns all possible permutations
     * of a given list
     * @param l
     * @return
     */
    public static <T> List<List<T>> permutations(List<T> l){
        List<List<T>> list = new ArrayList<>();
                
        if(l.size() < 2){
            list.add(l);
            return list;
        }
        else{
            List<List<T>> possiblePermutations = permutations(l.subList(1, l.size()));
            for(List<T> ls : possiblePermutations){
                for(int i = 0; i < ls.size()+1; i++){
                    List<T> newList = new ArrayList(ls);
                    newList.add(i, l.get(0));
                    list.add(newList);
                }
            }
        }
        return list;
    }
}
