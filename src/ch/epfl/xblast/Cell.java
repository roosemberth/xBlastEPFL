package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public final class Cell {
    public static int COLUMNS = 15;
    public static int ROWS = 13;
    public static int COUNT = COLUMNS * ROWS;
    
    private final int x, y;
    
    public static final List<Cell> ROW_MAJOR_ORDER = 
            Collections.unmodifiableList(rowMajorOrder());
    public static final List<Cell> SPIRAL_ORDER = 
            Collections.unmodifiableList(spiralOrder());
    

    private static ArrayList<Cell> rowMajorOrder(){
        ArrayList<Cell> rowMajorOrder = new ArrayList<Cell>();
        for(int y = 0; y < ROWS; y++){
            for(int x = 0; x < COLUMNS; x++){
                rowMajorOrder.add(new Cell(x, y));
            }
        }
        return rowMajorOrder;
    }
    
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
        
        //Ma version
        /*
        int x = 0, y = 0;
        int minRow = 1, minCol = 0, maxRow = ROWS-1, maxCol = COLUMNS-1;
        
        int horizontalDirection = 1, verticalDirection = 1;

        for(int i = 0; i < ROWS; i++){
            while(x <= maxCol && x >= minCol) {
                spiralOrder.add(new Cell(x,y));
                x += horizontalDirection;
            }

            x -= horizontalDirection;
            y += verticalDirection;
            
            while(y <= maxRow && y >= minRow){
                spiralOrder.add(new Cell(x,y));
                y += verticalDirection;
            }
            

            y -= verticalDirection;
            x -= horizontalDirection;
            
            if(horizontalDirection > 0){
                maxCol--;
            }
            else{
                minCol++;
            }
            horizontalDirection *= -1;
            if(verticalDirection > 0){
                maxRow--;
            }
            else{
                minRow++;
            }
            verticalDirection *= -1;

        }*/
        return spiral;
    }
    public Cell(int x, int y){
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }
    public int x(){
        return x;
    }
    public int y(){
        return y;
    }
    
    public int rowMajorIndex(){
        return x + COLUMNS*y;
    }
    
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
    
    public String toString(){
        return "("+x+","+y+")";
    }
}
