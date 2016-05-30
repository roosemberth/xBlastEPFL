/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


package ch.epfl.xblast.etape6.generator;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.etape6.events.EventSequence;
import ch.epfl.xblast.etape6.events.PlayerEvent;

public class PlayersEventsGenerator extends EventsGenerator {

    public PlayersEventsGenerator(EventSequence[] playersEvents) {
        super(playersEvents);

    }

    public Map<PlayerID, Optional<Direction>> speedChangeEvents() {

        Map<PlayerID, Optional<Direction>> events = new EnumMap<>(PlayerID.class);
        for (PlayerID pId : getPlayersEvents().keySet()) {
            PlayerEvent playerEvent = (PlayerEvent) getCurrent().get(pId);
            if (playerEvent.hasChangedSpeed())
                events.put(pId, playerEvent.getDirection());
        }
        return Collections.unmodifiableMap(events);
    }

    public Set<PlayerID> bombDropEvents() {
        Set<PlayerID> events = EnumSet.noneOf(PlayerID.class);
        for (PlayerID pId : getPlayersEvents().keySet()) {
            PlayerEvent playerEvent = (PlayerEvent) getCurrent().get(pId);
            if (playerEvent.isDropBomb())

                events.add(pId);
        }
        return Collections.unmodifiableSet(events);
    }

}