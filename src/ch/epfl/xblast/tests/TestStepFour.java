package ch.epfl.xblast.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import ch.epfl.xblast.Lists;

public class TestStepFour {

    @Test
    public void testPermutations() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("permutations.txt"));
        List<List<Integer>> permutations = Lists.permutations(Arrays.asList(1,2,3,4,5,6,7,8));
        while(scan.hasNextLine()){
            List<Integer> newLine = new ArrayList<>();
            for(int i = 0; i < 8; i++){
                newLine.add(scan.nextInt());
            }
            assertTrue(permutations.contains(newLine));
        }
    }

}
