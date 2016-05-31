/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lists
 *
 * Provides tools for managing the Lists 
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class Lists {
    private Lists(){}

    /**
     * Returns the list mirrored, the 
     * last element in the original list not being repeated
     * ie. the sequence 3,2,1 will be converted to 3,2,1,2,3
     * @param  Source list
     * @return Mirrored list
     */
    public static <T> List<T> mirrored(List<T> l){
        if(l.isEmpty()) throw new IllegalArgumentException(
                "The list you tryed to mirror is empty");
        List<T> mirrored = new ArrayList<T>(l.subList(0, l.size()));
        Collections.reverse(mirrored);
        List<T> result = new ArrayList<T>(l.subList(0, l.size()-1));
        result.addAll(mirrored);
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns all posible list permutations
     * @param  Origin list
     * @return Permutations of the original list
     */
    public static <T> List<List<T>> permutations(List<T> l){
        List<List<T>> list = new ArrayList<>();
        if(l.size() == 0){
            list.add(new ArrayList<>());
            return Collections.unmodifiableList(list);
        }
        else if(l.size() < 2){
            list.add(l);
            return Collections.unmodifiableList(list);
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
        return Collections.unmodifiableList(list);
    }
}
