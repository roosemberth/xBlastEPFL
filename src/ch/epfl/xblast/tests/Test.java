package ch.epfl.xblast.tests;

import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Lists;

public class Test {

    public static void main(String[] args) {
        List<List<Integer>> l = Lists.permutations(Arrays.asList(1,2,3,4,5));
        System.out.println(l.size() + " elements:" + l);
    }

}
