package ch.epfl.xblast.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.*;

public class GameStateTest {
    private static Board gameBoard;
    private static List<Player> players;
    private final static int PlayerInitialLives = 1;
    private final static int PlayerInitialBombR = 3;
    private final static int PlayerInitialBombs = 3;

    public GameStateTest() {
        final Block XX = Block.INDESTRUCTIBLE_WALL;
        final Block __ = Block.FREE;
        final Block II = Block.DESTRUCTIBLE_WALL;
        final Block BB = Block.BONUS_BOMB;
        final Block RR = Block.BONUS_RANGE;

        final List<Block> qL1 = Arrays.asList(__, __, __, __, XX, XX, XX);
        final List<Block> qL2 = Arrays.asList(BB, XX, II, __, __, __, __);
        final List<Block> qL3 = Arrays.asList(__, II, __, RR, XX, XX, BB);
        final List<Block> qL4 = Arrays.asList(__, __, BB, II, __, __, __);
        final List<Block> qL5 = Arrays.asList(XX, __, XX, __, XX, __, XX);
        final List<Block> qL6 = Arrays.asList(XX, __, XX, __, __, __, XX);
        final List<List<Block>> testBoardQuadrant = Arrays.asList(qL1, qL2, qL3,
                qL4, qL5, qL6);
        gameBoard = Board.ofQuadrantNWBlocksWalled(testBoardQuadrant);

        Player p1 = new Player(PlayerID.PLAYER_1, PlayerInitialLives,
                new Cell(1               , 1            ), PlayerInitialBombs, PlayerInitialBombR);
        Player p2 = new Player(PlayerID.PLAYER_2, PlayerInitialLives,
                new Cell(1               , Cell.ROWS - 1), PlayerInitialBombs, PlayerInitialBombR);
        Player p3 = new Player(PlayerID.PLAYER_3, PlayerInitialLives,
                new Cell(Cell.COLUMNS - 1, 1            ), PlayerInitialBombs, PlayerInitialBombR);
        Player p4 = new Player(PlayerID.PLAYER_4, PlayerInitialLives,
                new Cell(Cell.COLUMNS - 1, Cell.ROWS - 1), PlayerInitialBombs, PlayerInitialBombR);
        players = Arrays.asList(p1, p2, p3, p4);
    }

    @Test
    public void initialGameStateIntegrity() {
        GameState initGame = new GameState(gameBoard, players);
        assertEquals(0, initGame.ticks());
        assertEquals(false, initGame.isGameOver());
        assertEquals(
                ((double) (Ticks.TOTAL_TICKS)) / Ticks.TICKS_PER_SECOND,
                initGame.remainingTime(),
                1 / Ticks.TICKS_PER_SECOND
        );
        assertEquals(Optional.empty(), initGame.winner());
        assertEquals(players, initGame.players());
        assertEquals(4, initGame.alivePlayers().size());
        assertEquals(0, initGame.bombedCells().size());
        assertEquals(0, initGame.blastedCells().size());
        for (Player player : players) {
            assertEquals(3, player.bombRange());
        }
    }

    @Test
    public void nextGameStateIntegrity() {
        GameState game = new GameState(gameBoard, players);
        Map<PlayerID, Optional<Direction>> noMovement = new HashMap<>();
        Set<PlayerID> noBombDrops = new HashSet<>();
        Map<PlayerID, Optional<Direction>> player1South = new HashMap<>();
        player1South.put(PlayerID.PLAYER_1, Optional.of(Direction.S));
        Set<PlayerID> player1DropsBomb = new HashSet<>();
        player1DropsBomb.add(PlayerID.PLAYER_1);
        GameState next = game.next(noMovement, noBombDrops);

        assertEquals(false, next.isGameOver());
        assertEquals(game.ticks() + 1, next.ticks());
        assertEquals(
                ((double) (Ticks.TOTAL_TICKS - 1)) / Ticks.TICKS_PER_SECOND,
                next.remainingTime(),
                1 / Ticks.TICKS_PER_SECOND
        );

        // Let player invulnerable period to expire
        for (int i = 0; i < Ticks.PLAYER_INVULNERABLE_TICKS + 1; ++i)
            next = next.next(noMovement, noBombDrops);
        next = next.next(player1South, player1DropsBomb);

        assertEquals(Optional.empty(), next.winner());
        assertEquals(players, next.players());
        assertEquals(4, next.alivePlayers().size());
        assertEquals(1, next.bombedCells().size());
        assertEquals(0, next.blastedCells().size());

        // Let the bomb explode and add one to kill player1
        for (int i = 0; i < Ticks.BOMB_FUSE_TICKS + 1; ++i)
            next = next.next(noMovement, noBombDrops);

        // original tick + set bomb + bomb fuse burn + kill player1
        assertEquals(false, next.isGameOver());
        assertEquals(Optional.empty(), next.winner());
        assertEquals(players, next.players());
        assertEquals(0, next.bombedCells().size());

        // let player1 die
        for (int i = 0; i < Ticks.PLAYER_DYING_TICKS + 1; ++i)
            next = next.next(noMovement, noBombDrops);

        /*
         * --> Player Mechanics not implemented... assertEquals(3,
         * next.alivePlayers().size()); assertEquals(false,
         * players.get(0).isAlive());
         * 
         * Map<PlayerID, Optional<Direction>> player34North = new HashMap<>();
         * player1South.put(PlayerID.PLAYER_3, Optional.of(Direction.N));
         * player1South.put(PlayerID.PLAYER_4, Optional.of(Direction.N));
         * Set<PlayerID> player34DropBomb = new HashSet<>();
         * player1DropsBomb.add(PlayerID.PLAYER_3);
         * player1DropsBomb.add(PlayerID.PLAYER_4);
         * 
         * GameState end = game.next(player34North, player34DropBomb);
         * assertEquals(2, next.bombedCells().size());
         * 
         * // Let the bomb explode and add one to kill players 3 and 4 for (int
         * i = 0; i<Ticks.BOMB_FUSE_TICKS + 1; ++i) end = end.next(noMovement,
         * noBombDrops);
         * 
         * assertEquals(players, next.players()); assertEquals(0,
         * next.bombedCells().size());
         * 
         * // let players 3 and 4 die for (int i = 0;
         * i<Ticks.PLAYER_DYING_TICKS; ++i) next = next.next(noMovement,
         * noBombDrops);
         * 
         * assertEquals(1, next.alivePlayers().size()); assertEquals(true,
         * next.isGameOver()); assertEquals(PlayerID.PLAYER_2,
         * next.winner().get());
         * 
         * //
         */
    }
}
