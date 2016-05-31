/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast;

/**
 * Direction
 *
 * Represents a player chosen direction
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public enum Direction {
    N,
    E,
    S,
    W;

    /**
     * @return the opposite direction.
     */
    public Direction opposite(){
        return values()[(this.ordinal()+2)%4];
    }

    /**
     * @return whether this direction if horizontal or not.
     */
    public boolean isHorizontal(){
       if( this == E || this == W){
           return true;
       }
       else{
           return false;
       }
    }

    /**
     * @return whether this direction is parallel to the specified direction
     * @param that direction to copare to.
     */
    public boolean isParallelTo(Direction that){
       if(this.ordinal() == that.ordinal() || this.ordinal() == (that.ordinal()+2)%4){
           return true;
       }
       return false;
    }
}
