package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {
    public static final int MAX_NUM_OF_TICKS = 60;
    public static final int NUM_PLAYERS = 4;
    public GameState(List<Player> players, List<Image> boardImages,
            List<Image> bombsExplImages,List<Image> scoreImages,List<Image> timeImages){
        this.players = Objects.requireNonNull(players);
        this.boardImages = Objects.requireNonNull(boardImages);
        this.bombsExplImages = Objects.requireNonNull(bombsExplImages);
        this.scoreImages = Objects.requireNonNull(scoreImages);
        this.timeImages = Objects.requireNonNull(timeImages);
    }
    
    public final List<Player> players;
    public final List<Image> boardImages;
    public final List<Image> bombsExplImages;
    public final List<Image> scoreImages;
    public final List<Image> timeImages;
    
    public static final class Player{
        public final PlayerID id;
        public final int lives;
        public final SubCell position;
        public final Image image;

        public Player(PlayerID id, int lives, SubCell position, Image image){
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }
    }
}
