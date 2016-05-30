/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

public final class Bomb {
    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;
    
    /**
     * Creates an inmutable bomb from a sequence
     * @param Owner playerID
     * @param position
     * @param fuseLengths
     * @param range
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range){
        this.ownerId = Objects.requireNonNull(ownerId);
        this.position = Objects.requireNonNull(position);
        if(fuseLengths.isEmpty()) throw new IllegalArgumentException();
        this.fuseLengths = Objects.requireNonNull(fuseLengths);
        this.range = ArgumentChecker.requireNonNegative(range);
    }
    
    /**
     * Creates an inmutable bomb from a fuselength
     * @param ownerId
     * @param position
     * @param fuseLength
     * @param range
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range){
        this(ownerId, position, Sq.iterate(fuseLength, s->s-1).limit(fuseLength), range);
    }
    
    public PlayerID ownerId(){
        return ownerId;
    }
    
    public Cell position(){
        return position;
    }
    
    
    public Sq<Integer> fuseLengths(){
        return fuseLengths;
    }
    
    public int fuseLength(){
        return fuseLengths.head();
    }
    
    public int range(){
        return range;
    }
    
    /**
     * @return list of explossion cells
     */
    public List<Sq<Sq<Cell>>> explosion(){
        List<Sq<Sq<Cell>>> l = new ArrayList<>();
        for(Direction d : Direction.values()){
            l.add(explosionArmTowards(d));
        }
        return l;
    }
    
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir){
        Sq<Cell> c = Sq.iterate(position, n -> n.neighbor(dir)).limit(range);
        return Sq.repeat(Ticks.EXPLOSION_TICKS, c);
    }
    
    
}
