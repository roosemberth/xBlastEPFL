package ch.epfl.xblast.server;

public enum Bonus {  
    INC_BOMB{
        public Player applyTo(Player player){
            if(player.bombRange() != MAX_RANGE)
                return player.withBombRange(player.bombRange()+1);
            return player;
        }
    },
    INC_RANGE{
        public Player applyTo(Player player){
            if(player.maxBombs() != MAX_BOMBS)
                return player.withMaxBombs(player.maxBombs()+1);
            return player;
        }
    };


    private static final int MAX_RANGE = 9;
    private static final int MAX_BOMBS = 9;
    
    abstract public Player applyTo(Player player);
}
