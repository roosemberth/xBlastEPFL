/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


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

/**
 * Server
 *
 * This class is responsible for the server behavior.
 * Keeps the internal logic of the game development.
 * Tracks the state of the network connection.
 *
 * <Class Description>
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public class Server {
    private  GameState gameState;
    private  int maxClients;
    private  DatagramChannel channel;
    private  Map<SocketAddress,PlayerID> clients;

    /**
     * Constructor.
     * @param maxClients number of clients to wait for.
     * @param port       port where to listen for clients.
     */
    public Server(int maxClients, int port) throws IOException{
        if(maxClients > PlayerID.values().length) throw new IllegalArgumentException("Unvalid number of players");
        clients = new HashMap<>();
        this.maxClients = maxClients;
        gameState = Level.DEFAULT_LEVEL.getGs();
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(port));
    }

    /**
     * Runs the server.
     * @throws InterruptedException         if the waitClients thread is interrupted.
     * @throws IOException                 if there's a problem with the network transport.
     */
    public void run() throws IOException{
        System.out.println("Starting server");
        waitClients();

        channel.configureBlocking(false);
        startGame();
        close();
    }

    /**
     * Waits for clients to connect.
     * @throws InterruptedException         if the wait thread is interrupted.
     * @throws IOException                 if there's a problem with the network transport.
     */
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

    /**
     * Starts the game!
     * @throws InterruptedException         if the wait thread is interrupted.
     * @throws IOException                 if there's a problem with the network transport.
     */
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

    /**
     * Sends the game state to the clients.
     */
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

    /**
     * Closes the channel.
     */
    private void close() throws IOException{
        channel.close();
    }

    /**
     * Checks for player events.
     *
     * @return List of {@link PlayerEvent}s.
     */
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

    /**
     * PlayerEvent
     *
     * Describes actions performed by clients.
     * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
     * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
     */
    private static class PlayerEvent{
        //Used to handle the <Optional<Direction>>
        /**
         * @return A map between {@link PlayerID}s and {@link Optional} {@link Direction}s representing the actions taken by the clients.
         * @param List of {@link PlayerEvent}s.
         */
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
                default:
                        break;
                }
                if(direction != null){
                    speedEvents.put(event.id,direction);
                }
            }
            return Collections.unmodifiableMap(speedEvents);
        }

        /**
         * @return A set of {@link PlayerID}s representing the dropped bombs by the clients.
         * @param List of {@link PlayerEvent}s.
         */
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

        /**
         * Constructor
         * @param id     {@link PlayerID}
         * @param action {@link PlayerAction}
         */
        public PlayerEvent(PlayerID id, PlayerAction action) {
            this.id = id; this.action = action;
        }


    }
}
