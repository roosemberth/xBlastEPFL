package ch.epfl.xblast.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.junit.Test;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class GameBoardDraw {
    private static RandomEventGenerator randEvents = new RandomEventGenerator(2016, 30, 100);
    private static ch.epfl.xblast.server.GameState serverGame = Level.defaultGameState();
    private static ch.epfl.xblast.client.GameState clientGame = null;

    private XBlastComponent gameRender = new XBlastComponent();
    private JFrame frame = new JFrame("XBlast Test");

    @Test
    public void showInterface(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(gameRender);
        frame.pack();
        frame.setVisible(true);
        
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP,    PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN,  PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT,  PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        Consumer<PlayerAction> c = System.out::println;

        frame.addKeyListener(new KeyboardEventHandler(kb, c));
        frame.requestFocusInWindow();
        
        ActionListener tick = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                gameRender.setGameState(clientGame, null);
                nextGameState();
                if (serverGame.isGameOver()){
                    try {
                        while (System.in.read()!=0x20);
                    } catch (IOException e1) {
                    }
                    System.exit(0);
                }
            }
        };
        
        Timer t = new Timer(1000/20, tick);
        t.start();

        while (t.isRunning())
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e1) {

            }
    }
    
    private void nextGameState(){
        serverGame = serverGame.next(randEvents.randomSpeedChangeEvents(), randEvents.randomBombDropEvents());
        clientGame = GameStateDeserializer.deserializeGameState(
                GameStateSerializer.serialize(Level.defaultBoardPainter(), serverGame)
                );
        
    }
}
