package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * @author pedro
 *
 */
/**
 * @author pedro
 *
 */
/**
 * @author pedro
 *
 */
public final class GameState {
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions; 
    private final List<Sq<Cell>> blasts;
    
    //Copie de securite?
    /**
     * Constructs a new Player, using the given vaues
     * @param ticks
     * @param board
     * @param players
     * @param bombs
     * @param explosions
     * @param blasts
     */
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs,
                    List<Sq<Sq<Cell>>> explosions, List<Sq<Cell>> blasts){
        if(ticks < 0) throw new IllegalArgumentException("Can't start at negative ticks");
        this.ticks = ticks;
        this.board = Objects.requireNonNull(board);
        this.players = Collections.unmodifiableList(new ArrayList(players));
        if(this.players.size() != 4) throw new IllegalArgumentException("Unvalid number of players");
        this.bombs = Collections.unmodifiableList(new ArrayList(bombs));
        this.explosions = Collections.unmodifiableList(new ArrayList(explosions));
        this.blasts = Collections.unmodifiableList(new ArrayList(blasts));
    }
    
    /**
     * Constructs a new Player, using the default values
     * @param board
     * @param players
     */
    public GameState(Board board, List<Player> players){
        this(0, board, players, new ArrayList<Bomb>(),new ArrayList<Sq<Sq<Cell>>>(), new ArrayList <Sq<Cell>>());
    }
    
    public int ticks(){
        return ticks;
    }
    
    /**
     * @return whether or not the game is over
     */
    public boolean isGameOver(){
        if(ticks == Ticks.TOTAL_TICKS)
            return true;
        int numPlayers = 0;
        for(Player p : players){
            if(p.isAlive())
                numPlayers++;
        }
        return numPlayers <= 1;
    }
 
    /**
     * @return remainingTime in seconds
     */
    public double remainingTime(){
        return (double)(Ticks.TOTAL_TICKS-ticks)/Ticks.TICKS_PER_SECOND;
    }
  
    /**
     * @return winner, if he exists
     */
    public Optional<PlayerID> winner(){
        List<Player> a = alivePlayers();
        if(a.size() == 1){
            return Optional.of(a.get(0).id());
        }
        else return Optional.empty();
    }

    /**
     * @return the game board
     */
    public Board board(){
        return board;
    }

    /**
     * @return the list of players
     */
    public List<Player> players(){
        return Collections.unmodifiableList(((new ArrayList(players))));
    }
    
    /**
     * @return the list of currently alivePlayers
     */
    public List<Player> alivePlayers(){
        List<Player> alive = new ArrayList<>();
        for(Player p : players){
            if(p.isAlive())
                alive.add(p);
        }
        return Collections.unmodifiableList(alive);
    }
    
    public Map<Cell, Bomb> bombedCells(){
        return null;
    }
    
    public Set<Cell> blastedCells(){
        return null;
    }
    
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents){
        return null;
    }
    
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> l = new ArrayList<>();
        for(Sq<Cell> bl : blasts0){
            Sq<Cell> t = bl.tail();
            if(!bl.isEmpty() && board0.blockAt(bl.head()).isFree())
                l.add(t);
        }
        for(Sq<Sq<Cell>> bl : explosions0){
            Sq<Cell> b = bl.head();
            //Probaby unecessary as the bomb can't be placed in a non-free position
            if(!b.isEmpty() && board0.blockAt(b.head()).isFree())
                l.add(b);
        }
        return Collections.unmodifiableList(l);
    }
    
    
}
