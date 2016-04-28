package ch.epfl.xblast;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class GameStateSerializerTest {

    @Test
    public void serializeDefaultGame() {
        List<Byte> recompressed = GameStateSerializer.serialize(
                Level.DEFAULT_LEVEL.getBoardPainter(), 
                Level.DEFAULT_LEVEL.getGameState());

        List<Integer> compressedAsInts = Arrays.asList(
                121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3,
                1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1,
                3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
                3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
                1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2,
                3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2,

                4, -128, 16, -63, 16,

                3, 24, 24, 6,
                3, -40, 24, 26,
                3, -40, -72, 46,
                3, 24, -72, 66,

                60
                );

        List<Byte> compressed = Arrays.asList(compressedAsInts.stream().map(e->(byte)((int)e)).toArray(Byte[]::new));

        compressed.stream().forEach(e->System.out.print(e + ", ")); System.out.println();
        recompressed.stream().forEach(e->System.out.print(e + ", ")); System.out.println();

        if (compressed.size()!=recompressed.size())
            fail("Size mismatch between compressed("+compressed.size()+") and recompressed("+recompressed.size()+") streams");
        for (int i = 0; i<Math.min(compressed.size(), recompressed.size()); ++i){
            if ((byte)compressed.get(i) != (byte)recompressed.get(i))
                fail("Compression mismatch on index " + (i+1));
        }
    }

}
