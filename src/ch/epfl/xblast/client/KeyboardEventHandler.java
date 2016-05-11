package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

public class KeyboardEventHandler extends KeyAdapter implements KeyListener{

    private Map<Integer,PlayerAction> keys;
    private Consumer<PlayerAction> consumer;
    
    public KeyboardEventHandler(Map<Integer,PlayerAction> keys, Consumer<PlayerAction> consumer){
        this.keys = new HashMap<>(keys);
        this.consumer = Objects.requireNonNull(consumer);
    }
    
    public void keyPressed(KeyEvent e){
        if(keys.containsKey(e.getKeyCode()))
            consumer.accept(keys.get(e.getKeyCode()));
    }
    
    
}
