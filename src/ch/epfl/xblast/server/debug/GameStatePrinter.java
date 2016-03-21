package ch.epfl.xblast.server.debug;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;


public final class GameStatePrinter {

    private static String red = "\u001b[31m";
    private static String blinkBlue = "\u001b[5;37;44m";
    private static String std = "\u001b[m";
    private static String clear = "\u001b[2J";
    
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        System.out.println(clear);
        
        List<Player> ps = s.alivePlayers();
        Board board = s.board();
        Map<Cell,Bomb> bombs = s.bombedCells();
        Set<Cell> blastedCells = s.blastedCells();
        
        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                //check if c has a bomb, if so draw different color
                for (Player p: ps) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(blinkBlue +stringForPlayer(p)+ std);
                        continue xLoop;
                    }
                }
                for(Map.Entry<Cell, Bomb> b : bombs.entrySet()){
                    if (b.getKey().equals(c)) {
                        System.out.print( red + "öö" + std);
                        continue xLoop;
                    }
                }
                for(Cell bC : blastedCells){
                    if (bC.equals(c)) {
                        System.out.print("**");
                        continue xLoop;
                    }
                }
                Block b = board.blockAt(c);
                System.out.print(stringForBlock(b));
            }
            System.out.println();
        }
        Iterator it = bombs.entrySet().iterator();
        
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N: b.append('^'); break;
        case E: b.append('>'); break;
        case S: b.append('v'); break;
        case W: b.append('<'); break;
        }
        return b.toString();
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return "##";
        case DESTRUCTIBLE_WALL: return "??";
        case CRUMBLING_WALL: return "¿¿";
        case BONUS_BOMB: return "+b";
        case BONUS_RANGE: return "+r";
        default: throw new Error();
        }
    }
}
