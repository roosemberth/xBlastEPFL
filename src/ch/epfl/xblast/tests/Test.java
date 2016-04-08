package ch.epfl.xblast.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class Test {
    static boolean autoSimulate = true;
    private static int frequency = 5;

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0){

            try {
                final int newFreq = Integer.parseInt(args[0]);
                frequency = newFreq;
            } catch (NumberFormatException e) { }
        }

        RandomEventGenerator rand = new RandomEventGenerator(2016, 30,100);
        
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;

        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        List<Player> lP = new ArrayList<Player>();
        
        //Initialize players
        lP.add(new Player(PlayerID.values()[0], 3,new Cell(1,1),2,3));
        lP.add(new Player(PlayerID.values()[1], 3,new Cell(13,1),2,3));
        lP.add(new Player(PlayerID.values()[2], 3,new Cell(13,11),2,3));
        lP.add(new Player(PlayerID.values()[3], 3,new Cell(1,11),2,3));
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Callable<String> stdInReader = new Callable<String>(){
            @Override public String call() throws Exception {
                return new String("" + (char)System.in.read());
            }
        };
        
        Thread inputListener = new Thread(new Runnable(){
            @Override public void run() {
                boolean updateReader = false;
                Future<String> stdInLine = executor.submit(stdInReader);
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        if (updateReader){
                            stdInLine = executor.submit(stdInReader);
                            updateReader = !updateReader;
                        }
                        stdInLine.get(100, TimeUnit.MILLISECONDS);
                        autoSimulate = !autoSimulate;
                        updateReader = true;
                    } catch (TimeoutException te) {
                    } catch (InterruptedException ie) { // exit...
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                executor.shutdownNow();
            }
        });
        
        Thread gameConsole = new Thread(new Runnable(){
            @Override public void run() {
                GameState gs = new GameState(board, lP);
                while(!gs.isGameOver()){
                    try {
                        Thread.sleep(1000/frequency);
                    } catch (InterruptedException e) {
                        System.out.println("Received interruption, exiting...");
                        break;
                    }
                    if (!autoSimulate)
                        continue;
                    GameStatePrinter.printGameState(gs);
                    gs = gs.next(rand.randomSpeedChangeEvents(),rand.randomBombDropEvents());
                }
                GameStatePrinter.printGameState(gs);
            }
        });
        
        inputListener.start();
        gameConsole.start();
        gameConsole.join();
        inputListener.interrupt();
    }
}
