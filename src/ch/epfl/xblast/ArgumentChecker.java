/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast;

/**
 * ArgumentChecker
 *
 * Checks Helper class for verifying arguments 
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public class ArgumentChecker {
    /**
     * Private Constructor.
     * This class is not instanciable.
     */
    private ArgumentChecker(){};

    /**
     * Checks that the argument is not negative
     * @param   value
     * @return  value assured to be positive.
     */
    public static int requireNonNegative(int value){
        if(value >= 0) return value; 
        else throw new IllegalArgumentException("Value is negative.");
    }
}
