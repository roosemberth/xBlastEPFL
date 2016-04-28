package ch.epfl.xblast.client;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.RunLengthEncoder;

public class GameStateDeserializer {
    private static final int PLAYERS_SEQUENCE_LENGTH = 16;
    private GameStateDeserializer() { }
    
    public static final List<Byte> deserializeGameState(List<Byte> encoded){
        // I tryed using InputStream Class, but there were unnecessary byte to Byte conversions.
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
        
        StreamValve<Byte> encodedStream = new StreamValve<Byte>(encoded);
        
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
        if (encoded.size() < 11 + PLAYERS_SEQUENCE_LENGTH)
            throw new IllegalArgumentException("Sequence is too short to be valid!");
        // TODO: Check if it's too long
        
        List<Byte>  compressedBoard         = null;
        List<Byte>  compressedExplossions   = null;
        List<Byte>  decompressedBoard       = null;
        List<Byte>  decompressedExplossions = null;
        List<Byte>  players                 = null;
        Byte        timeRemaining           = null;

        int boardSequenceLength       = 0;
        int explossionsSequeceLength  = 0;

        List<Byte>  decompressedSequence = new ArrayList<>();
        
        try {
            boardSequenceLength = Byte.toUnsignedInt(encodedStream.next());
            check(boardSequenceLength >= 4, "Invalid Board Sequence length");
            compressedBoard = encodedStream.next(boardSequenceLength);
            
            explossionsSequeceLength = Byte.toUnsignedInt(encodedStream.next());
            check(explossionsSequeceLength >= 4, "Invalid Explosions Sequece length");
            compressedExplossions = encodedStream.next(explossionsSequeceLength);
            
            players = encodedStream.next(PLAYERS_SEQUENCE_LENGTH);
            
            timeRemaining = encodedStream.next();
        } catch (EOFException e){
            throw new IllegalArgumentException("Error Decomposing the Sequece: ", e);
        }
        
        String currentSequence = "\b";    // Debug tracking
        try {
            currentSequence = "Board";
            decompressedBoard = RunLengthEncoder.decode(compressedBoard);
            currentSequence = "Explosions";
            decompressedExplossions = RunLengthEncoder.decode(compressedExplossions);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Error Decoding the " + currentSequence + " Sequece: ", e);
        }
        
        decompressedSequence.addAll(decompressedBoard);
        decompressedSequence.addAll(decompressedExplossions);
        decompressedSequence.addAll(players);
        decompressedSequence.add(timeRemaining);

        return decompressedSequence;
    }

    private static void check(boolean condition, String message){
        if (!condition) throw new IllegalArgumentException(message);
    }
}
