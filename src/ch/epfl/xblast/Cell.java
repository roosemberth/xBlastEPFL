package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cell {
    public static final int COLUMNS = 15;
    public static final int ROWS = 13;
    public static final int COUNT = COLUMNS * ROWS;
    
    public static final List<Cell> SPIRAL_ORDER =
            Collections.unmodifiableList(spiralOrder());
    public static final List<Cell> ROW_MAJOR_ORDER =
            Collections.unmodifiableList(rowMajorOrder());

    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> ret = new ArrayList<>();

        // XXXXXXXXXX
        // ----------
        // XXXXXXXXXX
        for (int j = 0; j < ROWS; ++j) {
            // XXXXXXXXXX
            // XXXX-XXXXX
            // XXXXXXXXXX
            for (int i = 0; i < COLUMNS; ++i) {
                ret.add(new Cell(i,j));
            }
        }
        return ret;
    }
    private static ArrayList<Cell> spiralOrder() {
        return rowMajorOrder();
    }
    
    private final int x, y;
    
    private static int normalizeX(int x){
        return Math.floorMod(x, COLUMNS);
    }
    private static int normalizeY(int y){
        return Math.floorMod(y, ROWS);
    }
    
    public Cell(int x, int y) {
        this.x = normalizeX(x);
        this.y = normalizeY(y);
    }
    public static int rowMajorIndexAtPos(int x, int y){
        return x + y*COLUMNS;
    }

    public int x(){
        return x;
    }
    public int y(){
        return y;
    }
    public int rowMajorIndex(){
        return rowMajorIndexAtPos(x, y);
    }
    public Cell neighbor(Direction dir){
        int xOffset = 0;
        int yOffset = 0;
        // We could do something faster with knowledge on the ordinal position
        switch (dir){
        case E:
            xOffset = +1;
            yOffset = +0;
            break;
        case N:
            xOffset = +0;
            yOffset = -1;
            break;
        case S:
            xOffset = +0;
            yOffset = +1;
            break;
        case W:
            xOffset = -1;
            yOffset = +0;
            break;
        }
        // Get the "safe" position
        final int neighborCellPosX = normalizeX(x + xOffset);
        final int neighborCellPosY = normalizeY(y + yOffset);
        final int neighborCellIndex =
                rowMajorIndexAtPos(neighborCellPosX, neighborCellPosY);

        return ROW_MAJOR_ORDER.get(neighborCellIndex);
    }
    public boolean equals(Object that){
        if (that==null) return false;
        if (!(that instanceof Cell)) return false;
        Cell otherCell = (Cell) that;
        return (this.x == otherCell.x) && (this.y == otherCell.y);
    }
    public String toString(){
        return "Cell at position (" + x + "," + y + ").";
    }
}
