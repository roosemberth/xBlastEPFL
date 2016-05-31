/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * ImageCollection
 *
 * Holds the Image set for the client.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class ImageCollection {
    private final Map<Integer, Image> images;

    /**
     * Constructor
     * @param dirName directory under the classpath where to search the images.
     */
    public ImageCollection(String dirName){
        images = new HashMap<>();
        File dir;
        try {
            dir = new File(ImageCollection.class.getClassLoader().getResource(dirName).toURI());
            for(File f : dir.listFiles()){
                String name = f.getName();
                int index = Integer.parseInt(name.substring(0, 3));
                images.put(index, ImageIO.read(f));
            }
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return the image corresponding to the given index.
     * @throws NoSuchElementException if the given index does not correspond to
     *        any of hte found images. If you want null instead use imageOrNull method.
     */
    public Image image(int index){
        if(!images.containsKey(index))
            throw new NoSuchElementException("Image not available");
        return images.get(index);
    }

    /**
     * @return the image corresponding to the given index.
     * @return null if the given index does not correspond to any of the found images.
     *        If you want an exception instead use image method.
     */
    public Image imageOrNull(int index){
        return images.get(index);
    }
}
