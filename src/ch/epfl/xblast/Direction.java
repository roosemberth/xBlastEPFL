package ch.epfl.xblast;

public enum Direction {
    N,
    E,
    S,
    W;
    
    public Direction opposite(){
        switch(this){
        case N:
            return S;
        case S:
            return N;
        case E:
            return W;
        case W: 
            return E;
        default: // impossible
            return null;
        }
    }
    public boolean isHorizontal(){
        return (this == E || this == W);
    }
    public boolean isParallelTo(Direction that){
        return (this.isHorizontal()==that.isHorizontal());
    }
}
