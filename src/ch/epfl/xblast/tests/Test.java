package ch.epfl.xblast.tests;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

public class Test {

    public static void main(String[] args) {
        Direction d = Direction.N;
        System.out.println(d.isParallelTo(Direction.E));
        System.out.println(Cell.SPIRAL_ORDER);
    }

}
