package ch.epfl.xblast.server;

public enum Block {
    FREE,
    INDESTRUCTIBLE_WALL,
    DESTRUCTIBLE_WALL,
    CRUMBLING_WALL;
    
    public boolean isFree(){
        if(this == FREE){
            return true;
        }
        return false;
    }
    public boolean canHostPlayer(){
        if(this == FREE){
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
}
