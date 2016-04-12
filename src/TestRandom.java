import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Ticks;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class TestRandom {

    public static void main(String[] args) {
       int ticks = 0;


       Set<PlayerID> bombDropEvent = new HashSet<>();
       RandomEventGenerator rand = new RandomEventGenerator(2016, 30,100);

       Map<PlayerID,Optional<Direction>> speedChangeEvent = new HashMap<>();

       for(int i = 0; i < 1200; i++){
           System.out.println("Current time: " + (double)(Ticks.TOTAL_TICKS-i)/Ticks.TICKS_PER_SECOND);
           speedChangeEvent = rand.randomSpeedChangeEvents();
           bombDropEvent = rand.randomBombDropEvents();

           System.out.println("EVENTS" + speedChangeEvent + " " + bombDropEvent);
       }
    }

}
