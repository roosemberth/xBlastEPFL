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
 * Cell
 *
 * Represents a Game Board Cell
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class Cell {
    public static final int COLUMNS = 15;
    public static final int ROWS = 13;
    public static final int COUNT = COLUMNS * ROWS;

    private final int x, y;

    public static final List<Cell> ROW_MAJOR_ORDER = 
            Collections.unmodifiableList(rowMajorOrder());
    public static final List<Cell> SPIRAL_ORDER = 
            Collections.unmodifiableList(spiralOrder());

    /**
     * @return List of {@link Cell}s in a row major order basis.
     */
    private static ArrayList<Cell> rowMajorOrder(){
        ArrayList<Cell> rowMajorOrder = new ArrayList<Cell>();
        for(int y = 0; y < ROWS; y++){
            for(int x = 0; x < COLUMNS; x++){
                rowMajorOrder.add(new Cell(x, y));
            }
        }
        return rowMajorOrder;
    }

    /**
     * @return List of {@link Cell}s in an spiral order. Clockwise from the upper Left corner.
     */
    private static ArrayList<Cell> spiralOrder(){

        ArrayList<Cell> spiral = new ArrayList<Cell>();
        ArrayList<Integer> ix = new ArrayList<Integer>();
        ArrayList<Integer> iy = new ArrayList<Integer>();

        for(int x = 0; x < COLUMNS; x++){
            ix.add(x);
        }
        for(int y = 0; y < ROWS; y++){
            iy.add(y);
        }

        boolean horizontal = true;

        while(!ix.isEmpty() && !iy.isEmpty()){
            ArrayList<Integer> i1;
            ArrayList<Integer> i2;
            if(horizontal){
                i1 = ix;
                i2 = iy;
            }
            else{
                i1 = iy;
                i2 = ix;
            }
            int c2 = i2.get(0);
            i2.remove(0);
            for(int c1 : i1){
                if(horizontal){
                    spiral.add(new Cell(c1,c2));
                }
                else{
                    spiral.add(new Cell(c2,c1));
                }
            }
            Collections.reverse(i1);
            horizontal = !horizontal;
        }

        return spiral;
    }

    /**
     * Constructs a new cell, storing position x and y (fits into board size)
     * @param x
     * @param y
     */
    public Cell(int x, int y){
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * @return the component x of the position
     */
    public int x(){
        return x;
    }

    /**
     * @return the component y of the position
     */
    public int y(){
        return y;
    }


    /**
     * @return the index of the cell, in row order
     */
    public int rowMajorIndex(){
        return x + COLUMNS*y;
    }


    /**
     * @return the neighbor Cell in the given Direction
     */
    public Cell neighbor(Direction dir){
        switch(dir){
        case N:
            return new Cell(x, y-1);
        case E:
            return new Cell(x+1, y);
        case S:
            return new Cell(x, y+1);
        case W:
            return new Cell(x-1, y);
        default:
            return null;
        }
    }

    /**
     * @return whether this object equals to the specified object.
     */
    public boolean equals(Object that){
        if(that == null){
            return false;
        }
        if (!(that instanceof Cell)) return false;

        if(((Cell)that).x== this.x && ((Cell)that).y == this.y)
            return true;
        else 
            return false;
    }

    /**
     * @return a {@link String} Representation of the current object.
     */
    public String toString(){
        return "("+String.format("%02d", x)+","+String.format("%02d", y)+")";
    }

    /**
     * @return a hash of this object.
     */
    public int hashCode(){
        return rowMajorIndex();
    }
}
