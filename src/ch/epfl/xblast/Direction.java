package ch.epfl.xblast;

public enum Direction {
    N,
    E,
    S,
    W;
    
    public Direction opposite(){
        return values()[(this.ordinal()+2)%4];
    }
    public boolean isHorizontal(){
       if( this == E || this == W){
           return true;
       }
       else{
           return false;
       }
    }
    public boolean isParallelTo(Direction that){
       if(this.ordinal() == that.ordinal() || this.ordinal() == (that.ordinal()+2)%4){
           return true;
       }
       return false;
    }
}
