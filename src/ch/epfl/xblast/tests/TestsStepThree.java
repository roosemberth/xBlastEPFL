package ch.epfl.xblast.tests;

import static org.junit.Assert.*;

import java.lang.reflect.*;

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
import ch.epfl.xblast.server.Ticks;

public class TestsStepThree {
    
    @Test
    public void playerCheckStats(){
    	// Create a player with 2 lives
        Player p = new Player(PlayerID.PLAYER_1,2,new Cell(0,0),0,0);
        assertEquals(p.bombRange(), 0,0);
        assertEquals(p.maxBombs(), 0,0) ;
        assertTrue(p.isAlive());

        try {
			Field lifeStates = Player.class.getDeclaredField("lifeStates");
			lifeStates.setAccessible(true);

			Sq <LifeState> l;

			// Cycle life state. Lives -> 1
			lifeStates.set(p, p.statesForNextLife());

			assertTrue(p.isAlive());
			for (int i=0; i<Ticks.PLAYER_DYING_TICKS; ++i){
				assertEquals(Player.LifeState.State.DYING, p.lifeState().state());
				lifeStates.set(p, p.lifeStates().tail());
			}
			for (int i=0; i<Ticks.PLAYER_INVULNERABLE_TICKS; ++i){
				assertEquals(Player.LifeState.State.INVULNERABLE, p.lifeState().state());
				lifeStates.set(p, p.lifeStates().tail());
			}
			assertEquals(Player.LifeState.State.VULNERABLE, p.lifeState().state());

			// Cycle life state. Lives -> 0
			lifeStates.set(p, p.statesForNextLife());
			
			for (int i=0; i<Ticks.PLAYER_DYING_TICKS; ++i){
				assertEquals(Player.LifeState.State.DYING, p.lifeState().state());
				lifeStates.set(p, p.lifeStates().tail());
			}
			assertEquals(Player.LifeState.State.DEAD, p.lifeState().state());
			lifeStates.set(p, p.lifeStates().tail());
			
		} catch (ReflectiveOperationException e) {
			System.out.println("Reflective check failed: " + e.getMessage());
			throw new RuntimeException(e);
		}
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
