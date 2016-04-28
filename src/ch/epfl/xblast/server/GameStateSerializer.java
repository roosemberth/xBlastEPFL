package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.swing.event.CellEditorListener;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.server.drawer.BlockImage;
import ch.epfl.xblast.server.drawer.BoardPainter;
import ch.epfl.xblast.server.drawer.ExplosionPainter;
import ch.epfl.xblast.server.drawer.PlayerPainter;

public class GameStateSerializer {
    private GameStateSerializer(){}
    public static List<Byte> serialize(BoardPainter boardPainter, GameState gameState){
        List<Byte> ret = new ArrayList<>();
 
        Board board = gameState.board();
        // GameBoard in Spiral Order
        {
            BoardPainter painter = new BoardPainter(Defaults.DEFAULT_PALETTE, BlockImage.IRON_FLOOR_S);
            List<Byte> uncompressedBoard = 
                    Arrays.asList(Cell.SPIRAL_ORDER.stream().map(c -> painter.byteForCell(board, c)).toArray(Byte[]::new));
            List<Byte> compressedBoard = RunLengthEncoder.encode(uncompressedBoard);
            
            ret.add((byte) compressedBoard.size());
            ret.addAll(compressedBoard);
        }

        // Explosions in a Row-Major Order
        {
        Map<Cell, Bomb> bombedCells = gameState.bombedCells();
        Set<Cell> blastedCells = gameState.blastedCells();
            List<Byte> uncompressedExplossions = new ArrayList<>();
            for (Cell c : Cell.ROW_MAJOR_ORDER){
                if (bombedCells.containsKey(c)){
                    uncompressedExplossions.add(ExplosionPainter.byteForBomb(bombedCells.get(c)));
                    continue;
                }
                if (blastedCells.contains(c)){
                    if (!board.blockAt(c).isFree())
                        uncompressedExplossions.add(ExplosionPainter.BYTE_FOR_EMPTY);
                    else {
                        boolean N,E,S,W;
                        N = blastedCells.contains(c.neighbor(Direction.N));
                        E = blastedCells.contains(c.neighbor(Direction.E));
                        S = blastedCells.contains(c.neighbor(Direction.S));
                        W = blastedCells.contains(c.neighbor(Direction.W));
                        uncompressedExplossions.add(ExplosionPainter.byteForBlast(N, E, S, W));
                    }
                    continue;
                }
                uncompressedExplossions.add(ExplosionPainter.BYTE_FOR_EMPTY);
            }
            List<Byte> compressedExplossions = RunLengthEncoder.encode(uncompressedExplossions);
            
            ret.add((byte)compressedExplossions.size());
            ret.addAll(compressedExplossions);
        }
        
        // Players
        {
            List<Byte> serializedPlayers = new ArrayList<>();
            for (Player p : gameState.players()){
                serializedPlayers.addAll(Arrays.asList(
                        (byte)p.lives(),
                        (byte)p.position().x(), (byte)p.position().y(),
                        (byte)PlayerPainter.byteForPlayer(p, gameState.ticks())
                        ));
            }
            ret.addAll(serializedPlayers);
        }
        
        // Remaining ticks
        byte encodedRemainingTime = (byte) (Math.ceil(gameState.remainingTime())/2);
        ret.add(encodedRemainingTime);
        
        return ret;
    }
}
