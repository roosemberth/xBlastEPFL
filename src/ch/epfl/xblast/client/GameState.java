package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {

    private final List<Player> players;
    private final List<Image> boardImages;
    private final List<Image> bombsExplImages;
    private final List<Image> scoreImages;
    private final List<Image> timeImages;
    
    public GameState(List<Player> players, List<Image> boardImages,
            List<Image> bombsExplImages,List<Image> scoreImages,List<Image> timeImages){
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.boardImages = Collections.unmodifiableList(new ArrayList<>(boardImages));
        this.bombsExplImages = Collections.unmodifiableList(new ArrayList<>(bombsExplImages));
        this.scoreImages = null;//Collections.unmodifiableList(new ArrayList<>(scoreImages));
        this.timeImages = Collections.unmodifiableList(new ArrayList<>(timeImages));
    }
    
   

    
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }




    public List<Image> getBoardImages() {
        return Collections.unmodifiableList(boardImages);
    }




    public List<Image> getBombsExplImages() {
        return Collections.unmodifiableList(bombsExplImages);
    }




    public List<Image> getScoreImages() {
        return Collections.unmodifiableList(scoreImages);
    }




    public List<Image> getTimeImages() {
        return Collections.unmodifiableList(timeImages);
    }




    public static final class Player{
        public final PlayerID id;
        public final int lives;
        public final SubCell position;
        public final Image image;
        
        public Player(PlayerID id, int lives, SubCell position, Image image){
            this.id = Objects.requireNonNull(id);
            this.lives = lives;
            this.position = Objects.requireNonNull(position);
            this.image = image;
        }
        
        
    }
}
