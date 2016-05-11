package ch.epfl.xblast.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

public final class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        String hostAdress = "localhost";
        int hostPort = 2016;
        int localPort = 2017;
        
        
        if(args.length > 0){
            hostAdress = args[0];
        }
        if(args.length > 1){
            try{
                hostPort = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                System.out.println("Unvalid host port");
                e.printStackTrace();
            }
        }
        if(args.length > 2){
            try{
                localPort = Integer.parseInt(args[2]);
            }
            catch(NumberFormatException e){
                System.out.println("Unvalid local port");
                e.printStackTrace();
            }
        }
        try {
            Client client = new Client(hostAdress,hostPort,localPort);
            client.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
