package ch.epfl.xblast.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

public class TestsStepOne {

    @Test
    public void checkDirectionParallel(){
        assertTrue(Direction.N.isParallelTo(Direction.S));
        assertTrue(Direction.S.isParallelTo(Direction.N));
        assertTrue(Direction.W.isParallelTo(Direction.E));
        assertTrue(Direction.E.isParallelTo(Direction.W));
        
        assertTrue(Direction.S.isParallelTo(Direction.S));
        assertTrue(Direction.N.isParallelTo(Direction.N));
        assertTrue(Direction.E.isParallelTo(Direction.E));
        assertTrue(Direction.W.isParallelTo(Direction.W));
        
        assertFalse(Direction.E.isParallelTo(Direction.S));
    }
    
    @Test
    public void checkDirectionOpposite(){
        assertTrue(Direction.N.opposite() == Direction.S);
        assertTrue(Direction.S.opposite() == Direction.N);
        assertTrue(Direction.W.opposite() == Direction.E);
        assertTrue(Direction.E.opposite() == Direction.W);
    }
    @Test
    public void checkNumberCells(){
        assertEquals(Cell.ROW_MAJOR_ORDER.size(), Cell.COUNT, 0);
        assertEquals(Cell.SPIRAL_ORDER.size(), Cell.COUNT, 0);
    }
}
