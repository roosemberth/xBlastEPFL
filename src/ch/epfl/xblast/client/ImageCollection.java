package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public final class ImageCollection {
    
    private final String dirName;
    private final Map<Integer, Image> images;
    
    public ImageCollection(String dirName) throws URISyntaxException, IOException{
        this.dirName = dirName;
        File dir = new File(ImageCollection.class.getClassLoader().getResource(dirName).toURI());
        images = new HashMap<>();
        for(File f : dir.listFiles()){
            String name = f.getName();
            int index = Integer.parseInt(name.substring(0, 3));
            images.put(index, ImageIO.read(f));
        }
    }
    
    public Image image(int index){
        if(!images.containsKey(index))
            throw new NoSuchElementException("Image not available");
        return images.get(index);
    }
    public Image imageOrNull(int index){
        return images.get(index);
    }
}
