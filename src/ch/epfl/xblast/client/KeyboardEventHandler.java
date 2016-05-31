/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

/**
 * KeyboardEventHandler
 *
 * Filters events from the keyboard.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public class KeyboardEventHandler extends KeyAdapter implements KeyListener{
    private Map<Integer,PlayerAction> keys;
    private Consumer<PlayerAction> consumer;

    /**
     * Constructor
     * @param keys Map of keycodes to player actions.
     * @param consumer Callback for pressed keycodes contained on the keys map.
     */
    public KeyboardEventHandler(Map<Integer,PlayerAction> keys, Consumer<PlayerAction> consumer){
        this.keys = new HashMap<>(keys);
        this.consumer = Objects.requireNonNull(consumer);
    }

    /**
     * Keyboard event listener.
     * Filters the keyboard event received.
     */
    @Override public void keyPressed(KeyEvent e){
        if(keys.containsKey(e.getKeyCode()))
            consumer.accept(keys.get(e.getKeyCode()));
    }


}
