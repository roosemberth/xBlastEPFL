package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
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
        this.players = Objects.requireNonNull(players);
        if(this.players.size() != 4) throw new IllegalArgumentException("Unvalid number of players");
        this.bombs = Objects.requireNonNull(bombs);
        this.explosions = Objects.requireNonNull(explosions);
        this.blasts = Objects.requireNonNull(blasts);
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
    
    //How it works???
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
    //Rendre final tous les elements?
    /**
     * @return the game board
     */
    public Board board(){
        return board;
    }
    //Send copie??
    /**
     * @return the list of players
     */
    public List<Player> players(){
        return new ArrayList(new ArrayList(players));
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
        return alive;
    }
    //How it works?
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        return null;
    }
}
