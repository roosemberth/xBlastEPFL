/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.server;

import java.io.IOException;

/**
 * Server Main
 *
 * This is the main class for the server.
 * Handles parameters and calls the server class.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
public final class Main {
    public static void main(String[] args) {
        int maxClients = 4;
        int port = 2016;

        if(args.length > 0){
            try{
                maxClients  = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e){
                throw new RuntimeException("Exception parsing the first argument: ", e);
            }

        }

        if(args.length > 1){
            try{
                port = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                throw new RuntimeException("Exception parsing the second argument: ", e);
            }

        }

        try {
            Server server = new Server(maxClients, port);
            server.run();
        } catch (IOException e) {
            throw new RuntimeException("Exception on the server runtime: ", e);
        }

    }

}
