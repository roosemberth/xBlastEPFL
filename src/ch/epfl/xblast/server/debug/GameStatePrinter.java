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

    private static String cyanWhite = "\u001b[1;37;46m";
    private static String redkWhite = "\u001b[1;37;41m";
    private static String blueWhite = "\u001b[37;44m";
    private static String tealBlack = "\u001b[0;37;44m";
    private static String redWhite = "\u001b[0;37;41m";
    private static String greenBlack = "\u001b[0;32;40m";
    private static String yellow = "\u001b[1;;43m";
    private static String blackWhite = "\u001b[1;37;40m";
    private static String std = "\u001b[m";
    private static String clear = "\u001b[2J\u001b[1;1H";
    
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();
        Map<Cell,Bomb> bombs = s.bombedCells();
        Set<Cell> blastedCells = s.blastedCells();
        
        System.out.println(clear);
        
        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                //check if c has a bomb, if so draw different color
                for (Player p: ps) {
                    if (p.position().containingCell().equals(c)) {
                        String color = tealBlack;
                        switch (p.lifeState().state()){
                            case DEAD:         color=blackWhite; break;
                            case DYING:        color=redWhite;   break;
                            case INVULNERABLE: color=cyanWhite;  break;
                            case VULNERABLE:   color=tealBlack;  break;
                            default: throw new InternalError("Unexpected Player Status Value");
                        }
                        System.out.print(color +stringForPlayer(p)+ std);
                        continue xLoop;
                    }
                }
                for(Map.Entry<Cell, Bomb> b : bombs.entrySet()){
                    if (b.getKey().equals(c)) {
                        System.out.print( redWhite + "öö" + std);
                        continue xLoop;
                    }
                }
                for(Cell bC : blastedCells){
                    if (bC.equals(c) && !board.blockAt(c).isFree()) {
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
       
        System.out.println("Remaining ticks : " + String.format("%3.4f", s.remainingTime()));
        for(Player p : ps)
            System.out.println(""
                    + p.id() + " (" + p.lives() + "," + p.lifeState().state() + "): " 
                    + p.position() + " : " + p.position().containingCell()
            );
        
        if(bombs.size() == 2)
            System.out.println(bombs);
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
        case FREE: return yellow + "  " + std;
        case INDESTRUCTIBLE_WALL: return blackWhite + "##" + std;
        case DESTRUCTIBLE_WALL: return blackWhite + "??" + std;
        case CRUMBLING_WALL: return greenBlack + "¿¿" + std;
        case BONUS_BOMB: return "+b";
        case BONUS_RANGE: return "+r";
        default: throw new Error();
        }
    }
}
