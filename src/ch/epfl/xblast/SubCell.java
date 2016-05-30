/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast;

/**
 * Represents a subcell of the game. Each subcell corresponds to a movement (and therefore minimal distance) unit on the game.
 */
public final class SubCell {
    private static final int COLUMNS = 16;
    private static final int ROWS = 16;
    private static final int BOARD_WIDTH = COLUMNS*Cell.COLUMNS;
    private static final int BOARD_HEIGHT = ROWS*Cell.ROWS;
    
    private final int x, y;
    
    /**
     * Creates a subcell at a given position
     * @param x
     * @param y
     */
    public SubCell(int x, int y){
        this.x = Math.floorMod(x, BOARD_WIDTH);
        this.y = Math.floorMod(y, BOARD_HEIGHT);
    }
    
    /**
     * @param cell
     * @return the central subcell corresponding to the specified cell
     */
    public static SubCell centralSubCellOf(Cell cell){
        return new SubCell(cell.x()*COLUMNS+COLUMNS/2, cell.y()*ROWS+ROWS/2);
    }  
    
    /**
     * @return the Manhathan distance to the central subcell
     */
    public int distanceToCentral(){
        return Math.abs((x%COLUMNS)-(COLUMNS/2)) + Math.abs((y%COLUMNS)-(ROWS/2));
    }
    
    public int x(){
        return x;
    }
    public int y(){
        return y;
    }
    /**
     * @return is this subcell is central
     */
    public boolean isCentral(){
        if((x%COLUMNS) == (COLUMNS/2) && (y%COLUMNS) == (COLUMNS/2)){
            return true;
        }
        return false;
    }
    
    public SubCell neighbor(Direction dir){
        switch(dir){
        case N:
            return new SubCell(x, y-1);
        case E:
            return new SubCell(x+1, y);
        case S:
            return new SubCell(x, y+1);
        case W:
            return new SubCell(x-1, y);
        default:
            return null;
        }
    }
    
    /**
     * @return the cell to which this subcell bellongs to
     */
    public Cell containingCell(){
        return new Cell(x/COLUMNS,(y/ROWS));
    }
    
    public boolean equals(Object that){
        if(that == null){
            return false;
        }
        else if(that.getClass() != this.getClass()){
            return false;
        }
        else{
            if(((SubCell)that).x== this.x && ((SubCell)that).y == this.y)
                return true;
            else 
                return false;
        }
    }
    
    public String toString(){
        return "(" + String.format("%03d", x) + "," + String.format("%03d", y) + ")";
    }
    public int hashCode(){
        return x + BOARD_WIDTH*y;
    }
}
