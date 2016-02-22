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
        default:
            return null;
        }
    }
    boolean isHorizontal(){
       if( this == E || this == W){
           return true;
       }
       else{
           return false;
       }
    }
}
