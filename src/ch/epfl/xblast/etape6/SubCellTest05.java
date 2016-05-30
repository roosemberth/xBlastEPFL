/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.etape6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.SubCell;

public class SubCellTest05 {

    private static final int SUBDIVISIONS = 16;

    @Test
    public void hashCodeSubCellsConsistentWithEquals() {
        for (int i = 0; i < Cell.COLUMNS * SUBDIVISIONS; i++) {
            for (int j = 0; j < Cell.ROWS * SUBDIVISIONS; j++) {
                SubCell c1 = new SubCell(i, j);
                SubCell c2 = new SubCell(i, j);
                if (c1.equals(c2)) {
                    assertEquals(c1.hashCode(), c2.hashCode());
                }
            }
        }
    }

}
