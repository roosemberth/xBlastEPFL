/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class GameStateDeserializer {

    private static final ImageCollection BLOCK_COLLECTION = new ImageCollection("block");
    private static final ImageCollection EXPLOSION_COLLECTION = new ImageCollection("explosion");
    private static final ImageCollection PLAYER_COLLECTION = new ImageCollection("player");
    private static final ImageCollection SCORE_COLLECTION = new ImageCollection("score");
    
    private GameStateDeserializer(){}
    
    public static GameState deserializeGameState(List<Byte> gs){
        List<Image> images = new ArrayList<>();
        int bytesBoard = gs.get(0);
        int bytesBombsExplosions = gs.get(bytesBoard+1);
        
        List<Image> boardImages = readBoard(gs.subList(1, bytesBoard+1));
        List<Image> bombsExplImages = readBombsExplosions(gs.subList(bytesBoard+2, bytesBoard+bytesBombsExplosions+2));
        List<Player> players = readPlayers(gs.subList(bytesBoard+bytesBombsExplosions+2, bytesBoard+bytesBombsExplosions+16+2));
        List<Image> scoreImages = readScore(players);
        List<Image> timerImages = readTimer(gs.get(bytesBoard+bytesBombsExplosions+16+2)); 
        
        return new GameState(players, boardImages, bombsExplImages, scoreImages, timerImages);
    }

    private static List<Player> readPlayers(List<Byte> playerInfo){
        List<Player> players = new ArrayList<>();
        Iterator<Byte> it = playerInfo.iterator();
        for(int i = 0; i < 4; i++){
            PlayerID id = PlayerID.values()[i];
            int lives = (int)it.next();
            int x = Byte.toUnsignedInt(it.next());
            int y = Byte.toUnsignedInt(it.next());
            int imageID = it.next();
            Image image = PLAYER_COLLECTION.imageOrNull(imageID);
            SubCell position = new SubCell(x, y);
            Player p = new Player(id, lives, position, image);
            players.add(p);
        }
        return players;
    }
    
    private static List<Image> readBoard(List<Byte> board){
        List<Image> images = new ArrayList<>();
        
        List<Byte> decoded = RunLengthEncoder.decode(board);
        Map<Cell, Integer> order = new HashMap<>();
        int i = 0;
        //Get ordering from Spirar_order
        for(Cell c : Cell.SPIRAL_ORDER){
            order.put(c, i);
            i++;
        }
        for(Cell c : Cell.ROW_MAJOR_ORDER){
            int index = decoded.get(order.get(c));
            images.add(BLOCK_COLLECTION.imageOrNull(index));
        }
        
        return images;
    }
    
    private static List<Image> readBombsExplosions(List<Byte> bombs){
        List<Image> images = new ArrayList<>();

        for(Byte b : RunLengthEncoder.decode(bombs)){
            images.add(EXPLOSION_COLLECTION.imageOrNull(b));
        }
        
        return images;
    }
    
    private static List<Image> readScore(List<Player> players){
        List<Image> images = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            int playerId = 2 * i;
            int dead = players.get(i).getLives() <= 0 ? 1 : 0;
            images.add(SCORE_COLLECTION.imageOrNull(playerId+dead));
            images.add(SCORE_COLLECTION.imageOrNull(10));
            images.add(SCORE_COLLECTION.imageOrNull(11));
        }
        for(int i = 0; i < 8; i++){
            images.add(6, SCORE_COLLECTION.imageOrNull(12));
        }
        for(int i = 2; i < 4; i++){
            int playerId = 2 * i;
            int dead = players.get(i).getLives() <= 0 ? 1 : 0;
            images.add(SCORE_COLLECTION.imageOrNull(playerId+dead));
            images.add(SCORE_COLLECTION.imageOrNull(10));
            images.add(SCORE_COLLECTION.imageOrNull(11));
        }
        return images;
    }
    
    private static List<Image> readTimer(int remainingSec){
        List<Image> images = new ArrayList<>();
        for(int i = 0; i < 60; i++){
            if(i >= remainingSec)
                images.add(SCORE_COLLECTION.imageOrNull(20));
            else
                images.add(SCORE_COLLECTION.imageOrNull(21));
        }
        return images;
    }
}
