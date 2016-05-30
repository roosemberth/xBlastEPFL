/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Represents GameBoard Block
 *
 */
public enum Block {
    FREE,
    INDESTRUCTIBLE_WALL,
    DESTRUCTIBLE_WALL,
    CRUMBLING_WALL,
    BONUS_BOMB(Bonus.INC_BOMB),
    BONUS_RANGE(Bonus.INC_RANGE);
    
    private final Bonus maybeAssociateBonus;
    
    private Block(Bonus maybeAssociateBonus){
        this.maybeAssociateBonus = maybeAssociateBonus;
    }
    private Block(){
        this.maybeAssociateBonus = null;
    }
    
    
    /**
     * @return whether or not the given block is free
     */
    public boolean isFree(){
        return (this == FREE);
    }
    
    /**
     * @return whether or not the block can host the player
     */
    
    public boolean canHostPlayer(){
        return (this == FREE || this.isBonus());
    }
    
    
    /**
     * @return whether or not the block should cast a shadow
     */
    public boolean castsShadow(){
        return (this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL || this == CRUMBLING_WALL);
    }
    
   
    /**
     * @return whether or not the block is of type bonus
     */
    public boolean isBonus(){
        return (this == BONUS_BOMB || this == BONUS_RANGE);
    }
    
    /**
     * @return the bonus associated to the block
     */
    public Bonus associatedBonus(){
        if(maybeAssociateBonus == null)
             throw new NoSuchElementException("Bonus non existant");
        else
            return maybeAssociateBonus;
    }
}
