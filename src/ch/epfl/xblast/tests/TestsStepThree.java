package ch.epfl.xblast.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

public class TestsStepThree {
    
    
    
    
    
  
    
    @Test
    public void playerCheckStats(){
        Player p = new Player(PlayerID.PLAYER_1,0,new Cell(0,0),0,0);
        assertFalse(p.isAlive());
        assertEquals(p.bombRange(), 0,0);
        assertEquals(p.maxBombs(), 0,0);
    }
    
    @Test
    public void playerCheckMove(){

        Player p = new Player(PlayerID.PLAYER_1,Sq.constant(new LifeState(0,LifeState.State.DYING)), 
                DirectedPosition.stopped(new DirectedPosition(new SubCell(0,0),Direction.E)),0,0);
        assertFalse(p.lifeState().canMove());
    }
    
    @Test(expected = NullPointerException.class)
    public void bombConstructorNull(){
        Bomb b = new Bomb(null,null,0,0);
    }
    @Test(expected = NullPointerException.class)
    public void playerConstructorNull(){
        Player p = new Player(null,0,null,0,0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void checkArgumentCheckerNegative(){
        ArgumentChecker.requireNonNegative(-1);
    }
    
    

}
