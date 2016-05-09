package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

public class KeyboardEventHandler extends KeyAdapter {
    private final Map<Integer, PlayerAction> keyBindings;
    private final Consumer<PlayerAction> callback;
    public KeyboardEventHandler(Map<Integer, PlayerAction> keyBindings, Consumer<PlayerAction> callback) {
        this.keyBindings = keyBindings;
        this.callback = callback;
    }
    @Override public void keyTyped(KeyEvent e) {
        if (keyBindings.keySet().contains(e.getKeyCode()))
            callback.accept(keyBindings.get(e.getKeyCode()));
    }
}
