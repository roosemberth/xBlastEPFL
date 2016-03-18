package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
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
    
    private static final List<List<PlayerID>> playerPermutations = Lists.permutations(Arrays.asList(PlayerID.values()));
    //or maybe just use ticks%numPermutations
    private static int indexPermutation = 0;
    
    private static final Random RANDOM = new Random(2016);
    
    private static final Block[] transformBlocks = {Block.BONUS_BOMB,Block.BONUS_RANGE,Block.FREE};
    
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
        Map<Cell,Bomb> bombedCells = new HashMap<>();
        for(Bomb b : bombs){
            bombedCells.put(b.position(), b);
        }
        return bombedCells;
    }
    
    public Set<Cell> blastedCells(){
        Set<Cell> blastedCells = new HashSet<>();
        List<Sq<Cell>> particles = nextBlasts(blasts, board, explosions);
        for(Sq<Cell> c : particles){
            blastedCells.add(c.head());
        }
        return blastedCells;
    }
    
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents){
        //Find bonus cells containing player
        Set<Cell> consumedBonuses = new HashSet<>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        for(PlayerID id : playerPermutations.get(indexPermutation)){
            Player p = listToHash(players).get(id);
            Cell playerPos = p.position().containingCell();
            if(board.blockAt(playerPos).isBonus() && !consumedBonuses.contains(playerPos)){
                consumedBonuses.add(playerPos);
                playerBonuses.put(id, board.blockAt(playerPos).associatedBonus());
            }
        }
        
        Board nextBoard = nextBoard(board,consumedBonuses,blastedCells());
        

        //Update so it uses next permutation for conflict
        indexPermutation = (indexPermutation+1)%playerPermutations.size();
        
        List<Player> newPlayers = nextPlayers(players, playerBonuses, null, nextBoard, blastedCells(), speedChangeEvents);
        List<Bomb> newBombs = newlyDroppedBombs(players, bombDropEvents, bombs);
        List<Sq<Sq<Cell>>>  nextExplosions = nextExplosions(explosions);
        
        for(Bomb b : bombs){
            if(b.fuseLengths().isEmpty())
                nextExplosions.addAll(b.explosion());
            else{
                newBombs.add(new Bomb(b.ownerId(),b.position(),b.fuseLengths().tail(),b.range()));
            }
        }
        
        
        
        
        List<Sq<Cell>> nextBlasts = nextBlasts(blasts, board, explosions);
        
        return new GameState(ticks+1,nextBoard,newPlayers, newBombs, nextExplosions, nextBlasts);
    }
    
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> l = new ArrayList<>();
        for(Sq<Cell> bl : blasts0){
            Sq<Cell> t = bl.tail();
            if(!t.isEmpty() && board0.blockAt(bl.head()).isFree())
                l.add(t);
        }
        for(Sq<Sq<Cell>> bl : explosions0){
            if(!bl.isEmpty()){
                Sq<Cell> b = bl.head();
                if(!b.isEmpty())
                    l.add(b);
            }
        }
        return l;
    }
    
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1){
        List<Sq<Block>> newBlocks = new ArrayList<>(); 
        for(int y = 0; y < Cell.ROWS; y++){
            for(int x = 0; x < Cell.COLUMNS; x++){
                Cell curPos = new Cell(x,y);
                if (consumedBonuses.contains(curPos)){
                    newBlocks.add(Sq.constant(Block.FREE));
                }
                else if(!blastedCells1.contains(curPos))
                    newBlocks.add(board0.blocksAt(curPos).tail());
                else{
                    if(board0.blockAt(curPos) == Block.DESTRUCTIBLE_WALL){
                        newBlocks.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,Block.CRUMBLING_WALL).concat(
                                Sq.constant(transformBlocks[RANDOM.nextInt(transformBlocks.length)])));
                    }
                    else if(board0.blockAt(curPos).isBonus())
                            newBlocks.add(board0.blocksAt(curPos).tail().limit(Ticks.BONUS_DISAPPEARING_TICKS).concat(Sq.constant(Block.FREE)));
                    else
                        newBlocks.add(board0.blocksAt(curPos).tail());
      
                }
            }
        }
        Board nextBoard = new Board(newBlocks);
        return nextBoard;
    }
    
    private static List<Player> nextPlayers(List<Player> players0, Map<PlayerID, Bonus> playerBonuses, 
            Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1, Map<PlayerID, Optional<Direction>> speedChangeEvents){
        return players0;
    }
    
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Sq<Cell>>> newList = new ArrayList<>();
        for(Sq<Sq<Cell>> c : explosions0){
            if(!c.isEmpty())
                newList.add(c.tail());
        }
        return newList;
    }
    
    //Check if needs to check player alive or list already has players alive
    private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents, List<Bomb> bombs0){
        Map<PlayerID, Player> playerHash = listToHash(players0);
        List<Bomb> newBombs = new ArrayList<>();
        for(PlayerID id : playerPermutations.get(indexPermutation)){
            if(bombDropEvents.contains(id)){
                Player owner = playerHash.get(id);
                int numBombs = 0;
                
                if(owner != null){
                    //Find how many bombs player has active
                    boolean occupied = false;
                    for(Bomb b : bombs0){
                        if(b.ownerId()==owner.id())
                            numBombs++;
                        if(b.position().equals(owner.position()))
                            occupied = true;
                    }
                    if(owner.maxBombs() > numBombs && !occupied)
                        newBombs.add(owner.newBomb());
                }
            }
        }
        return newBombs;
    }
    
    //Return HashMap containing <PlayerID, Player>
    private static Map<PlayerID, Player> listToHash(List<Player> players){
        Map<PlayerID,Player> hashMap = new HashMap<>();
        for(Player p : players){
            hashMap.put(p.id(),p);
        }
        return hashMap;
    }
    
    
}
