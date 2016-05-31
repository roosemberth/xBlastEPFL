/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast;

/**
 * Time
 *
 * Provides Time roll constrains
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public interface Time{
    public static final int S_PER_MIN = 60;
    public static final int MS_PER_S = 1000;
    public static final int US_PER_S = 1000 * MS_PER_S;
    public static final int NS_PER_S = 1000 * US_PER_S;
}
