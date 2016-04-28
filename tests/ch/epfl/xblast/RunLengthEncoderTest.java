package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class RunLengthEncoderTest {

    @Test
    public void BackAndForth() {
        List<Integer> compressedAsInts = Arrays.asList(
                -50,  2,  1, -2, 0, 3, 1, 3, 1,-2, 0, 1, 1, 3, 1, 3,
                  1,  3,  1,  1,-2, 0, 1, 3, 1, 3,-2, 0,-1, 1, 3, 1, 3, 1,
                  3,  1,  1,  2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                  2,  3,  2,  3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
                  3,  1,  0,  0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
                  1,  3,  0,  0,-1, 1, 3, 1, 1,-5, 2, 3, 2, 3,-5, 2, 3, 2,
                  3,  1, -2 , 0, 3,-2, 0, 1, 3, 2, 1, 2
                  );
        List<Byte> compressed = Arrays.asList(compressedAsInts.stream().map(e->(byte)((int)e)).toArray(Byte[]::new));
        compressed.stream().forEach(e->System.out.print(e + ", ")); System.out.println();
        List<Byte> uncompressed = RunLengthEncoder.decode(compressed);
        uncompressed.stream().forEach(e->System.out.print(e + ", ")); System.out.println();
        assertEquals(195, uncompressed.size());
        List<Byte> recompressed = RunLengthEncoder.encode(uncompressed);
        recompressed.stream().forEach(e->System.out.print(e + ", ")); System.out.println();

        if (compressed.size()!=recompressed.size())
            fail("Size mismatch between compressed("+compressed.size()+") and recompressed("+recompressed.size()+") streams");
        for (int i = 0; i<Math.min(compressed.size(), recompressed.size()); ++i){
            if ((byte)compressed.get(i) != (byte)recompressed.get(i))
                fail("Compression mismatch on index ");
        }
    }

}
