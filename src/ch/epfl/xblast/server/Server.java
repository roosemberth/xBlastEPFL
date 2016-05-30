package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class Server {
    private  GameState gameState;
    private  int maxClients;
    private  DatagramChannel channel;
    private  Map<SocketAddress,PlayerID> clients;

    
    public Server(int maxClients, int port) throws IOException{
        if(maxClients > PlayerID.values().length) throw new IllegalArgumentException("Unvalid number of players");
        clients = new HashMap<>();
        this.maxClients = maxClients;
        gameState = Level.DEFAULT_LEVEL.getGs();
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(port));
    }
    
    public void run() throws IOException{
        System.out.println("Starting server");
        waitClients();
        
        channel.configureBlocking(false);
        startGame();
        close();
    }
    
    private void waitClients() throws IOException{
        while(clients.size() < maxClients){
            ByteBuffer buffer = ByteBuffer.allocate(1);
            SocketAddress senderAdress = null;
            senderAdress = channel.receive(buffer);
            buffer.flip();
            if(buffer.get() == PlayerAction.JOIN_GAME.ordinal() && !clients.containsKey(senderAdress)){
                System.out.println("New client at adress " + senderAdress.toString());
                clients.put(senderAdress, PlayerID.values()[clients.size()]);
            }
        }
    }
    
    private void startGame() throws IOException{
        System.out.println("Starting game...");
        long oldTime = System.nanoTime();
        
        while(!gameState.isGameOver()) { 
            oldTime = System.nanoTime();
            
            sendGamestate();
            
            List<PlayerEvent> events = checkEvents();
            
            gameState = gameState.next(PlayerEvent.getSpeedChangeEvents(events), PlayerEvent.getBombDropEvents(events));
            long newTime = System.nanoTime();
            
            try {
                Thread.sleep((long) Math.max(((Time.MS_PER_S/Ticks.TICKS_PER_SECOND)-(newTime-oldTime)/1000000.0),0));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //Send last gamestate
        sendGamestate();
        System.out.println("Winner is " + (gameState.winner().orElse(null)!=null ? gameState.winner().get() : "nobody") + "!");
    }
    
    
    private void sendGamestate(){
        List<Byte> gamestatePacket = GameStateSerializer.serialize(Level.DEFAULT_LEVEL.getBp(), gameState);
        //Send gamestate to clients
        ByteBuffer buffer = ByteBuffer.allocate(gamestatePacket.size()+1);
        buffer.position(1);
        for(Byte b : gamestatePacket)
            buffer.put(b);
        for(Map.Entry<SocketAddress, PlayerID> entry : clients.entrySet()){
            buffer.put(0, (byte)entry.getValue().ordinal());
            buffer.rewind();
            try {
                channel.send(buffer, entry.getKey());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void close() throws IOException{
        channel.close();
    }
    
    private List<PlayerEvent> checkEvents() throws IOException{
        List<PlayerEvent> events = new ArrayList<>();
        SocketAddress sender = null;
        ByteBuffer buffer = ByteBuffer.allocate(1);
        while((sender=channel.receive(buffer)) != null){
            if(clients.containsKey(sender) && clients.get(sender) != null){
                events.add(new PlayerEvent(clients.get(sender), PlayerAction.values()[(int)buffer.get(0)]));
                buffer.flip();
            }
        }
        return events;
    }

    private static class PlayerEvent{
        //Used to handle the <Optional<Direction>>
        private static Map<PlayerID, Optional<Direction>> getSpeedChangeEvents(List<PlayerEvent> events) {
            Map<PlayerID, Optional<Direction>> speedEvents = new EnumMap<>(PlayerID.class);
            for (PlayerEvent event : events) {
                Optional<Direction> direction = null;
                switch(event.action){
                    case MOVE_N:
                        direction = Optional.of(Direction.N);
                        break;
                    case MOVE_E:
                        direction = Optional.of(Direction.E);
                        break;
                    case MOVE_S:
                        direction = Optional.of(Direction.S);
                        break;
                    case MOVE_W:
                        direction = Optional.of(Direction.W);
                        break;
                    case STOP:
                        direction = Optional.empty();
                        break;
                        
                }
                if(direction != null){
                    speedEvents.put(event.id,direction);
                }
            }
            return Collections.unmodifiableMap(speedEvents);
        }
        
        private static Set<PlayerID> getBombDropEvents(List<PlayerEvent> events){
            Set<PlayerID> bombDropEvents = new HashSet<>();
            for (PlayerEvent event : events) {
                if(event.action == PlayerAction.DROP_BOMB)
                    bombDropEvents.add(event.id);
            }
            return bombDropEvents;
        }
        
        private final PlayerID id;
        private final PlayerAction action;
        public PlayerEvent(PlayerID id, PlayerAction action) {
            this.id = id; this.action = action;
        }
        
                
    }
}
