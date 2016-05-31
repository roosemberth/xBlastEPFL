/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * Client
 *
 * This class is responsible for the client behavior.
 * Draws the GUI and keeps track of the game state and network connection.
 *
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class Client {
    //COLS*ROWS*2 + NUM_INFOS*NUM_PLAYER + NUM_SIZE_INFO + TIMER(1)
    private static int MAX_CAPACITY = Cell.COLUMNS*Cell.ROWS*2 + 4*PlayerID.values().length + 2 + 1+1;

    private SocketAddress serverAdress;
    private JFrame frame;
    private XBlastComponent xbComponent;
    private String title;
    private PlayerID id;
    private DatagramChannel channel;

    /**
     * Client Constructor
     *
     * @param hostName    Server Hostname (or IP).
     * @param hostPort    Server Port.
     *
     * @throws InvocationTargetException if createUI method fails to be executed.
     * @throws InterruptedException         if the invokeAndWait thread is interrupted.
     * @throws IOException                 if there's a problem with the network transport.
     */
    public Client(String hostName, int hostPort)  throws InvocationTargetException, InterruptedException, IOException{
        serverAdress = new InetSocketAddress(hostName, hostPort);
        xbComponent = new XBlastComponent();

        //Create channel
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.configureBlocking(false);


        //launch the window
        SwingUtilities.invokeAndWait(() -> createUI());
    }

    /**
     * Runs the client
     * @throws InterruptedException         if a sleeping thread is interrupted.
     * @throws IOException                 if there's a problem with the network transport.
     */
    public void run() throws IOException, InterruptedException{
        GameState gamestate = null;
        while((gamestate = getGamestate()) == null){
            requestJoin();
            Thread.sleep(1000);
        }

        channel.configureBlocking(true);

        while(!shouldClose()){
            if(gamestate != null)
                xbComponent.setGameState(gamestate, id);
            gamestate = getGamestate(); 
        }

        close();
    }

    /**
     * Creates the GUI window.
     * Instanciates and prepares the XBlastComponent class.
     * Sets event listeners on the greated window for the player actions.
     */
    private void createUI(){

        //Set up the key event manager
        Map<Integer, PlayerAction> keyBindings = new HashMap<>();
        keyBindings.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        keyBindings.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        keyBindings.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        keyBindings.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        keyBindings.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        keyBindings.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);

        KeyboardEventHandler keyEventHandler = new KeyboardEventHandler(keyBindings, (p) -> {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put((byte)p.ordinal());
            buffer.flip();

            try {
                channel.send(buffer, serverAdress);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        xbComponent.addKeyListener(keyEventHandler);

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(xbComponent);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        xbComponent.requestFocus();
    }

    //NOTE: Currently there's no way to check if the game is over...
    /**
     * @return if the graphical component should close.
     */
    public boolean shouldClose(){
        return false;
    }

    /**
     * Closes the communication channel.
     */
    private void close() throws IOException{
        channel.close();
    }

    /**
     * Retreives the next gamestate from the server.
     * @throws IOException    if there's a problem with the network transport.
     *
     * @return the next gamestate from the server.
     */
    public GameState getGamestate() throws IOException{
        //Create buffer and try to read data
        ByteBuffer gamestateBuffer  = ByteBuffer.allocate(MAX_CAPACITY);
        SocketAddress sender = null;
        sender = channel.receive(gamestateBuffer);

        if(sender == null || !sender.equals(serverAdress))
            return null;

        List<Byte> gamestateData = new ArrayList<>();
        gamestateBuffer.rewind();
        //Fill the list
        Byte idB = gamestateBuffer.get();
        if(id == null)
            id = PlayerID.values()[idB];
        else if(idB != (byte)id.ordinal())
            return null;
        while(gamestateBuffer.hasRemaining()){
            gamestateData.add(gamestateBuffer.get());
        }
        GameState newGamestate = GameStateDeserializer.deserializeGameState(gamestateData);

        return newGamestate;
    }

    /**
     * Requests joint to the server
     * @throws IOException    if there's a problem with the network transport.
     */
    private void requestJoin() throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte)PlayerAction.JOIN_GAME.ordinal());
        buffer.flip();
        channel.send(buffer, serverAdress);
    }
}
