package ch.epfl.xblast.server.debug;

import javax.swing.JFrame;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
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

        frame.add(xc);
        frame.setVisible(true);
        frame.setPreferredSize(xc.getPreferredSize());
        frame.setSize(xc.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while(!gs.isGameOver()){
            xc.setGameState(GameStateDeserializer.deserializeGameState(
                    GameStateSerializer.serialize(lv.getBp(), gs)), PlayerID.PLAYER_1);
            gs = gs.next(reg.randomSpeedChangeEvents(), reg.randomBombDropEvents());
            Thread.sleep(20);
        }
    }

}
