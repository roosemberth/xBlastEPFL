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
        if(this == FREE){
            return true;
        }
        return false;
    }
    
    /**
     * @return whether or not the block can host the player
     */
    public boolean canHostPlayer(){
        if(this == FREE || this.isBonus()){
            return true;
        }
        return false;
    }
    
    
    /**
     * @return whether or not the block should cast a shadow
     */
    public boolean castsShadow(){
        if(this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL || this == CRUMBLING_WALL){
            return true;
        }
        return false;
    }
    
   
    /**
     * @return whether or not the block is of type bonus
     */
    public boolean isBonus(){
        if(this == BONUS_BOMB || this == BONUS_RANGE){
            return true;
        }
        return false;
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
