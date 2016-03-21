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
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

/**
 * This class represents the game state at a given game tick
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
     * Constructs a new Game state given Specified Conditions
     * @param ticks			Game elapsed ticks
     * @param board			Game Board
     * @param players		Game players
     * @param bombs			Boards currently placed on Board
     * @param explosions	Explosions currently taking place
     * @param blasts		Blasts (Explosion Fragments) Moving on the board
     */
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs,
                    List<Sq<Sq<Cell>>> explosions, List<Sq<Cell>> blasts){
        if(ticks < 0)
        	throw new IllegalArgumentException("Can't start at negative ticks");
        if (players.size() != 4) 
        	throw new IllegalArgumentException("Invalid number of players");

        this.ticks = ticks;
        this.board = Objects.requireNonNull(board);
        this.players = Collections.unmodifiableList(new ArrayList<Player>(players));
        this.bombs = Collections.unmodifiableList(new ArrayList<Bomb>(bombs));
        this.explosions = Collections.unmodifiableList(new ArrayList<Sq<Sq<Cell>>>(explosions));
        this.blasts = Collections.unmodifiableList(new ArrayList<Sq<Cell>>(blasts));
    }
    
    /**
     * Construct a Beginning-of-times Game state
     * @param board		Game Board
     * @param players	Game Pkayers
     */
    public GameState(Board board, List<Player> players){
        this(0, board, players, new ArrayList<Bomb>(),new ArrayList<Sq<Sq<Cell>>>(), new ArrayList <Sq<Cell>>());
    }
    
    /**
     * @return Current Game ticks
     */
    public int ticks(){
        return ticks;
    }
    
    /**
     * @return Whether or not the game is over
     */
    public boolean isGameOver(){
        if(ticks == Ticks.TOTAL_TICKS)
            return true;
        return alivePlayers().size() <= 1;
    }
 
    /**
     * @return Remaining Game time in seconds
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
     * @return The game board
     */
    public Board board(){
        return board;
    }

    /**
     * @return a list of players
     */
    public List<Player> players(){
        return Collections.unmodifiableList((players));
    }
    
    /**
     * @return the list of currently alivePlayers
     */
    public List<Player> alivePlayers(){
        List<Player> alivePlayers = new ArrayList<>(4);
        for(Player p : players){
            if(p.isAlive())
                alivePlayers.add(p);
        }
        return alivePlayers;
    }
    
    /**
     * @return	A Map containing Fused bombs and their positions (in no particular order)
     */
    public Map<Cell, Bomb> bombedCells(){
        Map<Cell,Bomb> bombedCells = new HashMap<>(bombs.size());
        for(Bomb b : bombs){
            bombedCells.put(b.position(), b);
        }
        return bombedCells;
    }
    
    /**
     * @return 	A ser containing currently Blasted cells
     */
    public Set<Cell> blastedCells(){
        Set<Cell> blastedCells = new HashSet<>(blasts.size());
        for(Sq<Cell> c : blasts){
            blastedCells.add(c.head());
        }
        return blastedCells;
    }
    
    /**
     * The next GameState based in current one
     * @param speedChangeEvents 	A Player-(Opt)Direction Map Containing Player Movements
     * @param bombDropEvents		Players that dropped a bomb
     * @return 	The Game state at the next Game tick
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents){
        //Find bonus cells containing player
        List<Player> orderedPlayers = new ArrayList<>(4);
        
        for(PlayerID id : playerPermutations.get(indexPermutation)){
            orderedPlayers.add(listToHash(players).get(id));
        }

        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);
        Set <Cell> blastedCells1 = new HashSet<>();
        
        for(Sq<Cell> c : blasts1){
            blastedCells1.add(c.head());
        }
        
        Set<Cell> consumedBonuses = new HashSet<>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        
        //check the bonus collisions
        for(Player p : orderedPlayers){
            Cell playerPos = p.position().containingCell();
            if(board.blockAt(playerPos).isBonus() && !consumedBonuses.contains(playerPos)){
                consumedBonuses.add(playerPos);
                playerBonuses.put(p.id(), board.blockAt(playerPos).associatedBonus());
            }
        }
        
        Board board1 = nextBoard(board,consumedBonuses,blastedCells1);
        
        List<Bomb> bombs1 = newlyDroppedBombs(orderedPlayers, bombDropEvents, bombs);     
        List<Sq<Sq<Cell>>>  explosions1 = nextExplosions(explosions);       
        
        for(Bomb b : bombs){
            if(b.fuseLengths().isEmpty() || blastedCells().contains(b.position()))
                explosions1.addAll(b.explosion());
            else{
                bombs1.add(new Bomb(b.ownerId(),b.position(),b.fuseLengths().tail(),b.range()));
            }
        }
        Set<Cell> bombedCells1 = new HashSet<>();
        for(Bomb b : bombs1){
            bombedCells1.add(b.position());
        }
        List<Player> newPlayers = nextPlayers(players, playerBonuses, bombedCells1, board1, blastedCells1, speedChangeEvents);
       
        //Update so it uses next permutation for conflict
        indexPermutation = (indexPermutation+1)%playerPermutations.size();
        
        return new GameState(ticks+1,board1,newPlayers, bombs1, explosions1, blasts1);
    }
    
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> nextBlasts = new ArrayList<>();
        for(Sq<Cell> blast : blasts0){
            Sq<Cell> t = blast.tail();
            if(!t.isEmpty() && board0.blockAt(blast.head()).isFree())
                nextBlasts.add(t);
        }
        for(Sq<Sq<Cell>> explosion : explosions0){
            if(explosion.isEmpty())
            	continue;
            Sq<Cell> b = explosion.head();
            if(!b.isEmpty())
                nextBlasts.add(b);
        }
        return nextBlasts;
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
        List<Player> players1 = new ArrayList<>();
          
        //Update players
        for(Player p : players0){
            Player newP = null;
            
            Sq<DirectedPosition> newDPos = p.directedPositions();
            Sq<LifeState> newLifestate = null;

            int maxBombs1 = p.maxBombs(), maxRange1 = p.bombRange();
            //Check changeEvents
            if(p.lifeState().canMove()){
                newDPos = nextSqDPos(p, speedChangeEvents);
                //Check collisions and update position
                //Collision with blocks
                newDPos = nextSqCollision(newDPos,bombedCells1,board1,blastedCells1);
                
                newLifestate = nextSqLifestate(p, newDPos,blastedCells1);

                newP = new Player(p.id(), newLifestate ,newDPos,maxBombs1, maxRange1);
                //Check bonuses
                if(playerBonuses.containsKey(p.id())){
                    newP = playerBonuses.get(p.id()).applyTo(newP);
                }
            }
            else{
                newP = new Player(p.id(),p.lifeStates().tail(),p.directedPositions(),p.maxBombs(),p.bombRange());
            }
            players1.add(newP);
        }
        
        return players1;
    }
    private static Sq<DirectedPosition> nextSqDPos(Player p, Map<PlayerID, Optional<Direction>> speedChangeEvents){
        Optional<Direction> speedChange = speedChangeEvents.get(p.id());
        
        if(speedChange != null && speedChange.isPresent()){
            if(!speedChange.get().isParallelTo(p.direction())){
                DirectedPosition centralPos = p.directedPositions().findFirst(n -> n.position().isCentral());
                return p.directedPositions().takeWhile(c -> !c.equals(centralPos)).
                        concat(Player.DirectedPosition.moving(new DirectedPosition( centralPos.position(), speedChange.get())));
            }
            else{
                return DirectedPosition.moving(new DirectedPosition(p.position(), speedChange.get()));
            }
        }
        else{
            return p.directedPositions();
        }
    }
    
    private static Sq<DirectedPosition> nextSqCollision(Sq<DirectedPosition> newDPos, Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1){
        boolean collision = false;

        if(board1.blockAt(newDPos.head().position().containingCell().
                neighbor(newDPos.head().direction())).canHostPlayer()){
            //Collision with block
            if(newDPos.head().position().isCentral()){
                    return newDPos.tail();
            }
            else{
                if(bombedCells1.contains(newDPos.head().position().containingCell().
                        neighbor(newDPos.head().direction()))){
                    //Collision with bombs
                    if(newDPos.head().position().distanceToCentral() < 6){
                        return newDPos.tail();
                    }
                }
                else{
                    return newDPos.tail();
                }
            }
        }
        return newDPos;
    }
    
    private static Sq<LifeState> nextSqLifestate(Player p, Sq<DirectedPosition> newDPos, Set<Cell> blastedCells1){
      //Collision with particles
        if(blastedCells1.contains(newDPos.head().position().containingCell())){
            return p.statesForNextLife();
        }
        else{
            return p.lifeStates();
        }
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
        List<Bomb> newBombs = new ArrayList<>();
        
        for(Player p : players0){
        	if (p==null) continue;
            if(bombDropEvents.contains(p.id())){
                int numBombs = 0;
                //Find how many bombs player has active
                boolean occupied = false;
                for(Bomb b : bombs0){
                    if(b.ownerId() == p.id())
                        numBombs++;
                    // Why we can't drop a bomb in the current cell?
                    if(b.position().equals(p.position()))
                        occupied = true;
                }
                if(p.maxBombs() > numBombs && !occupied && p.isAlive())
                    newBombs.add(p.newBomb());
            }
        }
        
        return newBombs;
    }
    
    //Return HashMap containing <PlayerID, Player>
    /**
     * @param 	Players
     * @return 	A Map of PlayerIDs and Players
     */
    private static Map<PlayerID, Player> listToHash(List<Player> players){
        Map<PlayerID,Player> hashMap = new HashMap<>();
        for(Player p : players){
            hashMap.put(p.id(),p);
        }
        return hashMap;
    }
}
