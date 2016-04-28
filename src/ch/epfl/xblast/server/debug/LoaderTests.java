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
