package ch.epfl.xblast;

public final class SubCell {
    
    public static int COLUMNS = 16;
    public static int ROWS = 16;
    public static int BOARD_WIDTH = COLUMNS*Cell.COLUMNS;
    public static int BOARD_HEIGHT = ROWS*Cell.ROWS;
    
    private final int x, y;
    
    public SubCell(int x, int y){
        this.x = Math.floorMod(x, BOARD_WIDTH);
        this.y = Math.floorMod(y, BOARD_HEIGHT);
    }
    
    public static SubCell centralSubCellOf(Cell cell){
        return new SubCell(cell.x()*COLUMNS+COLUMNS/2, cell.y()*ROWS+ROWS/2);
    }  
    
    public int distanceToCentral(){
        return Math.abs((x%COLUMNS)-(COLUMNS/2)) + Math.abs((y%COLUMNS)-(COLUMNS/2));
    }
    
    public int x(){
        return x;
    }
    public int y(){
        return y;
    }
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
        return "(" + x + "," + y + ")";
    }
}
