package ch.epfl.xblast.server;

public enum Block {
    FREE,
    INDESTRUCTIBLE_WALL,
    DESTRUCTIBLE_WALL,
    CRUMBLING_WALL,
    BONUS_BOMB(Bonus.),
    BONUS_RANGE;
    
    private Bonus maybeAssociateBonus;
    
    private Block(Bonus maybeAssociateBonus){
        this.maybeAssociateBonus = maybeAssociateBonus;
    }
    private Block(){
        this.maybeAssociateBonus = null;
    }
    public boolean isFree(){
        if(this == FREE){
            return true;
        }
        return false;
    }
    public boolean canHostPlayer(){
        if(this == FREE || this.isBonus()){
            return true;
        }
        return false;
    }
    public boolean castsShadow(){
        if(this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL || this == CRUMBLING_WALL){
            return true;
        }
        return false;
    }
    public boolean isBonus(){
        if(this == BONUS_BOMB || this == BONUS_RANGE){
            return true;
        }
        return false;
    }
}
