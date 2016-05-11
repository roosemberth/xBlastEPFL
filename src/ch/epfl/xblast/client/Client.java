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

public final class Client {
    
    //COLS*ROWS*2 + NUM_INFOS*NUM_PLAYER + NUM_SIZE_INFO + TIMER(1)
    private static int MAX_CAPACITY = Cell.COLUMNS*Cell.ROWS*2 + 4*PlayerID.values().length + 2 + 1;
    
    private SocketAddress serverAdress;
    private JFrame frame;
    private XBlastComponent xbComponent;
    private String title;
    private PlayerID id;
    private DatagramChannel channel;
    
    public Client(String hostName, int hostPort, int localPort) throws InvocationTargetException, InterruptedException, IOException{
        serverAdress = new InetSocketAddress(hostName, hostPort);
        xbComponent = new XBlastComponent();
        
        //Create channel
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(localPort));
        channel.configureBlocking(false);
        
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
        
        //launch the window
        SwingUtilities.invokeAndWait(() -> createUI());
    }
    
    public void run() throws IOException{
        GameState gamestate = null;
        SocketAddress adress = null;
        ByteBuffer buffer = ByteBuffer.allocate(1);
        while((adress=channel.receive(buffer)) == null || !adress.equals(serverAdress)){
            requestJoin();
        }
        
        id = PlayerID.values()[buffer.get(0)];
        
        while(!shouldClose()){
            gamestate = getGamestate(); 
            if(gamestate != null)
                xbComponent.setGameState(gamestate, id);
        }
        
        close();
    }

    private void createUI(){
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(xbComponent);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        xbComponent.requestFocus();
    }

    public boolean shouldClose(){
        return false;
    }
    
    private void close() throws IOException{
        channel.close();
    }
    public GameState getGamestate() throws IOException{ 
        //Create buffer and try to read data
        ByteBuffer gamestateBuffer  = ByteBuffer.allocate(MAX_CAPACITY);
        SocketAddress sender = null;
        sender = channel.receive(gamestateBuffer);
        
        if(sender == null)
            return null;
        List<Byte> gamestateData = new ArrayList<>();
        gamestateBuffer.rewind();
        //Fill the list
        while(gamestateBuffer.hasRemaining()){
            gamestateData.add(gamestateBuffer.get());
        }
        GameState newGamestate = GameStateDeserializer.deserializeGameState(gamestateData);
        
        return newGamestate;
    }
    
    private void requestJoin() throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte)PlayerAction.JOIN_GAME.ordinal());
        channel.send(buffer, serverAdress);
    }
    
    
}
