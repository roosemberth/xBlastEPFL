package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.client.GameState.Player;

public final class GameStateDeserializer {
    private GameStateDeserializer(){}
    
    public static GameState deserializeGameState(List<Byte> gs){
        List<Image> images = new ArrayList<>();
        int bytesBoard = gs.get(0);
        int bytesBombsExplosions = gs.get(bytesBoard);
        
        images.addAll(readBoard(bytesBoard,gs.subList(1, bytesBoard)));
        images.addAll(readBombsExplosions(
                bytesBombsExplosions, gs.subList(bytesBoard+1, bytesBoard+bytesBombsExplosions)));
        
    }
    
    private static List<Image> readBoard(int numBytes, List<Byte> board){
        return null;
    }
    private static List<Image> readBombsExplosions(int numBytes, List<Byte> bombs){
        return null;
    }
    private static List<Player> readPlayers(int numPlayers, List<Byte> playerInfo){
        return null;
    }
    private static List<Image> getScore(List<Player> players){
        return null;
    }
    private static List<Image> getTimer(int ticks){
        return null;
    }
}
