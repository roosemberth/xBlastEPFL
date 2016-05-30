/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {
    private PlayerPainter(){}
    
    private static final int stepsId[] = {0,1,0,2};
    
    private static final byte BYTE_FOR_DEAD = 15;
    
    public static byte byteForPlayer(int tick, Player p){ 
        LifeState ls = p.lifeState();
        SubCell c = p.position();
        
        int playerID = 20*(ls.state().equals(State.INVULNERABLE) && (tick % 2) == 1 ? 4 : p.id().ordinal());
        
        if(ls.canMove()){
            int steps = 0;
            
            if(p.direction().isParallelTo(Direction.W)){
                steps = stepsId[c.x()%4];
            }
            else{
                steps = stepsId[c.y()%4];
            }
            return (byte)(playerID + p.direction().ordinal() * 3 + steps);
        }
        else if(ls.state() == State.DYING){
            if(ls.lives() > 1){
                return (byte)(playerID + 12);
            }
            else{
                return (byte)(playerID + 13);
            }
        }
        return (byte)(playerID + BYTE_FOR_DEAD);
    }
}
