/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Main client Class
 *
 * This is the main class for the client. 
 * Handles parameters and calls the client class.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class Main {

    /**
     * Client main function
     */
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        String hostAdress = "localhost";
        int hostPort = 2016;

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

        try {
            Client client = new Client(hostAdress,hostPort);
            client.run();
        } catch (IOException e) {
            throw new RuntimeException("Exception while running the Client: ", e);
        } 
    }
}
