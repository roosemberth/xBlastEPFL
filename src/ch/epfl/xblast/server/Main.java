package ch.epfl.xblast.server;

import java.io.IOException;

public final class Main {
    
    public static void main(String[] args) {
        
        int maxClients = 4;
        int port = 2016;
        
        if(args.length > 0){
            try{
                maxClients  = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
            }
            
        }
       
        if(args.length > 1){
            try{
                port = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
            }
            
        }
        
        try {
            Server server = new Server(maxClients, port);
            server.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
