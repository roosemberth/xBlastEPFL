package ch.epfl.xblast.server.debug;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class TestGraphic {

    public static void main(String[] args) throws InterruptedException {

        Level lv = Level.DEFAULT_LEVEL;
        JFrame frame = new JFrame();
        XBlastComponent xc = new XBlastComponent();
        GameState gs = lv.getGs();

        RandomEventGenerator reg = new RandomEventGenerator(2016,30,100);


        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        
        Consumer<PlayerAction> c = System.out::println;
        
        
        frame.add(xc);
        
       // frame.addKeyListener(new KeyboardEventHandler(kb, c));
        xc.addKeyListener(new KeyboardEventHandler(kb, c));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        frame.setVisible(true);

        xc.requestFocusInWindow();
        while(!gs.isGameOver()){
            xc.setGameState(GameStateDeserializer.deserializeGameState(
                    GameStateSerializer.serialize(lv.getBp(), gs)), PlayerID.PLAYER_1);
            gs = gs.next(reg.randomSpeedChangeEvents(), reg.randomBombDropEvents());
            Thread.sleep(20);
        }
    }

}
