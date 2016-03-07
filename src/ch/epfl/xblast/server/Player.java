package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class Player {
    private PlayerID id;
    private Sq<LifeState> lifeStates;
    private Sq<DirectedPosition> directedPos;
    private int maxBombs, bombRange;
    
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange){
        this.id = Objects.requireNonNull(id);
        this.lifeStates = Objects.requireNonNull(lifeStates);
        this.directedPos = Objects.requireNonNull(directedPos);
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
    }
    
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange){

        this.id = Objects.requireNonNull(id);
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
        Sq <LifeState> l = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, LifeState.State.INVULNERABLE))
                .concat(Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE)));
        this.lifeStates = Objects.requireNonNull(l);
        this.directedPos = Objects.requireNonNull(DirectedPosition.stopped(
                new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)));
    }
    
    public PlayerID id(){
        return id;
    }
    
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    public LifeState lifeState(){
        return lifeStates.head();
    }
//    TO DO
    public Sq<LifeState> statesForNextLife(){
//        Sq <LifeState> l = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(),LifeState.State.DYING));
//        if(l.head().lives() == 1)
        return null;
    }
//    
    public int lives(){
        return lifeStates.head().lives();
    }
    
    public boolean isAlive(){
        return lives() > 0 ? true : false;
    }
    
    public Sq<DirectedPosition> directedPositions(){
        return directedPos;
    }
    
    public SubCell position(){
        return directedPos.head().position();
    }
    
    public Direction direction(){
        return directedPos.head().direction();
    }
    
    public int maxBombs(){
        return maxBombs;
    }
    
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }
    
    public int bombRange(){
        return bombRange;
    }
    
    public Player withBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPos,maxBombs, newBombRange );
    }
    
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }
    
    public static final class DirectedPosition{
        private SubCell position;
        private Direction direction;
        
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            return Sq.iterate(p, x -> x.withPosition(x.position.neighbor(x.direction)));
        }
        
        public DirectedPosition(SubCell position, Direction direction){
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }
        
        public SubCell position(){
            return position;
        }
        
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition(newPosition, direction);
        }
        
        public Direction direction(){
            return direction;
        }
        
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition(position, newDirection);
        }
    }
    
    public static final class LifeState{
        private int numLives;
        private State state;
        

        public LifeState(int lives, State state){
             this.numLives = ArgumentChecker.requireNonNegative(lives);
             this.state =  Objects.requireNonNull(state);
         }
        
        public int lives(){
            return numLives;
        }
        
        public State state(){
            return state;
        }
      
        public boolean canMove(){
            if(state == State.INVULNERABLE || state == State.VULNERABLE) return true;
            else return false;
        }
        
        public enum State{
            INVULNERABLE,
            VULNERABLE,
            DYING,
            DEAD;
        }
    }
}
