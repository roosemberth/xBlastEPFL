package ch.epfl.xblast.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import ch.epfl.xblast.Lists;

public class TestsStepTwo {

    @Test
    public void checkContent(){
        Random r = new Random();
        List <Integer> n = new ArrayList();
        for(int i = 0; i < 30; i++){
            n.add(r.nextInt(4000));
        }
        //Check the number of elemenents is odd
        List mirrored = Lists.mirrored(n);
        assertEquals(mirrored.size()%2, 1, 0);
        for(int i = 0; i < mirrored.size()/2;i++){
            assertEquals(mirrored.get(i+i), mirrored.get(mirrored.size()-1-i));
        }
    }
}
