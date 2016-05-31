/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

/**
 * Bonus
 *
 * This class represents bonuses appearing after a destructible block explosion
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public enum Bonus {  
    /**
     * Extra bomb bomus:
     * Gives the player the ability to plant an additional {@link Bomb}, with a maximum of 9
     */
    INC_BOMB{
        @Override public Player applyTo(Player player){
            // Prevent an "invalid" player status to keep growing 
            if(player.maxBombs() < MAX_BOMBS)
                return player.withMaxBombs(player.maxBombs()+1);
            return player;
        }
    },
    /**
     * Bomb range bonus
     * Increments the player's {@link Bomb} range with a maximum of 9 {@link Cell}s
     */
    INC_RANGE{
        @Override public Player applyTo(Player player){
            // Prevent an "invalid" player status to keep growing 
            if(player.bombRange() < MAX_RANGE)
                return player.withBombRange(player.bombRange()+1);
            return player;
        }
    };

    private static final int MAX_RANGE = 9;
    private static final int MAX_BOMBS = 9;

    /**
     * Applies this {@link Bonus} to the Specified {@link Player}
     * @param  Player to whom apply the bonus to
     * @return Specified Player with the applied Bonus
     */
    abstract public Player applyTo(Player player);
}
