package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

public final class GameStateDeserializer {
    private static final int PLAYERS_SEQUENCE_LENGTH = 16;
    private static final String IMAGES_BLOCK_DIRNAME = "block";
    private static final String IMAGES_EXPLOSION_DIRNAME = "explosion";
    private static final String IMAGES_PLAYER_DIRNAME = "player";
    private static final String IMAGES_SCORE_DIRNAME = "score";
    
    private static final int SCORE_MIDDLE_CODE = 10;
    private static final int SCORE_RIGHT_CODE  = 11;
    private GameStateDeserializer(){}
    
    public static GameState deserializeGameState(List<Byte> gs){
        /*
         * A minimal sequence is made of 
         *  - A min list of blocks:
         *    + made with min entropy -> 195 equal blocks -> sequence of type {4, -128, x, -63, x} total: 5 Bytes
         *  - A min list of explosions:
         *    + made with min entropy -> 195 equal blocks -> sequence of type {4, -128, x, -63, x} total: 5 Bytes
         *  - A list of players:
         *    + Fixed Length: PLAYERS_SEQUENCE_LENGTH
         *  - Remaining time:
         *    + A single byte
         * 
         * Thus any sequence is at least 11 + PLAYERS_SEQUENCE_LENGTH Bytes Long.
         */
        if (gs.size() < 11 + PLAYERS_SEQUENCE_LENGTH)
            throw new IllegalArgumentException("Sequence is too short to be valid!");
        // TODO: Check if it's too long

        // I tryed using InputStream Class, but there were unnecessary byte to Byte conversions.
        StreamValve<Byte> encodedStream = new StreamValve<Byte>(gs);
        
        List<Image>  boardImages     = null;
        List<Image>  bombsExplImages = null;
        List<Player> players         = null;
        List<Image>  scoreImages     = null;
        List<Image>  timeImages      = null;

        try {
            int boardSequenceLength      = Byte.toUnsignedInt(encodedStream.next());
            boardImages = readBoard(boardSequenceLength, encodedStream.next(boardSequenceLength));
            int explosionsSequeceLength = Byte.toUnsignedInt(encodedStream.next());
            bombsExplImages = readBombsExplosions(explosionsSequeceLength, encodedStream.next(explosionsSequeceLength));
            
            players = readPlayers(encodedStream.next(PLAYERS_SEQUENCE_LENGTH));
            timeImages = getTimer(encodedStream.next());
        } catch (EOFException e){
            throw new IllegalArgumentException("Could not Deserialize the sequence: ", e);
        }
        
        scoreImages = getScore(players);

        return new GameState(players, boardImages, bombsExplImages, scoreImages, timeImages);
    }
    
    private static List<Image> readBoard(int numBytes, List<Byte> board){
        check(board.size() >= 4, "Invalid Board Sequence length");
        ImageCollection boardTiles = new ImageCollection(IMAGES_BLOCK_DIRNAME);
        return RunLengthEncoder.decode(board).stream().map(b -> boardTiles.image(b)).collect(Collectors.toList());
    }
    private static List<Image> readBombsExplosions(int numBytes, List<Byte> bombs){
        check(bombs.size() >= 4, "Invalid Explosions Sequece length");
        ImageCollection bombAndExplTiles = new ImageCollection(IMAGES_EXPLOSION_DIRNAME);
        return RunLengthEncoder.decode(bombs).stream().map(b -> bombAndExplTiles.imageOrNull(b)).collect(Collectors.toList());
    }
    private static List<Player> readPlayers(List<Byte> playerInfo){
        check(playerInfo.size() == PLAYERS_SEQUENCE_LENGTH, "Invalid Player Sequence length");
        check((playerInfo.size()%GameState.NUM_PLAYERS)==0, "Invalid number of players");
        ImageCollection playerImages = new ImageCollection(IMAGES_PLAYER_DIRNAME);
        List<Player> players = new ArrayList<>(GameState.NUM_PLAYERS);
        
        int playerLives, xPos, yPos, state;
        for (int i = 0; i<GameState.NUM_PLAYERS; ++i){
            playerLives = playerInfo.get(i*4+0);
            xPos = Byte.toUnsignedInt(playerInfo.get(i*4+1));
            yPos = Byte.toUnsignedInt(playerInfo.get(i*4+2));
            state = playerInfo.get(i*4+3);

            players.add(new GameState.Player(
                    PlayerID.values()[i],
                    playerLives,
                    new SubCell(xPos, yPos),
                    playerImages.imageOrNull(state)));
        }

        return players;
    }
    private static List<Image> getScore(List<Player> players){
        ImageCollection scoreImages = new ImageCollection(IMAGES_SCORE_DIRNAME);
        List<Image> scoreRow = new ArrayList<>();
        for (int i = 0; i<GameState.NUM_PLAYERS; ++i){
            Player p = players.get(i);
            if (i==2)
                for (int j=0; j<8; ++j)
                    scoreRow.add(scoreImages.image(12));
            int idx = i*2;

            if (p.lives==0) idx++;               // if player is dying
            
            scoreRow.add(scoreImages.image(idx));
            scoreRow.add(scoreImages.image(SCORE_MIDDLE_CODE)); // Text middle
            scoreRow.add(scoreImages.image(SCORE_RIGHT_CODE)); // Text right
        }

        return scoreRow;
    }
    private static List<Image> getTimer(int ticks){
        List<Image> ret = new ArrayList<>(GameState.MAX_NUM_OF_TICKS);
        ImageCollection tickImages = new ImageCollection(IMAGES_SCORE_DIRNAME);
        final Image ledOn = tickImages.image(21);
        final Image ledOff = tickImages.image(20);

        // 
        for (int i = 0; i<GameState.MAX_NUM_OF_TICKS; ++i){
            ret.add(ticks-->0?ledOn:ledOff);
        }
        return ret;
    }

    private static void check(boolean condition, String message){
        if (!condition) throw new IllegalArgumentException(message);
    }
}

class StreamValve <T>{
    private List<T> stream;
    public StreamValve(List<T> stream) {
        this.stream = stream;
    }
    public T next() throws EOFException {
        if (stream.size()==0)
            throw new EOFException("End of Stream");
        T ret = stream.get(0);
        stream = stream.subList(1, stream.size());
        return ret;
    }
    public List<T> next(int length) throws EOFException{
        List<T> ret = new ArrayList<>();
        for (int i = 0; i<length; ++i)
            ret.add(next());
        return ret;
    }
}
