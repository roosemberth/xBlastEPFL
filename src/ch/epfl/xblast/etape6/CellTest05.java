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

public class CellTest05 {

    @Test
    public void hashCodeCellsConsistentWithEquals() {
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            Cell c1 = new Cell(c.x(), c.y());
            if (c.equals(c1)) {
                assertEquals(c.hashCode(), c1.hashCode());
            }
        }
    }
    
}
