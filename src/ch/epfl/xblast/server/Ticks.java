/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;

/**
 *  Game Time-Related Constants
 */
public interface Ticks {
    public static final int PLAYER_DYING_TICKS = 8;
    public static final int PLAYER_INVULNERABLE_TICKS = 64;
    public static final int BOMB_FUSE_TICKS = 100;
    public static final int EXPLOSION_TICKS = 30;
    public static final int WALL_CRUMBLING_TICKS = EXPLOSION_TICKS;
    public static final int BONUS_DISAPPEARING_TICKS = EXPLOSION_TICKS;

    public static final int TICKS_PER_SECOND = 20;
    public static final int TICK_NANOSECOND_DURATION = Time.NS_PER_S/TICKS_PER_SECOND;  
    public static final int TOTAL_TICKS = TICKS_PER_SECOND*Time.S_PER_MIN*2;
}
