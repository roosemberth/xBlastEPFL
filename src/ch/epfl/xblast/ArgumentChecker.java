package ch.epfl.xblast;

/**
 *  Checks Helper class for verifying arguments 
 */
public class ArgumentChecker {
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
