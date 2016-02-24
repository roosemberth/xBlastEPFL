package ch.epfl.xblast;

import java.nio.file.DirectoryIteratorException;

public class Test {

    public static void main(String[] args) {
        Direction d = Direction.N;
        System.out.println(d.isParallelTo(Direction.E));
        System.out.println(Cell.SPIRAL_ORDER);
    }

}
