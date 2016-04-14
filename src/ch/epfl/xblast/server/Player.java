package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class Player {
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs, bombRange;
    
    /**
     * Returns true if two objects are internally equal
     */
    @Override public boolean equals(Object obj) {
        if (!(obj instanceof Player))
            return false;
        Player p = (Player) obj;
        if (!id.equals(p.id))
            return false;
        if (!lifeStates.equals(p.lifeStates))
            return false;
        if (!directedPos.equals(p.directedPos))
            return false;
        if (maxBombs != p.maxBombs)
            return false;
        if (bombRange != p.bombRange)
            return false;
        return true;
    };
    
    /**
     * Returns a hash of the Object
     */
    @Override public int hashCode() {
        return 7*id.ordinal()+11*lifeStates.hashCode()+13*directedPos.hashCode()+17*maxBombs+19*bombRange;
    }
    
    /**
     * Construct player
     * @param Player ID
     * @param lifeStates: Player future states and lives
     * @param directedPos: Oriented position (where is and in which direction is looking at)
     * @param maxBombs: Maximum number of bombs the player can own simultaneously on the board
     * @param bombRange: Range of a bomb explosion
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange){
        this.id = Objects.requireNonNull(id);
        this.lifeStates = Objects.requireNonNull(lifeStates);
        this.directedPos = Objects.requireNonNull(directedPos);
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
    }
    
    /**
     * Construct player
     * @param Player ID
     * @param lives: Number of lives for player
     * @param position: position where the player is
     * @param maxBombs: Maximum number of bombs the player can own simultaneously on the board
     * @param bombRange: Range of a bomb explosion
     */
	public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) {
		this(id,getNewLife(lives), getStoppedPosition(position), maxBombs, bombRange);
	}
	
	private static Sq<LifeState> getNewLife(int lives){
	    if(lives != 0)
	        return Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, 
					new LifeState(lives, LifeState.State.INVULNERABLE))
				.concat(Sq.constant(
					new LifeState(lives, LifeState.State.VULNERABLE)));
	    else
	        return Sq.constant(new LifeState(0,LifeState.State.DEAD));
	}
	
	/**
	 * @param  target position
	 * @return Stopped Directed Position Sequence on a given Cell
	 */
	private static Sq<DirectedPosition> getStoppedPosition(Cell position){
		return DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S));
	}
    
	/**
	 * @return Player id
	 */
    public PlayerID id(){
        return id;
    }
    
    /**
     * @return A list containing the player's current and future states
     */
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    /**
     * @return the player's current state
     */
    public LifeState lifeState(){
        return lifeStates.head();
    }

    /**
     * Calculates the next lifestates.
     * this will include a sequence of dying ticks and
     *  if the player's in his last life it ends with an infinite dead state list. 
     *  else, it'll end with a new invulnerable + infinite vulnerable sequence
     * @return a sequence of the next LifeState's
     */
    public Sq<LifeState> statesForNextLife(){
        Sq <LifeState> l = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(),LifeState.State.DYING));
        if(lifeState().state() == LifeState.State.DYING || lifeState().state() == LifeState.State.DEAD){
            return lifeStates().tail();
        }
        else if(lifeState().lives() <= 1){
            return l.concat(Sq.constant(new LifeState(lives()-1,LifeState.State.DEAD)));
        }
        else{
            return l.concat(Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives()-1,LifeState.State.INVULNERABLE))
            		.concat(Sq.constant(new LifeState(lives()-1,LifeState.State.VULNERABLE))));
        }
    }
  
    /**
     * @return current lives remaining to the player
     */
    public int lives(){
        return lifeStates.head().lives();
    }
    
    /**
     * @return whether or not the player is alive
     */
    public boolean isAlive(){
        return lives() > 0 ? true : false;
    }
    
    /**
     * @return directed position list (where the player is and is looking at)
     */
    public Sq<DirectedPosition> directedPositions(){
        return directedPos;
    }
    
    /**
     * @return current position of the player
     */
    public SubCell position(){
        return directedPos.head().position();
    }
    
    /**
     * @return where the player is heading to
     */
    public Direction direction(){
        return directedPos.head().direction();
    }
    
    /**
     * @return Maximum number of bombs a player can have on the board simultaneously
     */
    public int maxBombs(){
        return maxBombs;
    }
    
    /**
     * @param newMaxBombs
     * @return a player with the specified number of maximum bombs
     */
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }
    
    /**
     * @return Range of an explosion causes by a bomb
     */
    public int bombRange(){
        return bombRange;
    }
    
    /**
     * @param newBombRange
     * @return a player whose bombs have the specified range
     */
    public Player withBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPos,maxBombs, newBombRange );
    }
    
    /**
     * @return a new bomb created by the player corresponding to his capabilities
     */
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }
    
    /**
     *	Represents a a position and a direction (and movement)
     */
    public static final class DirectedPosition{
        private final SubCell position;
        private final Direction direction;
        
        /**
         * @return a stopped directed position of a player standing still and facing dp
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition dp){
            return Sq.constant(dp);
        }
        
        /**
         * @return a moving directedPosition of a player at location moving to dp
         */
        public static Sq<DirectedPosition> moving(DirectedPosition dp){
            return Sq.iterate(dp, x -> x.withPosition(x.position.neighbor(x.direction)));
        }
        
        /**
         * @param position
         * @param direction
         */
        public DirectedPosition(SubCell position, Direction direction){
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }
        
        /**
         * @return current position
         */
        public SubCell position(){
            return position;
        }
        
        /**
         * @param newPosition
         * @return a new directedPosition in a newPosition
         */
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition(newPosition, direction);
        }
        
        /**
         * @return current direction
         */
        public Direction direction(){
            return direction;
        }
        
        /**
         * @param newDirection
         * @return a new directedPosition towards newDirection
         */
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition(position, newDirection);
        }
    }
    
    /**
     * Represents number of lives left and Current state 
     */
    public static final class LifeState{
        private final int numLives;
        private final State state;
        

        /**
         * @param lives
         * @param state
         */
        public LifeState(int lives, State state){
             this.numLives = ArgumentChecker.requireNonNegative(lives);
             this.state =  Objects.requireNonNull(state);
         }
        
        /**
         * @return number of lives
         */
        public int lives(){
            return numLives;
        }
        
        /**
         * @return current state
         */
        public State state(){
            return state;
        }
      
        /**
         * @return true if either INVULNERABLE or VULNERABLE
         */
        public boolean canMove(){
            if(state == State.INVULNERABLE || state == State.VULNERABLE) return true;
            else return false;
        }
        
        /**
         * Possibles states in life 
         */
        public enum State{
            INVULNERABLE,
            VULNERABLE,
            DYING,
            DEAD,
            ;
            @Override public String toString(){
                return String.format("%12s", super.toString());
            }
        }
    }
}
