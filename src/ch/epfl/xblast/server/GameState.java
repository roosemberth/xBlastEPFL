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
import ch.epfl.xblast.server.Player.LifeState.State;

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
    
    private static final Random RANDOM = new Random(2016);

    //or maybe just use ticks%numPermutations
    private int indexPermutation = 0;
    
    private static final Block[] transformBlocks = {Block.BONUS_BOMB,Block.BONUS_RANGE,Block.FREE};
    
    //Disntance at which the player is blocked
    private static final int BLOCK_DISTANCE = 6;
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
        if (players.size() != PlayerID.values().length) 
        	throw new IllegalArgumentException("Invalid number of players");

        this.ticks = ticks;
        this.board = Objects.requireNonNull(board);
        this.players = Collections.unmodifiableList(new ArrayList<Player>(Objects.requireNonNull(players)));
        this.bombs = Collections.unmodifiableList(new ArrayList<Bomb>(Objects.requireNonNull(bombs)));
        this.explosions = Collections.unmodifiableList(new ArrayList<Sq<Sq<Cell>>>(Objects.requireNonNull(explosions)));
        this.blasts = Collections.unmodifiableList(new ArrayList<Sq<Cell>>(Objects.requireNonNull(blasts)));
    }
    
    /**
     * Construct a Beginning-of-times Game state
     * @param board		Game Board
     * @param players	Game Players
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
        if(ticks >= Ticks.TOTAL_TICKS)
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
        List<Player> alivePlayers = new ArrayList<>();
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
        Map<Cell,Bomb> bombedCells = new HashMap<Cell,Bomb>(bombs.size());
        for(Bomb b : bombs){
            bombedCells.put(b.position(), b);
        }
        return bombedCells;
    }
    
    /**
     * @return 	A set containing currently Blasted cells
     */
    public Set<Cell> blastedCells(){
        Set<Cell> blastedCells = new HashSet<Cell>(blasts.size());
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
        List<Player> orderedPlayers = new ArrayList<>();
        
        Map<PlayerID, Player> hashedPlayers = listToHash(players);
        for(PlayerID id : playerPermutations.get(indexPermutation)){
            orderedPlayers.add(hashedPlayers.get(id));
        }

        //1. Evolution des particules d'explosion
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
            if(board.blockAt(playerPos).isBonus() && !consumedBonuses.contains(playerPos) && p.position().isCentral()){
                consumedBonuses.add(playerPos);
                playerBonuses.put(p.id(), board.blockAt(playerPos).associatedBonus());
            }
        }
        
        //2. Evolution of the board
        Board board1 = nextBoard(board,consumedBonuses,blastedCells1);
        
        //3. Evolution of the explosions
        List<Sq<Sq<Cell>>>  explosions1 = nextExplosions(explosions);   
        //4. Evolution of existing bombs

        List<Bomb> bombs0 = newlyDroppedBombs(orderedPlayers, bombDropEvents, bombs);
        bombs0.addAll(bombs);

        
        List<Bomb> bombs1 = new ArrayList<>();
        
        for(Bomb b : bombs0){
            if(b.fuseLengths().tail().isEmpty() || blastedCells1.contains(b.position()))
                explosions1.addAll(b.explosion());
            else{
                bombs1.add(new Bomb(b.ownerId(),b.position(),b.fuseLengths().tail(),b.range()));
            }
        }

        Set<Cell> bombedCells1 = new HashSet<>();
        
        for(Bomb b : bombs1){
            bombedCells1.add(b.position());
        }
        //5. Evolution of players
        List<Player> newPlayers = nextPlayers(players, playerBonuses, bombedCells1, board1, blastedCells1, speedChangeEvents);
       
        //Update so it uses next permutation for conflict
        indexPermutation = (indexPermutation+1)%playerPermutations.size();
        
        return new GameState(ticks+1,board1,newPlayers, bombs1, explosions1, blasts1);
    }
    
    /**
     * Calculates the next sequence of blasts
     * @param Current Blasts
     * @param Current Board
     * @param List of a Sequence of Explossions
     * @return  Next List of Explossions
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> nextBlasts = new ArrayList<>();
        for(Sq<Cell> blast : blasts0){
            Sq<Cell> t = blast.tail();
            if(!t.isEmpty() && board0.blockAt(blast.head()).isFree())
                nextBlasts.add(t);
        }
        for(Sq<Sq<Cell>> explosion : explosions0){
            Sq<Cell> b = explosion.head();
             nextBlasts.add(b);
        }
        return nextBlasts;
    }
    
    /**
     * Calculates the next Game Board
     * @param board0
     * @param consumedBonuses
     * @param blastedCells1
     * @return
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1){
        List<Sq<Block>> newBlocks = new ArrayList<>(); 

        Cell.ROW_MAJOR_ORDER.stream().forEachOrdered(curPos -> {
            if (consumedBonuses.contains(curPos)){
                newBlocks.add(Sq.constant(Block.FREE));
            } else if (!blastedCells1.contains(curPos)){
                newBlocks.add(board0.blocksAt(curPos).tail());
            } else {
                if(board0.blockAt(curPos) == Block.DESTRUCTIBLE_WALL){
                    newBlocks.add(Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,Block.CRUMBLING_WALL).concat(
                            Sq.constant(transformBlocks[RANDOM.nextInt(transformBlocks.length)])));
                }
                else if(board0.blockAt(curPos).isBonus())
                    newBlocks.add(board0.blocksAt(curPos).tail().limit(Ticks.BONUS_DISAPPEARING_TICKS).concat(Sq.constant(Block.FREE)));
                else
                    newBlocks.add(board0.blocksAt(curPos).tail());
            }
        
        });

        Board nextBoard = new Board(newBlocks);
        return nextBoard;
    }
    
    /**
     * Gets the next list of players with the next tick attributes applied
     * @param players0          current players
     * @param playerBonuses
     * @param bombedCells1
     * @param board1
     * @param blastedCells1
     * @param speedChangeEvents
     * @return
     */
    private static List<Player> nextPlayers(
            List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, 
            Set<Cell> bombedCells1,
            Board board1,
            Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents
            ){

        List<Player> players1 = new ArrayList<>();
        
        //Update players
        for(Player p : players0){
            Player newP;
            
            Sq<DirectedPosition> newDPos =null;
            Sq<LifeState> newLifestate = null;

            final int maxBombs1 = p.maxBombs();
            final int maxRange1 = p.bombRange();


            // Change his current path sequence
            newDPos = nextSqDPos(p, speedChangeEvents);
            //Check changeEvents
            if(p.lifeState().canMove()){
                // Get his future path (checking if he Can take that path.
                newDPos = nextSqCollision(newDPos,bombedCells1,board1,blastedCells1);
                
            }

            // Get his next state wrt explosions going on
            newLifestate = nextSqLifestate(p, newDPos,blastedCells1);

            // Create new player entity for the next state
            newP = new Player(p.id(), newLifestate ,newDPos,maxBombs1, maxRange1);

            //Check bonuses
            if(playerBonuses.containsKey(p.id())){
                newP = playerBonuses.get(p.id()).applyTo(newP);
            }
            
            players1.add(newP);
        }
        
        return players1;
    }

    /**
     * Calculates current and future positions from players and Give actions
     * Applies the speed change event so it will change it's current direction if the player reverses it's direction 
     * or is in a central subcell. Otherwire return the path continuing to the center of the cell and then apply the
     * specified speed change.
     * 
     * @param p                     Player
     * @param speedChangeEvents     Speed change events
     * @return                      Current and future positions
     */
    private static Sq<DirectedPosition> nextSqDPos(Player p, Map<PlayerID, Optional<Direction>> speedChangeEvents){
        Direction speedChange = null;

        DirectedPosition centralPos = p.directedPositions().findFirst(n -> n.position().isCentral());
        if(speedChangeEvents.containsKey(p.id())){
            speedChange = speedChangeEvents.get(p.id()).orElse(null);
            if(speedChange != null){
                if(!speedChange.isParallelTo(p.direction())){
                    return p.directedPositions().takeWhile(c -> !c.position().isCentral()).
                            concat(Player.DirectedPosition.moving(new DirectedPosition( centralPos.position(), speedChange)));
                }
                else{
                    return DirectedPosition.moving(new DirectedPosition(p.position(), speedChange));
                }
            }
            else{
                return p.directedPositions().takeWhile(c -> !c.position().isCentral()).
                        concat(Player.DirectedPosition.stopped(centralPos));
            }
        }
        
       return p.directedPositions();
    }
    
    /**
     * Gives the next Sq<DirectedPosition> for the players, checking collisions etc
     * @param movingPath       current and future positions
     * @param bombedCells1
     * @param board1
     * @param blastedCells1
     * @return                 future positions
     */
    private static Sq<DirectedPosition> nextSqCollision(Sq<DirectedPosition> movingPath, Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1){
        if(movingPath.head().position().isCentral()){
            if(board1.blockAt(movingPath.head().position().containingCell().
                    neighbor(movingPath.head().direction())).canHostPlayer()){
                // Okay to move
                return movingPath.tail();
            }
        } else {
            if(bombedCells1.contains(movingPath.head().position().containingCell())){
                //Collision with bombs
                final int distanceToCentral = movingPath.head().position().distanceToCentral();
                if(distanceToCentral ==  BLOCK_DISTANCE){
                    //Is he heading away from center?
                    if(distanceToCentral < movingPath.tail().head().position().distanceToCentral() ){
                        // it's okay to continue
                        return movingPath.tail();
                    } else
                        // we can't move, stay in the same state
                        return movingPath;
                }
                else{
                    // okay to move
                    return movingPath.tail();
                }
            }
            else{
                // okay to move
                return movingPath.tail();
            }
        }
        // we can't move, stay in the same state
        return movingPath;
    }
    
    /**
     * Calculates the next sequence of player lifestates
     * @param p
     * @param newDPos
     * @param blastedCells1
     * @return
     */
    private static Sq<LifeState> nextSqLifestate(Player p, Sq<DirectedPosition> newDPos, Set<Cell> blastedCells1){
      // Hit by a particle?
        if(p.lifeState().state() == State.VULNERABLE){
            if(blastedCells1.contains(newDPos.head().position().containingCell())){
                // Die...
                return p.statesForNextLife();
            }
        }
        // continue your life
        return p.lifeStates().tail();
    }

    /**
     * Calculates next list of Explosions
     * @param  Current Explosions
     * @return List of Explosions of the next state
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Sq<Cell>>> newList = new ArrayList<>();
        for(Sq<Sq<Cell>> c : explosions0){
            if(!c.tail().isEmpty())
                newList.add(c.tail());
        }
        return newList;
    }
    
    /**
     * Calculates the new bombs based on the current players, bombs and bomb drop events
     * @param players0
     * @param bombDropEvents
     * @param bombs0
     * @return Lis of bombs of the next state
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents, List<Bomb> bombs0){
        List<Bomb> newBombs = new ArrayList<>();
        
        for(Player p : players0){
            // This is an unlikely condition, therefore the overhead cost of having a continue instruction,
            // shouldn't be taken into account.
        	if (p==null) continue;
            if(bombDropEvents.contains(p.id())){
                int numBombs = 0;
                //Find how many bombs player has active
                boolean occupied = false;
                for(Bomb b : bombs0){
                    if(b.ownerId() == p.id())
                        numBombs++;
                    if(b.position().equals(p.position().containingCell()))
                        occupied = true;
                }
                //Check that another player didn't place a bomb at same place
                for(Bomb b : newBombs){
                    if(b.position().equals(p.position().containingCell()))
                        occupied = true;
                }
                if(p.maxBombs() > numBombs && !occupied && p.isAlive()){
                    newBombs.add(p.newBomb());
                }
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
