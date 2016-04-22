package ch.epfl.xblast.server;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {
    private PlayerPainter(){}
    
    static final int stepsId[] = {0,1,0,2};
    
    public static byte byteForPlayer(int tick, Player p){ 
        //CHECK WHAT ELSE NEEDS TO BE ADDED
        LifeState ls = p.lifeState();
        Cell c = p.position().containingCell();
        
        int playerID = 20*(ls.state().equals(State.INVULNERABLE) && (tick % 2) == 1 ? 5 : p.id().ordinal());
        
        if(ls.canMove()){
            int steps = 0;
            
            if(p.direction().isParallelTo(Direction.W)){
                steps = stepsId[c.x()%4];
            }
            else{
                steps = stepsId[c.y()%4];
            }
            
            if(ls.state().equals(State.INVULNERABLE)){
                return (byte)(playerID + p.direction().ordinal() * 3 + steps);
            }
            else{
                return (byte)(20*p.id().ordinal() + p.direction().ordinal() * 3 + steps);
            }
        }
        else{
            if(ls.lives() > 0){
                return (byte)(playerID + 12);
            }
            else{
                return (byte)(playerID + 13);
            }
        }
    }
}
