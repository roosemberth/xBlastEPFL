package ch.epfl.xblast;

public enum Direction {
    N,
    E,
    S,
    W;
    
    Direction opposite(){
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
    boolean isHorizontal(){
        return (this == E || this == W);
    }
    boolean isParallelTo(Direction that){
        return (this.isHorizontal()==that.isHorizontal());
    }
}
