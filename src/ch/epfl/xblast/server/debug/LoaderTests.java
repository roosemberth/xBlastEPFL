/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server.debug;

import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;

import ch.epfl.xblast.client.ImageCollection;

public final class LoaderTests {

    public static void main(String[] args) throws URISyntaxException, IOException {
        // TODO Auto-generated method stub
        ImageCollection c = new ImageCollection("explosion");
        Image bomb = c.image(20);
    }

}
