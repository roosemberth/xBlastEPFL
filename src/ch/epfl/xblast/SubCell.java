package ch.epfl.xblast;

public final class SubCell {
    private final int x;
    private final int y;

    private final static int MAX_COLUMNS = 240;
    private final static int MAX_ROWS = 208;
    
    private final static int COLUMNS = 16;
    private final static int ROWS = 16;
    
    private static final int centralCaseX = 8;
    private static final int centralCaseY = 8;

    private static int normalizeX(int x){
        return Math.floorMod(x, MAX_COLUMNS);
    }
    private static int normalizeY(int y){
        return Math.floorMod(y, MAX_ROWS);
    }

    public SubCell(int x, int y){
        this.x = normalizeX(x);
        this.y = normalizeY(y);
    }

    public static SubCell centralSubCellOf(Cell cell){
        int scX = COLUMNS*cell.x()+centralCaseX;
        int scY = ROWS   *cell.y()+centralCaseY;
        return new SubCell(scX, scY);
    }

    public int x(){
        return x;
    }
    public int y(){
        return y;
    }
    public int distanceToCentral(){
        int cX = x % COLUMNS;
        int cY = y % ROWS;
        return Math.abs(cX - centralCaseX) + Math.abs(cY - centralCaseY);
    }
    public boolean isCentral(){
        return distanceToCentral() == 0;
    }

    public SubCell neighbor(Direction d){
        int xOffset = 0;
        int yOffset = 0;
        // We could do something faster with knowledge on the ordinal position
        switch (d){
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
        final int neighborSubcellPosX = normalizeX(x + xOffset);
        final int neighborSubcellPosY = normalizeY(y + yOffset);
        // TODO: Return from static table
        return new SubCell(neighborSubcellPosX, neighborSubcellPosY);
    }
    public Cell containingCell(){
        // TODO: Return from static table
        return new Cell(Math.floorDiv(x, COLUMNS), Math.floorDiv(y, ROWS));
    }
    @Override
    public boolean equals(Object that){
        if (that != null) return false;
        if (!(that instanceof SubCell)) return false;
        SubCell otherSubcell = (SubCell) that;
        return (this.x == otherSubcell.x) && (this.y == otherSubcell.y);
    }
    @Override
    public String toString(){
        Cell containingCell = containingCell();
        return "Subcell at (" + x + "," + y + "). "
                + "On Cell (" + containingCell.x() + "," + containingCell.y() + ")"
                        + "->(" + x % COLUMNS + "," + y % ROWS + ")";
    }

}
