package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {

    public GameState(List<Player> players, List<Image> boardImages,
            List<Image> bombsExplImages,List<Image> scoreImages,List<Image> timeImages){
        this.players = Objects.requireNonNull(players);
        this.boardImages = Objects.requireNonNull(boardImages);
        this.bombsExplImages = Objects.requireNonNull(bombsExplImages);
        this.scoreImages = Objects.requireNonNull(scoreImages);
        this.timeImages = Objects.requireNonNull(timeImages);
    }
    
    private final List<Player> players;
    private final List<Image> boardImages;
    private final List<Image> bombsExplImages;
    private final List<Image> scoreImages;
    private final List<Image> timeImages;
    
    public final class Player{
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;
        public Player(PlayerID id, int lives, SubCell position, Image image){
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }
    }
}
