/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.etape6;

import static org.junit.Assert.*;

import org.junit.Test;

import static ch.epfl.xblast.ArgumentChecker.*;

public class ArgumentCheckerTest {
    @Test
    public void requireNonNegativeAcceptPositives() {
        assertEquals("Zero is accepted", 0, requireNonNegative(0));
        assertEquals("One is accepted", 1, requireNonNegative(1));
        assertEquals("42 is accepted", 42, requireNonNegative(42));
        assertEquals("Max integer is accepted", Integer.MAX_VALUE, requireNonNegative(Integer.MAX_VALUE));
    }

    @Test(expected=IllegalArgumentException.class)
    public void requireNonNegativeRejectMinusOne() {
        requireNonNegative(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void requireNonNegativeRejectMinus42() {
        requireNonNegative(-42);
    }

    @Test(expected=IllegalArgumentException.class)
    public void requireNonNegativeRejectMinInteger() {
        requireNonNegative(Integer.MIN_VALUE);
    }
}
