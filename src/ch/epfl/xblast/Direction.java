package ch.epfl.xblast;

public enum Direction {
    N,
    E,
    S,
    W;
    
    Direction opposite(){
        return values()[(this.ordinal()+2)%4];
    }
    boolean isHorizontal(){
       if( this == E || this == W){
           return true;
       }
       else{
           return false;
       }
    }
    boolean isParallelTo(Direction that){
       if(this.ordinal() == that.ordinal() || this.ordinal() == (that.ordinal()+2)%4){
           return true;
       }
       return false;
    }
}
