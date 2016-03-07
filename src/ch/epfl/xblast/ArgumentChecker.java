package ch.epfl.xblast;

public class ArgumentChecker {
    private ArgumentChecker(){};
    
    public static int requireNonNegative(int value){
        if(value >= 0) return value; 
        else throw new IllegalArgumentException("Value is negative.");
    }
}
